package net.minecraft.util;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import net.minecraft.entity.Entity;

public class BlockPos extends Vec3i
{
    /** The BlockPos with all coordinates 0 */
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public BlockPos(int x, int y, int z)
    {
        super(x, y, z);
    }

    public BlockPos(double x, double y, double z)
    {
        super(x, y, z);
    }

    public BlockPos(Entity source)
    {
        this(source.posX, source.posY, source.posZ);
    }

    public BlockPos(Vec3 source)
    {
        this(source.xCoord, source.yCoord, source.zCoord);
    }

    public BlockPos(Vec3i source)
    {
        this(source.getX(), source.getY(), source.getZ());
    }

    /**
     * Add the given coordinates to the coordinates of this BlockPos
     *  
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public BlockPos add(double x, double y, double z)
    {
        return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    /**
     * Add the given coordinates to the coordinates of this BlockPos
     *  
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public BlockPos add(int x, int y, int z)
    {
        return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    /**
     * Add the given Vector to this BlockPos
     */
    public BlockPos add(Vec3i vec)
    {
        return new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }

    /**
     * Subtract the given Vector from this BlockPos
     */
    public BlockPos subtract(Vec3i vec)
    {
        return new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }

    /**
     * Multiply every coordinate by the given factor
     */
    public BlockPos multiply(int factor)
    {
        return new BlockPos(this.getX() * factor, this.getY() * factor, this.getZ() * factor);
    }

    /**
     * Offset this BlockPos 1 block up
     */
    public BlockPos up()
    {
        return this.up(1);
    }

    /**
     * Offset this BlockPos n blocks up
     */
    public BlockPos up(int n)
    {
        return this.offset(EnumFacing.UP, n);
    }

    /**
     * Offset this BlockPos 1 block down
     */
    public BlockPos down()
    {
        return this.down(1);
    }

    /**
     * Offset this BlockPos n blocks down
     */
    public BlockPos down(int n)
    {
        return this.offset(EnumFacing.DOWN, n);
    }

    /**
     * Offset this BlockPos 1 block in northern direction
     */
    public BlockPos north()
    {
        return this.north(1);
    }

    /**
     * Offset this BlockPos n blocks in northern direction
     */
    public BlockPos north(int n)
    {
        return this.offset(EnumFacing.NORTH, n);
    }

    /**
     * Offset this BlockPos 1 block in southern direction
     */
    public BlockPos south()
    {
        return this.south(1);
    }

    /**
     * Offset this BlockPos n blocks in southern direction
     */
    public BlockPos south(int n)
    {
        return this.offset(EnumFacing.SOUTH, n);
    }

    /**
     * Offset this BlockPos 1 block in western direction
     */
    public BlockPos west()
    {
        return this.west(1);
    }

    /**
     * Offset this BlockPos n blocks in western direction
     */
    public BlockPos west(int n)
    {
        return this.offset(EnumFacing.WEST, n);
    }

    /**
     * Offset this BlockPos 1 block in eastern direction
     */
    public BlockPos east()
    {
        return this.east(1);
    }

    /**
     * Offset this BlockPos n blocks in eastern direction
     */
    public BlockPos east(int n)
    {
        return this.offset(EnumFacing.EAST, n);
    }

    /**
     * Offset this BlockPos 1 block in the given direction
     */
    public BlockPos offset(EnumFacing facing)
    {
        return this.offset(facing, 1);
    }

    /**
     * Offsets this BlockPos n blocks in the given direction
     *  
     * @param facing The direction of the offset
     * @param n The number of blocks to offset by
     */
    public BlockPos offset(EnumFacing facing, int n)
    {
        return new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }

