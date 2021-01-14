package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class ContainerBrewingStand extends Container {
    private IInventory tileBrewingStand;

    /**
     * Instance of Slot.
     */
    private final Slot theSlot;
    private int brewTime;
    private static final String __OBFID = "CL_00001737";

    public ContainerBrewingStand(InventoryPlayer p_i45802_1_, IInventory p_i45802_2_) {
        this.tileBrewingStand = p_i45802_2_;
        this.addSlotToContainer(new ContainerBrewingStand.Potion(p_i45802_1_.player, p_i45802_2_, 0, 56, 46));
        this.addSlotToContainer(new ContainerBrewingStand.Potion(p_i45802_1_.player, p_i45802_2_, 1, 79, 53));
        this.addSlotToContainer(new ContainerBrewingStand.Potion(p_i45802_1_.player, p_i45802_2_, 2, 102, 46));
        this.theSlot = this.addSlotToContainer(new ContainerBrewingStand.Ingredient(p_i45802_2_, 3, 79, 17));
        int var3;

        for (var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot(p_i45802_1_, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(new Slot(p_i45802_1_, var3, 8 + var3 * 18, 142));
        }
    }

    public void onCraftGuiOpened(ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.func_175173_a(this, this.tileBrewingStand);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.brewTime != this.tileBrewingStand.getField(0)) {
                var2.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }
        }

        this.brewTime = this.tileBrewingStand.getField(0);
    }

    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        this.tileBrewingStand.setField(p_75137_1_, p_75137_2_);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBrewingStand.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot) this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if ((index < 0 || index > 2) && index != 3) {
                if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(var5)) {
                    if (!this.mergeItemStack(var5, 3, 4, false)) {
                        return null;
                    }
                } else if (ContainerBrewingStand.Potion.canHoldPotion(var3)) {
                    if (!this.mergeItemStack(var5, 0, 3, false)) {
                        return null;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(var5, 31, 40, false)) {
                        return null;
                    }
                } else if (index >= 31 && index < 40) {
                    if (!this.mergeItemStack(var5, 4, 31, false)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(var5, 4, 40, false)) {
                    return null;
                }
            } else {
                if (!this.mergeItemStack(var5, 4, 40, true)) {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }

            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize) {
                return null;
            }

            var4.onPickupFromSlot(playerIn, var5);
        }

        return var3;
    }

    class Ingredient extends Slot {
        private static final String __OBFID = "CL_00001738";

        public Ingredient(IInventory p_i1803_2_, int p_i1803_3_, int p_i1803_4_, int p_i1803_5_) {
            super(p_i1803_2_, p_i1803_3_, p_i1803_4_, p_i1803_5_);
        }

        public boolean isItemValid(ItemStack stack) {
            return stack != null ? stack.getItem().isPotionIngredient(stack) : false;
        }

        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Potion extends Slot {
        private EntityPlayer player;
        private static final String __OBFID = "CL_00001740";

        public Potion(EntityPlayer p_i1804_1_, IInventory p_i1804_2_, int p_i1804_3_, int p_i1804_4_, int p_i1804_5_) {
            super(p_i1804_2_, p_i1804_3_, p_i1804_4_, p_i1804_5_);
            this.player = p_i1804_1_;
        }

        public boolean isItemValid(ItemStack stack) {
            return canHoldPotion(stack);
        }

        public int getSlotStackLimit() {
            return 1;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
            if (stack.getItem() == Items.potionitem && stack.getMetadata() > 0) {
                this.player.triggerAchievement(AchievementList.potion);
            }

            super.onPickupFromSlot(playerIn, stack);
        }

        public static boolean canHoldPotion(ItemStack p_75243_0_) {
            return p_75243_0_ != null && (p_75243_0_.getItem() == Items.potionitem || p_75243_0_.getItem() == Items.glass_bottle);
        }
    }
}
