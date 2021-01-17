// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStainedGlassPane extends BlockPane
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
    
    public BlockStainedGlassPane() {
        super(Material.glass, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStainedGlassPane.NORTH, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.EAST, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.SOUTH, false).withProperty((IProperty<Comparable>)BlockStainedGlassPane.WEST, false).withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStainedGlassPane.COLOR).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockStainedGlassPane.COLOR).getMapColor();
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStainedGlassPane.COLOR).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStainedGlassPane.NORTH, BlockStainedGlassPane.EAST, BlockStainedGlassPane.WEST, BlockStainedGlassPane.SOUTH, BlockStainedGlassPane.COLOR });
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
}
