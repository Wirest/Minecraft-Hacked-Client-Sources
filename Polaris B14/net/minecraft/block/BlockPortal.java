/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.BlockWorldState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockPattern.PatternHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.AxisDirection;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPortal extends BlockBreakable
/*     */ {
/*  26 */   public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z });
/*     */   
/*     */   public BlockPortal()
/*     */   {
/*  30 */     super(Material.portal, false);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
/*  32 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  37 */     super.updateTick(worldIn, pos, state, rand);
/*     */     
/*  39 */     if ((worldIn.provider.isSurfaceWorld()) && (worldIn.getGameRules().getBoolean("doMobSpawning")) && (rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()))
/*     */     {
/*  41 */       int i = pos.getY();
/*     */       
/*     */ 
/*  44 */       for (BlockPos blockpos = pos; (!World.doesBlockHaveSolidTopSurface(worldIn, blockpos)) && (blockpos.getY() > 0); blockpos = blockpos.down()) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  49 */       if ((i > 0) && (!worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube()))
/*     */       {
/*  51 */         Entity entity = net.minecraft.item.ItemMonsterPlacer.spawnCreature(worldIn, 57, blockpos.getX() + 0.5D, blockpos.getY() + 1.1D, blockpos.getZ() + 0.5D);
/*     */         
/*  53 */         if (entity != null)
/*     */         {
/*  55 */           entity.timeUntilPortal = entity.getPortalCooldown();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  63 */     return null;
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  68 */     EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)worldIn.getBlockState(pos).getValue(AXIS);
/*  69 */     float f = 0.125F;
/*  70 */     float f1 = 0.125F;
/*     */     
/*  72 */     if (enumfacing$axis == EnumFacing.Axis.X)
/*     */     {
/*  74 */       f = 0.5F;
/*     */     }
/*     */     
/*  77 */     if (enumfacing$axis == EnumFacing.Axis.Z)
/*     */     {
/*  79 */       f1 = 0.5F;
/*     */     }
/*     */     
/*  82 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
/*     */   }
/*     */   
/*     */   public static int getMetaForAxis(EnumFacing.Axis axis)
/*     */   {
/*  87 */     return axis == EnumFacing.Axis.Z ? 2 : axis == EnumFacing.Axis.X ? 1 : 0;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   public boolean func_176548_d(World worldIn, BlockPos p_176548_2_)
/*     */   {
/*  97 */     Size blockportal$size = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
/*     */     
/*  99 */     if ((blockportal$size.func_150860_b()) && (blockportal$size.field_150864_e == 0))
/*     */     {
/* 101 */       blockportal$size.func_150859_c();
/* 102 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 106 */     Size blockportal$size1 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
/*     */     
/* 108 */     if ((blockportal$size1.func_150860_b()) && (blockportal$size1.field_150864_e == 0))
/*     */     {
/* 110 */       blockportal$size1.func_150859_c();
/* 111 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 125 */     EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue(AXIS);
/*     */     
/* 127 */     if (enumfacing$axis == EnumFacing.Axis.X)
/*     */     {
/* 129 */       Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
/*     */       
/* 131 */       if ((!blockportal$size.func_150860_b()) || (blockportal$size.field_150864_e < blockportal$size.field_150868_h * blockportal$size.field_150862_g))
/*     */       {
/* 133 */         worldIn.setBlockState(pos, Blocks.air.getDefaultState());
/*     */       }
/*     */     }
/* 136 */     else if (enumfacing$axis == EnumFacing.Axis.Z)
/*     */     {
/* 138 */       Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
/*     */       
/* 140 */       if ((!blockportal$size1.func_150860_b()) || (blockportal$size1.field_150864_e < blockportal$size1.field_150868_h * blockportal$size1.field_150862_g))
/*     */       {
/* 142 */         worldIn.setBlockState(pos, Blocks.air.getDefaultState());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 149 */     EnumFacing.Axis enumfacing$axis = null;
/* 150 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 152 */     if (worldIn.getBlockState(pos).getBlock() == this)
/*     */     {
/* 154 */       enumfacing$axis = (EnumFacing.Axis)iblockstate.getValue(AXIS);
/*     */       
/* 156 */       if (enumfacing$axis == null)
/*     */       {
/* 158 */         return false;
/*     */       }
/*     */       
/* 161 */       if ((enumfacing$axis == EnumFacing.Axis.Z) && (side != EnumFacing.EAST) && (side != EnumFacing.WEST))
/*     */       {
/* 163 */         return false;
/*     */       }
/*     */       
/* 166 */       if ((enumfacing$axis == EnumFacing.Axis.X) && (side != EnumFacing.SOUTH) && (side != EnumFacing.NORTH))
/*     */       {
/* 168 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 172 */     boolean flag = (worldIn.getBlockState(pos.west()).getBlock() == this) && (worldIn.getBlockState(pos.west(2)).getBlock() != this);
/* 173 */     boolean flag1 = (worldIn.getBlockState(pos.east()).getBlock() == this) && (worldIn.getBlockState(pos.east(2)).getBlock() != this);
/* 174 */     boolean flag2 = (worldIn.getBlockState(pos.north()).getBlock() == this) && (worldIn.getBlockState(pos.north(2)).getBlock() != this);
/* 175 */     boolean flag3 = (worldIn.getBlockState(pos.south()).getBlock() == this) && (worldIn.getBlockState(pos.south(2)).getBlock() != this);
/* 176 */     boolean flag4 = (flag) || (flag1) || (enumfacing$axis == EnumFacing.Axis.X);
/* 177 */     boolean flag5 = (flag2) || (flag3) || (enumfacing$axis == EnumFacing.Axis.Z);
/* 178 */     return (flag4) && (side == EnumFacing.WEST);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 186 */     return 0;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 191 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/* 199 */     if ((entityIn.ridingEntity == null) && (entityIn.riddenByEntity == null))
/*     */     {
/* 201 */       entityIn.func_181015_d(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 207 */     if (rand.nextInt(100) == 0)
/*     */     {
/* 209 */       worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "portal.portal", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
/*     */     }
/*     */     
/* 212 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 214 */       double d0 = pos.getX() + rand.nextFloat();
/* 215 */       double d1 = pos.getY() + rand.nextFloat();
/* 216 */       double d2 = pos.getZ() + rand.nextFloat();
/* 217 */       double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 218 */       double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 219 */       double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
/* 220 */       int j = rand.nextInt(2) * 2 - 1;
/*     */       
/* 222 */       if ((worldIn.getBlockState(pos.west()).getBlock() != this) && (worldIn.getBlockState(pos.east()).getBlock() != this))
/*     */       {
/* 224 */         d0 = pos.getX() + 0.5D + 0.25D * j;
/* 225 */         d3 = rand.nextFloat() * 2.0F * j;
/*     */       }
/*     */       else
/*     */       {
/* 229 */         d2 = pos.getZ() + 0.5D + 0.25D * j;
/* 230 */         d5 = rand.nextFloat() * 2.0F * j;
/*     */       }
/*     */       
/* 233 */       worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 239 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 247 */     return getDefaultState().withProperty(AXIS, (meta & 0x3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 255 */     return getMetaForAxis((EnumFacing.Axis)state.getValue(AXIS));
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 260 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { AXIS });
/*     */   }
/*     */   
/*     */   public BlockPattern.PatternHelper func_181089_f(World p_181089_1_, BlockPos p_181089_2_)
/*     */   {
/* 265 */     EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
/* 266 */     Size blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.X);
/* 267 */     LoadingCache<BlockPos, BlockWorldState> loadingcache = net.minecraft.block.state.pattern.BlockPattern.func_181627_a(p_181089_1_, true);
/*     */     
/* 269 */     if (!blockportal$size.func_150860_b())
/*     */     {
/* 271 */       enumfacing$axis = EnumFacing.Axis.X;
/* 272 */       blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.Z);
/*     */     }
/*     */     
/* 275 */     if (!blockportal$size.func_150860_b())
/*     */     {
/* 277 */       return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
/*     */     }
/*     */     
/*     */ 
/* 281 */     int[] aint = new int[EnumFacing.AxisDirection.values().length];
/* 282 */     EnumFacing enumfacing = blockportal$size.field_150866_c.rotateYCCW();
/* 283 */     BlockPos blockpos = blockportal$size.field_150861_f.up(blockportal$size.func_181100_a() - 1);
/*     */     EnumFacing.AxisDirection[] arrayOfAxisDirection;
/* 285 */     int j = (arrayOfAxisDirection = EnumFacing.AxisDirection.values()).length; BlockPattern.PatternHelper blockpattern$patternhelper; for (int i = 0; i < j; i++) { EnumFacing.AxisDirection enumfacing$axisdirection = arrayOfAxisDirection[i];
/*     */       
/* 287 */       blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.func_181076_a(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
/*     */       
/* 289 */       for (int i = 0; i < blockportal$size.func_181101_b(); i++)
/*     */       {
/* 291 */         for (int j = 0; j < blockportal$size.func_181100_a(); j++)
/*     */         {
/* 293 */           BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
/*     */           
/* 295 */           if ((blockworldstate.getBlockState() != null) && (blockworldstate.getBlockState().getBlock().getMaterial() != Material.air))
/*     */           {
/* 297 */             aint[enumfacing$axisdirection.ordinal()] += 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 303 */     EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
/*     */     
/* 305 */     int k = (blockpattern$patternhelper = EnumFacing.AxisDirection.values()).length; for (j = 0; j < k; j++) { EnumFacing.AxisDirection enumfacing$axisdirection2 = blockpattern$patternhelper[j];
/*     */       
/* 307 */       if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()])
/*     */       {
/* 309 */         enumfacing$axisdirection1 = enumfacing$axisdirection2;
/*     */       }
/*     */     }
/*     */     
/* 313 */     return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.func_181076_a(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Size
/*     */   {
/*     */     private final World world;
/*     */     private final EnumFacing.Axis axis;
/*     */     private final EnumFacing field_150866_c;
/*     */     private final EnumFacing field_150863_d;
/* 323 */     private int field_150864_e = 0;
/*     */     private BlockPos field_150861_f;
/*     */     private int field_150862_g;
/*     */     private int field_150868_h;
/*     */     
/*     */     public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_)
/*     */     {
/* 330 */       this.world = worldIn;
/* 331 */       this.axis = p_i45694_3_;
/*     */       
/* 333 */       if (p_i45694_3_ == EnumFacing.Axis.X)
/*     */       {
/* 335 */         this.field_150863_d = EnumFacing.EAST;
/* 336 */         this.field_150866_c = EnumFacing.WEST;
/*     */       }
/*     */       else
/*     */       {
/* 340 */         this.field_150863_d = EnumFacing.NORTH;
/* 341 */         this.field_150866_c = EnumFacing.SOUTH;
/*     */       }
/*     */       
/* 344 */       for (BlockPos blockpos = p_i45694_2_; (p_i45694_2_.getY() > blockpos.getY() - 21) && (p_i45694_2_.getY() > 0) && (func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock())); p_i45694_2_ = p_i45694_2_.down()) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 349 */       int i = func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
/*     */       
/* 351 */       if (i >= 0)
/*     */       {
/* 353 */         this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, i);
/* 354 */         this.field_150868_h = func_180120_a(this.field_150861_f, this.field_150866_c);
/*     */         
/* 356 */         if ((this.field_150868_h < 2) || (this.field_150868_h > 21))
/*     */         {
/* 358 */           this.field_150861_f = null;
/* 359 */           this.field_150868_h = 0;
/*     */         }
/*     */       }
/*     */       
/* 363 */       if (this.field_150861_f != null)
/*     */       {
/* 365 */         this.field_150862_g = func_150858_a();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_)
/*     */     {
/* 373 */       for (int i = 0; i < 22; i++)
/*     */       {
/* 375 */         BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
/*     */         
/* 377 */         if ((!func_150857_a(this.world.getBlockState(blockpos).getBlock())) || (this.world.getBlockState(blockpos.down()).getBlock() != Blocks.obsidian)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 383 */       Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
/* 384 */       return block == Blocks.obsidian ? i : 0;
/*     */     }
/*     */     
/*     */     public int func_181100_a()
/*     */     {
/* 389 */       return this.field_150862_g;
/*     */     }
/*     */     
/*     */     public int func_181101_b()
/*     */     {
/* 394 */       return this.field_150868_h;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected int func_150858_a()
/*     */     {
/* 401 */       for (this.field_150862_g = 0; this.field_150862_g < 21; this.field_150862_g += 1)
/*     */       {
/* 403 */         for (int i = 0; i < this.field_150868_h; i++)
/*     */         {
/* 405 */           BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
/* 406 */           Block block = this.world.getBlockState(blockpos).getBlock();
/*     */           
/* 408 */           if (!func_150857_a(block)) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 413 */           if (block == Blocks.portal)
/*     */           {
/* 415 */             this.field_150864_e += 1;
/*     */           }
/*     */           
/* 418 */           if (i == 0)
/*     */           {
/* 420 */             block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock();
/*     */             
/* 422 */             if (block != Blocks.obsidian) {
/*     */               break;
/*     */             }
/*     */             
/*     */           }
/* 427 */           else if (i == this.field_150868_h - 1)
/*     */           {
/* 429 */             block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock();
/*     */             
/* 431 */             if (block != Blocks.obsidian) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 439 */       for (int j = 0; j < this.field_150868_h; j++)
/*     */       {
/* 441 */         if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g)).getBlock() != Blocks.obsidian)
/*     */         {
/* 443 */           this.field_150862_g = 0;
/* 444 */           break;
/*     */         }
/*     */       }
/*     */       
/* 448 */       if ((this.field_150862_g <= 21) && (this.field_150862_g >= 3))
/*     */       {
/* 450 */         return this.field_150862_g;
/*     */       }
/*     */       
/*     */ 
/* 454 */       this.field_150861_f = null;
/* 455 */       this.field_150868_h = 0;
/* 456 */       this.field_150862_g = 0;
/* 457 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */     protected boolean func_150857_a(Block p_150857_1_)
/*     */     {
/* 463 */       return (p_150857_1_.blockMaterial == Material.air) || (p_150857_1_ == Blocks.fire) || (p_150857_1_ == Blocks.portal);
/*     */     }
/*     */     
/*     */     public boolean func_150860_b()
/*     */     {
/* 468 */       return (this.field_150861_f != null) && (this.field_150868_h >= 2) && (this.field_150868_h <= 21) && (this.field_150862_g >= 3) && (this.field_150862_g <= 21);
/*     */     }
/*     */     
/*     */     public void func_150859_c()
/*     */     {
/* 473 */       for (int i = 0; i < this.field_150868_h; i++)
/*     */       {
/* 475 */         BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);
/*     */         
/* 477 */         for (int j = 0; j < this.field_150862_g; j++)
/*     */         {
/* 479 */           this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */