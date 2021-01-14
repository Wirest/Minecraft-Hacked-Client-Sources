package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
    private final Block field_150944_b;
    private String[] field_150945_c;
    private static final String __OBFID = "CL_00000003";

    public ItemColored(Block p_i45332_1_, boolean p_i45332_2_) {
        super(p_i45332_1_);
        field_150944_b = p_i45332_1_;

        if (p_i45332_2_) {
            setMaxDamage(0);
            setHasSubtypes(true);
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return field_150944_b.getRenderColor(field_150944_b.getStateFromMeta(stack.getMetadata()));
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be
     * placed in the world when this Item is placed as a Block (mostly used with
     * ItemBlocks).
     */
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public ItemColored func_150943_a(String[] p_150943_1_) {
        field_150945_c = p_150943_1_;
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (field_150945_c == null) {
            return super.getUnlocalizedName(stack);
        } else {
            int var2 = stack.getMetadata();
            return var2 >= 0 && var2 < field_150945_c.length ? super.getUnlocalizedName(stack) + "." + field_150945_c[var2] : super.getUnlocalizedName(stack);
        }
    }
}