    /**
     * Calculate the cross product of this BlockPos and the given Vector. Version of crossProduct that returns a
     * BlockPos instead of a Vec3i
     */
    public BlockPos crossProductBP(Vec3i vec)
    {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    /**
     * Serialize this BlockPos into a long value
     */
    public long toLong()
    {
        return (this.getX() & X_MASK) << X_SHIFT | (this.getY() & Y_MASK) << Y_SHIFT | (this.getZ() & Z_MASK) << 0;
    }

    /**
     * Create a BlockPos from a serialized long value (created by toLong)
     */
    public static BlockPos fromLong(long serialized)
    {
        int var2 = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int var3 = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int var4 = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(var2, var3, var4);
    }

    /**
     * Create an Iterable that returns all positions in the box specified by the given corners
     *  
     * @param from The first corner (inclusive)
     * @param to the second corner (exclusive)
     */
    public static Iterable getAllInBox(BlockPos from, BlockPos to)
    {
        final BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable()
        {
            @Override
			public Iterator iterator()
            {
                return new AbstractIterator()
                {
                    private BlockPos lastReturned = null;
                    protected BlockPos computeNext0()
                    {
                        if (this.lastReturned == null)
                        {
                            this.lastReturned = var2;
                            return this.lastReturned;
                        }
                        else if (this.lastReturned.equals(var3))
                        {
                            return (BlockPos)this.endOfData();
                        }
                        else
                        {
                            int var1 = this.lastReturned.getX();
                            int var2x = this.lastReturned.getY();
                            int var3x = this.lastReturned.getZ();

                            if (var1 < var3.getX())
                            {
                                ++var1;
                            }
                            else if (var2x < var3.getY())
                            {
                                var1 = var2.getX();
                                ++var2x;
                            }
                            else if (var3x < var3.getZ())
                            {
                                var1 = var2.getX();
                                var2x = var2.getY();
                                ++var3x;
                            }

                            this.lastReturned = new BlockPos(var1, var2x, var3x);
                            return this.lastReturned;
                        }
                    }
                    @Override
					protected Object computeNext()
                    {
                        return this.computeNext0();
                    }
                };
            }
        };
    }

    /**
     * Like getAllInBox but reuses a single MutableBlockPos instead. If this method is used, the resulting BlockPos
     * instances can only be used inside the iteration loop.
     *  
     * @param from The first corner (inclusive)
     * @param to the second corner (exclusive)
     */
    public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to)
    {
        final BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable()
        {
            @Override
			public Iterator iterator()
            {
                return new AbstractIterator()
                {
                    private BlockPos.MutableBlockPos theBlockPos = null;
                    protected BlockPos.MutableBlockPos computeNext0()
                    {
                        if (this.theBlockPos == null)
                        {
                            this.theBlockPos = new BlockPos.MutableBlockPos(var2.getX(), var2.getY(), var2.getZ(), null);
                            return this.theBlockPos;
                        }
                        else if (this.theBlockPos.equals(var3))
                        {
                            return (BlockPos.MutableBlockPos)this.endOfData();
                        }
                        else
                        {
                            int var1 = this.theBlockPos.getX();
                            int var2xx = this.theBlockPos.getY();
                            int var3x = this.theBlockPos.getZ();

                            if (var1 < var3.getX())
                            {
                                ++var1;
                            }
                            else if (var2xx < var3.getY())
                            {
                                var1 = var2.getX();
                                ++var2xx;
                            }
                            else if (var3x < var3.getZ())
                            {
                                var1 = var2.getX();
                                var2xx = var2.getY();
                                ++var3x;
                            }

                            this.theBlockPos.x = var1;
                            this.theBlockPos.y = var2xx;
                            this.theBlockPos.z = var3x;
                            return this.theBlockPos;
                        }
                    }
                    @Override
					protected Object computeNext()
                    {
                        return this.computeNext0();
                    }
                };
            }
        };
    }

    /**
     * Calculate the cross product of this and the given Vector
     */
    @Override
	public Vec3i crossProduct(Vec3i vec)
    {
        return this.crossProductBP(vec);
    }

    public static final class MutableBlockPos extends BlockPos
    {
        public int x;
        public int y;
        public int z;

        private MutableBlockPos(int x_, int y_, int z_)
        {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }

        @Override
		public int getX()
        {
            return this.x;
        }

        @Override
		public int getY()
        {
            return this.y;
        }

        @Override
		public int getZ()
        {
            return this.z;
        }

        @Override
		public Vec3i crossProduct(Vec3i vec)
        {
            return super.crossProductBP(vec);
        }

        MutableBlockPos(int p_i46025_1_, int p_i46025_2_, int p_i46025_3_, Object p_i46025_4_)
        {
            this(p_i46025_1_, p_i46025_2_, p_i46025_3_);
        }
    }
}
