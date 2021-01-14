package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

public class BlockMushroom extends BlockBush implements IGrowable {
    private static final String __OBFID = "CL_00000272";

    protected BlockMushroom() {
        float var1 = 0.2F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (rand.nextInt(25) == 0) {
            int var5 = 5;
            boolean var6 = true;
            Iterator var7 = BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4)).iterator();

            while (var7.hasNext()) {
                BlockPos var8 = (BlockPos) var7.next();

                if (worldIn.getBlockState(var8).getBlock() == this) {
                    --var5;

                    if (var5 <= 0) {
                        return;
                    }
                }
            }

            BlockPos var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for (int var10 = 0; var10 < 4; ++var10) {
                if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState())) {
                    pos = var9;
                }

                var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }

            if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState())) {
                worldIn.setBlockState(var9, this.getDefaultState(), 2);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block ground) {
        return ground.isFullBlock();
    }

    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        if (p_180671_2_.getY() >= 0 && p_180671_2_.getY() < 256) {
            IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetDown());
            return var4.getBlock() == Blocks.mycelium ? true : (var4.getBlock() == Blocks.dirt && var4.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL ? true : worldIn.getLight(p_180671_2_) < 13 && this.canPlaceBlockOn(var4.getBlock()));
        } else {
            return false;
        }
    }

    public boolean func_176485_d(World worldIn, BlockPos p_176485_2_, IBlockState p_176485_3_, Random p_176485_4_) {
        worldIn.setBlockToAir(p_176485_2_);
        WorldGenBigMushroom var5 = null;

        if (this == Blocks.brown_mushroom) {
            var5 = new WorldGenBigMushroom(0);
        } else if (this == Blocks.red_mushroom) {
            var5 = new WorldGenBigMushroom(1);
        }

        if (var5 != null && var5.generate(worldIn, p_176485_4_, p_176485_2_)) {
            return true;
        } else {
            worldIn.setBlockState(p_176485_2_, p_176485_3_, 3);
            return false;
        }
    }

    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return (double) p_180670_2_.nextFloat() < 0.4D;
    }

    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        this.func_176485_d(worldIn, p_176474_3_, p_176474_4_, p_176474_2_);
    }
}
