package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

import java.io.IOException;

public class C12PacketUpdateSign implements Packet {
    private BlockPos field_179723_a;
    private IChatComponent[] lines;
    private static final String __OBFID = "CL_00001370";

    public C12PacketUpdateSign() {
    }

    public C12PacketUpdateSign(BlockPos p_i45933_1_, IChatComponent[] p_i45933_2_) {
        this.field_179723_a = p_i45933_1_;
        this.lines = new IChatComponent[]{p_i45933_2_[0], p_i45933_2_[1], p_i45933_2_[2], p_i45933_2_[3]};
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179723_a = data.readBlockPos();
        this.lines = new IChatComponent[4];

        for (int var2 = 0; var2 < 4; ++var2) {
            this.lines[var2] = data.readChatComponent();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179723_a);

        for (int var2 = 0; var2 < 4; ++var2) {
            data.writeChatComponent(this.lines[var2]);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processUpdateSign(this);
    }

    public BlockPos func_179722_a() {
        return this.field_179723_a;
    }

    public IChatComponent[] func_180768_b() {
        return this.lines;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }
}
