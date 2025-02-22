package fr.breadeater;

import fr.breadeater.http.HttpServer;
import fr.breadeater.http.Methods;
import fr.breadeater.websocket.WebSocketServer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        WebSocketServer webSocketServer = new WebSocketServer();
        HttpServer httpServer = new HttpServer();

        httpServer.add(Methods.GET, "/", (req, res) -> {
            File indexFile = new File("index.html");
            String content;

            if (!indexFile.exists()) {
                try {
                    indexFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                content = Files.readString(indexFile.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            res.setStatus(200, "OK");
            res.setHeader("Content-Type", "text/html");
            res.send(content);
        });

        webSocketServer.onConnection((req, ws) -> {
            System.out.println("User connected !");

            ws.onMessage((data) -> {
                System.out.println(data);

                ws.send(data);
            });

            ws.onClose(() -> {
                System.out.println("User disconnected !");
            });
        });

        new Thread(() -> {
            try {
                webSocketServer.listen(8080);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        httpServer.listen(80);
    }
}