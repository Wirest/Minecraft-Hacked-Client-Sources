/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIFleeSun extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theCreature;
/*    */   private double shelterX;
/*    */   private double shelterY;
/*    */   private double shelterZ;
/*    */   private double movementSpeed;
/*    */   private World theWorld;
/*    */   
/*    */   public EntityAIFleeSun(EntityCreature theCreatureIn, double movementSpeedIn)
/*    */   {
/* 20 */     this.theCreature = theCreatureIn;
/* 21 */     this.movementSpeed = movementSpeedIn;
/* 22 */     this.theWorld = theCreatureIn.worldObj;
/* 23 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 31 */     if (!this.theWorld.isDaytime())
/*    */     {
/* 33 */       return false;
/*    */     }
/* 35 */     if (!this.theCreature.isBurning())
/*    */     {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ)))
/*    */     {
/* 41 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 45 */     Vec3 vec3 = findPossibleShelter();
/*    */     
/* 47 */     if (vec3 == null)
/*    */     {
/* 49 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 53 */     this.shelterX = vec3.xCoord;
/* 54 */     this.shelterY = vec3.yCoord;
/* 55 */     this.shelterZ = vec3.zCoord;
/* 56 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 66 */     return !this.theCreature.getNavigator().noPath();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 74 */     this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
/*    */   }
/*    */   
/*    */   private Vec3 findPossibleShelter()
/*    */   {
/* 79 */     Random random = this.theCreature.getRNG();
/* 80 */     BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
/*    */     
/* 82 */     for (int i = 0; i < 10; i++)
/*    */     {
/* 84 */       BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
/*    */       
/* 86 */       if ((!this.theWorld.canSeeSky(blockpos1)) && (this.theCreature.getBlockPathWeight(blockpos1) < 0.0F))
/*    */       {
/* 88 */         return new Vec3(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
/*    */       }
/*    */     }
/*    */     
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIFleeSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */