package fr.breadeater.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HttpServer {
    private ServerSocket socket;
    private List<BiConsumer<Request, Response>> middlewares;
    private List<Routes> routes;

    public HttpServer(){
        this.routes = new ArrayList<>();
        this.middlewares = new ArrayList<>();
    }

    public void add(Methods method, String route, BiConsumer<Request, Response> callback){
        Routes newRoute = new Routes();

        newRoute.setRoute(route);
        newRoute.setMethod(method);
        newRoute.setCallback(callback);

        this.routes.add(newRoute);
    }

    public void use(BiConsumer<Request, Response> middleware){
        this.middlewares.add(middleware);
    }

    public void listen(int port) throws IOException {
        this.socket = new ServerSocket(port);

        while (true){
            Socket client = socket.accept();

            new Thread(() -> handleClient(client)).start();
        }
    }

    public void close() throws IOException {
        if (this.socket != null && !this.socket.isClosed()) this.socket.close();
    }

    private void handleClient(Socket client){
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream())
        ){
            Request requestInstance = new Request();
            Response responseInstance = new Response(in, out, client);
            String line;
            int reqContentLength = 0;

            requestInstance.RemoteAddress = client.getRemoteSocketAddress().toString().replaceAll("/", "");

            while ((line = in.readLine()) != null){
                if (line.isEmpty()) break;

                String[] header = line.split(": ", 2);

                if (line.contains("HTTP/")){
                    String[] request = line.split(" ");

                    requestInstance.Method = request[0];
                    requestInstance.Path = request[1].replaceAll("/+$", "");
                    requestInstance.HttpVersion = request[2];

                    // Checks twice if path is empty, always check if its empty after the replaceAll with this regex, incase if path is root and the slash got deleted
                    if (requestInstance.Path.isEmpty()) requestInstance.Path += "/"; // Retries to set the path, usefull when the path is only a slash and replaceAll removes the slash

                    String[] pathParts = requestInstance.Path.split("\\?", 2);
                    requestInstance.Path = pathParts[0].replaceAll("/+$", "");

                    if (requestInstance.Path.isEmpty()) requestInstance.Path += "/";

                    if (pathParts.length > 1) {
                        String queryString = pathParts[1];
                        String[] queryPairs = queryString.split("&");

                        for (String pair : queryPairs) {
                            String[] keyValue = pair.split("=", 2);
                            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
                            requestInstance.GETData.put(key, value);
                        }

                        requestInstance.RawQueryString = pathParts[1];
                    }
                }

                if (header[0].contains("Host")) requestInstance.Host = header[1];
                if (header[0].contains("User-Agent")) requestInstance.UserAgent = header[1];
                if (header[0].contains("Accept")) requestInstance.Accept = header[1];
                if (header[0].contains("Accept-Language")) requestInstance.AcceptLanguage = header[1];
                if (header[0].contains("Accept-Encoding")) requestInstance.AcceptEncoding = header[1];
                if (header[0].contains("Connection")) requestInstance.Connection = header[1];
                if (header[0].contains("Cookie")) requestInstance.Cookie = HttpCookie.parse(header[1]);
                if (header[0].contains("Upgrade-Insecure-Requests")) requestInstance.UpgradeInsecureRequests = header[1];
                if (header[0].contains("Content-Type")) requestInstance.ContentType = header[1];

                if (header[0].contains("Content-Length")) reqContentLength = Integer.parseInt(header[1]);
            }

            char[] bodyChar = new char[reqContentLength];
            int bytesRead = in.read(bodyChar, 0, reqContentLength);
            String body = new String(bodyChar, 0, bytesRead);

            if (requestInstance.ContentType != null && requestInstance.ContentType.contains("application/x-www-form-urlencoded")){
                String[] postVars = body.split("&");

                for (String postVar : postVars){
                    String[] splittedPostVar = postVar.split("=", 2);
                    String key = URLDecoder.decode(splittedPostVar[0], StandardCharsets.UTF_8);
                    String value = splittedPostVar.length > 1 ? URLDecoder.decode(splittedPostVar[1], StandardCharsets.UTF_8) : "";

                    requestInstance.POSTData.put(key, value);
                }

                requestInstance.RawQueryString = body;
            } else requestInstance.Body = body;

            for (BiConsumer<Request, Response> middleware : this.middlewares){
                if (responseInstance.isSent()) return;

                middleware.accept(requestInstance, responseInstance);
            }

            for (Routes route : this.routes){
                if (responseInstance.isSent()) return;

                if ((route.getMethod().name().equals(requestInstance.Method) || route.getMethod() == Methods.ANY) && route.getRoute().equals(requestInstance.Path)){
                    route.getCallback().accept(requestInstance, responseInstance);
                    return;
                }
            }

            if (responseInstance.isSent()) return; // Returns if response is sent and incase if return in for loops just breaks out of loop and does not stop entirely

            responseInstance.setHeader("Content-Type", "text/plain");
            responseInstance.setStatus(404, "Not Found");
            responseInstance.send("404 Not Found");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
