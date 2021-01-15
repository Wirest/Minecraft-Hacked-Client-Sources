/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityMob extends EntityCreature implements IMob
/*     */ {
/*     */   public EntityMob(World worldIn)
/*     */   {
/*  19 */     super(worldIn);
/*  20 */     this.experienceValue = 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  29 */     updateArmSwingProgress();
/*  30 */     float f = getBrightness(1.0F);
/*     */     
/*  32 */     if (f > 0.5F)
/*     */     {
/*  34 */       this.entityAge += 2;
/*     */     }
/*     */     
/*  37 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  45 */     super.onUpdate();
/*     */     
/*  47 */     if ((!this.worldObj.isRemote) && (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL))
/*     */     {
/*  49 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getSwimSound()
/*     */   {
/*  55 */     return "game.hostile.swim";
/*     */   }
/*     */   
/*     */   protected String getSplashSound()
/*     */   {
/*  60 */     return "game.hostile.swim.splash";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  68 */     if (isEntityInvulnerable(source))
/*     */     {
/*  70 */       return false;
/*     */     }
/*  72 */     if (super.attackEntityFrom(source, amount))
/*     */     {
/*  74 */       Entity entity = source.getEntity();
/*  75 */       return (this.riddenByEntity != entity) && (this.ridingEntity != entity);
/*     */     }
/*     */     
/*     */ 
/*  79 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  88 */     return "game.hostile.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  96 */     return "game.hostile.die";
/*     */   }
/*     */   
/*     */   protected String getFallSoundString(int damageValue)
/*     */   {
/* 101 */     return damageValue > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 106 */     float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
/* 107 */     int i = 0;
/*     */     
/* 109 */     if ((entityIn instanceof EntityLivingBase))
/*     */     {
/* 111 */       f += EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase)entityIn).getCreatureAttribute());
/* 112 */       i += EnchantmentHelper.getKnockbackModifier(this);
/*     */     }
/*     */     
/* 115 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
/*     */     
/* 117 */     if (flag)
/*     */     {
/* 119 */       if (i > 0)
/*     */       {
/* 121 */         entityIn.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F);
/* 122 */         this.motionX *= 0.6D;
/* 123 */         this.motionZ *= 0.6D;
/*     */       }
/*     */       
/* 126 */       int j = EnchantmentHelper.getFireAspectModifier(this);
/*     */       
/* 128 */       if (j > 0)
/*     */       {
/* 130 */         entityIn.setFire(j * 4);
/*     */       }
/*     */       
/* 133 */       applyEnchantments(this, entityIn);
/*     */     }
/*     */     
/* 136 */     return flag;
/*     */   }
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos)
/*     */   {
/* 141 */     return 0.5F - this.worldObj.getLightBrightness(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidLightLevel()
/*     */   {
/* 149 */     BlockPos blockpos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);
/*     */     
/* 151 */     if (this.worldObj.getLightFor(net.minecraft.world.EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
/*     */     {
/* 153 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 157 */     int i = this.worldObj.getLightFromNeighbors(blockpos);
/*     */     
/* 159 */     if (this.worldObj.isThundering())
/*     */     {
/* 161 */       int j = this.worldObj.getSkylightSubtracted();
/* 162 */       this.worldObj.setSkylightSubtracted(10);
/* 163 */       i = this.worldObj.getLightFromNeighbors(blockpos);
/* 164 */       this.worldObj.setSkylightSubtracted(j);
/*     */     }
/*     */     
/* 167 */     return i <= this.rand.nextInt(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 176 */     return (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel()) && (super.getCanSpawnHere());
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 181 */     super.applyEntityAttributes();
/* 182 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDropLoot()
/*     */   {
/* 190 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */