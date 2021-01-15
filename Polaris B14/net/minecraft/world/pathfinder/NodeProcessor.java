/*    */ package net.minecraft.world.pathfinder;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.pathfinding.PathPoint;
/*    */ import net.minecraft.util.IntHashMap;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public abstract class NodeProcessor
/*    */ {
/*    */   protected IBlockAccess blockaccess;
/* 12 */   protected IntHashMap<PathPoint> pointMap = new IntHashMap();
/*    */   protected int entitySizeX;
/*    */   protected int entitySizeY;
/*    */   protected int entitySizeZ;
/*    */   
/*    */   public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
/*    */   {
/* 19 */     this.blockaccess = iblockaccessIn;
/* 20 */     this.pointMap.clearMap();
/* 21 */     this.entitySizeX = MathHelper.floor_float(entityIn.width + 1.0F);
/* 22 */     this.entitySizeY = MathHelper.floor_float(entityIn.height + 1.0F);
/* 23 */     this.entitySizeZ = MathHelper.floor_float(entityIn.width + 1.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void postProcess() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected PathPoint openPoint(int x, int y, int z)
/*    */   {
/* 40 */     int i = PathPoint.makeHash(x, y, z);
/* 41 */     PathPoint pathpoint = (PathPoint)this.pointMap.lookup(i);
/*    */     
/* 43 */     if (pathpoint == null)
/*    */     {
/* 45 */       pathpoint = new PathPoint(x, y, z);
/* 46 */       this.pointMap.addKey(i, pathpoint);
/*    */     }
/*    */     
/* 49 */     return pathpoint;
/*    */   }
/*    */   
/*    */   public abstract PathPoint getPathPointTo(Entity paramEntity);
/*    */   
/*    */   public abstract PathPoint getPathPointToCoords(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */   
/*    */   public abstract int findPathOptions(PathPoint[] paramArrayOfPathPoint, Entity paramEntity, PathPoint paramPathPoint1, PathPoint paramPathPoint2, float paramFloat);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\pathfinder\NodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */