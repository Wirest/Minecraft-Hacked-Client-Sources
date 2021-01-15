package net.minecraft.block;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid
{
    int adjacentSourceBlocks;

    protected BlockDynamicLiquid(Material materialIn)
    {
        super(materialIn);
    }

    private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState)
    {
        worldIn.setBlockState(pos, getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, currentState.getValue(LEVEL)), 2);
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int var5 = ((Integer)state.getValue(LEVEL)).intValue();
        byte var6 = 1;

        if (this.blockMaterial == Material.lava && !worldIn.provider.doesWaterVaporize())
        {
            var6 = 2;
        }

        int var7 = this.tickRate(worldIn);
        int var16;

        if (var5 > 0)
        {
            int var8 = -100;
            this.adjacentSourceBlocks = 0;
            EnumFacing var10;

            for (Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator(); var9.hasNext(); var8 = this.checkAdjacentBlock(worldIn, pos.offset(var10), var8))
            {
                var10 = (EnumFacing)var9.next();
            }

            int var14 = var8 + var6;

            if (var14 >= 8 || var8 < 0)
            {
                var14 = -1;
            }

            if (this.getLevel(worldIn, pos.up()) >= 0)
            {
                var16 = this.getLevel(worldIn, pos.up());

                if (var16 >= 8)
                {
                    var14 = var16;
                }
                else
                {
                    var14 = var16 + 8;
                }
            }

            if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.water)
            {
                IBlockState var17 = worldIn.getBlockState(pos.down());

                if (var17.getBlock().getMaterial().isSolid())
                {
                    var14 = 0;
                }
                else if (var17.getBlock().getMaterial() == this.blockMaterial && ((Integer)var17.getValue(LEVEL)).intValue() == 0)
                {
                    var14 = 0;
                }
            }

            if (this.blockMaterial == Material.lava && var5 < 8 && var14 < 8 && var14 > var5 && rand.nextInt(4) != 0)
            {
                var7 *= 4;
            }

            if (var14 == var5)
            {
                this.placeStaticBlock(worldIn, pos, state);
            }
            else
            {
                var5 = var14;

                if (var14 < 0)
                {
                    worldIn.setBlockToAir(pos);
                }
                else
                {
                    state = state.withProperty(LEVEL, Integer.valueOf(var14));
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.scheduleUpdate(pos, this, var7);
                    worldIn.notifyNeighborsOfStateChange(pos, this);
                }
            }
        }
        else
        {
            this.placeStaticBlock(worldIn, pos, state);
        }

        IBlockState var13 = worldIn.getBlockState(pos.down());

        if (this.canFlowInto(worldIn, pos.down(), var13))
        {
            if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.down()).getBlock().getMaterial() == Material.water)
            {
                worldIn.setBlockState(pos.down(), Blocks.stone.getDefaultState());
                this.triggerMixEffects(worldIn, pos.down());
                return;
            }

            if (var5 >= 8)
            {
                this.tryFlowInto(worldIn, pos.down(), var13, var5);
            }
            else
            {
                this.tryFlowInto(worldIn, pos.down(), var13, var5 + 8);
            }
        }
        else if (var5 >= 0 && (var5 == 0 || this.isBlocked(worldIn, pos.down(), var13)))
        {
            Set var15 = this.getPossibleFlowDirections(worldIn, pos);
            var16 = var5 + var6;

            if (var5 >= 8)
            {
                var16 = 1;
            }

            if (var16 >= 8)
            {
                return;
            }

            Iterator var11 = var15.iterator();

            while (var11.hasNext())
            {
                EnumFacing var12 = (EnumFacing)var11.next();
                this.tryFlowInto(worldIn, pos.offset(var12), worldIn.getBlockState(pos.offset(var12)), var16);
            }
        }
    }

    private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level)
    {
        if (this.canFlowInto(worldIn, pos, state))
        {
            if (state.getBlock() != Blocks.air)
            {
                if (this.blockMaterial == Material.lava)
                {
                    this.triggerMixEffects(worldIn, pos);
                }
                else
                {
                    state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
                }
            }

            worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, Integer.valueOf(level)), 3);
        }
    }

    private int func_176374_a(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost)
    {
        int var5 = 1000;
        Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var6.hasNext())
        {
            EnumFacing var7 = (EnumFacing)var6.next();

            if (var7 != calculateFlowCost)
            {
                BlockPos var8 = pos.offset(var7);
                IBlockState var9 = worldIn.getBlockState(var8);

                if (!this.isBlocked(worldIn, var8, var9) && (var9.getBlock().getMaterial() != this.blockMaterial || ((Integer)var9.getValue(LEVEL)).intValue() > 0))
                {
                    if (!this.isBlocked(worldIn, var8.down(), var9))
                    {
                        return distance;
                    }

                    if (distance < 4)
                    {
                        int var10 = this.func_176374_a(worldIn, var8, distance + 1, var7.getOpposite());

                        if (var10 < var5)
                        {
                            var5 = var10;
                        }
                    }
                }
            }
        }

        return var5;
    }

    /**
     * This method returns a Set of EnumFacing
     */
    private Set getPossibleFlowDirections(World worldIn, BlockPos pos)
    {
        int var3 = 1000;
        EnumSet var4 = EnumSet.noneOf(EnumFacing.class);
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var5.hasNext())
        {
            EnumFacing var6 = (EnumFacing)var5.next();
            BlockPos var7 = pos.offset(var6);
            IBlockState var8 = worldIn.getBlockState(var7);

            if (!this.isBlocked(worldIn, var7, var8) && (var8.getBlock().getMaterial() != this.blockMaterial || ((Integer)var8.getValue(LEVEL)).intValue() > 0))
            {
                int var9;

                if (this.isBlocked(worldIn, var7.down(), worldIn.getBlockState(var7.down())))
                {
                    var9 = this.func_176374_a(worldIn, var7, 1, var6.getOpposite());
                }
                else
                {
                    var9 = 0;
                }

                if (var9 < var3)
                {
                    var4.clear();
                }

                if (var9 <= var3)
                {
                    var4.add(var6);
                    var3 = var9;
                }
            }
        }

        return var4;
    }

    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state)
    {
        Block var4 = worldIn.getBlockState(pos).getBlock();
        return !(var4 instanceof BlockDoor) && var4 != Blocks.standing_sign && var4 != Blocks.ladder && var4 != Blocks.reeds ? (var4.blockMaterial == Material.portal ? true : var4.blockMaterial.blocksMovement()) : true;
    }

    protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel)
    {
        int var4 = this.getLevel(worldIn, pos);

        if (var4 < 0)
        {
            return currentMinLevel;
        }
        else
        {
            if (var4 == 0)
            {
                ++this.adjacentSourceBlocks;
            }

            if (var4 >= 8)
            {
                var4 = 0;
            }

            return currentMinLevel >= 0 && var4 >= currentMinLevel ? currentMinLevel : var4;
        }
    }

    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state)
    {
        Material var4 = state.getBlock().getMaterial();
        return var4 != this.blockMaterial && var4 != Material.lava && !this.isBlocked(worldIn, pos, state);
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkForMixing(worldIn, pos, state))
        {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
}
