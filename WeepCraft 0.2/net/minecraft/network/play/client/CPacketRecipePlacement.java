package net.minecraft.network.play.client;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketRecipePlacement implements Packet<INetHandlerPlayServer>
{
    private int field_192616_a;
    private short field_192617_b;
    private List<CPacketRecipePlacement.ItemMove> field_192618_c;
    private List<CPacketRecipePlacement.ItemMove> field_192619_d;

    public CPacketRecipePlacement()
    {
    }

    public CPacketRecipePlacement(int p_i47425_1_, List<CPacketRecipePlacement.ItemMove> p_i47425_2_, List<CPacketRecipePlacement.ItemMove> p_i47425_3_, short p_i47425_4_)
    {
        this.field_192616_a = p_i47425_1_;
        this.field_192617_b = p_i47425_4_;
        this.field_192618_c = p_i47425_2_;
        this.field_192619_d = p_i47425_3_;
    }

    public int func_192613_a()
    {
        return this.field_192616_a;
    }

    public short func_192614_b()
    {
        return this.field_192617_b;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_192616_a = buf.readByte();
        this.field_192617_b = buf.readShort();
        this.field_192618_c = this.func_192611_c(buf);
        this.field_192619_d = this.func_192611_c(buf);
    }

    private List<CPacketRecipePlacement.ItemMove> func_192611_c(PacketBuffer p_192611_1_) throws IOException
    {
        int i = p_192611_1_.readShort();
        List<CPacketRecipePlacement.ItemMove> list = Lists.<CPacketRecipePlacement.ItemMove>newArrayListWithCapacity(i);

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = p_192611_1_.readItemStackFromBuffer();
            byte b0 = p_192611_1_.readByte();
            byte b1 = p_192611_1_.readByte();
            list.add(new CPacketRecipePlacement.ItemMove(itemstack, b0, b1));
        }

        return list;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_192616_a);
        buf.writeShort(this.field_192617_b);
        this.func_192612_a(buf, this.field_192618_c);
        this.func_192612_a(buf, this.field_192619_d);
    }

    private void func_192612_a(PacketBuffer p_192612_1_, List<CPacketRecipePlacement.ItemMove> p_192612_2_)
    {
        p_192612_1_.writeShort(p_192612_2_.size());

        for (CPacketRecipePlacement.ItemMove cpacketrecipeplacement$itemmove : p_192612_2_)
        {
            p_192612_1_.writeItemStackToBuffer(cpacketrecipeplacement$itemmove.field_192673_a);
            p_192612_1_.writeByte(cpacketrecipeplacement$itemmove.field_192674_b);
            p_192612_1_.writeByte(cpacketrecipeplacement$itemmove.field_192675_c);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_191985_a(this);
    }

    public List<CPacketRecipePlacement.ItemMove> func_192615_c()
    {
        return this.field_192619_d;
    }

    public List<CPacketRecipePlacement.ItemMove> func_192610_d()
    {
        return this.field_192618_c;
    }

    public static class ItemMove
    {
        public ItemStack field_192673_a;
        public int field_192674_b;
        public int field_192675_c;

        public ItemMove(ItemStack p_i47401_1_, int p_i47401_2_, int p_i47401_3_)
        {
            this.field_192673_a = p_i47401_1_.copy();
            this.field_192674_b = p_i47401_2_;
            this.field_192675_c = p_i47401_3_;
        }
    }
}
