// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import com.google.common.collect.Lists;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S27PacketExplosion implements Packet<INetHandlerPlayClient>
{
    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;
    
    public S27PacketExplosion() {
    }
    
    public S27PacketExplosion(final double p_i45193_1_, final double y, final double z, final float strengthIn, final List<BlockPos> affectedBlocksIn, final Vec3 p_i45193_9_) {
        this.posX = p_i45193_1_;
        this.posY = y;
        this.posZ = z;
        this.strength = strengthIn;
        this.affectedBlockPositions = (List<BlockPos>)Lists.newArrayList((Iterable<?>)affectedBlocksIn);
        if (p_i45193_9_ != null) {
            this.field_149152_f = (float)p_i45193_9_.xCoord;
            this.field_149153_g = (float)p_i45193_9_.yCoord;
            this.field_149159_h = (float)p_i45193_9_.zCoord;
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.posX = buf.readFloat();
        this.posY = buf.readFloat();
        this.posZ = buf.readFloat();
        this.strength = buf.readFloat();
        final int i = buf.readInt();
        this.affectedBlockPositions = (List<BlockPos>)Lists.newArrayListWithCapacity(i);
        final int j = (int)this.posX;
        final int k = (int)this.posY;
        final int l = (int)this.posZ;
        for (int i2 = 0; i2 < i; ++i2) {
            final int j2 = buf.readByte() + j;
            final int k2 = buf.readByte() + k;
            final int l2 = buf.readByte() + l;
            this.affectedBlockPositions.add(new BlockPos(j2, k2, l2));
        }
        this.field_149152_f = buf.readFloat();
        this.field_149153_g = buf.readFloat();
        this.field_149159_h = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeFloat((float)this.posX);
        buf.writeFloat((float)this.posY);
        buf.writeFloat((float)this.posZ);
        buf.writeFloat(this.strength);
        buf.writeInt(this.affectedBlockPositions.size());
        final int i = (int)this.posX;
        final int j = (int)this.posY;
        final int k = (int)this.posZ;
        for (final BlockPos blockpos : this.affectedBlockPositions) {
            final int l = blockpos.getX() - i;
            final int i2 = blockpos.getY() - j;
            final int j2 = blockpos.getZ() - k;
            buf.writeByte(l);
            buf.writeByte(i2);
            buf.writeByte(j2);
        }
        buf.writeFloat(this.field_149152_f);
        buf.writeFloat(this.field_149153_g);
        buf.writeFloat(this.field_149159_h);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleExplosion(this);
    }
    
    public float func_149149_c() {
        return this.field_149152_f;
    }
    
    public float func_149144_d() {
        return this.field_149153_g;
    }
    
    public float func_149147_e() {
        return this.field_149159_h;
    }
    
    public double getX() {
        return this.posX;
    }
    
    public double getY() {
        return this.posY;
    }
    
    public double getZ() {
        return this.posZ;
    }
    
    public float getStrength() {
        return this.strength;
    }
    
    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}
