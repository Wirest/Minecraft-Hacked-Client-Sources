/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockPortal;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockPattern.PatternHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.AxisDirection;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class Teleporter
/*     */ {
/*     */   private final WorldServer worldServerInstance;
/*     */   private final Random random;
/*  23 */   private final LongHashMap destinationCoordinateCache = new LongHashMap();
/*  24 */   private final java.util.List<Long> destinationCoordinateKeys = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */   public Teleporter(WorldServer worldIn)
/*     */   {
/*  28 */     this.worldServerInstance = worldIn;
/*  29 */     this.random = new Random(worldIn.getSeed());
/*     */   }
/*     */   
/*     */   public void placeInPortal(Entity entityIn, float rotationYaw)
/*     */   {
/*  34 */     if (this.worldServerInstance.provider.getDimensionId() != 1)
/*     */     {
/*  36 */       if (!placeInExistingPortal(entityIn, rotationYaw))
/*     */       {
/*  38 */         makePortal(entityIn);
/*  39 */         placeInExistingPortal(entityIn, rotationYaw);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  44 */       int i = MathHelper.floor_double(entityIn.posX);
/*  45 */       int j = MathHelper.floor_double(entityIn.posY) - 1;
/*  46 */       int k = MathHelper.floor_double(entityIn.posZ);
/*  47 */       int l = 1;
/*  48 */       int i1 = 0;
/*     */       
/*  50 */       for (int j1 = -2; j1 <= 2; j1++)
/*     */       {
/*  52 */         for (int k1 = -2; k1 <= 2; k1++)
/*     */         {
/*  54 */           for (int l1 = -1; l1 < 3; l1++)
/*     */           {
/*  56 */             int i2 = i + k1 * l + j1 * i1;
/*  57 */             int j2 = j + l1;
/*  58 */             int k2 = k + k1 * i1 - j1 * l;
/*  59 */             boolean flag = l1 < 0;
/*  60 */             this.worldServerInstance.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  65 */       entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0F);
/*  66 */       entityIn.motionX = (entityIn.motionY = entityIn.motionZ = 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
/*     */   {
/*  72 */     int i = 128;
/*  73 */     double d0 = -1.0D;
/*  74 */     int j = MathHelper.floor_double(entityIn.posX);
/*  75 */     int k = MathHelper.floor_double(entityIn.posZ);
/*  76 */     boolean flag = true;
/*  77 */     BlockPos blockpos = BlockPos.ORIGIN;
/*  78 */     long l = ChunkCoordIntPair.chunkXZ2Int(j, k);
/*     */     
/*  80 */     if (this.destinationCoordinateCache.containsItem(l))
/*     */     {
/*  82 */       PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(l);
/*  83 */       d0 = 0.0D;
/*  84 */       blockpos = teleporter$portalposition;
/*  85 */       teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
/*  86 */       flag = false;
/*     */     }
/*     */     else
/*     */     {
/*  90 */       BlockPos blockpos3 = new BlockPos(entityIn);
/*     */       
/*  92 */       for (int i1 = -128; i1 <= 128; i1++)
/*     */       {
/*     */ 
/*     */ 
/*  96 */         for (int j1 = -128; j1 <= 128; j1++) {
/*     */           BlockPos blockpos2;
/*  98 */           for (BlockPos blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2)
/*     */           {
/* 100 */             blockpos2 = blockpos1.down();
/*     */             
/* 102 */             if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == Blocks.portal)
/*     */             {
/* 104 */               while (this.worldServerInstance.getBlockState(blockpos2 = blockpos1.down()).getBlock() == Blocks.portal)
/*     */               {
/* 106 */                 blockpos1 = blockpos2;
/*     */               }
/*     */               
/* 109 */               double d1 = blockpos1.distanceSq(blockpos3);
/*     */               
/* 111 */               if ((d0 < 0.0D) || (d1 < d0))
/*     */               {
/* 113 */                 d0 = d1;
/* 114 */                 blockpos = blockpos1;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     if (d0 >= 0.0D)
/*     */     {
/* 124 */       if (flag)
/*     */       {
/* 126 */         this.destinationCoordinateCache.add(l, new PortalPosition(blockpos, this.worldServerInstance.getTotalWorldTime()));
/* 127 */         this.destinationCoordinateKeys.add(Long.valueOf(l));
/*     */       }
/*     */       
/* 130 */       double d5 = blockpos.getX() + 0.5D;
/* 131 */       double d6 = blockpos.getY() + 0.5D;
/* 132 */       double d7 = blockpos.getZ() + 0.5D;
/* 133 */       BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldServerInstance, blockpos);
/* 134 */       boolean flag1 = blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
/* 135 */       double d2 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? blockpattern$patternhelper.func_181117_a().getZ() : blockpattern$patternhelper.func_181117_a().getX();
/* 136 */       d6 = blockpattern$patternhelper.func_181117_a().getY() + 1 - entityIn.func_181014_aG().yCoord * blockpattern$patternhelper.func_181119_e();
/*     */       
/* 138 */       if (flag1)
/*     */       {
/* 140 */         d2 += 1.0D;
/*     */       }
/*     */       
/* 143 */       if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X)
/*     */       {
/* 145 */         d7 = d2 + (1.0D - entityIn.func_181014_aG().xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
/*     */       }
/*     */       else
/*     */       {
/* 149 */         d5 = d2 + (1.0D - entityIn.func_181014_aG().xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
/*     */       }
/*     */       
/* 152 */       float f = 0.0F;
/* 153 */       float f1 = 0.0F;
/* 154 */       float f2 = 0.0F;
/* 155 */       float f3 = 0.0F;
/*     */       
/* 157 */       if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH())
/*     */       {
/* 159 */         f = 1.0F;
/* 160 */         f1 = 1.0F;
/*     */       }
/* 162 */       else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().getOpposite())
/*     */       {
/* 164 */         f = -1.0F;
/* 165 */         f1 = -1.0F;
/*     */       }
/* 167 */       else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().rotateY())
/*     */       {
/* 169 */         f2 = 1.0F;
/* 170 */         f3 = -1.0F;
/*     */       }
/*     */       else
/*     */       {
/* 174 */         f2 = -1.0F;
/* 175 */         f3 = 1.0F;
/*     */       }
/*     */       
/* 178 */       double d3 = entityIn.motionX;
/* 179 */       double d4 = entityIn.motionZ;
/* 180 */       entityIn.motionX = (d3 * f + d4 * f3);
/* 181 */       entityIn.motionZ = (d3 * f2 + d4 * f1);
/* 182 */       entityIn.rotationYaw = (rotationYaw - entityIn.func_181012_aH().getOpposite().getHorizontalIndex() * 90 + blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90);
/* 183 */       entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
/* 184 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 188 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makePortal(Entity p_85188_1_)
/*     */   {
/* 194 */     int i = 16;
/* 195 */     double d0 = -1.0D;
/* 196 */     int j = MathHelper.floor_double(p_85188_1_.posX);
/* 197 */     int k = MathHelper.floor_double(p_85188_1_.posY);
/* 198 */     int l = MathHelper.floor_double(p_85188_1_.posZ);
/* 199 */     int i1 = j;
/* 200 */     int j1 = k;
/* 201 */     int k1 = l;
/* 202 */     int l1 = 0;
/* 203 */     int i2 = this.random.nextInt(4);
/* 204 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 206 */     for (int j2 = j - i; j2 <= j + i; j2++)
/*     */     {
/* 208 */       double d1 = j2 + 0.5D - p_85188_1_.posX;
/*     */       
/* 210 */       for (int l2 = l - i; l2 <= l + i; l2++)
/*     */       {
/* 212 */         double d2 = l2 + 0.5D - p_85188_1_.posZ;
/*     */         
/*     */ 
/* 215 */         for (int j3 = this.worldServerInstance.getActualHeight() - 1; j3 >= 0; j3--)
/*     */         {
/* 217 */           if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(j2, j3, l2)))
/*     */           {
/* 219 */             while ((j3 > 0) && (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(j2, j3 - 1, l2))))
/*     */             {
/* 221 */               j3--;
/*     */             }
/*     */             
/* 224 */             for (int k3 = i2; k3 < i2 + 4; k3++)
/*     */             {
/* 226 */               int l3 = k3 % 2;
/* 227 */               int i4 = 1 - l3;
/*     */               
/* 229 */               if (k3 % 4 >= 2)
/*     */               {
/* 231 */                 l3 = -l3;
/* 232 */                 i4 = -i4;
/*     */               }
/*     */               
/* 235 */               for (int j4 = 0; j4 < 3; j4++)
/*     */               {
/* 237 */                 for (int k4 = 0; k4 < 4; k4++)
/*     */                 {
/* 239 */                   for (int l4 = -1; l4 < 4; l4++)
/*     */                   {
/* 241 */                     int i5 = j2 + (k4 - 1) * l3 + j4 * i4;
/* 242 */                     int j5 = j3 + l4;
/* 243 */                     int k5 = l2 + (k4 - 1) * i4 - j4 * l3;
/* 244 */                     blockpos$mutableblockpos.func_181079_c(i5, j5, k5);
/*     */                     
/* 246 */                     if (((l4 < 0) && (!this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid())) || ((l4 >= 0) && (!this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)))) {
/*     */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/* 254 */               double d5 = j3 + 0.5D - p_85188_1_.posY;
/* 255 */               double d7 = d1 * d1 + d5 * d5 + d2 * d2;
/*     */               
/* 257 */               if ((d0 < 0.0D) || (d7 < d0))
/*     */               {
/* 259 */                 d0 = d7;
/* 260 */                 i1 = j2;
/* 261 */                 j1 = j3;
/* 262 */                 k1 = l2;
/* 263 */                 l1 = k3 % 4;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 271 */     if (d0 < 0.0D)
/*     */     {
/* 273 */       for (int l5 = j - i; l5 <= j + i; l5++)
/*     */       {
/* 275 */         double d3 = l5 + 0.5D - p_85188_1_.posX;
/*     */         
/* 277 */         for (int j6 = l - i; j6 <= l + i; j6++)
/*     */         {
/* 279 */           double d4 = j6 + 0.5D - p_85188_1_.posZ;
/*     */           
/*     */ 
/* 282 */           for (int i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; i7--)
/*     */           {
/* 284 */             if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7, j6)))
/*     */             {
/* 286 */               while ((i7 > 0) && (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7 - 1, j6))))
/*     */               {
/* 288 */                 i7--;
/*     */               }
/*     */               
/* 291 */               for (int k7 = i2; k7 < i2 + 2; k7++)
/*     */               {
/* 293 */                 int j8 = k7 % 2;
/* 294 */                 int j9 = 1 - j8;
/*     */                 
/* 296 */                 for (int j10 = 0; j10 < 4; j10++)
/*     */                 {
/* 298 */                   for (int j11 = -1; j11 < 4; j11++)
/*     */                   {
/* 300 */                     int j12 = l5 + (j10 - 1) * j8;
/* 301 */                     int i13 = i7 + j11;
/* 302 */                     int j13 = j6 + (j10 - 1) * j9;
/* 303 */                     blockpos$mutableblockpos.func_181079_c(j12, i13, j13);
/*     */                     
/* 305 */                     if (((j11 < 0) && (!this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid())) || ((j11 >= 0) && (!this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)))) {
/*     */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 
/*     */ 
/* 312 */                 double d6 = i7 + 0.5D - p_85188_1_.posY;
/* 313 */                 double d8 = d3 * d3 + d6 * d6 + d4 * d4;
/*     */                 
/* 315 */                 if ((d0 < 0.0D) || (d8 < d0))
/*     */                 {
/* 317 */                   d0 = d8;
/* 318 */                   i1 = l5;
/* 319 */                   j1 = i7;
/* 320 */                   k1 = j6;
/* 321 */                   l1 = k7 % 2;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 330 */     int i6 = i1;
/* 331 */     int k2 = j1;
/* 332 */     int k6 = k1;
/* 333 */     int l6 = l1 % 2;
/* 334 */     int i3 = 1 - l6;
/*     */     
/* 336 */     if (l1 % 4 >= 2)
/*     */     {
/* 338 */       l6 = -l6;
/* 339 */       i3 = -i3;
/*     */     }
/*     */     
/* 342 */     if (d0 < 0.0D)
/*     */     {
/* 344 */       j1 = MathHelper.clamp_int(j1, 70, this.worldServerInstance.getActualHeight() - 10);
/* 345 */       k2 = j1;
/*     */       
/* 347 */       for (int j7 = -1; j7 <= 1; j7++)
/*     */       {
/* 349 */         for (int l7 = 1; l7 < 3; l7++)
/*     */         {
/* 351 */           for (int k8 = -1; k8 < 3; k8++)
/*     */           {
/* 353 */             int k9 = i6 + (l7 - 1) * l6 + j7 * i3;
/* 354 */             int k10 = k2 + k8;
/* 355 */             int k11 = k6 + (l7 - 1) * i3 - j7 * l6;
/* 356 */             boolean flag = k8 < 0;
/* 357 */             this.worldServerInstance.setBlockState(new BlockPos(k9, k10, k11), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 363 */     IBlockState iblockstate = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, l6 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
/*     */     
/* 365 */     for (int i8 = 0; i8 < 4; i8++)
/*     */     {
/* 367 */       for (int l8 = 0; l8 < 4; l8++)
/*     */       {
/* 369 */         for (int l9 = -1; l9 < 4; l9++)
/*     */         {
/* 371 */           int l10 = i6 + (l8 - 1) * l6;
/* 372 */           int l11 = k2 + l9;
/* 373 */           int k12 = k6 + (l8 - 1) * i3;
/* 374 */           boolean flag1 = (l8 == 0) || (l8 == 3) || (l9 == -1) || (l9 == 3);
/* 375 */           this.worldServerInstance.setBlockState(new BlockPos(l10, l11, k12), flag1 ? Blocks.obsidian.getDefaultState() : iblockstate, 2);
/*     */         }
/*     */       }
/*     */       
/* 379 */       for (int i9 = 0; i9 < 4; i9++)
/*     */       {
/* 381 */         for (int i10 = -1; i10 < 4; i10++)
/*     */         {
/* 383 */           int i11 = i6 + (i9 - 1) * l6;
/* 384 */           int i12 = k2 + i10;
/* 385 */           int l12 = k6 + (i9 - 1) * i3;
/* 386 */           BlockPos blockpos = new BlockPos(i11, i12, l12);
/* 387 */           this.worldServerInstance.notifyNeighborsOfStateChange(blockpos, this.worldServerInstance.getBlockState(blockpos).getBlock());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 392 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeStalePortalLocations(long worldTime)
/*     */   {
/* 401 */     if (worldTime % 100L == 0L)
/*     */     {
/* 403 */       Iterator<Long> iterator = this.destinationCoordinateKeys.iterator();
/* 404 */       long i = worldTime - 300L;
/*     */       
/* 406 */       while (iterator.hasNext())
/*     */       {
/* 408 */         Long olong = (Long)iterator.next();
/* 409 */         PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(olong.longValue());
/*     */         
/* 411 */         if ((teleporter$portalposition == null) || (teleporter$portalposition.lastUpdateTime < i))
/*     */         {
/* 413 */           iterator.remove();
/* 414 */           this.destinationCoordinateCache.remove(olong.longValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public class PortalPosition extends BlockPos
/*     */   {
/*     */     public long lastUpdateTime;
/*     */     
/*     */     public PortalPosition(BlockPos pos, long lastUpdate)
/*     */     {
/* 426 */       super(pos.getY(), pos.getZ());
/* 427 */       this.lastUpdateTime = lastUpdate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\Teleporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */