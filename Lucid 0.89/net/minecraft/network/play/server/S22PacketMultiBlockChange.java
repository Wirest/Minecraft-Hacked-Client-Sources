package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class S22PacketMultiBlockChange implements Packet
{
    private ChunkCoordIntPair chunkPosCoord;
    private S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks;

    public S22PacketMultiBlockChange() {}

    public S22PacketMultiBlockChange(int p_i45181_1_, short[] p_i45181_2_, Chunk p_i45181_3_)
    {
        this.chunkPosCoord = new ChunkCoordIntPair(p_i45181_3_.xPosition, p_i45181_3_.zPosition);
        this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[p_i45181_1_];

        for (int var4 = 0; var4 < this.changedBlocks.length; ++var4)
        {
            this.changedBlocks[var4] = new S22PacketMultiBlockChange.BlockUpdateData(p_i45181_2_[var4], p_i45181_3_);
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chunkPosCoord = new ChunkCoordIntPair(buf.readInt(), buf.readInt());
        this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[buf.readVarIntFromBuffer()];

        for (int var2 = 0; var2 < this.changedBlocks.length; ++var2)
        {
            this.changedBlocks[var2] = new S22PacketMultiBlockChange.BlockUpdateData(buf.readShort(), (IBlockState)Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer()));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.chunkPosCoord.chunkXPos);
        buf.writeInt(this.chunkPosCoord.chunkZPos);
        buf.writeVarIntToBuffer(this.changedBlocks.length);
        S22PacketMultiBlockChange.BlockUpdateData[] var2 = this.changedBlocks;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            S22PacketMultiBlockChange.BlockUpdateData var5 = var2[var4];
            buf.writeShort(var5.func_180089_b());
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(var5.func_180088_c()));
        }
    }

    public void func_180729_a(INetHandlerPlayClient p_180729_1_)
    {
        p_180729_1_.handleMultiBlockChange(this);
    }

    public S22PacketMultiBlockChange.BlockUpdateData[] func_179844_a()
    {
        return this.changedBlocks;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180729_a((INetHandlerPlayClient)handler);
    }

    public class BlockUpdateData
    {
        private final short field_180091_b;
        private final IBlockState field_180092_c;

        public BlockUpdateData(short p_i45984_2_, IBlockState state)
        {
            this.field_180091_b = p_i45984_2_;
            this.field_180092_c = state;
        }

        public BlockUpdateData(short p_i45985_2_, Chunk chunkIn)
        {
            this.field_180091_b = p_i45985_2_;
            this.field_180092_c = chunkIn.getBlockState(this.func_180090_a());
        }

        public BlockPos func_180090_a()
        {
            return new BlockPos(S22PacketMultiBlockChange.this.chunkPosCoord.getBlock(this.field_180091_b >> 12 & 15, this.field_180091_b & 255, this.field_180091_b >> 8 & 15));
        }

        public short func_180089_b()
        {
            return this.field_180091_b;
        }

        public IBlockState func_180088_c()
        {
            return this.field_180092_c;
        }
    }
}
