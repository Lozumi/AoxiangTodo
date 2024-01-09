package trans;

import util.Encrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CloudServer {
    public static final String serverIpAddress = "8.210.61.64:8080";
    static String successString = "Success";
    static String failureString = "Failure";

    public static boolean sendRegisterRequest(String userName, String account, String hashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/register-servlet?user-name=%s&account=%s&password=%s",
                serverIpAddress, userName, account, hashedPwd
        );
        return requestGET(urlStr).equals("Success");
    }

    public static String sendLoginRequest(String account, String hashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/login-servlet?account=%s&password=%s",
                serverIpAddress, account, hashedPwd
        );
        String tokenResponse = requestGET(urlStr);
        return tokenResponse;
    }

    public static boolean sendLogoutRequest(String userToken) throws IOException {
        String urlStr = String.format(
                "http://%s/list/logout-servlet?token=%s",
                serverIpAddress, userToken
        );
        return requestGET(urlStr).equals(successString);
    }

    public static boolean sendChangeUserInfoRequest(String userToken, String newUserName, String oldHashedPwd, String newHashedPwd) throws IOException {
        String urlStr = String.format(
                "http://%s/list/change-servlet?token=%s&new-user-name=%s&old-password=%s&new-password=%s",
                serverIpAddress, userToken, newUserName, oldHashedPwd, newHashedPwd
        );
        return requestGET(urlStr).equals(successString);
    }

    public static String sendPullRequest(String userToken) throws IOException {
        String urlStr = String.format(
                "http://%s/list/pull-servlet?token=%s",
                serverIpAddress, userToken
        );
        return requestGET(urlStr);
    }

    public static boolean sendPushRequest(String userToken, String systemDataJson) throws IOException {
        String urlStr = String.format(
                "http://%s/list/overwrite-servlet?token=%s", serverIpAddress, userToken
        );
        return requestPOST(urlStr, systemDataJson).equals(successString);
    }

    static String requestGET(String urlStr) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "text/plain")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException e) {
            System.err.printf("向%s发送GET请求时发生错误：%s",urlStr,e.getMessage());
            return "Failure";
        }
    }


    static String requestPOST(String urlStr, String content) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException e) {
            System.err.printf("向%s发送POST请求时发生错误：%s",urlStr,e.getMessage());
            return "Failure";
        }
    }
}
