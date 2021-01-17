// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockStaticLiquid extends BlockLiquid
{
    protected BlockStaticLiquid(final Material materialIn) {
        super(materialIn);
        this.setTickRandomly(false);
        if (materialIn == Material.lava) {
            this.setTickRandomly(true);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.checkForMixing(worldIn, pos, state)) {
            this.updateLiquid(worldIn, pos, state);
        }
    }
    
    private void updateLiquid(final World worldIn, final BlockPos pos, final IBlockState state) {
        final BlockDynamicLiquid blockdynamicliquid = BlockLiquid.getFlowingBlock(this.blockMaterial);
        worldIn.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty((IProperty<Comparable>)BlockStaticLiquid.LEVEL, (Integer)state.getValue((IProperty<V>)BlockStaticLiquid.LEVEL)), 2);
        worldIn.scheduleUpdate(pos, blockdynamicliquid, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.blockMaterial == Material.lava && worldIn.getGameRules().getBoolean("doFireTick")) {
            final int i = rand.nextInt(3);
            if (i > 0) {
                BlockPos blockpos = pos;
                for (int j = 0; j < i; ++j) {
                    blockpos = blockpos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
                    final Block block = worldIn.getBlockState(blockpos).getBlock();
                    if (block.blockMaterial == Material.air) {
                        if (this.isSurroundingBlockFlammable(worldIn, blockpos)) {
                            worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
                            return;
                        }
                    }
                    else if (block.blockMaterial.blocksMovement()) {
                        return;
                    }
                }
            }
            else {
                for (int k = 0; k < 3; ++k) {
                    final BlockPos blockpos2 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
                    if (worldIn.isAirBlock(blockpos2.up()) && this.getCanBlockBurn(worldIn, blockpos2)) {
                        worldIn.setBlockState(blockpos2.up(), Blocks.fire.getDefaultState());
                    }
                }
            }
        }
    }
    
    protected boolean isSurroundingBlockFlammable(final World worldIn, final BlockPos pos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (this.getCanBlockBurn(worldIn, pos.offset(enumfacing))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean getCanBlockBurn(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().getMaterial().getCanBurn();
    }
}
