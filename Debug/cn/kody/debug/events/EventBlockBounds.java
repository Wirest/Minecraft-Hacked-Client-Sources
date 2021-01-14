package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlockBounds extends EventCancellable
{
    private Block block;
    private BlockPos pos;
    private AxisAlignedBB bounds;
    
    public EventBlockBounds(Block block, BlockPos pos, AxisAlignedBB bounds) {
	 this.block = block;
	 this.pos = pos;
	 this.bounds = bounds;
    }
    
    public AxisAlignedBB getBounds() {
        return this.bounds;
    }
    
    public void setBounds(final AxisAlignedBB p_setBounds_1_) {
        this.bounds = p_setBounds_1_;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
