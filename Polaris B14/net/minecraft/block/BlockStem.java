/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStem extends BlockBush implements IGrowable
/*     */ {
/*  24 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
/*  25 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
/*     */   {
/*     */     public boolean apply(EnumFacing p_apply_1_)
/*     */     {
/*  29 */       return p_apply_1_ != EnumFacing.DOWN;
/*     */     }
/*  25 */   });
/*     */   
/*     */ 
/*     */ 
/*     */   private final Block crop;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BlockStem(Block crop)
/*     */   {
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(FACING, EnumFacing.UP));
/*  37 */     this.crop = crop;
/*  38 */     setTickRandomly(true);
/*  39 */     float f = 0.125F;
/*  40 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*  41 */     setCreativeTab(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  50 */     state = state.withProperty(FACING, EnumFacing.UP);
/*     */     
/*  52 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  54 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/*  56 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop)
/*     */       {
/*  58 */         state = state.withProperty(FACING, enumfacing);
/*  59 */         break;
/*     */       }
/*     */     }
/*     */     
/*  63 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canPlaceBlockOn(Block ground)
/*     */   {
/*  71 */     return ground == Blocks.farmland;
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  76 */     super.updateTick(worldIn, pos, state, rand);
/*     */     
/*  78 */     if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
/*     */     {
/*  80 */       float f = BlockCrops.getGrowthChance(this, worldIn, pos);
/*     */       
/*  82 */       if (rand.nextInt((int)(25.0F / f) + 1) == 0)
/*     */       {
/*  84 */         int i = ((Integer)state.getValue(AGE)).intValue();
/*     */         
/*  86 */         if (i < 7)
/*     */         {
/*  88 */           state = state.withProperty(AGE, Integer.valueOf(i + 1));
/*  89 */           worldIn.setBlockState(pos, state, 2);
/*     */         }
/*     */         else
/*     */         {
/*  93 */           for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */           {
/*  95 */             EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */             
/*  97 */             if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop)
/*     */             {
/*  99 */               return;
/*     */             }
/*     */           }
/*     */           
/* 103 */           pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
/* 104 */           Block block = worldIn.getBlockState(pos.down()).getBlock();
/*     */           
/* 106 */           if ((worldIn.getBlockState(pos).getBlock().blockMaterial == Material.air) && ((block == Blocks.farmland) || (block == Blocks.dirt) || (block == Blocks.grass)))
/*     */           {
/* 108 */             worldIn.setBlockState(pos, this.crop.getDefaultState());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void growStem(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 117 */     int i = ((Integer)state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/* 118 */     worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(Math.min(7, i))), 2);
/*     */   }
/*     */   
/*     */   public int getRenderColor(IBlockState state)
/*     */   {
/* 123 */     if (state.getBlock() != this)
/*     */     {
/* 125 */       return super.getRenderColor(state);
/*     */     }
/*     */     
/*     */ 
/* 129 */     int i = ((Integer)state.getValue(AGE)).intValue();
/* 130 */     int j = i * 32;
/* 131 */     int k = 255 - i * 8;
/* 132 */     int l = i * 4;
/* 133 */     return j << 16 | k << 8 | l;
/*     */   }
/*     */   
/*     */ 
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/* 139 */     return getRenderColor(worldIn.getBlockState(pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/* 147 */     float f = 0.125F;
/* 148 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 153 */     this.maxY = ((((Integer)worldIn.getBlockState(pos).getValue(AGE)).intValue() * 2 + 2) / 16.0F);
/* 154 */     float f = 0.125F;
/* 155 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (float)this.maxY, 0.5F + f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 163 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     
/* 165 */     if (!worldIn.isRemote)
/*     */     {
/* 167 */       Item item = getSeedItem();
/*     */       
/* 169 */       if (item != null)
/*     */       {
/* 171 */         int i = ((Integer)state.getValue(AGE)).intValue();
/*     */         
/* 173 */         for (int j = 0; j < 3; j++)
/*     */         {
/* 175 */           if (worldIn.rand.nextInt(15) <= i)
/*     */           {
/* 177 */             spawnAsEntity(worldIn, pos, new ItemStack(item));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Item getSeedItem()
/*     */   {
/* 186 */     return this.crop == Blocks.melon_block ? Items.melon_seeds : this.crop == Blocks.pumpkin ? Items.pumpkin_seeds : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 194 */     return null;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 199 */     Item item = getSeedItem();
/* 200 */     return item != null ? item : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/* 208 */     return ((Integer)state.getValue(AGE)).intValue() != 7;
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 218 */     growStem(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 226 */     return getDefaultState().withProperty(AGE, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 234 */     return ((Integer)state.getValue(AGE)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 239 */     return new BlockState(this, new IProperty[] { AGE, FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */