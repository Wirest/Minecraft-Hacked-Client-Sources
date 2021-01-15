/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStairs extends Block
/*     */ {
/*  30 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*  31 */   public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);
/*  32 */   public static final PropertyEnum<EnumShape> SHAPE = PropertyEnum.create("shape", EnumShape.class);
/*  33 */   private static final int[][] field_150150_a = { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 2 } };
/*     */   private final Block modelBlock;
/*     */   private final IBlockState modelState;
/*     */   private boolean hasRaytraced;
/*     */   private int rayTracePass;
/*     */   
/*     */   protected BlockStairs(IBlockState modelState)
/*     */   {
/*  41 */     super(modelState.getBlock().blockMaterial);
/*  42 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, EnumHalf.BOTTOM).withProperty(SHAPE, EnumShape.STRAIGHT));
/*  43 */     this.modelBlock = modelState.getBlock();
/*  44 */     this.modelState = modelState;
/*  45 */     setHardness(this.modelBlock.blockHardness);
/*  46 */     setResistance(this.modelBlock.blockResistance / 3.0F);
/*  47 */     setStepSound(this.modelBlock.stepSound);
/*  48 */     setLightOpacity(255);
/*  49 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  54 */     if (this.hasRaytraced)
/*     */     {
/*  56 */       setBlockBounds(0.5F * (this.rayTracePass % 2), 0.5F * (this.rayTracePass / 4 % 2), 0.5F * (this.rayTracePass / 2 % 2), 0.5F + 0.5F * (this.rayTracePass % 2), 0.5F + 0.5F * (this.rayTracePass / 4 % 2), 0.5F + 0.5F * (this.rayTracePass / 2 % 2));
/*     */     }
/*     */     else
/*     */     {
/*  60 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  69 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  74 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  82 */     if (worldIn.getBlockState(pos).getValue(HALF) == EnumHalf.TOP)
/*     */     {
/*  84 */       setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/*  88 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isBlockStairs(Block blockIn)
/*     */   {
/*  97 */     return blockIn instanceof BlockStairs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 105 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 106 */     Block block = iblockstate.getBlock();
/* 107 */     return (isBlockStairs(block)) && (iblockstate.getValue(HALF) == state.getValue(HALF)) && (iblockstate.getValue(FACING) == state.getValue(FACING));
/*     */   }
/*     */   
/*     */   public int func_176307_f(IBlockAccess blockAccess, BlockPos pos)
/*     */   {
/* 112 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 113 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/* 114 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue(HALF);
/* 115 */     boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
/*     */     
/* 117 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 119 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
/* 120 */       Block block = iblockstate1.getBlock();
/*     */       
/* 122 */       if ((isBlockStairs(block)) && (blockstairs$enumhalf == iblockstate1.getValue(HALF)))
/*     */       {
/* 124 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
/*     */         
/* 126 */         if ((enumfacing1 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 128 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 131 */         if ((enumfacing1 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 133 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     }
/* 137 */     else if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 139 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
/* 140 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 142 */       if ((isBlockStairs(block1)) && (blockstairs$enumhalf == iblockstate2.getValue(HALF)))
/*     */       {
/* 144 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
/*     */         
/* 146 */         if ((enumfacing2 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 148 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 151 */         if ((enumfacing2 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 153 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     }
/* 157 */     else if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 159 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
/* 160 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 162 */       if ((isBlockStairs(block2)) && (blockstairs$enumhalf == iblockstate3.getValue(HALF)))
/*     */       {
/* 164 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue(FACING);
/*     */         
/* 166 */         if ((enumfacing3 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 168 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 171 */         if ((enumfacing3 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 173 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     }
/* 177 */     else if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 179 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
/* 180 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 182 */       if ((isBlockStairs(block3)) && (blockstairs$enumhalf == iblockstate4.getValue(HALF)))
/*     */       {
/* 184 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue(FACING);
/*     */         
/* 186 */         if ((enumfacing4 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 188 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 191 */         if ((enumfacing4 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 193 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     return 0;
/*     */   }
/*     */   
/*     */   public int func_176305_g(IBlockAccess blockAccess, BlockPos pos)
/*     */   {
/* 203 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 204 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/* 205 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue(HALF);
/* 206 */     boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
/*     */     
/* 208 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 210 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
/* 211 */       Block block = iblockstate1.getBlock();
/*     */       
/* 213 */       if ((isBlockStairs(block)) && (blockstairs$enumhalf == iblockstate1.getValue(HALF)))
/*     */       {
/* 215 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
/*     */         
/* 217 */         if ((enumfacing1 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 219 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 222 */         if ((enumfacing1 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 224 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     }
/* 228 */     else if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 230 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
/* 231 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 233 */       if ((isBlockStairs(block1)) && (blockstairs$enumhalf == iblockstate2.getValue(HALF)))
/*     */       {
/* 235 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
/*     */         
/* 237 */         if ((enumfacing2 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 239 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 242 */         if ((enumfacing2 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 244 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     }
/* 248 */     else if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 250 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
/* 251 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 253 */       if ((isBlockStairs(block2)) && (blockstairs$enumhalf == iblockstate3.getValue(HALF)))
/*     */       {
/* 255 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue(FACING);
/*     */         
/* 257 */         if ((enumfacing3 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 259 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 262 */         if ((enumfacing3 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 264 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     }
/* 268 */     else if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 270 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
/* 271 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 273 */       if ((isBlockStairs(block3)) && (blockstairs$enumhalf == iblockstate4.getValue(HALF)))
/*     */       {
/* 275 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue(FACING);
/*     */         
/* 277 */         if ((enumfacing4 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 279 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 282 */         if ((enumfacing4 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 284 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 289 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean func_176306_h(IBlockAccess blockAccess, BlockPos pos)
/*     */   {
/* 294 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 295 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/* 296 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue(HALF);
/* 297 */     boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
/* 298 */     float f = 0.5F;
/* 299 */     float f1 = 1.0F;
/*     */     
/* 301 */     if (flag)
/*     */     {
/* 303 */       f = 0.0F;
/* 304 */       f1 = 0.5F;
/*     */     }
/*     */     
/* 307 */     float f2 = 0.0F;
/* 308 */     float f3 = 1.0F;
/* 309 */     float f4 = 0.0F;
/* 310 */     float f5 = 0.5F;
/* 311 */     boolean flag1 = true;
/*     */     
/* 313 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 315 */       f2 = 0.5F;
/* 316 */       f5 = 1.0F;
/* 317 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
/* 318 */       Block block = iblockstate1.getBlock();
/*     */       
/* 320 */       if ((isBlockStairs(block)) && (blockstairs$enumhalf == iblockstate1.getValue(HALF)))
/*     */       {
/* 322 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
/*     */         
/* 324 */         if ((enumfacing1 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 326 */           f5 = 0.5F;
/* 327 */           flag1 = false;
/*     */         }
/* 329 */         else if ((enumfacing1 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 331 */           f4 = 0.5F;
/* 332 */           flag1 = false;
/*     */         }
/*     */       }
/*     */     }
/* 336 */     else if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 338 */       f3 = 0.5F;
/* 339 */       f5 = 1.0F;
/* 340 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
/* 341 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 343 */       if ((isBlockStairs(block1)) && (blockstairs$enumhalf == iblockstate2.getValue(HALF)))
/*     */       {
/* 345 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
/*     */         
/* 347 */         if ((enumfacing2 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 349 */           f5 = 0.5F;
/* 350 */           flag1 = false;
/*     */         }
/* 352 */         else if ((enumfacing2 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 354 */           f4 = 0.5F;
/* 355 */           flag1 = false;
/*     */         }
/*     */       }
/*     */     }
/* 359 */     else if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 361 */       f4 = 0.5F;
/* 362 */       f5 = 1.0F;
/* 363 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
/* 364 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 366 */       if ((isBlockStairs(block2)) && (blockstairs$enumhalf == iblockstate3.getValue(HALF)))
/*     */       {
/* 368 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue(FACING);
/*     */         
/* 370 */         if ((enumfacing3 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 372 */           f3 = 0.5F;
/* 373 */           flag1 = false;
/*     */         }
/* 375 */         else if ((enumfacing3 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 377 */           f2 = 0.5F;
/* 378 */           flag1 = false;
/*     */         }
/*     */       }
/*     */     }
/* 382 */     else if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 384 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
/* 385 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 387 */       if ((isBlockStairs(block3)) && (blockstairs$enumhalf == iblockstate4.getValue(HALF)))
/*     */       {
/* 389 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue(FACING);
/*     */         
/* 391 */         if ((enumfacing4 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 393 */           f3 = 0.5F;
/* 394 */           flag1 = false;
/*     */         }
/* 396 */         else if ((enumfacing4 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 398 */           f2 = 0.5F;
/* 399 */           flag1 = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 404 */     setBlockBounds(f2, f, f4, f3, f1, f5);
/* 405 */     return flag1;
/*     */   }
/*     */   
/*     */   public boolean func_176304_i(IBlockAccess blockAccess, BlockPos pos)
/*     */   {
/* 410 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 411 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/* 412 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue(HALF);
/* 413 */     boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
/* 414 */     float f = 0.5F;
/* 415 */     float f1 = 1.0F;
/*     */     
/* 417 */     if (flag)
/*     */     {
/* 419 */       f = 0.0F;
/* 420 */       f1 = 0.5F;
/*     */     }
/*     */     
/* 423 */     float f2 = 0.0F;
/* 424 */     float f3 = 0.5F;
/* 425 */     float f4 = 0.5F;
/* 426 */     float f5 = 1.0F;
/* 427 */     boolean flag1 = false;
/*     */     
/* 429 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 431 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
/* 432 */       Block block = iblockstate1.getBlock();
/*     */       
/* 434 */       if ((isBlockStairs(block)) && (blockstairs$enumhalf == iblockstate1.getValue(HALF)))
/*     */       {
/* 436 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
/*     */         
/* 438 */         if ((enumfacing1 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 440 */           f4 = 0.0F;
/* 441 */           f5 = 0.5F;
/* 442 */           flag1 = true;
/*     */         }
/* 444 */         else if ((enumfacing1 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 446 */           f4 = 0.5F;
/* 447 */           f5 = 1.0F;
/* 448 */           flag1 = true;
/*     */         }
/*     */       }
/*     */     }
/* 452 */     else if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 454 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
/* 455 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 457 */       if ((isBlockStairs(block1)) && (blockstairs$enumhalf == iblockstate2.getValue(HALF)))
/*     */       {
/* 459 */         f2 = 0.5F;
/* 460 */         f3 = 1.0F;
/* 461 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
/*     */         
/* 463 */         if ((enumfacing2 == EnumFacing.NORTH) && (!isSameStair(blockAccess, pos.north(), iblockstate)))
/*     */         {
/* 465 */           f4 = 0.0F;
/* 466 */           f5 = 0.5F;
/* 467 */           flag1 = true;
/*     */         }
/* 469 */         else if ((enumfacing2 == EnumFacing.SOUTH) && (!isSameStair(blockAccess, pos.south(), iblockstate)))
/*     */         {
/* 471 */           f4 = 0.5F;
/* 472 */           f5 = 1.0F;
/* 473 */           flag1 = true;
/*     */         }
/*     */       }
/*     */     }
/* 477 */     else if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 479 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
/* 480 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 482 */       if ((isBlockStairs(block2)) && (blockstairs$enumhalf == iblockstate3.getValue(HALF)))
/*     */       {
/* 484 */         f4 = 0.0F;
/* 485 */         f5 = 0.5F;
/* 486 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue(FACING);
/*     */         
/* 488 */         if ((enumfacing3 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 490 */           flag1 = true;
/*     */         }
/* 492 */         else if ((enumfacing3 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 494 */           f2 = 0.5F;
/* 495 */           f3 = 1.0F;
/* 496 */           flag1 = true;
/*     */         }
/*     */       }
/*     */     }
/* 500 */     else if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 502 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
/* 503 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 505 */       if ((isBlockStairs(block3)) && (blockstairs$enumhalf == iblockstate4.getValue(HALF)))
/*     */       {
/* 507 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue(FACING);
/*     */         
/* 509 */         if ((enumfacing4 == EnumFacing.WEST) && (!isSameStair(blockAccess, pos.west(), iblockstate)))
/*     */         {
/* 511 */           flag1 = true;
/*     */         }
/* 513 */         else if ((enumfacing4 == EnumFacing.EAST) && (!isSameStair(blockAccess, pos.east(), iblockstate)))
/*     */         {
/* 515 */           f2 = 0.5F;
/* 516 */           f3 = 1.0F;
/* 517 */           flag1 = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 522 */     if (flag1)
/*     */     {
/* 524 */       setBlockBounds(f2, f, f4, f3, f1, f5);
/*     */     }
/*     */     
/* 527 */     return flag1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/* 535 */     setBaseCollisionBounds(worldIn, pos);
/* 536 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 537 */     boolean flag = func_176306_h(worldIn, pos);
/* 538 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     
/* 540 */     if ((flag) && (func_176304_i(worldIn, pos)))
/*     */     {
/* 542 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/* 545 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 550 */     this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
/*     */   }
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
/*     */   {
/* 555 */     this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 563 */     this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 568 */     return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getExplosionResistance(Entity exploder)
/*     */   {
/* 576 */     return this.modelBlock.getExplosionResistance(exploder);
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 581 */     return this.modelBlock.getBlockLayer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/* 589 */     return this.modelBlock.tickRate(worldIn);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*     */   {
/* 594 */     return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */   
/*     */   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
/*     */   {
/* 599 */     return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCollidable()
/*     */   {
/* 607 */     return this.modelBlock.isCollidable();
/*     */   }
/*     */   
/*     */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
/*     */   {
/* 612 */     return this.modelBlock.canCollideCheck(state, hitIfLiquid);
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 617 */     return this.modelBlock.canPlaceBlockAt(worldIn, pos);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 622 */     onNeighborBlockChange(worldIn, pos, this.modelState, net.minecraft.init.Blocks.air);
/* 623 */     this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 628 */     this.modelBlock.breakBlock(worldIn, pos, this.modelState);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
/*     */   {
/* 636 */     this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 641 */     this.modelBlock.updateTick(worldIn, pos, state, rand);
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 646 */     return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
/*     */   {
/* 654 */     this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/* 662 */     return this.modelBlock.getMapColor(this.modelState);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 671 */     IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
/* 672 */     iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, EnumShape.STRAIGHT);
/* 673 */     return (facing != EnumFacing.DOWN) && ((facing == EnumFacing.UP) || (hitY <= 0.5D)) ? iblockstate.withProperty(HALF, EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, EnumHalf.TOP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
/*     */   {
/* 681 */     MovingObjectPosition[] amovingobjectposition = new MovingObjectPosition[8];
/* 682 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 683 */     int i = ((EnumFacing)iblockstate.getValue(FACING)).getHorizontalIndex();
/* 684 */     boolean flag = iblockstate.getValue(HALF) == EnumHalf.TOP;
/* 685 */     int[] aint = field_150150_a[(i + 0)];
/* 686 */     this.hasRaytraced = true;
/*     */     
/* 688 */     for (int j = 0; j < 8; j++)
/*     */     {
/* 690 */       this.rayTracePass = j;
/*     */       
/* 692 */       if (Arrays.binarySearch(aint, j) < 0)
/*     */       {
/* 694 */         amovingobjectposition[j] = super.collisionRayTrace(worldIn, pos, start, end);
/*     */       }
/*     */     }
/*     */     int[] arrayOfInt1;
/* 698 */     int j = (arrayOfInt1 = aint).length; for (int i = 0; i < j; i++) { int k = arrayOfInt1[i];
/*     */       
/* 700 */       amovingobjectposition[k] = null;
/*     */     }
/*     */     
/* 703 */     MovingObjectPosition movingobjectposition1 = null;
/* 704 */     double d1 = 0.0D;
/*     */     MovingObjectPosition[] arrayOfMovingObjectPosition1;
/* 706 */     int m = (arrayOfMovingObjectPosition1 = amovingobjectposition).length; for (int k = 0; k < m; k++) { MovingObjectPosition movingobjectposition = arrayOfMovingObjectPosition1[k];
/*     */       
/* 708 */       if (movingobjectposition != null)
/*     */       {
/* 710 */         double d0 = movingobjectposition.hitVec.squareDistanceTo(end);
/*     */         
/* 712 */         if (d0 > d1)
/*     */         {
/* 714 */           movingobjectposition1 = movingobjectposition;
/* 715 */           d1 = d0;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 720 */     return movingobjectposition1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 728 */     IBlockState iblockstate = getDefaultState().withProperty(HALF, (meta & 0x4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM);
/* 729 */     iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - (meta & 0x3)));
/* 730 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 738 */     int i = 0;
/*     */     
/* 740 */     if (state.getValue(HALF) == EnumHalf.TOP)
/*     */     {
/* 742 */       i |= 0x4;
/*     */     }
/*     */     
/* 745 */     i |= 5 - ((EnumFacing)state.getValue(FACING)).getIndex();
/* 746 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 755 */     if (func_176306_h(worldIn, pos))
/*     */     {
/* 757 */       switch (func_176305_g(worldIn, pos))
/*     */       {
/*     */       case 0: 
/* 760 */         state = state.withProperty(SHAPE, EnumShape.STRAIGHT);
/* 761 */         break;
/*     */       
/*     */       case 1: 
/* 764 */         state = state.withProperty(SHAPE, EnumShape.INNER_RIGHT);
/* 765 */         break;
/*     */       
/*     */       case 2: 
/* 768 */         state = state.withProperty(SHAPE, EnumShape.INNER_LEFT);
/*     */       
/*     */       }
/*     */       
/*     */     } else {
/* 773 */       switch (func_176307_f(worldIn, pos))
/*     */       {
/*     */       case 0: 
/* 776 */         state = state.withProperty(SHAPE, EnumShape.STRAIGHT);
/* 777 */         break;
/*     */       
/*     */       case 1: 
/* 780 */         state = state.withProperty(SHAPE, EnumShape.OUTER_RIGHT);
/* 781 */         break;
/*     */       
/*     */       case 2: 
/* 784 */         state = state.withProperty(SHAPE, EnumShape.OUTER_LEFT);
/*     */       }
/*     */       
/*     */     }
/* 788 */     return state;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 793 */     return new BlockState(this, new IProperty[] { FACING, HALF, SHAPE });
/*     */   }
/*     */   
/*     */   public static enum EnumHalf implements IStringSerializable
/*     */   {
/* 798 */     TOP("top"), 
/* 799 */     BOTTOM("bottom");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private EnumHalf(String name)
/*     */     {
/* 805 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 810 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 815 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumShape implements IStringSerializable
/*     */   {
/* 821 */     STRAIGHT("straight"), 
/* 822 */     INNER_LEFT("inner_left"), 
/* 823 */     INNER_RIGHT("inner_right"), 
/* 824 */     OUTER_LEFT("outer_left"), 
/* 825 */     OUTER_RIGHT("outer_right");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private EnumShape(String name)
/*     */     {
/* 831 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 836 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 841 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStairs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */