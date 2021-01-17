// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;

public class BlockPos extends Vec3i
{
    public static final BlockPos ORIGIN;
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS;
    private static final int NUM_Y_BITS;
    private static final int Y_SHIFT;
    private static final int X_SHIFT;
    private static final long X_MASK;
    private static final long Y_MASK;
    private static final long Z_MASK;
    
    static {
        ORIGIN = new BlockPos(0, 0, 0);
        NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
        NUM_Z_BITS = BlockPos.NUM_X_BITS;
        NUM_Y_BITS = 64 - BlockPos.NUM_X_BITS - BlockPos.NUM_Z_BITS;
        Y_SHIFT = 0 + BlockPos.NUM_Z_BITS;
        X_SHIFT = BlockPos.Y_SHIFT + BlockPos.NUM_Y_BITS;
        X_MASK = (1L << BlockPos.NUM_X_BITS) - 1L;
        Y_MASK = (1L << BlockPos.NUM_Y_BITS) - 1L;
        Z_MASK = (1L << BlockPos.NUM_Z_BITS) - 1L;
    }
    
    public BlockPos(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public BlockPos(final double x, final double y, final double z) {
        super(x, y, z);
    }
    
    public BlockPos(final Entity source) {
        this(source.posX, source.posY, source.posZ);
    }
    
    public BlockPos(final Vec3 source) {
        this(source.xCoord, source.yCoord, source.zCoord);
    }
    
    public BlockPos(final Vec3i source) {
        this(source.getX(), source.getY(), source.getZ());
    }
    
    public BlockPos add(final double x, final double y, final double z) {
        return (x == 0.0 && y == 0.0 && z == 0.0) ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final int x, final int y, final int z) {
        return (x == 0 && y == 0 && z == 0) ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final Vec3i vec) {
        return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }
    
    public BlockPos subtract(final Vec3i vec) {
        return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }
    
    public BlockPos up() {
        return this.up(1);
    }
    
    public BlockPos up(final int n) {
        return this.offset(EnumFacing.UP, n);
    }
    
    public BlockPos down() {
        return this.down(1);
    }
    
    public BlockPos down(final int n) {
        return this.offset(EnumFacing.DOWN, n);
    }
    
    public BlockPos north() {
        return this.north(1);
    }
    
    public BlockPos north(final int n) {
        return this.offset(EnumFacing.NORTH, n);
    }
    
    public BlockPos south() {
        return this.south(1);
    }
    
    public BlockPos south(final int n) {
        return this.offset(EnumFacing.SOUTH, n);
    }
    
    public BlockPos west() {
        return this.west(1);
    }
    
    public BlockPos west(final int n) {
        return this.offset(EnumFacing.WEST, n);
    }
    
    public BlockPos east() {
        return this.east(1);
    }
    
    public BlockPos east(final int n) {
        return this.offset(EnumFacing.EAST, n);
    }
    
    public BlockPos offset(final EnumFacing facing) {
        return this.offset(facing, 1);
    }
    
    public BlockPos offset(final EnumFacing facing, final int n) {
        return (n == 0) ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }
    
    @Override
    public BlockPos crossProduct(final Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public long toLong() {
        return ((long)this.getX() & BlockPos.X_MASK) << BlockPos.X_SHIFT | ((long)this.getY() & BlockPos.Y_MASK) << BlockPos.Y_SHIFT | ((long)this.getZ() & BlockPos.Z_MASK) << 0;
    }
    
    public static BlockPos fromLong(final long serialized) {
        final int i = (int)(serialized << 64 - BlockPos.X_SHIFT - BlockPos.NUM_X_BITS >> 64 - BlockPos.NUM_X_BITS);
        final int j = (int)(serialized << 64 - BlockPos.Y_SHIFT - BlockPos.NUM_Y_BITS >> 64 - BlockPos.NUM_Y_BITS);
        final int k = (int)(serialized << 64 - BlockPos.NUM_Z_BITS >> 64 - BlockPos.NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }
    
    public static Iterable<BlockPos> getAllInBox(final BlockPos from, final BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos2 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<BlockPos>() {
            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator<BlockPos>() {
                    private BlockPos lastReturned = null;
                    
                    @Override
                    protected BlockPos computeNext() {
                        if (this.lastReturned == null) {
                            return this.lastReturned = blockpos;
                        }
                        if (this.lastReturned.equals(blockpos2)) {
                            return this.endOfData();
                        }
                        int i = this.lastReturned.getX();
                        int j = this.lastReturned.getY();
                        int k = this.lastReturned.getZ();
                        if (i < blockpos2.getX()) {
                            ++i;
                        }
                        else if (j < blockpos2.getY()) {
                            i = blockpos.getX();
                            ++j;
                        }
                        else if (k < blockpos2.getZ()) {
                            i = blockpos.getX();
                            j = blockpos.getY();
                            ++k;
                        }
                        return this.lastReturned = new BlockPos(i, j, k);
                    }
                };
            }
        };
    }
    
    public static Iterable<MutableBlockPos> getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos2 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<MutableBlockPos>() {
            @Override
            public Iterator<MutableBlockPos> iterator() {
                return new AbstractIterator<MutableBlockPos>() {
                    private MutableBlockPos theBlockPos = null;
                    
                    @Override
                    protected MutableBlockPos computeNext() {
                        if (this.theBlockPos == null) {
                            return this.theBlockPos = new MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                        }
                        if (this.theBlockPos.equals(blockpos2)) {
                            return this.endOfData();
                        }
                        int i = this.theBlockPos.getX();
                        int j = this.theBlockPos.getY();
                        int k = this.theBlockPos.getZ();
                        if (i < blockpos2.getX()) {
                            ++i;
                        }
                        else if (j < blockpos2.getY()) {
                            i = blockpos.getX();
                            ++j;
                        }
                        else if (k < blockpos2.getZ()) {
                            i = blockpos.getX();
                            j = blockpos.getY();
                            ++k;
                        }
                        MutableBlockPos.access$0(this.theBlockPos, i);
                        MutableBlockPos.access$1(this.theBlockPos, j);
                        MutableBlockPos.access$2(this.theBlockPos, k);
                        return this.theBlockPos;
                    }
                };
            }
        };
    }
    
    public static final class MutableBlockPos extends BlockPos
    {
        private int x;
        private int y;
        private int z;
        
        public MutableBlockPos() {
            this(0, 0, 0);
        }
        
        public MutableBlockPos(final int x_, final int y_, final int z_) {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }
        
        @Override
        public int getX() {
            return this.x;
        }
        
        @Override
        public int getY() {
            return this.y;
        }
        
        @Override
        public int getZ() {
            return this.z;
        }
        
        public MutableBlockPos func_181079_c(final int p_181079_1_, final int p_181079_2_, final int p_181079_3_) {
            this.x = p_181079_1_;
            this.y = p_181079_2_;
            this.z = p_181079_3_;
            return this;
        }
        
        static /* synthetic */ void access$0(final MutableBlockPos mutableBlockPos, final int x) {
            mutableBlockPos.x = x;
        }
        
        static /* synthetic */ void access$1(final MutableBlockPos mutableBlockPos, final int y) {
            mutableBlockPos.y = y;
        }
        
        static /* synthetic */ void access$2(final MutableBlockPos mutableBlockPos, final int z) {
            mutableBlockPos.z = z;
        }
    }
}
