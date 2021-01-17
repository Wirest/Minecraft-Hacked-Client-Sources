// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCake extends Block
{
    public static final PropertyInteger BITES;
    
    static {
        BITES = PropertyInteger.create("bites", 0, 6);
    }
    
    protected BlockCake() {
        super(Material.cake);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCake.BITES, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final float f = 0.0625f;
        final float f2 = (1 + worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockCake.BITES) * 2) / 16.0f;
        final float f3 = 0.5f;
        this.setBlockBounds(f2, 0.0f, f, 1.0f - f, f3, 1.0f - f);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float f = 0.0625f;
        final float f2 = 0.5f;
        this.setBlockBounds(f, 0.0f, f, 1.0f - f, f2, 1.0f - f);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final float f = 0.0625f;
        final float f2 = (1 + state.getValue((IProperty<Integer>)BlockCake.BITES) * 2) / 16.0f;
        final float f3 = 0.5f;
        return new AxisAlignedBB(pos.getX() + f2, pos.getY(), pos.getZ() + f, pos.getX() + 1 - f, pos.getY() + f3, pos.getZ() + 1 - f);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
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
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        this.eatCake(worldIn, pos, state, playerIn);
        return true;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.eatCake(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }
    
    private void eatCake(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (player.canEat(false)) {
            player.triggerAchievement(StatList.field_181724_H);
            player.getFoodStats().addStats(2, 0.1f);
            final int i = state.getValue((IProperty<Integer>)BlockCake.BITES);
            if (i < 6) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCake.BITES, i + 1), 3);
            }
            else {
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid();
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.cake;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCake.BITES, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCake.BITES);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCake.BITES });
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return (7 - worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockCake.BITES)) * 2;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
}
