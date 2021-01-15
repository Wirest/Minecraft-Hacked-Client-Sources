/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathEntity
/*     */ {
/*     */   private final PathPoint[] points;
/*     */   private int currentPathIndex;
/*     */   private int pathLength;
/*     */   
/*     */   public PathEntity(PathPoint[] pathpoints)
/*     */   {
/*  19 */     this.points = pathpoints;
/*  20 */     this.pathLength = pathpoints.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incrementPathIndex()
/*     */   {
/*  28 */     this.currentPathIndex += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/*  36 */     return this.currentPathIndex >= this.pathLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathPoint getFinalPathPoint()
/*     */   {
/*  44 */     return this.pathLength > 0 ? this.points[(this.pathLength - 1)] : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathPoint getPathPointFromIndex(int index)
/*     */   {
/*  52 */     return this.points[index];
/*     */   }
/*     */   
/*     */   public int getCurrentPathLength()
/*     */   {
/*  57 */     return this.pathLength;
/*     */   }
/*     */   
/*     */   public void setCurrentPathLength(int length)
/*     */   {
/*  62 */     this.pathLength = length;
/*     */   }
/*     */   
/*     */   public int getCurrentPathIndex()
/*     */   {
/*  67 */     return this.currentPathIndex;
/*     */   }
/*     */   
/*     */   public void setCurrentPathIndex(int currentPathIndexIn)
/*     */   {
/*  72 */     this.currentPathIndex = currentPathIndexIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getVectorFromIndex(Entity entityIn, int index)
/*     */   {
/*  80 */     double d0 = this.points[index].xCoord + (int)(entityIn.width + 1.0F) * 0.5D;
/*  81 */     double d1 = this.points[index].yCoord;
/*  82 */     double d2 = this.points[index].zCoord + (int)(entityIn.width + 1.0F) * 0.5D;
/*  83 */     return new Vec3(d0, d1, d2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getPosition(Entity entityIn)
/*     */   {
/*  91 */     return getVectorFromIndex(entityIn, this.currentPathIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSamePath(PathEntity pathentityIn)
/*     */   {
/*  99 */     if (pathentityIn == null)
/*     */     {
/* 101 */       return false;
/*     */     }
/* 103 */     if (pathentityIn.points.length != this.points.length)
/*     */     {
/* 105 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 109 */     for (int i = 0; i < this.points.length; i++)
/*     */     {
/* 111 */       if ((this.points[i].xCoord != pathentityIn.points[i].xCoord) || (this.points[i].yCoord != pathentityIn.points[i].yCoord) || (this.points[i].zCoord != pathentityIn.points[i].zCoord))
/*     */       {
/* 113 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDestinationSame(Vec3 vec)
/*     */   {
/* 126 */     PathPoint pathpoint = getFinalPathPoint();
/* 127 */     return pathpoint != null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */