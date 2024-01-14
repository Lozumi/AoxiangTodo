package unittest;

import shared.ToDoWorkItem;
import shared.UserInfo;
import trans.RequestPacket;
import trans.RequestType;
import trans.ResponsePacket;
import trans.ResponseStatus;
import user.User;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 用于测试HTTP请求
 */
public class HttpTest {
    String url;

    public HttpTest(String url) {
        this.url = url;
    }

    public ResponsePacket tryRequest(RequestPacket request) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:20220"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(request.toJsonString()))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return ResponsePacket.fromJsonString(response.body());
        } catch (Exception exception) {
            return null;
        }
    }


    public ResponsePacket tryRequestCreateToDoWork(ToDoWorkItem item) throws IOException {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.CreateToDoWork);
        requestPacket.setContent(item.toJsonString());
        var response = tryRequest(requestPacket);
        printTrace(requestPacket,response);
        return response;
    }
    public ResponsePacket tryRequestEnumeration() {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.EnumerateToDoWorkList);
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    public ResponsePacket tryRequestStartPomodoro(int innerId) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.StartPomodoro);
        requestPacket.setContent(String.valueOf(innerId));
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    public ResponsePacket tryRequestEndPomodoro() {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.EndPomodoro);
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    public ResponsePacket tryRequestQueryToDoWorkItem(int innerId) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.QueryToDoWork);
        requestPacket.setContent(String.valueOf(innerId));
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    public ResponsePacket tryRequestSynchronize() {
        return tryBasicRequest(RequestType.Synchronize);
    }

    public ResponsePacket tryRequestGetUser(){
        return tryBasicRequest(RequestType.GetCurrentUser,null);
    }

    public  ResponsePacket tryRequestUserRegister(UserInfo info) throws IOException {
        return tryBasicRequest(RequestType.UserRegister,info.toJsonString());
    }
    public ResponsePacket tryRequestUserLogin(UserInfo info) throws IOException {
        return tryBasicRequest(RequestType.UserLogin,info.toJsonString());
    }
    ResponsePacket tryBasicRequest(RequestType requestType, String content) {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(requestType);
        requestPacket.setContent(content);
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    public ResponsePacket tryRequestLogout(){
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.UserLogout);
        var response = tryRequest(packet);
        printTrace(packet,response);
        return tryRequest(packet);
    }
    ResponsePacket tryBasicRequest(RequestType requestType) {
        return tryBasicRequest(requestType,"");
    }

    public ResponsePacket tryRequestExit() {
        RequestPacket requestPacket = new RequestPacket();
        requestPacket.setRequestType(RequestType.ExitApplication);
        var response = this.tryRequest(requestPacket);
        printTrace(requestPacket, response);
        return response;
    }

    private void printTrace(RequestPacket request, ResponsePacket response) {
        if (response == null) {
            System.err.printf("[测试自动打印] request.requestType = %s, ***HTTP服务器没有返回响应***", request.getRequestType());
            return;
        }
        PrintStream targetStream = response.getStatus().equals(ResponseStatus.Success) ? System.out : System.err;
        targetStream.printf("[测试自动打印] request.requestType = %s, response.message = \"%s\", response.content = %s\n", request.getRequestType(), response.getMessage(), response.getContent());
    }
}
