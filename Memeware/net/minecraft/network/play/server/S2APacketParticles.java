package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class S2APacketParticles implements Packet {
    private EnumParticleTypes field_179751_a;
    private float field_149234_b;
    private float field_149235_c;
    private float field_149232_d;
    private float field_149233_e;
    private float field_149230_f;
    private float field_149231_g;
    private float field_149237_h;
    private int field_149238_i;
    private boolean field_179752_j;
    private int[] field_179753_k;
    private static final String __OBFID = "CL_00001308";

    public S2APacketParticles() {
    }

    public S2APacketParticles(EnumParticleTypes p_i45977_1_, boolean p_i45977_2_, float p_i45977_3_, float p_i45977_4_, float p_i45977_5_, float p_i45977_6_, float p_i45977_7_, float p_i45977_8_, float p_i45977_9_, int p_i45977_10_, int... p_i45977_11_) {
        this.field_179751_a = p_i45977_1_;
        this.field_179752_j = p_i45977_2_;
        this.field_149234_b = p_i45977_3_;
        this.field_149235_c = p_i45977_4_;
        this.field_149232_d = p_i45977_5_;
        this.field_149233_e = p_i45977_6_;
        this.field_149230_f = p_i45977_7_;
        this.field_149231_g = p_i45977_8_;
        this.field_149237_h = p_i45977_9_;
        this.field_149238_i = p_i45977_10_;
        this.field_179753_k = p_i45977_11_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179751_a = EnumParticleTypes.func_179342_a(data.readInt());

        if (this.field_179751_a == null) {
            this.field_179751_a = EnumParticleTypes.BARRIER;
        }

        this.field_179752_j = data.readBoolean();
        this.field_149234_b = data.readFloat();
        this.field_149235_c = data.readFloat();
        this.field_149232_d = data.readFloat();
        this.field_149233_e = data.readFloat();
        this.field_149230_f = data.readFloat();
        this.field_149231_g = data.readFloat();
        this.field_149237_h = data.readFloat();
        this.field_149238_i = data.readInt();
        int var2 = this.field_179751_a.func_179345_d();
        this.field_179753_k = new int[var2];

        for (int var3 = 0; var3 < var2; ++var3) {
            this.field_179753_k[var3] = data.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeInt(this.field_179751_a.func_179348_c());
        data.writeBoolean(this.field_179752_j);
        data.writeFloat(this.field_149234_b);
        data.writeFloat(this.field_149235_c);
        data.writeFloat(this.field_149232_d);
        data.writeFloat(this.field_149233_e);
        data.writeFloat(this.field_149230_f);
        data.writeFloat(this.field_149231_g);
        data.writeFloat(this.field_149237_h);
        data.writeInt(this.field_149238_i);
        int var2 = this.field_179751_a.func_179345_d();

        for (int var3 = 0; var3 < var2; ++var3) {
            data.writeVarIntToBuffer(this.field_179753_k[var3]);
        }
    }

    public EnumParticleTypes func_179749_a() {
        return this.field_179751_a;
    }

    public boolean func_179750_b() {
        return this.field_179752_j;
    }

    public double func_149220_d() {
        return (double) this.field_149234_b;
    }

    public double func_149226_e() {
        return (double) this.field_149235_c;
    }

    public double func_149225_f() {
        return (double) this.field_149232_d;
    }

    public float func_149221_g() {
        return this.field_149233_e;
    }

    public float func_149224_h() {
        return this.field_149230_f;
    }

    public float func_149223_i() {
        return this.field_149231_g;
    }

    public float func_149227_j() {
        return this.field_149237_h;
    }

    public int func_149222_k() {
        return this.field_149238_i;
    }

    public int[] func_179748_k() {
        return this.field_179753_k;
    }

    public void func_180740_a(INetHandlerPlayClient p_180740_1_) {
        p_180740_1_.handleParticles(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180740_a((INetHandlerPlayClient) handler);
    }
}
