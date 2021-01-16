package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventPreRenderBlock extends EventCancellable implements Event {
   private Block block = null;
   private BlockPos pos = null;

   public EventPreRenderBlock(Block block, BlockPos pos) {
      this.setBlock(block);
      this.setPos(pos);
   }

   public Block getBlock() {
      return this.block;
   }

   public void setBlock(Block block) {
      this.block = block;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public void setPos(BlockPos pos) {
      this.pos = pos;
   }
}
