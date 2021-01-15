/*     */ package net.minecraft.enchantment;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class EnchantmentProtection
/*     */   extends Enchantment
/*     */ {
/*  11 */   private static final String[] protectionName = { "all", "fire", "fall", "explosion", "projectile" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  16 */   private static final int[] baseEnchantability = { 1, 10, 5, 5, 3 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  21 */   private static final int[] levelEnchantability = { 11, 8, 6, 8, 6 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  27 */   private static final int[] thresholdEnchantability = { 20, 12, 10, 12, 15 };
/*     */   
/*     */ 
/*     */ 
/*     */   public final int protectionType;
/*     */   
/*     */ 
/*     */ 
/*     */   public EnchantmentProtection(int p_i45765_1_, ResourceLocation p_i45765_2_, int p_i45765_3_, int p_i45765_4_)
/*     */   {
/*  37 */     super(p_i45765_1_, p_i45765_2_, p_i45765_3_, EnumEnchantmentType.ARMOR);
/*  38 */     this.protectionType = p_i45765_4_;
/*     */     
/*  40 */     if (p_i45765_4_ == 2)
/*     */     {
/*  42 */       this.type = EnumEnchantmentType.ARMOR_FEET;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMinEnchantability(int enchantmentLevel)
/*     */   {
/*  51 */     return baseEnchantability[this.protectionType] + (enchantmentLevel - 1) * levelEnchantability[this.protectionType];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxEnchantability(int enchantmentLevel)
/*     */   {
/*  59 */     return getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.protectionType];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxLevel()
/*     */   {
/*  67 */     return 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int calcModifierDamage(int level, DamageSource source)
/*     */   {
/*  75 */     if (source.canHarmInCreative())
/*     */     {
/*  77 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  81 */     float f = (6 + level * level) / 3.0F;
/*  82 */     return (this.protectionType == 4) && (source.isProjectile()) ? MathHelper.floor_float(f * 1.5F) : (this.protectionType == 3) && (source.isExplosion()) ? MathHelper.floor_float(f * 1.5F) : (this.protectionType == 2) && (source == DamageSource.fall) ? MathHelper.floor_float(f * 2.5F) : (this.protectionType == 1) && (source.isFireDamage()) ? MathHelper.floor_float(f * 1.25F) : this.protectionType == 0 ? MathHelper.floor_float(f * 0.75F) : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  91 */     return "enchantment.protect." + protectionName[this.protectionType];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canApplyTogether(Enchantment ench)
/*     */   {
/*  99 */     if ((ench instanceof EnchantmentProtection))
/*     */     {
/* 101 */       EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
/* 102 */       return enchantmentprotection.protectionType != this.protectionType;
/*     */     }
/*     */     
/*     */ 
/* 106 */     return super.canApplyTogether(ench);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getFireTimeForEntity(Entity p_92093_0_, int p_92093_1_)
/*     */   {
/* 115 */     int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, p_92093_0_.getInventory());
/*     */     
/* 117 */     if (i > 0)
/*     */     {
/* 119 */       p_92093_1_ -= MathHelper.floor_float(p_92093_1_ * i * 0.15F);
/*     */     }
/*     */     
/* 122 */     return p_92093_1_;
/*     */   }
/*     */   
/*     */   public static double func_92092_a(Entity p_92092_0_, double p_92092_1_)
/*     */   {
/* 127 */     int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, p_92092_0_.getInventory());
/*     */     
/* 129 */     if (i > 0)
/*     */     {
/* 131 */       p_92092_1_ -= MathHelper.floor_double(p_92092_1_ * (i * 0.15F));
/*     */     }
/*     */     
/* 134 */     return p_92092_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentProtection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */