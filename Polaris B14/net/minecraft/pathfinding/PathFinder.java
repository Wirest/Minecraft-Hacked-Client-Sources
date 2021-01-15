/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.pathfinder.NodeProcessor;
/*     */ 
/*     */ public class PathFinder
/*     */ {
/*  11 */   private Path path = new Path();
/*     */   
/*     */ 
/*  14 */   private PathPoint[] pathOptions = new PathPoint[32];
/*     */   private NodeProcessor nodeProcessor;
/*     */   
/*     */   public PathFinder(NodeProcessor nodeProcessorIn)
/*     */   {
/*  19 */     this.nodeProcessor = nodeProcessorIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityFrom, Entity entityTo, float dist)
/*     */   {
/*  27 */     return createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, dist);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, BlockPos targetPos, float dist)
/*     */   {
/*  35 */     return createEntityPathTo(blockaccess, entityIn, targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, dist);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, double x, double y, double z, float distance)
/*     */   {
/*  43 */     this.path.clearPath();
/*  44 */     this.nodeProcessor.initProcessor(blockaccess, entityIn);
/*  45 */     PathPoint pathpoint = this.nodeProcessor.getPathPointTo(entityIn);
/*  46 */     PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(entityIn, x, y, z);
/*  47 */     PathEntity pathentity = addToPath(entityIn, pathpoint, pathpoint1, distance);
/*  48 */     this.nodeProcessor.postProcess();
/*  49 */     return pathentity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathEntity addToPath(Entity entityIn, PathPoint pathpointStart, PathPoint pathpointEnd, float maxDistance)
/*     */   {
/*  57 */     pathpointStart.totalPathDistance = 0.0F;
/*  58 */     pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
/*  59 */     pathpointStart.distanceToTarget = pathpointStart.distanceToNext;
/*  60 */     this.path.clearPath();
/*  61 */     this.path.addPoint(pathpointStart);
/*  62 */     PathPoint pathpoint = pathpointStart;
/*     */     int i;
/*  64 */     int j; for (; !this.path.isPathEmpty(); 
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
/*  81 */         j < i)
/*     */     {
/*  66 */       PathPoint pathpoint1 = this.path.dequeue();
/*     */       
/*  68 */       if (pathpoint1.equals(pathpointEnd))
/*     */       {
/*  70 */         return createEntityPath(pathpointStart, pathpointEnd);
/*     */       }
/*     */       
/*  73 */       if (pathpoint1.distanceToSquared(pathpointEnd) < pathpoint.distanceToSquared(pathpointEnd))
/*     */       {
/*  75 */         pathpoint = pathpoint1;
/*     */       }
/*     */       
/*  78 */       pathpoint1.visited = true;
/*  79 */       i = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, pathpoint1, pathpointEnd, maxDistance);
/*     */       
/*  81 */       j = 0; continue;
/*     */       
/*  83 */       PathPoint pathpoint2 = this.pathOptions[j];
/*  84 */       float f = pathpoint1.totalPathDistance + pathpoint1.distanceToSquared(pathpoint2);
/*     */       
/*  86 */       if ((f < maxDistance * 2.0F) && ((!pathpoint2.isAssigned()) || (f < pathpoint2.totalPathDistance)))
/*     */       {
/*  88 */         pathpoint2.previous = pathpoint1;
/*  89 */         pathpoint2.totalPathDistance = f;
/*  90 */         pathpoint2.distanceToNext = pathpoint2.distanceToSquared(pathpointEnd);
/*     */         
/*  92 */         if (pathpoint2.isAssigned())
/*     */         {
/*  94 */           this.path.changeDistance(pathpoint2, pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
/*     */         }
/*     */         else
/*     */         {
/*  98 */           pathpoint2.distanceToTarget = (pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
/*  99 */           this.path.addPoint(pathpoint2);
/*     */         }
/*     */       }
/*  81 */       j++;
/*     */     }
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
/* 105 */     if (pathpoint == pathpointStart)
/*     */     {
/* 107 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 111 */     return createEntityPath(pathpointStart, pathpoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathEntity createEntityPath(PathPoint start, PathPoint end)
/*     */   {
/* 120 */     int i = 1;
/*     */     
/* 122 */     for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous)
/*     */     {
/* 124 */       i++;
/*     */     }
/*     */     
/* 127 */     PathPoint[] apathpoint = new PathPoint[i];
/* 128 */     PathPoint pathpoint1 = end;
/* 129 */     i--;
/*     */     
/* 131 */     for (apathpoint[i] = end; pathpoint1.previous != null; apathpoint[i] = pathpoint1)
/*     */     {
/* 133 */       pathpoint1 = pathpoint1.previous;
/* 134 */       i--;
/*     */     }
/*     */     
/* 137 */     return new PathEntity(apathpoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */