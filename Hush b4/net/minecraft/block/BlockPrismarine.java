// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPrismarine extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    public static final int ROUGH_META;
    public static final int BRICKS_META;
    public static final int DARK_META;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
        ROUGH_META = EnumType.ROUGH.getMetadata();
        BRICKS_META = EnumType.BRICKS.getMetadata();
        DARK_META = EnumType.DARK.getMetadata();
    }
    
    public BlockPrismarine() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPrismarine.VARIANT, EnumType.ROUGH));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + EnumType.ROUGH.getUnlocalizedName() + ".name");
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return (state.getValue(BlockPrismarine.VARIANT) == EnumType.ROUGH) ? MapColor.cyanColor : MapColor.diamondColor;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockPrismarine.VARIANT).getMetadata();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockPrismarine.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPrismarine.VARIANT });
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPrismarine.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.ROUGH_META));
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.BRICKS_META));
        list.add(new ItemStack(itemIn, 1, BlockPrismarine.DARK_META));
    }
    
    public enum EnumType implements IStringSerializable
    {
        ROUGH("ROUGH", 0, 0, "prismarine", "rough"), 
        BRICKS("BRICKS", 1, 1, "prismarine_bricks", "bricks"), 
        DARK("DARK", 2, 2, "dark_prismarine", "dark");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockprismarine$enumtype = values[i];
                EnumType.META_LOOKUP[blockprismarine$enumtype.getMetadata()] = blockprismarine$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
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
