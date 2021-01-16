package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;

public class ItemAir extends Item
{
    private final Block field_190904_a;

    public ItemAir(Block p_i47264_1_)
    {
        this.field_190904_a = p_i47264_1_;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.field_190904_a.getUnlocalizedName();
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getUnlocalizedName()
    {
        return this.field_190904_a.getUnlocalizedName();
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        this.field_190904_a.func_190948_a(stack, playerIn, tooltip, advanced);
    }
}
