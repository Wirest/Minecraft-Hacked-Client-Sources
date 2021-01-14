package net.minecraft.inventory;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container {
    /**
     * SlotEnchantmentTable object with ItemStack to be enchanted
     */
    public IInventory tableInventory;

    /**
     * current world (for bookshelf counting)
     */
    private World worldPointer;
    private BlockPos field_178150_j;
    private Random rand;
    public int field_178149_f;

    /**
     * 3-member array storing the enchantment levels of each slot
     */
    public int[] enchantLevels;
    public int[] field_178151_h;
    private static final String __OBFID = "CL_00001745";

    public ContainerEnchantment(InventoryPlayer p_i45797_1_, World worldIn) {
        this(p_i45797_1_, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantment(InventoryPlayer p_i45798_1_, World worldIn, BlockPos p_i45798_3_) {
        tableInventory = new InventoryBasic("Enchant", true, 2) {
            private static final String __OBFID = "CL_00001746";

            @Override
            public int getInventoryStackLimit() {
                return 64;
            }

            @Override
            public void markDirty() {
                super.markDirty();
                ContainerEnchantment.this.onCraftMatrixChanged(this);
            }
        };
        rand = new Random();
        enchantLevels = new int[3];
        field_178151_h = new int[]{-1, -1, -1};
        worldPointer = worldIn;
        field_178150_j = p_i45798_3_;
        field_178149_f = p_i45798_1_.player.func_175138_ci();
        addSlotToContainer(new Slot(tableInventory, 0, 15, 47) {
            private static final String __OBFID = "CL_00001747";

            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        addSlotToContainer(new Slot(tableInventory, 1, 35, 47) {
            private static final String __OBFID = "CL_00002185";

            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.dye && EnumDyeColor.func_176766_a(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        int var4;

        for (var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 9; ++var5) {
                addSlotToContainer(new Slot(p_i45798_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4) {
            addSlotToContainer(new Slot(p_i45798_1_, var4, 8 + var4 * 18, 142));
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, enchantLevels[0]);
        p_75132_1_.sendProgressBarUpdate(this, 1, enchantLevels[1]);
        p_75132_1_.sendProgressBarUpdate(this, 2, enchantLevels[2]);
        p_75132_1_.sendProgressBarUpdate(this, 3, field_178149_f & -16);
        p_75132_1_.sendProgressBarUpdate(this, 4, field_178151_h[0]);
        p_75132_1_.sendProgressBarUpdate(this, 5, field_178151_h[1]);
        p_75132_1_.sendProgressBarUpdate(this, 6, field_178151_h[2]);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) crafters.get(var1);
            var2.sendProgressBarUpdate(this, 0, enchantLevels[0]);
            var2.sendProgressBarUpdate(this, 1, enchantLevels[1]);
            var2.sendProgressBarUpdate(this, 2, enchantLevels[2]);
            var2.sendProgressBarUpdate(this, 3, field_178149_f & -16);
            var2.sendProgressBarUpdate(this, 4, field_178151_h[0]);
            var2.sendProgressBarUpdate(this, 5, field_178151_h[1]);
            var2.sendProgressBarUpdate(this, 6, field_178151_h[2]);
        }
    }

    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ >= 0 && p_75137_1_ <= 2) {
            enchantLevels[p_75137_1_] = p_75137_2_;
        } else if (p_75137_1_ == 3) {
            field_178149_f = p_75137_2_;
        } else if (p_75137_1_ >= 4 && p_75137_1_ <= 6) {
            field_178151_h[p_75137_1_ - 4] = p_75137_2_;
        } else {
            super.updateProgressBar(p_75137_1_, p_75137_2_);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        if (p_75130_1_ == tableInventory) {
            ItemStack var2 = p_75130_1_.getStackInSlot(0);
            int var3;

            if (var2 != null && var2.isItemEnchantable()) {
                if (!worldPointer.isRemote) {
                    var3 = 0;
                    int var4;

                    for (var4 = -1; var4 <= 1; ++var4) {
                        for (int var5 = -1; var5 <= 1; ++var5) {
                            if ((var4 != 0 || var5 != 0) && worldPointer.isAirBlock(field_178150_j.add(var5, 0, var4)) && worldPointer.isAirBlock(field_178150_j.add(var5, 1, var4))) {
                                if (worldPointer.getBlockState(field_178150_j.add(var5 * 2, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++var3;
                                }

                                if (worldPointer.getBlockState(field_178150_j.add(var5 * 2, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++var3;
                                }

                                if (var5 != 0 && var4 != 0) {
                                    if (worldPointer.getBlockState(field_178150_j.add(var5 * 2, 0, var4)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }

                                    if (worldPointer.getBlockState(field_178150_j.add(var5 * 2, 1, var4)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }

                                    if (worldPointer.getBlockState(field_178150_j.add(var5, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }

                                    if (worldPointer.getBlockState(field_178150_j.add(var5, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }
                                }
                            }
                        }
                    }

                    rand.setSeed(field_178149_f);

                    for (var4 = 0; var4 < 3; ++var4) {
                        enchantLevels[var4] = EnchantmentHelper.calcItemStackEnchantability(rand, var4, var3, var2);
                        field_178151_h[var4] = -1;

                        if (enchantLevels[var4] < var4 + 1) {
                            enchantLevels[var4] = 0;
                        }
                    }

                    for (var4 = 0; var4 < 3; ++var4) {
                        if (enchantLevels[var4] > 0) {
                            List var7 = func_178148_a(var2, var4, enchantLevels[var4]);

                            if (var7 != null && !var7.isEmpty()) {
                                EnchantmentData var6 = (EnchantmentData) var7.get(rand.nextInt(var7.size()));
                                field_178151_h[var4] = var6.enchantmentobj.effectId | var6.enchantmentLevel << 8;
                            }
                        }
                    }

                    detectAndSendChanges();
                }
            } else {
                for (var3 = 0; var3 < 3; ++var3) {
                    enchantLevels[var3] = 0;
                    field_178151_h[var3] = -1;
                }
            }
        }
    }

    /**
     * enchants the item on the table using the specified slot; also deducts XP
     * from player
     */
    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        ItemStack var3 = tableInventory.getStackInSlot(0);
        ItemStack var4 = tableInventory.getStackInSlot(1);
        int var5 = id + 1;

        if ((var4 == null || var4.stackSize < var5) && !playerIn.capabilities.isCreativeMode) {
            return false;
        } else if (enchantLevels[id] > 0 && var3 != null && (playerIn.experienceLevel >= var5 && playerIn.experienceLevel >= enchantLevels[id] || playerIn.capabilities.isCreativeMode)) {
            if (!worldPointer.isRemote) {
                List var6 = func_178148_a(var3, id, enchantLevels[id]);
                boolean var7 = var3.getItem() == Items.book;

                if (var6 != null) {
                    playerIn.func_71013_b(var5);

                    if (var7) {
                        var3.setItem(Items.enchanted_book);
                    }

                    for (int var8 = 0; var8 < var6.size(); ++var8) {
                        EnchantmentData var9 = (EnchantmentData) var6.get(var8);

                        if (var7) {
                            Items.enchanted_book.addEnchantment(var3, var9);
                        } else {
                            var3.addEnchantment(var9.enchantmentobj, var9.enchantmentLevel);
                        }
                    }

                    if (!playerIn.capabilities.isCreativeMode) {
                        var4.stackSize -= var5;

                        if (var4.stackSize <= 0) {
                            tableInventory.setInventorySlotContents(1, (ItemStack) null);
                        }
                    }

                    tableInventory.markDirty();
                    field_178149_f = playerIn.func_175138_ci();
                    onCraftMatrixChanged(tableInventory);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private List func_178148_a(ItemStack p_178148_1_, int p_178148_2_, int p_178148_3_) {
        rand.setSeed(field_178149_f + p_178148_2_);
        List var4 = EnchantmentHelper.buildEnchantmentList(rand, p_178148_1_, p_178148_3_);

        if (p_178148_1_.getItem() == Items.book && var4 != null && var4.size() > 1) {
            var4.remove(rand.nextInt(var4.size()));
        }

        return var4;
    }

    public int func_178147_e() {
        ItemStack var1 = tableInventory.getStackInSlot(1);
        return var1 == null ? 0 : var1.stackSize;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);

        if (!worldPointer.isRemote) {
            for (int var2 = 0; var2 < tableInventory.getSizeInventory(); ++var2) {
                ItemStack var3 = tableInventory.getStackInSlotOnClosing(var2);

                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return worldPointer.getBlockState(field_178150_j).getBlock() != Blocks.enchanting_table ? false : playerIn.getDistanceSq(field_178150_j.getX() + 0.5D, field_178150_j.getY() + 0.5D, field_178150_j.getZ() + 0.5D) <= 64.0D;
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

            if (index == 0) {
                if (!mergeItemStack(var5, 2, 38, true)) {
                    return null;
                }
            } else if (index == 1) {
                if (!mergeItemStack(var5, 2, 38, true)) {
                    return null;
                }
            } else if (var5.getItem() == Items.dye && EnumDyeColor.func_176766_a(var5.getMetadata()) == EnumDyeColor.BLUE) {
                if (!mergeItemStack(var5, 1, 2, true)) {
                    return null;
                }
            } else {
                if (((Slot) inventorySlots.get(0)).getHasStack() || !((Slot) inventorySlots.get(0)).isItemValid(var5)) {
                    return null;
                }

                if (var5.hasTagCompound() && var5.stackSize == 1) {
                    ((Slot) inventorySlots.get(0)).putStack(var5.copy());
                    var5.stackSize = 0;
                } else if (var5.stackSize >= 1) {
                    ((Slot) inventorySlots.get(0)).putStack(new ItemStack(var5.getItem(), 1, var5.getMetadata()));
                    --var5.stackSize;
                }
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
}
