import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import java.util.Hashtable;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * ChangeServlet, handle change requests
 *
 * @author ZSC
 */
public class ChangeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String success = "Success";

    private static final String failure = "Failure";

    private static final int expectedParamCount = 4;

    private Hashtable<String, String> parameters = new Hashtable<>() {{
        put("token", "");
        put("new-user-name", "");
        put("old-password", "");
        put("new-password", "");
    }};

    private DBSingletonInstance dbSingletonInstance = DBSingletonInstance.getDbSingletonInstance();

    public ChangeServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("ChangeServlet::doGet");
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
                out.print(failure);
                out.flush();
                return;
            }
        }
        if (paramCount != expectedParamCount) {
            System.out.println("Error: wrong parameter count");
            out.print(failure);
            out.flush();
            return;
        }

        Connection c = dbSingletonInstance.getConnectionToDB();
        try {
            PreparedStatement statement = c.prepareStatement("SELECT * FROM tokens WHERE token=?;");
            statement.setString(1, parameters.get("token"));
            ResultSet tokenResultSet = statement.executeQuery();
            if (!tokenResultSet.next()) {
                tokenResultSet.close();
                statement.close();
                System.out.println("Error: user not found or not logged in");
                out.print(failure);
                out.flush();
                return;
            }

            String account = tokenResultSet.getString("account");
            String token = tokenResultSet.getString("token");
            long expire = tokenResultSet.getLong("expire_time");
            if (System.currentTimeMillis() > expire) {
                tokenResultSet.close();
                statement.close();
                System.out.println("Error: token expired");
                out.print(failure);
                out.flush();
                return;
            }
            TokenPair newToken = new TokenPair(token, expire).renew();
            statement = c.prepareStatement("UPDATE tokens SET expire_time=? WHERE token=?;");
            statement.setLong(1, newToken.getExpire());
            statement.setString(2, newToken.getToken());
            statement.executeUpdate();
            c.commit();

            statement = c.prepareStatement("SELECT * FROM users WHERE account=?;");
            statement.setString(1, account);
            ResultSet userResultSet = statement.executeQuery();
            if (parameters.get("old-password").equals(userResultSet.getString("password"))) {
                statement = c.prepareStatement("UPDATE users SET user_name=? WHERE account=?;");
                statement.setString(1, parameters.get("new-user-name"));
                statement.setString(2, account);
                statement.executeUpdate();
                statement = c.prepareStatement("UPDATE users SET password=? WHERE account=?;");
                statement.setString(1, parameters.get("new-password"));
                statement.setString(2, account);
                statement.executeUpdate();
                c.commit();
                System.out.println("User info changed successfully");
                out.print(success);
                out.flush();
            } else {
                System.out.println("Error: wrong old-password, password not changed");
                out.print(failure);
                out.flush();
            }

            tokenResultSet.close();
            userResultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            out.print(failure);
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
