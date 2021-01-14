package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;

@Label("Sprint")
@Category(ModuleCategory.MOVEMENT)
public final class SprintMod extends Module {
   @Listener(MoveEvent.class)
   public final void onMove() {
      EntityPlayerSP player = mc.thePlayer;
      if (player.isMoving() && player.getFoodStats().getFoodLevel() > 6) {
         player.setSprinting(true);
      }

   }
}
