/*
 * Decompiled with CFR 0_122.
 */
package me.memewaredevs.proxymanager;

public final class SocksProxy {
    private String type;
    private final String ip;
    private String port;

    public SocksProxy(String ip, String port, ProxyVersion version) {
        this(ip, port, version.name().toUpperCase().replace("V", "v"));
    }

    public SocksProxy(String ip, String port, String mask) {
        this.ip = ip;
        this.port = port;
        this.type = mask;
    }

    public String getMask() {
        return this.type;
    }

    public String getPassword() {
        return this.port;
    }

    public String getIP() {
        return this.ip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPort(String port) {
        this.port = port;
    }
}

