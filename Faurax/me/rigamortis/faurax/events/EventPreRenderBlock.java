package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class EventPreRenderBlock extends EventCancellable implements Event
{
    private Block block;
    private BlockPos pos;
    
    public EventPreRenderBlock(final Block block, final BlockPos pos) {
        this.block = null;
        this.pos = null;
        this.setBlock(block);
        this.setPos(pos);
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public void setBlock(final Block block) {
        this.block = block;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
}
