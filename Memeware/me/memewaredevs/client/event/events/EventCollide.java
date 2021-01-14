package me.memewaredevs.client.event.events;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import me.memewaredevs.client.event.Event;

public class EventCollide extends Event {

    private AxisAlignedBB axisalignedbb;
    private Block block;
    private Entity collidingEntity;
    private int x, y, z;

    public EventCollide(Entity collidingEntity, int x, int y, int z, AxisAlignedBB axisalignedbb, Block block) {
        this.collidingEntity = collidingEntity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.axisalignedbb = axisalignedbb;
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.axisalignedbb;
    }

    public Entity getCollidingEntity() {
        return collidingEntity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock() {
        return block;
    }

    public void setBoundingBox(AxisAlignedBB object) {
        this.axisalignedbb = object;
    }

}
