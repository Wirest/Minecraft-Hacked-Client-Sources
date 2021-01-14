package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C11PacketEnchantItem implements Packet {
    private int id;
    private int button;
    private static final String __OBFID = "CL_00001352";

    public C11PacketEnchantItem() {
    }

    public C11PacketEnchantItem(int p_i45245_1_, int p_i45245_2_) {
        this.id = p_i45245_1_;
        this.button = p_i45245_2_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processEnchantItem(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.id = data.readByte();
        this.button = data.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.id);
        data.writeByte(this.button);
    }

    public int getId() {
        return this.id;
    }

    public int getButton() {
        return this.button;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }
}
