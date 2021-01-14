package net.minecraft.network.play.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C14PacketTabComplete implements Packet<INetHandlerPlayServer>
{
    private String message;
    private BlockPos targetBlock;

    public C14PacketTabComplete()
    {
    }

    public C14PacketTabComplete(String msg)
    {
        this(msg, (BlockPos)null);
    }

    public C14PacketTabComplete(String msg, BlockPos target)
    {
        this.message = msg;
        this.targetBlock = target;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.message = buf.readStringFromBuffer(32767);
        boolean flag = buf.readBoolean();

        if (flag)
        {
            this.targetBlock = buf.readBlockPos();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(StringUtils.substring(this.message, 0, 32767));
        boolean flag = this.targetBlock != null;
        buf.writeBoolean(flag);

        if (flag)
        {
            buf.writeBlockPos(this.targetBlock);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processTabComplete(this);
    }

    public String getMessage()
    {
        return this.message;
    }

    public BlockPos getTargetBlock()
    {
        return this.targetBlock;
    }
}
