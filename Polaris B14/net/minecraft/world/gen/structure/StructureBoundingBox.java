/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Objects.ToStringHelper;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3i;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureBoundingBox
/*     */ {
/*     */   public int minX;
/*     */   public int minY;
/*     */   public int minZ;
/*     */   public int maxX;
/*     */   public int maxY;
/*     */   public int maxZ;
/*     */   
/*     */   public StructureBoundingBox() {}
/*     */   
/*     */   public StructureBoundingBox(int[] coords)
/*     */   {
/*  35 */     if (coords.length == 6)
/*     */     {
/*  37 */       this.minX = coords[0];
/*  38 */       this.minY = coords[1];
/*  39 */       this.minZ = coords[2];
/*  40 */       this.maxX = coords[3];
/*  41 */       this.maxY = coords[4];
/*  42 */       this.maxZ = coords[5];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StructureBoundingBox getNewBoundingBox()
/*     */   {
/*  51 */     return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StructureBoundingBox getComponentToAddBoundingBox(int p_175897_0_, int p_175897_1_, int p_175897_2_, int p_175897_3_, int p_175897_4_, int p_175897_5_, int p_175897_6_, int p_175897_7_, int p_175897_8_, EnumFacing p_175897_9_)
/*     */   {
/*  60 */     switch (p_175897_9_)
/*     */     {
/*     */     case NORTH: 
/*  63 */       return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ - p_175897_8_ + 1 + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_5_);
/*     */     
/*     */     case SOUTH: 
/*  66 */       return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
/*     */     
/*     */     case UP: 
/*  69 */       return new StructureBoundingBox(p_175897_0_ - p_175897_8_ + 1 + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
/*     */     
/*     */     case WEST: 
/*  72 */       return new StructureBoundingBox(p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_8_ - 1 + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
/*     */     }
/*     */     
/*  75 */     return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
/*     */   }
/*     */   
/*     */ 
/*     */   public static StructureBoundingBox func_175899_a(int p_175899_0_, int p_175899_1_, int p_175899_2_, int p_175899_3_, int p_175899_4_, int p_175899_5_)
/*     */   {
/*  81 */     return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
/*     */   }
/*     */   
/*     */   public StructureBoundingBox(StructureBoundingBox structurebb)
/*     */   {
/*  86 */     this.minX = structurebb.minX;
/*  87 */     this.minY = structurebb.minY;
/*  88 */     this.minZ = structurebb.minZ;
/*  89 */     this.maxX = structurebb.maxX;
/*  90 */     this.maxY = structurebb.maxY;
/*  91 */     this.maxZ = structurebb.maxZ;
/*     */   }
/*     */   
/*     */   public StructureBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax)
/*     */   {
/*  96 */     this.minX = xMin;
/*  97 */     this.minY = yMin;
/*  98 */     this.minZ = zMin;
/*  99 */     this.maxX = xMax;
/* 100 */     this.maxY = yMax;
/* 101 */     this.maxZ = zMax;
/*     */   }
/*     */   
/*     */   public StructureBoundingBox(Vec3i vec1, Vec3i vec2)
/*     */   {
/* 106 */     this.minX = Math.min(vec1.getX(), vec2.getX());
/* 107 */     this.minY = Math.min(vec1.getY(), vec2.getY());
/* 108 */     this.minZ = Math.min(vec1.getZ(), vec2.getZ());
/* 109 */     this.maxX = Math.max(vec1.getX(), vec2.getX());
/* 110 */     this.maxY = Math.max(vec1.getY(), vec2.getY());
/* 111 */     this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
/*     */   }
/*     */   
/*     */   public StructureBoundingBox(int xMin, int zMin, int xMax, int zMax)
/*     */   {
/* 116 */     this.minX = xMin;
/* 117 */     this.minZ = zMin;
/* 118 */     this.maxX = xMax;
/* 119 */     this.maxZ = zMax;
/* 120 */     this.minY = 1;
/* 121 */     this.maxY = 512;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersectsWith(StructureBoundingBox structurebb)
/*     */   {
/* 129 */     return (this.maxX >= structurebb.minX) && (this.minX <= structurebb.maxX) && (this.maxZ >= structurebb.minZ) && (this.minZ <= structurebb.maxZ) && (this.maxY >= structurebb.minY) && (this.minY <= structurebb.maxY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn)
/*     */   {
/* 137 */     return (this.maxX >= minXIn) && (this.minX <= maxXIn) && (this.maxZ >= minZIn) && (this.minZ <= maxZIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void expandTo(StructureBoundingBox sbb)
/*     */   {
/* 145 */     this.minX = Math.min(this.minX, sbb.minX);
/* 146 */     this.minY = Math.min(this.minY, sbb.minY);
/* 147 */     this.minZ = Math.min(this.minZ, sbb.minZ);
/* 148 */     this.maxX = Math.max(this.maxX, sbb.maxX);
/* 149 */     this.maxY = Math.max(this.maxY, sbb.maxY);
/* 150 */     this.maxZ = Math.max(this.maxZ, sbb.maxZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void offset(int x, int y, int z)
/*     */   {
/* 158 */     this.minX += x;
/* 159 */     this.minY += y;
/* 160 */     this.minZ += z;
/* 161 */     this.maxX += x;
/* 162 */     this.maxY += y;
/* 163 */     this.maxZ += z;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVecInside(Vec3i vec)
/*     */   {
/* 171 */     return (vec.getX() >= this.minX) && (vec.getX() <= this.maxX) && (vec.getZ() >= this.minZ) && (vec.getZ() <= this.maxZ) && (vec.getY() >= this.minY) && (vec.getY() <= this.maxY);
/*     */   }
/*     */   
/*     */   public Vec3i func_175896_b()
/*     */   {
/* 176 */     return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getXSize()
/*     */   {
/* 184 */     return this.maxX - this.minX + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getYSize()
/*     */   {
/* 192 */     return this.maxY - this.minY + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getZSize()
/*     */   {
/* 200 */     return this.maxZ - this.minZ + 1;
/*     */   }
/*     */   
/*     */   public Vec3i getCenter()
/*     */   {
/* 205 */     return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 210 */     return Objects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
/*     */   }
/*     */   
/*     */   public NBTTagIntArray toNBTTagIntArray()
/*     */   {
/* 215 */     return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureBoundingBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */