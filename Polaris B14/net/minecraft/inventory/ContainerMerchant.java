/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class ContainerMerchant
/*     */   extends Container
/*     */ {
/*     */   private IMerchant theMerchant;
/*     */   private InventoryMerchant merchantInventory;
/*     */   private final World theWorld;
/*     */   
/*     */   public ContainerMerchant(InventoryPlayer playerInventory, IMerchant merchant, World worldIn)
/*     */   {
/*  20 */     this.theMerchant = merchant;
/*  21 */     this.theWorld = worldIn;
/*  22 */     this.merchantInventory = new InventoryMerchant(playerInventory.player, merchant);
/*  23 */     addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
/*  24 */     addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
/*  25 */     addSlotToContainer(new SlotMerchantResult(playerInventory.player, merchant, this.merchantInventory, 2, 120, 53));
/*     */     
/*  27 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  29 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  31 */         addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     }
/*     */     
/*  35 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  37 */       addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */   
/*     */   public InventoryMerchant getMerchantInventory()
/*     */   {
/*  43 */     return this.merchantInventory;
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/*  48 */     super.onCraftGuiOpened(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void detectAndSendChanges()
/*     */   {
/*  56 */     super.detectAndSendChanges();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/*  64 */     this.merchantInventory.resetRecipeAndSlots();
/*  65 */     super.onCraftMatrixChanged(inventoryIn);
/*     */   }
/*     */   
/*     */   public void setCurrentRecipeIndex(int currentRecipeIndex)
/*     */   {
/*  70 */     this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateProgressBar(int id, int data) {}
/*     */   
/*     */ 
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/*  79 */     return this.theMerchant.getCustomer() == playerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/*  87 */     ItemStack itemstack = null;
/*  88 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/*  90 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/*  92 */       ItemStack itemstack1 = slot.getStack();
/*  93 */       itemstack = itemstack1.copy();
/*     */       
/*  95 */       if (index == 2)
/*     */       {
/*  97 */         if (!mergeItemStack(itemstack1, 3, 39, true))
/*     */         {
/*  99 */           return null;
/*     */         }
/*     */         
/* 102 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 104 */       else if ((index != 0) && (index != 1))
/*     */       {
/* 106 */         if ((index >= 3) && (index < 30))
/*     */         {
/* 108 */           if (!mergeItemStack(itemstack1, 30, 39, false))
/*     */           {
/* 110 */             return null;
/*     */           }
/*     */         }
/* 113 */         else if ((index >= 30) && (index < 39) && (!mergeItemStack(itemstack1, 3, 30, false)))
/*     */         {
/* 115 */           return null;
/*     */         }
/*     */       }
/* 118 */       else if (!mergeItemStack(itemstack1, 3, 39, false))
/*     */       {
/* 120 */         return null;
/*     */       }
/*     */       
/* 123 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 125 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 129 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 132 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 134 */         return null;
/*     */       }
/*     */       
/* 137 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 140 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/* 148 */     super.onContainerClosed(playerIn);
/* 149 */     this.theMerchant.setCustomer(null);
/* 150 */     super.onContainerClosed(playerIn);
/*     */     
/* 152 */     if (!this.theWorld.isRemote)
/*     */     {
/* 154 */       ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);
/*     */       
/* 156 */       if (itemstack != null)
/*     */       {
/* 158 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */       
/* 161 */       itemstack = this.merchantInventory.removeStackFromSlot(1);
/*     */       
/* 163 */       if (itemstack != null)
/*     */       {
/* 165 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */