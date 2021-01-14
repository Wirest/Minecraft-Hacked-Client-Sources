package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

/**
 * Created by Arithmo on 5/3/2017 at 7:17 PM.
 */
public class EventLiquidCollide extends Event {

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
