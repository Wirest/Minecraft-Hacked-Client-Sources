package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S36PacketSignEditorOpen implements Packet {
    private BlockPos field_179778_a;
    private static final String __OBFID = "CL_00001316";

    public S36PacketSignEditorOpen() {
    }

    public S36PacketSignEditorOpen(BlockPos p_i45971_1_) {
        this.field_179778_a = p_i45971_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSignEditorOpen(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179778_a = data.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179778_a);
    }

    public BlockPos func_179777_a() {
        return this.field_179778_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
