package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMerchant extends Container {
    /**
     * Instance of Merchant.
     */
    private IMerchant theMerchant;
    private InventoryMerchant merchantInventory;

    /**
     * Instance of World.
     */
    private final World theWorld;
    private static final String __OBFID = "CL_00001757";

    public ContainerMerchant(InventoryPlayer p_i1821_1_, IMerchant p_i1821_2_, World worldIn) {
        theMerchant = p_i1821_2_;
        theWorld = worldIn;
        merchantInventory = new InventoryMerchant(p_i1821_1_.player, p_i1821_2_);
        addSlotToContainer(new Slot(merchantInventory, 0, 36, 53));
        addSlotToContainer(new Slot(merchantInventory, 1, 62, 53));
        addSlotToContainer(new SlotMerchantResult(p_i1821_1_.player, p_i1821_2_, merchantInventory, 2, 120, 53));
        int var4;

        for (var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 9; ++var5) {
                addSlotToContainer(new Slot(p_i1821_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4) {
            addSlotToContainer(new Slot(p_i1821_1_, var4, 8 + var4 * 18, 142));
        }
    }

    public InventoryMerchant getMerchantInventory() {
        return merchantInventory;
    }

    @Override
    public void onCraftGuiOpened(ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        merchantInventory.resetRecipeAndSlots();
        super.onCraftMatrixChanged(p_75130_1_);
    }

    public void setCurrentRecipeIndex(int p_75175_1_) {
        merchantInventory.setCurrentRecipeIndex(p_75175_1_);
    }

    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return theMerchant.getCustomer() == playerIn;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot) inventorySlots.get(index);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index == 2) {
                if (!mergeItemStack(var5, 3, 39, true)) {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            } else if (index != 0 && index != 1) {
                if (index >= 3 && index < 30) {
                    if (!mergeItemStack(var5, 30, 39, false)) {
                        return null;
                    }
                } else if (index >= 30 && index < 39 && !mergeItemStack(var5, 3, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(var5, 3, 39, false)) {
                return null;
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

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        theMerchant.setCustomer((EntityPlayer) null);
        super.onContainerClosed(p_75134_1_);

        if (!theWorld.isRemote) {
            ItemStack var2 = merchantInventory.getStackInSlotOnClosing(0);

            if (var2 != null) {
                p_75134_1_.dropPlayerItemWithRandomChoice(var2, false);
            }

            var2 = merchantInventory.getStackInSlotOnClosing(1);

            if (var2 != null) {
                p_75134_1_.dropPlayerItemWithRandomChoice(var2, false);
            }
        }
    }
}
