// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S49PacketUpdateEntityNBT implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private NBTTagCompound tagCompound;
    
    public S49PacketUpdateEntityNBT() {
    }
    
    public S49PacketUpdateEntityNBT(final int entityIdIn, final NBTTagCompound tagCompoundIn) {
        this.entityId = entityIdIn;
        this.tagCompound = tagCompoundIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.tagCompound = buf.readNBTTagCompoundFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeNBTTagCompoundToBuffer(this.tagCompound);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityNBT(this);
    }
    
    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }
    
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
}
