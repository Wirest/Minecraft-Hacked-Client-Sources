package com.thealtening.auth.service;

import java.net.URL;

public final class ServiceSwitcher {
    private final String MINECRAFT_SESSION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
    private final String MINECRAFT_AUTHENTICATION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
    private final String[] WHITELISTED_DOMAINS = {".minecraft.net", ".mojang.com", ".thealtening.com"};
    private final FieldAdapter minecraftSessionServer = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    private final FieldAdapter userAuthentication = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");

    public ServiceSwitcher() {
        try {
            this.minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", this.WHITELISTED_DOMAINS);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public AlteningServiceType switchToService(AlteningServiceType paramAlteningServiceType) {
        try {
            String str1 = paramAlteningServiceType.getAuthServer();
            FieldAdapter localFieldAdapter1 = this.userAuthentication;
            localFieldAdapter1.updateFieldIfPresent("BASE_URL", str1);
            localFieldAdapter1.updateFieldIfPresent("ROUTE_AUTHENTICATE", new URL(str1 + "authenticate"));
            localFieldAdapter1.updateFieldIfPresent("ROUTE_INVALIDATE", new URL(str1 + "invalidate"));
            localFieldAdapter1.updateFieldIfPresent("ROUTE_REFRESH", new URL(str1 + "refresh"));
            localFieldAdapter1.updateFieldIfPresent("ROUTE_VALIDATE", new URL(str1 + "validate"));
            localFieldAdapter1.updateFieldIfPresent("ROUTE_SIGNOUT", new URL(str1 + "signout"));
            String str2 = paramAlteningServiceType.getSessionServer();
            FieldAdapter localFieldAdapter2 = this.minecraftSessionServer;
            localFieldAdapter2.updateFieldIfPresent("BASE_URL", str2 + "session/minecraft/");
            localFieldAdapter2.updateFieldIfPresent("JOIN_URL", new URL(str2 + "session/minecraft/join"));
            localFieldAdapter2.updateFieldIfPresent("CHECK_URL", new URL(str2 + "session/minecraft/hasJoined"));
        } catch (Exception localException) {
            localException.printStackTrace();
            return AlteningServiceType.MOJANG;
        }
        return paramAlteningServiceType;
    }
}




