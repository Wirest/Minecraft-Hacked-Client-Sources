/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.village.VillageDoorInfo;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIMoveIndoors extends EntityAIBase
/*     */ {
/*     */   private EntityCreature entityObj;
/*     */   private VillageDoorInfo doorInfo;
/*  13 */   private int insidePosX = -1;
/*  14 */   private int insidePosZ = -1;
/*     */   
/*     */   public EntityAIMoveIndoors(EntityCreature entityObjIn)
/*     */   {
/*  18 */     this.entityObj = entityObjIn;
/*  19 */     setMutexBits(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  27 */     BlockPos blockpos = new BlockPos(this.entityObj);
/*     */     
/*  29 */     if (((!this.entityObj.worldObj.isDaytime()) || ((this.entityObj.worldObj.isRaining()) && (!this.entityObj.worldObj.getBiomeGenForCoords(blockpos).canSpawnLightningBolt()))) && (!this.entityObj.worldObj.provider.getHasNoSky()))
/*     */     {
/*  31 */       if (this.entityObj.getRNG().nextInt(50) != 0)
/*     */       {
/*  33 */         return false;
/*     */       }
/*  35 */       if ((this.insidePosX != -1) && (this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0D))
/*     */       {
/*  37 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  41 */       net.minecraft.village.Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 14);
/*     */       
/*  43 */       if (village == null)
/*     */       {
/*  45 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  49 */       this.doorInfo = village.getDoorInfo(blockpos);
/*  50 */       return this.doorInfo != null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  65 */     return !this.entityObj.getNavigator().noPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  73 */     this.insidePosX = -1;
/*  74 */     BlockPos blockpos = this.doorInfo.getInsideBlockPos();
/*  75 */     int i = blockpos.getX();
/*  76 */     int j = blockpos.getY();
/*  77 */     int k = blockpos.getZ();
/*     */     
/*  79 */     if (this.entityObj.getDistanceSq(blockpos) > 256.0D)
/*     */     {
/*  81 */       Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3(i + 0.5D, j, k + 0.5D));
/*     */       
/*  83 */       if (vec3 != null)
/*     */       {
/*  85 */         this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  90 */       this.entityObj.getNavigator().tryMoveToXYZ(i + 0.5D, j, k + 0.5D, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  99 */     this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
/* 100 */     this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
/* 101 */     this.doorInfo = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIMoveIndoors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */