/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ContainerChest extends Container
/*    */ {
/*    */   private IInventory lowerChestInventory;
/*    */   private int numRows;
/*    */   
/*    */   public ContainerChest(IInventory playerInventory, IInventory chestInventory, EntityPlayer player)
/*    */   {
/* 13 */     this.lowerChestInventory = chestInventory;
/* 14 */     this.numRows = (chestInventory.getSizeInventory() / 9);
/* 15 */     chestInventory.openInventory(player);
/* 16 */     int i = (this.numRows - 4) * 18;
/*    */     
/* 18 */     for (int j = 0; j < this.numRows; j++)
/*    */     {
/* 20 */       for (int k = 0; k < 9; k++)
/*    */       {
/* 22 */         addSlotToContainer(new Slot(chestInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
/*    */       }
/*    */     }
/*    */     
/* 26 */     for (int l = 0; l < 3; l++)
/*    */     {
/* 28 */       for (int j1 = 0; j1 < 9; j1++)
/*    */       {
/* 30 */         addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
/*    */       }
/*    */     }
/*    */     
/* 34 */     for (int i1 = 0; i1 < 9; i1++)
/*    */     {
/* 36 */       addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn)
/*    */   {
/* 42 */     return this.lowerChestInventory.isUseableByPlayer(playerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*    */   {
/* 50 */     ItemStack itemstack = null;
/* 51 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*    */     
/* 53 */     if ((slot != null) && (slot.getHasStack()))
/*    */     {
/* 55 */       ItemStack itemstack1 = slot.getStack();
/* 56 */       itemstack = itemstack1.copy();
/*    */       
/* 58 */       if (index < this.numRows * 9)
/*    */       {
/* 60 */         if (!mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
/*    */         {
/* 62 */           return null;
/*    */         }
/*    */       }
/* 65 */       else if (!mergeItemStack(itemstack1, 0, this.numRows * 9, false))
/*    */       {
/* 67 */         return null;
/*    */       }
/*    */       
/* 70 */       if (itemstack1.stackSize == 0)
/*    */       {
/* 72 */         slot.putStack(null);
/*    */       }
/*    */       else
/*    */       {
/* 76 */         slot.onSlotChanged();
/*    */       }
/*    */     }
/*    */     
/* 80 */     return itemstack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onContainerClosed(EntityPlayer playerIn)
/*    */   {
/* 88 */     super.onContainerClosed(playerIn);
/* 89 */     this.lowerChestInventory.closeInventory(playerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IInventory getLowerChestInventory()
/*    */   {
/* 97 */     return this.lowerChestInventory;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */