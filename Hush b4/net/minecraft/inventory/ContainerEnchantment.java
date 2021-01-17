// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container
{
    public IInventory tableInventory;
    private World worldPointer;
    private BlockPos position;
    private Random rand;
    public int xpSeed;
    public int[] enchantLevels;
    public int[] field_178151_h;
    
    public ContainerEnchantment(final InventoryPlayer playerInv, final World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }
    
    public ContainerEnchantment(final InventoryPlayer playerInv, final World worldIn, final BlockPos pos) {
        this.tableInventory = new InventoryBasic("Enchant", true, 2) {
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
        this.rand = new Random();
        this.enchantLevels = new int[3];
        this.field_178151_h = new int[] { -1, -1, -1 };
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return true;
            }
            
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        listener.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
        listener.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
        listener.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
        listener.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            final ICrafting icrafting = this.crafters.get(i);
            icrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            icrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            icrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            icrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
            icrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            icrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            icrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
        }
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        if (id >= 0 && id <= 2) {
            this.enchantLevels[id] = data;
        }
        else if (id == 3) {
            this.xpSeed = data;
        }
        else if (id >= 4 && id <= 6) {
            this.field_178151_h[id - 4] = data;
        }
        else {
            super.updateProgressBar(id, data);
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            final ItemStack itemstack = inventoryIn.getStackInSlot(0);
            if (itemstack != null && itemstack.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int l = 0;
                    for (int j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.worldPointer.isAirBlock(this.position.add(k, 0, j)) && this.worldPointer.isAirBlock(this.position.add(k, 1, j))) {
                                if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j * 2)).getBlock() == Blocks.bookshelf) {
                                    ++l;
                                }
                                if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j * 2)).getBlock() == Blocks.bookshelf) {
                                    ++l;
                                }
                                if (k != 0 && j != 0) {
                                    if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j)).getBlock() == Blocks.bookshelf) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j)).getBlock() == Blocks.bookshelf) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k, 0, j * 2)).getBlock() == Blocks.bookshelf) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k, 1, j * 2)).getBlock() == Blocks.bookshelf) {
                                        ++l;
                                    }
                                }
                            }
                        }
                    }
                    this.rand.setSeed(this.xpSeed);
                    for (int i1 = 0; i1 < 3; ++i1) {
                        this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l, itemstack);
                        this.field_178151_h[i1] = -1;
                        if (this.enchantLevels[i1] < i1 + 1) {
                            this.enchantLevels[i1] = 0;
                        }
                    }
                    for (int j2 = 0; j2 < 3; ++j2) {
                        if (this.enchantLevels[j2] > 0) {
                            final List<EnchantmentData> list = this.func_178148_a(itemstack, j2, this.enchantLevels[j2]);
                            if (list != null && !list.isEmpty()) {
                                final EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                                this.field_178151_h[j2] = (enchantmentdata.enchantmentobj.effectId | enchantmentdata.enchantmentLevel << 8);
                            }
                        }
                    }
                    this.detectAndSendChanges();
                }
            }
            else {
                for (int m = 0; m < 3; ++m) {
                    this.enchantLevels[m] = 0;
                    this.field_178151_h[m] = -1;
                }
            }
        }
    }
    
    @Override
    public boolean enchantItem(final EntityPlayer playerIn, final int id) {
        final ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        final ItemStack itemstack2 = this.tableInventory.getStackInSlot(1);
        final int i = id + 1;
        if ((itemstack2 == null || itemstack2.stackSize < i) && !playerIn.capabilities.isCreativeMode) {
            return false;
        }
        if (this.enchantLevels[id] > 0 && itemstack != null && ((playerIn.experienceLevel >= i && playerIn.experienceLevel >= this.enchantLevels[id]) || playerIn.capabilities.isCreativeMode)) {
            if (!this.worldPointer.isRemote) {
                final List<EnchantmentData> list = this.func_178148_a(itemstack, id, this.enchantLevels[id]);
                final boolean flag = itemstack.getItem() == Items.book;
                if (list != null) {
                    playerIn.removeExperienceLevel(i);
                    if (flag) {
                        itemstack.setItem(Items.enchanted_book);
                    }
                    for (int j = 0; j < list.size(); ++j) {
                        final EnchantmentData enchantmentdata = list.get(j);
                        if (flag) {
                            Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
                        }
                        else {
                            itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                        }
                    }
                    if (!playerIn.capabilities.isCreativeMode) {
                        final ItemStack itemStack = itemstack2;
                        itemStack.stackSize -= i;
                        if (itemstack2.stackSize <= 0) {
                            this.tableInventory.setInventorySlotContents(1, null);
                        }
                    }
                    playerIn.triggerAchievement(StatList.field_181739_W);
                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                }
            }
            return true;
        }
        return false;
    }
    
    private List<EnchantmentData> func_178148_a(final ItemStack stack, final int p_178148_2_, final int p_178148_3_) {
        this.rand.setSeed(this.xpSeed + p_178148_2_);
        final List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);
        if (stack.getItem() == Items.book && list != null && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }
        return list;
    }
    
    public int getLapisAmount() {
        final ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return (itemstack == null) ? 0 : itemstack.stackSize;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.worldPointer.isRemote) {
            for (int i = 0; i < this.tableInventory.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.tableInventory.removeStackFromSlot(i);
                if (itemstack != null) {
                    playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.worldPointer.getBlockState(this.position).getBlock() == Blocks.enchanting_table && playerIn.getDistanceSq(this.position.getX() + 0.5, this.position.getY() + 0.5, this.position.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 2, 38, true)) {
                    return null;
                }
            }
            else if (index == 1) {
                if (!this.mergeItemStack(itemstack2, 2, 38, true)) {
                    return null;
                }
            }
            else if (itemstack2.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemstack2.getMetadata()) == EnumDyeColor.BLUE) {
                if (!this.mergeItemStack(itemstack2, 1, 2, true)) {
                    return null;
                }
            }
            else {
                if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack2)) {
                    return null;
                }
                if (itemstack2.hasTagCompound() && itemstack2.stackSize == 1) {
                    this.inventorySlots.get(0).putStack(itemstack2.copy());
                    itemstack2.stackSize = 0;
                }
                else if (itemstack2.stackSize >= 1) {
                    this.inventorySlots.get(0).putStack(new ItemStack(itemstack2.getItem(), 1, itemstack2.getMetadata()));
                    final ItemStack itemStack = itemstack2;
                    --itemStack.stackSize;
                }
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, itemstack2);
        }
        return itemstack;
    }
}
