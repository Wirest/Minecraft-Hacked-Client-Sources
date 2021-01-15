package net.minecraft.network;

public enum EnumPacketDirection
{
    SERVERBOUND("SERVERBOUND", 0),
    CLIENTBOUND("CLIENTBOUND", 1);

    private static final EnumPacketDirection[] $VALUES = new EnumPacketDirection[]{SERVERBOUND, CLIENTBOUND};
    private static final String __OBFID = "CL_00002307";

    private EnumPacketDirection(String p_i45995_1_, int p_i45995_2_) {}
}
