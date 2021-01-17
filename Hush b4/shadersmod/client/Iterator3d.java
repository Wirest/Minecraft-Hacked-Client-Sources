// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.util.Vec3;
import optifine.BlockPosM;
import net.minecraft.util.BlockPos;
import java.util.Iterator;

public class Iterator3d implements Iterator<BlockPos>
{
    private IteratorAxis iteratorAxis;
    private BlockPosM blockPos;
    private int axis;
    private int kX;
    private int kY;
    private int kZ;
    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 1;
    private static final int AXIS_Z = 2;
    
    public Iterator3d(BlockPos posStart, BlockPos posEnd, final int width, final int height) {
        this.blockPos = new BlockPosM(0, 0, 0);
        this.axis = 0;
        final boolean flag = posStart.getX() > posEnd.getX();
        final boolean flag2 = posStart.getY() > posEnd.getY();
        final boolean flag3 = posStart.getZ() > posEnd.getZ();
        posStart = this.reverseCoord(posStart, flag, flag2, flag3);
        posEnd = this.reverseCoord(posEnd, flag, flag2, flag3);
        this.kX = (flag ? -1 : 1);
        this.kY = (flag2 ? -1 : 1);
        this.kZ = (flag3 ? -1 : 1);
        final Vec3 vec3 = new Vec3(posEnd.getX() - posStart.getX(), posEnd.getY() - posStart.getY(), posEnd.getZ() - posStart.getZ());
        final Vec3 vec4 = vec3.normalize();
        final Vec3 vec5 = new Vec3(1.0, 0.0, 0.0);
        final double d0 = vec4.dotProduct(vec5);
        final double d2 = Math.abs(d0);
        final Vec3 vec6 = new Vec3(0.0, 1.0, 0.0);
        final double d3 = vec4.dotProduct(vec6);
        final double d4 = Math.abs(d3);
        final Vec3 vec7 = new Vec3(0.0, 0.0, 1.0);
        final double d5 = vec4.dotProduct(vec7);
        final double d6 = Math.abs(d5);
        if (d6 >= d4 && d6 >= d2) {
            this.axis = 2;
            final BlockPos blockpos3 = new BlockPos(posStart.getZ(), posStart.getY() - width, posStart.getX() - height);
            final BlockPos blockpos4 = new BlockPos(posEnd.getZ(), posStart.getY() + width + 1, posStart.getX() + height + 1);
            final int k = posEnd.getZ() - posStart.getZ();
            final double d7 = (posEnd.getY() - posStart.getY()) / (1.0 * k);
            final double d8 = (posEnd.getX() - posStart.getX()) / (1.0 * k);
            this.iteratorAxis = new IteratorAxis(blockpos3, blockpos4, d7, d8);
        }
        else if (d4 >= d2 && d4 >= d6) {
            this.axis = 1;
            final BlockPos blockpos5 = new BlockPos(posStart.getY(), posStart.getX() - width, posStart.getZ() - height);
            final BlockPos blockpos6 = new BlockPos(posEnd.getY(), posStart.getX() + width + 1, posStart.getZ() + height + 1);
            final int j = posEnd.getY() - posStart.getY();
            final double d9 = (posEnd.getX() - posStart.getX()) / (1.0 * j);
            final double d10 = (posEnd.getZ() - posStart.getZ()) / (1.0 * j);
            this.iteratorAxis = new IteratorAxis(blockpos5, blockpos6, d9, d10);
        }
        else {
            this.axis = 0;
            final BlockPos blockpos7 = new BlockPos(posStart.getX(), posStart.getY() - width, posStart.getZ() - height);
            final BlockPos blockpos8 = new BlockPos(posEnd.getX(), posStart.getY() + width + 1, posStart.getZ() + height + 1);
            final int i = posEnd.getX() - posStart.getX();
            final double d11 = (posEnd.getY() - posStart.getY()) / (1.0 * i);
            final double d12 = (posEnd.getZ() - posStart.getZ()) / (1.0 * i);
            this.iteratorAxis = new IteratorAxis(blockpos7, blockpos8, d11, d12);
        }
    }
    
    private BlockPos reverseCoord(BlockPos pos, final boolean revX, final boolean revY, final boolean revZ) {
        if (revX) {
            pos = new BlockPos(-pos.getX(), pos.getY(), pos.getZ());
        }
        if (revY) {
            pos = new BlockPos(pos.getX(), -pos.getY(), pos.getZ());
        }
        if (revZ) {
            pos = new BlockPos(pos.getX(), pos.getY(), -pos.getZ());
        }
        return pos;
    }
    
    @Override
    public boolean hasNext() {
        return this.iteratorAxis.hasNext();
    }
    
    @Override
    public BlockPos next() {
        final BlockPos blockpos = this.iteratorAxis.next();
        switch (this.axis) {
            case 0: {
                this.blockPos.setXyz(blockpos.getX() * this.kX, blockpos.getY() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;
            }
            case 1: {
                this.blockPos.setXyz(blockpos.getY() * this.kX, blockpos.getX() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;
            }
            case 2: {
                this.blockPos.setXyz(blockpos.getZ() * this.kX, blockpos.getY() * this.kY, blockpos.getX() * this.kZ);
                return this.blockPos;
            }
            default: {
                this.blockPos.setXyz(blockpos.getX() * this.kX, blockpos.getY() * this.kY, blockpos.getZ() * this.kZ);
                return this.blockPos;
            }
        }
    }
    
    @Override
    public void remove() {
        throw new RuntimeException("Not supported");
    }
    
    public static void main(final String[] args) {
        final BlockPos blockpos = new BlockPos(10, 20, 30);
        final BlockPos blockpos2 = new BlockPos(30, 40, 20);
        final Iterator3d iterator3d = new Iterator3d(blockpos, blockpos2, 1, 1);
        while (iterator3d.hasNext()) {
            final BlockPos blockpos3 = iterator3d.next();
            System.out.println(new StringBuilder().append(blockpos3).toString());
        }
    }
}
