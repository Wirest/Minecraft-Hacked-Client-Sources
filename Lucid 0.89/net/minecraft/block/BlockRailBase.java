package net.minecraft.block;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    protected final boolean isPowered;

    public static boolean isRailBlock(World worldIn, BlockPos pos)
    {
        return isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state)
    {
        Block var1 = state.getBlock();
        return var1 == Blocks.rail || var1 == Blocks.golden_rail || var1 == Blocks.detector_rail || var1 == Blocks.activator_rail;
    }

    protected BlockRailBase(boolean isPowered)
    {
        super(Material.circuits);
        this.isPowered = isPowered;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos);
        BlockRailBase.EnumRailDirection var4 = var3.getBlock() == this ? (BlockRailBase.EnumRailDirection)var3.getValue(this.getShapeProperty()) : null;

        if (var4 != null && var4.isAscending())
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            state = this.func_176564_a(worldIn, pos, state, true);

            if (this.isPowered)
            {
                this.onNeighborBlockChange(worldIn, pos, state, this);
            }
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty());
            boolean var6 = false;

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
            {
                var6 = true;
            }

            if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.east()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.west()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.north()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.south()))
            {
                var6 = true;
            }

            if (var6)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else
            {
                this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
            }
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}

    protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_)
    {
        return worldIn.isRemote ? p_176564_3_ : (new BlockRailBase.Rail(worldIn, p_176564_2_, p_176564_3_)).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
    }

    @Override
	public int getMobilityFlag()
    {
        return 0;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (((BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty())).isAscending())
        {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this);
        }

        if (this.isPowered)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        }
    }

    public abstract IProperty getShapeProperty();

    public static enum EnumRailDirection implements IStringSerializable
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
        private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
        private final int meta;
        private final String name; 

        private EnumRailDirection(String p_i45738_1_, int p_i45738_2_, int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        public boolean isAscending()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        static {
            BlockRailBase.EnumRailDirection[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockRailBase.EnumRailDirection var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }

    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List field_150657_g = Lists.newArrayList();

        public Rail(World worldIn, BlockPos pos, IBlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)state.getValue(BlockRailBase.this.getShapeProperty());
            this.isPowered = this.block.isPowered;
            this.func_180360_a(var5);
        }

        private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_)
        {
            this.field_150657_g.clear();

            switch (BlockRailBase.SwitchEnumRailDirection.DIRECTION_LOOKUP[p_180360_1_.ordinal()])
            {
                case 1:
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south());
                    break;

                case 2:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east());
                    break;

                case 3:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east().up());
                    break;

                case 4:
                    this.field_150657_g.add(this.pos.west().up());
                    this.field_150657_g.add(this.pos.east());
                    break;

                case 5:
                    this.field_150657_g.add(this.pos.north().up());
                    this.field_150657_g.add(this.pos.south());
                    break;

                case 6:
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south().up());
                    break;

                case 7:
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.south());
                    break;

                case 8:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.south());
                    break;

                case 9:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.north());
                    break;

                case 10:
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.north());
            }
        }

        private void func_150651_b()
        {
            for (int var1 = 0; var1 < this.field_150657_g.size(); ++var1)
            {
                BlockRailBase.Rail var2 = this.findRailAt((BlockPos)this.field_150657_g.get(var1));

                if (var2 != null && var2.func_150653_a(this))
                {
                    this.field_150657_g.set(var1, var2.pos);
                }
                else
                {
                    this.field_150657_g.remove(var1--);
                }
            }
        }

        private boolean hasRailAt(BlockPos pos)
        {
            return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down());
        }

        private BlockRailBase.Rail findRailAt(BlockPos pos)
        {
            IBlockState var3 = this.world.getBlockState(pos);

            if (BlockRailBase.isRailBlock(var3))
            {
                return BlockRailBase.this.new Rail(this.world, pos, var3);
            }
            else
            {
                BlockPos var2 = pos.up();
                var3 = this.world.getBlockState(var2);

                if (BlockRailBase.isRailBlock(var3))
                {
                    return BlockRailBase.this.new Rail(this.world, var2, var3);
                }
                else
                {
                    var2 = pos.down();
                    var3 = this.world.getBlockState(var2);
                    return BlockRailBase.isRailBlock(var3) ? BlockRailBase.this.new Rail(this.world, var2, var3) : null;
                }
            }
        }

        private boolean func_150653_a(BlockRailBase.Rail p_150653_1_)
        {
            return this.func_180363_c(p_150653_1_.pos);
        }

        private boolean func_180363_c(BlockPos p_180363_1_)
        {
            for (int var2 = 0; var2 < this.field_150657_g.size(); ++var2)
            {
                BlockPos var3 = (BlockPos)this.field_150657_g.get(var2);

                if (var3.getX() == p_180363_1_.getX() && var3.getZ() == p_180363_1_.getZ())
                {
                    return true;
                }
            }

            return false;
        }

        protected int countAdjacentRails()
        {
            int var1 = 0;
            Iterator var2 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var2.hasNext())
            {
                EnumFacing var3 = (EnumFacing)var2.next();

                if (this.hasRailAt(this.pos.offset(var3)))
                {
                    ++var1;
                }
            }

            return var1;
        }

        private boolean func_150649_b(BlockRailBase.Rail rail)
        {
            return this.func_150653_a(rail) || this.field_150657_g.size() != 2;
        }

        private void func_150645_c(BlockRailBase.Rail p_150645_1_)
        {
            this.field_150657_g.add(p_150645_1_.pos);
            BlockPos var2 = this.pos.north();
            BlockPos var3 = this.pos.south();
            BlockPos var4 = this.pos.west();
            BlockPos var5 = this.pos.east();
            boolean var6 = this.func_180363_c(var2);
            boolean var7 = this.func_180363_c(var3);
            boolean var8 = this.func_180363_c(var4);
            boolean var9 = this.func_180363_c(var5);
            BlockRailBase.EnumRailDirection var10 = null;

            if (var6 || var7)
            {
                var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (var8 || var9)
            {
                var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (var7 && var9 && !var6 && !var8)
                {
                    var10 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (var7 && var8 && !var6 && !var9)
                {
                    var10 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (var6 && var8 && !var7 && !var9)
                {
                    var10 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (var6 && var9 && !var7 && !var8)
                {
                    var10 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (var10 == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (BlockRailBase.isRailBlock(this.world, var2.up()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, var3.up()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (var10 == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.isRailBlock(this.world, var5.up()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, var4.up()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (var10 == null)
            {
                var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.state = this.state.withProperty(this.block.getShapeProperty(), var10);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean func_180361_d(BlockPos p_180361_1_)
        {
            BlockRailBase.Rail var2 = this.findRailAt(p_180361_1_);

            if (var2 == null)
            {
                return false;
            }
            else
            {
                var2.func_150651_b();
                return var2.func_150649_b(this);
            }
        }

        public BlockRailBase.Rail func_180364_a(boolean p_180364_1_, boolean p_180364_2_)
        {
            BlockPos var3 = this.pos.north();
            BlockPos var4 = this.pos.south();
            BlockPos var5 = this.pos.west();
            BlockPos var6 = this.pos.east();
            boolean var7 = this.func_180361_d(var3);
            boolean var8 = this.func_180361_d(var4);
            boolean var9 = this.func_180361_d(var5);
            boolean var10 = this.func_180361_d(var6);
            BlockRailBase.EnumRailDirection var11 = null;

            if ((var7 || var8) && !var9 && !var10)
            {
                var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if ((var9 || var10) && !var7 && !var8)
            {
                var11 = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (var8 && var10 && !var7 && !var9)
                {
                    var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (var8 && var9 && !var7 && !var10)
                {
                    var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (var7 && var9 && !var8 && !var10)
                {
                    var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (var7 && var10 && !var8 && !var9)
                {
                    var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (var11 == null)
            {
                if (var7 || var8)
                {
                    var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                }

                if (var9 || var10)
                {
                    var11 = BlockRailBase.EnumRailDirection.EAST_WEST;
                }

                if (!this.isPowered)
                {
                    if (p_180364_1_)
                    {
                        if (var8 && var10)
                        {
                            var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }

                        if (var9 && var8)
                        {
                            var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (var10 && var7)
                        {
                            var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (var7 && var9)
                        {
                            var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else
                    {
                        if (var7 && var9)
                        {
                            var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }

                        if (var10 && var7)
                        {
                            var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (var9 && var8)
                        {
                            var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (var8 && var10)
                        {
                            var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (var11 == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (BlockRailBase.isRailBlock(this.world, var3.up()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, var4.up()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (var11 == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.isRailBlock(this.world, var6.up()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, var5.up()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (var11 == null)
            {
                var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.func_180360_a(var11);
            this.state = this.state.withProperty(this.block.getShapeProperty(), var11);

            if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state)
            {
                this.world.setBlockState(this.pos, this.state, 3);

                for (int var12 = 0; var12 < this.field_150657_g.size(); ++var12)
                {
                    BlockRailBase.Rail var13 = this.findRailAt((BlockPos)this.field_150657_g.get(var12));

                    if (var13 != null)
                    {
                        var13.func_150651_b();

                        if (var13.func_150649_b(this))
                        {
                            var13.func_150645_c(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState()
        {
            return this.state;
        }
    }

    static final class SwitchEnumRailDirection
    {
        static final int[] DIRECTION_LOOKUP = new int[BlockRailBase.EnumRailDirection.values().length];

        static
        {
            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
