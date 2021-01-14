package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class S10PacketSpawnPainting implements Packet {
    private int field_148973_a;
    private BlockPos field_179838_b;
    private EnumFacing field_179839_c;
    private String field_148968_f;
    private static final String __OBFID = "CL_00001280";

    public S10PacketSpawnPainting() {
    }

    public S10PacketSpawnPainting(EntityPainting p_i45170_1_) {
        this.field_148973_a = p_i45170_1_.getEntityId();
        this.field_179838_b = p_i45170_1_.func_174857_n();
        this.field_179839_c = p_i45170_1_.field_174860_b;
        this.field_148968_f = p_i45170_1_.art.title;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_148973_a = data.readVarIntFromBuffer();
        this.field_148968_f = data.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
        this.field_179838_b = data.readBlockPos();
        this.field_179839_c = EnumFacing.getHorizontal(data.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_148973_a);
        data.writeString(this.field_148968_f);
        data.writeBlockPos(this.field_179838_b);
        data.writeByte(this.field_179839_c.getHorizontalIndex());
    }

    public void func_180722_a(INetHandlerPlayClient p_180722_1_) {
        p_180722_1_.handleSpawnPainting(this);
    }

    public int func_148965_c() {
        return this.field_148973_a;
    }

    public BlockPos func_179837_b() {
        return this.field_179838_b;
    }

    public EnumFacing func_179836_c() {
        return this.field_179839_c;
    }

    public String func_148961_h() {
        return this.field_148968_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180722_a((INetHandlerPlayClient) handler);
    }
}
