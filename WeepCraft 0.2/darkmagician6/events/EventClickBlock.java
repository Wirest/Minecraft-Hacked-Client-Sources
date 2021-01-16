/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;
import net.minecraft.util.math.BlockPos;

public class EventClickBlock
extends EventCancellable {
    BlockPos blockpos;

    public EventClickBlock(BlockPos blockpos) {
        this.blockpos = blockpos;
    }

    public BlockPos getBlockPos() {
        return this.blockpos;
    }
}

