// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
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

public class BlockStainedGlass extends BlockBreakable
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
    
    public BlockStainedGlass(final Material materialIn) {
        super(materialIn, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStainedGlass.COLOR).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumDyeColor[] values;
        for (int length = (values = EnumDyeColor.values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockStainedGlass.COLOR).getMapColor();
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.byMetadata(meta));
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
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStainedGlass.COLOR).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStainedGlass.COLOR });
    }
}
