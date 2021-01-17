// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S0EPacketSpawnObject implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int x;
    private int y;
    private int z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private int type;
    private int field_149020_k;
    
    public S0EPacketSpawnObject() {
    }
    
    public S0EPacketSpawnObject(final Entity entityIn, final int typeIn) {
        this(entityIn, typeIn, 0);
    }
    
    public S0EPacketSpawnObject(final Entity entityIn, final int typeIn, final int p_i45166_3_) {
        this.entityId = entityIn.getEntityId();
        this.x = MathHelper.floor_double(entityIn.posX * 32.0);
        this.y = MathHelper.floor_double(entityIn.posY * 32.0);
        this.z = MathHelper.floor_double(entityIn.posZ * 32.0);
        this.pitch = MathHelper.floor_float(entityIn.rotationPitch * 256.0f / 360.0f);
        this.yaw = MathHelper.floor_float(entityIn.rotationYaw * 256.0f / 360.0f);
        this.type = typeIn;
        this.field_149020_k = p_i45166_3_;
        if (p_i45166_3_ > 0) {
            double d0 = entityIn.motionX;
            double d2 = entityIn.motionY;
            double d3 = entityIn.motionZ;
            final double d4 = 3.9;
            if (d0 < -d4) {
                d0 = -d4;
            }
            if (d2 < -d4) {
                d2 = -d4;
            }
            if (d3 < -d4) {
                d3 = -d4;
            }
            if (d0 > d4) {
                d0 = d4;
            }
            if (d2 > d4) {
                d2 = d4;
            }
            if (d3 > d4) {
                d3 = d4;
            }
            this.speedX = (int)(d0 * 8000.0);
            this.speedY = (int)(d2 * 8000.0);
            this.speedZ = (int)(d3 * 8000.0);
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.type = buf.readByte();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.field_149020_k = buf.readInt();
        if (this.field_149020_k > 0) {
            this.speedX = buf.readShort();
            this.speedY = buf.readShort();
            this.speedZ = buf.readShort();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.type);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeByte(this.pitch);
        buf.writeByte(this.yaw);
        buf.writeInt(this.field_149020_k);
        if (this.field_149020_k > 0) {
            buf.writeShort(this.speedX);
            buf.writeShort(this.speedY);
            buf.writeShort(this.speedZ);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnObject(this);
    }
    
    public int getEntityID() {
        return this.entityId;
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
    
    public int getSpeedX() {
        return this.speedX;
    }
    
    public int getSpeedY() {
        return this.speedY;
    }
    
    public int getSpeedZ() {
        return this.speedZ;
    }
    
    public int getPitch() {
        return this.pitch;
    }
    
    public int getYaw() {
        return this.yaw;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int func_149009_m() {
        return this.field_149020_k;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void setZ(final int newZ) {
        this.z = newZ;
    }
    
    public void setSpeedX(final int newSpeedX) {
        this.speedX = newSpeedX;
    }
    
    public void setSpeedY(final int newSpeedY) {
        this.speedY = newSpeedY;
    }
    
    public void setSpeedZ(final int newSpeedZ) {
        this.speedZ = newSpeedZ;
    }
    
    public void func_149002_g(final int p_149002_1_) {
        this.field_149020_k = p_149002_1_;
    }
}
