package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class C14PacketTabComplete implements Packet
{
    private String message;
    private BlockPos field_179710_b;
    private static final String __OBFID = "CL_00001346";

    public C14PacketTabComplete() {}

    public C14PacketTabComplete(String msg)
    {
        this(msg, (BlockPos)null);
    }

    public C14PacketTabComplete(String p_i45948_1_, BlockPos p_i45948_2_)
    {
        this.message = p_i45948_1_;
        this.field_179710_b = p_i45948_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.message = data.readStringFromBuffer(32767);
        boolean var2 = data.readBoolean();

        if (var2)
        {
            this.field_179710_b = data.readBlockPos();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeString(StringUtils.substring(this.message, 0, 32767));
        boolean var2 = this.field_179710_b != null;
        data.writeBoolean(var2);

        if (var2)
        {
            data.writeBlockPos(this.field_179710_b);
        }
    }

    public void func_180756_a(INetHandlerPlayServer p_180756_1_)
    {
        p_180756_1_.processTabComplete(this);
    }

    public String getMessage()
    {
        return this.message;
    }

    public BlockPos func_179709_b()
    {
        return this.field_179710_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180756_a((INetHandlerPlayServer)handler);
    }
}
