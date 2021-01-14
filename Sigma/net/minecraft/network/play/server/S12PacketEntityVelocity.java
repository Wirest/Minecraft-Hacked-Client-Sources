package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity implements Packet {
    private int entID;
    private int motX;
    private int motY;
    private int motZ;
    private static final String __OBFID = "CL_00001328";

    public S12PacketEntityVelocity() {
    }

    public S12PacketEntityVelocity(Entity e) {
        this(e.getEntityId(), e.motionX, e.motionY, e.motionZ);
    }

    public S12PacketEntityVelocity(int i, double x, double y, double z) {
        this.entID = i;
        double var8 = 3.9D;

        if (x < -var8) {
            x = -var8;
        }

        if (y < -var8) {
            y = -var8;
        }

        if (z < -var8) {
            z = -var8;
        }

        if (x > var8) {
            x = var8;
        }

        if (y > var8) {
            y = var8;
        }

        if (z > var8) {
            z = var8;
        }

        this.motX = (int) (x * 8000.0D);
        this.motY = (int) (y * 8000.0D);
        this.motZ = (int) (z * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.entID = data.readVarIntFromBuffer();
        this.motX = data.readShort();
        this.motY = data.readShort();
        this.motZ = data.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.entID);
        data.writeShort(this.motX);
        data.writeShort(this.motY);
        data.writeShort(this.motZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }

    public int getEntityID() {
        return this.entID;
    }

    public int getX() {
        return this.motX;
    }

    public int getY() {
        return this.motY;
    }

    public int getZ() {
        return this.motZ;
    }

    public void setMotX(int motX) {
        this.motX = motX;
    }

    public void setMotY(int motY) {
        this.motY = motY;
    }

    public void setMotZ(int motZ) {
        this.motZ = motZ;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
