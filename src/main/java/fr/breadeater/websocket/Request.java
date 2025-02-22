package fr.breadeater.websocket;

/**
 * Represents an incoming WebSocket handshake request.
 * Stores various HTTP headers related to the WebSocket connection.
 */
public class Request {
    /** The host domain from the request (e.g., example.com). */
    public String Host;

    /** The requested path (e.g., /chat). */
    public String Path;

    /** The HTTP version used in the request (e.g., HTTP/1.1). */
    public String HttpVersion;

    /** The User-Agent header, typically identifies the client making the request. */
    public String UserAgent;

    /** The Accept header, specifies the media types accepted by the client. */
    public String Accept;

    /** The Accept-Language header, specifies the preferred language of the client. */
    public String AcceptLanguage;

    /** The Accept-Encoding header, specifies the content encoding (e.g., gzip, deflate). */
    public String AcceptEncoding;

    /** The WebSocket protocol version specified by the client. */
    public int SecWebSocketVersion;

    /** The Origin header, indicates the source of the request. */
    public String Origin;

    /** The WebSocket extensions requested by the client. */
    public String SecWebSocketExtension;

    /** The Connection header, typically should be "Upgrade" for WebSockets. */
    public String Connection;

    /** The Upgrade header, indicates the request is for upgrading to a WebSocket connection. */
    public String Upgrade;
}