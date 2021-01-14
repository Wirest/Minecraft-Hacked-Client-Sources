package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S49PacketUpdateEntityNBT implements Packet {
    private int field_179766_a;
    private NBTTagCompound field_179765_b;
    private static final String __OBFID = "CL_00002301";

    public S49PacketUpdateEntityNBT() {
    }

    public S49PacketUpdateEntityNBT(int p_i45979_1_, NBTTagCompound p_i45979_2_) {
        this.field_179766_a = p_i45979_1_;
        this.field_179765_b = p_i45979_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179766_a = data.readVarIntFromBuffer();
        this.field_179765_b = data.readNBTTagCompoundFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_179766_a);
        data.writeNBTTagCompoundToBuffer(this.field_179765_b);
    }

    public void func_179762_a(INetHandlerPlayClient p_179762_1_) {
        p_179762_1_.func_175097_a(this);
    }

    public NBTTagCompound func_179763_a() {
        return this.field_179765_b;
    }

    public Entity func_179764_a(World worldIn) {
        return worldIn.getEntityByID(this.field_179766_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_179762_a((INetHandlerPlayClient) handler);
    }
}
