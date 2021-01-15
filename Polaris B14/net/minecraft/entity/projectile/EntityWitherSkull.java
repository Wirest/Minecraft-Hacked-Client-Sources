/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWitherSkull extends EntityFireball
/*     */ {
/*     */   public EntityWitherSkull(World worldIn)
/*     */   {
/*  20 */     super(worldIn);
/*  21 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */   
/*     */   public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
/*     */   {
/*  26 */     super(worldIn, shooter, accelX, accelY, accelZ);
/*  27 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getMotionFactor()
/*     */   {
/*  35 */     return isInvulnerable() ? 0.73F : super.getMotionFactor();
/*     */   }
/*     */   
/*     */   public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
/*     */   {
/*  40 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/*  41 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBurning()
/*     */   {
/*  49 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
/*     */   {
/*  57 */     float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
/*  58 */     net.minecraft.block.Block block = blockStateIn.getBlock();
/*     */     
/*  60 */     if ((isInvulnerable()) && (net.minecraft.entity.boss.EntityWither.func_181033_a(block)))
/*     */     {
/*  62 */       f = Math.min(0.8F, f);
/*     */     }
/*     */     
/*  65 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onImpact(MovingObjectPosition movingObject)
/*     */   {
/*  73 */     if (!this.worldObj.isRemote)
/*     */     {
/*  75 */       if (movingObject.entityHit != null)
/*     */       {
/*  77 */         if (this.shootingEntity != null)
/*     */         {
/*  79 */           if (movingObject.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
/*     */           {
/*  81 */             if (!movingObject.entityHit.isEntityAlive())
/*     */             {
/*  83 */               this.shootingEntity.heal(5.0F);
/*     */             }
/*     */             else
/*     */             {
/*  87 */               applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */         else {
/*  93 */           movingObject.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
/*     */         }
/*     */         
/*  96 */         if ((movingObject.entityHit instanceof EntityLivingBase))
/*     */         {
/*  98 */           int i = 0;
/*     */           
/* 100 */           if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
/*     */           {
/* 102 */             i = 10;
/*     */           }
/* 104 */           else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*     */           {
/* 106 */             i = 40;
/*     */           }
/*     */           
/* 109 */           if (i > 0)
/*     */           {
/* 111 */             ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * i, 1));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 116 */       this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
/* 117 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 139 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInvulnerable()
/*     */   {
/* 147 */     return this.dataWatcher.getWatchableObjectByte(10) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInvulnerable(boolean invulnerable)
/*     */   {
/* 155 */     this.dataWatcher.updateObject(10, Byte.valueOf((byte)(invulnerable ? 1 : 0)));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */