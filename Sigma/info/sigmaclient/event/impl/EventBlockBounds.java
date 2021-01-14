package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlockBounds extends Event {
    private Block block;
    private BlockPos pos;
    private AxisAlignedBB bounds;

    public void fire(Block block, BlockPos pos, AxisAlignedBB bounds) {
        this.block = block;
        this.pos = pos;
        this.bounds = bounds;
        super.fire();
    }

    public AxisAlignedBB getBounds() {
        return bounds;
    }

    public void setBounds(AxisAlignedBB bounds) {
        this.bounds = bounds;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Block getBlock() {
        return block;
    }
}
