/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ public class BlockPos
/*     */   extends Vec3i
/*     */ {
/*  10 */   public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
/*  11 */   private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
/*  12 */   private static final int NUM_Z_BITS = NUM_X_BITS;
/*  13 */   private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
/*  14 */   private static final int Y_SHIFT = 0 + NUM_Z_BITS;
/*  15 */   private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
/*  16 */   private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
/*  17 */   private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
/*  18 */   private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;
/*     */   
/*     */   public BlockPos(int x, int y, int z)
/*     */   {
/*  22 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public BlockPos(double x, double y, double z)
/*     */   {
/*  27 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public BlockPos(Entity source)
/*     */   {
/*  32 */     this(source.posX, source.posY, source.posZ);
/*     */   }
/*     */   
/*     */   public BlockPos(Vec3 source)
/*     */   {
/*  37 */     this(source.xCoord, source.yCoord, source.zCoord);
/*     */   }
/*     */   
/*     */   public BlockPos(Vec3i source)
/*     */   {
/*  42 */     this(source.getX(), source.getY(), source.getZ());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos add(double x, double y, double z)
/*     */   {
/*  50 */     return (x == 0.0D) && (y == 0.0D) && (z == 0.0D) ? this : new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos add(int x, int y, int z)
/*     */   {
/*  58 */     return (x == 0) && (y == 0) && (z == 0) ? this : new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos add(Vec3i vec)
/*     */   {
/*  66 */     return (vec.getX() == 0) && (vec.getY() == 0) && (vec.getZ() == 0) ? this : new BlockPos(getX() + vec.getX(), getY() + vec.getY(), getZ() + vec.getZ());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos subtract(Vec3i vec)
/*     */   {
/*  74 */     return (vec.getX() == 0) && (vec.getY() == 0) && (vec.getZ() == 0) ? this : new BlockPos(getX() - vec.getX(), getY() - vec.getY(), getZ() - vec.getZ());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos up()
/*     */   {
/*  82 */     return up(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos up(int n)
/*     */   {
/*  90 */     return offset(EnumFacing.UP, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos down()
/*     */   {
/*  98 */     return down(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos down(int n)
/*     */   {
/* 106 */     return offset(EnumFacing.DOWN, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos north()
/*     */   {
/* 114 */     return north(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos north(int n)
/*     */   {
/* 122 */     return offset(EnumFacing.NORTH, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos south()
/*     */   {
/* 130 */     return south(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos south(int n)
/*     */   {
/* 138 */     return offset(EnumFacing.SOUTH, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos west()
/*     */   {
/* 146 */     return west(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos west(int n)
/*     */   {
/* 154 */     return offset(EnumFacing.WEST, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos east()
/*     */   {
/* 162 */     return east(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos east(int n)
/*     */   {
/* 170 */     return offset(EnumFacing.EAST, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos offset(EnumFacing facing)
/*     */   {
/* 178 */     return offset(facing, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos offset(EnumFacing facing, int n)
/*     */   {
/* 186 */     return n == 0 ? this : new BlockPos(getX() + facing.getFrontOffsetX() * n, getY() + facing.getFrontOffsetY() * n, getZ() + facing.getFrontOffsetZ() * n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos crossProduct(Vec3i vec)
/*     */   {
/* 194 */     return new BlockPos(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long toLong()
/*     */   {
/* 202 */     return (getX() & X_MASK) << X_SHIFT | (getY() & Y_MASK) << Y_SHIFT | (getZ() & Z_MASK) << 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BlockPos fromLong(long serialized)
/*     */   {
/* 210 */     int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
/* 211 */     int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
/* 212 */     int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
/* 213 */     return new BlockPos(i, j, k);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to)
/*     */   {
/* 218 */     BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 219 */     final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/* 220 */     new Iterable()
/*     */     {
/*     */       public Iterator<BlockPos> iterator()
/*     */       {
/* 224 */         new AbstractIterator()
/*     */         {
/* 226 */           private BlockPos lastReturned = null;
/*     */           
/*     */           protected BlockPos computeNext() {
/* 229 */             if (this.lastReturned == null)
/*     */             {
/* 231 */               this.lastReturned = this.val$blockpos;
/* 232 */               return this.lastReturned;
/*     */             }
/* 234 */             if (this.lastReturned.equals(this.val$blockpos1))
/*     */             {
/* 236 */               return (BlockPos)endOfData();
/*     */             }
/*     */             
/*     */ 
/* 240 */             int i = this.lastReturned.getX();
/* 241 */             int j = this.lastReturned.getY();
/* 242 */             int k = this.lastReturned.getZ();
/*     */             
/* 244 */             if (i < this.val$blockpos1.getX())
/*     */             {
/* 246 */               i++;
/*     */             }
/* 248 */             else if (j < this.val$blockpos1.getY())
/*     */             {
/* 250 */               i = this.val$blockpos.getX();
/* 251 */               j++;
/*     */             }
/* 253 */             else if (k < this.val$blockpos1.getZ())
/*     */             {
/* 255 */               i = this.val$blockpos.getX();
/* 256 */               j = this.val$blockpos.getY();
/* 257 */               k++;
/*     */             }
/*     */             
/* 260 */             this.lastReturned = new BlockPos(i, j, k);
/* 261 */             return this.lastReturned;
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to)
/*     */   {
/* 271 */     BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 272 */     final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/* 273 */     new Iterable()
/*     */     {
/*     */       public Iterator<BlockPos.MutableBlockPos> iterator()
/*     */       {
/* 277 */         new AbstractIterator()
/*     */         {
/* 279 */           private BlockPos.MutableBlockPos theBlockPos = null;
/*     */           
/*     */           protected BlockPos.MutableBlockPos computeNext() {
/* 282 */             if (this.theBlockPos == null)
/*     */             {
/* 284 */               this.theBlockPos = new BlockPos.MutableBlockPos(this.val$blockpos.getX(), this.val$blockpos.getY(), this.val$blockpos.getZ());
/* 285 */               return this.theBlockPos;
/*     */             }
/* 287 */             if (this.theBlockPos.equals(this.val$blockpos1))
/*     */             {
/* 289 */               return (BlockPos.MutableBlockPos)endOfData();
/*     */             }
/*     */             
/*     */ 
/* 293 */             int i = this.theBlockPos.getX();
/* 294 */             int j = this.theBlockPos.getY();
/* 295 */             int k = this.theBlockPos.getZ();
/*     */             
/* 297 */             if (i < this.val$blockpos1.getX())
/*     */             {
/* 299 */               i++;
/*     */             }
/* 301 */             else if (j < this.val$blockpos1.getY())
/*     */             {
/* 303 */               i = this.val$blockpos.getX();
/* 304 */               j++;
/*     */             }
/* 306 */             else if (k < this.val$blockpos1.getZ())
/*     */             {
/* 308 */               i = this.val$blockpos.getX();
/* 309 */               j = this.val$blockpos.getY();
/* 310 */               k++;
/*     */             }
/*     */             
/* 313 */             BlockPos.MutableBlockPos.access$0(this.theBlockPos, i);
/* 314 */             BlockPos.MutableBlockPos.access$1(this.theBlockPos, j);
/* 315 */             BlockPos.MutableBlockPos.access$2(this.theBlockPos, k);
/* 316 */             return this.theBlockPos;
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static final class MutableBlockPos
/*     */     extends BlockPos
/*     */   {
/*     */     private int x;
/*     */     private int y;
/*     */     private int z;
/*     */     
/*     */     public MutableBlockPos()
/*     */     {
/* 332 */       this(0, 0, 0);
/*     */     }
/*     */     
/*     */     public MutableBlockPos(int x_, int y_, int z_)
/*     */     {
/* 337 */       super(0, 0);
/* 338 */       this.x = x_;
/* 339 */       this.y = y_;
/* 340 */       this.z = z_;
/*     */     }
/*     */     
/*     */     public int getX()
/*     */     {
/* 345 */       return this.x;
/*     */     }
/*     */     
/*     */     public int getY()
/*     */     {
/* 350 */       return this.y;
/*     */     }
/*     */     
/*     */     public int getZ()
/*     */     {
/* 355 */       return this.z;
/*     */     }
/*     */     
/*     */     public MutableBlockPos func_181079_c(int p_181079_1_, int p_181079_2_, int p_181079_3_)
/*     */     {
/* 360 */       this.x = p_181079_1_;
/* 361 */       this.y = p_181079_2_;
/* 362 */       this.z = p_181079_3_;
/* 363 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\BlockPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */