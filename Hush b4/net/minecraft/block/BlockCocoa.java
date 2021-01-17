// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCocoa extends BlockDirectional implements IGrowable
{
    public static final PropertyInteger AGE;
    
    static {
        AGE = PropertyInteger.create("age", 0, 2);
    }
    
    public BlockCocoa() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockCocoa.AGE, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
        else if (worldIn.rand.nextInt(5) == 0) {
            final int i = state.getValue((IProperty<Integer>)BlockCocoa.AGE);
            if (i < 2) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.AGE, i + 1), 2);
            }
        }
    }
    
    public boolean canBlockStay(final World worldIn, BlockPos pos, final IBlockState state) {
        pos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING));
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == Blocks.log && iblockstate.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE;
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
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockCocoa.FACING);
        final int i = iblockstate.getValue((IProperty<Integer>)BlockCocoa.AGE);
        final int j = 4 + i * 2;
        final int k = 5 + i * 2;
        final float f = j / 2.0f;
        switch (enumfacing) {
            case SOUTH: {
                this.setBlockBounds((8.0f - f) / 16.0f, (12.0f - k) / 16.0f, (15.0f - j) / 16.0f, (8.0f + f) / 16.0f, 0.75f, 0.9375f);
                break;
            }
            case NORTH: {
                this.setBlockBounds((8.0f - f) / 16.0f, (12.0f - k) / 16.0f, 0.0625f, (8.0f + f) / 16.0f, 0.75f, (1.0f + j) / 16.0f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.0625f, (12.0f - k) / 16.0f, (8.0f - f) / 16.0f, (1.0f + j) / 16.0f, 0.75f, (8.0f + f) / 16.0f);
                break;
            }
            case EAST: {
                this.setBlockBounds((15.0f - j) / 16.0f, (12.0f - k) / 16.0f, (8.0f - f) / 16.0f, 0.9375f, 0.75f, (8.0f + f) / 16.0f);
                break;
            }
        }
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.FACING, enumfacing), 2);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, facing.getOpposite()).withProperty((IProperty<Comparable>)BlockCocoa.AGE, 0);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
    }
    
    private void dropBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, pos, state, 0);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final int i = state.getValue((IProperty<Integer>)BlockCocoa.AGE);
        int j = 1;
        if (i >= 2) {
            j = 3;
        }
        for (int k = 0; k < j; ++k) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.dye;
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return EnumDyeColor.BROWN.getDyeDamage();
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue((IProperty<Integer>)BlockCocoa.AGE) < 2;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCocoa.AGE, state.getValue((IProperty<Integer>)BlockCocoa.AGE) + 1), 2);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.FACING, EnumFacing.getHorizontal(meta)).withProperty((IProperty<Comparable>)BlockCocoa.AGE, (meta & 0xF) >> 2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockCocoa.FACING).getHorizontalIndex();
        i |= state.getValue((IProperty<Integer>)BlockCocoa.AGE) << 2;
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCocoa.FACING, BlockCocoa.AGE });
    }
}
