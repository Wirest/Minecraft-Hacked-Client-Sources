// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockEndPortal extends BlockContainer
{
    protected BlockEndPortal(final Material materialIn) {
        super(materialIn);
        this.setLightLevel(1.0f);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEndPortal();
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final float f = 0.0625f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, f, 1.0f);
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
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
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null && !worldIn.isRemote) {
            entityIn.travelToDimension(1);
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final double d0 = pos.getX() + rand.nextFloat();
        final double d2 = pos.getY() + 0.8f;
        final double d3 = pos.getZ() + rand.nextFloat();
        final double d4 = 0.0;
        final double d5 = 0.0;
        final double d6 = 0.0;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, d4, d5, d6, new int[0]);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.blackColor;
    }
}
