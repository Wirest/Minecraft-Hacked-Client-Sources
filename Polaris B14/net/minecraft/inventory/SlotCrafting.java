/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.Item.ToolMaterial;
/*     */ import net.minecraft.item.ItemHoe;
/*     */ import net.minecraft.item.ItemPickaxe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SlotCrafting
/*     */   extends Slot
/*     */ {
/*     */   private final InventoryCrafting craftMatrix;
/*     */   private final EntityPlayer thePlayer;
/*     */   private int amountCrafted;
/*     */   
/*     */   public SlotCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory p_i45790_3_, int slotIndex, int xPosition, int yPosition)
/*     */   {
/*  29 */     super(p_i45790_3_, slotIndex, xPosition, yPosition);
/*  30 */     this.thePlayer = player;
/*  31 */     this.craftMatrix = craftingInventory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValid(ItemStack stack)
/*     */   {
/*  39 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int amount)
/*     */   {
/*  48 */     if (getHasStack())
/*     */     {
/*  50 */       this.amountCrafted += Math.min(amount, getStack().stackSize);
/*     */     }
/*     */     
/*  53 */     return super.decrStackSize(amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack, int amount)
/*     */   {
/*  62 */     this.amountCrafted += amount;
/*  63 */     onCrafting(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack)
/*     */   {
/*  71 */     if (this.amountCrafted > 0)
/*     */     {
/*  73 */       stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
/*     */     }
/*     */     
/*  76 */     this.amountCrafted = 0;
/*     */     
/*  78 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.crafting_table))
/*     */     {
/*  80 */       this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
/*     */     }
/*     */     
/*  83 */     if ((stack.getItem() instanceof ItemPickaxe))
/*     */     {
/*  85 */       this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
/*     */     }
/*     */     
/*  88 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.furnace))
/*     */     {
/*  90 */       this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
/*     */     }
/*     */     
/*  93 */     if ((stack.getItem() instanceof ItemHoe))
/*     */     {
/*  95 */       this.thePlayer.triggerAchievement(AchievementList.buildHoe);
/*     */     }
/*     */     
/*  98 */     if (stack.getItem() == Items.bread)
/*     */     {
/* 100 */       this.thePlayer.triggerAchievement(AchievementList.makeBread);
/*     */     }
/*     */     
/* 103 */     if (stack.getItem() == Items.cake)
/*     */     {
/* 105 */       this.thePlayer.triggerAchievement(AchievementList.bakeCake);
/*     */     }
/*     */     
/* 108 */     if (((stack.getItem() instanceof ItemPickaxe)) && (((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD))
/*     */     {
/* 110 */       this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
/*     */     }
/*     */     
/* 113 */     if ((stack.getItem() instanceof ItemSword))
/*     */     {
/* 115 */       this.thePlayer.triggerAchievement(AchievementList.buildSword);
/*     */     }
/*     */     
/* 118 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table))
/*     */     {
/* 120 */       this.thePlayer.triggerAchievement(AchievementList.enchantments);
/*     */     }
/*     */     
/* 123 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.bookshelf))
/*     */     {
/* 125 */       this.thePlayer.triggerAchievement(AchievementList.bookcase);
/*     */     }
/*     */     
/* 128 */     if ((stack.getItem() == Items.golden_apple) && (stack.getMetadata() == 1))
/*     */     {
/* 130 */       this.thePlayer.triggerAchievement(AchievementList.overpowered);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*     */   {
/* 136 */     onCrafting(stack);
/* 137 */     ItemStack[] aitemstack = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
/*     */     
/* 139 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 141 */       ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
/* 142 */       ItemStack itemstack1 = aitemstack[i];
/*     */       
/* 144 */       if (itemstack != null)
/*     */       {
/* 146 */         this.craftMatrix.decrStackSize(i, 1);
/*     */       }
/*     */       
/* 149 */       if (itemstack1 != null)
/*     */       {
/* 151 */         if (this.craftMatrix.getStackInSlot(i) == null)
/*     */         {
/* 153 */           this.craftMatrix.setInventorySlotContents(i, itemstack1);
/*     */         }
/* 155 */         else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack1))
/*     */         {
/* 157 */           this.thePlayer.dropPlayerItemWithRandomChoice(itemstack1, false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\SlotCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */