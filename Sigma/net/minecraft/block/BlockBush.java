package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBush extends Block {
    private static final String __OBFID = "CL_00000208";

    protected BlockBush(Material materialIn) {
        super(materialIn);
        setTickRandomly(true);
        float var2 = 0.2F;
        setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var2 * 3.0F, 0.5F + var2);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    protected BlockBush() {
        this(Material.plants);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && canPlaceBlockOn(worldIn.getBlockState(pos.offsetDown()).getBlock());
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.grass || ground == Blocks.dirt || ground == Blocks.farmland;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        func_176475_e(worldIn, pos, state);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        func_176475_e(worldIn, pos, state);
    }

    protected void func_176475_e(World worldIn, BlockPos p_176475_2_, IBlockState p_176475_3_) {
        if (!canBlockStay(worldIn, p_176475_2_, p_176475_3_)) {
            dropBlockAsItem(worldIn, p_176475_2_, p_176475_3_, 0);
            worldIn.setBlockState(p_176475_2_, Blocks.air.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        return canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
