/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.Event;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

public class EventBoundingBox
implements Event {
    private final Block block;
    private AxisAlignedBB boundingBox;
    private final double x;
    private final double y;
    private final double z;

    public EventBoundingBox(AxisAlignedBB bb, Block block, double x, double y, double z) {
        this.block = block;
        this.boundingBox = bb;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Block getBlock() {
        return this.block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}

