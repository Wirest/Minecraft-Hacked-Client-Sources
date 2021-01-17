// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockQuartz extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockQuartz() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockQuartz.VARIANT, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (meta != EnumType.LINES_Y.getMetadata()) {
            return (meta == EnumType.CHISELED.getMetadata()) ? this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.CHISELED) : this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.DEFAULT);
        }
        switch (facing.getAxis()) {
            case Z: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_Z);
            }
            case X: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_X);
            }
            default: {
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_Y);
            }
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        final EnumType blockquartz$enumtype = state.getValue(BlockQuartz.VARIANT);
        return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        final EnumType blockquartz$enumtype = state.getValue(BlockQuartz.VARIANT);
        return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetadata());
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, EnumType.DEFAULT.getMetadata()));
        list.add(new ItemStack(itemIn, 1, EnumType.CHISELED.getMetadata()));
        list.add(new ItemStack(itemIn, 1, EnumType.LINES_Y.getMetadata()));
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.quartzColor;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockQuartz.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockQuartz.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "default", "default"), 
        CHISELED("CHISELED", 1, 1, "chiseled", "chiseled"), 
        LINES_Y("LINES_Y", 2, 2, "lines_y", "lines"), 
        LINES_X("LINES_X", 3, 3, "lines_x", "lines"), 
        LINES_Z("LINES_Z", 4, 4, "lines_z", "lines");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String field_176805_h;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockquartz$enumtype = values[i];
                EnumType.META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.field_176805_h = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.unlocalizedName;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.field_176805_h;
        }
    }
}
