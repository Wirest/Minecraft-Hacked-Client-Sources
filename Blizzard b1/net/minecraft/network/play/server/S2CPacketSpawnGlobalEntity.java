package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S2CPacketSpawnGlobalEntity implements Packet
{
    private int field_149059_a;
    private int field_149057_b;
    private int field_149058_c;
    private int field_149055_d;
    private int field_149056_e;
    private static final String __OBFID = "CL_00001278";

    public S2CPacketSpawnGlobalEntity() {}

    public S2CPacketSpawnGlobalEntity(Entity p_i45191_1_)
    {
        this.field_149059_a = p_i45191_1_.getEntityId();
        this.field_149057_b = MathHelper.floor_double(p_i45191_1_.posX * 32.0D);
        this.field_149058_c = MathHelper.floor_double(p_i45191_1_.posY * 32.0D);
        this.field_149055_d = MathHelper.floor_double(p_i45191_1_.posZ * 32.0D);

        if (p_i45191_1_ instanceof EntityLightningBolt)
        {
            this.field_149056_e = 1;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149059_a = data.readVarIntFromBuffer();
        this.field_149056_e = data.readByte();
        this.field_149057_b = data.readInt();
        this.field_149058_c = data.readInt();
        this.field_149055_d = data.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.field_149059_a);
        data.writeByte(this.field_149056_e);
        data.writeInt(this.field_149057_b);
        data.writeInt(this.field_149058_c);
        data.writeInt(this.field_149055_d);
    }

    public void func_180720_a(INetHandlerPlayClient p_180720_1_)
    {
        p_180720_1_.handleSpawnGlobalEntity(this);
    }

    public int func_149052_c()
    {
        return this.field_149059_a;
    }

    public int func_149051_d()
    {
        return this.field_149057_b;
    }

    public int func_149050_e()
    {
        return this.field_149058_c;
    }

    public int func_149049_f()
    {
        return this.field_149055_d;
    }

    public int func_149053_g()
    {
        return this.field_149056_e;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180720_a((INetHandlerPlayClient)handler);
    }
}
