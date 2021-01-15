/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.village.Village;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIRestrictOpenDoor extends EntityAIBase
/*    */ {
/*    */   private EntityCreature entityObj;
/*    */   private net.minecraft.village.VillageDoorInfo frontDoor;
/*    */   
/*    */   public EntityAIRestrictOpenDoor(EntityCreature creatureIn)
/*    */   {
/* 16 */     this.entityObj = creatureIn;
/*    */     
/* 18 */     if (!(creatureIn.getNavigator() instanceof PathNavigateGround))
/*    */     {
/* 20 */       throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 29 */     if (this.entityObj.worldObj.isDaytime())
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 35 */     BlockPos blockpos = new BlockPos(this.entityObj);
/* 36 */     Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 16);
/*    */     
/* 38 */     if (village == null)
/*    */     {
/* 40 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 44 */     this.frontDoor = village.getNearestDoor(blockpos);
/* 45 */     return this.frontDoor != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 55 */     return !this.entityObj.worldObj.isDaytime();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 63 */     ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(false);
/* 64 */     ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 72 */     ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(true);
/* 73 */     ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(true);
/* 74 */     this.frontDoor = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 82 */     this.frontDoor.incrementDoorOpeningRestrictionCounter();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIRestrictOpenDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */