package fr.breadeater.http;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The HTTP Server Request Handling Class
 */
public class Request {
    /**
     * The User Agent used by the client
     */
    public String UserAgent;

    /**
     * The URL that the client uses to connect
     */
    public String Host;

    /**
     * The HTTP Version used by the client (example: HTTP/1.0, HTTP/1.1, etc...)
     */
    public String HttpVersion;

    /**
     * The Path of the request (example: /)
     */
    public String Path;

    /**
     * The Request Method
     */
    public String Method;

    /**
     * Defines MIME types supported by the client
     */
    public String Accept;

    /**
     * The languages that the client prefers
     */
    public String AcceptLanguage;

    /**
     * Defines content encoding
     */
    public String AcceptEncoding;

    /**
     * Defines if the connection with the client should be closed after response or not
     */
    public String Connection;

    /**
     * The Cookies sent by the client
     */
    public List<HttpCookie> Cookie;

    /**
     * Defines if insecure requests should be upgraded
     */
    public String UpgradeInsecureRequests;

    /**
     * The Content Type of the request
     */
    public String ContentType;

    /**
     * The URL Encoded POST variables, filled only if request header is x-www-form-urlencoded
     */
    public Map<String, String> POSTData = new HashMap<>();

    /**
     * The URL Encoded GET variables, filled only if the path contains GET variables
     */
    public Map<String, String> GETData = new HashMap<>();

    /**
     * The body of the request
     */
    public String Body;

    /**
     * The IP of the client;
     */
    public String RemoteAddress;
}