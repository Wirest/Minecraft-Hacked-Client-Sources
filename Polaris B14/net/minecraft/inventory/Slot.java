/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slot
/*     */ {
/*     */   private final int slotIndex;
/*     */   public final IInventory inventory;
/*     */   public int slotNumber;
/*     */   public int xDisplayPosition;
/*     */   public int yDisplayPosition;
/*     */   
/*     */   public Slot(IInventory inventoryIn, int index, int xPosition, int yPosition)
/*     */   {
/*  25 */     this.inventory = inventoryIn;
/*  26 */     this.slotIndex = index;
/*  27 */     this.xDisplayPosition = xPosition;
/*  28 */     this.yDisplayPosition = yPosition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
/*     */   {
/*  36 */     if ((p_75220_1_ != null) && (p_75220_2_ != null))
/*     */     {
/*  38 */       if (p_75220_1_.getItem() == p_75220_2_.getItem())
/*     */       {
/*  40 */         int i = p_75220_2_.stackSize - p_75220_1_.stackSize;
/*     */         
/*  42 */         if (i > 0)
/*     */         {
/*  44 */           onCrafting(p_75220_1_, i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack, int amount) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*     */   {
/*  67 */     onSlotChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValid(ItemStack stack)
/*     */   {
/*  75 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStack()
/*     */   {
/*  83 */     return this.inventory.getStackInSlot(this.slotIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getHasStack()
/*     */   {
/*  91 */     return getStack() != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putStack(ItemStack stack)
/*     */   {
/*  99 */     this.inventory.setInventorySlotContents(this.slotIndex, stack);
/* 100 */     onSlotChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onSlotChanged()
/*     */   {
/* 108 */     this.inventory.markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSlotStackLimit()
/*     */   {
/* 117 */     return this.inventory.getInventoryStackLimit();
/*     */   }
/*     */   
/*     */   public int getItemStackLimit(ItemStack stack)
/*     */   {
/* 122 */     return getSlotStackLimit();
/*     */   }
/*     */   
/*     */   public String getSlotTexture()
/*     */   {
/* 127 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int amount)
/*     */   {
/* 136 */     return this.inventory.decrStackSize(this.slotIndex, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHere(IInventory inv, int slotIn)
/*     */   {
/* 144 */     return (inv == this.inventory) && (slotIn == this.slotIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canTakeStack(EntityPlayer playerIn)
/*     */   {
/* 152 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeHovered()
/*     */   {
/* 161 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\Slot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */