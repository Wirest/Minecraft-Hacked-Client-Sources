package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S46PacketSetCompressionLevel implements Packet {
    private int field_179761_a;
    private static final String __OBFID = "CL_00002300";

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179761_a = data.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_179761_a);
    }

    public void func_179759_a(INetHandlerPlayClient p_179759_1_) {
        p_179759_1_.func_175100_a(this);
    }

    public int func_179760_a() {
        return this.field_179761_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_179759_a((INetHandlerPlayClient) handler);
    }
}
