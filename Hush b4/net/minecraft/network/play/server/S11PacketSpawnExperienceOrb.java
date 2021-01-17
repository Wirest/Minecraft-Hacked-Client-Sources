// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S11PacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int posX;
    private int posY;
    private int posZ;
    private int xpValue;
    
    public S11PacketSpawnExperienceOrb() {
    }
    
    public S11PacketSpawnExperienceOrb(final EntityXPOrb xpOrb) {
        this.entityID = xpOrb.getEntityId();
        this.posX = MathHelper.floor_double(xpOrb.posX * 32.0);
        this.posY = MathHelper.floor_double(xpOrb.posY * 32.0);
        this.posZ = MathHelper.floor_double(xpOrb.posZ * 32.0);
        this.xpValue = xpOrb.getXpValue();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.xpValue = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeShort(this.xpValue);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnExperienceOrb(this);
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public int getX() {
        return this.posX;
    }
    
    public int getY() {
        return this.posY;
    }
    
    public int getZ() {
        return this.posZ;
    }
    
    public int getXPValue() {
        return this.xpValue;
    }
}
