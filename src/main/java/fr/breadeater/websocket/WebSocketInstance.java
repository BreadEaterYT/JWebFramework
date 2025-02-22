package fr.breadeater.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class WebSocketInstance {
    protected Socket socket;
    protected Consumer<String> onMessageCallback = null;
    protected Runnable onCloseCallback = null;

    public WebSocketInstance(Socket client){
        this.socket = client;
    }

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

    public void onMessage(Consumer<String> callback){
        this.onMessageCallback = callback;
    }

    public void onClose(Runnable callback){
        this.onCloseCallback = callback;
    }
}
