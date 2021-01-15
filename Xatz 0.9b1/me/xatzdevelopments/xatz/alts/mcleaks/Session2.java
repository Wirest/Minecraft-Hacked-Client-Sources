package me.xatzdevelopments.xatz.alts.mcleaks;
public class Session2 {

    private final String username;
    private final String token;

    public Session2(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
