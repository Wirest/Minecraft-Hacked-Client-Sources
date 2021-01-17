// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockMushroom extends BlockBush implements IGrowable
{
    protected BlockMushroom() {
        final float f = 0.2f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 2.0f, 0.5f + f);
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(25) == 0) {
            int i = 5;
            final int j = 4;
            for (final BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if (worldIn.getBlockState(blockpos).getBlock() == this && --i <= 0) {
                    return;
                }
            }
            BlockPos blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            for (int k = 0; k < 4; ++k) {
                if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState())) {
                    pos = blockpos2;
                }
                blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }
            if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState())) {
                worldIn.setBlockState(blockpos2, this.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground.isFullBlock();
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            return iblockstate.getBlock() == Blocks.mycelium || (iblockstate.getBlock() == Blocks.dirt && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) || (worldIn.getLight(pos) < 13 && this.canPlaceBlockOn(iblockstate.getBlock()));
        }
        return false;
    }
    
    public boolean generateBigMushroom(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        worldIn.setBlockToAir(pos);
        WorldGenerator worldgenerator = null;
        if (this == Blocks.brown_mushroom) {
            worldgenerator = new WorldGenBigMushroom(Blocks.brown_mushroom_block);
        }
        else if (this == Blocks.red_mushroom) {
            worldgenerator = new WorldGenBigMushroom(Blocks.red_mushroom_block);
        }
        if (worldgenerator != null && worldgenerator.generate(worldIn, rand, pos)) {
            return true;
        }
        worldIn.setBlockState(pos, state, 3);
        return false;
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return rand.nextFloat() < 0.4;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.generateBigMushroom(worldIn, pos, state, rand);
    }
}
