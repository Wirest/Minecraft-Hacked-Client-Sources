package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer implements Packet {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    private static final String __OBFID = "CL_00001360";

    public C03PacketPlayer() {
    }

    public C03PacketPlayer(boolean p_i45256_1_) {
        this.onGround = p_i45256_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.onGround = data.readUnsignedByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.onGround ? 1 : 0);
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

    public void setPositionY(double y) {
        this.y = y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
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

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }

    public static class C04PacketPlayerPosition extends C03PacketPlayer {
        private static final String __OBFID = "CL_00001361";

        public C04PacketPlayerPosition() {
            this.moving = true;
        }

        public C04PacketPlayerPosition(double x, double y, double z, boolean g) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.onGround = g;
            this.moving = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }

    public static class C05PacketPlayerLook extends C03PacketPlayer {
        private static final String __OBFID = "CL_00001363";

        public C05PacketPlayerLook() {
            this.rotating = true;
        }

        public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_) {
            this.yaw = p_i45255_1_;
            this.pitch = p_i45255_2_;
            this.onGround = p_i45255_3_;
            this.rotating = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }

    public static class C06PacketPlayerPosLook extends C03PacketPlayer {
        private static final String __OBFID = "CL_00001362";

        public C06PacketPlayerPosLook() {
            this.moving = true;
            this.rotating = true;
        }

        public C06PacketPlayerPosLook(double p_i45941_1_, double p_i45941_3_, double p_i45941_5_, float p_i45941_7_, float p_i45941_8_, boolean p_i45941_9_) {
            this.x = p_i45941_1_;
            this.y = p_i45941_3_;
            this.z = p_i45941_5_;
            this.yaw = p_i45941_7_;
            this.pitch = p_i45941_8_;
            this.onGround = p_i45941_9_;
            this.rotating = true;
            this.moving = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }
}
