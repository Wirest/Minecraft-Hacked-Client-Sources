/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class AxisAlignedBB
/*     */ {
/*     */   public double minX;
/*     */   public double minY;
/*     */   public double minZ;
/*     */   public double maxX;
/*     */   public double maxY;
/*     */   public double maxZ;
/*     */   
/*     */   public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
/*     */   {
/*  14 */     this.minX = Math.min(x1, x2);
/*  15 */     this.minY = Math.min(y1, y2);
/*  16 */     this.minZ = Math.min(z1, z2);
/*  17 */     this.maxX = Math.max(x1, x2);
/*  18 */     this.maxY = Math.max(y1, y2);
/*  19 */     this.maxZ = Math.max(z1, z2);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB(BlockPos pos1, BlockPos pos2)
/*     */   {
/*  24 */     this.minX = pos1.getX();
/*  25 */     this.minY = pos1.getY();
/*  26 */     this.minZ = pos1.getZ();
/*  27 */     this.maxX = pos2.getX();
/*  28 */     this.maxY = pos2.getY();
/*  29 */     this.maxZ = pos2.getZ();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB addCoord(double x, double y, double z)
/*     */   {
/*  37 */     double d0 = this.minX;
/*  38 */     double d1 = this.minY;
/*  39 */     double d2 = this.minZ;
/*  40 */     double d3 = this.maxX;
/*  41 */     double d4 = this.maxY;
/*  42 */     double d5 = this.maxZ;
/*     */     
/*  44 */     if (x < 0.0D)
/*     */     {
/*  46 */       d0 += x;
/*     */     }
/*  48 */     else if (x > 0.0D)
/*     */     {
/*  50 */       d3 += x;
/*     */     }
/*     */     
/*  53 */     if (y < 0.0D)
/*     */     {
/*  55 */       d1 += y;
/*     */     }
/*  57 */     else if (y > 0.0D)
/*     */     {
/*  59 */       d4 += y;
/*     */     }
/*     */     
/*  62 */     if (z < 0.0D)
/*     */     {
/*  64 */       d2 += z;
/*     */     }
/*  66 */     else if (z > 0.0D)
/*     */     {
/*  68 */       d5 += z;
/*     */     }
/*     */     
/*  71 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB expand(double x, double y, double z)
/*     */   {
/*  80 */     double d0 = this.minX - x;
/*  81 */     double d1 = this.minY - y;
/*  82 */     double d2 = this.minZ - z;
/*  83 */     double d3 = this.maxX + x;
/*  84 */     double d4 = this.maxY + y;
/*  85 */     double d5 = this.maxZ + z;
/*  86 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB union(AxisAlignedBB other)
/*     */   {
/*  91 */     double d0 = Math.min(this.minX, other.minX);
/*  92 */     double d1 = Math.min(this.minY, other.minY);
/*  93 */     double d2 = Math.min(this.minZ, other.minZ);
/*  94 */     double d3 = Math.max(this.maxX, other.maxX);
/*  95 */     double d4 = Math.max(this.maxY, other.maxY);
/*  96 */     double d5 = Math.max(this.maxZ, other.maxZ);
/*  97 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2)
/*     */   {
/* 105 */     double d0 = Math.min(x1, x2);
/* 106 */     double d1 = Math.min(y1, y2);
/* 107 */     double d2 = Math.min(z1, z2);
/* 108 */     double d3 = Math.max(x1, x2);
/* 109 */     double d4 = Math.max(y1, y2);
/* 110 */     double d5 = Math.max(z1, z2);
/* 111 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB offset(double x, double y, double z)
/*     */   {
/* 119 */     return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB offsetAndUpdate(double d1, double d2, double d3) {
/* 123 */     this.minX += d1;
/* 124 */     this.minY += d2;
/* 125 */     this.minZ += d3;
/* 126 */     this.maxX += d1;
/* 127 */     this.maxY += d2;
/* 128 */     this.maxZ += d3;
/* 129 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double calculateXOffset(AxisAlignedBB other, double offsetX)
/*     */   {
/* 139 */     if ((other.maxY > this.minY) && (other.minY < this.maxY) && (other.maxZ > this.minZ) && (other.minZ < this.maxZ))
/*     */     {
/* 141 */       if ((offsetX > 0.0D) && (other.maxX <= this.minX))
/*     */       {
/* 143 */         double d1 = this.minX - other.maxX;
/*     */         
/* 145 */         if (d1 < offsetX)
/*     */         {
/* 147 */           offsetX = d1;
/*     */         }
/*     */       }
/* 150 */       else if ((offsetX < 0.0D) && (other.minX >= this.maxX))
/*     */       {
/* 152 */         double d0 = this.maxX - other.minX;
/*     */         
/* 154 */         if (d0 > offsetX)
/*     */         {
/* 156 */           offsetX = d0;
/*     */         }
/*     */       }
/*     */       
/* 160 */       return offsetX;
/*     */     }
/*     */     
/*     */ 
/* 164 */     return offsetX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double calculateYOffset(AxisAlignedBB other, double offsetY)
/*     */   {
/* 175 */     if ((other.maxX > this.minX) && (other.minX < this.maxX) && (other.maxZ > this.minZ) && (other.minZ < this.maxZ))
/*     */     {
/* 177 */       if ((offsetY > 0.0D) && (other.maxY <= this.minY))
/*     */       {
/* 179 */         double d1 = this.minY - other.maxY;
/*     */         
/* 181 */         if (d1 < offsetY)
/*     */         {
/* 183 */           offsetY = d1;
/*     */         }
/*     */       }
/* 186 */       else if ((offsetY < 0.0D) && (other.minY >= this.maxY))
/*     */       {
/* 188 */         double d0 = this.maxY - other.minY;
/*     */         
/* 190 */         if (d0 > offsetY)
/*     */         {
/* 192 */           offsetY = d0;
/*     */         }
/*     */       }
/*     */       
/* 196 */       return offsetY;
/*     */     }
/*     */     
/*     */ 
/* 200 */     return offsetY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double calculateZOffset(AxisAlignedBB other, double offsetZ)
/*     */   {
/* 211 */     if ((other.maxX > this.minX) && (other.minX < this.maxX) && (other.maxY > this.minY) && (other.minY < this.maxY))
/*     */     {
/* 213 */       if ((offsetZ > 0.0D) && (other.maxZ <= this.minZ))
/*     */       {
/* 215 */         double d1 = this.minZ - other.maxZ;
/*     */         
/* 217 */         if (d1 < offsetZ)
/*     */         {
/* 219 */           offsetZ = d1;
/*     */         }
/*     */       }
/* 222 */       else if ((offsetZ < 0.0D) && (other.minZ >= this.maxZ))
/*     */       {
/* 224 */         double d0 = this.maxZ - other.minZ;
/*     */         
/* 226 */         if (d0 > offsetZ)
/*     */         {
/* 228 */           offsetZ = d0;
/*     */         }
/*     */       }
/*     */       
/* 232 */       return offsetZ;
/*     */     }
/*     */     
/*     */ 
/* 236 */     return offsetZ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersectsWith(AxisAlignedBB other)
/*     */   {
/* 245 */     return (other.maxZ > this.minZ) && (other.minZ < this.maxZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVecInside(Vec3 vec)
/*     */   {
/* 253 */     return (vec.zCoord > this.minZ) && (vec.zCoord < this.maxZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getAverageEdgeLength()
/*     */   {
/* 261 */     double d0 = this.maxX - this.minX;
/* 262 */     double d1 = this.maxY - this.minY;
/* 263 */     double d2 = this.maxZ - this.minZ;
/* 264 */     return (d0 + d1 + d2) / 3.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisAlignedBB contract(double x, double y, double z)
/*     */   {
/* 272 */     double d0 = this.minX + x;
/* 273 */     double d1 = this.minY + y;
/* 274 */     double d2 = this.minZ + z;
/* 275 */     double d3 = this.maxX - x;
/* 276 */     double d4 = this.maxY - y;
/* 277 */     double d5 = this.maxZ - z;
/* 278 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */   
/*     */   public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB)
/*     */   {
/* 283 */     Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
/* 284 */     Vec3 vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
/* 285 */     Vec3 vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
/* 286 */     Vec3 vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
/* 287 */     Vec3 vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
/* 288 */     Vec3 vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);
/*     */     
/* 290 */     if (!isVecInYZ(vec3))
/*     */     {
/* 292 */       vec3 = null;
/*     */     }
/*     */     
/* 295 */     if (!isVecInYZ(vec31))
/*     */     {
/* 297 */       vec31 = null;
/*     */     }
/*     */     
/* 300 */     if (!isVecInXZ(vec32))
/*     */     {
/* 302 */       vec32 = null;
/*     */     }
/*     */     
/* 305 */     if (!isVecInXZ(vec33))
/*     */     {
/* 307 */       vec33 = null;
/*     */     }
/*     */     
/* 310 */     if (!isVecInXY(vec34))
/*     */     {
/* 312 */       vec34 = null;
/*     */     }
/*     */     
/* 315 */     if (!isVecInXY(vec35))
/*     */     {
/* 317 */       vec35 = null;
/*     */     }
/*     */     
/* 320 */     Vec3 vec36 = null;
/*     */     
/* 322 */     if (vec3 != null)
/*     */     {
/* 324 */       vec36 = vec3;
/*     */     }
/*     */     
/* 327 */     if ((vec31 != null) && ((vec36 == null) || (vecA.squareDistanceTo(vec31) < vecA.squareDistanceTo(vec36))))
/*     */     {
/* 329 */       vec36 = vec31;
/*     */     }
/*     */     
/* 332 */     if ((vec32 != null) && ((vec36 == null) || (vecA.squareDistanceTo(vec32) < vecA.squareDistanceTo(vec36))))
/*     */     {
/* 334 */       vec36 = vec32;
/*     */     }
/*     */     
/* 337 */     if ((vec33 != null) && ((vec36 == null) || (vecA.squareDistanceTo(vec33) < vecA.squareDistanceTo(vec36))))
/*     */     {
/* 339 */       vec36 = vec33;
/*     */     }
/*     */     
/* 342 */     if ((vec34 != null) && ((vec36 == null) || (vecA.squareDistanceTo(vec34) < vecA.squareDistanceTo(vec36))))
/*     */     {
/* 344 */       vec36 = vec34;
/*     */     }
/*     */     
/* 347 */     if ((vec35 != null) && ((vec36 == null) || (vecA.squareDistanceTo(vec35) < vecA.squareDistanceTo(vec36))))
/*     */     {
/* 349 */       vec36 = vec35;
/*     */     }
/*     */     
/* 352 */     if (vec36 == null)
/*     */     {
/* 354 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 358 */     EnumFacing enumfacing = null;
/*     */     
/* 360 */     if (vec36 == vec3)
/*     */     {
/* 362 */       enumfacing = EnumFacing.WEST;
/*     */     }
/* 364 */     else if (vec36 == vec31)
/*     */     {
/* 366 */       enumfacing = EnumFacing.EAST;
/*     */     }
/* 368 */     else if (vec36 == vec32)
/*     */     {
/* 370 */       enumfacing = EnumFacing.DOWN;
/*     */     }
/* 372 */     else if (vec36 == vec33)
/*     */     {
/* 374 */       enumfacing = EnumFacing.UP;
/*     */     }
/* 376 */     else if (vec36 == vec34)
/*     */     {
/* 378 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     else
/*     */     {
/* 382 */       enumfacing = EnumFacing.SOUTH;
/*     */     }
/*     */     
/* 385 */     return new MovingObjectPosition(vec36, enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isVecInYZ(Vec3 vec)
/*     */   {
/* 394 */     return vec != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isVecInXZ(Vec3 vec)
/*     */   {
/* 402 */     return vec != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isVecInXY(Vec3 vec)
/*     */   {
/* 410 */     return vec != null;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 415 */     return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
/*     */   }
/*     */   
/*     */   public boolean func_181656_b()
/*     */   {
/* 420 */     return (Double.isNaN(this.minX)) || (Double.isNaN(this.minY)) || (Double.isNaN(this.minZ)) || (Double.isNaN(this.maxX)) || (Double.isNaN(this.maxY)) || (Double.isNaN(this.maxZ));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\AxisAlignedBB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */