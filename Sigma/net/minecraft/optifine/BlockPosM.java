package net.minecraft.optifine;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockPosM extends BlockPos {
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;

    public BlockPosM(int x, int y, int z) {
        this(x, y, z, 0);
    }

    public BlockPosM(double xIn, double yIn, double zIn) {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    public BlockPosM(int x, int y, int z, int level) {
        super(0, 0, 0);
        mx = x;
        my = y;
        mz = z;
        this.level = level;
    }

    /**
     * Get the X coordinate
     */
    @Override
    public int getX() {
        return mx;
    }

    /**
     * Get the Y coordinate
     */
    @Override
    public int getY() {
        return my;
    }

    /**
     * Get the Z coordinate
     */
    @Override
    public int getZ() {
        return mz;
    }

    public void setXyz(int x, int y, int z) {
        mx = x;
        my = y;
        mz = z;
        needsUpdate = true;
    }

    public void setXyz(double xIn, double yIn, double zIn) {
        this.setXyz(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    /**
     * Offset this BlockPos 1 block in the given direction
     */
    @Override
    public BlockPos offset(EnumFacing facing) {
        if (level <= 0) {
            return super.offset(facing, 1);
        } else {
            if (facings == null) {
                facings = new BlockPosM[EnumFacing.VALUES.length];
            }

            if (needsUpdate) {
                update();
            }

            int index = facing.getIndex();
            BlockPosM bpm = facings[index];

            if (bpm == null) {
                int nx = mx + facing.getFrontOffsetX();
                int ny = my + facing.getFrontOffsetY();
                int nz = mz + facing.getFrontOffsetZ();
                bpm = new BlockPosM(nx, ny, nz, level - 1);
                facings[index] = bpm;
            }

            return bpm;
        }
    }

    /**
     * Offset this BlockPos n blocks in the given direction
     */
    @Override
    public BlockPos offset(EnumFacing facing, int n) {
        return n == 1 ? this.offset(facing) : super.offset(facing, 1);
    }

    private void update() {
        for (int i = 0; i < 6; ++i) {
            BlockPosM bpm = facings[i];

            if (bpm != null) {
                EnumFacing facing = EnumFacing.VALUES[i];
                int nx = mx + facing.getFrontOffsetX();
                int ny = my + facing.getFrontOffsetY();
                int nz = mz + facing.getFrontOffsetZ();
                bpm.setXyz(nx, ny, nz);
            }
        }

        needsUpdate = false;
    }

    public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to) {
        final BlockPos posFrom = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos posTo = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable() {
            @Override
            public Iterator iterator() {
                return new AbstractIterator() {
                    private BlockPosM theBlockPosM = null;

                    protected BlockPosM computeNext0() {
                        if (theBlockPosM == null) {
                            theBlockPosM = new BlockPosM(posFrom.getX(), posFrom.getY(), posFrom.getZ(), 3);
                            return theBlockPosM;
                        } else if (theBlockPosM.equals(posTo)) {
                            return (BlockPosM) endOfData();
                        } else {
                            int bx = theBlockPosM.getX();
                            int by = theBlockPosM.getY();
                            int bz = theBlockPosM.getZ();

                            if (bx < posTo.getX()) {
                                ++bx;
                            } else if (by < posTo.getY()) {
                                bx = posFrom.getX();
                                ++by;
                            } else if (bz < posTo.getZ()) {
                                bx = posFrom.getX();
                                by = posFrom.getY();
                                ++bz;
                            }

                            theBlockPosM.setXyz(bx, by, bz);
                            return theBlockPosM;
                        }
                    }

                    @Override
                    protected Object computeNext() {
                        return computeNext0();
                    }
                };
            }
        };
    }
}
