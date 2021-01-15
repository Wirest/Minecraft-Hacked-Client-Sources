/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockRailBase;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartTNT extends EntityMinecart
/*     */ {
/*  18 */   private int minecartTNTFuse = -1;
/*     */   
/*     */   public EntityMinecartTNT(World worldIn)
/*     */   {
/*  22 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityMinecartTNT(World worldIn, double p_i1728_2_, double p_i1728_4_, double p_i1728_6_)
/*     */   {
/*  27 */     super(worldIn, p_i1728_2_, p_i1728_4_, p_i1728_6_);
/*     */   }
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType()
/*     */   {
/*  32 */     return EntityMinecart.EnumMinecartType.TNT;
/*     */   }
/*     */   
/*     */   public IBlockState getDefaultDisplayTile()
/*     */   {
/*  37 */     return Blocks.tnt.getDefaultState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  45 */     super.onUpdate();
/*     */     
/*  47 */     if (this.minecartTNTFuse > 0)
/*     */     {
/*  49 */       this.minecartTNTFuse -= 1;
/*  50 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*  52 */     else if (this.minecartTNTFuse == 0)
/*     */     {
/*  54 */       explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */     }
/*     */     
/*  57 */     if (this.isCollidedHorizontally)
/*     */     {
/*  59 */       double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*     */       
/*  61 */       if (d0 >= 0.009999999776482582D)
/*     */       {
/*  63 */         explodeCart(d0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  73 */     net.minecraft.entity.Entity entity = source.getSourceOfDamage();
/*     */     
/*  75 */     if ((entity instanceof EntityArrow))
/*     */     {
/*  77 */       EntityArrow entityarrow = (EntityArrow)entity;
/*     */       
/*  79 */       if (entityarrow.isBurning())
/*     */       {
/*  81 */         explodeCart(entityarrow.motionX * entityarrow.motionX + entityarrow.motionY * entityarrow.motionY + entityarrow.motionZ * entityarrow.motionZ);
/*     */       }
/*     */     }
/*     */     
/*  85 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */   public void killMinecart(DamageSource p_94095_1_)
/*     */   {
/*  90 */     super.killMinecart(p_94095_1_);
/*  91 */     double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;
/*     */     
/*  93 */     if ((!p_94095_1_.isExplosion()) && (this.worldObj.getGameRules().getBoolean("doEntityDrops")))
/*     */     {
/*  95 */       entityDropItem(new net.minecraft.item.ItemStack(Blocks.tnt, 1), 0.0F);
/*     */     }
/*     */     
/*  98 */     if ((p_94095_1_.isFireDamage()) || (p_94095_1_.isExplosion()) || (d0 >= 0.009999999776482582D))
/*     */     {
/* 100 */       explodeCart(d0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void explodeCart(double p_94103_1_)
/*     */   {
/* 109 */     if (!this.worldObj.isRemote)
/*     */     {
/* 111 */       double d0 = Math.sqrt(p_94103_1_);
/*     */       
/* 113 */       if (d0 > 5.0D)
/*     */       {
/* 115 */         d0 = 5.0D;
/*     */       }
/*     */       
/* 118 */       this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * d0), true);
/* 119 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */   public void fall(float distance, float damageMultiplier)
/*     */   {
/* 125 */     if (distance >= 3.0F)
/*     */     {
/* 127 */       float f = distance / 10.0F;
/* 128 */       explodeCart(f * f);
/*     */     }
/*     */     
/* 131 */     super.fall(distance, damageMultiplier);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
/*     */   {
/* 139 */     if ((receivingPower) && (this.minecartTNTFuse < 0))
/*     */     {
/* 141 */       ignite();
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 147 */     if (id == 10)
/*     */     {
/* 149 */       ignite();
/*     */     }
/*     */     else
/*     */     {
/* 153 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void ignite()
/*     */   {
/* 162 */     this.minecartTNTFuse = 80;
/*     */     
/* 164 */     if (!this.worldObj.isRemote)
/*     */     {
/* 166 */       this.worldObj.setEntityState(this, (byte)10);
/*     */       
/* 168 */       if (!isSilent())
/*     */       {
/* 170 */         this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFuseTicks()
/*     */   {
/* 180 */     return this.minecartTNTFuse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isIgnited()
/*     */   {
/* 188 */     return this.minecartTNTFuse > -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
/*     */   {
/* 196 */     return (!isIgnited()) || ((!BlockRailBase.isRailBlock(blockStateIn)) && (!BlockRailBase.isRailBlock(worldIn, pos.up()))) ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 0.0F;
/*     */   }
/*     */   
/*     */   public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
/*     */   {
/* 201 */     return (!isIgnited()) || ((!BlockRailBase.isRailBlock(blockStateIn)) && (!BlockRailBase.isRailBlock(worldIn, pos.up()))) ? super.verifyExplosion(explosionIn, worldIn, pos, blockStateIn, p_174816_5_) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 209 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 211 */     if (tagCompund.hasKey("TNTFuse", 99))
/*     */     {
/* 213 */       this.minecartTNTFuse = tagCompund.getInteger("TNTFuse");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 222 */     super.writeEntityToNBT(tagCompound);
/* 223 */     tagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityMinecartTNT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */