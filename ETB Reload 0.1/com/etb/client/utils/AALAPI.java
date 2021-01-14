package com.etb.client.utils;

public class AALAPI {
    private static String username;

    public static String getUsername() {
        if (username == null) {
            username = getUsernameUncached();
        }
        return username;
    }

    private static String getUsernameUncached() {
        Class api;
        try {
            api = Class.forName("net.aal.API");
        } catch (ClassNotFoundException ignored) {
            api = null;
        }
        if (api == null) { // we are not running AAL
            return "debug-mode"; // TODO: maybe add killswitch here
        } else {
            try {
                return (String) api.getMethod("getUsername").invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
                return "error-getting-username";
            }
        }
    }
}
