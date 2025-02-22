package fr.breadeater.http.cookie;

import java.util.Date;

/**
 * Defines options for HTTP cookies in the response.
 */
public class CookieOptions {
    private String Domain = null;
    private Date Expires = null;
    private boolean HttpOnly = true;
    private int MaxAge = 0; // Max-Age in seconds
    private String Path = "/";
    private String SameSite = "Strict";
    private boolean Secure = false;

    /**
     * Sets the domain for the cookie.
     * This determines which domain(s) can access the cookie.
     *
     * @param domain The domain name (e.g., "example.com").
     */
    public void setDomain(String domain){ this.Domain = domain; }

    /**
     * Sets the expiration date of the cookie.
     * After this date, the cookie will be automatically deleted.
     *
     * @param date The expiration date.
     */
    public void setExpireDate(Date date){ this.Expires = date; }

    /**
     * Enables or disables the HttpOnly flag.
     * When true, the cookie is inaccessible to JavaScript (for security reasons).
     *
     * @param httpOnly If true, the cookie is HttpOnly.
     */
    public void setHttpOnly(boolean httpOnly){ this.HttpOnly = httpOnly; }

    /**
     * Sets the Max-Age of the cookie in **seconds**.
     * Determines how long the cookie remains valid before expiring.
     *
     * @param maxAgeSeconds The max age in **seconds** (not milliseconds).
     */
    public void setMaxAge(int maxAgeSeconds){ this.MaxAge = maxAgeSeconds; }

    /**
     * Defines the URL path where the cookie is accessible.
     *
     * @param path The path (e.g., "/" for all pages or "/user" for specific routes).
     */
    public void setPath(String path){ this.Path = path; }

    /**
     * Enables or disables the Secure flag.
     * When true, the cookie is only sent over HTTPS connections.
     *
     * @param secure If true, the cookie is Secure.
     */
    public void setSecure(boolean secure){ this.Secure = secure; }

    /**
     * Sets the SameSite attribute, which controls cross-site cookie behavior.
     *
     * Options:
     * - "Strict" → Only sent for same-site requests (most secure).
     * - "Lax" → Sent for top-level navigation (default for most browsers).
     * - "None" → Sent with all requests (must be Secure if set to None).
     *
     * @param sameSite The SameSite value ("Strict", "Lax", or "None").
     */
    public void setSameSite(String sameSite){
        if (!sameSite.equals("None") && !sameSite.equals("Lax") && !sameSite.equals("Strict")) return;
        this.SameSite = sameSite;
    }

    /** @return The domain for the cookie. */
    public String getDomain(){ return this.Domain; }

    /** @return The expiration date of the cookie. */
    public Date getExpireDate(){ return this.Expires; }

    /** @return True if the cookie is HttpOnly, false otherwise. */
    public boolean getHttpOnly(){ return this.HttpOnly; }

    /** @return The Max-Age of the cookie in **seconds**. */
    public int getMaxAge(){ return this.MaxAge; }

    /** @return The path where the cookie is accessible. */
    public String getPath(){ return this.Path; }

    /** @return The SameSite attribute value ("Strict", "Lax", or "None"). */
    public String getSameSite(){ return this.SameSite; }

    /** @return True if the cookie is Secure, false otherwise. */
    public boolean getSecure(){ return this.Secure; }
}