// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic
{
    private TileEntityEnderChest associatedChest;
    
    public InventoryEnderChest() {
        super("container.enderchest", false, 27);
    }
    
    public void setChestTileEntity(final TileEntityEnderChest chestTileEntity) {
        this.associatedChest = chestTileEntity;
    }
    
    public void loadInventoryFromNBT(final NBTTagList p_70486_1_) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, null);
        }
        for (int k = 0; k < p_70486_1_.tagCount(); ++k) {
            final NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
    }
    
    public NBTTagList saveInventoryToNBT() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        return nbttaglist;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return (this.associatedChest == null || this.associatedChest.canBeUsed(player)) && super.isUseableByPlayer(player);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        if (this.associatedChest != null) {
            this.associatedChest.openChest();
        }
        super.openInventory(player);
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        if (this.associatedChest != null) {
            this.associatedChest.closeChest();
        }
        super.closeInventory(player);
        this.associatedChest = null;
    }
}
