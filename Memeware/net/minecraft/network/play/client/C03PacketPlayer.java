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
    public boolean field_149480_h;
    public boolean rotating;
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

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean func_149465_i() {
        return this.onGround;
    }

    public boolean func_149466_j() {
        return this.field_149480_h;
    }

    public boolean getRotating() {
        return this.rotating;
    }

    public void func_149469_a(boolean p_149469_1_) {
        this.field_149480_h = p_149469_1_;
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
            this.field_149480_h = true;
        }

        public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_, boolean p_i45942_7_) {
            this.x = p_i45942_1_;
            this.y = p_i45942_3_;
            this.z = p_i45942_5_;
            this.onGround = p_i45942_7_;
            this.field_149480_h = true;
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
            this.field_149480_h = true;
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
            this.field_149480_h = true;
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
