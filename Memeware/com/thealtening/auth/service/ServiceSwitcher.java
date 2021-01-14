
package com.thealtening.auth.service;

import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.FieldAdapter;

import java.net.URL;

public final class ServiceSwitcher {
    private final String MINECRAFT_SESSION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
    private final String MINECRAFT_AUTHENTICATION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
    private final String[] WHITELISTED_DOMAINS = new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"};
    private final FieldAdapter minecraftSessionServer = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    private final FieldAdapter userAuthentication = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");

    public ServiceSwitcher() {
        try {
            this.minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", this.WHITELISTED_DOMAINS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlteningServiceType switchToService(AlteningServiceType service) {
        try {
            String authServer = service.getAuthServer();
            FieldAdapter userAuth = this.userAuthentication;
            userAuth.updateFieldIfPresent("BASE_URL", authServer);
            userAuth.updateFieldIfPresent("ROUTE_AUTHENTICATE", new URL(String.valueOf(authServer) + "authenticate"));
            userAuth.updateFieldIfPresent("ROUTE_INVALIDATE", new URL(String.valueOf(authServer) + "invalidate"));
            userAuth.updateFieldIfPresent("ROUTE_REFRESH", new URL(String.valueOf(authServer) + "refresh"));
            userAuth.updateFieldIfPresent("ROUTE_VALIDATE", new URL(String.valueOf(authServer) + "validate"));
            userAuth.updateFieldIfPresent("ROUTE_SIGNOUT", new URL(String.valueOf(authServer) + "signout"));
            String sessionServer = service.getSessionServer();
            FieldAdapter userSession = this.minecraftSessionServer;
            userSession.updateFieldIfPresent("BASE_URL", String.valueOf(sessionServer) + "session/minecraft/");
            userSession.updateFieldIfPresent("JOIN_URL", new URL(String.valueOf(sessionServer) + "session/minecraft/join"));
            userSession.updateFieldIfPresent("CHECK_URL", new URL(String.valueOf(sessionServer) + "session/minecraft/hasJoined"));
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return AlteningServiceType.MOJANG;
        }
        return service;
    }
}

