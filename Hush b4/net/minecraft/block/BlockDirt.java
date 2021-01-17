// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockDirt extends Block
{
    public static final PropertyEnum<DirtType> VARIANT;
    public static final PropertyBool SNOWY;
    
    static {
        VARIANT = PropertyEnum.create("variant", DirtType.class);
        SNOWY = PropertyBool.create("snowy");
    }
    
    protected BlockDirt() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirt.VARIANT, DirtType.DIRT).withProperty((IProperty<Comparable>)BlockDirt.SNOWY, false));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockDirt.VARIANT).func_181066_d();
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDirt.VARIANT) == DirtType.PODZOL) {
            final Block block = worldIn.getBlockState(pos.up()).getBlock();
            state = state.withProperty((IProperty<Comparable>)BlockDirt.SNOWY, block == Blocks.snow || block == Blocks.snow_layer);
        }
        return state;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return (iblockstate.getBlock() != this) ? 0 : iblockstate.getValue(BlockDirt.VARIANT).getMetadata();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockDirt.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirt.VARIANT, BlockDirt.SNOWY });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        DirtType blockdirt$dirttype = state.getValue(BlockDirt.VARIANT);
        if (blockdirt$dirttype == DirtType.PODZOL) {
            blockdirt$dirttype = DirtType.DIRT;
        }
        return blockdirt$dirttype.getMetadata();
    }
    
    public enum DirtType implements IStringSerializable
    {
        DIRT("DIRT", 0, 0, "dirt", "default", MapColor.dirtColor), 
        COARSE_DIRT("COARSE_DIRT", 1, 1, "coarse_dirt", "coarse", MapColor.dirtColor), 
        PODZOL("PODZOL", 2, 2, "podzol", MapColor.obsidianColor);
        
        private static final DirtType[] METADATA_LOOKUP;
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor field_181067_h;
        
        static {
            METADATA_LOOKUP = new DirtType[values().length];
            DirtType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DirtType blockdirt$dirttype = values[i];
                DirtType.METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
            }
        }
        
        private DirtType(final String s, final int n, final int p_i46396_3_, final String p_i46396_4_, final MapColor p_i46396_5_) {
            this(s, n, p_i46396_3_, p_i46396_4_, p_i46396_4_, p_i46396_5_);
        }
        
        private DirtType(final String name, final int ordinal, final int p_i46397_3_, final String p_i46397_4_, final String p_i46397_5_, final MapColor p_i46397_6_) {
            this.metadata = p_i46397_3_;
            this.name = p_i46397_4_;
            this.unlocalizedName = p_i46397_5_;
            this.field_181067_h = p_i46397_6_;
        }
        
        public int getMetadata() {
            return this.metadata;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
        
        public MapColor func_181066_d() {
            return this.field_181067_h;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static DirtType byMetadata(int metadata) {
            if (metadata < 0 || metadata >= DirtType.METADATA_LOOKUP.length) {
                metadata = 0;
            }
            return DirtType.METADATA_LOOKUP[metadata];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
