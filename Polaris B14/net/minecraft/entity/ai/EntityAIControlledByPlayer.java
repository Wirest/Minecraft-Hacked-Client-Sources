/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.BlockStairs;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.pathfinder.WalkNodeProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIControlledByPlayer
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final EntityLiving thisEntity;
/*     */   private final float maxSpeed;
/*     */   private float currentSpeed;
/*     */   private boolean speedBoosted;
/*     */   private int speedBoostTime;
/*     */   private int maxSpeedBoostTime;
/*     */   
/*     */   public EntityAIControlledByPlayer(EntityLiving entitylivingIn, float maxspeed)
/*     */   {
/*  35 */     this.thisEntity = entitylivingIn;
/*  36 */     this.maxSpeed = maxspeed;
/*  37 */     setMutexBits(7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  45 */     this.currentSpeed = 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  53 */     this.speedBoosted = false;
/*  54 */     this.currentSpeed = 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  62 */     return (this.thisEntity.isEntityAlive()) && (this.thisEntity.riddenByEntity != null) && ((this.thisEntity.riddenByEntity instanceof EntityPlayer)) && ((this.speedBoosted) || (this.thisEntity.canBeSteered()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  70 */     EntityPlayer entityplayer = (EntityPlayer)this.thisEntity.riddenByEntity;
/*  71 */     EntityCreature entitycreature = (EntityCreature)this.thisEntity;
/*  72 */     float f = MathHelper.wrapAngleTo180_float(entityplayer.rotationYaw - this.thisEntity.rotationYaw) * 0.5F;
/*     */     
/*  74 */     if (f > 5.0F)
/*     */     {
/*  76 */       f = 5.0F;
/*     */     }
/*     */     
/*  79 */     if (f < -5.0F)
/*     */     {
/*  81 */       f = -5.0F;
/*     */     }
/*     */     
/*  84 */     this.thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.thisEntity.rotationYaw + f);
/*     */     
/*  86 */     if (this.currentSpeed < this.maxSpeed)
/*     */     {
/*  88 */       this.currentSpeed += (this.maxSpeed - this.currentSpeed) * 0.01F;
/*     */     }
/*     */     
/*  91 */     if (this.currentSpeed > this.maxSpeed)
/*     */     {
/*  93 */       this.currentSpeed = this.maxSpeed;
/*     */     }
/*     */     
/*  96 */     int i = MathHelper.floor_double(this.thisEntity.posX);
/*  97 */     int j = MathHelper.floor_double(this.thisEntity.posY);
/*  98 */     int k = MathHelper.floor_double(this.thisEntity.posZ);
/*  99 */     float f1 = this.currentSpeed;
/*     */     
/* 101 */     if (this.speedBoosted)
/*     */     {
/* 103 */       if (this.speedBoostTime++ > this.maxSpeedBoostTime)
/*     */       {
/* 105 */         this.speedBoosted = false;
/*     */       }
/*     */       
/* 108 */       f1 += f1 * 1.15F * MathHelper.sin(this.speedBoostTime / this.maxSpeedBoostTime * 3.1415927F);
/*     */     }
/*     */     
/* 111 */     float f2 = 0.91F;
/*     */     
/* 113 */     if (this.thisEntity.onGround)
/*     */     {
/* 115 */       f2 = this.thisEntity.worldObj.getBlockState(new BlockPos(MathHelper.floor_float(i), MathHelper.floor_float(j) - 1, MathHelper.floor_float(k))).getBlock().slipperiness * 0.91F;
/*     */     }
/*     */     
/* 118 */     float f3 = 0.16277136F / (f2 * f2 * f2);
/* 119 */     float f4 = MathHelper.sin(entitycreature.rotationYaw * 3.1415927F / 180.0F);
/* 120 */     float f5 = MathHelper.cos(entitycreature.rotationYaw * 3.1415927F / 180.0F);
/* 121 */     float f6 = entitycreature.getAIMoveSpeed() * f3;
/* 122 */     float f7 = Math.max(f1, 1.0F);
/* 123 */     f7 = f6 / f7;
/* 124 */     float f8 = f1 * f7;
/* 125 */     float f9 = -(f8 * f4);
/* 126 */     float f10 = f8 * f5;
/*     */     
/* 128 */     if (MathHelper.abs(f9) > MathHelper.abs(f10))
/*     */     {
/* 130 */       if (f9 < 0.0F)
/*     */       {
/* 132 */         f9 -= this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 135 */       if (f9 > 0.0F)
/*     */       {
/* 137 */         f9 += this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 140 */       f10 = 0.0F;
/*     */     }
/*     */     else
/*     */     {
/* 144 */       f9 = 0.0F;
/*     */       
/* 146 */       if (f10 < 0.0F)
/*     */       {
/* 148 */         f10 -= this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 151 */       if (f10 > 0.0F)
/*     */       {
/* 153 */         f10 += this.thisEntity.width / 2.0F;
/*     */       }
/*     */     }
/*     */     
/* 157 */     int l = MathHelper.floor_double(this.thisEntity.posX + f9);
/* 158 */     int i1 = MathHelper.floor_double(this.thisEntity.posZ + f10);
/* 159 */     int j1 = MathHelper.floor_float(this.thisEntity.width + 1.0F);
/* 160 */     int k1 = MathHelper.floor_float(this.thisEntity.height + entityplayer.height + 1.0F);
/* 161 */     int l1 = MathHelper.floor_float(this.thisEntity.width + 1.0F);
/*     */     
/* 163 */     if ((i != l) || (k != i1))
/*     */     {
/* 165 */       Block block = this.thisEntity.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
/* 166 */       boolean flag = (!isStairOrSlab(block)) && ((block.getMaterial() != Material.air) || (!isStairOrSlab(this.thisEntity.worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock())));
/*     */       
/* 168 */       if ((flag) && (WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, l, j, i1, j1, k1, l1, false, false, true) == 0) && (1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, i, j + 1, k, j1, k1, l1, false, false, true)) && (1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, l, j + 1, i1, j1, k1, l1, false, false, true)))
/*     */       {
/* 170 */         entitycreature.getJumpHelper().setJumping();
/*     */       }
/*     */     }
/*     */     
/* 174 */     if ((!entityplayer.capabilities.isCreativeMode) && (this.currentSpeed >= this.maxSpeed * 0.5F) && (this.thisEntity.getRNG().nextFloat() < 0.006F) && (!this.speedBoosted))
/*     */     {
/* 176 */       ItemStack itemstack = entityplayer.getHeldItem();
/*     */       
/* 178 */       if ((itemstack != null) && (itemstack.getItem() == Items.carrot_on_a_stick))
/*     */       {
/* 180 */         itemstack.damageItem(1, entityplayer);
/*     */         
/* 182 */         if (itemstack.stackSize == 0)
/*     */         {
/* 184 */           ItemStack itemstack1 = new ItemStack(Items.fishing_rod);
/* 185 */           itemstack1.setTagCompound(itemstack.getTagCompound());
/* 186 */           entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 191 */     this.thisEntity.moveEntityWithHeading(0.0F, f1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isStairOrSlab(Block blockIn)
/*     */   {
/* 199 */     return ((blockIn instanceof BlockStairs)) || ((blockIn instanceof BlockSlab));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSpeedBoosted()
/*     */   {
/* 207 */     return this.speedBoosted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void boostSpeed()
/*     */   {
/* 215 */     this.speedBoosted = true;
/* 216 */     this.speedBoostTime = 0;
/* 217 */     this.maxSpeedBoostTime = (this.thisEntity.getRNG().nextInt(841) + 140);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isControlledByPlayer()
/*     */   {
/* 225 */     return (!isSpeedBoosted()) && (this.currentSpeed > this.maxSpeed * 0.3F);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIControlledByPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */