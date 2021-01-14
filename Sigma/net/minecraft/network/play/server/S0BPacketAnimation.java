package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0BPacketAnimation implements Packet {
    private int entityId;
    private int type;
    private static final String __OBFID = "CL_00001282";

    public S0BPacketAnimation() {
    }

    public S0BPacketAnimation(Entity ent, int animationType) {
        this.entityId = ent.getEntityId();
        this.type = animationType;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.entityId = data.readVarIntFromBuffer();
        this.type = data.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.entityId);
        data.writeByte(this.type);
    }

    public void func_180723_a(INetHandlerPlayClient p_180723_1_) {
        p_180723_1_.handleAnimation(this);
    }

    public int func_148978_c() {
        return this.entityId;
    }

    public int func_148977_d() {
        return this.type;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180723_a((INetHandlerPlayClient) handler);
    }
}
