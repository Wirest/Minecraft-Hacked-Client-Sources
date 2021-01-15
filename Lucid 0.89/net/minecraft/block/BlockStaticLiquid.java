package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockStaticLiquid extends BlockLiquid
{

    protected BlockStaticLiquid(Material materialIn)
    {
        super(materialIn);
        this.setTickRandomly(false);

        if (materialIn == Material.lava)
        {
            this.setTickRandomly(true);
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!this.checkForMixing(worldIn, pos, state))
        {
            this.updateLiquid(worldIn, pos, state);
        }
    }

    private void updateLiquid(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockDynamicLiquid var4 = getFlowingBlock(this.blockMaterial);
        worldIn.setBlockState(pos, var4.getDefaultState().withProperty(LEVEL, state.getValue(LEVEL)), 2);
        worldIn.scheduleUpdate(pos, var4, this.tickRate(worldIn));
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.blockMaterial == Material.lava)
        {
            if (worldIn.getGameRules().getGameRuleBooleanValue("doFireTick"))
            {
                int var5 = rand.nextInt(3);

                if (var5 > 0)
                {
                    BlockPos var6 = pos;

                    for (int var7 = 0; var7 < var5; ++var7)
                    {
                        var6 = var6.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
                        Block var8 = worldIn.getBlockState(var6).getBlock();

                        if (var8.blockMaterial == Material.air)
                        {
                            if (this.isSurroundingBlockFlammable(worldIn, var6))
                            {
                                worldIn.setBlockState(var6, Blocks.fire.getDefaultState());
                                return;
                            }
                        }
                        else if (var8.blockMaterial.blocksMovement())
                        {
                            return;
                        }
                    }
                }
                else
                {
                    for (int var9 = 0; var9 < 3; ++var9)
                    {
                        BlockPos var10 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);

                        if (worldIn.isAirBlock(var10.up()) && this.getCanBlockBurn(worldIn, var10))
                        {
                            worldIn.setBlockState(var10.up(), Blocks.fire.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    protected boolean isSurroundingBlockFlammable(World worldIn, BlockPos pos)
    {
        EnumFacing[] var3 = EnumFacing.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            EnumFacing var6 = var3[var5];

            if (this.getCanBlockBurn(worldIn, pos.offset(var6)))
            {
                return true;
            }
        }

        return false;
    }

    private boolean getCanBlockBurn(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().getMaterial().getCanBurn();
    }
}
