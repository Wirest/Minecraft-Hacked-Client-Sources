package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
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
    private static final String __OBFID = "CL_00000195";

    public static boolean func_176562_d(World worldIn, BlockPos p_176562_1_)
    {
        return func_176563_d(worldIn.getBlockState(p_176562_1_));
    }

    public static boolean func_176563_d(IBlockState p_176563_0_)
    {
        Block var1 = p_176563_0_.getBlock();
        return var1 == Blocks.rail || var1 == Blocks.golden_rail || var1 == Blocks.detector_rail || var1 == Blocks.activator_rail;
    }

    protected BlockRailBase(boolean p_i45389_1_)
    {
        super(Material.circuits);
        this.isPowered = p_i45389_1_;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

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
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        IBlockState var3 = access.getBlockState(pos);
        BlockRailBase.EnumRailDirection var4 = var3.getBlock() == this ? (BlockRailBase.EnumRailDirection)var3.getValue(this.func_176560_l()) : null;

        if (var4 != null && var4.func_177018_c())
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
    }

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

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)state.getValue(this.func_176560_l());
            boolean var6 = false;

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()))
            {
                var6 = true;
            }

            if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetEast()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetWest()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetNorth()))
            {
                var6 = true;
            }
            else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetSouth()))
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
                this.func_176561_b(worldIn, pos, state, neighborBlock);
            }
        }
    }

    protected void func_176561_b(World worldIn, BlockPos p_176561_2_, IBlockState p_176561_3_, Block p_176561_4_) {}

    protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_)
    {
        return worldIn.isRemote ? p_176564_3_ : (new BlockRailBase.Rail(worldIn, p_176564_2_, p_176564_3_)).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).func_180362_b();
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (((BlockRailBase.EnumRailDirection)state.getValue(this.func_176560_l())).func_177018_c())
        {
            worldIn.notifyNeighborsOfStateChange(pos.offsetUp(), this);
        }

        if (this.isPowered)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offsetDown(), this);
        }
    }

    public abstract IProperty func_176560_l();

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
        private static final BlockRailBase.EnumRailDirection[] field_177030_k = new BlockRailBase.EnumRailDirection[values().length];
        private final int field_177027_l;
        private final String field_177028_m;

        private static final BlockRailBase.EnumRailDirection[] $VALUES = new BlockRailBase.EnumRailDirection[]{NORTH_SOUTH, EAST_WEST, ASCENDING_EAST, ASCENDING_WEST, ASCENDING_NORTH, ASCENDING_SOUTH, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST};
        private static final String __OBFID = "CL_00002137";

        private EnumRailDirection(String p_i45738_1_, int p_i45738_2_, int p_i45738_3_, String p_i45738_4_)
        {
            this.field_177027_l = p_i45738_3_;
            this.field_177028_m = p_i45738_4_;
        }

        public int func_177015_a()
        {
            return this.field_177027_l;
        }

        public String toString()
        {
            return this.field_177028_m;
        }

        public boolean func_177018_c()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection func_177016_a(int p_177016_0_)
        {
            if (p_177016_0_ < 0 || p_177016_0_ >= field_177030_k.length)
            {
                p_177016_0_ = 0;
            }

            return field_177030_k[p_177016_0_];
        }

        public String getName()
        {
            return this.field_177028_m;
        }

        static {
            BlockRailBase.EnumRailDirection[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockRailBase.EnumRailDirection var3 = var0[var2];
                field_177030_k[var3.func_177015_a()] = var3;
            }
        }
    }

    public class Rail
    {
        private final World field_150660_b;
        private final BlockPos field_180367_c;
        private final BlockRailBase field_180365_d;
        private IBlockState field_180366_e;
        private final boolean field_150656_f;
        private final List field_150657_g = Lists.newArrayList();
        private static final String __OBFID = "CL_00000196";

        public Rail(World worldIn, BlockPos p_i45739_3_, IBlockState p_i45739_4_)
        {
            this.field_150660_b = worldIn;
            this.field_180367_c = p_i45739_3_;
            this.field_180366_e = p_i45739_4_;
            this.field_180365_d = (BlockRailBase)p_i45739_4_.getBlock();
            BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)p_i45739_4_.getValue(BlockRailBase.this.func_176560_l());
            this.field_150656_f = this.field_180365_d.isPowered;
            this.func_180360_a(var5);
        }

        private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_)
        {
            this.field_150657_g.clear();

            switch (BlockRailBase.SwitchEnumRailDirection.field_180371_a[p_180360_1_.ordinal()])
            {
                case 1:
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;

                case 2:
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    break;

                case 3:
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetEast().offsetUp());
                    break;

                case 4:
                    this.field_150657_g.add(this.field_180367_c.offsetWest().offsetUp());
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    break;

                case 5:
                    this.field_150657_g.add(this.field_180367_c.offsetNorth().offsetUp());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;

                case 6:
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth().offsetUp());
                    break;

                case 7:
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;

                case 8:
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;

                case 9:
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    break;

                case 10:
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
            }
        }

        private void func_150651_b()
        {
            for (int var1 = 0; var1 < this.field_150657_g.size(); ++var1)
            {
                BlockRailBase.Rail var2 = this.func_180697_b((BlockPos)this.field_150657_g.get(var1));

                if (var2 != null && var2.func_150653_a(this))
                {
                    this.field_150657_g.set(var1, var2.field_180367_c);
                }
                else
                {
                    this.field_150657_g.remove(var1--);
                }
            }
        }

        private boolean func_180359_a(BlockPos p_180359_1_)
        {
            return BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_) || BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_.offsetUp()) || BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_.offsetDown());
        }

        private BlockRailBase.Rail func_180697_b(BlockPos p_180697_1_)
        {
            IBlockState var3 = this.field_150660_b.getBlockState(p_180697_1_);

            if (BlockRailBase.func_176563_d(var3))
            {
                return BlockRailBase.this.new Rail(this.field_150660_b, p_180697_1_, var3);
            }
            else
            {
                BlockPos var2 = p_180697_1_.offsetUp();
                var3 = this.field_150660_b.getBlockState(var2);

                if (BlockRailBase.func_176563_d(var3))
                {
                    return BlockRailBase.this.new Rail(this.field_150660_b, var2, var3);
                }
                else
                {
                    var2 = p_180697_1_.offsetDown();
                    var3 = this.field_150660_b.getBlockState(var2);
                    return BlockRailBase.func_176563_d(var3) ? BlockRailBase.this.new Rail(this.field_150660_b, var2, var3) : null;
                }
            }
        }

        private boolean func_150653_a(BlockRailBase.Rail p_150653_1_)
        {
            return this.func_180363_c(p_150653_1_.field_180367_c);
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

                if (this.func_180359_a(this.field_180367_c.offset(var3)))
                {
                    ++var1;
                }
            }

            return var1;
        }

        private boolean func_150649_b(BlockRailBase.Rail p_150649_1_)
        {
            return this.func_150653_a(p_150649_1_) || this.field_150657_g.size() != 2;
        }

        private void func_150645_c(BlockRailBase.Rail p_150645_1_)
        {
            this.field_150657_g.add(p_150645_1_.field_180367_c);
            BlockPos var2 = this.field_180367_c.offsetNorth();
            BlockPos var3 = this.field_180367_c.offsetSouth();
            BlockPos var4 = this.field_180367_c.offsetWest();
            BlockPos var5 = this.field_180367_c.offsetEast();
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

            if (!this.field_150656_f)
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
                if (BlockRailBase.func_176562_d(this.field_150660_b, var2.offsetUp()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, var3.offsetUp()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (var10 == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var5.offsetUp()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, var4.offsetUp()))
                {
                    var10 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (var10 == null)
            {
                var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.field_180366_e = this.field_180366_e.withProperty(this.field_180365_d.func_176560_l(), var10);
            this.field_150660_b.setBlockState(this.field_180367_c, this.field_180366_e, 3);
        }

        private boolean func_180361_d(BlockPos p_180361_1_)
        {
            BlockRailBase.Rail var2 = this.func_180697_b(p_180361_1_);

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
            BlockPos var3 = this.field_180367_c.offsetNorth();
            BlockPos var4 = this.field_180367_c.offsetSouth();
            BlockPos var5 = this.field_180367_c.offsetWest();
            BlockPos var6 = this.field_180367_c.offsetEast();
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

            if (!this.field_150656_f)
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

                if (!this.field_150656_f)
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
                if (BlockRailBase.func_176562_d(this.field_150660_b, var3.offsetUp()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, var4.offsetUp()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (var11 == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var6.offsetUp()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, var5.offsetUp()))
                {
                    var11 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (var11 == null)
            {
                var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.func_180360_a(var11);
            this.field_180366_e = this.field_180366_e.withProperty(this.field_180365_d.func_176560_l(), var11);

            if (p_180364_2_ || this.field_150660_b.getBlockState(this.field_180367_c) != this.field_180366_e)
            {
                this.field_150660_b.setBlockState(this.field_180367_c, this.field_180366_e, 3);

                for (int var12 = 0; var12 < this.field_150657_g.size(); ++var12)
                {
                    BlockRailBase.Rail var13 = this.func_180697_b((BlockPos)this.field_150657_g.get(var12));

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

        public IBlockState func_180362_b()
        {
            return this.field_180366_e;
        }
    }

    static final class SwitchEnumRailDirection
    {
        static final int[] field_180371_a = new int[BlockRailBase.EnumRailDirection.values().length];
        private static final String __OBFID = "CL_00002138";

        static
        {
            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_180371_a[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
