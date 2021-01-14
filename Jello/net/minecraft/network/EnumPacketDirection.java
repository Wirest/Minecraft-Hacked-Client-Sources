package net.minecraft.network;

public enum EnumPacketDirection
{
    SERVERBOUND("SERVERBOUND", 0),
    CLIENTBOUND("CLIENTBOUND", 1);

    private static final EnumPacketDirection[] $VALUES = new EnumPacketDirection[]{SERVERBOUND, CLIENTBOUND};
    

    private EnumPacketDirection(String p_i45995_1_, int p_i45995_2_) {}
}
