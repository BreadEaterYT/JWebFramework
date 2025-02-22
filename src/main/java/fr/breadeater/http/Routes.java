package fr.breadeater.http;

import java.util.function.BiConsumer;

public class Routes {
    private String route;
    private Methods method;
    private BiConsumer<Request, Response> callback;

    public String getRoute(){ return this.route; }
    public Methods getMethod(){ return this.method; }
    public BiConsumer<Request, Response> getCallback(){ return this.callback; }

    public void setRoute(String route){ this.route = route; }
    public void setMethod(Methods method){ this.method = method; }
    public void setCallback(BiConsumer<Request, Response> callback){ this.callback = callback; }
}
