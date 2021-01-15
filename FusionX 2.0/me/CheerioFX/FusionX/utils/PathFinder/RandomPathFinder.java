// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import me.CheerioFX.FusionX.utils.BlockUtils2;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.Vec3i;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.BlockPos;

public class RandomPathFinder extends PathFinder
{
    private final BlockPos center;
    private final float rangeSq;
    private final int blockRange;
    private final Random random;
    
    public RandomPathFinder(final BlockPos center, final float range) {
        super(new BlockPos(Minecraft.getMinecraft().thePlayer));
        this.center = center;
        this.rangeSq = (float)Math.pow(range, 2.0);
        this.blockRange = (int)Math.ceil(range);
        this.random = new Random();
    }
    
    public RandomPathFinder(final RandomPathFinder pathFinder) {
        super(new BlockPos(Minecraft.getMinecraft().thePlayer));
        this.thinkSpeed = pathFinder.thinkSpeed;
        this.thinkTime = pathFinder.thinkTime;
        this.fallingAllowed = pathFinder.fallingAllowed;
        this.center = pathFinder.center;
        this.rangeSq = pathFinder.rangeSq;
        this.blockRange = pathFinder.blockRange;
        this.random = pathFinder.random;
    }
    
    @Override
    protected boolean isPassable(final BlockPos pos) {
        return Math.abs(this.center.getX() - pos.getX()) <= this.blockRange && Math.abs(this.center.getY() - pos.getY()) <= this.blockRange && Math.abs(this.center.getZ() - pos.getZ()) <= this.blockRange && super.isPassable(pos);
    }
    
    @Override
    protected boolean checkDone() {
        return false;
    }
    
    private void setCurrentToRandomNode() {
        int currentIndex = 0;
        final int randomIndex = this.random.nextInt(this.prevPosMap.size());
        final Iterator<PathPos> itr = this.prevPosMap.keySet().iterator();
        while (itr.hasNext()) {
            if (currentIndex == randomIndex) {
                this.current = itr.next();
                break;
            }
            itr.next();
            ++currentIndex;
        }
    }
    
    @Override
    public ArrayList<PathPos> formatPath() {
        this.done = true;
        this.failed = false;
        do {
            this.setCurrentToRandomNode();
        } while (this.center.distanceSq(this.current) > this.rangeSq || (!this.flying && !this.canBeSolid(this.current.offsetDown())) || BlockUtils2.getBlock(this.current.offsetUp()) instanceof BlockLiquid);
        return super.formatPath();
    }
}
