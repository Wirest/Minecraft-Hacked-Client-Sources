package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.util.*;

public class EventDamageBlock extends EventCancellable implements Event
{
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    
    public EventDamageBlock(final BlockPos blockPos, final EnumFacing enumFacing) {
        this.blockPos = null;
        this.enumFacing = null;
        this.setBlockPos(blockPos);
        this.setEnumFacing(enumFacing);
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
    
    public void setEnumFacing(final EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}
