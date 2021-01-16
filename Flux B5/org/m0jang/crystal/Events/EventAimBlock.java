package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.BlockPos;

public class EventAimBlock implements Event {
   public BlockPos To;

   public EventAimBlock(BlockPos To) {
      this.To = To;
   }
}
