// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C03PacketPlayer implements Packet<INetHandlerPlayServer>
{
    protected double x;
    protected double y;
    protected double z;
    public static float yaw;
    public static float pitch;
    protected boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    
    public C03PacketPlayer() {
    }
    
    public C03PacketPlayer(final boolean isOnGround) {
        this.onGround = isOnGround;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.onGround = (buf.readUnsignedByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.onGround ? 1 : 0);
    }
    
    public double getPositionX() {
        return this.x;
    }
    
    public double getPositionY() {
        return this.y;
    }
    
    public double getPositionZ() {
        return this.z;
    }
    
    public float getYaw() {
        return C03PacketPlayer.yaw;
    }
    
    public float getPitch() {
        return C03PacketPlayer.pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public boolean isMoving() {
        return this.moving;
    }
    
    public boolean getRotating() {
        return this.rotating;
    }
    
    public void setMoving(final boolean isMoving) {
        this.moving = isMoving;
    }
    
    public static class C04PacketPlayerPosition extends C03PacketPlayer
    {
        public C04PacketPlayerPosition() {
            this.moving = true;
        }
        
        public C04PacketPlayerPosition(final double posX, final double posY, final double posZ, final boolean isOnGround) {
            this.x = posX;
            this.y = posY;
            this.z = posZ;
            this.onGround = isOnGround;
            this.moving = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            this.x = buf.readDouble();
            this.y = buf.readDouble();
            this.z = buf.readDouble();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            super.writePacketData(buf);
        }
    }
    
    public static class C05PacketPlayerLook extends C03PacketPlayer
    {
        public C05PacketPlayerLook() {
            this.rotating = true;
        }
        
        public C05PacketPlayerLook(final float playerYaw, final float playerPitch, final boolean isOnGround) {
            C05PacketPlayerLook.yaw = playerYaw;
            C05PacketPlayerLook.pitch = playerPitch;
            this.onGround = isOnGround;
            this.rotating = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            C05PacketPlayerLook.yaw = buf.readFloat();
            C05PacketPlayerLook.pitch = buf.readFloat();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeFloat(C05PacketPlayerLook.yaw);
            buf.writeFloat(C05PacketPlayerLook.pitch);
            super.writePacketData(buf);
        }
    }
    
    public static class C06PacketPlayerPosLook extends C03PacketPlayer
    {
        public C06PacketPlayerPosLook() {
            this.moving = true;
            this.rotating = true;
        }
        
        public C06PacketPlayerPosLook(final double playerX, final double playerY, final double playerZ, final float playerYaw, final float playerPitch, final boolean playerIsOnGround) {
            this.x = playerX;
            this.y = playerY;
            this.z = playerZ;
            C06PacketPlayerPosLook.yaw = playerYaw;
            C06PacketPlayerPosLook.pitch = playerPitch;
            this.onGround = playerIsOnGround;
            this.rotating = true;
            this.moving = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            this.x = buf.readDouble();
            this.y = buf.readDouble();
            this.z = buf.readDouble();
            C06PacketPlayerPosLook.yaw = buf.readFloat();
            C06PacketPlayerPosLook.pitch = buf.readFloat();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeFloat(C06PacketPlayerPosLook.yaw);
            buf.writeFloat(C06PacketPlayerPosLook.pitch);
            super.writePacketData(buf);
        }
    }
}
