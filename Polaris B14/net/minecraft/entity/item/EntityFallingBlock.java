/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockAnvil;
/*     */ import net.minecraft.block.BlockFalling;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityFallingBlock extends Entity
/*     */ {
/*     */   private IBlockState fallTile;
/*     */   public int fallTime;
/*  29 */   public boolean shouldDropItem = true;
/*     */   private boolean canSetAsBlock;
/*     */   private boolean hurtEntities;
/*  32 */   private int fallHurtMax = 40;
/*  33 */   private float fallHurtAmount = 2.0F;
/*     */   public NBTTagCompound tileEntityData;
/*     */   
/*     */   public EntityFallingBlock(World worldIn)
/*     */   {
/*  38 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState)
/*     */   {
/*  43 */     super(worldIn);
/*  44 */     this.fallTile = fallingBlockState;
/*  45 */     this.preventEntitySpawning = true;
/*  46 */     setSize(0.98F, 0.98F);
/*  47 */     setPosition(x, y, z);
/*  48 */     this.motionX = 0.0D;
/*  49 */     this.motionY = 0.0D;
/*  50 */     this.motionZ = 0.0D;
/*  51 */     this.prevPosX = x;
/*  52 */     this.prevPosY = y;
/*  53 */     this.prevPosZ = z;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  62 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/*  74 */     return !this.isDead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  82 */     Block block = this.fallTile.getBlock();
/*     */     
/*  84 */     if (block.getMaterial() == Material.air)
/*     */     {
/*  86 */       setDead();
/*     */     }
/*     */     else
/*     */     {
/*  90 */       this.prevPosX = this.posX;
/*  91 */       this.prevPosY = this.posY;
/*  92 */       this.prevPosZ = this.posZ;
/*     */       
/*  94 */       if (this.fallTime++ == 0)
/*     */       {
/*  96 */         BlockPos blockpos = new BlockPos(this);
/*     */         
/*  98 */         if (this.worldObj.getBlockState(blockpos).getBlock() == block)
/*     */         {
/* 100 */           this.worldObj.setBlockToAir(blockpos);
/*     */         }
/* 102 */         else if (!this.worldObj.isRemote)
/*     */         {
/* 104 */           setDead();
/* 105 */           return;
/*     */         }
/*     */       }
/*     */       
/* 109 */       this.motionY -= 0.03999999910593033D;
/* 110 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 111 */       this.motionX *= 0.9800000190734863D;
/* 112 */       this.motionY *= 0.9800000190734863D;
/* 113 */       this.motionZ *= 0.9800000190734863D;
/*     */       
/* 115 */       if (!this.worldObj.isRemote)
/*     */       {
/* 117 */         BlockPos blockpos1 = new BlockPos(this);
/*     */         
/* 119 */         if (this.onGround)
/*     */         {
/* 121 */           this.motionX *= 0.699999988079071D;
/* 122 */           this.motionZ *= 0.699999988079071D;
/* 123 */           this.motionY *= -0.5D;
/*     */           
/* 125 */           if (this.worldObj.getBlockState(blockpos1).getBlock() != Blocks.piston_extension)
/*     */           {
/* 127 */             setDead();
/*     */             
/* 129 */             if (!this.canSetAsBlock)
/*     */             {
/* 131 */               if ((this.worldObj.canBlockBePlaced(block, blockpos1, true, net.minecraft.util.EnumFacing.UP, null, null)) && (!BlockFalling.canFallInto(this.worldObj, blockpos1.down())) && (this.worldObj.setBlockState(blockpos1, this.fallTile, 3)))
/*     */               {
/* 133 */                 if ((block instanceof BlockFalling))
/*     */                 {
/* 135 */                   ((BlockFalling)block).onEndFalling(this.worldObj, blockpos1);
/*     */                 }
/*     */                 
/* 138 */                 if ((this.tileEntityData != null) && ((block instanceof net.minecraft.block.ITileEntityProvider)))
/*     */                 {
/* 140 */                   TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);
/*     */                   
/* 142 */                   if (tileentity != null)
/*     */                   {
/* 144 */                     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 145 */                     tileentity.writeToNBT(nbttagcompound);
/*     */                     
/* 147 */                     for (String s : this.tileEntityData.getKeySet())
/*     */                     {
/* 149 */                       NBTBase nbtbase = this.tileEntityData.getTag(s);
/*     */                       
/* 151 */                       if ((!s.equals("x")) && (!s.equals("y")) && (!s.equals("z")))
/*     */                       {
/* 153 */                         nbttagcompound.setTag(s, nbtbase.copy());
/*     */                       }
/*     */                     }
/*     */                     
/* 157 */                     tileentity.readFromNBT(nbttagcompound);
/* 158 */                     tileentity.markDirty();
/*     */                   }
/*     */                 }
/*     */               }
/* 162 */               else if ((this.shouldDropItem) && (this.worldObj.getGameRules().getBoolean("doEntityDrops")))
/*     */               {
/* 164 */                 entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 169 */         else if (((this.fallTime > 100) && (!this.worldObj.isRemote) && ((blockpos1.getY() < 1) || (blockpos1.getY() > 256))) || (this.fallTime > 600))
/*     */         {
/* 171 */           if ((this.shouldDropItem) && (this.worldObj.getGameRules().getBoolean("doEntityDrops")))
/*     */           {
/* 173 */             entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
/*     */           }
/*     */           
/* 176 */           setDead();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void fall(float distance, float damageMultiplier)
/*     */   {
/* 184 */     Block block = this.fallTile.getBlock();
/*     */     
/* 186 */     if (this.hurtEntities)
/*     */     {
/* 188 */       int i = MathHelper.ceiling_float_int(distance - 1.0F);
/*     */       
/* 190 */       if (i > 0)
/*     */       {
/* 192 */         java.util.List<Entity> list = com.google.common.collect.Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
/* 193 */         boolean flag = block == Blocks.anvil;
/* 194 */         DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
/*     */         
/* 196 */         for (Entity entity : list)
/*     */         {
/* 198 */           entity.attackEntityFrom(damagesource, Math.min(MathHelper.floor_float(i * this.fallHurtAmount), this.fallHurtMax));
/*     */         }
/*     */         
/* 201 */         if ((flag) && (this.rand.nextFloat() < 0.05000000074505806D + i * 0.05D))
/*     */         {
/* 203 */           int j = ((Integer)this.fallTile.getValue(BlockAnvil.DAMAGE)).intValue();
/* 204 */           j++;
/*     */           
/* 206 */           if (j > 2)
/*     */           {
/* 208 */             this.canSetAsBlock = true;
/*     */           }
/*     */           else
/*     */           {
/* 212 */             this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(j));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 224 */     Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.air;
/* 225 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(block);
/* 226 */     tagCompound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
/* 227 */     tagCompound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
/* 228 */     tagCompound.setByte("Time", (byte)this.fallTime);
/* 229 */     tagCompound.setBoolean("DropItem", this.shouldDropItem);
/* 230 */     tagCompound.setBoolean("HurtEntities", this.hurtEntities);
/* 231 */     tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
/* 232 */     tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
/*     */     
/* 234 */     if (this.tileEntityData != null)
/*     */     {
/* 236 */       tagCompound.setTag("TileEntityData", this.tileEntityData);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 245 */     int i = tagCompund.getByte("Data") & 0xFF;
/*     */     
/* 247 */     if (tagCompund.hasKey("Block", 8))
/*     */     {
/* 249 */       this.fallTile = Block.getBlockFromName(tagCompund.getString("Block")).getStateFromMeta(i);
/*     */     }
/* 251 */     else if (tagCompund.hasKey("TileID", 99))
/*     */     {
/* 253 */       this.fallTile = Block.getBlockById(tagCompund.getInteger("TileID")).getStateFromMeta(i);
/*     */     }
/*     */     else
/*     */     {
/* 257 */       this.fallTile = Block.getBlockById(tagCompund.getByte("Tile") & 0xFF).getStateFromMeta(i);
/*     */     }
/*     */     
/* 260 */     this.fallTime = (tagCompund.getByte("Time") & 0xFF);
/* 261 */     Block block = this.fallTile.getBlock();
/*     */     
/* 263 */     if (tagCompund.hasKey("HurtEntities", 99))
/*     */     {
/* 265 */       this.hurtEntities = tagCompund.getBoolean("HurtEntities");
/* 266 */       this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
/* 267 */       this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
/*     */     }
/* 269 */     else if (block == Blocks.anvil)
/*     */     {
/* 271 */       this.hurtEntities = true;
/*     */     }
/*     */     
/* 274 */     if (tagCompund.hasKey("DropItem", 99))
/*     */     {
/* 276 */       this.shouldDropItem = tagCompund.getBoolean("DropItem");
/*     */     }
/*     */     
/* 279 */     if (tagCompund.hasKey("TileEntityData", 10))
/*     */     {
/* 281 */       this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
/*     */     }
/*     */     
/* 284 */     if ((block == null) || (block.getMaterial() == Material.air))
/*     */     {
/* 286 */       this.fallTile = Blocks.sand.getDefaultState();
/*     */     }
/*     */   }
/*     */   
/*     */   public World getWorldObj()
/*     */   {
/* 292 */     return this.worldObj;
/*     */   }
/*     */   
/*     */   public void setHurtEntities(boolean p_145806_1_)
/*     */   {
/* 297 */     this.hurtEntities = p_145806_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canRenderOnFire()
/*     */   {
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   public void addEntityCrashInfo(CrashReportCategory category)
/*     */   {
/* 310 */     super.addEntityCrashInfo(category);
/*     */     
/* 312 */     if (this.fallTile != null)
/*     */     {
/* 314 */       Block block = this.fallTile.getBlock();
/* 315 */       category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
/* 316 */       category.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(this.fallTile)));
/*     */     }
/*     */   }
/*     */   
/*     */   public IBlockState getBlock()
/*     */   {
/* 322 */     return this.fallTile;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */