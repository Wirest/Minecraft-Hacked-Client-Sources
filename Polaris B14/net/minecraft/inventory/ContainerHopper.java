/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ContainerHopper extends Container
/*    */ {
/*    */   private final IInventory hopperInventory;
/*    */   
/*    */   public ContainerHopper(InventoryPlayer playerInventory, IInventory hopperInventoryIn, EntityPlayer player)
/*    */   {
/* 13 */     this.hopperInventory = hopperInventoryIn;
/* 14 */     hopperInventoryIn.openInventory(player);
/* 15 */     int i = 51;
/*    */     
/* 17 */     for (int j = 0; j < hopperInventoryIn.getSizeInventory(); j++)
/*    */     {
/* 19 */       addSlotToContainer(new Slot(hopperInventoryIn, j, 44 + j * 18, 20));
/*    */     }
/*    */     
/* 22 */     for (int l = 0; l < 3; l++)
/*    */     {
/* 24 */       for (int k = 0; k < 9; k++)
/*    */       {
/* 26 */         addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + i));
/*    */       }
/*    */     }
/*    */     
/* 30 */     for (int i1 = 0; i1 < 9; i1++)
/*    */     {
/* 32 */       addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 58 + i));
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn)
/*    */   {
/* 38 */     return this.hopperInventory.isUseableByPlayer(playerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*    */   {
/* 46 */     ItemStack itemstack = null;
/* 47 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*    */     
/* 49 */     if ((slot != null) && (slot.getHasStack()))
/*    */     {
/* 51 */       ItemStack itemstack1 = slot.getStack();
/* 52 */       itemstack = itemstack1.copy();
/*    */       
/* 54 */       if (index < this.hopperInventory.getSizeInventory())
/*    */       {
/* 56 */         if (!mergeItemStack(itemstack1, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true))
/*    */         {
/* 58 */           return null;
/*    */         }
/*    */       }
/* 61 */       else if (!mergeItemStack(itemstack1, 0, this.hopperInventory.getSizeInventory(), false))
/*    */       {
/* 63 */         return null;
/*    */       }
/*    */       
/* 66 */       if (itemstack1.stackSize == 0)
/*    */       {
/* 68 */         slot.putStack(null);
/*    */       }
/*    */       else
/*    */       {
/* 72 */         slot.onSlotChanged();
/*    */       }
/*    */     }
/*    */     
/* 76 */     return itemstack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onContainerClosed(EntityPlayer playerIn)
/*    */   {
/* 84 */     super.onContainerClosed(playerIn);
/* 85 */     this.hopperInventory.closeInventory(playerIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */