package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import java.io.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

public class S22PacketMultiBlockChange implements Packet<INetHandlerPlayClient>
{
    private ChunkCoordIntPair chunkPosCoord;
    private BlockUpdateData[] changedBlocks;
    
    public S22PacketMultiBlockChange() {
    }
    
    public S22PacketMultiBlockChange(final int p_i45181_1_, final short[] crammedPositionsIn, final Chunk chunkIn) {
        this.chunkPosCoord = new ChunkCoordIntPair(chunkIn.xPosition, chunkIn.zPosition);
        this.changedBlocks = new BlockUpdateData[p_i45181_1_];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(crammedPositionsIn[i], chunkIn);
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chunkPosCoord = new ChunkCoordIntPair(buf.readInt(), buf.readInt());
        this.changedBlocks = new BlockUpdateData[buf.readVarIntFromBuffer()];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(buf.readShort(), (IBlockState)Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer()));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkPosCoord.chunkXPos);
        buf.writeInt(this.chunkPosCoord.chunkZPos);
        buf.writeVarIntToBuffer(this.changedBlocks.length);
        BlockUpdateData[] changedBlocks;
        for (int length = (changedBlocks = this.changedBlocks).length, i = 0; i < length; ++i) {
            final BlockUpdateData s22packetmultiblockchange$blockupdatedata = changedBlocks[i];
            buf.writeShort(s22packetmultiblockchange$blockupdatedata.func_180089_b());
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(s22packetmultiblockchange$blockupdatedata.getBlockState()));
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleMultiBlockChange(this);
    }
    
    public BlockUpdateData[] getChangedBlocks() {
        return this.changedBlocks;
    }
    
    public class BlockUpdateData
    {
        private final short chunkPosCrammed;
        private final IBlockState blockState;
        
        public BlockUpdateData(final short p_i45984_2_, final IBlockState state) {
            this.chunkPosCrammed = p_i45984_2_;
            this.blockState = state;
        }
        
        public BlockUpdateData(final short p_i45985_2_, final Chunk chunkIn) {
            this.chunkPosCrammed = p_i45985_2_;
            this.blockState = chunkIn.getBlockState(this.getPos());
        }
        
        public BlockPos getPos() {
            return new BlockPos(S22PacketMultiBlockChange.this.chunkPosCoord.getBlock(this.chunkPosCrammed >> 12 & 0xF, this.chunkPosCrammed & 0xFF, this.chunkPosCrammed >> 8 & 0xF));
        }
        
        public short func_180089_b() {
            return this.chunkPosCrammed;
        }
        
        public IBlockState getBlockState() {
            return this.blockState;
        }
    }
}
