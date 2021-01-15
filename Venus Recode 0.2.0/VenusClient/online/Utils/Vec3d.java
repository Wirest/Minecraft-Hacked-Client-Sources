/*     */ package VenusClient.online.Utils;
/*     */ 
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ public class Vec3d {
/*   8 */   public static final Vec3d ZERO = new Vec3d(0.0D, 0.0D, 0.0D);
/*     */   
/*     */   public final double xCoord;
/*     */   
/*     */   public final double yCoord;
/*     */   
/*     */   public final double zCoord;
/*     */   
/*     */   public Vec3d(double x, double y2, double z) {
/*  14 */     if (x == -0.0D)
/*  15 */       x = 0.0D; 
/*  17 */     if (y2 == -0.0D)
/*  18 */       y2 = 0.0D; 
/*  20 */     if (z == -0.0D)
/*  21 */       z = 0.0D; 
/*  23 */     this.xCoord = x;
/*  24 */     this.yCoord = y2;
/*  25 */     this.zCoord = z;
/*     */   }
/*     */   
/*     */   public Vec3d(Vec3i vector) {
/*  29 */     this(vector.getX(), vector.getY(), vector.getZ());
/*     */   }
/*     */   
/*     */   public Vec3d subtractReverse(Vec3d vec) {
/*  33 */     return new Vec3d(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
/*     */   }
/*     */   
/*     */   public Vec3d normalize() {
/*  37 */     double d0 = Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*  38 */     return (d0 < 1.0E-4D) ? ZERO : new Vec3d(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
/*     */   }
/*     */   
/*     */   public double dotProduct(Vec3d vec) {
/*  42 */     return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
/*     */   }
/*     */   
/*     */   public Vec3d crossProduct(Vec3d vec) {
/*  46 */     return new Vec3d(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
/*     */   }
/*     */   
/*     */   public Vec3d subtract(Vec3d vec) {
/*  52 */     return subtract(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */   
/*     */   public Vec3d subtract(double x, double y2, double z) {
/*  56 */     return addVector(-x, -y2, -z);
/*     */   }
/*     */   
/*     */   public Vec3d add(Vec3d vec) {
/*  60 */     return addVector(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */   
/*     */   public Vec3d addVector(double x, double y2, double z) {
/*  64 */     return new Vec3d(this.xCoord + x, this.yCoord + y2, this.zCoord + z);
/*     */   }
/*     */   
/*     */   public double distanceTo(Vec3d vec) {
/*  68 */     double d0 = vec.xCoord - this.xCoord;
/*  69 */     double d2 = vec.yCoord - this.yCoord;
/*  70 */     double d3 = vec.zCoord - this.zCoord;
/*  71 */     return Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
/*     */   }
/*     */   
/*     */   public double squareDistanceTo(Vec3d vec) {
/*  75 */     double d0 = vec.xCoord - this.xCoord;
/*  76 */     double d2 = vec.yCoord - this.yCoord;
/*  77 */     double d3 = vec.zCoord - this.zCoord;
/*  78 */     return d0 * d0 + d2 * d2 + d3 * d3;
/*     */   }
/*     */   
/*     */   public double squareDistanceTo(double xIn, double yIn, double zIn) {
/*  82 */     double d0 = xIn - this.xCoord;
/*  83 */     double d2 = yIn - this.yCoord;
/*  84 */     double d3 = zIn - this.zCoord;
/*  85 */     return d0 * d0 + d2 * d2 + d3 * d3;
/*     */   }
/*     */   
/*     */   public Vec3d scale(double p_186678_1_) {
/*  89 */     return new Vec3d(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
/*     */   }
/*     */   
/*     */   public double lengthVector() {
/*  93 */     return Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*     */   }
/*     */   
/*     */   public double lengthSquared() {
/*  97 */     return this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord;
/*     */   }
/*     */   
/*     */   public Vec3d getIntermediateWithXValue(Vec3d vec, double x) {
/* 101 */     double d0 = vec.xCoord - this.xCoord;
/* 102 */     double d2 = vec.yCoord - this.yCoord;
/* 103 */     double d3 = vec.zCoord - this.zCoord;
/* 104 */     if (d0 * d0 < 1.0000000116860974E-7D)
/* 105 */       return null; 
/* 107 */     double d4 = (x - this.xCoord) / d0;
/* 108 */     return (d4 >= 0.0D && d4 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
/*     */   }
/*     */   
/*     */   public Vec3d getIntermediateWithYValue(Vec3d vec, double y2) {
/* 113 */     double d0 = vec.xCoord - this.xCoord;
/* 114 */     double d2 = vec.yCoord - this.yCoord;
/* 115 */     double d3 = vec.zCoord - this.zCoord;
/* 116 */     if (d2 * d2 < 1.0000000116860974E-7D)
/* 117 */       return null; 
/* 119 */     double d4 = (y2 - this.yCoord) / d2;
/* 120 */     return (d4 >= 0.0D && d4 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
/*     */   }
/*     */   
/*     */   public Vec3d getIntermediateWithZValue(Vec3d vec, double z) {
/* 125 */     double d0 = vec.xCoord - this.xCoord;
/* 126 */     double d2 = vec.yCoord - this.yCoord;
/* 127 */     double d3 = vec.zCoord - this.zCoord;
/* 128 */     if (d3 * d3 < 1.0000000116860974E-7D)
/* 129 */       return null; 
/* 131 */     double d4 = (z - this.zCoord) / d3;
/* 132 */     return (d4 >= 0.0D && d4 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 138 */     if (this == p_equals_1_)
/* 139 */       return true; 
/* 141 */     if (!(p_equals_1_ instanceof Vec3d))
/* 142 */       return false; 
/* 144 */     Vec3d vec3d = (Vec3d)p_equals_1_;
/* 145 */     return (Double.compare(vec3d.xCoord, this.xCoord) == 0 && Double.compare(vec3d.yCoord, this.yCoord) == 0 && 
/* 146 */       Double.compare(vec3d.zCoord, this.zCoord) == 0);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 151 */     long j = Double.doubleToLongBits(this.xCoord);
/* 152 */     int i = (int)(j ^ j >>> 32L);
/* 153 */     j = Double.doubleToLongBits(this.yCoord);
/* 154 */     i = 31 * i + (int)(j ^ j >>> 32L);
/* 155 */     j = Double.doubleToLongBits(this.zCoord);
/* 156 */     i = 31 * i + (int)(j ^ j >>> 32L);
/* 157 */     return i;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 162 */     return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
/*     */   }
/*     */   
/*     */   public Vec3d rotatePitch(float pitch) {
/* 166 */     float f = MathHelper.cos(pitch);
/* 167 */     float f2 = MathHelper.sin(pitch);
/* 168 */     double d0 = this.xCoord;
/* 169 */     double d2 = this.yCoord * f + this.zCoord * f2;
/* 170 */     double d3 = this.zCoord * f - this.yCoord * f2;
/* 171 */     return new Vec3d(d0, d2, d3);
/*     */   }
/*     */   
/*     */   public Vec3d rotateYaw(float yaw) {
/* 175 */     float f = MathHelper.cos(yaw);
/* 176 */     float f2 = MathHelper.sin(yaw);
/* 177 */     double d0 = this.xCoord * f + this.zCoord * f2;
/* 178 */     double d2 = this.yCoord;
/* 179 */     double d3 = this.zCoord * f - this.xCoord * f2;
/* 180 */     return new Vec3d(d0, d2, d3);
/*     */   }
/*     */   
/*     */   public static Vec3d fromPitchYaw(float p_189986_0_, float p_189986_1_) {
/* 184 */     float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - 3.1415927F);
/* 185 */     float f2 = MathHelper.sin(-p_189986_1_ * 0.017453292F - 3.1415927F);
/* 186 */     float f3 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
/* 187 */     float f4 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
/* 188 */     return new Vec3d((f2 * f3), f4, (f * f3));
/*     */   }
/*     */ }


/* Location:              C:\Users\Dominik\AppData\Local\Temp\Rar$DRa40324.25194\CumSock\CumSock.jar!\me\memewaredevlopement\clien\\util\blocks\Vec3d.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */