package net.minecraft.network.handshake.client;

import java.io.IOException;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet
{
    private int protocolVersion;
    private String ip;
    private int port;
    private EnumConnectionState requestedState;
    private static final String __OBFID = "CL_00001372";

    public C00Handshake() {}

    public C00Handshake(int p_i45266_1_, String p_i45266_2_, int p_i45266_3_, EnumConnectionState p_i45266_4_)
    {
        this.protocolVersion = p_i45266_1_;
        this.ip = p_i45266_2_;
        this.port = p_i45266_3_;
        this.requestedState = p_i45266_4_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.protocolVersion = data.readVarIntFromBuffer();
        this.ip = data.readStringFromBuffer(255);
        this.port = data.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(data.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.protocolVersion);
        data.writeString(this.ip);
        data.writeShort(this.port);
        data.writeVarIntToBuffer(this.requestedState.getId());
    }

    public void func_180770_a(INetHandlerHandshakeServer p_180770_1_)
    {
        p_180770_1_.processHandshake(this);
    }

    public EnumConnectionState getRequestedState()
    {
        return this.requestedState;
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180770_a((INetHandlerHandshakeServer)handler);
    }
}
