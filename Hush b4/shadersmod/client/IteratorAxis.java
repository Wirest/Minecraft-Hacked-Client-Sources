// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.util.NoSuchElementException;
import optifine.BlockPosM;
import net.minecraft.util.BlockPos;
import java.util.Iterator;

public class IteratorAxis implements Iterator<BlockPos>
{
    private double yDelta;
    private double zDelta;
    private int xStart;
    private int xEnd;
    private double yStart;
    private double yEnd;
    private double zStart;
    private double zEnd;
    private int xNext;
    private double yNext;
    private double zNext;
    private BlockPosM pos;
    private boolean hasNext;
    
    public IteratorAxis(final BlockPos posStart, final BlockPos posEnd, final double yDelta, final double zDelta) {
        this.pos = new BlockPosM(0, 0, 0);
        this.hasNext = false;
        this.yDelta = yDelta;
        this.zDelta = zDelta;
        this.xStart = posStart.getX();
        this.xEnd = posEnd.getX();
        this.yStart = posStart.getY();
        this.yEnd = posEnd.getY() - 0.5;
        this.zStart = posStart.getZ();
        this.zEnd = posEnd.getZ() - 0.5;
        this.xNext = this.xStart;
        this.yNext = this.yStart;
        this.zNext = this.zStart;
        this.hasNext = (this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd);
    }
    
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public BlockPos next() {
        if (!this.hasNext) {
            throw new NoSuchElementException();
        }
        this.pos.setXyz(this.xNext, this.yNext, this.zNext);
        this.nextPos();
        this.hasNext = (this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd);
        return this.pos;
    }
    
    private void nextPos() {
        ++this.zNext;
        if (this.zNext >= this.zEnd) {
            this.zNext = this.zStart;
            ++this.yNext;
            if (this.yNext >= this.yEnd) {
                this.yNext = this.yStart;
                this.yStart += this.yDelta;
                this.yEnd += this.yDelta;
                this.yNext = this.yStart;
                this.zStart += this.zDelta;
                this.zEnd += this.zDelta;
                this.zNext = this.zStart;
                ++this.xNext;
                if (this.xNext >= this.xEnd) {}
            }
        }
    }
    
    @Override
    public void remove() {
        throw new RuntimeException("Not implemented");
    }
    
    public static void main(final String[] args) throws Exception {
        final BlockPos blockpos = new BlockPos(-2, 10, 20);
        final BlockPos blockpos2 = new BlockPos(2, 12, 22);
        final double d0 = -0.5;
        final double d2 = 0.5;
        final IteratorAxis iteratoraxis = new IteratorAxis(blockpos, blockpos2, d0, d2);
        System.out.println("Start: " + blockpos + ", end: " + blockpos2 + ", yDelta: " + d0 + ", zDelta: " + d2);
        while (iteratoraxis.hasNext()) {
            final BlockPos blockpos3 = iteratoraxis.next();
            System.out.println(new StringBuilder().append(blockpos3).toString());
        }
    }
}
