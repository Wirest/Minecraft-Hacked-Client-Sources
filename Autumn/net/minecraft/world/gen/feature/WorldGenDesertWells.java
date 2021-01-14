package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator {
   private static final BlockStateHelper field_175913_a;
   private final IBlockState field_175911_b;
   private final IBlockState field_175912_c;
   private final IBlockState field_175910_d;

   public WorldGenDesertWells() {
      this.field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      this.field_175912_c = Blocks.sandstone.getDefaultState();
      this.field_175910_d = Blocks.flowing_water.getDefaultState();
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      while(worldIn.isAirBlock(position) && position.getY() > 2) {
         position = position.down();
      }

      if (!field_175913_a.apply(worldIn.getBlockState(position))) {
         return false;
      } else {
         int j1;
         int j2;
         for(j1 = -2; j1 <= 2; ++j1) {
            for(j2 = -2; j2 <= 2; ++j2) {
               if (worldIn.isAirBlock(position.add(j1, -1, j2)) && worldIn.isAirBlock(position.add(j1, -2, j2))) {
                  return false;
               }
            }
         }

         for(j1 = -1; j1 <= 0; ++j1) {
            for(j2 = -2; j2 <= 2; ++j2) {
               for(int k = -2; k <= 2; ++k) {
                  worldIn.setBlockState(position.add(j2, j1, k), this.field_175912_c, 2);
               }
            }
         }

         worldIn.setBlockState(position, this.field_175910_d, 2);
         Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var7.hasNext()) {
            Object enumfacing0 = var7.next();
            EnumFacing enumfacing = (EnumFacing)enumfacing0;
            worldIn.setBlockState(position.offset(enumfacing), this.field_175910_d, 2);
         }

         for(j1 = -2; j1 <= 2; ++j1) {
            for(j2 = -2; j2 <= 2; ++j2) {
               if (j1 == -2 || j1 == 2 || j2 == -2 || j2 == 2) {
                  worldIn.setBlockState(position.add(j1, 1, j2), this.field_175912_c, 2);
               }
            }
         }

         worldIn.setBlockState(position.add(2, 1, 0), this.field_175911_b, 2);
         worldIn.setBlockState(position.add(-2, 1, 0), this.field_175911_b, 2);
         worldIn.setBlockState(position.add(0, 1, 2), this.field_175911_b, 2);
         worldIn.setBlockState(position.add(0, 1, -2), this.field_175911_b, 2);

         for(j1 = -1; j1 <= 1; ++j1) {
            for(j2 = -1; j2 <= 1; ++j2) {
               if (j1 == 0 && j2 == 0) {
                  worldIn.setBlockState(position.add(j1, 4, j2), this.field_175912_c, 2);
               } else {
                  worldIn.setBlockState(position.add(j1, 4, j2), this.field_175911_b, 2);
               }
            }
         }

         for(j1 = 1; j1 <= 3; ++j1) {
            worldIn.setBlockState(position.add(-1, j1, -1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(-1, j1, 1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(1, j1, -1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(1, j1, 1), this.field_175912_c, 2);
         }

         return true;
      }
   }

   static {
      field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
   }
}
