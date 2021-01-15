/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.tileentity.TileEntityEnderChest;
/*    */ 
/*    */ public class InventoryEnderChest extends InventoryBasic
/*    */ {
/*    */   private TileEntityEnderChest associatedChest;
/*    */   
/*    */   public InventoryEnderChest()
/*    */   {
/* 15 */     super("container.enderchest", false, 27);
/*    */   }
/*    */   
/*    */   public void setChestTileEntity(TileEntityEnderChest chestTileEntity)
/*    */   {
/* 20 */     this.associatedChest = chestTileEntity;
/*    */   }
/*    */   
/*    */   public void loadInventoryFromNBT(NBTTagList p_70486_1_)
/*    */   {
/* 25 */     for (int i = 0; i < getSizeInventory(); i++)
/*    */     {
/* 27 */       setInventorySlotContents(i, null);
/*    */     }
/*    */     
/* 30 */     for (int k = 0; k < p_70486_1_.tagCount(); k++)
/*    */     {
/* 32 */       NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
/* 33 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*    */       
/* 35 */       if ((j >= 0) && (j < getSizeInventory()))
/*    */       {
/* 37 */         setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public NBTTagList saveInventoryToNBT()
/*    */   {
/* 44 */     NBTTagList nbttaglist = new NBTTagList();
/*    */     
/* 46 */     for (int i = 0; i < getSizeInventory(); i++)
/*    */     {
/* 48 */       ItemStack itemstack = getStackInSlot(i);
/*    */       
/* 50 */       if (itemstack != null)
/*    */       {
/* 52 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 53 */         nbttagcompound.setByte("Slot", (byte)i);
/* 54 */         itemstack.writeToNBT(nbttagcompound);
/* 55 */         nbttaglist.appendTag(nbttagcompound);
/*    */       }
/*    */     }
/*    */     
/* 59 */     return nbttaglist;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUseableByPlayer(EntityPlayer player)
/*    */   {
/* 67 */     return (this.associatedChest != null) && (!this.associatedChest.canBeUsed(player)) ? false : super.isUseableByPlayer(player);
/*    */   }
/*    */   
/*    */   public void openInventory(EntityPlayer player)
/*    */   {
/* 72 */     if (this.associatedChest != null)
/*    */     {
/* 74 */       this.associatedChest.openChest();
/*    */     }
/*    */     
/* 77 */     super.openInventory(player);
/*    */   }
/*    */   
/*    */   public void closeInventory(EntityPlayer player)
/*    */   {
/* 82 */     if (this.associatedChest != null)
/*    */     {
/* 84 */       this.associatedChest.closeChest();
/*    */     }
/*    */     
/* 87 */     super.closeInventory(player);
/* 88 */     this.associatedChest = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryEnderChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */