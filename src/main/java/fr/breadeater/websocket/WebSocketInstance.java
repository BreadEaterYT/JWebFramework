package fr.breadeater.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Represents a connected WebSocket client instance.
 */
public class WebSocketInstance {
    protected Socket socket;
    protected Consumer<String> onMessageCallback = null;
    protected Runnable onCloseCallback = null;

    /**
     * Creates a WebSocket instance for a connected client.
     *
     * @param client The socket representing the client connection.
     */
    public WebSocketInstance(Socket client){
        this.socket = client;
    }

    /**
     * Sends a message to the WebSocket client.
     *
     * @param data The message to send.
     */
    public void send(String data){
        try {
            OutputStream out = this.socket.getOutputStream();

            byte[] rawData = data.getBytes();
            int frameSize = rawData.length + 2;

            byte[] frame = new byte[frameSize];

            frame[0] = (byte) 129;
            frame[1] = (byte) rawData.length;

            System.arraycopy(rawData, 0, frame, 2, rawData.length);

            out.write(frame);
            out.flush();
        } catch (IOException ignored){}
    }

    /**
     * Sets a callback to handle incoming messages.
     */
    public void onMessage(Consumer<String> callback){
        this.onMessageCallback = callback;
    }

    /**
     * Sets a callback to handle client disconnection.
     */
    public void onClose(Runnable callback){
        this.onCloseCallback = callback;
    }
}