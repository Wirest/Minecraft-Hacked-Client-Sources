/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBat extends EntityAmbientCreature
/*     */ {
/*     */   private BlockPos spawnPosition;
/*     */   
/*     */   public EntityBat(World worldIn)
/*     */   {
/*  21 */     super(worldIn);
/*  22 */     setSize(0.5F, 0.9F);
/*  23 */     setIsBatHanging(true);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  28 */     super.entityInit();
/*  29 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/*  37 */     return 0.1F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundPitch()
/*     */   {
/*  45 */     return super.getSoundPitch() * 0.95F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  53 */     return (getIsBatHanging()) && (this.rand.nextInt(4) != 0) ? null : "mob.bat.idle";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  61 */     return "mob.bat.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  69 */     return "mob.bat.death";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBePushed()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void collideWithEntity(Entity p_82167_1_) {}
/*     */   
/*     */ 
/*     */   protected void collideWithNearbyEntities() {}
/*     */   
/*     */ 
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  90 */     super.applyEntityAttributes();
/*  91 */     getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
/*     */   }
/*     */   
/*     */   public boolean getIsBatHanging()
/*     */   {
/*  96 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public void setIsBatHanging(boolean isHanging)
/*     */   {
/* 101 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 103 */     if (isHanging)
/*     */     {
/* 105 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else
/*     */     {
/* 109 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 118 */     super.onUpdate();
/*     */     
/* 120 */     if (getIsBatHanging())
/*     */     {
/* 122 */       this.motionX = (this.motionY = this.motionZ = 0.0D);
/* 123 */       this.posY = (MathHelper.floor_double(this.posY) + 1.0D - this.height);
/*     */     }
/*     */     else
/*     */     {
/* 127 */       this.motionY *= 0.6000000238418579D;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 133 */     super.updateAITasks();
/* 134 */     BlockPos blockpos = new BlockPos(this);
/* 135 */     BlockPos blockpos1 = blockpos.up();
/*     */     
/* 137 */     if (getIsBatHanging())
/*     */     {
/* 139 */       if (!this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube())
/*     */       {
/* 141 */         setIsBatHanging(false);
/* 142 */         this.worldObj.playAuxSFXAtEntity(null, 1015, blockpos, 0);
/*     */       }
/*     */       else
/*     */       {
/* 146 */         if (this.rand.nextInt(200) == 0)
/*     */         {
/* 148 */           this.rotationYawHead = this.rand.nextInt(360);
/*     */         }
/*     */         
/* 151 */         if (this.worldObj.getClosestPlayerToEntity(this, 4.0D) != null)
/*     */         {
/* 153 */           setIsBatHanging(false);
/* 154 */           this.worldObj.playAuxSFXAtEntity(null, 1015, blockpos, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 160 */       if ((this.spawnPosition != null) && ((!this.worldObj.isAirBlock(this.spawnPosition)) || (this.spawnPosition.getY() < 1)))
/*     */       {
/* 162 */         this.spawnPosition = null;
/*     */       }
/*     */       
/* 165 */       if ((this.spawnPosition == null) || (this.rand.nextInt(30) == 0) || (this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0D))
/*     */       {
/* 167 */         this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
/*     */       }
/*     */       
/* 170 */       double d0 = this.spawnPosition.getX() + 0.5D - this.posX;
/* 171 */       double d1 = this.spawnPosition.getY() + 0.1D - this.posY;
/* 172 */       double d2 = this.spawnPosition.getZ() + 0.5D - this.posZ;
/* 173 */       this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
/* 174 */       this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
/* 175 */       this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
/* 176 */       float f = (float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0D / 3.141592653589793D) - 90.0F;
/* 177 */       float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
/* 178 */       this.moveForward = 0.5F;
/* 179 */       this.rotationYaw += f1;
/*     */       
/* 181 */       if ((this.rand.nextInt(100) == 0) && (this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube()))
/*     */       {
/* 183 */         setIsBatHanging(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/* 194 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean doesEntityNotTriggerPressurePlate()
/*     */   {
/* 210 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 218 */     if (isEntityInvulnerable(source))
/*     */     {
/* 220 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 224 */     if ((!this.worldObj.isRemote) && (getIsBatHanging()))
/*     */     {
/* 226 */       setIsBatHanging(false);
/*     */     }
/*     */     
/* 229 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 238 */     super.readEntityFromNBT(tagCompund);
/* 239 */     this.dataWatcher.updateObject(16, Byte.valueOf(tagCompund.getByte("BatFlags")));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 247 */     super.writeEntityToNBT(tagCompound);
/* 248 */     tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 256 */     BlockPos blockpos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);
/*     */     
/* 258 */     if (blockpos.getY() >= this.worldObj.func_181545_F())
/*     */     {
/* 260 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 264 */     int i = this.worldObj.getLightFromNeighbors(blockpos);
/* 265 */     int j = 4;
/*     */     
/* 267 */     if (isDateAroundHalloween(this.worldObj.getCurrentDate()))
/*     */     {
/* 269 */       j = 7;
/*     */     }
/* 271 */     else if (this.rand.nextBoolean())
/*     */     {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     return i > this.rand.nextInt(j) ? false : super.getCanSpawnHere();
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isDateAroundHalloween(Calendar p_175569_1_)
/*     */   {
/* 282 */     return ((p_175569_1_.get(2) + 1 == 10) && (p_175569_1_.get(5) >= 20)) || ((p_175569_1_.get(2) + 1 == 11) && (p_175569_1_.get(5) <= 3));
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 287 */     return this.height / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */