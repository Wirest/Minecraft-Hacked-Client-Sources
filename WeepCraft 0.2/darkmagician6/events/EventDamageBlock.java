/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.Event;
import darkmagician6.EventCancellable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventDamageBlock
extends EventCancellable
implements Event {
    private BlockPos blockPos = null;
    private EnumFacing enumFacing = null;

    public EventDamageBlock(BlockPos blockPos, EnumFacing enumFacing) {
        this.setBlockPos(blockPos);
        this.setEnumFacing(enumFacing);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public void setEnumFacing(EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}

