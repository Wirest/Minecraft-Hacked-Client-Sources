/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.passive.EntityTameable;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityCreature extends EntityLiving
/*     */ {
/*  14 */   public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
/*  15 */   public static final AttributeModifier FLEEING_SPEED_MODIFIER = new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2).setSaved(false);
/*  16 */   private BlockPos homePosition = BlockPos.ORIGIN;
/*     */   
/*     */ 
/*  19 */   private float maximumHomeDistance = -1.0F;
/*  20 */   private net.minecraft.entity.ai.EntityAIBase aiBase = new net.minecraft.entity.ai.EntityAIMoveTowardsRestriction(this, 1.0D);
/*     */   private boolean isMovementAITaskSet;
/*     */   
/*     */   public EntityCreature(World worldIn)
/*     */   {
/*  25 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos)
/*     */   {
/*  30 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/*  38 */     return (super.getCanSpawnHere()) && (getBlockPathWeight(new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ)) >= 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasPath()
/*     */   {
/*  46 */     return !this.navigator.noPath();
/*     */   }
/*     */   
/*     */   public boolean isWithinHomeDistanceCurrentPosition()
/*     */   {
/*  51 */     return isWithinHomeDistanceFromPosition(new BlockPos(this));
/*     */   }
/*     */   
/*     */   public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
/*     */   {
/*  56 */     return this.maximumHomeDistance == -1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHomePosAndDistance(BlockPos pos, int distance)
/*     */   {
/*  64 */     this.homePosition = pos;
/*  65 */     this.maximumHomeDistance = distance;
/*     */   }
/*     */   
/*     */   public BlockPos getHomePosition()
/*     */   {
/*  70 */     return this.homePosition;
/*     */   }
/*     */   
/*     */   public float getMaximumHomeDistance()
/*     */   {
/*  75 */     return this.maximumHomeDistance;
/*     */   }
/*     */   
/*     */   public void detachHome()
/*     */   {
/*  80 */     this.maximumHomeDistance = -1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasHome()
/*     */   {
/*  88 */     return this.maximumHomeDistance != -1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateLeashedState()
/*     */   {
/*  96 */     super.updateLeashedState();
/*     */     
/*  98 */     if ((getLeashed()) && (getLeashedToEntity() != null) && (getLeashedToEntity().worldObj == this.worldObj))
/*     */     {
/* 100 */       Entity entity = getLeashedToEntity();
/* 101 */       setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
/* 102 */       float f = getDistanceToEntity(entity);
/*     */       
/* 104 */       if (((this instanceof EntityTameable)) && (((EntityTameable)this).isSitting()))
/*     */       {
/* 106 */         if (f > 10.0F)
/*     */         {
/* 108 */           clearLeashed(true, true);
/*     */         }
/*     */         
/* 111 */         return;
/*     */       }
/*     */       
/* 114 */       if (!this.isMovementAITaskSet)
/*     */       {
/* 116 */         this.tasks.addTask(2, this.aiBase);
/*     */         
/* 118 */         if ((getNavigator() instanceof PathNavigateGround))
/*     */         {
/* 120 */           ((PathNavigateGround)getNavigator()).setAvoidsWater(false);
/*     */         }
/*     */         
/* 123 */         this.isMovementAITaskSet = true;
/*     */       }
/*     */       
/* 126 */       func_142017_o(f);
/*     */       
/* 128 */       if (f > 4.0F)
/*     */       {
/* 130 */         getNavigator().tryMoveToEntityLiving(entity, 1.0D);
/*     */       }
/*     */       
/* 133 */       if (f > 6.0F)
/*     */       {
/* 135 */         double d0 = (entity.posX - this.posX) / f;
/* 136 */         double d1 = (entity.posY - this.posY) / f;
/* 137 */         double d2 = (entity.posZ - this.posZ) / f;
/* 138 */         this.motionX += d0 * Math.abs(d0) * 0.4D;
/* 139 */         this.motionY += d1 * Math.abs(d1) * 0.4D;
/* 140 */         this.motionZ += d2 * Math.abs(d2) * 0.4D;
/*     */       }
/*     */       
/* 143 */       if (f > 10.0F)
/*     */       {
/* 145 */         clearLeashed(true, true);
/*     */       }
/*     */     }
/* 148 */     else if ((!getLeashed()) && (this.isMovementAITaskSet))
/*     */     {
/* 150 */       this.isMovementAITaskSet = false;
/* 151 */       this.tasks.removeTask(this.aiBase);
/*     */       
/* 153 */       if ((getNavigator() instanceof PathNavigateGround))
/*     */       {
/* 155 */         ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*     */       }
/*     */       
/* 158 */       detachHome();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_142017_o(float p_142017_1_) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityCreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */