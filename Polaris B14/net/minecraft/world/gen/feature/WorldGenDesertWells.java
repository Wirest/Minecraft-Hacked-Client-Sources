/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockDynamicLiquid;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockStateHelper;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenDesertWells extends WorldGenerator
/*     */ {
/*  17 */   private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(net.minecraft.block.BlockSand.VARIANT, Predicates.equalTo(net.minecraft.block.BlockSand.EnumType.SAND));
/*  18 */   private final IBlockState field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(net.minecraft.block.BlockStoneSlab.VARIANT, net.minecraft.block.BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, net.minecraft.block.BlockSlab.EnumBlockHalf.BOTTOM);
/*  19 */   private final IBlockState field_175912_c = Blocks.sandstone.getDefaultState();
/*  20 */   private final IBlockState field_175910_d = Blocks.flowing_water.getDefaultState();
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  24 */     while ((worldIn.isAirBlock(position)) && (position.getY() > 2))
/*     */     {
/*  26 */       position = position.down();
/*     */     }
/*     */     
/*  29 */     if (!field_175913_a.apply(worldIn.getBlockState(position)))
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  35 */     for (int i = -2; i <= 2; i++)
/*     */     {
/*  37 */       for (int j = -2; j <= 2; j++)
/*     */       {
/*  39 */         if ((worldIn.isAirBlock(position.add(i, -1, j))) && (worldIn.isAirBlock(position.add(i, -2, j))))
/*     */         {
/*  41 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     int l1;
/*  46 */     for (int l = -1; l <= 0; l++)
/*     */     {
/*  48 */       for (l1 = -2; l1 <= 2; l1++)
/*     */       {
/*  50 */         for (int k = -2; k <= 2; k++)
/*     */         {
/*  52 */           worldIn.setBlockState(position.add(l1, l, k), this.field_175912_c, 2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  57 */     worldIn.setBlockState(position, this.field_175910_d, 2);
/*     */     
/*  59 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  61 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*  62 */       worldIn.setBlockState(position.offset(enumfacing), this.field_175910_d, 2);
/*     */     }
/*     */     
/*  65 */     for (int i1 = -2; i1 <= 2; i1++)
/*     */     {
/*  67 */       for (int i2 = -2; i2 <= 2; i2++)
/*     */       {
/*  69 */         if ((i1 == -2) || (i1 == 2) || (i2 == -2) || (i2 == 2))
/*     */         {
/*  71 */           worldIn.setBlockState(position.add(i1, 1, i2), this.field_175912_c, 2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  76 */     worldIn.setBlockState(position.add(2, 1, 0), this.field_175911_b, 2);
/*  77 */     worldIn.setBlockState(position.add(-2, 1, 0), this.field_175911_b, 2);
/*  78 */     worldIn.setBlockState(position.add(0, 1, 2), this.field_175911_b, 2);
/*  79 */     worldIn.setBlockState(position.add(0, 1, -2), this.field_175911_b, 2);
/*     */     
/*  81 */     for (int j1 = -1; j1 <= 1; j1++)
/*     */     {
/*  83 */       for (int j2 = -1; j2 <= 1; j2++)
/*     */       {
/*  85 */         if ((j1 == 0) && (j2 == 0))
/*     */         {
/*  87 */           worldIn.setBlockState(position.add(j1, 4, j2), this.field_175912_c, 2);
/*     */         }
/*     */         else
/*     */         {
/*  91 */           worldIn.setBlockState(position.add(j1, 4, j2), this.field_175911_b, 2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  96 */     for (int k1 = 1; k1 <= 3; k1++)
/*     */     {
/*  98 */       worldIn.setBlockState(position.add(-1, k1, -1), this.field_175912_c, 2);
/*  99 */       worldIn.setBlockState(position.add(-1, k1, 1), this.field_175912_c, 2);
/* 100 */       worldIn.setBlockState(position.add(1, k1, -1), this.field_175912_c, 2);
/* 101 */       worldIn.setBlockState(position.add(1, k1, 1), this.field_175912_c, 2);
/*     */     }
/*     */     
/* 104 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenDesertWells.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */