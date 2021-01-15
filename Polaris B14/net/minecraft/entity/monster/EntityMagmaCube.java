/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMagmaCube extends EntitySlime
/*     */ {
/*     */   public EntityMagmaCube(World worldIn)
/*     */   {
/*  14 */     super(worldIn);
/*  15 */     this.isImmuneToFire = true;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  20 */     super.applyEntityAttributes();
/*  21 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/*  29 */     return this.worldObj.getDifficulty() != net.minecraft.world.EnumDifficulty.PEACEFUL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNotColliding()
/*     */   {
/*  37 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTotalArmorValue()
/*     */   {
/*  45 */     return getSlimeSize() * 3;
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/*  50 */     return 15728880;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/*  58 */     return 1.0F;
/*     */   }
/*     */   
/*     */   protected EnumParticleTypes getParticleType()
/*     */   {
/*  63 */     return EnumParticleTypes.FLAME;
/*     */   }
/*     */   
/*     */   protected EntitySlime createInstance()
/*     */   {
/*  68 */     return new EntityMagmaCube(this.worldObj);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/*  73 */     return Items.magma_cream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/*  81 */     Item item = getDropItem();
/*     */     
/*  83 */     if ((item != null) && (getSlimeSize() > 1))
/*     */     {
/*  85 */       int i = this.rand.nextInt(4) - 2;
/*     */       
/*  87 */       if (p_70628_2_ > 0)
/*     */       {
/*  89 */         i += this.rand.nextInt(p_70628_2_ + 1);
/*     */       }
/*     */       
/*  92 */       for (int j = 0; j < i; j++)
/*     */       {
/*  94 */         dropItem(item, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBurning()
/*     */   {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getJumpDelay()
/*     */   {
/* 112 */     return super.getJumpDelay() * 4;
/*     */   }
/*     */   
/*     */   protected void alterSquishAmount()
/*     */   {
/* 117 */     this.squishAmount *= 0.9F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void jump()
/*     */   {
/* 125 */     this.motionY = (0.42F + getSlimeSize() * 0.1F);
/* 126 */     this.isAirBorne = true;
/*     */   }
/*     */   
/*     */   protected void handleJumpLava()
/*     */   {
/* 131 */     this.motionY = (0.22F + getSlimeSize() * 0.05F);
/* 132 */     this.isAirBorne = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean canDamagePlayer()
/*     */   {
/* 144 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getAttackStrength()
/*     */   {
/* 152 */     return super.getAttackStrength() + 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getJumpSound()
/*     */   {
/* 160 */     return getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean makesSoundOnLand()
/*     */   {
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */