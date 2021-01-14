package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;

public class S29PacketSoundEffect implements Packet {
    private String name;
    private int x;
    private int y = Integer.MAX_VALUE;
    private int z;
    private float field_149216_e;
    private int field_149214_f;
    private static final String __OBFID = "CL_00001309";

    public S29PacketSoundEffect() {
    }

    public S29PacketSoundEffect(String name, double x, double y, double z, float p_i45200_8_, float p_i45200_9_) {
        Validate.notNull(name, "name", new Object[0]);
        this.name = name;
        this.x = (int) (x * 8.0D);
        this.y = (int) (y * 8.0D);
        this.z = (int) (z * 8.0D);
        this.field_149216_e = p_i45200_8_;
        this.field_149214_f = (int) (p_i45200_9_ * 63.0F);
        p_i45200_9_ = MathHelper.clamp_float(p_i45200_9_, 0.0F, 255.0F);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.name = data.readStringFromBuffer(256);
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.field_149216_e = data.readFloat();
        this.field_149214_f = data.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeString(this.name);
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeFloat(this.field_149216_e);
        data.writeByte(this.field_149214_f);
    }

    public String getName() {
        return this.name;
    }

    public double getX() {
        return (double) ((float) this.x / 8.0F);
    }

    public double getY() {
        return (double) ((float) this.y / 8.0F);
    }

    public double getZ() {
        return (double) ((float) this.z / 8.0F);
    }

    public float func_149208_g() {
        return this.field_149216_e;
    }

    public float func_149209_h() {
        return (float) this.field_149214_f / 63.0F;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
