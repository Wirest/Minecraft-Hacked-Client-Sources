// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockReed extends Block
{
    public static final PropertyInteger AGE;
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
    }
    
    protected BlockReed() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockReed.AGE, 0));
        final float f = 0.375f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if ((worldIn.getBlockState(pos.down()).getBlock() == Blocks.reeds || this.checkForDrop(worldIn, pos, state)) && worldIn.isAirBlock(pos.up())) {
            int i;
            for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {}
            if (i < 3) {
                final int j = state.getValue((IProperty<Integer>)BlockReed.AGE);
                if (j == 15) {
                    worldIn.setBlockState(pos.up(), this.getDefaultState());
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockReed.AGE, 0), 4);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockReed.AGE, j + 1), 4);
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.down()).getBlock();
        if (block == this) {
            return true;
        }
        if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.sand) {
            return false;
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing).down()).getBlock().getMaterial() == Material.water) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    protected final boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.canBlockStay(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return this.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.reeds;
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
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.reeds;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockReed.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockReed.AGE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockReed.AGE });
    }
}
