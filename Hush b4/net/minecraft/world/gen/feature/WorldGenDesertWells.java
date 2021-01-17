// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockSand;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateHelper field_175913_a;
    private final IBlockState field_175911_b;
    private final IBlockState field_175912_c;
    private final IBlockState field_175910_d;
    
    static {
        field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, (Predicate<? extends BlockSand.EnumType>)Predicates.equalTo((V)BlockSand.EnumType.SAND));
    }
    
    public WorldGenDesertWells() {
        this.field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.field_175912_c = Blocks.sandstone.getDefaultState();
        this.field_175910_d = Blocks.flowing_water.getDefaultState();
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (!WorldGenDesertWells.field_175913_a.apply(worldIn.getBlockState(position))) {
            return false;
        }
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j))) {
                    return false;
                }
            }
        }
        for (int l = -1; l <= 0; ++l) {
            for (int l2 = -2; l2 <= 2; ++l2) {
                for (int k = -2; k <= 2; ++k) {
                    worldIn.setBlockState(position.add(l2, l, k), this.field_175912_c, 2);
                }
            }
        }
        worldIn.setBlockState(position, this.field_175910_d, 2);
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            worldIn.setBlockState(position.offset((EnumFacing)enumfacing), this.field_175910_d, 2);
        }
        for (int i2 = -2; i2 <= 2; ++i2) {
            for (int i3 = -2; i3 <= 2; ++i3) {
                if (i2 == -2 || i2 == 2 || i3 == -2 || i3 == 2) {
                    worldIn.setBlockState(position.add(i2, 1, i3), this.field_175912_c, 2);
                }
            }
        }
        worldIn.setBlockState(position.add(2, 1, 0), this.field_175911_b, 2);
        worldIn.setBlockState(position.add(-2, 1, 0), this.field_175911_b, 2);
        worldIn.setBlockState(position.add(0, 1, 2), this.field_175911_b, 2);
        worldIn.setBlockState(position.add(0, 1, -2), this.field_175911_b, 2);
        for (int j2 = -1; j2 <= 1; ++j2) {
            for (int j3 = -1; j3 <= 1; ++j3) {
                if (j2 == 0 && j3 == 0) {
                    worldIn.setBlockState(position.add(j2, 4, j3), this.field_175912_c, 2);
                }
                else {
                    worldIn.setBlockState(position.add(j2, 4, j3), this.field_175911_b, 2);
                }
            }
        }
        for (int k2 = 1; k2 <= 3; ++k2) {
            worldIn.setBlockState(position.add(-1, k2, -1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(-1, k2, 1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(1, k2, -1), this.field_175912_c, 2);
            worldIn.setBlockState(position.add(1, k2, 1), this.field_175912_c, 2);
        }
        return true;
    }
}
