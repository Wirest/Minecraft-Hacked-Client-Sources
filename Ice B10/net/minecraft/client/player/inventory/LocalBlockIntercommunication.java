package net.minecraft.client.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;

public class LocalBlockIntercommunication implements IInteractionObject
{
    private String field_175126_a;
    private IChatComponent field_175125_b;
    private static final String __OBFID = "CL_00002571";

    public LocalBlockIntercommunication(String p_i46277_1_, IChatComponent p_i46277_2_)
    {
        this.field_175126_a = p_i46277_1_;
        this.field_175125_b = p_i46277_2_;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return this.field_175125_b.getUnformattedText();
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return true;
    }

    public String getGuiID()
    {
        return this.field_175126_a;
    }

    public IChatComponent getDisplayName()
    {
        return this.field_175125_b;
    }
}
