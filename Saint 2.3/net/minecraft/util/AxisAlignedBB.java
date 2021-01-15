package net.minecraft.util;

public class AxisAlignedBB {
   public double minX;
   public double minY;
   public double minZ;
   public double maxX;
   public double maxY;
   public double maxZ;

   public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
      this.minX = Math.min(x1, x2);
      this.minY = Math.min(y1, y2);
      this.minZ = Math.min(z1, z2);
      this.maxX = Math.max(x1, x2);
      this.maxY = Math.max(y1, y2);
      this.maxZ = Math.max(z1, z2);
   }

   public AxisAlignedBB(BlockPos p_i45554_1_, BlockPos p_i45554_2_) {
      this.minX = (double)p_i45554_1_.getX();
      this.minY = (double)p_i45554_1_.getY();
      this.minZ = (double)p_i45554_1_.getZ();
      this.maxX = (double)p_i45554_2_.getX();
      this.maxY = (double)p_i45554_2_.getY();
      this.maxZ = (double)p_i45554_2_.getZ();
   }

   public AxisAlignedBB addCoord(double x, double y, double z) {
      double var7 = this.minX;
      double var9 = this.minY;
      double var11 = this.minZ;
      double var13 = this.maxX;
      double var15 = this.maxY;
      double var17 = this.maxZ;
      if (x < 0.0D) {
         var7 += x;
      } else if (x > 0.0D) {
         var13 += x;
      }

      if (y < 0.0D) {
         var9 += y;
      } else if (y > 0.0D) {
         var15 += y;
      }

      if (z < 0.0D) {
         var11 += z;
      } else if (z > 0.0D) {
         var17 += z;
      }

      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public AxisAlignedBB expand(double x, double y, double z) {
      double var7 = this.minX - x;
      double var9 = this.minY - y;
      double var11 = this.minZ - z;
      double var13 = this.maxX + x;
      double var15 = this.maxY + y;
      double var17 = this.maxZ + z;
      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public AxisAlignedBB union(AxisAlignedBB other) {
      double var2 = Math.min(this.minX, other.minX);
      double var4 = Math.min(this.minY, other.minY);
      double var6 = Math.min(this.minZ, other.minZ);
      double var8 = Math.max(this.maxX, other.maxX);
      double var10 = Math.max(this.maxY, other.maxY);
      double var12 = Math.max(this.maxZ, other.maxZ);
      return new AxisAlignedBB(var2, var4, var6, var8, var10, var12);
   }

   public static AxisAlignedBB fromBounds(double p_178781_0_, double p_178781_2_, double p_178781_4_, double p_178781_6_, double p_178781_8_, double p_178781_10_) {
      double var12 = Math.min(p_178781_0_, p_178781_6_);
      double var14 = Math.min(p_178781_2_, p_178781_8_);
      double var16 = Math.min(p_178781_4_, p_178781_10_);
      double var18 = Math.max(p_178781_0_, p_178781_6_);
      double var20 = Math.max(p_178781_2_, p_178781_8_);
      double var22 = Math.max(p_178781_4_, p_178781_10_);
      return new AxisAlignedBB(var12, var14, var16, var18, var20, var22);
   }

   public AxisAlignedBB offset(double x, double y, double z) {
      return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
   }

   public AxisAlignedBB offsetAndUpdate(double par1, double par3, double par5) {
      this.minX += par1;
      this.minY += par3;
      this.minZ += par5;
      this.maxX += par1;
      this.maxY += par3;
      this.maxZ += par5;
      return this;
   }

   public double calculateXOffset(AxisAlignedBB other, double p_72316_2_) {
      if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
         double var4;
         if (p_72316_2_ > 0.0D && other.maxX <= this.minX) {
            var4 = this.minX - other.maxX;
            if (var4 < p_72316_2_) {
               p_72316_2_ = var4;
            }
         } else if (p_72316_2_ < 0.0D && other.minX >= this.maxX) {
            var4 = this.maxX - other.minX;
            if (var4 > p_72316_2_) {
               p_72316_2_ = var4;
            }
         }

         return p_72316_2_;
      } else {
         return p_72316_2_;
      }
   }

   public double calculateYOffset(AxisAlignedBB other, double p_72323_2_) {
      if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
         double var4;
         if (p_72323_2_ > 0.0D && other.maxY <= this.minY) {
            var4 = this.minY - other.maxY;
            if (var4 < p_72323_2_) {
               p_72323_2_ = var4;
            }
         } else if (p_72323_2_ < 0.0D && other.minY >= this.maxY) {
            var4 = this.maxY - other.minY;
            if (var4 > p_72323_2_) {
               p_72323_2_ = var4;
            }
         }

         return p_72323_2_;
      } else {
         return p_72323_2_;
      }
   }

   public double calculateZOffset(AxisAlignedBB other, double p_72322_2_) {
      if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
         double var4;
         if (p_72322_2_ > 0.0D && other.maxZ <= this.minZ) {
            var4 = this.minZ - other.maxZ;
            if (var4 < p_72322_2_) {
               p_72322_2_ = var4;
            }
         } else if (p_72322_2_ < 0.0D && other.minZ >= this.maxZ) {
            var4 = this.maxZ - other.minZ;
            if (var4 > p_72322_2_) {
               p_72322_2_ = var4;
            }
         }

         return p_72322_2_;
      } else {
         return p_72322_2_;
      }
   }

   public boolean intersectsWith(AxisAlignedBB other) {
      return other.maxX > this.minX && other.minX < this.maxX ? (other.maxY > this.minY && other.minY < this.maxY ? other.maxZ > this.minZ && other.minZ < this.maxZ : false) : false;
   }

   public boolean isVecInside(Vec3 vec) {
      return vec.xCoord > this.minX && vec.xCoord < this.maxX ? (vec.yCoord > this.minY && vec.yCoord < this.maxY ? vec.zCoord > this.minZ && vec.zCoord < this.maxZ : false) : false;
   }

   public double getAverageEdgeLength() {
      double var1 = this.maxX - this.minX;
      double var3 = this.maxY - this.minY;
      double var5 = this.maxZ - this.minZ;
      return (var1 + var3 + var5) / 3.0D;
   }

   public AxisAlignedBB contract(double x, double y, double z) {
      double var7 = this.minX + x;
      double var9 = this.minY + y;
      double var11 = this.minZ + z;
      double var13 = this.maxX - x;
      double var15 = this.maxY - y;
      double var17 = this.maxZ - z;
      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_) {
      Vec3 var3 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
      Vec3 var4 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
      Vec3 var5 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
      Vec3 var6 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
      Vec3 var7 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
      Vec3 var8 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);
      if (!this.isVecInYZ(var3)) {
         var3 = null;
      }

      if (!this.isVecInYZ(var4)) {
         var4 = null;
      }

      if (!this.isVecInXZ(var5)) {
         var5 = null;
      }

      if (!this.isVecInXZ(var6)) {
         var6 = null;
      }

      if (!this.isVecInXY(var7)) {
         var7 = null;
      }

      if (!this.isVecInXY(var8)) {
         var8 = null;
      }

      Vec3 var9 = null;
      if (var3 != null) {
         var9 = var3;
      }

      if (var4 != null && (var9 == null || p_72327_1_.squareDistanceTo(var4) < p_72327_1_.squareDistanceTo(var9))) {
         var9 = var4;
      }

      if (var5 != null && (var9 == null || p_72327_1_.squareDistanceTo(var5) < p_72327_1_.squareDistanceTo(var9))) {
         var9 = var5;
      }

      if (var6 != null && (var9 == null || p_72327_1_.squareDistanceTo(var6) < p_72327_1_.squareDistanceTo(var9))) {
         var9 = var6;
      }

      if (var7 != null && (var9 == null || p_72327_1_.squareDistanceTo(var7) < p_72327_1_.squareDistanceTo(var9))) {
         var9 = var7;
      }

      if (var8 != null && (var9 == null || p_72327_1_.squareDistanceTo(var8) < p_72327_1_.squareDistanceTo(var9))) {
         var9 = var8;
      }

      if (var9 == null) {
         return null;
      } else {
         EnumFacing var10 = null;
         if (var9 == var3) {
            var10 = EnumFacing.WEST;
         } else if (var9 == var4) {
            var10 = EnumFacing.EAST;
         } else if (var9 == var5) {
            var10 = EnumFacing.DOWN;
         } else if (var9 == var6) {
            var10 = EnumFacing.UP;
         } else if (var9 == var7) {
            var10 = EnumFacing.NORTH;
         } else {
            var10 = EnumFacing.SOUTH;
         }

         return new MovingObjectPosition(var9, var10);
      }
   }

   private boolean isVecInYZ(Vec3 vec) {
      return vec == null ? false : vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
   }

   private boolean isVecInXZ(Vec3 vec) {
      return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
   }

   private boolean isVecInXY(Vec3 vec) {
      return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
   }

   public String toString() {
      return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
   }
}
