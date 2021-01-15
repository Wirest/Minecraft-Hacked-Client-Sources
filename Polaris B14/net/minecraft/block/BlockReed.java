/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class BlockReed extends Block
/*     */ {
/*  21 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
/*     */   
/*     */   protected BlockReed()
/*     */   {
/*  25 */     super(Material.plants);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
/*  27 */     float f = 0.375F;
/*  28 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
/*  29 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  34 */     if ((worldIn.getBlockState(pos.down()).getBlock() == Blocks.reeds) || (checkForDrop(worldIn, pos, state)))
/*     */     {
/*  36 */       if (worldIn.isAirBlock(pos.up()))
/*     */       {
/*     */ 
/*     */ 
/*  40 */         for (int i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; i++) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  45 */         if (i < 3)
/*     */         {
/*  47 */           int j = ((Integer)state.getValue(AGE)).intValue();
/*     */           
/*  49 */           if (j == 15)
/*     */           {
/*  51 */             worldIn.setBlockState(pos.up(), getDefaultState());
/*  52 */             worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
/*     */           }
/*     */           else
/*     */           {
/*  56 */             worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  65 */     Block block = worldIn.getBlockState(pos.down()).getBlock();
/*     */     
/*  67 */     if (block == this)
/*     */     {
/*  69 */       return true;
/*     */     }
/*  71 */     if ((block != Blocks.grass) && (block != Blocks.dirt) && (block != Blocks.sand))
/*     */     {
/*  73 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  77 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  79 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/*  81 */       if (worldIn.getBlockState(pos.offset(enumfacing).down()).getBlock().getMaterial() == Material.water)
/*     */       {
/*  83 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  87 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  96 */     checkForDrop(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 101 */     if (canBlockStay(worldIn, pos))
/*     */     {
/* 103 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 107 */     dropBlockAsItem(worldIn, pos, state, 0);
/* 108 */     worldIn.setBlockToAir(pos);
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos)
/*     */   {
/* 115 */     return canPlaceBlockAt(worldIn, pos);
/*     */   }
/*     */   
/*     */   public net.minecraft.util.AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 128 */     return Items.reeds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 146 */     return Items.reeds;
/*     */   }
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/* 151 */     return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 156 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 164 */     return getDefaultState().withProperty(AGE, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 172 */     return ((Integer)state.getValue(AGE)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 177 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockReed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */