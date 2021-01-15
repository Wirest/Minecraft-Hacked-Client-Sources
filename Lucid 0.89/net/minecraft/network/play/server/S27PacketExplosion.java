package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion implements Packet
{
    private double field_149158_a;
    private double field_149156_b;
    private double field_149157_c;
    private float field_149154_d;
    private List field_149155_e;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;

    public S27PacketExplosion() {}

    public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_, double p_i45193_5_, float p_i45193_7_, List p_i45193_8_, Vec3 p_i45193_9_)
    {
        this.field_149158_a = p_i45193_1_;
        this.field_149156_b = p_i45193_3_;
        this.field_149157_c = p_i45193_5_;
        this.field_149154_d = p_i45193_7_;
        this.field_149155_e = Lists.newArrayList(p_i45193_8_);

        if (p_i45193_9_ != null)
        {
            this.field_149152_f = (float)p_i45193_9_.xCoord;
            this.field_149153_g = (float)p_i45193_9_.yCoord;
            this.field_149159_h = (float)p_i45193_9_.zCoord;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149158_a = buf.readFloat();
        this.field_149156_b = buf.readFloat();
        this.field_149157_c = buf.readFloat();
        this.field_149154_d = buf.readFloat();
        int var2 = buf.readInt();
        this.field_149155_e = Lists.newArrayListWithCapacity(var2);
        int var3 = (int)this.field_149158_a;
        int var4 = (int)this.field_149156_b;
        int var5 = (int)this.field_149157_c;

        for (int var6 = 0; var6 < var2; ++var6)
        {
            int var7 = buf.readByte() + var3;
            int var8 = buf.readByte() + var4;
            int var9 = buf.readByte() + var5;
            this.field_149155_e.add(new BlockPos(var7, var8, var9));
        }

        this.field_149152_f = buf.readFloat();
        this.field_149153_g = buf.readFloat();
        this.field_149159_h = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat((float)this.field_149158_a);
        buf.writeFloat((float)this.field_149156_b);
        buf.writeFloat((float)this.field_149157_c);
        buf.writeFloat(this.field_149154_d);
        buf.writeInt(this.field_149155_e.size());
        int var2 = (int)this.field_149158_a;
        int var3 = (int)this.field_149156_b;
        int var4 = (int)this.field_149157_c;
        Iterator var5 = this.field_149155_e.iterator();

        while (var5.hasNext())
        {
            BlockPos var6 = (BlockPos)var5.next();
            int var7 = var6.getX() - var2;
            int var8 = var6.getY() - var3;
            int var9 = var6.getZ() - var4;
            buf.writeByte(var7);
            buf.writeByte(var8);
            buf.writeByte(var9);
        }

        buf.writeFloat(this.field_149152_f);
        buf.writeFloat(this.field_149153_g);
        buf.writeFloat(this.field_149159_h);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleExplosion(this);
    }

    public float func_149149_c()
    {
        return this.field_149152_f;
    }

    public float func_149144_d()
    {
        return this.field_149153_g;
    }

    public float func_149147_e()
    {
        return this.field_149159_h;
    }

    public double func_149148_f()
    {
        return this.field_149158_a;
    }

    public double func_149143_g()
    {
        return this.field_149156_b;
    }

    public double func_149145_h()
    {
        return this.field_149157_c;
    }

    public float func_149146_i()
    {
        return this.field_149154_d;
    }

    public List func_149150_j()
    {
        return this.field_149155_e;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
