package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class C07PacketPlayerDigging implements Packet
{
    private BlockPos position;
    private EnumFacing facing;

    /** Status of the digging (started, ongoing, broken). */
    private C07PacketPlayerDigging.Action status;

    public C07PacketPlayerDigging() {}

    public C07PacketPlayerDigging(C07PacketPlayerDigging.Action statusIn, BlockPos posIn, EnumFacing facingIn)
    {
        this.status = statusIn;
        this.position = posIn;
        this.facing = facingIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.status = (C07PacketPlayerDigging.Action)buf.readEnumValue(C07PacketPlayerDigging.Action.class);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.getFront(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.status);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getIndex());
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerDigging(this);
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public C07PacketPlayerDigging.Action getStatus()
    {
        return this.status;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static enum Action
    {
        START_DESTROY_BLOCK("START_DESTROY_BLOCK", 0),
        ABORT_DESTROY_BLOCK("ABORT_DESTROY_BLOCK", 1),
        STOP_DESTROY_BLOCK("STOP_DESTROY_BLOCK", 2),
        DROP_ALL_ITEMS("DROP_ALL_ITEMS", 3),
        DROP_ITEM("DROP_ITEM", 4),
        RELEASE_USE_ITEM("RELEASE_USE_ITEM", 5); 

        private Action(String p_i45939_1_, int p_i45939_2_) {}
    }
}
