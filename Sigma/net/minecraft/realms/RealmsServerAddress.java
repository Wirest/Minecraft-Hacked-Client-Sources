package net.minecraft.realms;

import net.minecraft.client.multiplayer.ServerAddress;

public class RealmsServerAddress {
    private final String host;
    private final int port;
    private static final String __OBFID = "CL_00001864";

    protected RealmsServerAddress(String p_i1121_1_, int p_i1121_2_) {
        host = p_i1121_1_;
        port = p_i1121_2_;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public static RealmsServerAddress parseString(String p_parseString_0_) {
        ServerAddress var1 = ServerAddress.func_78860_a(p_parseString_0_);
        return new RealmsServerAddress(var1.getIP(), var1.getPort());
    }
}
