package trans;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sys.AoXiangToDoListSystem;
import sys.SystemController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class BackEndHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html;charset=utf-8"); //设置编码格式

        String uriPath = exchange.getRequestURI().getPath();
        InputStream requestBodyStream = exchange.getRequestBody();
        OutputStream responseBodyStream = exchange.getResponseBody();

        RequestPacket requestPacket = encodeRequestPacket(uriPath,getRequestContent(exchange));

        ResponsePacket responsePacket = AoXiangToDoListSystem.getInstance().getSystemController().invokeRequestHandler(requestPacket, "default");

        //写响应数据。
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);//发送响应头
        responseBodyStream.write(responsePacket.toJsonBytes());

        requestBodyStream.close();
        responseBodyStream.close();
    }

    private static RequestPacket encodeRequestPacket(String uriPath, String requestContent) {
        List<String> paths = Arrays.stream(uriPath.split("/")).filter(str -> !str.isBlank()).toList();
        String primaryPath = paths.get(0);
        String secondaryPath = paths.size() >= 2 ? paths.get(1) : null;

        RequestPacket requestPacket = new RequestPacket();
        if (primaryPath.equals("todoworkitem")) {
            if (secondaryPath != null) {
                requestPacket.setRequestType(RequestType.QueryToDoWork);
                requestPacket.setContent(secondaryPath);
            } else {
                requestPacket.setRequestType(RequestType.EnumerateToDoWorkList);
                requestPacket.setContent(null);
            }
        }
        return requestPacket;
    }

    private static String getRequestContent(HttpExchange exchange) {
        var headers = exchange.getRequestHeaders();
        var lengthString = headers.getFirst("Content-Length");
        if(lengthString == null)
            return null; //请求不包含正文
        int length = Integer.parseInt(lengthString);
        var inputStream = exchange.getRequestBody();
        try {
            return new String(inputStream.readNBytes(length));
        }catch (IOException exception)
        {
            System.err.printf("后端Http服务器处理请求内容时发生IO错误：%s\n",exception.getMessage());
            return null;
        }
    }

    private static List<String> splitUri(URI uri) {
        return splitUri(uri.getPath());
    }
    private static List<String> splitUri(String uri) {
        return Arrays.stream(uri.split("/")).filter(str -> !str.isBlank()).toList();
    }
}
