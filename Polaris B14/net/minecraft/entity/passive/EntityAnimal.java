/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityAnimal extends EntityAgeable implements IAnimals
/*     */ {
/*  18 */   protected net.minecraft.block.Block spawnableBlock = net.minecraft.init.Blocks.grass;
/*     */   private int inLove;
/*     */   private EntityPlayer playerInLove;
/*     */   
/*     */   public EntityAnimal(World worldIn)
/*     */   {
/*  24 */     super(worldIn);
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/*  29 */     if (getGrowingAge() != 0)
/*     */     {
/*  31 */       this.inLove = 0;
/*     */     }
/*     */     
/*  34 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  43 */     super.onLivingUpdate();
/*     */     
/*  45 */     if (getGrowingAge() != 0)
/*     */     {
/*  47 */       this.inLove = 0;
/*     */     }
/*     */     
/*  50 */     if (this.inLove > 0)
/*     */     {
/*  52 */       this.inLove -= 1;
/*     */       
/*  54 */       if (this.inLove % 10 == 0)
/*     */       {
/*  56 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  57 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  58 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  59 */         this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  69 */     if (isEntityInvulnerable(source))
/*     */     {
/*  71 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  75 */     this.inLove = 0;
/*  76 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public float getBlockPathWeight(BlockPos pos)
/*     */   {
/*  82 */     return this.worldObj.getBlockState(pos.down()).getBlock() == net.minecraft.init.Blocks.grass ? 10.0F : this.worldObj.getLightBrightness(pos) - 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  90 */     super.writeEntityToNBT(tagCompound);
/*  91 */     tagCompound.setInteger("InLove", this.inLove);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  99 */     super.readEntityFromNBT(tagCompund);
/* 100 */     this.inLove = tagCompund.getInteger("InLove");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 108 */     int i = MathHelper.floor_double(this.posX);
/* 109 */     int j = MathHelper.floor_double(getEntityBoundingBox().minY);
/* 110 */     int k = MathHelper.floor_double(this.posZ);
/* 111 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 112 */     return (this.worldObj.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock) && (this.worldObj.getLight(blockpos) > 8) && (super.getCanSpawnHere());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTalkInterval()
/*     */   {
/* 120 */     return 120;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDespawn()
/*     */   {
/* 128 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getExperiencePoints(EntityPlayer player)
/*     */   {
/* 136 */     return 1 + this.worldObj.rand.nextInt(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBreedingItem(ItemStack stack)
/*     */   {
/* 145 */     return stack != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 153 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 155 */     if (itemstack != null)
/*     */     {
/* 157 */       if ((isBreedingItem(itemstack)) && (getGrowingAge() == 0) && (this.inLove <= 0))
/*     */       {
/* 159 */         consumeItemFromStack(player, itemstack);
/* 160 */         setInLove(player);
/* 161 */         return true;
/*     */       }
/*     */       
/* 164 */       if ((isChild()) && (isBreedingItem(itemstack)))
/*     */       {
/* 166 */         consumeItemFromStack(player, itemstack);
/* 167 */         func_175501_a((int)(-getGrowingAge() / 20 * 0.1F), true);
/* 168 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 172 */     return super.interact(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void consumeItemFromStack(EntityPlayer player, ItemStack stack)
/*     */   {
/* 180 */     if (!player.capabilities.isCreativeMode)
/*     */     {
/* 182 */       stack.stackSize -= 1;
/*     */       
/* 184 */       if (stack.stackSize <= 0)
/*     */       {
/* 186 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setInLove(EntityPlayer player)
/*     */   {
/* 193 */     this.inLove = 600;
/* 194 */     this.playerInLove = player;
/* 195 */     this.worldObj.setEntityState(this, (byte)18);
/*     */   }
/*     */   
/*     */   public EntityPlayer getPlayerInLove()
/*     */   {
/* 200 */     return this.playerInLove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInLove()
/*     */   {
/* 208 */     return this.inLove > 0;
/*     */   }
/*     */   
/*     */   public void resetInLove()
/*     */   {
/* 213 */     this.inLove = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMateWith(EntityAnimal otherAnimal)
/*     */   {
/* 221 */     return otherAnimal != this;
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 226 */     if (id == 18)
/*     */     {
/* 228 */       for (int i = 0; i < 7; i++)
/*     */       {
/* 230 */         double d0 = this.rand.nextGaussian() * 0.02D;
/* 231 */         double d1 = this.rand.nextGaussian() * 0.02D;
/* 232 */         double d2 = this.rand.nextGaussian() * 0.02D;
/* 233 */         this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 238 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */