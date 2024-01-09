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
 * PullServlet, handle pull requests
 *
 * @author ZSC
 */
public class PullServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String failure = "Failure";

    private static final int expectedParamCount = 1;

    private Hashtable<String, String> parameters = new Hashtable<>() {{
        put("token", "");
    }};

    private DBSingletonInstance dbSingletonInstance = DBSingletonInstance.getDbSingletonInstance();

    public PullServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("PullServlet::doGet");
        System.out.println("Received: " + request.getRequestURL());

        // setup response
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // parse params
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

        // connect & find token
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

            // verify token
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

            // renew token
            TokenPair newToken = new TokenPair(token, expire).renew();
            statement = c.prepareStatement("UPDATE tokens SET expire_time=? WHERE token=?;");
            statement.setLong(1, newToken.getExpire());
            statement.setString(2, newToken.getToken());
            statement.executeUpdate();
            c.commit();

            // get list and ret
            statement = c.prepareStatement("SELECT * FROM users WHERE account=?;");
            statement.setString(1, account);
            ResultSet listResultSet = statement.executeQuery();
            String todoList = listResultSet.getString("todo_list");
            if (todoList == null) {
                out.print("");
                out.flush();
            } else {
                out.print(todoList);
                out.flush();
            }
            System.out.println("Pulled todo list successfully");

            tokenResultSet.close();
            listResultSet.close();
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
