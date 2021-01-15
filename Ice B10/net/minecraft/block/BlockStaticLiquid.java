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
    private static final String __OBFID = "CL_00000315";

    protected BlockStaticLiquid(Material p_i45429_1_)
    {
        super(p_i45429_1_);
        this.setTickRandomly(false);

        if (p_i45429_1_ == Material.lava)
        {
            this.setTickRandomly(true);
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!this.func_176365_e(worldIn, pos, state))
        {
            this.updateLiquid(worldIn, pos, state);
        }
    }

    private void updateLiquid(World worldIn, BlockPos p_176370_2_, IBlockState p_176370_3_)
    {
        BlockDynamicLiquid var4 = getDynamicLiquidForMaterial(this.blockMaterial);
        worldIn.setBlockState(p_176370_2_, var4.getDefaultState().withProperty(LEVEL, p_176370_3_.getValue(LEVEL)), 2);
        worldIn.scheduleUpdate(p_176370_2_, var4, this.tickRate(worldIn));
    }

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

                        if (worldIn.isAirBlock(var10.offsetUp()) && this.getCanBlockBurn(worldIn, var10))
                        {
                            worldIn.setBlockState(var10.offsetUp(), Blocks.fire.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    protected boolean isSurroundingBlockFlammable(World worldIn, BlockPos p_176369_2_)
    {
        EnumFacing[] var3 = EnumFacing.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            EnumFacing var6 = var3[var5];

            if (this.getCanBlockBurn(worldIn, p_176369_2_.offset(var6)))
            {
                return true;
            }
        }

        return false;
    }

    private boolean getCanBlockBurn(World worldIn, BlockPos p_176368_2_)
    {
        return worldIn.getBlockState(p_176368_2_).getBlock().getMaterial().getCanBurn();
    }
}
