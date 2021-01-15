package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class C12PacketUpdateSign implements Packet
{
    private BlockPos pos;
    private IChatComponent[] lines;

    public C12PacketUpdateSign() {}

    public C12PacketUpdateSign(BlockPos pos, IChatComponent[] lines)
    {
        this.pos = pos;
        this.lines = new IChatComponent[] {lines[0], lines[1], lines[2], lines[3]};
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.pos = buf.readBlockPos();
        this.lines = new IChatComponent[4];

        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.lines[var2] = buf.readChatComponent();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.pos);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            buf.writeChatComponent(this.lines[var2]);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processUpdateSign(this);
    }

    public BlockPos getPosition()
    {
        return this.pos;
    }

    public IChatComponent[] getLines()
    {
        return this.lines;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
