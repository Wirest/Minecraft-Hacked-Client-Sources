// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSand extends BlockFalling
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockSand() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSand.VARIANT, EnumType.SAND));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSand.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blocksand$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blocksand$enumtype.getMetadata()));
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockSand.VARIANT).getMapColor();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSand.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockSand.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSand.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        SAND("SAND", 0, 0, "sand", "default", MapColor.sandColor), 
        RED_SAND("RED_SAND", 1, 1, "red_sand", "red", MapColor.adobeColor);
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final MapColor mapColor;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blocksand$enumtype = values[i];
                EnumType.META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name, final String unlocalizedName, final MapColor mapColor) {
            this.meta = meta;
            this.name = name;
            this.mapColor = mapColor;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public MapColor getMapColor() {
            return this.mapColor;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
    }
}
