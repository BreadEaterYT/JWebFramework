package fr.breadeater.http;

import java.io.*;
import java.net.Socket;
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

    protected boolean isSent(){
        return this.isSent;
    }

    /**
     * Builds Response instance containing all the logic to respond to the client
     * @param in The InputStream of the client
     * @param out The OutputStream of the client
     * @param client The Client Socket
     */
    public Response(BufferedReader in, PrintWriter out, Socket client){
        this.headers = new HashMap<>();
        this.client = client;
        this.outputStream = out;
        this.inputStream = in;
    }

    /**
     * Sends data to the client with the headers specified by setHeader function
     * @param data The data to send
     */
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
     * Sets the status of the response
     * @param status The status code
     */
    public void setStatus(int status){
        this.status = status;
    }

    /**
     * Sets the status of the response
     * @param status The status code
     * @param statusText The status text
     */
    public void setStatus(int status, String statusText){
        this.status = status;
        this.statusText = statusText;
    }

    /**
     * Sets header to be sent to the client when send function called
     * @param header The Header name
     * @param value The Header value
     */
    public void setHeader(String header, String value){
        if (header.equalsIgnoreCase("Content-Length")) return;

        if (value.contains("\r")) value = value.replace("\r", "");
        if (value.contains("\n")) value = value.replace("\n", "");

        this.headers.put(header, value);
    }

    /**
     * Removes a specific header
     * @param header The name of the Header to be removed
     */
    public void removeHeader(String header){
        if (header.equalsIgnoreCase("Content-Length")) return;

        this.headers.remove(header);
    }
}
