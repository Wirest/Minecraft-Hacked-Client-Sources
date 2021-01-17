// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S0FPacketSpawnMob implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int type;
    private int x;
    private int y;
    private int z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private byte yaw;
    private byte pitch;
    private byte headPitch;
    private DataWatcher field_149043_l;
    private List<DataWatcher.WatchableObject> watcher;
    
    public S0FPacketSpawnMob() {
    }
    
    public S0FPacketSpawnMob(final EntityLivingBase entityIn) {
        this.entityId = entityIn.getEntityId();
        this.type = (byte)EntityList.getEntityID(entityIn);
        this.x = MathHelper.floor_double(entityIn.posX * 32.0);
        this.y = MathHelper.floor_double(entityIn.posY * 32.0);
        this.z = MathHelper.floor_double(entityIn.posZ * 32.0);
        this.yaw = (byte)(entityIn.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityIn.rotationPitch * 256.0f / 360.0f);
        this.headPitch = (byte)(entityIn.rotationYawHead * 256.0f / 360.0f);
        final double d0 = 3.9;
        double d2 = entityIn.motionX;
        double d3 = entityIn.motionY;
        double d4 = entityIn.motionZ;
        if (d2 < -d0) {
            d2 = -d0;
        }
        if (d3 < -d0) {
            d3 = -d0;
        }
        if (d4 < -d0) {
            d4 = -d0;
        }
        if (d2 > d0) {
            d2 = d0;
        }
        if (d3 > d0) {
            d3 = d0;
        }
        if (d4 > d0) {
            d4 = d0;
        }
        this.velocityX = (int)(d2 * 8000.0);
        this.velocityY = (int)(d3 * 8000.0);
        this.velocityZ = (int)(d4 * 8000.0);
        this.field_149043_l = entityIn.getDataWatcher();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.type = (buf.readByte() & 0xFF);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.headPitch = buf.readByte();
        this.velocityX = buf.readShort();
        this.velocityY = buf.readShort();
        this.velocityZ = buf.readShort();
        this.watcher = DataWatcher.readWatchedListFromPacketBuffer(buf);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.type & 0xFF);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        buf.writeByte(this.headPitch);
        buf.writeShort(this.velocityX);
        buf.writeShort(this.velocityY);
        buf.writeShort(this.velocityZ);
        this.field_149043_l.writeTo(buf);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnMob(this);
    }
    
    public List<DataWatcher.WatchableObject> func_149027_c() {
        if (this.watcher == null) {
            this.watcher = this.field_149043_l.getAllWatched();
        }
        return this.watcher;
    }
    
    public int getEntityID() {
        return this.entityId;
    }
    
    public int getEntityType() {
        return this.type;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public int getVelocityX() {
        return this.velocityX;
    }
    
    public int getVelocityY() {
        return this.velocityY;
    }
    
    public int getVelocityZ() {
        return this.velocityZ;
    }
    
    public byte getYaw() {
        return this.yaw;
    }
    
    public byte getPitch() {
        return this.pitch;
    }
    
    public byte getHeadPitch() {
        return this.headPitch;
    }
}
