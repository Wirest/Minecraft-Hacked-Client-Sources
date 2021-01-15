/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.BlockWorldState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.block.state.pattern.BlockPattern.PatternHelper;
/*     */ import net.minecraft.block.state.pattern.BlockStateHelper;
/*     */ import net.minecraft.block.state.pattern.FactoryBlockPattern;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntitySnowman;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPumpkin extends BlockDirectional
/*     */ {
/*     */   private BlockPattern snowmanBasePattern;
/*     */   private BlockPattern snowmanPattern;
/*     */   private BlockPattern golemBasePattern;
/*     */   private BlockPattern golemPattern;
/*  29 */   private static final Predicate<IBlockState> field_181085_Q = new Predicate()
/*     */   {
/*     */     public boolean apply(IBlockState p_apply_1_)
/*     */     {
/*  33 */       return (p_apply_1_ != null) && ((p_apply_1_.getBlock() == Blocks.pumpkin) || (p_apply_1_.getBlock() == Blocks.lit_pumpkin));
/*     */     }
/*     */   };
/*     */   
/*     */   protected BlockPumpkin()
/*     */   {
/*  39 */     super(Material.gourd, MapColor.adobeColor);
/*  40 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*  41 */     setTickRandomly(true);
/*  42 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  47 */     super.onBlockAdded(worldIn, pos, state);
/*  48 */     trySpawnGolem(worldIn, pos);
/*     */   }
/*     */   
/*     */   public boolean canDispenserPlace(World worldIn, BlockPos pos)
/*     */   {
/*  53 */     return (getSnowmanBasePattern().match(worldIn, pos) != null) || (getGolemBasePattern().match(worldIn, pos) != null);
/*     */   }
/*     */   
/*     */ 
/*     */   private void trySpawnGolem(World worldIn, BlockPos pos)
/*     */   {
/*     */     BlockPattern.PatternHelper blockpattern$patternhelper;
/*  60 */     if ((blockpattern$patternhelper = getSnowmanPattern().match(worldIn, pos)) != null)
/*     */     {
/*  62 */       for (int i = 0; i < getSnowmanPattern().getThumbLength(); i++)
/*     */       {
/*  64 */         BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
/*  65 */         worldIn.setBlockState(blockworldstate.getPos(), Blocks.air.getDefaultState(), 2);
/*     */       }
/*     */       
/*  68 */       EntitySnowman entitysnowman = new EntitySnowman(worldIn);
/*  69 */       BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
/*  70 */       entitysnowman.setLocationAndAngles(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.05D, blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
/*  71 */       worldIn.spawnEntityInWorld(entitysnowman);
/*     */       
/*  73 */       for (int j = 0; j < 120; j++)
/*     */       {
/*  75 */         worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, blockpos1.getX() + worldIn.rand.nextDouble(), blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       
/*  78 */       for (int i1 = 0; i1 < getSnowmanPattern().getThumbLength(); i1++)
/*     */       {
/*  80 */         BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(0, i1, 0);
/*  81 */         worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.air);
/*     */       }
/*     */     }
/*  84 */     else if ((blockpattern$patternhelper = getGolemPattern().match(worldIn, pos)) != null)
/*     */     {
/*  86 */       for (int k = 0; k < getGolemPattern().getPalmLength(); k++)
/*     */       {
/*  88 */         for (int l = 0; l < getGolemPattern().getThumbLength(); l++)
/*     */         {
/*  90 */           worldIn.setBlockState(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(), Blocks.air.getDefaultState(), 2);
/*     */         }
/*     */       }
/*     */       
/*  94 */       BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
/*  95 */       EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
/*  96 */       entityirongolem.setPlayerCreated(true);
/*  97 */       entityirongolem.setLocationAndAngles(blockpos.getX() + 0.5D, blockpos.getY() + 0.05D, blockpos.getZ() + 0.5D, 0.0F, 0.0F);
/*  98 */       worldIn.spawnEntityInWorld(entityirongolem);
/*     */       
/* 100 */       for (int j1 = 0; j1 < 120; j1++)
/*     */       {
/* 102 */         worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos.getX() + worldIn.rand.nextDouble(), blockpos.getY() + worldIn.rand.nextDouble() * 3.9D, blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       
/* 105 */       for (int k1 = 0; k1 < getGolemPattern().getPalmLength(); k1++)
/*     */       {
/* 107 */         for (int l1 = 0; l1 < getGolemPattern().getThumbLength(); l1++)
/*     */         {
/* 109 */           BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
/* 110 */           worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 118 */     return (worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable()) && (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 127 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 135 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 143 */     return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 148 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING });
/*     */   }
/*     */   
/*     */   protected BlockPattern getSnowmanBasePattern()
/*     */   {
/* 153 */     if (this.snowmanBasePattern == null)
/*     */     {
/* 155 */       this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[] { " ", "#", "#" }).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
/*     */     }
/*     */     
/* 158 */     return this.snowmanBasePattern;
/*     */   }
/*     */   
/*     */   protected BlockPattern getSnowmanPattern()
/*     */   {
/* 163 */     if (this.snowmanPattern == null)
/*     */     {
/* 165 */       this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[] { "^", "#", "#" }).where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
/*     */     }
/*     */     
/* 168 */     return this.snowmanPattern;
/*     */   }
/*     */   
/*     */   protected BlockPattern getGolemBasePattern()
/*     */   {
/* 173 */     if (this.golemBasePattern == null)
/*     */     {
/* 175 */       this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[] { "~ ~", "###", "~#~" }).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
/*     */     }
/*     */     
/* 178 */     return this.golemBasePattern;
/*     */   }
/*     */   
/*     */   protected BlockPattern getGolemPattern()
/*     */   {
/* 183 */     if (this.golemPattern == null)
/*     */     {
/* 185 */       this.golemPattern = FactoryBlockPattern.start().aisle(new String[] { "~^~", "###", "~#~" }).where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
/*     */     }
/*     */     
/* 188 */     return this.golemPattern;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPumpkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */