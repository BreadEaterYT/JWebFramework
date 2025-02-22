package fr.breadeater.http;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private Map<String, String> headers;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private Socket client;
    private String statusText;
    private int status = 200;

    protected boolean isSent = false;

    public boolean isSent(){
        return this.isSent;
    }

    public Response(BufferedReader in, PrintWriter out, Socket client){
        this.headers = new HashMap<>();
        this.client = client;
        this.outputStream = out;
        this.inputStream = in;
    }

    public void send(String data){
        if (this.isSent) return;

        StringBuilder response = new StringBuilder();

        if (this.statusText == null) this.statusText = "";
        if (!this.statusText.startsWith(" ") && (!this.statusText.isEmpty() || this.statusText != null)) this.statusText = " " + statusText;
        if (!this.headers.containsKey("Content-Type")) this.headers.put("Content-Type", "text/html");

        response.append("HTTP/1.1 ").append(this.status).append(this.statusText).append("\r\n");

        this.headers.forEach((header, value) -> response.append(header).append(": ").append(value).append("\r\n"));

        response.append("Content-Length: ").append(data.getBytes().length).append("\r\n");
        response.append("\r\n");
        response.append(data);

        this.outputStream.write(response.toString());
        this.isSent = true;
    }

    /**
     * Same as send function but to send a file<br>
     * <br>
     * DEPRECATED: This function is broken, consider reading the file yourself and send the content via {@link #send(String)} function !
     */
    @Deprecated
    public void sendFile(File file) throws IOException {
        if (this.isSent) return;

        StringBuilder response = new StringBuilder();
        byte[] content = Files.readAllBytes(file.toPath());

        if (!this.statusText.startsWith(" ")) this.statusText = " " + statusText;

        response.append("HTTP/1.1 ").append(this.status).append(this.statusText).append("\r\n");

        this.headers.forEach((header, value) -> {
            if (header.equals("Content-Length") || header.equals("Content-Type")) response.append(header).append(": ").append(value).append("\r\n");
        });

        response.append("Content-Type: ").append(Files.probeContentType(Path.of(file.getAbsolutePath()))).append("\r\n");
        response.append("Content-Length: ").append(content.length).append("\r\n");
        response.append("\r\n");
        response.append(Arrays.toString(content));

        this.outputStream.write(response.toString());
        this.isSent = true;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setStatus(int status, String statusText){
        this.status = status;
        this.statusText = statusText;
    }

    public void setHeader(String header, String value){
        if (header.equalsIgnoreCase("Content-Length")) return;

        if (value.contains("\r")) value = value.replace("\r", "");
        if (value.contains("\n")) value = value.replace("\n", "");

        this.headers.put(header, value);
    }

    public void removeHeader(String header){
        if (header.equalsIgnoreCase("Content-Length")) return;

        this.headers.remove(header);
    }
}
