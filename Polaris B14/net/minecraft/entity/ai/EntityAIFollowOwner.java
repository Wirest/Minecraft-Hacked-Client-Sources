/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityTameable;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIFollowOwner extends EntityAIBase
/*     */ {
/*     */   private EntityTameable thePet;
/*     */   private EntityLivingBase theOwner;
/*     */   World theWorld;
/*     */   private double followSpeed;
/*     */   private PathNavigate petPathfinder;
/*     */   private int field_75343_h;
/*     */   float maxDist;
/*     */   float minDist;
/*     */   private boolean field_75344_i;
/*     */   
/*     */   public EntityAIFollowOwner(EntityTameable thePetIn, double followSpeedIn, float minDistIn, float maxDistIn)
/*     */   {
/*  29 */     this.thePet = thePetIn;
/*  30 */     this.theWorld = thePetIn.worldObj;
/*  31 */     this.followSpeed = followSpeedIn;
/*  32 */     this.petPathfinder = thePetIn.getNavigator();
/*  33 */     this.minDist = minDistIn;
/*  34 */     this.maxDist = maxDistIn;
/*  35 */     setMutexBits(3);
/*     */     
/*  37 */     if (!(thePetIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  39 */       throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  48 */     EntityLivingBase entitylivingbase = this.thePet.getOwner();
/*     */     
/*  50 */     if (entitylivingbase == null)
/*     */     {
/*  52 */       return false;
/*     */     }
/*  54 */     if (((entitylivingbase instanceof EntityPlayer)) && (((EntityPlayer)entitylivingbase).isSpectator()))
/*     */     {
/*  56 */       return false;
/*     */     }
/*  58 */     if (this.thePet.isSitting())
/*     */     {
/*  60 */       return false;
/*     */     }
/*  62 */     if (this.thePet.getDistanceSqToEntity(entitylivingbase) < this.minDist * this.minDist)
/*     */     {
/*  64 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  68 */     this.theOwner = entitylivingbase;
/*  69 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  78 */     return (!this.petPathfinder.noPath()) && (this.thePet.getDistanceSqToEntity(this.theOwner) > this.maxDist * this.maxDist) && (!this.thePet.isSitting());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  86 */     this.field_75343_h = 0;
/*  87 */     this.field_75344_i = ((PathNavigateGround)this.thePet.getNavigator()).getAvoidsWater();
/*  88 */     ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  96 */     this.theOwner = null;
/*  97 */     this.petPathfinder.clearPathEntity();
/*  98 */     ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(true);
/*     */   }
/*     */   
/*     */   private boolean func_181065_a(BlockPos p_181065_1_)
/*     */   {
/* 103 */     IBlockState iblockstate = this.theWorld.getBlockState(p_181065_1_);
/* 104 */     Block block = iblockstate.getBlock();
/* 105 */     return block == net.minecraft.init.Blocks.air;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 113 */     this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, this.thePet.getVerticalFaceSpeed());
/*     */     
/* 115 */     if (!this.thePet.isSitting())
/*     */     {
/* 117 */       if (--this.field_75343_h <= 0)
/*     */       {
/* 119 */         this.field_75343_h = 10;
/*     */         
/* 121 */         if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed))
/*     */         {
/* 123 */           if (!this.thePet.getLeashed())
/*     */           {
/* 125 */             if (this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0D)
/*     */             {
/* 127 */               int i = MathHelper.floor_double(this.theOwner.posX) - 2;
/* 128 */               int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
/* 129 */               int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
/*     */               
/* 131 */               for (int l = 0; l <= 4; l++)
/*     */               {
/* 133 */                 for (int i1 = 0; i1 <= 4; i1++)
/*     */                 {
/* 135 */                   if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3)) && (World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1))) && (func_181065_a(new BlockPos(i + l, k, j + i1))) && (func_181065_a(new BlockPos(i + l, k + 1, j + i1))))
/*     */                   {
/* 137 */                     this.thePet.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, this.thePet.rotationYaw, this.thePet.rotationPitch);
/* 138 */                     this.petPathfinder.clearPathEntity();
/* 139 */                     return;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIFollowOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */