package cedo.events.listeners;

import cedo.events.Event;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventLiquidBB extends Event<EventLiquidBB> { //Iffy hook, gets called when other entities are on water as well
    BlockLiquid blockLiquid;
    BlockPos pos;
    AxisAlignedBB axisAlignedBB;

    public EventLiquidBB(BlockLiquid blockLiquid, BlockPos pos, AxisAlignedBB axisAlignedBB) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
        this.axisAlignedBB = axisAlignedBB;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }

    public BlockLiquid getBlock() {
        return blockLiquid;
    }

    public BlockPos getPos() {
        return pos;
    }
}
