/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;
import net.minecraft.block.Block;

public class EventCollideCheck
extends EventCancellable {
    public boolean collide;
    public Block block;

    public EventCollideCheck(boolean collide, Block block) {
        this.collide = collide;
        this.block = block;
    }

    public void setCollidable(boolean collide) {
        this.collide = collide;
    }

    public boolean getCollidable() {
        return this.collide;
    }
}

