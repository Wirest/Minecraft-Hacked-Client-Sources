package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S11PacketSpawnExperienceOrb implements Packet
{
    private int field_148992_a;
    private int field_148990_b;
    private int field_148991_c;
    private int field_148988_d;
    private int field_148989_e;
    private static final String __OBFID = "CL_00001277";

    public S11PacketSpawnExperienceOrb() {}

    public S11PacketSpawnExperienceOrb(EntityXPOrb p_i45167_1_)
    {
        this.field_148992_a = p_i45167_1_.getEntityId();
        this.field_148990_b = MathHelper.floor_double(p_i45167_1_.posX * 32.0D);
        this.field_148991_c = MathHelper.floor_double(p_i45167_1_.posY * 32.0D);
        this.field_148988_d = MathHelper.floor_double(p_i45167_1_.posZ * 32.0D);
        this.field_148989_e = p_i45167_1_.getXpValue();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148992_a = data.readVarIntFromBuffer();
        this.field_148990_b = data.readInt();
        this.field_148991_c = data.readInt();
        this.field_148988_d = data.readInt();
        this.field_148989_e = data.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.field_148992_a);
        data.writeInt(this.field_148990_b);
        data.writeInt(this.field_148991_c);
        data.writeInt(this.field_148988_d);
        data.writeShort(this.field_148989_e);
    }

    public void func_180719_a(INetHandlerPlayClient p_180719_1_)
    {
        p_180719_1_.handleSpawnExperienceOrb(this);
    }

    public int func_148985_c()
    {
        return this.field_148992_a;
    }

    public int func_148984_d()
    {
        return this.field_148990_b;
    }

    public int func_148983_e()
    {
        return this.field_148991_c;
    }

    public int func_148982_f()
    {
        return this.field_148988_d;
    }

    public int func_148986_g()
    {
        return this.field_148989_e;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180719_a((INetHandlerPlayClient)handler);
    }
}
