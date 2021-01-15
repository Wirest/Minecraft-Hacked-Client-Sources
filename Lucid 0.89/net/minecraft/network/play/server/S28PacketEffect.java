package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S28PacketEffect implements Packet
{
    private int soundType;
    private BlockPos field_179747_b;

    /** can be a block/item id or other depending on the soundtype */
    private int soundData;

    /** If true the sound is played across the server */
    private boolean serverWide;

    public S28PacketEffect() {}

    public S28PacketEffect(int soundTypeIn, BlockPos p_i45978_2_, int soundDataIn, boolean serverWideIn)
    {
        this.soundType = soundTypeIn;
        this.field_179747_b = p_i45978_2_;
        this.soundData = soundDataIn;
        this.serverWide = serverWideIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.soundType = buf.readInt();
        this.field_179747_b = buf.readBlockPos();
        this.soundData = buf.readInt();
        this.serverWide = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.soundType);
        buf.writeBlockPos(this.field_179747_b);
        buf.writeInt(this.soundData);
        buf.writeBoolean(this.serverWide);
    }

    public void func_180739_a(INetHandlerPlayClient handler)
    {
        handler.handleEffect(this);
    }

    public boolean isSoundServerwide()
    {
        return this.serverWide;
    }

    public int getSoundType()
    {
        return this.soundType;
    }

    public int getSoundData()
    {
        return this.soundData;
    }

    public BlockPos func_179746_d()
    {
        return this.field_179747_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180739_a((INetHandlerPlayClient)handler);
    }
}
