/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SlotMerchantResult
/*     */   extends Slot
/*     */ {
/*     */   private final InventoryMerchant theMerchantInventory;
/*     */   private EntityPlayer thePlayer;
/*     */   private int field_75231_g;
/*     */   private final IMerchant theMerchant;
/*     */   
/*     */   public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
/*     */   {
/*  23 */     super(merchantInventory, slotIndex, xPosition, yPosition);
/*  24 */     this.thePlayer = player;
/*  25 */     this.theMerchant = merchant;
/*  26 */     this.theMerchantInventory = merchantInventory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValid(ItemStack stack)
/*     */   {
/*  34 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int amount)
/*     */   {
/*  43 */     if (getHasStack())
/*     */     {
/*  45 */       this.field_75231_g += Math.min(amount, getStack().stackSize);
/*     */     }
/*     */     
/*  48 */     return super.decrStackSize(amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack, int amount)
/*     */   {
/*  57 */     this.field_75231_g += amount;
/*  58 */     onCrafting(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack)
/*     */   {
/*  66 */     stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
/*  67 */     this.field_75231_g = 0;
/*     */   }
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*     */   {
/*  72 */     onCrafting(stack);
/*  73 */     MerchantRecipe merchantrecipe = this.theMerchantInventory.getCurrentRecipe();
/*     */     
/*  75 */     if (merchantrecipe != null)
/*     */     {
/*  77 */       ItemStack itemstack = this.theMerchantInventory.getStackInSlot(0);
/*  78 */       ItemStack itemstack1 = this.theMerchantInventory.getStackInSlot(1);
/*     */       
/*  80 */       if ((doTrade(merchantrecipe, itemstack, itemstack1)) || (doTrade(merchantrecipe, itemstack1, itemstack)))
/*     */       {
/*  82 */         this.theMerchant.useRecipe(merchantrecipe);
/*  83 */         playerIn.triggerAchievement(StatList.timesTradedWithVillagerStat);
/*     */         
/*  85 */         if ((itemstack != null) && (itemstack.stackSize <= 0))
/*     */         {
/*  87 */           itemstack = null;
/*     */         }
/*     */         
/*  90 */         if ((itemstack1 != null) && (itemstack1.stackSize <= 0))
/*     */         {
/*  92 */           itemstack1 = null;
/*     */         }
/*     */         
/*  95 */         this.theMerchantInventory.setInventorySlotContents(0, itemstack);
/*  96 */         this.theMerchantInventory.setInventorySlotContents(1, itemstack1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem)
/*     */   {
/* 103 */     ItemStack itemstack = trade.getItemToBuy();
/* 104 */     ItemStack itemstack1 = trade.getSecondItemToBuy();
/*     */     
/* 106 */     if ((firstItem != null) && (firstItem.getItem() == itemstack.getItem()))
/*     */     {
/* 108 */       if ((itemstack1 != null) && (secondItem != null) && (itemstack1.getItem() == secondItem.getItem()))
/*     */       {
/* 110 */         firstItem.stackSize -= itemstack.stackSize;
/* 111 */         secondItem.stackSize -= itemstack1.stackSize;
/* 112 */         return true;
/*     */       }
/*     */       
/* 115 */       if ((itemstack1 == null) && (secondItem == null))
/*     */       {
/* 117 */         firstItem.stackSize -= itemstack.stackSize;
/* 118 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 122 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\SlotMerchantResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */