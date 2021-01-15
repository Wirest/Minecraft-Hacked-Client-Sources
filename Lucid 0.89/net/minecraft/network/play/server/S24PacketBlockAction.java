package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S24PacketBlockAction implements Packet
{
    private BlockPos blockPosition;
    private int instrument;
    private int pitch;
    private Block field_148871_f;

    public S24PacketBlockAction() {}

    public S24PacketBlockAction(BlockPos blockPositionIn, Block p_i45989_2_, int instrumentIn, int pitchIn)
    {
        this.blockPosition = blockPositionIn;
        this.instrument = instrumentIn;
        this.pitch = pitchIn;
        this.field_148871_f = p_i45989_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.blockPosition = buf.readBlockPos();
        this.instrument = buf.readUnsignedByte();
        this.pitch = buf.readUnsignedByte();
        this.field_148871_f = Block.getBlockById(buf.readVarIntFromBuffer() & 4095);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.blockPosition);
        buf.writeByte(this.instrument);
        buf.writeByte(this.pitch);
        buf.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148871_f) & 4095);
    }

    public void func_180726_a(INetHandlerPlayClient handler)
    {
        handler.handleBlockAction(this);
    }

    public BlockPos getBlockPosition()
    {
        return this.blockPosition;
    }

    /**
     * instrument data for noteblocks
     */
    public int getData1()
    {
        return this.instrument;
    }

    /**
     * pitch data for noteblocks
     */
    public int getData2()
    {
        return this.pitch;
    }

    public Block getBlockType()
    {
        return this.field_148871_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180726_a((INetHandlerPlayClient)handler);
    }
}
