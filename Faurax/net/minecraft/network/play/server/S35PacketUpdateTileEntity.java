package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S35PacketUpdateTileEntity implements Packet
{
    private BlockPos field_179824_a;

    /** Used only for vanilla tile entities */
    private int metadata;
    private NBTTagCompound nbt;
    private static final String __OBFID = "CL_00001285";

    public S35PacketUpdateTileEntity() {}

    public S35PacketUpdateTileEntity(BlockPos p_i45990_1_, int p_i45990_2_, NBTTagCompound p_i45990_3_)
    {
        this.field_179824_a = p_i45990_1_;
        this.metadata = p_i45990_2_;
        this.nbt = p_i45990_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_179824_a = data.readBlockPos();
        this.metadata = data.readUnsignedByte();
        this.nbt = data.readNBTTagCompoundFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeBlockPos(this.field_179824_a);
        data.writeByte((byte)this.metadata);
        data.writeNBTTagCompoundToBuffer(this.nbt);
    }

    public void func_180725_a(INetHandlerPlayClient p_180725_1_)
    {
        p_180725_1_.handleUpdateTileEntity(this);
    }

    public BlockPos func_179823_a()
    {
        return this.field_179824_a;
    }

    public int getTileEntityType()
    {
        return this.metadata;
    }

    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180725_a((INetHandlerPlayClient)handler);
    }
}
