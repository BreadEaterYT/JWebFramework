package fr.breadeater.http.cookie;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages HTTP cookies, allowing cookies to be set and formatted for HTTP responses.
 */
public class Cookie {
    private Map<String, Object> cookies = new HashMap<>(); // Stores all cookies in key-value format
    private CookieOptions options; // Stores options for cookie attributes (e.g., Secure, HttpOnly)

    /**
     * Creates a Cookie manager with specified cookie options.
     *
     * @param options The default options for cookies (e.g., domain, path, security settings).
     */
    public Cookie(CookieOptions options){
        this.options = options;
    }

    /**
     * Stores a cookie with a given name and value.
     *
     * @param name The name of the cookie.
     * @param data The value to store in the cookie.
     */
    public void setCookie(String name, Object data){
        this.cookies.put(name, data);
    }

    /**
     * Retrieves all stored cookies.
     *
     * @return A map containing all stored cookies.
     */
    public Map<String, Object> getCookies(){
        return this.cookies;
    }

    /**
     * Converts a stored cookie into a properly formatted "Set-Cookie" HTTP header string.
     *
     * @param name The name of the cookie to format.
     * @return The formatted "Set-Cookie" string, or null if the cookie does not exist.
     */
    public String toCookie(String name){
        if (!this.cookies.containsKey(name)) return null; // Return null if the cookie doesn't exist

        // Start building the cookie string
        String cookie = "Set-Cookie: " + name + "=" + this.cookies.get(name).toString();

        // Append optional attributes if set in CookieOptions
        if (this.options.getDomain() != null) cookie += "; Domain=" + this.options.getDomain();
        if (this.options.getExpireDate() != null) cookie += "; Expires=" + this.options.getExpireDate().toString();
        if (this.options.getHttpOnly()) cookie += "; HttpOnly";
        if (this.options.getMaxAge() > 0) cookie += "; Max-Age=" + this.options.getMaxAge();
        if (this.options.getPath() != null) cookie += "; Path=" + this.options.getPath();
        if (this.options.getSecure()) cookie += "; Secure";
        if (this.options.getSameSite() != null) cookie += "; SameSite=" + this.options.getSameSite();

        return cookie;
    }
}