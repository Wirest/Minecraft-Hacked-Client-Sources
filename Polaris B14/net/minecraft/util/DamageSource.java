/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityFireball;
/*     */ import net.minecraft.world.Explosion;
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*  12 */   public static DamageSource inFire = new DamageSource("inFire").setFireDamage();
/*  13 */   public static DamageSource lightningBolt = new DamageSource("lightningBolt");
/*  14 */   public static DamageSource onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
/*  15 */   public static DamageSource lava = new DamageSource("lava").setFireDamage();
/*  16 */   public static DamageSource inWall = new DamageSource("inWall").setDamageBypassesArmor();
/*  17 */   public static DamageSource drown = new DamageSource("drown").setDamageBypassesArmor();
/*  18 */   public static DamageSource starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
/*  19 */   public static DamageSource cactus = new DamageSource("cactus");
/*  20 */   public static DamageSource fall = new DamageSource("fall").setDamageBypassesArmor();
/*  21 */   public static DamageSource outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
/*  22 */   public static DamageSource generic = new DamageSource("generic").setDamageBypassesArmor();
/*  23 */   public static DamageSource magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
/*  24 */   public static DamageSource wither = new DamageSource("wither").setDamageBypassesArmor();
/*  25 */   public static DamageSource anvil = new DamageSource("anvil");
/*  26 */   public static DamageSource fallingBlock = new DamageSource("fallingBlock");
/*     */   
/*     */ 
/*     */   private boolean isUnblockable;
/*     */   
/*     */ 
/*     */   private boolean isDamageAllowedInCreativeMode;
/*     */   
/*     */   private boolean damageIsAbsolute;
/*     */   
/*  36 */   private float hungerDamage = 0.3F;
/*     */   
/*     */ 
/*     */   private boolean fireDamage;
/*     */   
/*     */ 
/*     */   private boolean projectile;
/*     */   
/*     */ 
/*     */   private boolean difficultyScaled;
/*     */   
/*     */   private boolean magicDamage;
/*     */   
/*     */   private boolean explosion;
/*     */   
/*     */   public String damageType;
/*     */   
/*     */ 
/*     */   public static DamageSource causeMobDamage(EntityLivingBase mob)
/*     */   {
/*  56 */     return new EntityDamageSource("mob", mob);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DamageSource causePlayerDamage(EntityPlayer player)
/*     */   {
/*  64 */     return new EntityDamageSource("player", player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DamageSource causeArrowDamage(EntityArrow arrow, Entity p_76353_1_)
/*     */   {
/*  72 */     return new EntityDamageSourceIndirect("arrow", arrow, p_76353_1_).setProjectile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DamageSource causeFireballDamage(EntityFireball fireball, Entity p_76362_1_)
/*     */   {
/*  80 */     return p_76362_1_ == null ? new EntityDamageSourceIndirect("onFire", fireball, fireball).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", fireball, p_76362_1_).setFireDamage().setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource causeThrownDamage(Entity p_76356_0_, Entity p_76356_1_)
/*     */   {
/*  85 */     return new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_).setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource causeIndirectMagicDamage(Entity p_76354_0_, Entity p_76354_1_)
/*     */   {
/*  90 */     return new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_).setDamageBypassesArmor().setMagicDamage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DamageSource causeThornsDamage(Entity p_92087_0_)
/*     */   {
/*  98 */     return new EntityDamageSource("thorns", p_92087_0_).setIsThornsDamage().setMagicDamage();
/*     */   }
/*     */   
/*     */   public static DamageSource setExplosionSource(Explosion explosionIn)
/*     */   {
/* 103 */     return (explosionIn != null) && (explosionIn.getExplosivePlacedBy() != null) ? new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isProjectile()
/*     */   {
/* 111 */     return this.projectile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DamageSource setProjectile()
/*     */   {
/* 119 */     this.projectile = true;
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isExplosion()
/*     */   {
/* 125 */     return this.explosion;
/*     */   }
/*     */   
/*     */   public DamageSource setExplosion()
/*     */   {
/* 130 */     this.explosion = true;
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isUnblockable()
/*     */   {
/* 136 */     return this.isUnblockable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getHungerDamage()
/*     */   {
/* 144 */     return this.hungerDamage;
/*     */   }
/*     */   
/*     */   public boolean canHarmInCreative()
/*     */   {
/* 149 */     return this.isDamageAllowedInCreativeMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDamageAbsolute()
/*     */   {
/* 157 */     return this.damageIsAbsolute;
/*     */   }
/*     */   
/*     */   protected DamageSource(String damageTypeIn)
/*     */   {
/* 162 */     this.damageType = damageTypeIn;
/*     */   }
/*     */   
/*     */   public Entity getSourceOfDamage()
/*     */   {
/* 167 */     return getEntity();
/*     */   }
/*     */   
/*     */   public Entity getEntity()
/*     */   {
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   protected DamageSource setDamageBypassesArmor()
/*     */   {
/* 177 */     this.isUnblockable = true;
/* 178 */     this.hungerDamage = 0.0F;
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   protected DamageSource setDamageAllowedInCreativeMode()
/*     */   {
/* 184 */     this.isDamageAllowedInCreativeMode = true;
/* 185 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DamageSource setDamageIsAbsolute()
/*     */   {
/* 194 */     this.damageIsAbsolute = true;
/* 195 */     this.hungerDamage = 0.0F;
/* 196 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DamageSource setFireDamage()
/*     */   {
/* 204 */     this.fireDamage = true;
/* 205 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_)
/*     */   {
/* 213 */     EntityLivingBase entitylivingbase = p_151519_1_.func_94060_bK();
/* 214 */     String s = "death.attack." + this.damageType;
/* 215 */     String s1 = s + ".player";
/* 216 */     return (entitylivingbase != null) && (StatCollector.canTranslate(s1)) ? new ChatComponentTranslation(s1, new Object[] { p_151519_1_.getDisplayName(), entitylivingbase.getDisplayName() }) : new ChatComponentTranslation(s, new Object[] { p_151519_1_.getDisplayName() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFireDamage()
/*     */   {
/* 224 */     return this.fireDamage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDamageType()
/*     */   {
/* 232 */     return this.damageType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DamageSource setDifficultyScaled()
/*     */   {
/* 240 */     this.difficultyScaled = true;
/* 241 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDifficultyScaled()
/*     */   {
/* 249 */     return this.difficultyScaled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMagicDamage()
/*     */   {
/* 257 */     return this.magicDamage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DamageSource setMagicDamage()
/*     */   {
/* 265 */     this.magicDamage = true;
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isCreativePlayer()
/*     */   {
/* 271 */     Entity entity = getEntity();
/* 272 */     return ((entity instanceof EntityPlayer)) && (((EntityPlayer)entity).capabilities.isCreativeMode);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\DamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */