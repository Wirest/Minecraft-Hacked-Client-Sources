package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2FPacketSetSlot implements Packet
{
    private int windowId;
    private int slot;
    private ItemStack item;
    

    public S2FPacketSetSlot() {}

    public S2FPacketSetSlot(int p_i45188_1_, int p_i45188_2_, ItemStack p_i45188_3_)
    {
        this.windowId = p_i45188_1_;
        this.slot = p_i45188_2_;
        this.item = p_i45188_3_ == null ? null : p_i45188_3_.copy();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSetSlot(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.windowId = data.readByte();
        this.slot = data.readShort();
        this.item = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.windowId);
        data.writeShort(this.slot);
        data.writeItemStackToBuffer(this.item);
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
