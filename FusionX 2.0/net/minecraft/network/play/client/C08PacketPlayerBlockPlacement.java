package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C08PacketPlayerBlockPlacement implements Packet
{
    private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
    private BlockPos field_179725_b;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";

    public C08PacketPlayerBlockPlacement() {}

    public C08PacketPlayerBlockPlacement(ItemStack p_i45930_1_)
    {
        this(field_179726_a, 255, p_i45930_1_, 0.0F, 0.0F, 0.0F);
    }

    public C08PacketPlayerBlockPlacement(BlockPos p_i45931_1_, int p_i45931_2_, ItemStack p_i45931_3_, float p_i45931_4_, float p_i45931_5_, float p_i45931_6_)
    {
        this.field_179725_b = p_i45931_1_;
        this.placedBlockDirection = p_i45931_2_;
        this.stack = p_i45931_3_ != null ? p_i45931_3_.copy() : null;
        this.facingX = p_i45931_4_;
        this.facingY = p_i45931_5_;
        this.facingZ = p_i45931_6_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_179725_b = data.readBlockPos();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = (float)data.readUnsignedByte() / 16.0F;
        this.facingY = (float)data.readUnsignedByte() / 16.0F;
        this.facingZ = (float)data.readUnsignedByte() / 16.0F;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeBlockPos(this.field_179725_b);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int)(this.facingX * 16.0F));
        data.writeByte((int)(this.facingY * 16.0F));
        data.writeByte((int)(this.facingZ * 16.0F));
    }

    public void func_180769_a(INetHandlerPlayServer p_180769_1_)
    {
        p_180769_1_.processPlayerBlockPlacement(this);
    }

    public BlockPos func_179724_a()
    {
        return this.field_179725_b;
    }

    public int getPlacedBlockDirection()
    {
        return this.placedBlockDirection;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    /**
     * Returns the offset from xPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetX()
    {
        return this.facingX;
    }

    /**
     * Returns the offset from yPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetY()
    {
        return this.facingY;
    }

    /**
     * Returns the offset from zPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetZ()
    {
        return this.facingZ;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180769_a((INetHandlerPlayServer)handler);
    }
}
