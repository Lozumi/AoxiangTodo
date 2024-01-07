package trans;

import util.Encrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;

public class CloudServer {
    public static final String serverIpAddress = "127.0.0.1:8080";
    static String successString = "Success";
    static String failureString = "Failure";
    public static boolean sendRegisterRequest(String userName,String account, String hashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/register-servlet?user-name=%s&account=%s&password=%s",
                serverIpAddress,userName,account, hashedPwd
        );
        return requestGET(urlStr).equals("Success");
    }

    public static String sendLoginRequest(String account, String hashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/login-servlet?account=%s&password=%s",
                serverIpAddress,account,hashedPwd
        );
        String tokenResponse = requestGET(urlStr);
        return tokenResponse;
    }

    public static boolean sendLogoutRequest(String userToken) throws IOException {
        String urlStr = String.format(
          "http://%s/list/logout-servlet?token=%s",
                serverIpAddress,userToken
        );
        return requestGET(urlStr).equals(successString);
    }

    public static boolean sendChangeUserInfoRequest(String userToken,String newUserName,String oldHashedPwd,String newHashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/change-servlet?token=%s&new-user-name=%s&old-password=%s&new-password=%s",
                serverIpAddress,userToken,newUserName,oldHashedPwd,newHashedPwd
        );
        return requestGET(urlStr).equals(successString);
    }

    static String requestGET(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}