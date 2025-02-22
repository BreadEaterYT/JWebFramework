package fr.breadeater.http.cookie;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private Map<String, Object> cookies = new HashMap<>();

    private CookieOptions options;

    public Cookie(CookieOptions options){
        this.options = options;
    }

    public void setCookie(String name, Object data){
        this.cookies.put(name, data);
    }

    public Map<String, Object> getCookies(){
        return this.cookies;
    }

    public String toCookie(String name){
        if (!this.cookies.containsKey(name)) return null;

        String cookie = "Set-Cookie: " + name + "=" + this.cookies.get(name).toString();

        if (this.options.getDomain() != null) cookie += "; Domain=" + this.options.getDomain();
        if (this.options.getExpireDate() != null) cookie += "; Expires=" + this.options.getExpireDate().toString();
        if (this.options.getHttpOnly() == true) cookie += "; HttpOnly";
        if (this.options.getMaxAge() != 0) cookie += "; Max-Age=" + this.options.getMaxAge();
        if (this.options.getPath() != null) cookie += "; Path=" + this.options.getPath();
        if (this.options.getSecure() == true) cookie += "; Secure";
        if (this.options.getSameSite() != null) cookie += "; SameSite=" + this.options.getSameSite();

        return cookie;
    }
}
