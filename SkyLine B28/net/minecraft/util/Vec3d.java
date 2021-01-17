package net.minecraft.util;

import net.minecraft.util.MathHelper;

public class Vec3d
{
  public final double xCoord;
  public final double yCoord;
  public final double zCoord;
  private static final String __OBFID = "CL_00000612";
  
  public Vec3d(double x, double y, double z)
  {
    if (x == -0.0D) {
      x = 0.0D;
    }
    if (y == -0.0D) {
      y = 0.0D;
    }
    if (z == -0.0D) {
      z = 0.0D;
    }
    this.xCoord = x;
    this.yCoord = y;
    this.zCoord = z;
  }
  
  public Vec3d(Vec3i vector)
  {
    this(vector.getX(), vector.getY(), vector.getZ());
  }
  
  public Vec3d scale(double p_186678_1_)
  {
    return new Vec3d(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, 
      this.zCoord * p_186678_1_);
  }
  
  public Vec3d subtractReverse(Vec3d vec)
  {
    return new Vec3d(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, 
      vec.zCoord - this.zCoord);
  }
  
  public Vec3d normalize()
  {
    double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + 
      this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    return var1 < 1.0E-4D ? new Vec3d(0.0D, 0.0D, 0.0D) : new Vec3d(
      this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
  }
  
  public double dotProduct(Vec3d vec)
  {
    return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + 
      this.zCoord * vec.zCoord;
  }
  
  public Vec3d crossProduct(Vec3d vec)
  {
    return new Vec3d(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, 
      this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, 
      this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
  }
  
  public Vec3d subtract(Vec3d p_178788_1_)
  {
    return subtract(p_178788_1_.xCoord, p_178788_1_.yCoord, 
      p_178788_1_.zCoord);
  }
  
  public Vec3d subtract(double p_178786_1_, double p_178786_3_, double p_178786_5_)
  {
    return addVector(-p_178786_1_, -p_178786_3_, -p_178786_5_);
  }
  
  public Vec3d add(Vec3d p_178787_1_)
  {
    return addVector(p_178787_1_.xCoord, p_178787_1_.yCoord, 
      p_178787_1_.zCoord);
  }
  
  public Vec3d addVector(double x, double y, double z)
  {
    return new Vec3d(this.xCoord + x, this.yCoord + y, this.zCoord + z);
  }
  
  public double distanceTo(Vec3d vec)
  {
    double var2 = vec.xCoord - this.xCoord;
    double var4 = vec.yCoord - this.yCoord;
    double var6 = vec.zCoord - this.zCoord;
    return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
  }
  
  public double squareDistanceTo(Vec3d vec)
  {
    double var2 = vec.xCoord - this.xCoord;
    double var4 = vec.yCoord - this.yCoord;
    double var6 = vec.zCoord - this.zCoord;
    return var2 * var2 + var4 * var4 + var6 * var6;
  }
  
  public double lengthVector()
  {
    return MathHelper.sqrt_double(this.xCoord * this.xCoord + 
      this.yCoord * this.yCoord + this.zCoord * this.zCoord);
  }
  
  public Vec3d getIntermediateWithXValue(Vec3d vec, double x)
  {
    double var4 = vec.xCoord - this.xCoord;
    double var6 = vec.yCoord - this.yCoord;
    double var8 = vec.zCoord - this.zCoord;
    if (var4 * var4 < 1.0000000116860974E-7D) {
      return null;
    }
    double var10 = (x - this.xCoord) / var4;
    return (var10 >= 0.0D) && (var10 <= 1.0D) ? 
      new Vec3d(this.xCoord + var4 * var10, 
      this.yCoord + var6 * var10, this.zCoord + var8 * var10) : 
      null;
  }
  
  public Vec3d getIntermediateWithYValue(Vec3d vec, double y)
  {
    double var4 = vec.xCoord - this.xCoord;
    double var6 = vec.yCoord - this.yCoord;
    double var8 = vec.zCoord - this.zCoord;
    if (var6 * var6 < 1.0000000116860974E-7D) {
      return null;
    }
    double var10 = (y - this.yCoord) / var6;
    return (var10 >= 0.0D) && (var10 <= 1.0D) ? 
      new Vec3d(this.xCoord + var4 * var10, 
      this.yCoord + var6 * var10, this.zCoord + var8 * var10) : 
      null;
  }
  
  public Vec3d getIntermediateWithZValue(Vec3d vec, double z)
  {
    double var4 = vec.xCoord - this.xCoord;
    double var6 = vec.yCoord - this.yCoord;
    double var8 = vec.zCoord - this.zCoord;
    if (var8 * var8 < 1.0000000116860974E-7D) {
      return null;
    }
    double var10 = (z - this.zCoord) / var8;
    return (var10 >= 0.0D) && (var10 <= 1.0D) ? 
      new Vec3d(this.xCoord + var4 * var10, 
      this.yCoord + var6 * var10, this.zCoord + var8 * var10) : 
      null;
  }
  
  public String toString()
  {
    return 
      "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
  }
  
  public Vec3d rotatePitch(float p_178789_1_)
  {
    float var2 = MathHelper.cos(p_178789_1_);
    float var3 = MathHelper.sin(p_178789_1_);
    double var4 = this.xCoord;
    double var6 = this.yCoord * var2 + this.zCoord * var3;
    double var8 = this.zCoord * var2 - this.yCoord * var3;
    return new Vec3d(var4, var6, var8);
  }
  
  public Vec3d rotateYaw(float p_178785_1_)
  {
    float var2 = MathHelper.cos(p_178785_1_);
    float var3 = MathHelper.sin(p_178785_1_);
    double var4 = this.xCoord * var2 + this.zCoord * var3;
    double var6 = this.yCoord;
    double var8 = this.zCoord * var2 - this.xCoord * var3;
    return new Vec3d(var4, var6, var8);
  }
}