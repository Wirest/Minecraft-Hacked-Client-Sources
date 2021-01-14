package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

import java.io.IOException;

public class S43PacketCamera implements Packet {
    public int field_179781_a;
    private static final String __OBFID = "CL_00002289";

    public S43PacketCamera() {
    }

    public S43PacketCamera(Entity p_i45960_1_) {
        this.field_179781_a = p_i45960_1_.getEntityId();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179781_a = data.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_179781_a);
    }

    public void func_179779_a(INetHandlerPlayClient p_179779_1_) {
        p_179779_1_.func_175094_a(this);
    }

    public Entity func_179780_a(World worldIn) {
        return worldIn.getEntityByID(this.field_179781_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_179779_a((INetHandlerPlayClient) handler);
    }
}
