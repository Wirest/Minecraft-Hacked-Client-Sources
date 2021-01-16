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
    private static final String __OBFID = "CL_00001307";

    public S28PacketEffect() {}

    public S28PacketEffect(int p_i45978_1_, BlockPos p_i45978_2_, int p_i45978_3_, boolean p_i45978_4_)
    {
        this.soundType = p_i45978_1_;
        this.field_179747_b = p_i45978_2_;
        this.soundData = p_i45978_3_;
        this.serverWide = p_i45978_4_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.soundType = data.readInt();
        this.field_179747_b = data.readBlockPos();
        this.soundData = data.readInt();
        this.serverWide = data.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.soundType);
        data.writeBlockPos(this.field_179747_b);
        data.writeInt(this.soundData);
        data.writeBoolean(this.serverWide);
    }

    public void func_180739_a(INetHandlerPlayClient p_180739_1_)
    {
        p_180739_1_.handleEffect(this);
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
    public void processPacket(INetHandler handler)
    {
        this.func_180739_a((INetHandlerPlayClient)handler);
    }
}
