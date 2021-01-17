// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSandStone extends Block
{
    public static final PropertyEnum<EnumType> TYPE;
    
    static {
        TYPE = PropertyEnum.create("type", EnumType.class);
    }
    
    public BlockSandStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSandStone.TYPE, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSandStone.TYPE).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blocksandstone$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blocksandstone$enumtype.getMetadata()));
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.sandColor;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSandStone.TYPE, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockSandStone.TYPE).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSandStone.TYPE });
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "sandstone", "default"), 
        CHISELED("CHISELED", 1, 1, "chiseled_sandstone", "chiseled"), 
        SMOOTH("SMOOTH", 2, 2, "smooth_sandstone", "smooth");
        
        private static final EnumType[] META_LOOKUP;
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blocksandstone$enumtype = values[i];
                EnumType.META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name, final String unlocalizedName) {
            this.metadata = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.metadata;
        }
        
        @Override
        public String toString() {
            return this.name;
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
