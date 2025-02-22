package fr.breadeater.websocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A simple WebSocket server that allows real-time bidirectional communication.
 */
public class WebSocketServer {
    private ServerSocket socket; // Server socket to listen for connections
    private BiConsumer<Request, WebSocketInstance> onConnectionCallback = null; // Callback for new connections

    protected Set<WebSocketInstance> clients = new HashSet<>(); // Active WebSocket clients

    /**
     * Starts the WebSocket server and listens for incoming connections.
     *
     * @param port The port number to listen on.
     * @throws IOException If an I/O error occurs.
     */
    public void listen(int port) throws IOException {
        this.socket = new ServerSocket(port);

        while (true){
            Socket client = this.socket.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }

    /**
     * Closes the WebSocket server.
     *
     * @throws IOException If an error occurs while closing the socket.
     */
    public void close() throws IOException {
        this.socket.close();
    }

    /**
     * Sets the callback function to execute when a new client connects.
     *
     * @param callback A function that takes a `Request` and a `WebSocketInstance`.
     */
    public void onConnection(BiConsumer<Request, WebSocketInstance> callback){
        this.onConnectionCallback = callback;
    }

    /**
     * Handles a new WebSocket connection from a client.
     *
     * @param client The socket representing the client connection.
     */
    private void handleClient(Socket client){
        WebSocketInstance webSocketInstance = null;

        try (
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ){
            Request requestInstance = new Request();
            webSocketInstance = new WebSocketInstance(client);

            String reqLine = reader.readLine();
            String webSocketKey = null;
            String line;

            if (reqLine == null || !reqLine.contains("GET")) return; // Ensure the request is a WebSocket handshake

            String[] request = reqLine.split(" ");

            requestInstance.Path = request[1];
            requestInstance.HttpVersion = request[2];

            // Read and parse the HTTP headers
            while (!(line = reader.readLine()).isEmpty()){
                String[] header = line.split(": ");

                switch (header[0]) {
                    case "Host" -> requestInstance.Host = header[1];
                    case "User-Agent" -> requestInstance.UserAgent = header[1];
                    case "Accept" -> requestInstance.Accept = header[1];
                    case "Accept-Language" -> requestInstance.AcceptLanguage = header[1];
                    case "Accept-Encoding" -> requestInstance.AcceptEncoding = header[1];
                    case "Sec-WebSocket-Version" -> requestInstance.SecWebSocketVersion = Integer.parseInt(header[1]);
                    case "Origin" -> requestInstance.Origin = header[1];
                    case "Sec-WebSocket-Extensions" -> requestInstance.SecWebSocketExtension = header[1];
                    case "Sec-WebSocket-Key" -> webSocketKey = header[1];
                    case "Connection" -> requestInstance.Connection = header[1];
                    case "Upgrade" -> requestInstance.Upgrade = header[1];
                }
            }

            if (webSocketKey == null){
                client.close();
                return;
            }

            // Generate WebSocket handshake response
            String acceptKey = genWebSocketKey(webSocketKey);
            String handshake = "HTTP/1.1 101 Switching Protocols\r\n" +
                    "Upgrade: websocket\r\n" +
                    "Connection: Upgrade\r\n" +
                    "Sec-WebSocket-Accept: " + acceptKey + "\r\n" +
                    "\r\n";

            out.write(handshake.getBytes());
            out.flush();

            this.clients.add(webSocketInstance);

            if (this.onConnectionCallback != null) this.onConnectionCallback.accept(requestInstance, webSocketInstance);

            // Keep the connection open and listen for messages
            while (!client.isClosed()){
                try {
                    String message = read(in);

                    if (message == null) break;

                    if (webSocketInstance.onMessageCallback != null && !message.equals("?"))
                        webSocketInstance.onMessageCallback.accept(message);
                } catch (IOException ignored){
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (webSocketInstance.onCloseCallback != null) webSocketInstance.onCloseCallback.run();

            this.clients.remove(webSocketInstance);

            try {
                client.close();
            } catch (IOException ignored){}
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param data The message to send.
     */
    public void broadcast(String data){
        for (WebSocketInstance client : this.clients){
            client.send(data);
        }
    }

    /**
     * Reads a message from a WebSocket client.
     *
     * @param in The input stream from the client.
     * @return The decoded message or null if an error occurs.
     * @throws IOException If an I/O error occurs.
     */
    private String read(InputStream in) throws IOException {
        int firstByte = in.read();

        if (firstByte == -1) return null;

        int payloadLength = in.read() & 127;

        if (payloadLength == 126){
            payloadLength = ((in.read() & 0xFF) << 8) | (in.read() & 0xFF);
        } else if (payloadLength == 127){
            for (int i = 0; i < 8; i++) in.read();
        }

        byte[] mask = new byte[4];
        byte[] encoded = new byte[payloadLength];
        byte[] decoded = new byte[payloadLength];

        in.read(mask);
        in.read(encoded);

        for (int i = 0; i < payloadLength; i++) decoded[i] = (byte) (encoded[i] ^ mask[i % 4]);

        return new String(decoded);
    }

    /**
     * Generates the WebSocket handshake response key.
     *
     * @param key The WebSocket key sent by the client.
     * @return The hashed key to send in the handshake response.
     * @throws IOException If an error occurs during hashing.
     */
    private String genWebSocketKey(String key) throws IOException {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            String secretString = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

            byte[] hash = sha1.digest(secretString.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception err){
            throw new IOException(err);
        }
    }
}