package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer>
{
    private CPacketRecipeInfo.Purpose field_194157_a;
    private IRecipe field_193649_d;
    private boolean field_192631_e;
    private boolean field_192632_f;

    public CPacketRecipeInfo()
    {
    }

    public CPacketRecipeInfo(IRecipe p_i47518_1_)
    {
        this.field_194157_a = CPacketRecipeInfo.Purpose.SHOWN;
        this.field_193649_d = p_i47518_1_;
    }

    public CPacketRecipeInfo(boolean p_i47424_1_, boolean p_i47424_2_)
    {
        this.field_194157_a = CPacketRecipeInfo.Purpose.SETTINGS;
        this.field_192631_e = p_i47424_1_;
        this.field_192632_f = p_i47424_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_194157_a = (CPacketRecipeInfo.Purpose)buf.readEnumValue(CPacketRecipeInfo.Purpose.class);

        if (this.field_194157_a == CPacketRecipeInfo.Purpose.SHOWN)
        {
            this.field_193649_d = CraftingManager.func_193374_a(buf.readInt());
        }
        else if (this.field_194157_a == CPacketRecipeInfo.Purpose.SETTINGS)
        {
            this.field_192631_e = buf.readBoolean();
            this.field_192632_f = buf.readBoolean();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.field_194157_a);

        if (this.field_194157_a == CPacketRecipeInfo.Purpose.SHOWN)
        {
            buf.writeInt(CraftingManager.func_193375_a(this.field_193649_d));
        }
        else if (this.field_194157_a == CPacketRecipeInfo.Purpose.SETTINGS)
        {
            buf.writeBoolean(this.field_192631_e);
            buf.writeBoolean(this.field_192632_f);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_191984_a(this);
    }

    public CPacketRecipeInfo.Purpose func_194156_a()
    {
        return this.field_194157_a;
    }

    public IRecipe func_193648_b()
    {
        return this.field_193649_d;
    }

    public boolean func_192624_c()
    {
        return this.field_192631_e;
    }

    public boolean func_192625_d()
    {
        return this.field_192632_f;
    }

    public static enum Purpose
    {
        SHOWN,
        SETTINGS;
    }
}
