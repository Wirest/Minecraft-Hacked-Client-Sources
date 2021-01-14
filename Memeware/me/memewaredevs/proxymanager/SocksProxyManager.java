/*
+
 * Decompiled with CFR 0_122.
 */
package me.memewaredevs.proxymanager;

import java.util.ArrayList;

public class SocksProxyManager {
    public static ArrayList<SocksProxy> registry;

    public ArrayList<SocksProxy> getRegistry() {
        return registry;
    }

    static {
        registry = new ArrayList();
    }
}

