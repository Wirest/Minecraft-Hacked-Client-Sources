// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStone extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStone.VARIANT, EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + EnumType.STONE.getUnlocalizedName() + ".name");
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockStone.VARIANT).func_181072_c();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockStone.VARIANT) == EnumType.STONE) ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStone.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blockstone$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blockstone$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStone.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStone.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStone.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, MapColor.stoneColor, "stone"), 
        GRANITE("GRANITE", 1, 1, MapColor.dirtColor, "granite"), 
        GRANITE_SMOOTH("GRANITE_SMOOTH", 2, 2, MapColor.dirtColor, "smooth_granite", "graniteSmooth"), 
        DIORITE("DIORITE", 3, 3, MapColor.quartzColor, "diorite"), 
        DIORITE_SMOOTH("DIORITE_SMOOTH", 4, 4, MapColor.quartzColor, "smooth_diorite", "dioriteSmooth"), 
        ANDESITE("ANDESITE", 5, 5, MapColor.stoneColor, "andesite"), 
        ANDESITE_SMOOTH("ANDESITE_SMOOTH", 6, 6, MapColor.stoneColor, "smooth_andesite", "andesiteSmooth");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor field_181073_l;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockstone$enumtype = values[i];
                EnumType.META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
            }
        }
        
        private EnumType(final String s, final int n, final int p_i46383_3_, final MapColor p_i46383_4_, final String p_i46383_5_) {
            this(s, n, p_i46383_3_, p_i46383_4_, p_i46383_5_, p_i46383_5_);
        }
        
        private EnumType(final String name, final int ordinal, final int p_i46384_3_, final MapColor p_i46384_4_, final String p_i46384_5_, final String p_i46384_6_) {
            this.meta = p_i46384_3_;
            this.name = p_i46384_5_;
            this.unlocalizedName = p_i46384_6_;
            this.field_181073_l = p_i46384_4_;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public MapColor func_181072_c() {
            return this.field_181073_l;
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
