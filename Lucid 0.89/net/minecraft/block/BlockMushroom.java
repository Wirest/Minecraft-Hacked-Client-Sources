package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

public class BlockMushroom extends BlockBush implements IGrowable
{

    protected BlockMushroom()
    {
        float var1 = 0.2F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(25) == 0)
        {
            int var5 = 5;
            Iterator var7 = BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4)).iterator();

            while (var7.hasNext())
            {
                BlockPos var8 = (BlockPos)var7.next();

                if (worldIn.getBlockState(var8).getBlock() == this)
                {
                    --var5;

                    if (var5 <= 0)
                    {
                        return;
                    }
                }
            }

            BlockPos var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for (int var10 = 0; var10 < 4; ++var10)
            {
                if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState()))
                {
                    pos = var9;
                }

                var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }

            if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState()))
            {
                worldIn.setBlockState(var9, this.getDefaultState(), 2);
            }
        }
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
    }

    /**
     * is the block grass, dirt or farmland
     */
    @Override
	protected boolean canPlaceBlockOn(Block ground)
    {
        return ground.isFullBlock();
    }

    @Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            IBlockState var4 = worldIn.getBlockState(pos.down());
            return var4.getBlock() == Blocks.mycelium ? true : (var4.getBlock() == Blocks.dirt && var4.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL ? true : worldIn.getLight(pos) < 13 && this.canPlaceBlockOn(var4.getBlock()));
        }
        else
        {
            return false;
        }
    }

    public boolean generateBigMushroom(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        worldIn.setBlockToAir(pos);
        WorldGenBigMushroom var5 = null;

        if (this == Blocks.brown_mushroom)
        {
            var5 = new WorldGenBigMushroom(0);
        }
        else if (this == Blocks.red_mushroom)
        {
            var5 = new WorldGenBigMushroom(1);
        }

        if (var5 != null && var5.generate(worldIn, rand, pos))
        {
            return true;
        }
        else
        {
            worldIn.setBlockState(pos, state, 3);
            return false;
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return rand.nextFloat() < 0.4D;
    }

    @Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.generateBigMushroom(worldIn, pos, state, rand);
    }
}
