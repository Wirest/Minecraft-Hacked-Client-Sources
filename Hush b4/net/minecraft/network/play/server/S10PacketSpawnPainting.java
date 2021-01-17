// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S10PacketSpawnPainting implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private BlockPos position;
    private EnumFacing facing;
    private String title;
    
    public S10PacketSpawnPainting() {
    }
    
    public S10PacketSpawnPainting(final EntityPainting painting) {
        this.entityID = painting.getEntityId();
        this.position = painting.getHangingPosition();
        this.facing = painting.facingDirection;
        this.title = painting.art.title;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.title = buf.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.getHorizontal(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeString(this.title);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getHorizontalIndex());
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnPainting(this);
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public EnumFacing getFacing() {
        return this.facing;
    }
    
    public String getTitle() {
        return this.title;
    }
}
