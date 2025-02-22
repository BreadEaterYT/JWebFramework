package fr.breadeater.http;

public enum Methods {
    GET("GET"),
    POST("POST"),
    ANY("ANY");

    public String method;

    Methods(String method){
        this.method = method;
    }
}
