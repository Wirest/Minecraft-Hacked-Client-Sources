// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCactus extends Block
{
    public static final PropertyInteger AGE;
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
    }
    
    protected BlockCactus() {
        super(Material.cactus);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCactus.AGE, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final BlockPos blockpos = pos.up();
        if (worldIn.isAirBlock(blockpos)) {
            int i;
            for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {}
            if (i < 3) {
                final int j = state.getValue((IProperty<Integer>)BlockCactus.AGE);
                if (j == 15) {
                    worldIn.setBlockState(blockpos, this.getDefaultState());
                    final IBlockState iblockstate = state.withProperty((IProperty<Comparable>)BlockCactus.AGE, 0);
                    worldIn.setBlockState(pos, iblockstate, 4);
                    this.onNeighborBlockChange(worldIn, blockpos, iblockstate, this);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCactus.AGE, j + 1), 4);
                }
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final float f = 0.0625f;
        return new AxisAlignedBB(pos.getX() + f, pos.getY(), pos.getZ() + f, pos.getX() + 1 - f, pos.getY() + 1 - f, pos.getZ() + 1 - f);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        final float f = 0.0625f;
        return new AxisAlignedBB(pos.getX() + f, pos.getY(), pos.getZ() + f, pos.getX() + 1 - f, pos.getY() + 1, pos.getZ() + 1 - f);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock().getMaterial().isSolid()) {
                return false;
            }
        }
        final Block block = worldIn.getBlockState(pos.down()).getBlock();
        return block == Blocks.cactus || block == Blocks.sand;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0f);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCactus.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCactus.AGE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCactus.AGE });
    }
}
