/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.ColorizerGrass;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class BlockGrass extends Block implements IGrowable
/*     */ {
/*  21 */   public static final PropertyBool SNOWY = PropertyBool.create("snowy");
/*     */   
/*     */   protected BlockGrass()
/*     */   {
/*  25 */     super(Material.grass);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
/*  27 */     setTickRandomly(true);
/*  28 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  37 */     Block block = worldIn.getBlockState(pos.up()).getBlock();
/*  38 */     return state.withProperty(SNOWY, Boolean.valueOf((block == Blocks.snow) || (block == Blocks.snow_layer)));
/*     */   }
/*     */   
/*     */   public int getBlockColor()
/*     */   {
/*  43 */     return ColorizerGrass.getGrassColor(0.5D, 1.0D);
/*     */   }
/*     */   
/*     */   public int getRenderColor(IBlockState state)
/*     */   {
/*  48 */     return getBlockColor();
/*     */   }
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/*  53 */     return net.minecraft.world.biome.BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  58 */     if (!worldIn.isRemote)
/*     */     {
/*  60 */       if ((worldIn.getLightFromNeighbors(pos.up()) < 4) && (worldIn.getBlockState(pos.up()).getBlock().getLightOpacity() > 2))
/*     */       {
/*  62 */         worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
/*     */ 
/*     */ 
/*     */       }
/*  66 */       else if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
/*     */       {
/*  68 */         for (int i = 0; i < 4; i++)
/*     */         {
/*  70 */           BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
/*  71 */           Block block = worldIn.getBlockState(blockpos.up()).getBlock();
/*  72 */           IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */           
/*  74 */           if ((iblockstate.getBlock() == Blocks.dirt) && (iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) && (worldIn.getLightFromNeighbors(blockpos.up()) >= 4) && (block.getLightOpacity() <= 2))
/*     */           {
/*  76 */             worldIn.setBlockState(blockpos, Blocks.grass.getDefaultState());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  89 */     return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 107 */     BlockPos blockpos = pos.up();
/*     */     
/* 109 */     for (int i = 0; i < 128; i++)
/*     */     {
/* 111 */       BlockPos blockpos1 = blockpos;
/* 112 */       int j = 0;
/*     */       
/*     */       for (;;)
/*     */       {
/* 116 */         if (j >= i / 16)
/*     */         {
/* 118 */           if (worldIn.getBlockState(blockpos1).getBlock().blockMaterial != Material.air)
/*     */             break;
/* 120 */           if (rand.nextInt(8) == 0)
/*     */           {
/* 122 */             BlockFlower.EnumFlowerType blockflower$enumflowertype = worldIn.getBiomeGenForCoords(blockpos1).pickRandomFlower(rand, blockpos1);
/* 123 */             BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
/* 124 */             IBlockState iblockstate = blockflower.getDefaultState().withProperty(blockflower.getTypeProperty(), blockflower$enumflowertype);
/*     */             
/* 126 */             if (!blockflower.canBlockStay(worldIn, blockpos1, iblockstate))
/*     */               break;
/* 128 */             worldIn.setBlockState(blockpos1, iblockstate, 3);
/*     */             
/* 130 */             break;
/*     */           }
/*     */           
/* 133 */           IBlockState iblockstate1 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
/*     */           
/* 135 */           if (!Blocks.tallgrass.canBlockStay(worldIn, blockpos1, iblockstate1))
/*     */             break;
/* 137 */           worldIn.setBlockState(blockpos1, iblockstate1, 3);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 142 */           break;
/*     */         }
/*     */         
/* 145 */         blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
/*     */         
/* 147 */         if ((worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.grass) || (worldIn.getBlockState(blockpos1).getBlock().isNormalCube())) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 152 */         j++;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 159 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 167 */     return 0;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 172 */     return new BlockState(this, new IProperty[] { SNOWY });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */