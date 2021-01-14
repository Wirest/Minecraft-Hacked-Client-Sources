package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock {
    private static final String __OBFID = "CL_00000054";

    public ItemPiston(Block p_i45348_1_) {
        super(p_i45348_1_);
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage) {
        return 7;
    }
}
