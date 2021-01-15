/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContainerPlayer
/*     */   extends Container
/*     */ {
/*  15 */   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
/*  16 */   public IInventory craftResult = new InventoryCraftResult();
/*     */   
/*     */   public boolean isLocalWorld;
/*     */   
/*     */   private final EntityPlayer thePlayer;
/*     */   
/*     */   public ContainerPlayer(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player)
/*     */   {
/*  24 */     this.isLocalWorld = localWorld;
/*  25 */     this.thePlayer = player;
/*  26 */     addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));
/*     */     
/*  28 */     for (int i = 0; i < 2; i++)
/*     */     {
/*  30 */       for (int j = 0; j < 2; j++)
/*     */       {
/*  32 */         addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
/*     */       }
/*     */     }
/*     */     
/*  36 */     for (int k = 0; k < 4; k++)
/*     */     {
/*  38 */       final int k_f = k;
/*  39 */       addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - k, 8, 8 + k * 18)
/*     */       {
/*     */         public int getSlotStackLimit()
/*     */         {
/*  43 */           return 1;
/*     */         }
/*     */         
/*     */         public boolean isItemValid(ItemStack stack) {
/*  47 */           return stack != null;
/*     */         }
/*     */         
/*     */         public String getSlotTexture() {
/*  51 */           return ItemArmor.EMPTY_SLOT_NAMES[k_f];
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  56 */     for (int l = 0; l < 3; l++)
/*     */     {
/*  58 */       for (int j1 = 0; j1 < 9; j1++)
/*     */       {
/*  60 */         addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
/*     */       }
/*     */     }
/*     */     
/*  64 */     for (int i1 = 0; i1 < 9; i1++)
/*     */     {
/*  66 */       addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
/*     */     }
/*     */     
/*  69 */     onCraftMatrixChanged(this.craftMatrix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/*  77 */     this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/*  85 */     super.onContainerClosed(playerIn);
/*     */     
/*  87 */     for (int i = 0; i < 4; i++)
/*     */     {
/*  89 */       ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
/*     */       
/*  91 */       if (itemstack != null)
/*     */       {
/*  93 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     }
/*     */     
/*  97 */     this.craftResult.setInventorySlotContents(0, null);
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/* 102 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/* 110 */     ItemStack itemstack = null;
/* 111 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/* 113 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/* 115 */       ItemStack itemstack1 = slot.getStack();
/* 116 */       itemstack = itemstack1.copy();
/*     */       
/* 118 */       if (index == 0)
/*     */       {
/* 120 */         if (!mergeItemStack(itemstack1, 9, 45, true))
/*     */         {
/* 122 */           return null;
/*     */         }
/*     */         
/* 125 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 127 */       else if ((index >= 1) && (index < 5))
/*     */       {
/* 129 */         if (!mergeItemStack(itemstack1, 9, 45, false))
/*     */         {
/* 131 */           return null;
/*     */         }
/*     */       }
/* 134 */       else if ((index >= 5) && (index < 9))
/*     */       {
/* 136 */         if (!mergeItemStack(itemstack1, 9, 45, false))
/*     */         {
/* 138 */           return null;
/*     */         }
/*     */       }
/* 141 */       else if (((itemstack.getItem() instanceof ItemArmor)) && (!((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack()))
/*     */       {
/* 143 */         int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;
/*     */         
/* 145 */         if (!mergeItemStack(itemstack1, i, i + 1, false))
/*     */         {
/* 147 */           return null;
/*     */         }
/*     */       }
/* 150 */       else if ((index >= 9) && (index < 36))
/*     */       {
/* 152 */         if (!mergeItemStack(itemstack1, 36, 45, false))
/*     */         {
/* 154 */           return null;
/*     */         }
/*     */       }
/* 157 */       else if ((index >= 36) && (index < 45))
/*     */       {
/* 159 */         if (!mergeItemStack(itemstack1, 9, 36, false))
/*     */         {
/* 161 */           return null;
/*     */         }
/*     */       }
/* 164 */       else if (!mergeItemStack(itemstack1, 9, 45, false))
/*     */       {
/* 166 */         return null;
/*     */       }
/*     */       
/* 169 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 171 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 175 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 178 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 180 */         return null;
/*     */       }
/*     */       
/* 183 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 186 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
/*     */   {
/* 195 */     return (p_94530_2_.inventory != this.craftResult) && (super.canMergeSlot(stack, p_94530_2_));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */