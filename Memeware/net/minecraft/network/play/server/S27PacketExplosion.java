package net.minecraft.network.play.server;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion implements Packet {
    private double field_149158_a;
    private double field_149156_b;
    private double field_149157_c;
    private float field_149154_d;
    private List field_149155_e;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;
    private static final String __OBFID = "CL_00001300";

    public S27PacketExplosion() {
    }

    public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_, double p_i45193_5_, float p_i45193_7_, List p_i45193_8_, Vec3 p_i45193_9_) {
        this.field_149158_a = p_i45193_1_;
        this.field_149156_b = p_i45193_3_;
        this.field_149157_c = p_i45193_5_;
        this.field_149154_d = p_i45193_7_;
        this.field_149155_e = Lists.newArrayList(p_i45193_8_);

        if (p_i45193_9_ != null) {
            this.field_149152_f = (float) p_i45193_9_.xCoord;
            this.field_149153_g = (float) p_i45193_9_.yCoord;
            this.field_149159_h = (float) p_i45193_9_.zCoord;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149158_a = (double) data.readFloat();
        this.field_149156_b = (double) data.readFloat();
        this.field_149157_c = (double) data.readFloat();
        this.field_149154_d = data.readFloat();
        int var2 = data.readInt();
        this.field_149155_e = Lists.newArrayListWithCapacity(var2);
        int var3 = (int) this.field_149158_a;
        int var4 = (int) this.field_149156_b;
        int var5 = (int) this.field_149157_c;

        for (int var6 = 0; var6 < var2; ++var6) {
            int var7 = data.readByte() + var3;
            int var8 = data.readByte() + var4;
            int var9 = data.readByte() + var5;
            this.field_149155_e.add(new BlockPos(var7, var8, var9));
        }

        this.field_149152_f = data.readFloat();
        this.field_149153_g = data.readFloat();
        this.field_149159_h = data.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeFloat((float) this.field_149158_a);
        data.writeFloat((float) this.field_149156_b);
        data.writeFloat((float) this.field_149157_c);
        data.writeFloat(this.field_149154_d);
        data.writeInt(this.field_149155_e.size());
        int var2 = (int) this.field_149158_a;
        int var3 = (int) this.field_149156_b;
        int var4 = (int) this.field_149157_c;
        Iterator var5 = this.field_149155_e.iterator();

        while (var5.hasNext()) {
            BlockPos var6 = (BlockPos) var5.next();
            int var7 = var6.getX() - var2;
            int var8 = var6.getY() - var3;
            int var9 = var6.getZ() - var4;
            data.writeByte(var7);
            data.writeByte(var8);
            data.writeByte(var9);
        }

        data.writeFloat(this.field_149152_f);
        data.writeFloat(this.field_149153_g);
        data.writeFloat(this.field_149159_h);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleExplosion(this);
    }

    public float func_149149_c() {
        return this.field_149152_f;
    }

    public float func_149144_d() {
        return this.field_149153_g;
    }

    public float func_149147_e() {
        return this.field_149159_h;
    }

    public double func_149148_f() {
        return this.field_149158_a;
    }

    public double func_149143_g() {
        return this.field_149156_b;
    }

    public double func_149145_h() {
        return this.field_149157_c;
    }

    public float func_149146_i() {
        return this.field_149154_d;
    }

    public List func_149150_j() {
        return this.field_149155_e;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
