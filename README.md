## JWebFramework

JWebFramework is a lightweight, dependency-free Java framework that provides both an HTTP server and a WebSocket server, built entirely with Java Sockets for Web Development.

### Installation

Simply copy the files containing the source code and put it in your project.

### Usage

Import HttpServer.java or WebSocketServer.java (depending on your needs) and do like this:

- Example of usage for the HTTP Server:

```java
import fr.breadeater.http.HttpServer;
import fr.breadeater.http.Methods;

public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        
        // Available methods: GET, POST, ANY (ANY means a route is accessible with every method)

        httpServer.use((req, res) -> {...}); // Your middleware code goes in the callback
        httpServer.add(Methods.GET, "/", (req, res) -> {...}); // Your code goes in the callback
    
        httpServer.listen(80); // Starts the webserver
    }
}
```

- Example of usage for the WebSocket Server:

```java
import fr.breadeater.websocket.WebSocketServer;

public class Main {
    public static void main(String[] args){
        WebSocketServer webSocketServer = new WebSocketServer();
        
        webSocketServer.onConnection((req, ws) -> {
            ws.onMessage((data) -> {...}); // Your code to handle the message goes in the callback
            ws.onClose(() -> {...}); // Your code to handle User disconnection goes in the callback [OPTIONAL]
        });
        
        webSocketServer.listen(8080); // Starts the websocket server
    }
}
```

### Contribution
If you want to contribute, you're welcome, just please provide documentation / comments of what you've done / modified in the code.

Modifying files like LICENSE or README does not count as contribution.

### License
This project is licensed under the MIT License (see [LICENSE](LICENSE) file for more infos)

You can use this framework for commercial needs only if you include the LICENSE file into your project or put the license into your LICENSE or README file, if It's for personal use, the license is optional.
