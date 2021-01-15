package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock
{
    protected final Block theBlock;
    protected final Function nameFunction;
    private static final String __OBFID = "CL_00000051";

    public ItemMultiTexture(Block p_i45784_1_, Block p_i45784_2_, Function p_i45784_3_)
    {
        super(p_i45784_1_);
        this.theBlock = p_i45784_2_;
        this.nameFunction = p_i45784_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public ItemMultiTexture(Block p_i45346_1_, Block p_i45346_2_, final String[] p_i45346_3_)
    {
        this(p_i45346_1_, p_i45346_2_, new Function()
        {
            private static final String __OBFID = "CL_00002161";
            public String apply(ItemStack p_179541_1_)
            {
                int var2 = p_179541_1_.getMetadata();

                if (var2 < 0 || var2 >= p_i45346_3_.length)
                {
                    var2 = 0;
                }

                return p_i45346_3_[var2];
            }
            public Object apply(Object p_apply_1_)
            {
                return this.apply((ItemStack)p_apply_1_);
            }
        });
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + (String)this.nameFunction.apply(stack);
    }
}
