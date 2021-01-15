/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.profiler.Profiler;
/*    */ 
/*    */ public class EntitySenses
/*    */ {
/*    */   EntityLiving entityObj;
/* 11 */   List<Entity> seenEntities = com.google.common.collect.Lists.newArrayList();
/* 12 */   List<Entity> unseenEntities = com.google.common.collect.Lists.newArrayList();
/*    */   
/*    */   public EntitySenses(EntityLiving entityObjIn)
/*    */   {
/* 16 */     this.entityObj = entityObjIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void clearSensingCache()
/*    */   {
/* 24 */     this.seenEntities.clear();
/* 25 */     this.unseenEntities.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canSee(Entity entityIn)
/*    */   {
/* 33 */     if (this.seenEntities.contains(entityIn))
/*    */     {
/* 35 */       return true;
/*    */     }
/* 37 */     if (this.unseenEntities.contains(entityIn))
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 43 */     this.entityObj.worldObj.theProfiler.startSection("canSee");
/* 44 */     boolean flag = this.entityObj.canEntityBeSeen(entityIn);
/* 45 */     this.entityObj.worldObj.theProfiler.endSection();
/*    */     
/* 47 */     if (flag)
/*    */     {
/* 49 */       this.seenEntities.add(entityIn);
/*    */     }
/*    */     else
/*    */     {
/* 53 */       this.unseenEntities.add(entityIn);
/*    */     }
/*    */     
/* 56 */     return flag;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntitySenses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */