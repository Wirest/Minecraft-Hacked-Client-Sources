/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ 
/*     */ public class BlockFire extends Block
/*     */ {
/*  25 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
/*  26 */   public static final PropertyBool FLIP = PropertyBool.create("flip");
/*  27 */   public static final PropertyBool ALT = PropertyBool.create("alt");
/*  28 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  29 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  30 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  31 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*  32 */   public static final PropertyInteger UPPER = PropertyInteger.create("upper", 0, 2);
/*  33 */   private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
/*  34 */   private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  42 */     int i = pos.getX();
/*  43 */     int j = pos.getY();
/*  44 */     int k = pos.getZ();
/*     */     
/*  46 */     if ((!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) && (!Blocks.fire.canCatchFire(worldIn, pos.down())))
/*     */     {
/*  48 */       boolean flag = (i + j + k & 0x1) == 1;
/*  49 */       boolean flag1 = (i / 2 + j / 2 + k / 2 & 0x1) == 1;
/*  50 */       int l = 0;
/*     */       
/*  52 */       if (canCatchFire(worldIn, pos.up()))
/*     */       {
/*  54 */         l = flag ? 1 : 2;
/*     */       }
/*     */       
/*  57 */       return state.withProperty(NORTH, Boolean.valueOf(canCatchFire(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(canCatchFire(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(canCatchFire(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(canCatchFire(worldIn, pos.west()))).withProperty(UPPER, Integer.valueOf(l)).withProperty(FLIP, Boolean.valueOf(flag1)).withProperty(ALT, Boolean.valueOf(flag));
/*     */     }
/*     */     
/*     */ 
/*  61 */     return getDefaultState();
/*     */   }
/*     */   
/*     */ 
/*     */   protected BlockFire()
/*     */   {
/*  67 */     super(Material.fire);
/*  68 */     setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(FLIP, Boolean.valueOf(false)).withProperty(ALT, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UPPER, Integer.valueOf(0)));
/*  69 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public static void init()
/*     */   {
/*  74 */     Blocks.fire.setFireInfo(Blocks.planks, 5, 20);
/*  75 */     Blocks.fire.setFireInfo(Blocks.double_wooden_slab, 5, 20);
/*  76 */     Blocks.fire.setFireInfo(Blocks.wooden_slab, 5, 20);
/*  77 */     Blocks.fire.setFireInfo(Blocks.oak_fence_gate, 5, 20);
/*  78 */     Blocks.fire.setFireInfo(Blocks.spruce_fence_gate, 5, 20);
/*  79 */     Blocks.fire.setFireInfo(Blocks.birch_fence_gate, 5, 20);
/*  80 */     Blocks.fire.setFireInfo(Blocks.jungle_fence_gate, 5, 20);
/*  81 */     Blocks.fire.setFireInfo(Blocks.dark_oak_fence_gate, 5, 20);
/*  82 */     Blocks.fire.setFireInfo(Blocks.acacia_fence_gate, 5, 20);
/*  83 */     Blocks.fire.setFireInfo(Blocks.oak_fence, 5, 20);
/*  84 */     Blocks.fire.setFireInfo(Blocks.spruce_fence, 5, 20);
/*  85 */     Blocks.fire.setFireInfo(Blocks.birch_fence, 5, 20);
/*  86 */     Blocks.fire.setFireInfo(Blocks.jungle_fence, 5, 20);
/*  87 */     Blocks.fire.setFireInfo(Blocks.dark_oak_fence, 5, 20);
/*  88 */     Blocks.fire.setFireInfo(Blocks.acacia_fence, 5, 20);
/*  89 */     Blocks.fire.setFireInfo(Blocks.oak_stairs, 5, 20);
/*  90 */     Blocks.fire.setFireInfo(Blocks.birch_stairs, 5, 20);
/*  91 */     Blocks.fire.setFireInfo(Blocks.spruce_stairs, 5, 20);
/*  92 */     Blocks.fire.setFireInfo(Blocks.jungle_stairs, 5, 20);
/*  93 */     Blocks.fire.setFireInfo(Blocks.log, 5, 5);
/*  94 */     Blocks.fire.setFireInfo(Blocks.log2, 5, 5);
/*  95 */     Blocks.fire.setFireInfo(Blocks.leaves, 30, 60);
/*  96 */     Blocks.fire.setFireInfo(Blocks.leaves2, 30, 60);
/*  97 */     Blocks.fire.setFireInfo(Blocks.bookshelf, 30, 20);
/*  98 */     Blocks.fire.setFireInfo(Blocks.tnt, 15, 100);
/*  99 */     Blocks.fire.setFireInfo(Blocks.tallgrass, 60, 100);
/* 100 */     Blocks.fire.setFireInfo(Blocks.double_plant, 60, 100);
/* 101 */     Blocks.fire.setFireInfo(Blocks.yellow_flower, 60, 100);
/* 102 */     Blocks.fire.setFireInfo(Blocks.red_flower, 60, 100);
/* 103 */     Blocks.fire.setFireInfo(Blocks.deadbush, 60, 100);
/* 104 */     Blocks.fire.setFireInfo(Blocks.wool, 30, 60);
/* 105 */     Blocks.fire.setFireInfo(Blocks.vine, 15, 100);
/* 106 */     Blocks.fire.setFireInfo(Blocks.coal_block, 5, 5);
/* 107 */     Blocks.fire.setFireInfo(Blocks.hay_block, 60, 20);
/* 108 */     Blocks.fire.setFireInfo(Blocks.carpet, 60, 20);
/*     */   }
/*     */   
/*     */   public void setFireInfo(Block blockIn, int encouragement, int flammability)
/*     */   {
/* 113 */     this.encouragements.put(blockIn, Integer.valueOf(encouragement));
/* 114 */     this.flammabilities.put(blockIn, Integer.valueOf(flammability));
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 119 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 132 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 140 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/* 148 */     return 30;
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 153 */     if (worldIn.getGameRules().getBoolean("doFireTick"))
/*     */     {
/* 155 */       if (!canPlaceBlockAt(worldIn, pos))
/*     */       {
/* 157 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       
/* 160 */       Block block = worldIn.getBlockState(pos.down()).getBlock();
/* 161 */       boolean flag = block == Blocks.netherrack;
/*     */       
/* 163 */       if (((worldIn.provider instanceof net.minecraft.world.WorldProviderEnd)) && (block == Blocks.bedrock))
/*     */       {
/* 165 */         flag = true;
/*     */       }
/*     */       
/* 168 */       if ((!flag) && (worldIn.isRaining()) && (canDie(worldIn, pos)))
/*     */       {
/* 170 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else
/*     */       {
/* 174 */         int i = ((Integer)state.getValue(AGE)).intValue();
/*     */         
/* 176 */         if (i < 15)
/*     */         {
/* 178 */           state = state.withProperty(AGE, Integer.valueOf(i + rand.nextInt(3) / 2));
/* 179 */           worldIn.setBlockState(pos, state, 4);
/*     */         }
/*     */         
/* 182 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + rand.nextInt(10));
/*     */         
/* 184 */         if (!flag)
/*     */         {
/* 186 */           if (!canNeighborCatchFire(worldIn, pos))
/*     */           {
/* 188 */             if ((!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) || (i > 3))
/*     */             {
/* 190 */               worldIn.setBlockToAir(pos);
/*     */             }
/*     */             
/* 193 */             return;
/*     */           }
/*     */           
/* 196 */           if ((!canCatchFire(worldIn, pos.down())) && (i == 15) && (rand.nextInt(4) == 0))
/*     */           {
/* 198 */             worldIn.setBlockToAir(pos);
/* 199 */             return;
/*     */           }
/*     */         }
/*     */         
/* 203 */         boolean flag1 = worldIn.isBlockinHighHumidity(pos);
/* 204 */         int j = 0;
/*     */         
/* 206 */         if (flag1)
/*     */         {
/* 208 */           j = -50;
/*     */         }
/*     */         
/* 211 */         catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
/* 212 */         catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
/* 213 */         catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
/* 214 */         catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
/* 215 */         catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
/* 216 */         catchOnFire(worldIn, pos.south(), 300 + j, rand, i);
/*     */         
/* 218 */         for (int k = -1; k <= 1; k++)
/*     */         {
/* 220 */           for (int l = -1; l <= 1; l++)
/*     */           {
/* 222 */             for (int i1 = -1; i1 <= 4; i1++)
/*     */             {
/* 224 */               if ((k != 0) || (i1 != 0) || (l != 0))
/*     */               {
/* 226 */                 int j1 = 100;
/*     */                 
/* 228 */                 if (i1 > 1)
/*     */                 {
/* 230 */                   j1 += (i1 - 1) * 100;
/*     */                 }
/*     */                 
/* 233 */                 BlockPos blockpos = pos.add(k, i1, l);
/* 234 */                 int k1 = getNeighborEncouragement(worldIn, blockpos);
/*     */                 
/* 236 */                 if (k1 > 0)
/*     */                 {
/* 238 */                   int l1 = (k1 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);
/*     */                   
/* 240 */                   if (flag1)
/*     */                   {
/* 242 */                     l1 /= 2;
/*     */                   }
/*     */                   
/* 245 */                   if ((l1 > 0) && (rand.nextInt(j1) <= l1) && ((!worldIn.isRaining()) || (!canDie(worldIn, blockpos))))
/*     */                   {
/* 247 */                     int i2 = i + rand.nextInt(5) / 4;
/*     */                     
/* 249 */                     if (i2 > 15)
/*     */                     {
/* 251 */                       i2 = 15;
/*     */                     }
/*     */                     
/* 254 */                     worldIn.setBlockState(blockpos, state.withProperty(AGE, Integer.valueOf(i2)), 3);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canDie(World worldIn, BlockPos pos)
/*     */   {
/* 267 */     return (worldIn.canLightningStrike(pos)) || (worldIn.canLightningStrike(pos.west())) || (worldIn.canLightningStrike(pos.east())) || (worldIn.canLightningStrike(pos.north())) || (worldIn.canLightningStrike(pos.south()));
/*     */   }
/*     */   
/*     */   public boolean requiresUpdates()
/*     */   {
/* 272 */     return false;
/*     */   }
/*     */   
/*     */   private int getFlammability(Block blockIn)
/*     */   {
/* 277 */     Integer integer = (Integer)this.flammabilities.get(blockIn);
/* 278 */     return integer == null ? 0 : integer.intValue();
/*     */   }
/*     */   
/*     */   private int getEncouragement(Block blockIn)
/*     */   {
/* 283 */     Integer integer = (Integer)this.encouragements.get(blockIn);
/* 284 */     return integer == null ? 0 : integer.intValue();
/*     */   }
/*     */   
/*     */   private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age)
/*     */   {
/* 289 */     int i = getFlammability(worldIn.getBlockState(pos).getBlock());
/*     */     
/* 291 */     if (random.nextInt(chance) < i)
/*     */     {
/* 293 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 295 */       if ((random.nextInt(age + 10) < 5) && (!worldIn.canLightningStrike(pos)))
/*     */       {
/* 297 */         int j = age + random.nextInt(5) / 4;
/*     */         
/* 299 */         if (j > 15)
/*     */         {
/* 301 */           j = 15;
/*     */         }
/*     */         
/* 304 */         worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, Integer.valueOf(j)), 3);
/*     */       }
/*     */       else
/*     */       {
/* 308 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       
/* 311 */       if (iblockstate.getBlock() == Blocks.tnt)
/*     */       {
/* 313 */         Blocks.tnt.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 320 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 322 */       if (canCatchFire(worldIn, pos.offset(enumfacing)))
/*     */       {
/* 324 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 328 */     return false;
/*     */   }
/*     */   
/*     */   private int getNeighborEncouragement(World worldIn, BlockPos pos)
/*     */   {
/* 333 */     if (!worldIn.isAirBlock(pos))
/*     */     {
/* 335 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 339 */     int i = 0;
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 341 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 343 */       i = Math.max(getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
/*     */     }
/*     */     
/* 346 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCollidable()
/*     */   {
/* 355 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 363 */     return getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 368 */     return (World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) || (canNeighborCatchFire(worldIn, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 376 */     if ((!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) && (!canNeighborCatchFire(worldIn, pos)))
/*     */     {
/* 378 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 384 */     if ((worldIn.provider.getDimensionId() > 0) || (!Blocks.portal.func_176548_d(worldIn, pos)))
/*     */     {
/* 386 */       if ((!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) && (!canNeighborCatchFire(worldIn, pos)))
/*     */       {
/* 388 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else
/*     */       {
/* 392 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + worldIn.rand.nextInt(10));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 399 */     if (rand.nextInt(24) == 0)
/*     */     {
/* 401 */       worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
/*     */     }
/*     */     
/* 404 */     if ((!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) && (!Blocks.fire.canCatchFire(worldIn, pos.down())))
/*     */     {
/* 406 */       if (Blocks.fire.canCatchFire(worldIn, pos.west()))
/*     */       {
/* 408 */         for (int j = 0; j < 2; j++)
/*     */         {
/* 410 */           double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
/* 411 */           double d8 = pos.getY() + rand.nextDouble();
/* 412 */           double d13 = pos.getZ() + rand.nextDouble();
/* 413 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 417 */       if (Blocks.fire.canCatchFire(worldIn, pos.east()))
/*     */       {
/* 419 */         for (int k = 0; k < 2; k++)
/*     */         {
/* 421 */           double d4 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612D;
/* 422 */           double d9 = pos.getY() + rand.nextDouble();
/* 423 */           double d14 = pos.getZ() + rand.nextDouble();
/* 424 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 428 */       if (Blocks.fire.canCatchFire(worldIn, pos.north()))
/*     */       {
/* 430 */         for (int l = 0; l < 2; l++)
/*     */         {
/* 432 */           double d5 = pos.getX() + rand.nextDouble();
/* 433 */           double d10 = pos.getY() + rand.nextDouble();
/* 434 */           double d15 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
/* 435 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 439 */       if (Blocks.fire.canCatchFire(worldIn, pos.south()))
/*     */       {
/* 441 */         for (int i1 = 0; i1 < 2; i1++)
/*     */         {
/* 443 */           double d6 = pos.getX() + rand.nextDouble();
/* 444 */           double d11 = pos.getY() + rand.nextDouble();
/* 445 */           double d16 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612D;
/* 446 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 450 */       if (Blocks.fire.canCatchFire(worldIn, pos.up()))
/*     */       {
/* 452 */         for (int j1 = 0; j1 < 2; j1++)
/*     */         {
/* 454 */           double d7 = pos.getX() + rand.nextDouble();
/* 455 */           double d12 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612D;
/* 456 */           double d17 = pos.getZ() + rand.nextDouble();
/* 457 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 463 */       for (int i = 0; i < 3; i++)
/*     */       {
/* 465 */         double d0 = pos.getX() + rand.nextDouble();
/* 466 */         double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
/* 467 */         double d2 = pos.getZ() + rand.nextDouble();
/* 468 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/* 478 */     return MapColor.tntColor;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 483 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 491 */     return getDefaultState().withProperty(AGE, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 499 */     return ((Integer)state.getValue(AGE)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 504 */     return new BlockState(this, new IProperty[] { AGE, NORTH, EAST, SOUTH, WEST, UPPER, FLIP, ALT });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */