/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockLog;
/*     */ import net.minecraft.block.BlockLog.EnumAxis;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenBigTree extends WorldGenAbstractTree
/*     */ {
/*     */   private Random rand;
/*     */   private World world;
/*  20 */   private BlockPos basePos = BlockPos.ORIGIN;
/*     */   int heightLimit;
/*     */   int height;
/*  23 */   double heightAttenuation = 0.618D;
/*  24 */   double branchSlope = 0.381D;
/*  25 */   double scaleWidth = 1.0D;
/*  26 */   double leafDensity = 1.0D;
/*  27 */   int trunkSize = 1;
/*  28 */   int heightLimitLimit = 12;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   int leafDistanceLimit = 4;
/*     */   List<FoliageCoordinates> field_175948_j;
/*     */   
/*     */   public WorldGenBigTree(boolean p_i2008_1_)
/*     */   {
/*  38 */     super(p_i2008_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateLeafNodeList()
/*     */   {
/*  46 */     this.height = ((int)(this.heightLimit * this.heightAttenuation));
/*     */     
/*  48 */     if (this.height >= this.heightLimit)
/*     */     {
/*  50 */       this.height = (this.heightLimit - 1);
/*     */     }
/*     */     
/*  53 */     int i = (int)(1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));
/*     */     
/*  55 */     if (i < 1)
/*     */     {
/*  57 */       i = 1;
/*     */     }
/*     */     
/*  60 */     int j = this.basePos.getY() + this.height;
/*  61 */     int k = this.heightLimit - this.leafDistanceLimit;
/*  62 */     this.field_175948_j = com.google.common.collect.Lists.newArrayList();
/*  63 */     this.field_175948_j.add(new FoliageCoordinates(this.basePos.up(k), j));
/*  65 */     for (; 
/*  65 */         k >= 0; k--)
/*     */     {
/*  67 */       float f = layerSize(k);
/*     */       
/*  69 */       if (f >= 0.0F)
/*     */       {
/*  71 */         for (int l = 0; l < i; l++)
/*     */         {
/*  73 */           double d0 = this.scaleWidth * f * (this.rand.nextFloat() + 0.328D);
/*  74 */           double d1 = this.rand.nextFloat() * 2.0F * 3.141592653589793D;
/*  75 */           double d2 = d0 * Math.sin(d1) + 0.5D;
/*  76 */           double d3 = d0 * Math.cos(d1) + 0.5D;
/*  77 */           BlockPos blockpos = this.basePos.add(d2, k - 1, d3);
/*  78 */           BlockPos blockpos1 = blockpos.up(this.leafDistanceLimit);
/*     */           
/*  80 */           if (checkBlockLine(blockpos, blockpos1) == -1)
/*     */           {
/*  82 */             int i1 = this.basePos.getX() - blockpos.getX();
/*  83 */             int j1 = this.basePos.getZ() - blockpos.getZ();
/*  84 */             double d4 = blockpos.getY() - Math.sqrt(i1 * i1 + j1 * j1) * this.branchSlope;
/*  85 */             int k1 = d4 > j ? j : (int)d4;
/*  86 */             BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());
/*     */             
/*  88 */             if (checkBlockLine(blockpos2, blockpos) == -1)
/*     */             {
/*  90 */               this.field_175948_j.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void func_181631_a(BlockPos p_181631_1_, float p_181631_2_, IBlockState p_181631_3_)
/*     */   {
/* 100 */     int i = (int)(p_181631_2_ + 0.618D);
/*     */     
/* 102 */     for (int j = -i; j <= i; j++)
/*     */     {
/* 104 */       for (int k = -i; k <= i; k++)
/*     */       {
/* 106 */         if (Math.pow(Math.abs(j) + 0.5D, 2.0D) + Math.pow(Math.abs(k) + 0.5D, 2.0D) <= p_181631_2_ * p_181631_2_)
/*     */         {
/* 108 */           BlockPos blockpos = p_181631_1_.add(j, 0, k);
/* 109 */           Material material = this.world.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 111 */           if ((material == Material.air) || (material == Material.leaves))
/*     */           {
/* 113 */             setBlockAndNotifyAdequately(this.world, blockpos, p_181631_3_);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   float layerSize(int p_76490_1_)
/*     */   {
/* 125 */     if (p_76490_1_ < this.heightLimit * 0.3F)
/*     */     {
/* 127 */       return -1.0F;
/*     */     }
/*     */     
/*     */ 
/* 131 */     float f = this.heightLimit / 2.0F;
/* 132 */     float f1 = f - p_76490_1_;
/* 133 */     float f2 = MathHelper.sqrt_float(f * f - f1 * f1);
/*     */     
/* 135 */     if (f1 == 0.0F)
/*     */     {
/* 137 */       f2 = f;
/*     */     }
/* 139 */     else if (Math.abs(f1) >= f)
/*     */     {
/* 141 */       return 0.0F;
/*     */     }
/*     */     
/* 144 */     return f2 * 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */   float leafSize(int p_76495_1_)
/*     */   {
/* 150 */     return (p_76495_1_ >= 0) && (p_76495_1_ < this.leafDistanceLimit) ? 2.0F : (p_76495_1_ != 0) && (p_76495_1_ != this.leafDistanceLimit - 1) ? 3.0F : -1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateLeafNode(BlockPos pos)
/*     */   {
/* 158 */     for (int i = 0; i < this.leafDistanceLimit; i++)
/*     */     {
/* 160 */       func_181631_a(pos.up(i), leafSize(i), Blocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
/*     */     }
/*     */   }
/*     */   
/*     */   void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_)
/*     */   {
/* 166 */     BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
/* 167 */     int i = getGreatestDistance(blockpos);
/* 168 */     float f = blockpos.getX() / i;
/* 169 */     float f1 = blockpos.getY() / i;
/* 170 */     float f2 = blockpos.getZ() / i;
/*     */     
/* 172 */     for (int j = 0; j <= i; j++)
/*     */     {
/* 174 */       BlockPos blockpos1 = p_175937_1_.add(0.5F + j * f, 0.5F + j * f1, 0.5F + j * f2);
/* 175 */       BlockLog.EnumAxis blocklog$enumaxis = func_175938_b(p_175937_1_, blockpos1);
/* 176 */       setBlockAndNotifyAdequately(this.world, blockpos1, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklog$enumaxis));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getGreatestDistance(BlockPos posIn)
/*     */   {
/* 185 */     int i = MathHelper.abs_int(posIn.getX());
/* 186 */     int j = MathHelper.abs_int(posIn.getY());
/* 187 */     int k = MathHelper.abs_int(posIn.getZ());
/* 188 */     return j > i ? j : (k > i) && (k > j) ? k : i;
/*     */   }
/*     */   
/*     */   private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_)
/*     */   {
/* 193 */     BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
/* 194 */     int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
/* 195 */     int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
/* 196 */     int k = Math.max(i, j);
/*     */     
/* 198 */     if (k > 0)
/*     */     {
/* 200 */       if (i == k)
/*     */       {
/* 202 */         blocklog$enumaxis = BlockLog.EnumAxis.X;
/*     */       }
/* 204 */       else if (j == k)
/*     */       {
/* 206 */         blocklog$enumaxis = BlockLog.EnumAxis.Z;
/*     */       }
/*     */     }
/*     */     
/* 210 */     return blocklog$enumaxis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateLeaves()
/*     */   {
/* 218 */     for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j)
/*     */     {
/* 220 */       generateLeafNode(worldgenbigtree$foliagecoordinates);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean leafNodeNeedsBase(int p_76493_1_)
/*     */   {
/* 229 */     return p_76493_1_ >= this.heightLimit * 0.2D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateTrunk()
/*     */   {
/* 238 */     BlockPos blockpos = this.basePos;
/* 239 */     BlockPos blockpos1 = this.basePos.up(this.height);
/* 240 */     Block block = Blocks.log;
/* 241 */     func_175937_a(blockpos, blockpos1, block);
/*     */     
/* 243 */     if (this.trunkSize == 2)
/*     */     {
/* 245 */       func_175937_a(blockpos.east(), blockpos1.east(), block);
/* 246 */       func_175937_a(blockpos.east().south(), blockpos1.east().south(), block);
/* 247 */       func_175937_a(blockpos.south(), blockpos1.south(), block);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateLeafNodeBases()
/*     */   {
/* 256 */     for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j)
/*     */     {
/* 258 */       int i = worldgenbigtree$foliagecoordinates.func_177999_q();
/* 259 */       BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
/*     */       
/* 261 */       if ((!blockpos.equals(worldgenbigtree$foliagecoordinates)) && (leafNodeNeedsBase(i - this.basePos.getY())))
/*     */       {
/* 263 */         func_175937_a(blockpos, worldgenbigtree$foliagecoordinates, Blocks.log);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int checkBlockLine(BlockPos posOne, BlockPos posTwo)
/*     */   {
/* 274 */     BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
/* 275 */     int i = getGreatestDistance(blockpos);
/* 276 */     float f = blockpos.getX() / i;
/* 277 */     float f1 = blockpos.getY() / i;
/* 278 */     float f2 = blockpos.getZ() / i;
/*     */     
/* 280 */     if (i == 0)
/*     */     {
/* 282 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 286 */     for (int j = 0; j <= i; j++)
/*     */     {
/* 288 */       BlockPos blockpos1 = posOne.add(0.5F + j * f, 0.5F + j * f1, 0.5F + j * f2);
/*     */       
/* 290 */       if (!func_150523_a(this.world.getBlockState(blockpos1).getBlock()))
/*     */       {
/* 292 */         return j;
/*     */       }
/*     */     }
/*     */     
/* 296 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_175904_e()
/*     */   {
/* 302 */     this.leafDistanceLimit = 5;
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/* 307 */     this.world = worldIn;
/* 308 */     this.basePos = position;
/* 309 */     this.rand = new Random(rand.nextLong());
/*     */     
/* 311 */     if (this.heightLimit == 0)
/*     */     {
/* 313 */       this.heightLimit = (5 + this.rand.nextInt(this.heightLimitLimit));
/*     */     }
/*     */     
/* 316 */     if (!validTreeLocation())
/*     */     {
/* 318 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 322 */     generateLeafNodeList();
/* 323 */     generateLeaves();
/* 324 */     generateTrunk();
/* 325 */     generateLeafNodeBases();
/* 326 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean validTreeLocation()
/*     */   {
/* 336 */     Block block = this.world.getBlockState(this.basePos.down()).getBlock();
/*     */     
/* 338 */     if ((block != Blocks.dirt) && (block != Blocks.grass) && (block != Blocks.farmland))
/*     */     {
/* 340 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 344 */     int i = checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
/*     */     
/* 346 */     if (i == -1)
/*     */     {
/* 348 */       return true;
/*     */     }
/* 350 */     if (i < 6)
/*     */     {
/* 352 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 356 */     this.heightLimit = i;
/* 357 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   static class FoliageCoordinates
/*     */     extends BlockPos
/*     */   {
/*     */     private final int field_178000_b;
/*     */     
/*     */     public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_)
/*     */     {
/* 368 */       super(p_i45635_1_.getY(), p_i45635_1_.getZ());
/* 369 */       this.field_178000_b = p_i45635_2_;
/*     */     }
/*     */     
/*     */     public int func_177999_q()
/*     */     {
/* 374 */       return this.field_178000_b;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenBigTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */