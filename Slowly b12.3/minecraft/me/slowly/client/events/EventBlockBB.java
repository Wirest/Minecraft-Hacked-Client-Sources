/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockBB
implements Event {
    private Block block;
    private AxisAlignedBB boundingBox;
    private int x;
    private int y;
    private int z;

    public void setBlock(Block block, AxisAlignedBB boundingBox, int x, int y, int z) {
        this.block = block;
        this.boundingBox = boundingBox;
        this.x = x;
        this.y = y;
        this.z = z;
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}

