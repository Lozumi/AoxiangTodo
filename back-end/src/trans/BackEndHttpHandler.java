package trans;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sys.AoXiangToDoListSystem;
import sys.Messages;
import sys.SystemController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;

/**
 * Http请求处理器。
 *
 * @author 贾聪毅
 */
public class BackEndHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html;charset=utf-8"); //设置编码格式
        responseHeaders.set("Access-Control-Allow-Origin","*");
        responseHeaders.set("Access-Control-Allow-Credentials","true");
        responseHeaders.set("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
        ResponsePacket responsePacket = new ResponsePacket();
        int httpCode = HttpURLConnection.HTTP_OK;

        boolean validArgument = true;
        try {
            validateArguments(exchange);
        } catch (IllegalArgumentException exception) {
            validArgument = false;
            responsePacket.setStatus(ResponseStatus.Failure);
            responsePacket.setMessage(String.format("HTTP参数错误：%s", exception.getMessage()));
//            httpCode = HttpURLConnection.HTTP_BAD_METHOD;
        }
        //获取输入输出流
        InputStream requestBodyStream = exchange.getRequestBody();
        OutputStream responseBodyStream = exchange.getResponseBody();

        if (validArgument) {
            RequestPacket requestPacket = null;
            try {
                requestPacket = RequestPacket.fromJsonString(getRequestBody(exchange));
                var controller = AoXiangToDoListSystem.getInstance().getSystemController();
                responsePacket = controller.invokeRequestHandler(requestPacket, "default");
//                if (responsePacket.getStatus() == ResponseStatus.Failure)
//                    httpCode = HttpURLConnection.HTTP_SEE_OTHER;
            } catch (Exception e) {
                System.err.printf("HTTP内部错误：无法读取请求内容，%s\n", e.getMessage());
                responsePacket.setStatus(ResponseStatus.Failure);
                responsePacket.setMessage(Messages.ZH_CN.HTTP_IO_EXCEPTION);
                return;
            }

        }

        //写响应数据，若本段出现错误，输出错误到错误流并不再发送响应。
        try {
            exchange.sendResponseHeaders(httpCode, 0);//发送响应头
            responseBodyStream.write(responsePacket.toJsonBytes());
            responseBodyStream.flush();
            requestBodyStream.close();
            responseBodyStream.close();
        }catch (IOException exception){
            System.err.printf("HTTP内部错误：无法发送响应，%s\n",exception.getMessage());
        }

    }

    private static void validateArguments(HttpExchange exchange) throws IllegalArgumentException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST"))
            throw new IllegalArgumentException(Messages.ZH_CN.HTTP_WRONG_METHOD_TYPE);
    }

    private static String getRequestBody(HttpExchange exchange) throws IOException {
        String body = "";
        var lengthHeader = exchange.getRequestHeaders().getFirst("Content-Length");
        if (lengthHeader == null) {
            return body;
        }
        InputStream inputStream = exchange.getRequestBody();
        var inputBytes = inputStream.readNBytes(Integer.parseInt(lengthHeader));
        body = new String(inputBytes);
        return body;
    }
}
