package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem
{
    IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem()
    {
        private static final String __OBFID = "CL_00001200";
        public ItemStack dispense(IBlockSource source, ItemStack stack)
        {
            return stack;
        }
    };

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    ItemStack dispense(IBlockSource var1, ItemStack var2);
}
