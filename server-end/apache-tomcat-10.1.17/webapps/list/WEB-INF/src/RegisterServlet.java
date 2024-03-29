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
 * RegisterServlet, handle register requests
 *
 * @author ZSC
 */
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String success = "Success";

    private static final String failure = "Failure";

    private static final int expectedParamCount = 3;

    private Hashtable<String, String> parameters = new Hashtable<>() {{
        put("user-name", "");
        put("account", "");
        put("password", "");
    }};

    private DBSingletonInstance dbSingletonInstance = DBSingletonInstance.getDbSingletonInstance();

    public RegisterServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("RegisterServlet::doGet");
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
            PreparedStatement statement = c.prepareStatement("SELECT * FROM users WHERE account=?;");
            statement.setString(1, parameters.get("account"));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                statement.close();
                System.out.println("Error: user already exists");
                out.print(failure);
                out.flush();
                return;
            }

            statement = c.prepareStatement("""
                    INSERT INTO users (account, user_name, password)
                    VALUES (?, ?, ?);
                    """);
            statement.setString(1, parameters.get("account"));
            statement.setString(2, parameters.get("user-name"));
            statement.setString(3, parameters.get("password"));
            statement.executeUpdate();
            c.commit();

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            out.print(failure);
            out.flush();
            return;
        }

        System.out.println("User registered successfully");
        out.print(success);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
