/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockCollideEvent
implements Event {
    public IBlockState state;
    public BlockPos pos;
    public List<AxisAlignedBB> boxes;

    public BlockCollideEvent(IBlockState state, BlockPos pos, List<AxisAlignedBB> boxes) {
        this.state = state;
        this.pos = pos;
        this.boxes = boxes;
    }
}

