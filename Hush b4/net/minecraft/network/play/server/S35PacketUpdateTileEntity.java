// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S35PacketUpdateTileEntity implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPos;
    private int metadata;
    private NBTTagCompound nbt;
    
    public S35PacketUpdateTileEntity() {
    }
    
    public S35PacketUpdateTileEntity(final BlockPos blockPosIn, final int metadataIn, final NBTTagCompound nbtIn) {
        this.blockPos = blockPosIn;
        this.metadata = metadataIn;
        this.nbt = nbtIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.blockPos = buf.readBlockPos();
        this.metadata = buf.readUnsignedByte();
        this.nbt = buf.readNBTTagCompoundFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.blockPos);
        buf.writeByte((byte)this.metadata);
        buf.writeNBTTagCompoundToBuffer(this.nbt);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateTileEntity(this);
    }
    
    public BlockPos getPos() {
        return this.blockPos;
    }
    
    public int getTileEntityType() {
        return this.metadata;
    }
    
    public NBTTagCompound getNbtCompound() {
        return this.nbt;
    }
}
