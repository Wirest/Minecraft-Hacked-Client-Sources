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

public class BlockPlanks extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockPlanks() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPlanks.VARIANT, EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blockplanks$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPlanks.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).func_181070_c();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPlanks.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        OAK("OAK", 0, 0, "oak", MapColor.woodColor), 
        SPRUCE("SPRUCE", 1, 1, "spruce", MapColor.obsidianColor), 
        BIRCH("BIRCH", 2, 2, "birch", MapColor.sandColor), 
        JUNGLE("JUNGLE", 3, 3, "jungle", MapColor.dirtColor), 
        ACACIA("ACACIA", 4, 4, "acacia", MapColor.adobeColor), 
        DARK_OAK("DARK_OAK", 5, 5, "dark_oak", "big_oak", MapColor.brownColor);
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor field_181071_k;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockplanks$enumtype = values[i];
                EnumType.META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
            }
        }
        
        private EnumType(final String s, final int n, final int p_i46388_3_, final String p_i46388_4_, final MapColor p_i46388_5_) {
            this(s, n, p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
        }
        
        private EnumType(final String name, final int ordinal, final int p_i46389_3_, final String p_i46389_4_, final String p_i46389_5_, final MapColor p_i46389_6_) {
            this.meta = p_i46389_3_;
            this.name = p_i46389_4_;
            this.unlocalizedName = p_i46389_5_;
            this.field_181071_k = p_i46389_6_;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public MapColor func_181070_c() {
            return this.field_181071_k;
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
