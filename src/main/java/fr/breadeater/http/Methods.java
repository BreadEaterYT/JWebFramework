package fr.breadeater.http;

/**
 * The Methods for the HTTP Server
 */
public enum Methods {
    GET("GET"),
    POST("POST"),
    ANY("ANY");

    public String method;

    Methods(String method){
        this.method = method;
    }
}
