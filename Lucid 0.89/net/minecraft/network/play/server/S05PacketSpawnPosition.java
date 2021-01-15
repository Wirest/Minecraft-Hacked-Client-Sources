package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S05PacketSpawnPosition implements Packet
{
    private BlockPos field_179801_a;

    public S05PacketSpawnPosition() {}

    public S05PacketSpawnPosition(BlockPos p_i45956_1_)
    {
        this.field_179801_a = p_i45956_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179801_a = buf.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.field_179801_a);
    }

    public void func_180752_a(INetHandlerPlayClient p_180752_1_)
    {
        p_180752_1_.handleSpawnPosition(this);
    }

    public BlockPos func_179800_a()
    {
        return this.field_179801_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180752_a((INetHandlerPlayClient)handler);
    }
}
