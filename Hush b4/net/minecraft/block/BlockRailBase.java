// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.IStringSerializable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    protected final boolean isPowered;
    
    public static boolean isRailBlock(final World worldIn, final BlockPos pos) {
        return isRailBlock(worldIn.getBlockState(pos));
    }
    
    public static boolean isRailBlock(final IBlockState state) {
        final Block block = state.getBlock();
        return block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail || block == Blocks.activator_rail;
    }
    
    protected BlockRailBase(final boolean isPowered) {
        super(Material.circuits);
        this.isPowered = isPowered;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() == this) ? iblockstate.getValue(this.getShapeProperty()) : null;
        if (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        }
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            state = this.func_176564_a(worldIn, pos, state, true);
            if (this.isPowered) {
                this.onNeighborBlockChange(worldIn, pos, state, this);
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final EnumRailDirection blockrailbase$enumraildirection = state.getValue(this.getShapeProperty());
            boolean flag = false;
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
                flag = true;
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.east())) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.west())) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.north())) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.south())) {
                flag = true;
            }
            if (flag) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else {
                this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
            }
        }
    }
    
    protected void onNeighborChangedInternal(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
    }
    
    protected IBlockState func_176564_a(final World worldIn, final BlockPos p_176564_2_, final IBlockState p_176564_3_, final boolean p_176564_4_) {
        return worldIn.isRemote ? p_176564_3_ : new Rail(worldIn, p_176564_2_, p_176564_3_).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (state.getValue(this.getShapeProperty()).isAscending()) {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this);
        }
        if (this.isPowered) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        }
    }
    
    public abstract IProperty<EnumRailDirection> getShapeProperty();
    
    public enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH("NORTH_SOUTH", 0, 0, "north_south"), 
        EAST_WEST("EAST_WEST", 1, 1, "east_west"), 
        ASCENDING_EAST("ASCENDING_EAST", 2, 2, "ascending_east"), 
        ASCENDING_WEST("ASCENDING_WEST", 3, 3, "ascending_west"), 
        ASCENDING_NORTH("ASCENDING_NORTH", 4, 4, "ascending_north"), 
        ASCENDING_SOUTH("ASCENDING_SOUTH", 5, 5, "ascending_south"), 
        SOUTH_EAST("SOUTH_EAST", 6, 6, "south_east"), 
        SOUTH_WEST("SOUTH_WEST", 7, 7, "south_west"), 
        NORTH_WEST("NORTH_WEST", 8, 8, "north_west"), 
        NORTH_EAST("NORTH_EAST", 9, 9, "north_east");
        
        private static final EnumRailDirection[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        static {
            META_LOOKUP = new EnumRailDirection[values().length];
            EnumRailDirection[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumRailDirection blockrailbase$enumraildirection = values[i];
                EnumRailDirection.META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
            }
        }
        
        private EnumRailDirection(final String name2, final int ordinal, final int meta, final String name) {
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
        
        public boolean isAscending() {
            return this == EnumRailDirection.ASCENDING_NORTH || this == EnumRailDirection.ASCENDING_EAST || this == EnumRailDirection.ASCENDING_SOUTH || this == EnumRailDirection.ASCENDING_WEST;
        }
        
        public static EnumRailDirection byMetadata(int meta) {
            if (meta < 0 || meta >= EnumRailDirection.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumRailDirection.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
    
    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> field_150657_g;
        
        public Rail(final World worldIn, final BlockPos pos, final IBlockState state) {
            this.field_150657_g = (List<BlockPos>)Lists.newArrayList();
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            final EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRailBase.this.getShapeProperty());
            this.isPowered = this.block.isPowered;
            this.func_180360_a(blockrailbase$enumraildirection);
        }
        
        private void func_180360_a(final EnumRailDirection p_180360_1_) {
            this.field_150657_g.clear();
            switch (p_180360_1_) {
                case NORTH_SOUTH: {
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south());
                    break;
                }
                case EAST_WEST: {
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east());
                    break;
                }
                case ASCENDING_EAST: {
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east().up());
                    break;
                }
                case ASCENDING_WEST: {
                    this.field_150657_g.add(this.pos.west().up());
                    this.field_150657_g.add(this.pos.east());
                    break;
                }
                case ASCENDING_NORTH: {
                    this.field_150657_g.add(this.pos.north().up());
                    this.field_150657_g.add(this.pos.south());
                    break;
                }
                case ASCENDING_SOUTH: {
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south().up());
                    break;
                }
                case SOUTH_EAST: {
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.south());
                    break;
                }
                case SOUTH_WEST: {
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.south());
                    break;
                }
                case NORTH_WEST: {
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.north());
                    break;
                }
                case NORTH_EAST: {
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.north());
                    break;
                }
            }
        }
        
        private void func_150651_b() {
            for (int i = 0; i < this.field_150657_g.size(); ++i) {
                final Rail blockrailbase$rail = this.findRailAt(this.field_150657_g.get(i));
                if (blockrailbase$rail != null && blockrailbase$rail.func_150653_a(this)) {
                    this.field_150657_g.set(i, blockrailbase$rail.pos);
                }
                else {
                    this.field_150657_g.remove(i--);
                }
            }
        }
        
        private boolean hasRailAt(final BlockPos pos) {
            return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down());
        }
        
        private Rail findRailAt(final BlockPos pos) {
            IBlockState iblockstate = this.world.getBlockState(pos);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                return new Rail(this.world, pos, iblockstate);
            }
            BlockPos lvt_2_1_ = pos.up();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                return new Rail(this.world, lvt_2_1_, iblockstate);
            }
            lvt_2_1_ = pos.down();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            return BlockRailBase.isRailBlock(iblockstate) ? new Rail(this.world, lvt_2_1_, iblockstate) : null;
        }
        
        private boolean func_150653_a(final Rail p_150653_1_) {
            return this.func_180363_c(p_150653_1_.pos);
        }
        
        private boolean func_180363_c(final BlockPos p_180363_1_) {
            for (int i = 0; i < this.field_150657_g.size(); ++i) {
                final BlockPos blockpos = this.field_150657_g.get(i);
                if (blockpos.getX() == p_180363_1_.getX() && blockpos.getZ() == p_180363_1_.getZ()) {
                    return true;
                }
            }
            return false;
        }
        
        protected int countAdjacentRails() {
            int i = 0;
            for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.hasRailAt(this.pos.offset((EnumFacing)enumfacing))) {
                    ++i;
                }
            }
            return i;
        }
        
        private boolean func_150649_b(final Rail rail) {
            return this.func_150653_a(rail) || this.field_150657_g.size() != 2;
        }
        
        private void func_150645_c(final Rail p_150645_1_) {
            this.field_150657_g.add(p_150645_1_.pos);
            final BlockPos blockpos = this.pos.north();
            final BlockPos blockpos2 = this.pos.south();
            final BlockPos blockpos3 = this.pos.west();
            final BlockPos blockpos4 = this.pos.east();
            final boolean flag = this.func_180363_c(blockpos);
            final boolean flag2 = this.func_180363_c(blockpos2);
            final boolean flag3 = this.func_180363_c(blockpos3);
            final boolean flag4 = this.func_180363_c(blockpos4);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if (flag || flag2) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if (flag3 || flag4) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag2 && flag4 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag2 && flag3 && !flag && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag3 && !flag2 && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag4 && !flag2 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos4.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }
        
        private boolean func_180361_d(final BlockPos p_180361_1_) {
            final Rail blockrailbase$rail = this.findRailAt(p_180361_1_);
            if (blockrailbase$rail == null) {
                return false;
            }
            blockrailbase$rail.func_150651_b();
            return blockrailbase$rail.func_150649_b(this);
        }
        
        public Rail func_180364_a(final boolean p_180364_1_, final boolean p_180364_2_) {
            final BlockPos blockpos = this.pos.north();
            final BlockPos blockpos2 = this.pos.south();
            final BlockPos blockpos3 = this.pos.west();
            final BlockPos blockpos4 = this.pos.east();
            final boolean flag = this.func_180361_d(blockpos);
            final boolean flag2 = this.func_180361_d(blockpos2);
            final boolean flag3 = this.func_180361_d(blockpos3);
            final boolean flag4 = this.func_180361_d(blockpos4);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if ((flag || flag2) && !flag3 && !flag4) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if ((flag3 || flag4) && !flag && !flag2) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag2 && flag4 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag2 && flag3 && !flag && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag3 && !flag2 && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag4 && !flag2 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                if (flag || flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                }
                if (flag3 || flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                }
                if (!this.isPowered) {
                    if (p_180364_1_) {
                        if (flag2 && flag4) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                        if (flag3 && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag4 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else {
                        if (flag && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                        if (flag4 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag3 && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag2 && flag4) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos4.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.func_180360_a(blockrailbase$enumraildirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state) {
                this.world.setBlockState(this.pos, this.state, 3);
                for (int i = 0; i < this.field_150657_g.size(); ++i) {
                    final Rail blockrailbase$rail = this.findRailAt(this.field_150657_g.get(i));
                    if (blockrailbase$rail != null) {
                        blockrailbase$rail.func_150651_b();
                        if (blockrailbase$rail.func_150649_b(this)) {
                            blockrailbase$rail.func_150645_c(this);
                        }
                    }
                }
            }
            return this;
        }
        
        public IBlockState getBlockState() {
            return this.state;
        }
    }
}
