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

public class S18PacketEntityTeleport implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int posX;
    private int posY;
    private int posZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;
    
    public S18PacketEntityTeleport() {
    }
    
    public S18PacketEntityTeleport(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        this.posX = MathHelper.floor_double(entityIn.posX * 32.0);
        this.posY = MathHelper.floor_double(entityIn.posY * 32.0);
        this.posZ = MathHelper.floor_double(entityIn.posZ * 32.0);
        this.yaw = (byte)(entityIn.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityIn.rotationPitch * 256.0f / 360.0f);
        this.onGround = entityIn.onGround;
    }
    
    public S18PacketEntityTeleport(final int entityIdIn, final int posXIn, final int posYIn, final int posZIn, final byte yawIn, final byte pitchIn, final boolean onGroundIn) {
        this.entityId = entityIdIn;
        this.posX = posXIn;
        this.posY = posYIn;
        this.posZ = posZIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.onGround = onGroundIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.onGround = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        buf.writeBoolean(this.onGround);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityTeleport(this);
    }
    
    public int getEntityId() {
        return this.entityId;
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
    
    public byte getYaw() {
        return this.yaw;
    }
    
    public byte getPitch() {
        return this.pitch;
    }
    
    public boolean getOnGround() {
        return this.onGround;
    }
}
