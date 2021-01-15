/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class FoodStats
/*     */ {
/*  12 */   private int foodLevel = 20;
/*     */   
/*     */ 
/*  15 */   private float foodSaturationLevel = 5.0F;
/*     */   
/*     */ 
/*     */   private float foodExhaustionLevel;
/*     */   
/*     */   private int foodTimer;
/*     */   
/*  22 */   private int prevFoodLevel = 20;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addStats(int foodLevelIn, float foodSaturationModifier)
/*     */   {
/*  29 */     this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
/*  30 */     this.foodSaturationLevel = Math.min(this.foodSaturationLevel + foodLevelIn * foodSaturationModifier * 2.0F, this.foodLevel);
/*     */   }
/*     */   
/*     */   public void addStats(ItemFood foodItem, ItemStack p_151686_2_)
/*     */   {
/*  35 */     addStats(foodItem.getHealAmount(p_151686_2_), foodItem.getSaturationModifier(p_151686_2_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate(EntityPlayer player)
/*     */   {
/*  43 */     EnumDifficulty enumdifficulty = player.worldObj.getDifficulty();
/*  44 */     this.prevFoodLevel = this.foodLevel;
/*     */     
/*  46 */     if (this.foodExhaustionLevel > 4.0F)
/*     */     {
/*  48 */       this.foodExhaustionLevel -= 4.0F;
/*     */       
/*  50 */       if (this.foodSaturationLevel > 0.0F)
/*     */       {
/*  52 */         this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
/*     */       }
/*  54 */       else if (enumdifficulty != EnumDifficulty.PEACEFUL)
/*     */       {
/*  56 */         this.foodLevel = Math.max(this.foodLevel - 1, 0);
/*     */       }
/*     */     }
/*     */     
/*  60 */     if ((player.worldObj.getGameRules().getBoolean("naturalRegeneration")) && (this.foodLevel >= 18) && (player.shouldHeal()))
/*     */     {
/*  62 */       this.foodTimer += 1;
/*     */       
/*  64 */       if (this.foodTimer >= 80)
/*     */       {
/*  66 */         player.heal(1.0F);
/*  67 */         addExhaustion(3.0F);
/*  68 */         this.foodTimer = 0;
/*     */       }
/*     */     }
/*  71 */     else if (this.foodLevel <= 0)
/*     */     {
/*  73 */       this.foodTimer += 1;
/*     */       
/*  75 */       if (this.foodTimer >= 80)
/*     */       {
/*  77 */         if ((player.getHealth() > 10.0F) || (enumdifficulty == EnumDifficulty.HARD) || ((player.getHealth() > 1.0F) && (enumdifficulty == EnumDifficulty.NORMAL)))
/*     */         {
/*  79 */           player.attackEntityFrom(DamageSource.starve, 1.0F);
/*     */         }
/*     */         
/*  82 */         this.foodTimer = 0;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  87 */       this.foodTimer = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readNBT(NBTTagCompound p_75112_1_)
/*     */   {
/*  96 */     if (p_75112_1_.hasKey("foodLevel", 99))
/*     */     {
/*  98 */       this.foodLevel = p_75112_1_.getInteger("foodLevel");
/*  99 */       this.foodTimer = p_75112_1_.getInteger("foodTickTimer");
/* 100 */       this.foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
/* 101 */       this.foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeNBT(NBTTagCompound p_75117_1_)
/*     */   {
/* 110 */     p_75117_1_.setInteger("foodLevel", this.foodLevel);
/* 111 */     p_75117_1_.setInteger("foodTickTimer", this.foodTimer);
/* 112 */     p_75117_1_.setFloat("foodSaturationLevel", this.foodSaturationLevel);
/* 113 */     p_75117_1_.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFoodLevel()
/*     */   {
/* 121 */     return this.foodLevel;
/*     */   }
/*     */   
/*     */   public int getPrevFoodLevel()
/*     */   {
/* 126 */     return this.prevFoodLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean needFood()
/*     */   {
/* 134 */     return this.foodLevel < 20;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addExhaustion(float p_75113_1_)
/*     */   {
/* 142 */     this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getSaturationLevel()
/*     */   {
/* 150 */     return this.foodSaturationLevel;
/*     */   }
/*     */   
/*     */   public void setFoodLevel(int foodLevelIn)
/*     */   {
/* 155 */     this.foodLevel = foodLevelIn;
/*     */   }
/*     */   
/*     */   public void setFoodSaturationLevel(float foodSaturationLevelIn)
/*     */   {
/* 160 */     this.foodSaturationLevel = foodSaturationLevelIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\FoodStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */