package fr.breadeater.http.cookie;

import java.util.Date;

public class CookieOptions {
    private String Domain = null;
    private Date Expires = null;
    private boolean HttpOnly = true;
    private int MaxAge = 0;
    private String Path = "/";
    private String SameSite = "Strict";
    private boolean Secure = false;

    public void setDomain(String domain){ this.Domain = domain; }
    public void setExpireDate(Date date){ this.Expires = date; }
    public void setHttpOnly(boolean httpOnly){ this.HttpOnly = httpOnly; }
    public void setMaxAge(int maxAgeMilliseconds){ this.MaxAge = maxAgeMilliseconds; }
    public void setPath(String path){ this.Path = path; }
    public void setSecure(boolean secure){ this.Secure = secure; }
    public void setSameSite(String sameSite){
        if (!sameSite.equals("None") && !sameSite.equals("Lax") && !sameSite.equals("Strict")) return;

        this.SameSite = sameSite;
    }

    public String getDomain(){ return this.Domain; }
    public Date getExpireDate(){ return this.Expires; }
    public boolean getHttpOnly(){ return this.HttpOnly; }
    public int getMaxAge(){ return this.MaxAge; }
    public String getPath(){ return this.Path; }
    public String getSameSite(){ return this.SameSite; }
    public boolean getSecure(){ return this.Secure; }
}
