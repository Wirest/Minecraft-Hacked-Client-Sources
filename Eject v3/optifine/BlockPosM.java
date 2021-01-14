package optifine;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.Iterator;

public class BlockPosM
        extends BlockPos {
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;

    public BlockPosM(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, paramInt3, 0);
    }

    public BlockPosM(double paramDouble1, double paramDouble2, double paramDouble3) {
        this(MathHelper.floor_double(paramDouble1), MathHelper.floor_double(paramDouble2), MathHelper.floor_double(paramDouble3));
    }

    public BlockPosM(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super(0, 0, 0);
        this.mx = paramInt1;
        this.my = paramInt2;
        this.mz = paramInt3;
        this.level = paramInt4;
    }

    public static Iterable getAllInBoxMutable(BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
        BlockPos localBlockPos1 = new BlockPos(Math.min(paramBlockPos1.getX(), paramBlockPos2.getX()), Math.min(paramBlockPos1.getY(), paramBlockPos2.getY()), Math.min(paramBlockPos1.getZ(), paramBlockPos2.getZ()));
        final BlockPos localBlockPos2 = new BlockPos(Math.max(paramBlockPos1.getX(), paramBlockPos2.getX()), Math.max(paramBlockPos1.getY(), paramBlockPos2.getY()), Math.max(paramBlockPos1.getZ(), paramBlockPos2.getZ()));
        new Iterable() {
            public Iterator iterator() {
                new AbstractIterator() {
                    private BlockPosM theBlockPosM = null;

                    protected BlockPosM computeNext0() {
                        if (this.theBlockPosM == null) {
                            this.theBlockPosM = new BlockPosM(BlockPosM .1. this.val$blockpos.getX(), BlockPosM .1.
                            this.val$blockpos.getY(), BlockPosM .1. this.val$blockpos.getZ(), 3);
                            return this.theBlockPosM;
                        }
                        if (this.theBlockPosM.equals(BlockPosM .1. this.val$blockpos1)){
                            return (BlockPosM) endOfData();
                        }
                        int i = this.theBlockPosM.getX();
                        int j = this.theBlockPosM.getY();
                        int k = this.theBlockPosM.getZ();
                        if (i < BlockPosM .1. this.val$blockpos1.getX())
                        {
                            i++;
                        }
            else if (j < BlockPosM .1. this.val$blockpos1.getY())
                        {
                            i = BlockPosM .1. this.val$blockpos.getX();
                            j++;
                        }
            else if (k < BlockPosM .1. this.val$blockpos1.getZ())
                        {
                            i = BlockPosM .1. this.val$blockpos.getX();
                            j = BlockPosM .1. this.val$blockpos.getY();
                            k++;
                        }
                        this.theBlockPosM.setXyz(i, j, k);
                        return this.theBlockPosM;
                    }

                    protected Object computeNext() {
                        return computeNext0();
                    }
                };
            }
        };
    }

    public int getX() {
        return this.mx;
    }

    public int getY() {
        return this.my;
    }

    public int getZ() {
        return this.mz;
    }

    public void setXyz(int paramInt1, int paramInt2, int paramInt3) {
        this.mx = paramInt1;
        this.my = paramInt2;
        this.mz = paramInt3;
        this.needsUpdate = true;
    }

    public void setXyz(double paramDouble1, double paramDouble2, double paramDouble3) {
        setXyz(MathHelper.floor_double(paramDouble1), MathHelper.floor_double(paramDouble2), MathHelper.floor_double(paramDouble3));
    }

    public BlockPos offset(EnumFacing paramEnumFacing) {
        if (this.level <= 0) {
            return super.offset(paramEnumFacing, 1);
        }
        if (this.facings == null) {
            this.facings = new BlockPosM[EnumFacing.VALUES.length];
        }
        if (this.needsUpdate) {
            update();
        }
        int i = paramEnumFacing.getIndex();
        BlockPosM localBlockPosM = this.facings[i];
        if (localBlockPosM == null) {
            int j = this.mx | paramEnumFacing.getFrontOffsetX();
            int k = this.my | paramEnumFacing.getFrontOffsetY();
            int m = this.mz | paramEnumFacing.getFrontOffsetZ();
            localBlockPosM = new BlockPosM(j, k, m, this.level - 1);
            this.facings[i] = localBlockPosM;
        }
        return localBlockPosM;
    }

    public BlockPos offset(EnumFacing paramEnumFacing, int paramInt) {
        return paramInt == 1 ? offset(paramEnumFacing) : super.offset(paramEnumFacing, paramInt);
    }

    private void update() {
        for (int i = 0; i < 6; i++) {
            BlockPosM localBlockPosM = this.facings[i];
            if (localBlockPosM != null) {
                EnumFacing localEnumFacing = EnumFacing.VALUES[i];
                int j = this.mx | localEnumFacing.getFrontOffsetX();
                int k = this.my | localEnumFacing.getFrontOffsetY();
                int m = this.mz | localEnumFacing.getFrontOffsetZ();
                localBlockPosM.setXyz(j, k, m);
            }
        }
        this.needsUpdate = false;
    }

    public BlockPos getImmutable() {
        return new BlockPos(getX(), getY(), getZ());
    }
}




