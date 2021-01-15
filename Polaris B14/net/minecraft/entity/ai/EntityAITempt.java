/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAITempt
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityCreature temptedEntity;
/*     */   private double speed;
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   private double pitch;
/*     */   private double yaw;
/*     */   private EntityPlayer temptingPlayer;
/*     */   private int delayTemptCounter;
/*     */   private boolean isRunning;
/*     */   private Item temptItem;
/*     */   private boolean scaredByPlayerMovement;
/*     */   private boolean avoidWater;
/*     */   
/*     */   public EntityAITempt(EntityCreature temptedEntityIn, double speedIn, Item temptItemIn, boolean scaredByPlayerMovementIn)
/*     */   {
/*  51 */     this.temptedEntity = temptedEntityIn;
/*  52 */     this.speed = speedIn;
/*  53 */     this.temptItem = temptItemIn;
/*  54 */     this.scaredByPlayerMovement = scaredByPlayerMovementIn;
/*  55 */     setMutexBits(3);
/*     */     
/*  57 */     if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  59 */       throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  68 */     if (this.delayTemptCounter > 0)
/*     */     {
/*  70 */       this.delayTemptCounter -= 1;
/*  71 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  75 */     this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
/*     */     
/*  77 */     if (this.temptingPlayer == null)
/*     */     {
/*  79 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  83 */     ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
/*  84 */     return itemstack != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  94 */     if (this.scaredByPlayerMovement)
/*     */     {
/*  96 */       if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D)
/*     */       {
/*  98 */         if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
/*     */         {
/* 100 */           return false;
/*     */         }
/*     */         
/* 103 */         if ((Math.abs(this.temptingPlayer.rotationPitch - this.pitch) > 5.0D) || (Math.abs(this.temptingPlayer.rotationYaw - this.yaw) > 5.0D))
/*     */         {
/* 105 */           return false;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 110 */         this.targetX = this.temptingPlayer.posX;
/* 111 */         this.targetY = this.temptingPlayer.posY;
/* 112 */         this.targetZ = this.temptingPlayer.posZ;
/*     */       }
/*     */       
/* 115 */       this.pitch = this.temptingPlayer.rotationPitch;
/* 116 */       this.yaw = this.temptingPlayer.rotationYaw;
/*     */     }
/*     */     
/* 119 */     return shouldExecute();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/* 127 */     this.targetX = this.temptingPlayer.posX;
/* 128 */     this.targetY = this.temptingPlayer.posY;
/* 129 */     this.targetZ = this.temptingPlayer.posZ;
/* 130 */     this.isRunning = true;
/* 131 */     this.avoidWater = ((PathNavigateGround)this.temptedEntity.getNavigator()).getAvoidsWater();
/* 132 */     ((PathNavigateGround)this.temptedEntity.getNavigator()).setAvoidsWater(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/* 140 */     this.temptingPlayer = null;
/* 141 */     this.temptedEntity.getNavigator().clearPathEntity();
/* 142 */     this.delayTemptCounter = 100;
/* 143 */     this.isRunning = false;
/* 144 */     ((PathNavigateGround)this.temptedEntity.getNavigator()).setAvoidsWater(this.avoidWater);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 152 */     this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, this.temptedEntity.getVerticalFaceSpeed());
/*     */     
/* 154 */     if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D)
/*     */     {
/* 156 */       this.temptedEntity.getNavigator().clearPathEntity();
/*     */     }
/*     */     else
/*     */     {
/* 160 */       this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/* 169 */     return this.isRunning;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAITempt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */