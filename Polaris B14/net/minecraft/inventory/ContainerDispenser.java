/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ContainerDispenser extends Container
/*    */ {
/*    */   private IInventory dispenserInventory;
/*    */   
/*    */   public ContainerDispenser(IInventory playerInventory, IInventory dispenserInventoryIn)
/*    */   {
/* 12 */     this.dispenserInventory = dispenserInventoryIn;
/*    */     
/* 14 */     for (int i = 0; i < 3; i++)
/*    */     {
/* 16 */       for (int j = 0; j < 3; j++)
/*    */       {
/* 18 */         addSlotToContainer(new Slot(dispenserInventoryIn, j + i * 3, 62 + j * 18, 17 + i * 18));
/*    */       }
/*    */     }
/*    */     
/* 22 */     for (int k = 0; k < 3; k++)
/*    */     {
/* 24 */       for (int i1 = 0; i1 < 9; i1++)
/*    */       {
/* 26 */         addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
/*    */       }
/*    */     }
/*    */     
/* 30 */     for (int l = 0; l < 9; l++)
/*    */     {
/* 32 */       addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn)
/*    */   {
/* 38 */     return this.dispenserInventory.isUseableByPlayer(playerIn);
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
/* 54 */       if (index < 9)
/*    */       {
/* 56 */         if (!mergeItemStack(itemstack1, 9, 45, true))
/*    */         {
/* 58 */           return null;
/*    */         }
/*    */       }
/* 61 */       else if (!mergeItemStack(itemstack1, 0, 9, false))
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
/*    */       
/* 75 */       if (itemstack1.stackSize == itemstack.stackSize)
/*    */       {
/* 77 */         return null;
/*    */       }
/*    */       
/* 80 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*    */     }
/*    */     
/* 83 */     return itemstack;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */