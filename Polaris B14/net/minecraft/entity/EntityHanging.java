/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockRedstoneDiode;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public abstract class EntityHanging extends Entity
/*     */ {
/*     */   private int tickCounter1;
/*     */   protected BlockPos hangingPosition;
/*     */   public EnumFacing facingDirection;
/*     */   
/*     */   public EntityHanging(World worldIn)
/*     */   {
/*  24 */     super(worldIn);
/*  25 */     setSize(0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public EntityHanging(World worldIn, BlockPos hangingPositionIn)
/*     */   {
/*  30 */     this(worldIn);
/*  31 */     this.hangingPosition = hangingPositionIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
/*     */   {
/*  43 */     Validate.notNull(facingDirectionIn);
/*  44 */     Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
/*  45 */     this.facingDirection = facingDirectionIn;
/*  46 */     this.prevRotationYaw = (this.rotationYaw = this.facingDirection.getHorizontalIndex() * 90);
/*  47 */     updateBoundingBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateBoundingBox()
/*     */   {
/*  55 */     if (this.facingDirection != null)
/*     */     {
/*  57 */       double d0 = this.hangingPosition.getX() + 0.5D;
/*  58 */       double d1 = this.hangingPosition.getY() + 0.5D;
/*  59 */       double d2 = this.hangingPosition.getZ() + 0.5D;
/*  60 */       double d3 = 0.46875D;
/*  61 */       double d4 = func_174858_a(getWidthPixels());
/*  62 */       double d5 = func_174858_a(getHeightPixels());
/*  63 */       d0 -= this.facingDirection.getFrontOffsetX() * 0.46875D;
/*  64 */       d2 -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
/*  65 */       d1 += d5;
/*  66 */       EnumFacing enumfacing = this.facingDirection.rotateYCCW();
/*  67 */       d0 += d4 * enumfacing.getFrontOffsetX();
/*  68 */       d2 += d4 * enumfacing.getFrontOffsetZ();
/*  69 */       this.posX = d0;
/*  70 */       this.posY = d1;
/*  71 */       this.posZ = d2;
/*  72 */       double d6 = getWidthPixels();
/*  73 */       double d7 = getHeightPixels();
/*  74 */       double d8 = getWidthPixels();
/*     */       
/*  76 */       if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
/*     */       {
/*  78 */         d8 = 1.0D;
/*     */       }
/*     */       else
/*     */       {
/*  82 */         d6 = 1.0D;
/*     */       }
/*     */       
/*  85 */       d6 /= 32.0D;
/*  86 */       d7 /= 32.0D;
/*  87 */       d8 /= 32.0D;
/*  88 */       setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
/*     */     }
/*     */   }
/*     */   
/*     */   private double func_174858_a(int p_174858_1_)
/*     */   {
/*  94 */     return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 102 */     this.prevPosX = this.posX;
/* 103 */     this.prevPosY = this.posY;
/* 104 */     this.prevPosZ = this.posZ;
/*     */     
/* 106 */     if ((this.tickCounter1++ == 100) && (!this.worldObj.isRemote))
/*     */     {
/* 108 */       this.tickCounter1 = 0;
/*     */       
/* 110 */       if ((!this.isDead) && (!onValidSurface()))
/*     */       {
/* 112 */         setDead();
/* 113 */         onBroken(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onValidSurface()
/*     */   {
/* 123 */     if (!this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty())
/*     */     {
/* 125 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 129 */     int i = Math.max(1, getWidthPixels() / 16);
/* 130 */     int j = Math.max(1, getHeightPixels() / 16);
/* 131 */     BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
/* 132 */     EnumFacing enumfacing = this.facingDirection.rotateYCCW();
/*     */     int l;
/* 134 */     for (int k = 0; k < i; k++)
/*     */     {
/* 136 */       for (l = 0; l < j; l++)
/*     */       {
/* 138 */         BlockPos blockpos1 = blockpos.offset(enumfacing, k).up(l);
/* 139 */         Block block = this.worldObj.getBlockState(blockpos1).getBlock();
/*     */         
/* 141 */         if ((!block.getMaterial().isSolid()) && (!BlockRedstoneDiode.isRedstoneRepeaterBlockID(block)))
/*     */         {
/* 143 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 148 */     for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()))
/*     */     {
/* 150 */       if ((entity instanceof EntityHanging))
/*     */       {
/* 152 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 156 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 165 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hitByEntity(Entity entityIn)
/*     */   {
/* 173 */     return (entityIn instanceof EntityPlayer) ? attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
/*     */   }
/*     */   
/*     */   public EnumFacing getHorizontalFacing()
/*     */   {
/* 178 */     return this.facingDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 186 */     if (isEntityInvulnerable(source))
/*     */     {
/* 188 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 192 */     if ((!this.isDead) && (!this.worldObj.isRemote))
/*     */     {
/* 194 */       setDead();
/* 195 */       setBeenAttacked();
/* 196 */       onBroken(source.getEntity());
/*     */     }
/*     */     
/* 199 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveEntity(double x, double y, double z)
/*     */   {
/* 208 */     if ((!this.worldObj.isRemote) && (!this.isDead) && (x * x + y * y + z * z > 0.0D))
/*     */     {
/* 210 */       setDead();
/* 211 */       onBroken(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addVelocity(double x, double y, double z)
/*     */   {
/* 220 */     if ((!this.worldObj.isRemote) && (!this.isDead) && (x * x + y * y + z * z > 0.0D))
/*     */     {
/* 222 */       setDead();
/* 223 */       onBroken(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 232 */     tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
/* 233 */     tagCompound.setInteger("TileX", getHangingPosition().getX());
/* 234 */     tagCompound.setInteger("TileY", getHangingPosition().getY());
/* 235 */     tagCompound.setInteger("TileZ", getHangingPosition().getZ());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 243 */     this.hangingPosition = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
/*     */     
/*     */     EnumFacing enumfacing;
/* 246 */     if (tagCompund.hasKey("Direction", 99))
/*     */     {
/* 248 */       EnumFacing enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
/* 249 */       this.hangingPosition = this.hangingPosition.offset(enumfacing);
/*     */     } else { EnumFacing enumfacing;
/* 251 */       if (tagCompund.hasKey("Facing", 99))
/*     */       {
/* 253 */         enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
/*     */       }
/*     */       else
/*     */       {
/* 257 */         enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
/*     */       }
/*     */     }
/* 260 */     updateFacingWithBoundingBox(enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int getWidthPixels();
/*     */   
/*     */ 
/*     */   public abstract int getHeightPixels();
/*     */   
/*     */ 
/*     */   public abstract void onBroken(Entity paramEntity);
/*     */   
/*     */   protected boolean shouldSetPosAfterLoading()
/*     */   {
/* 274 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPosition(double x, double y, double z)
/*     */   {
/* 282 */     this.posX = x;
/* 283 */     this.posY = y;
/* 284 */     this.posZ = z;
/* 285 */     BlockPos blockpos = this.hangingPosition;
/* 286 */     this.hangingPosition = new BlockPos(x, y, z);
/*     */     
/* 288 */     if (!this.hangingPosition.equals(blockpos))
/*     */     {
/* 290 */       updateBoundingBox();
/* 291 */       this.isAirBorne = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public BlockPos getHangingPosition()
/*     */   {
/* 297 */     return this.hangingPosition;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityHanging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */