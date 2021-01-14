package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0DPacketCollectItem implements Packet {
    private int field_149357_a;
    private int field_149356_b;
    private static final String __OBFID = "CL_00001339";

    public S0DPacketCollectItem() {
    }

    public S0DPacketCollectItem(int p_i45232_1_, int p_i45232_2_) {
        this.field_149357_a = p_i45232_1_;
        this.field_149356_b = p_i45232_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149357_a = data.readVarIntFromBuffer();
        this.field_149356_b = data.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149357_a);
        data.writeVarIntToBuffer(this.field_149356_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleCollectItem(this);
    }

    public int func_149354_c() {
        return this.field_149357_a;
    }

    public int func_149353_d() {
        return this.field_149356_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
