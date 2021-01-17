// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
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

public class BlockColored extends Block
{
    public static final PropertyEnum<EnumDyeColor> COLOR;
    
    static {
        COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    }
    
    public BlockColored(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockColored.COLOR).getMetadata();
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
        return state.getValue(BlockColored.COLOR).getMapColor();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockColored.COLOR).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockColored.COLOR });
    }
}
