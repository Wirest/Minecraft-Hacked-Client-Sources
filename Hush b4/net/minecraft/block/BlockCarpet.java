// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.PropertyEnum;

public class BlockCarpet extends Block
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
    
    protected BlockCarpet() {
        super(Material.carpet);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCarpet.COLOR, EnumDyeColor.WHITE));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockCarpet.COLOR).getMapColor();
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
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsFromMeta(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBlockBoundsFromMeta(0);
    }
    
    protected void setBlockBoundsFromMeta(final int meta) {
        final int i = 0;
        final float f = 1 * (1 + i) / 16.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, f, 1.0f);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    private boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }
    
    private boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockCarpet.COLOR).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < 16; ++i) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockCarpet.COLOR).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCarpet.COLOR });
    }
}
