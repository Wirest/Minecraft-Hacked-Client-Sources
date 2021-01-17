// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockSlab extends Block
{
    public static final PropertyEnum<EnumBlockHalf> HALF;
    
    static {
        HALF = PropertyEnum.create("half", EnumBlockHalf.class);
    }
    
    public BlockSlab(final Material materialIn) {
        super(materialIn);
        if (this.isDouble()) {
            this.fullBlock = true;
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        this.setLightOpacity(255);
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (iblockstate.getBlock() == this) {
                if (iblockstate.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
                    this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
                else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
                }
            }
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return this.isDouble();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockSlab.HALF, EnumBlockHalf.BOTTOM);
        return this.isDouble() ? iblockstate : ((facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5)) ? iblockstate : iblockstate.withProperty(BlockSlab.HALF, EnumBlockHalf.TOP));
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return this.isDouble() ? 2 : 1;
    }
    
    @Override
    public boolean isFullCube() {
        return this.isDouble();
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        if (this.isDouble()) {
            return super.shouldSideBeRendered(worldIn, pos, side);
        }
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side)) {
            return false;
        }
        final BlockPos blockpos = pos.offset(side.getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final IBlockState iblockstate2 = worldIn.getBlockState(blockpos);
        final boolean flag = isSlab(iblockstate.getBlock()) && iblockstate.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
        final boolean flag2 = isSlab(iblockstate2.getBlock()) && iblockstate2.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
        return flag2 ? (side == EnumFacing.DOWN || (side == EnumFacing.UP && super.shouldSideBeRendered(worldIn, pos, side)) || !isSlab(iblockstate.getBlock()) || !flag) : (side == EnumFacing.UP || (side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side)) || !isSlab(iblockstate.getBlock()) || flag);
    }
    
    protected static boolean isSlab(final Block blockIn) {
        return blockIn == Blocks.stone_slab || blockIn == Blocks.wooden_slab || blockIn == Blocks.stone_slab2;
    }
    
    public abstract String getUnlocalizedName(final int p0);
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return super.getDamageValue(worldIn, pos) & 0x7;
    }
    
    public abstract boolean isDouble();
    
    public abstract IProperty<?> getVariantProperty();
    
    public abstract Object getVariant(final ItemStack p0);
    
    public enum EnumBlockHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String name;
        
        private EnumBlockHalf(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
