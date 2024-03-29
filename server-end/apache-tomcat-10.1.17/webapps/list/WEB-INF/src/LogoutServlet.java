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
 * LogoutServlet, handle logout requests
 *
 * @author ZSC
 */
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String success = "Success";

    private static final String failure = "Failure";

    private static final int expectedParamCount = 1;

    private Hashtable<String, String> parameters = new Hashtable<>() {{
        put("token", "");
    }};

    private DBSingletonInstance dbSingletonInstance = DBSingletonInstance.getDbSingletonInstance();

    public LogoutServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("LogoutServlet::doGet");
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
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                resultSet.close();
                statement.close();
                System.out.println("Error: user not found or not logged in");
                out.print(failure);
                out.flush();
                return;
            }

            String token = resultSet.getString("token");
            statement = c.prepareStatement("UPDATE users SET token=NULL where token=?;");
            statement.setString(1, token);
            statement.executeUpdate();
            statement = c.prepareStatement("DELETE FROM tokens WHERE token=?;");
            statement.setString(1, token);
            statement.executeUpdate();
            c.commit();
            System.out.println("User logged out");
            out.print(success);
            out.flush();

            resultSet.close();
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
