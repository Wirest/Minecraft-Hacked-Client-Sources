package rip.autumn.events.render;

import net.minecraft.client.gui.ScaledResolution;
import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class RenderCrosshairEvent extends Cancellable implements Event {
   private final ScaledResolution sr;

   public RenderCrosshairEvent(ScaledResolution sr) {
      this.sr = sr;
   }

   public ScaledResolution getScaledRes() {
      return this.sr;
   }
}
