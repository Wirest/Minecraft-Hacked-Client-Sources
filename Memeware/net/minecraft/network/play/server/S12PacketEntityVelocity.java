package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity implements Packet {
    public int field_149417_a;
    public int field_149415_b;
    public int field_149416_c;
    public int field_149414_d;
    private static final String __OBFID = "CL_00001328";

    public S12PacketEntityVelocity() {
    }

    public S12PacketEntityVelocity(Entity p_i45219_1_) {
        this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX, p_i45219_1_.motionY, p_i45219_1_.motionZ);
    }

    public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_) {
        this.field_149417_a = p_i45220_1_;
        double var8 = 3.9D;

        if (p_i45220_2_ < -var8) {
            p_i45220_2_ = -var8;
        }

        if (p_i45220_4_ < -var8) {
            p_i45220_4_ = -var8;
        }

        if (p_i45220_6_ < -var8) {
            p_i45220_6_ = -var8;
        }

        if (p_i45220_2_ > var8) {
            p_i45220_2_ = var8;
        }

        if (p_i45220_4_ > var8) {
            p_i45220_4_ = var8;
        }

        if (p_i45220_6_ > var8) {
            p_i45220_6_ = var8;
        }

        this.field_149415_b = (int) (p_i45220_2_ * 8000.0D);
        this.field_149416_c = (int) (p_i45220_4_ * 8000.0D);
        this.field_149414_d = (int) (p_i45220_6_ * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149417_a = data.readVarIntFromBuffer();
        this.field_149415_b = data.readShort();
        this.field_149416_c = data.readShort();
        this.field_149414_d = data.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149417_a);
        data.writeShort(this.field_149415_b);
        data.writeShort(this.field_149416_c);
        data.writeShort(this.field_149414_d);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }

    public int func_149412_c() {
        return this.field_149417_a;
    }

    public int func_149411_d() {
        return this.field_149415_b;
    }

    public int func_149410_e() {
        return this.field_149416_c;
    }

    public int func_149409_f() {
        return this.field_149414_d;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
