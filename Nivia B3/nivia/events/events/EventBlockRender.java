package nivia.events.events;

import net.minecraft.block.Block;
import nivia.events.Event;


public class EventBlockRender implements Event {
    private int x;
    private int y;
    private int z;
    private final Block block;

    public EventBlockRender(int x, int y, int z, Block block) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock() {
        return this.block;
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


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}