package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1FPacketSetExperience implements Packet {
    private float field_149401_a;
    private int field_149399_b;
    private int field_149400_c;
    private static final String __OBFID = "CL_00001331";

    public S1FPacketSetExperience() {
    }

    public S1FPacketSetExperience(float p_i45222_1_, int p_i45222_2_, int p_i45222_3_) {
        this.field_149401_a = p_i45222_1_;
        this.field_149399_b = p_i45222_2_;
        this.field_149400_c = p_i45222_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149401_a = data.readFloat();
        this.field_149400_c = data.readVarIntFromBuffer();
        this.field_149399_b = data.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeFloat(this.field_149401_a);
        data.writeVarIntToBuffer(this.field_149400_c);
        data.writeVarIntToBuffer(this.field_149399_b);
    }

    public void func_180749_a(INetHandlerPlayClient p_180749_1_) {
        p_180749_1_.handleSetExperience(this);
    }

    public float func_149397_c() {
        return this.field_149401_a;
    }

    public int func_149396_d() {
        return this.field_149399_b;
    }

    public int func_149395_e() {
        return this.field_149400_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180749_a((INetHandlerPlayClient) handler);
    }
}
