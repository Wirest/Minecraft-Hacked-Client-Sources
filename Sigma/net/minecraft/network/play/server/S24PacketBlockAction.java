package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S24PacketBlockAction implements Packet {
    private BlockPos field_179826_a;
    private int field_148872_d;
    private int field_148873_e;
    private Block field_148871_f;
    private static final String __OBFID = "CL_00001286";

    public S24PacketBlockAction() {
    }

    public S24PacketBlockAction(BlockPos p_i45989_1_, Block p_i45989_2_, int p_i45989_3_, int p_i45989_4_) {
        this.field_179826_a = p_i45989_1_;
        this.field_148872_d = p_i45989_3_;
        this.field_148873_e = p_i45989_4_;
        this.field_148871_f = p_i45989_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179826_a = data.readBlockPos();
        this.field_148872_d = data.readUnsignedByte();
        this.field_148873_e = data.readUnsignedByte();
        this.field_148871_f = Block.getBlockById(data.readVarIntFromBuffer() & 4095);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179826_a);
        data.writeByte(this.field_148872_d);
        data.writeByte(this.field_148873_e);
        data.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148871_f) & 4095);
    }

    public void func_180726_a(INetHandlerPlayClient p_180726_1_) {
        p_180726_1_.handleBlockAction(this);
    }

    public BlockPos func_179825_a() {
        return this.field_179826_a;
    }

    /**
     * instrument data for noteblocks
     */
    public int getData1() {
        return this.field_148872_d;
    }

    /**
     * pitch data for noteblocks
     */
    public int getData2() {
        return this.field_148873_e;
    }

    public Block getBlockType() {
        return this.field_148871_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180726_a((INetHandlerPlayClient) handler);
    }
}
