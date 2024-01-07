package unittest;

import trans.RequestPacket;
import trans.ResponsePacket;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTest {
    String url;
    public HttpTest(String url)
    {
        this.url = url;
    }

    public ResponsePacket tryRequest(RequestPacket request)
    {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:20220"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(request.toJsonString()))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return ResponsePacket.fromJsonString(response.body());
        }catch (Exception exception) {
            return null;
        }

    }
}
