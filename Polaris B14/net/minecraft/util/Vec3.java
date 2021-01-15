/*     */ package net.minecraft.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Vec3
/*     */ {
/*     */   public final double xCoord;
/*     */   
/*     */   public final double yCoord;
/*     */   
/*     */   public final double zCoord;
/*     */   
/*     */ 
/*     */   public Vec3(double x, double y, double z)
/*     */   {
/*  16 */     if (x == -0.0D)
/*     */     {
/*  18 */       x = 0.0D;
/*     */     }
/*     */     
/*  21 */     if (y == -0.0D)
/*     */     {
/*  23 */       y = 0.0D;
/*     */     }
/*     */     
/*  26 */     if (z == -0.0D)
/*     */     {
/*  28 */       z = 0.0D;
/*     */     }
/*     */     
/*  31 */     this.xCoord = x;
/*  32 */     this.yCoord = y;
/*  33 */     this.zCoord = z;
/*     */   }
/*     */   
/*     */   public Vec3(Vec3i p_i46377_1_)
/*     */   {
/*  38 */     this(p_i46377_1_.getX(), p_i46377_1_.getY(), p_i46377_1_.getZ());
/*     */   }
/*     */   
/*     */   public Vec3 scale(double p_186678_1_) {
/*  42 */     return new Vec3(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 subtractReverse(Vec3 vec)
/*     */   {
/*  50 */     return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 normalize()
/*     */   {
/*  58 */     double d0 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*  59 */     return d0 < 1.0E-4D ? new Vec3(0.0D, 0.0D, 0.0D) : new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
/*     */   }
/*     */   
/*     */   public double dotProduct(Vec3 vec)
/*     */   {
/*  64 */     return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 crossProduct(Vec3 vec)
/*     */   {
/*  72 */     return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(Vec3 vec)
/*     */   {
/*  77 */     return subtract(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(double x, double y, double z)
/*     */   {
/*  82 */     return addVector(-x, -y, -z);
/*     */   }
/*     */   
/*     */   public Vec3 add(Vec3 vec)
/*     */   {
/*  87 */     return addVector(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 addVector(double x, double y, double z)
/*     */   {
/*  96 */     return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double distanceTo(Vec3 vec)
/*     */   {
/* 104 */     double d0 = vec.xCoord - this.xCoord;
/* 105 */     double d1 = vec.yCoord - this.yCoord;
/* 106 */     double d2 = vec.zCoord - this.zCoord;
/* 107 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double squareDistanceTo(Vec3 vec)
/*     */   {
/* 115 */     double d0 = vec.xCoord - this.xCoord;
/* 116 */     double d1 = vec.yCoord - this.yCoord;
/* 117 */     double d2 = vec.zCoord - this.zCoord;
/* 118 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double lengthVector()
/*     */   {
/* 126 */     return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getIntermediateWithXValue(Vec3 vec, double x)
/*     */   {
/* 135 */     double d0 = vec.xCoord - this.xCoord;
/* 136 */     double d1 = vec.yCoord - this.yCoord;
/* 137 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 139 */     if (d0 * d0 < 1.0000000116860974E-7D)
/*     */     {
/* 141 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 145 */     double d3 = (x - this.xCoord) / d0;
/* 146 */     return (d3 >= 0.0D) && (d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getIntermediateWithYValue(Vec3 vec, double y)
/*     */   {
/* 156 */     double d0 = vec.xCoord - this.xCoord;
/* 157 */     double d1 = vec.yCoord - this.yCoord;
/* 158 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 160 */     if (d1 * d1 < 1.0000000116860974E-7D)
/*     */     {
/* 162 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 166 */     double d3 = (y - this.yCoord) / d1;
/* 167 */     return (d3 >= 0.0D) && (d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getIntermediateWithZValue(Vec3 vec, double z)
/*     */   {
/* 177 */     double d0 = vec.xCoord - this.xCoord;
/* 178 */     double d1 = vec.yCoord - this.yCoord;
/* 179 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 181 */     if (d2 * d2 < 1.0000000116860974E-7D)
/*     */     {
/* 183 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 187 */     double d3 = (z - this.zCoord) / d2;
/* 188 */     return (d3 >= 0.0D) && (d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 194 */     return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
/*     */   }
/*     */   
/*     */   public Vec3 rotatePitch(float pitch)
/*     */   {
/* 199 */     float f = MathHelper.cos(pitch);
/* 200 */     float f1 = MathHelper.sin(pitch);
/* 201 */     double d0 = this.xCoord;
/* 202 */     double d1 = this.yCoord * f + this.zCoord * f1;
/* 203 */     double d2 = this.zCoord * f - this.yCoord * f1;
/* 204 */     return new Vec3(d0, d1, d2);
/*     */   }
/*     */   
/*     */   public Vec3 rotateYaw(float yaw)
/*     */   {
/* 209 */     float f = MathHelper.cos(yaw);
/* 210 */     float f1 = MathHelper.sin(yaw);
/* 211 */     double d0 = this.xCoord * f + this.zCoord * f1;
/* 212 */     double d1 = this.yCoord;
/* 213 */     double d2 = this.zCoord * f - this.xCoord * f1;
/* 214 */     return new Vec3(d0, d1, d2);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Vec3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */