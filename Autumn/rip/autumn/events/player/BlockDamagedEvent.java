package rip.autumn.events.player;

import net.minecraft.util.BlockPos;
import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class BlockDamagedEvent extends Cancellable implements Event {
   private final BlockPos blockPos;

   public BlockDamagedEvent(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
