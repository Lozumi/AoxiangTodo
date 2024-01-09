import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final int expectedParamCount = 2;

    private Hashtable<String, String> parameters = new Hashtable<>() {{
        put("account", "");
        put("password", "");
    }};

    private DBSingletonInstance dbSingletonInstance = DBSingletonInstance.getDbSingletonInstance();

    public LoginServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("LoginServlet::doGet");
        System.out.println("Received: " + request.getRequestURL());

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Enumeration<String> parameterNames = request.getParameterNames();
        int paramCount = 0;
        while (parameterNames.hasMoreElements()) {
            ++paramCount;
            String paramRec = parameterNames.nextElement();
            String valueRec = request.getParameter(paramRec);
            if (parameters.containsKey(paramRec)) {
                parameters.remove(paramRec);
                parameters.put(paramRec, valueRec);
                System.out.println("Parsed: %s - %s".formatted(paramRec, valueRec));
            } else {
                System.out.println("Error: wrong parameter name");
                out.print("");
                out.flush();
                return;
            }
        }
        if (paramCount != expectedParamCount) {
            System.out.println("Error: wrong parameter count");
            out.print("");
            out.flush();
            return;
        }

        Connection c = dbSingletonInstance.getConnectionToDB();
        try {
            Statement statement = c.createStatement();
            ResultSet userResultSet = statement.executeQuery(
                    "SELECT * FROM users WHERE account=\"%s\";".formatted(parameters.get("account")));
            if (!userResultSet.next()) {
                userResultSet.close();
                statement.close();
                System.out.println("Error: user not found");
                out.print("");
                out.flush();
                return;
            }

            String account = userResultSet.getString("account");
            String password = userResultSet.getString("password");
            if (account.equals(parameters.get("account")) && password.equals(parameters.get("password"))) {
                ResultSet tokenResultSet = statement.executeQuery(
                        "SELECT * FROM tokens WHERE account=\"%s\";".formatted(account));
                if (tokenResultSet.next()) {
                    String token = tokenResultSet.getString("token");
                    long expire = tokenResultSet.getLong("expire_time");
                    TokenPair newToken = new TokenPair(token, expire).renew();
                    statement.executeUpdate(String.format("UPDATE tokens SET expire_time=%d WHERE token=\"%s\";",
                            newToken.getExpire(), newToken.getToken()));
                    c.commit();
                    System.out.println("User logged in with renewed token");
                    out.print(newToken.getToken());
                    out.flush();
                } else {
                    TokenPair tokenPair = new TokenPair();
                    statement.executeUpdate("UPDATE users SET token=\"%s\" where account=\"%s\";".formatted(
                            tokenPair.getToken(), parameters.get("account")));
                    statement.executeUpdate(String.format("""
                    INSERT INTO tokens (token, account, expire_time)
                    VALUES ("%s", "%s", %d);
                    """, tokenPair.getToken(), parameters.get("account"), tokenPair.getExpire()));
                    c.commit();
                    System.out.println("User logged in with new token");
                    out.print(tokenPair.getToken());
                    out.flush();
                }
            } else {
                System.out.println("Login failed: wrong account/passwd");
                out.print("");
                out.flush();
            }

            userResultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            out.print("");
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
