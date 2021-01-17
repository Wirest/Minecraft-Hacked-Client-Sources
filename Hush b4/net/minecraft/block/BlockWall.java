// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockWall extends Block
{
    public static final PropertyBool UP;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyEnum<EnumType> VARIANT;
    
    static {
        UP = PropertyBool.create("up");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public BlockWall(final Block modelBlock) {
        super(modelBlock.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockWall.UP, false).withProperty((IProperty<Comparable>)BlockWall.NORTH, false).withProperty((IProperty<Comparable>)BlockWall.EAST, false).withProperty((IProperty<Comparable>)BlockWall.SOUTH, false).withProperty((IProperty<Comparable>)BlockWall.WEST, false).withProperty(BlockWall.VARIANT, EnumType.NORMAL));
        this.setHardness(modelBlock.blockHardness);
        this.setResistance(modelBlock.blockResistance / 3.0f);
        this.setStepSound(modelBlock.stepSound);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + EnumType.NORMAL.getUnlocalizedName() + ".name");
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final boolean flag = this.canConnectTo(worldIn, pos.north());
        final boolean flag2 = this.canConnectTo(worldIn, pos.south());
        final boolean flag3 = this.canConnectTo(worldIn, pos.west());
        final boolean flag4 = this.canConnectTo(worldIn, pos.east());
        float f = 0.25f;
        float f2 = 0.75f;
        float f3 = 0.25f;
        float f4 = 0.75f;
        float f5 = 1.0f;
        if (flag) {
            f3 = 0.0f;
        }
        if (flag2) {
            f4 = 1.0f;
        }
        if (flag3) {
            f = 0.0f;
        }
        if (flag4) {
            f2 = 1.0f;
        }
        if (flag && flag2 && !flag3 && !flag4) {
            f5 = 0.8125f;
            f = 0.3125f;
            f2 = 0.6875f;
        }
        else if (!flag && !flag2 && flag3 && flag4) {
            f5 = 0.8125f;
            f3 = 0.3125f;
            f4 = 0.6875f;
        }
        this.setBlockBounds(f, 0.0f, f3, f2, f5, f4);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        this.maxY = 1.5;
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    public boolean canConnectTo(final IBlockAccess worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        return block != Blocks.barrier && (block == this || block instanceof BlockFenceGate || (block.blockMaterial.isOpaque() && block.isFullCube() && block.blockMaterial != Material.gourd));
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blockwall$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blockwall$enumtype.getMetadata()));
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockWall.VARIANT).getMetadata();
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side != EnumFacing.DOWN || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockWall.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockWall.VARIANT).getMetadata();
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockWall.UP, !worldIn.isAirBlock(pos.up())).withProperty((IProperty<Comparable>)BlockWall.NORTH, this.canConnectTo(worldIn, pos.north())).withProperty((IProperty<Comparable>)BlockWall.EAST, this.canConnectTo(worldIn, pos.east())).withProperty((IProperty<Comparable>)BlockWall.SOUTH, this.canConnectTo(worldIn, pos.south())).withProperty((IProperty<Comparable>)BlockWall.WEST, this.canConnectTo(worldIn, pos.west()));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockWall.UP, BlockWall.NORTH, BlockWall.EAST, BlockWall.WEST, BlockWall.SOUTH, BlockWall.VARIANT });
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORMAL("NORMAL", 0, 0, "cobblestone", "normal"), 
        MOSSY("MOSSY", 1, 1, "mossy_cobblestone", "mossy");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockwall$enumtype = values[i];
                EnumType.META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
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
