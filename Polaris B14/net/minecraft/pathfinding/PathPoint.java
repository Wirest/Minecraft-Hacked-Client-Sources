/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.util.MathHelper;
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
/*     */ public class PathPoint
/*     */ {
/*     */   public final int xCoord;
/*     */   public final int yCoord;
/*     */   public final int zCoord;
/*     */   private final int hash;
/*  20 */   int index = -1;
/*     */   
/*     */ 
/*     */   float totalPathDistance;
/*     */   
/*     */ 
/*     */   float distanceToNext;
/*     */   
/*     */ 
/*     */   float distanceToTarget;
/*     */   
/*     */ 
/*     */   PathPoint previous;
/*     */   
/*     */   public boolean visited;
/*     */   
/*     */ 
/*     */   public PathPoint(int x, int y, int z)
/*     */   {
/*  39 */     this.xCoord = x;
/*  40 */     this.yCoord = y;
/*  41 */     this.zCoord = z;
/*  42 */     this.hash = makeHash(x, y, z);
/*     */   }
/*     */   
/*     */   public static int makeHash(int x, int y, int z)
/*     */   {
/*  47 */     return y & 0xFF | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? 32768 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float distanceTo(PathPoint pathpointIn)
/*     */   {
/*  55 */     float f = pathpointIn.xCoord - this.xCoord;
/*  56 */     float f1 = pathpointIn.yCoord - this.yCoord;
/*  57 */     float f2 = pathpointIn.zCoord - this.zCoord;
/*  58 */     return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float distanceToSquared(PathPoint pathpointIn)
/*     */   {
/*  66 */     float f = pathpointIn.xCoord - this.xCoord;
/*  67 */     float f1 = pathpointIn.yCoord - this.yCoord;
/*  68 */     float f2 = pathpointIn.zCoord - this.zCoord;
/*  69 */     return f * f + f1 * f1 + f2 * f2;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  74 */     if (!(p_equals_1_ instanceof PathPoint))
/*     */     {
/*  76 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  80 */     PathPoint pathpoint = (PathPoint)p_equals_1_;
/*  81 */     return (this.hash == pathpoint.hash) && (this.xCoord == pathpoint.xCoord) && (this.yCoord == pathpoint.yCoord) && (this.zCoord == pathpoint.zCoord);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  87 */     return this.hash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAssigned()
/*     */   {
/*  95 */     return this.index >= 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 100 */     return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */