package me.memewaredevs.proxymanager;

public enum ProxyVersion {
    SOCKSv4("SOCKS4", 4145, 1080), SOCKSv5("SOCKS5", 1080, 9050, 9051), AuthSocks("SOCKS5", 720, 1080, 4146);

    public String ver_name;
    public int defaultPort;
    public int extraPossiblePorts[];

    ProxyVersion(String name, int portDefault, int... extraPossiblePorts) {
        this.ver_name = name;
        this.defaultPort = portDefault;
        this.extraPossiblePorts = extraPossiblePorts;
    }

}
