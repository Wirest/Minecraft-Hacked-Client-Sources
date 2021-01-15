/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.FoodStats;
/*     */ import net.minecraft.world.World;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemFood
/*     */   extends Item
/*     */ {
/*     */   public final int itemUseDuration;
/*     */   private final int healAmount;
/*     */   private final float saturationModifier;
/*     */   private final boolean isWolfsFavoriteMeat;
/*     */   private boolean alwaysEdible;
/*     */   private int potionId;
/*     */   private int potionDuration;
/*     */   private int potionAmplifier;
/*     */   private float potionEffectProbability;
/*     */   
/*     */   public ItemFood(int amount, float saturation, boolean isWolfFood)
/*     */   {
/*  42 */     this.itemUseDuration = 32;
/*  43 */     this.healAmount = amount;
/*  44 */     this.isWolfsFavoriteMeat = isWolfFood;
/*  45 */     this.saturationModifier = saturation;
/*  46 */     setCreativeTab(CreativeTabs.tabFood);
/*     */   }
/*     */   
/*     */   public ItemFood(int amount, boolean isWolfFood)
/*     */   {
/*  51 */     this(amount, 0.6F, isWolfFood);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  60 */     stack.stackSize -= 1;
/*  61 */     playerIn.getFoodStats().addStats(this, stack);
/*  62 */     worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
/*  63 */     onFoodEaten(stack, worldIn, playerIn);
/*  64 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  65 */     return stack;
/*     */   }
/*     */   
/*     */   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
/*     */   {
/*  70 */     if ((!worldIn.isRemote) && (this.potionId > 0) && (worldIn.rand.nextFloat() < this.potionEffectProbability))
/*     */     {
/*  72 */       player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxItemUseDuration(ItemStack stack)
/*     */   {
/*  81 */     return 32;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumAction getItemUseAction(ItemStack stack)
/*     */   {
/*  89 */     return EnumAction.EAT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  97 */     if (playerIn.canEat(this.alwaysEdible))
/*     */     {
/*  99 */       playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/*     */     }
/*     */     
/* 102 */     return itemStackIn;
/*     */   }
/*     */   
/*     */   public int getHealAmount(ItemStack stack)
/*     */   {
/* 107 */     return this.healAmount;
/*     */   }
/*     */   
/*     */   public float getSaturationModifier(ItemStack stack)
/*     */   {
/* 112 */     return this.saturationModifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWolfsFavoriteMeat()
/*     */   {
/* 120 */     return this.isWolfsFavoriteMeat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemFood setPotionEffect(int id, int duration, int amplifier, float probability)
/*     */   {
/* 129 */     this.potionId = id;
/* 130 */     this.potionDuration = duration;
/* 131 */     this.potionAmplifier = amplifier;
/* 132 */     this.potionEffectProbability = probability;
/* 133 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemFood setAlwaysEdible()
/*     */   {
/* 141 */     this.alwaysEdible = true;
/* 142 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */