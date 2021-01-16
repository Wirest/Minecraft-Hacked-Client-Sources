package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemStack;

public class SlotShulkerBox extends Slot
{
    public SlotShulkerBox(IInventory p_i47265_1_, int p_i47265_2_, int p_i47265_3_, int p_i47265_4_)
    {
        super(p_i47265_1_, p_i47265_2_, p_i47265_3_, p_i47265_4_);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
    }
}
