package fr.breadeater.http;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    public String UserAgent;
    public String Host;
    public String HttpVersion;
    public String Path;
    public String Method;
    public String Accept;
    public String AcceptLanguage;
    public String AcceptEncoding;
    public String Connection;
    public List<HttpCookie> Cookie;
    public String UpgradeInsecureRequests;
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
    public String RemoteAddress;
    public int RemotePort;

    protected String RawQueryString;
}