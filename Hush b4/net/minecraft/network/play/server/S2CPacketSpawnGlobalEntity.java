// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S2CPacketSpawnGlobalEntity implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int x;
    private int y;
    private int z;
    private int type;
    
    public S2CPacketSpawnGlobalEntity() {
    }
    
    public S2CPacketSpawnGlobalEntity(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        this.x = MathHelper.floor_double(entityIn.posX * 32.0);
        this.y = MathHelper.floor_double(entityIn.posY * 32.0);
        this.z = MathHelper.floor_double(entityIn.posZ * 32.0);
        if (entityIn instanceof EntityLightningBolt) {
            this.type = 1;
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.type = buf.readByte();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.type);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnGlobalEntity(this);
    }
    
    public int func_149052_c() {
        return this.entityId;
    }
    
    public int func_149051_d() {
        return this.x;
    }
    
    public int func_149050_e() {
        return this.y;
    }
    
    public int func_149049_f() {
        return this.z;
    }
    
    public int func_149053_g() {
        return this.type;
    }
}
