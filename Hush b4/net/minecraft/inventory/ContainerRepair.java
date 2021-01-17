// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    private static final Logger logger;
    private IInventory outputSlot;
    private IInventory inputSlots;
    private World theWorld;
    private BlockPos selfPosition;
    public int maximumCost;
    private int materialCost;
    private String repairedItemName;
    private final EntityPlayer thePlayer;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ContainerRepair(final InventoryPlayer playerInventory, final World worldIn, final EntityPlayer player) {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }
    
    public ContainerRepair(final InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, final EntityPlayer player) {
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Repair", true, 2) {
            @Override
            public void markDirty() {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.selfPosition = blockPosIn;
        this.theWorld = worldIn;
        this.thePlayer = player;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return false;
            }
            
            @Override
            public boolean canTakeStack(final EntityPlayer playerIn) {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }
            
            @Override
            public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
                if (ContainerRepair.this.materialCost > 0) {
                    final ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (itemstack != null && itemstack.stackSize > ContainerRepair.this.materialCost) {
                        final ItemStack itemStack = itemstack;
                        itemStack.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
                    }
                    else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                    }
                }
                else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                }
                ContainerRepair.this.maximumCost = 0;
                final IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12f) {
                    int l = iblockstate.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
                    if (++l > 2) {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playAuxSFX(1020, blockPosIn, 0);
                    }
                    else {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, l), 2);
                        worldIn.playAuxSFX(1021, blockPosIn, 0);
                    }
                }
                else if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(1021, blockPosIn, 0);
                }
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        if (inventoryIn == this.inputSlots) {
            this.updateRepairOutput();
        }
    }
    
    public void updateRepairOutput() {
        final int i = 0;
        final int j = 1;
        final int k = 1;
        final int l = 1;
        final int i2 = 2;
        final int j2 = 1;
        final int k2 = 1;
        final ItemStack itemstack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int l2 = 0;
        int i3 = 0;
        int j3 = 0;
        if (itemstack == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        }
        else {
            ItemStack itemstack2 = itemstack.copy();
            final ItemStack itemstack3 = this.inputSlots.getStackInSlot(1);
            final Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemstack2);
            boolean flag = false;
            i3 = i3 + itemstack.getRepairCost() + ((itemstack3 == null) ? 0 : itemstack3.getRepairCost());
            this.materialCost = 0;
            if (itemstack3 != null) {
                flag = (itemstack3.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(itemstack3).tagCount() > 0);
                if (itemstack2.isItemStackDamageable() && itemstack2.getItem().getIsRepairable(itemstack, itemstack3)) {
                    int j4 = Math.min(itemstack2.getItemDamage(), itemstack2.getMaxDamage() / 4);
                    if (j4 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    int l3;
                    for (l3 = 0; j4 > 0 && l3 < itemstack3.stackSize; j4 = Math.min(itemstack2.getItemDamage(), itemstack2.getMaxDamage() / 4), ++l3) {
                        final int j5 = itemstack2.getItemDamage() - j4;
                        itemstack2.setItemDamage(j5);
                        ++l2;
                    }
                    this.materialCost = l3;
                }
                else {
                    if (!flag && (itemstack2.getItem() != itemstack3.getItem() || !itemstack2.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    if (itemstack2.isItemStackDamageable() && !flag) {
                        final int k3 = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        final int l4 = itemstack3.getMaxDamage() - itemstack3.getItemDamage();
                        final int i4 = l4 + itemstack2.getMaxDamage() * 12 / 100;
                        final int j6 = k3 + i4;
                        int k4 = itemstack2.getMaxDamage() - j6;
                        if (k4 < 0) {
                            k4 = 0;
                        }
                        if (k4 < itemstack2.getMetadata()) {
                            itemstack2.setItemDamage(k4);
                            l2 += 2;
                        }
                    }
                    final Map<Integer, Integer> map2 = EnchantmentHelper.getEnchantments(itemstack3);
                    for (final int i5 : map2.keySet()) {
                        final Enchantment enchantment = Enchantment.getEnchantmentById(i5);
                        if (enchantment != null) {
                            final int k5 = map.containsKey(i5) ? map.get(i5) : 0;
                            int l5 = map2.get(i5);
                            int i6;
                            if (k5 == l5) {
                                i6 = ++l5;
                            }
                            else {
                                i6 = Math.max(l5, k5);
                            }
                            l5 = i6;
                            boolean flag2 = enchantment.canApply(itemstack);
                            if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book) {
                                flag2 = true;
                            }
                            for (final int i7 : map.keySet()) {
                                if (i7 != i5 && !enchantment.canApplyTogether(Enchantment.getEnchantmentById(i7))) {
                                    flag2 = false;
                                    ++l2;
                                }
                            }
                            if (!flag2) {
                                continue;
                            }
                            if (l5 > enchantment.getMaxLevel()) {
                                l5 = enchantment.getMaxLevel();
                            }
                            map.put(i5, l5);
                            int l6 = 0;
                            switch (enchantment.getWeight()) {
                                case 1: {
                                    l6 = 8;
                                    break;
                                }
                                case 2: {
                                    l6 = 4;
                                    break;
                                }
                                case 5: {
                                    l6 = 2;
                                    break;
                                }
                                case 10: {
                                    l6 = 1;
                                    break;
                                }
                            }
                            if (flag) {
                                l6 = Math.max(1, l6 / 2);
                            }
                            l2 += l6 * l5;
                        }
                    }
                }
            }
            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    j3 = 1;
                    l2 += j3;
                    itemstack2.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
                j3 = 1;
                l2 += j3;
                itemstack2.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = i3 + l2;
            if (l2 <= 0) {
                itemstack2 = null;
            }
            if (j3 == l2 && j3 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                itemstack2 = null;
            }
            if (itemstack2 != null) {
                int k6 = itemstack2.getRepairCost();
                if (itemstack3 != null && k6 < itemstack3.getRepairCost()) {
                    k6 = itemstack3.getRepairCost();
                }
                k6 = k6 * 2 + 1;
                itemstack2.setRepairCost(k6);
                EnchantmentHelper.setEnchantments(map, itemstack2);
            }
            this.outputSlot.setInventorySlotContents(0, itemstack2);
            this.detectAndSendChanges();
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.maximumCost);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        if (id == 0) {
            this.maximumCost = data;
        }
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.theWorld.isRemote) {
            for (int i = 0; i < this.inputSlots.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.inputSlots.removeStackFromSlot(i);
                if (itemstack != null) {
                    playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.theWorld.getBlockState(this.selfPosition).getBlock() == Blocks.anvil && playerIn.getDistanceSq(this.selfPosition.getX() + 0.5, this.selfPosition.getY() + 0.5, this.selfPosition.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack2, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack2, 0, 2, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 3, 39, false)) {
                return null;
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
    
    public void updateItemName(final String newName) {
        this.repairedItemName = newName;
        if (this.getSlot(2).getHasStack()) {
            final ItemStack itemstack = this.getSlot(2).getStack();
            if (StringUtils.isBlank(newName)) {
                itemstack.clearCustomName();
            }
            else {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }
}
