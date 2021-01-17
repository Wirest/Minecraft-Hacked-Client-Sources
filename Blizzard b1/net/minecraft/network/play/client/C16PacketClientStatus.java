package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus implements Packet
{
    private C16PacketClientStatus.EnumState status;
    private static final String __OBFID = "CL_00001348";

    public C16PacketClientStatus() {}

    public C16PacketClientStatus(C16PacketClientStatus.EnumState statusIn)
    {
        this.status = statusIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.status = (C16PacketClientStatus.EnumState)data.readEnumValue(C16PacketClientStatus.EnumState.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeEnumValue(this.status);
    }

    public void func_180758_a(INetHandlerPlayServer p_180758_1_)
    {
        p_180758_1_.processClientStatus(this);
    }

    public C16PacketClientStatus.EnumState getStatus()
    {
        return this.status;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180758_a((INetHandlerPlayServer)handler);
    }

    public static enum EnumState
    {
        PERFORM_RESPAWN("PERFORM_RESPAWN", 0),
        REQUEST_STATS("REQUEST_STATS", 1),
        OPEN_INVENTORY_ACHIEVEMENT("OPEN_INVENTORY_ACHIEVEMENT", 2);

        private static final C16PacketClientStatus.EnumState[] $VALUES = new C16PacketClientStatus.EnumState[]{PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT};
        private static final String __OBFID = "CL_00001349";

        private EnumState(String p_i45947_1_, int p_i45947_2_) {}
    }
}
