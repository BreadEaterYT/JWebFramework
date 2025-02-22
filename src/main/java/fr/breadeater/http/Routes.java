package fr.breadeater.http;

import java.util.function.BiConsumer;

public class Routes {
    private String route;
    private Methods method;
    private BiConsumer<Request, Response> callback;

    protected String getRoute(){ return this.route; }
    protected Methods getMethod(){ return this.method; }
    protected BiConsumer<Request, Response> getCallback(){ return this.callback; }

    protected void setRoute(String route){ this.route = route; }
    protected void setMethod(Methods method){ this.method = method; }
    protected void setCallback(BiConsumer<Request, Response> callback){ this.callback = callback; }
}
