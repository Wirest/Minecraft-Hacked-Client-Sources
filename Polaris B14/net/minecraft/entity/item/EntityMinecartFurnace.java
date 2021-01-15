/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartFurnace extends EntityMinecart
/*     */ {
/*     */   private int fuel;
/*     */   public double pushX;
/*     */   public double pushZ;
/*     */   
/*     */   public EntityMinecartFurnace(World worldIn)
/*     */   {
/*  25 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityMinecartFurnace(World worldIn, double p_i1719_2_, double p_i1719_4_, double p_i1719_6_)
/*     */   {
/*  30 */     super(worldIn, p_i1719_2_, p_i1719_4_, p_i1719_6_);
/*     */   }
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType()
/*     */   {
/*  35 */     return EntityMinecart.EnumMinecartType.FURNACE;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  40 */     super.entityInit();
/*  41 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  49 */     super.onUpdate();
/*     */     
/*  51 */     if (this.fuel > 0)
/*     */     {
/*  53 */       this.fuel -= 1;
/*     */     }
/*     */     
/*  56 */     if (this.fuel <= 0)
/*     */     {
/*  58 */       this.pushX = (this.pushZ = 0.0D);
/*     */     }
/*     */     
/*  61 */     setMinecartPowered(this.fuel > 0);
/*     */     
/*  63 */     if ((isMinecartPowered()) && (this.rand.nextInt(4) == 0))
/*     */     {
/*  65 */       this.worldObj.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected double getMaximumSpeed()
/*     */   {
/*  74 */     return 0.2D;
/*     */   }
/*     */   
/*     */   public void killMinecart(DamageSource p_94095_1_)
/*     */   {
/*  79 */     super.killMinecart(p_94095_1_);
/*     */     
/*  81 */     if ((!p_94095_1_.isExplosion()) && (this.worldObj.getGameRules().getBoolean("doEntityDrops")))
/*     */     {
/*  83 */       entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_)
/*     */   {
/*  89 */     super.func_180460_a(p_180460_1_, p_180460_2_);
/*  90 */     double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
/*     */     
/*  92 */     if ((d0 > 1.0E-4D) && (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D))
/*     */     {
/*  94 */       d0 = MathHelper.sqrt_double(d0);
/*  95 */       this.pushX /= d0;
/*  96 */       this.pushZ /= d0;
/*     */       
/*  98 */       if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D)
/*     */       {
/* 100 */         this.pushX = 0.0D;
/* 101 */         this.pushZ = 0.0D;
/*     */       }
/*     */       else
/*     */       {
/* 105 */         double d1 = d0 / getMaximumSpeed();
/* 106 */         this.pushX *= d1;
/* 107 */         this.pushZ *= d1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyDrag()
/*     */   {
/* 114 */     double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
/*     */     
/* 116 */     if (d0 > 1.0E-4D)
/*     */     {
/* 118 */       d0 = MathHelper.sqrt_double(d0);
/* 119 */       this.pushX /= d0;
/* 120 */       this.pushZ /= d0;
/* 121 */       double d1 = 1.0D;
/* 122 */       this.motionX *= 0.800000011920929D;
/* 123 */       this.motionY *= 0.0D;
/* 124 */       this.motionZ *= 0.800000011920929D;
/* 125 */       this.motionX += this.pushX * d1;
/* 126 */       this.motionZ += this.pushZ * d1;
/*     */     }
/*     */     else
/*     */     {
/* 130 */       this.motionX *= 0.9800000190734863D;
/* 131 */       this.motionY *= 0.0D;
/* 132 */       this.motionZ *= 0.9800000190734863D;
/*     */     }
/*     */     
/* 135 */     super.applyDrag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer playerIn)
/*     */   {
/* 143 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*     */     
/* 145 */     if ((itemstack != null) && (itemstack.getItem() == net.minecraft.init.Items.coal))
/*     */     {
/* 147 */       if (!playerIn.capabilities.isCreativeMode) { if (--itemstack.stackSize == 0)
/*     */         {
/* 149 */           playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
/*     */         }
/*     */       }
/* 152 */       this.fuel += 3600;
/*     */     }
/*     */     
/* 155 */     this.pushX = (this.posX - playerIn.posX);
/* 156 */     this.pushZ = (this.posZ - playerIn.posZ);
/* 157 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 165 */     super.writeEntityToNBT(tagCompound);
/* 166 */     tagCompound.setDouble("PushX", this.pushX);
/* 167 */     tagCompound.setDouble("PushZ", this.pushZ);
/* 168 */     tagCompound.setShort("Fuel", (short)this.fuel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 176 */     super.readEntityFromNBT(tagCompund);
/* 177 */     this.pushX = tagCompund.getDouble("PushX");
/* 178 */     this.pushZ = tagCompund.getDouble("PushZ");
/* 179 */     this.fuel = tagCompund.getShort("Fuel");
/*     */   }
/*     */   
/*     */   protected boolean isMinecartPowered()
/*     */   {
/* 184 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   protected void setMinecartPowered(boolean p_94107_1_)
/*     */   {
/* 189 */     if (p_94107_1_)
/*     */     {
/* 191 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 0x1)));
/*     */     }
/*     */     else
/*     */     {
/* 195 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & 0xFFFFFFFE)));
/*     */     }
/*     */   }
/*     */   
/*     */   public IBlockState getDefaultDisplayTile()
/*     */   {
/* 201 */     return (isMinecartPowered() ? Blocks.lit_furnace : Blocks.furnace).getDefaultState().withProperty(net.minecraft.block.BlockFurnace.FACING, net.minecraft.util.EnumFacing.NORTH);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityMinecartFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */