/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ 
/*     */ public class Path
/*     */ {
/*   6 */   private PathPoint[] pathPoints = new PathPoint['Ѐ'];
/*     */   
/*     */ 
/*     */ 
/*     */   private int count;
/*     */   
/*     */ 
/*     */ 
/*     */   public PathPoint addPoint(PathPoint point)
/*     */   {
/*  16 */     if (point.index >= 0)
/*     */     {
/*  18 */       throw new IllegalStateException("OW KNOWS!");
/*     */     }
/*     */     
/*     */ 
/*  22 */     if (this.count == this.pathPoints.length)
/*     */     {
/*  24 */       PathPoint[] apathpoint = new PathPoint[this.count << 1];
/*  25 */       System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
/*  26 */       this.pathPoints = apathpoint;
/*     */     }
/*     */     
/*  29 */     this.pathPoints[this.count] = point;
/*  30 */     point.index = this.count;
/*  31 */     sortBack(this.count++);
/*  32 */     return point;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearPath()
/*     */   {
/*  41 */     this.count = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathPoint dequeue()
/*     */   {
/*  49 */     PathPoint pathpoint = this.pathPoints[0];
/*  50 */     this.pathPoints[0] = this.pathPoints[(--this.count)];
/*  51 */     this.pathPoints[this.count] = null;
/*     */     
/*  53 */     if (this.count > 0)
/*     */     {
/*  55 */       sortForward(0);
/*     */     }
/*     */     
/*  58 */     pathpoint.index = -1;
/*  59 */     return pathpoint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeDistance(PathPoint p_75850_1_, float p_75850_2_)
/*     */   {
/*  67 */     float f = p_75850_1_.distanceToTarget;
/*  68 */     p_75850_1_.distanceToTarget = p_75850_2_;
/*     */     
/*  70 */     if (p_75850_2_ < f)
/*     */     {
/*  72 */       sortBack(p_75850_1_.index);
/*     */     }
/*     */     else
/*     */     {
/*  76 */       sortForward(p_75850_1_.index);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sortBack(int p_75847_1_)
/*     */   {
/*  85 */     PathPoint pathpoint = this.pathPoints[p_75847_1_];
/*     */     
/*     */     int i;
/*  88 */     for (float f = pathpoint.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = i)
/*     */     {
/*  90 */       i = p_75847_1_ - 1 >> 1;
/*  91 */       PathPoint pathpoint1 = this.pathPoints[i];
/*     */       
/*  93 */       if (f >= pathpoint1.distanceToTarget) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*  98 */       this.pathPoints[p_75847_1_] = pathpoint1;
/*  99 */       pathpoint1.index = p_75847_1_;
/*     */     }
/*     */     
/* 102 */     this.pathPoints[p_75847_1_] = pathpoint;
/* 103 */     pathpoint.index = p_75847_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sortForward(int p_75846_1_)
/*     */   {
/* 111 */     PathPoint pathpoint = this.pathPoints[p_75846_1_];
/* 112 */     float f = pathpoint.distanceToTarget;
/*     */     
/*     */     for (;;)
/*     */     {
/* 116 */       int i = 1 + (p_75846_1_ << 1);
/* 117 */       int j = i + 1;
/*     */       
/* 119 */       if (i >= this.count) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 124 */       PathPoint pathpoint1 = this.pathPoints[i];
/* 125 */       float f1 = pathpoint1.distanceToTarget;
/*     */       float f2;
/*     */       PathPoint pathpoint2;
/*     */       float f2;
/* 129 */       if (j >= this.count)
/*     */       {
/* 131 */         PathPoint pathpoint2 = null;
/* 132 */         f2 = Float.POSITIVE_INFINITY;
/*     */       }
/*     */       else
/*     */       {
/* 136 */         pathpoint2 = this.pathPoints[j];
/* 137 */         f2 = pathpoint2.distanceToTarget;
/*     */       }
/*     */       
/* 140 */       if (f1 < f2)
/*     */       {
/* 142 */         if (f1 >= f) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 147 */         this.pathPoints[p_75846_1_] = pathpoint1;
/* 148 */         pathpoint1.index = p_75846_1_;
/* 149 */         p_75846_1_ = i;
/*     */       }
/*     */       else
/*     */       {
/* 153 */         if (f2 >= f) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 158 */         this.pathPoints[p_75846_1_] = pathpoint2;
/* 159 */         pathpoint2.index = p_75846_1_;
/* 160 */         p_75846_1_ = j;
/*     */       }
/*     */     }
/*     */     
/* 164 */     this.pathPoints[p_75846_1_] = pathpoint;
/* 165 */     pathpoint.index = p_75846_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPathEmpty()
/*     */   {
/* 173 */     return this.count == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */