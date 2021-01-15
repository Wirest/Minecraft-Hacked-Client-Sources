package net.minecraft.pathfinding;

public class Path {
   private PathPoint[] pathPoints = new PathPoint[1024];
   private int count;
   private static final String __OBFID = "CL_00000573";

   public PathPoint addPoint(PathPoint point) {
      if (point.index >= 0) {
         throw new IllegalStateException("OW KNOWS!");
      } else {
         if (this.count == this.pathPoints.length) {
            PathPoint[] var2 = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, var2, 0, this.count);
            this.pathPoints = var2;
         }

         this.pathPoints[this.count] = point;
         point.index = this.count;
         this.sortBack(this.count++);
         return point;
      }
   }

   public void clearPath() {
      this.count = 0;
   }

   public PathPoint dequeue() {
      PathPoint var1 = this.pathPoints[0];
      this.pathPoints[0] = this.pathPoints[--this.count];
      this.pathPoints[this.count] = null;
      if (this.count > 0) {
         this.sortForward(0);
      }

      var1.index = -1;
      return var1;
   }

   public void changeDistance(PathPoint p_75850_1_, float p_75850_2_) {
      float var3 = p_75850_1_.distanceToTarget;
      p_75850_1_.distanceToTarget = p_75850_2_;
      if (p_75850_2_ < var3) {
         this.sortBack(p_75850_1_.index);
      } else {
         this.sortForward(p_75850_1_.index);
      }

   }

   private void sortBack(int p_75847_1_) {
      PathPoint var2 = this.pathPoints[p_75847_1_];

      int var4;
      for(float var3 = var2.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = var4) {
         var4 = p_75847_1_ - 1 >> 1;
         PathPoint var5 = this.pathPoints[var4];
         if (var3 >= var5.distanceToTarget) {
            break;
         }

         this.pathPoints[p_75847_1_] = var5;
         var5.index = p_75847_1_;
      }

      this.pathPoints[p_75847_1_] = var2;
      var2.index = p_75847_1_;
   }

   private void sortForward(int p_75846_1_) {
      PathPoint var2 = this.pathPoints[p_75846_1_];
      float var3 = var2.distanceToTarget;

      while(true) {
         int var4 = 1 + (p_75846_1_ << 1);
         int var5 = var4 + 1;
         if (var4 >= this.count) {
            break;
         }

         PathPoint var6 = this.pathPoints[var4];
         float var7 = var6.distanceToTarget;
         PathPoint var8;
         float var9;
         if (var5 >= this.count) {
            var8 = null;
            var9 = Float.POSITIVE_INFINITY;
         } else {
            var8 = this.pathPoints[var5];
            var9 = var8.distanceToTarget;
         }

         if (var7 < var9) {
            if (var7 >= var3) {
               break;
            }

            this.pathPoints[p_75846_1_] = var6;
            var6.index = p_75846_1_;
            p_75846_1_ = var4;
         } else {
            if (var9 >= var3) {
               break;
            }

            this.pathPoints[p_75846_1_] = var8;
            var8.index = p_75846_1_;
            p_75846_1_ = var5;
         }
      }

      this.pathPoints[p_75846_1_] = var2;
      var2.index = p_75846_1_;
   }

   public boolean isPathEmpty() {
      return this.count == 0;
   }
}
