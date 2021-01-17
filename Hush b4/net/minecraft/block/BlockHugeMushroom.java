// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockHugeMushroom extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    private final Block smallBlock;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockHugeMushroom(final Material p_i46392_1_, final MapColor p_i46392_2_, final Block p_i46392_3_) {
        super(p_i46392_1_, p_i46392_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHugeMushroom.VARIANT, EnumType.ALL_OUTSIDE));
        this.smallBlock = p_i46392_3_;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        switch (state.getValue(BlockHugeMushroom.VARIANT)) {
            case ALL_STEM: {
                return MapColor.clothColor;
            }
            case ALL_INSIDE: {
                return MapColor.sandColor;
            }
            case STEM: {
                return MapColor.sandColor;
            }
            default: {
                return super.getMapColor(state);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this.smallBlock);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(this.smallBlock);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockHugeMushroom.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockHugeMushroom.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORTH_WEST("NORTH_WEST", 0, 1, "north_west"), 
        NORTH("NORTH", 1, 2, "north"), 
        NORTH_EAST("NORTH_EAST", 2, 3, "north_east"), 
        WEST("WEST", 3, 4, "west"), 
        CENTER("CENTER", 4, 5, "center"), 
        EAST("EAST", 5, 6, "east"), 
        SOUTH_WEST("SOUTH_WEST", 6, 7, "south_west"), 
        SOUTH("SOUTH", 7, 8, "south"), 
        SOUTH_EAST("SOUTH_EAST", 8, 9, "south_east"), 
        STEM("STEM", 9, 10, "stem"), 
        ALL_INSIDE("ALL_INSIDE", 10, 0, "all_inside"), 
        ALL_OUTSIDE("ALL_OUTSIDE", 11, 14, "all_outside"), 
        ALL_STEM("ALL_STEM", 12, 15, "all_stem");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        static {
            META_LOOKUP = new EnumType[16];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockhugemushroom$enumtype = values[i];
                EnumType.META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name) {
            this.meta = meta;
            this.name = name;
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
            final EnumType blockhugemushroom$enumtype = EnumType.META_LOOKUP[meta];
            return (blockhugemushroom$enumtype == null) ? EnumType.META_LOOKUP[0] : blockhugemushroom$enumtype;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
