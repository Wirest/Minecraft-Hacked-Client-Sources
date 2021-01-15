/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class SlotFurnaceOutput extends Slot
/*     */ {
/*     */   private EntityPlayer thePlayer;
/*     */   private int field_75228_b;
/*     */   
/*     */   public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
/*     */   {
/*  19 */     super(inventoryIn, slotIndex, xPosition, yPosition);
/*  20 */     this.thePlayer = player;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValid(ItemStack stack)
/*     */   {
/*  28 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int amount)
/*     */   {
/*  37 */     if (getHasStack())
/*     */     {
/*  39 */       this.field_75228_b += Math.min(amount, getStack().stackSize);
/*     */     }
/*     */     
/*  42 */     return super.decrStackSize(amount);
/*     */   }
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*     */   {
/*  47 */     onCrafting(stack);
/*  48 */     super.onPickupFromSlot(playerIn, stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack, int amount)
/*     */   {
/*  57 */     this.field_75228_b += amount;
/*  58 */     onCrafting(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onCrafting(ItemStack stack)
/*     */   {
/*  66 */     stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
/*     */     
/*  68 */     if (!this.thePlayer.worldObj.isRemote)
/*     */     {
/*  70 */       int i = this.field_75228_b;
/*  71 */       float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
/*     */       
/*  73 */       if (f == 0.0F)
/*     */       {
/*  75 */         i = 0;
/*     */       }
/*  77 */       else if (f < 1.0F)
/*     */       {
/*  79 */         int j = MathHelper.floor_float(i * f);
/*     */         
/*  81 */         if ((j < MathHelper.ceiling_float_int(i * f)) && (Math.random() < i * f - j))
/*     */         {
/*  83 */           j++;
/*     */         }
/*     */         
/*  86 */         i = j;
/*     */       }
/*     */       
/*  89 */       while (i > 0)
/*     */       {
/*  91 */         int k = EntityXPOrb.getXPSplit(i);
/*  92 */         i -= k;
/*  93 */         this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
/*     */       }
/*     */     }
/*     */     
/*  97 */     this.field_75228_b = 0;
/*     */     
/*  99 */     if (stack.getItem() == Items.iron_ingot)
/*     */     {
/* 101 */       this.thePlayer.triggerAchievement(AchievementList.acquireIron);
/*     */     }
/*     */     
/* 104 */     if (stack.getItem() == Items.cooked_fish)
/*     */     {
/* 106 */       this.thePlayer.triggerAchievement(AchievementList.cookFish);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\SlotFurnaceOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */