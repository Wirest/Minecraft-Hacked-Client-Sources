package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class PathEntity {
   private final PathPoint[] points;
   private int currentPathIndex;
   private int pathLength;
   private static final String __OBFID = "CL_00000575";

   public PathEntity(PathPoint[] p_i2136_1_) {
      this.points = p_i2136_1_;
      this.pathLength = p_i2136_1_.length;
   }

   public void incrementPathIndex() {
      ++this.currentPathIndex;
   }

   public boolean isFinished() {
      return this.currentPathIndex >= this.pathLength;
   }

   public PathPoint getFinalPathPoint() {
      return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
   }

   public PathPoint getPathPointFromIndex(int p_75877_1_) {
      return this.points[p_75877_1_];
   }

   public int getCurrentPathLength() {
      return this.pathLength;
   }

   public void setCurrentPathLength(int p_75871_1_) {
      this.pathLength = p_75871_1_;
   }

   public int getCurrentPathIndex() {
      return this.currentPathIndex;
   }

   public void setCurrentPathIndex(int p_75872_1_) {
      this.currentPathIndex = p_75872_1_;
   }

   public Vec3 getVectorFromIndex(Entity p_75881_1_, int p_75881_2_) {
      double var3 = (double)this.points[p_75881_2_].xCoord + (double)((int)(p_75881_1_.width + 1.0F)) * 0.5D;
      double var5 = (double)this.points[p_75881_2_].yCoord;
      double var7 = (double)this.points[p_75881_2_].zCoord + (double)((int)(p_75881_1_.width + 1.0F)) * 0.5D;
      return new Vec3(var3, var5, var7);
   }

   public Vec3 getPosition(Entity p_75878_1_) {
      return this.getVectorFromIndex(p_75878_1_, this.currentPathIndex);
   }

   public boolean isSamePath(PathEntity p_75876_1_) {
      if (p_75876_1_ == null) {
         return false;
      } else if (p_75876_1_.points.length != this.points.length) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.points.length; ++var2) {
            if (this.points[var2].xCoord != p_75876_1_.points[var2].xCoord || this.points[var2].yCoord != p_75876_1_.points[var2].yCoord || this.points[var2].zCoord != p_75876_1_.points[var2].zCoord) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isDestinationSame(Vec3 p_75880_1_) {
      PathPoint var2 = this.getFinalPathPoint();
      return var2 == null ? false : var2.xCoord == (int)p_75880_1_.xCoord && var2.zCoord == (int)p_75880_1_.zCoord;
   }
}
