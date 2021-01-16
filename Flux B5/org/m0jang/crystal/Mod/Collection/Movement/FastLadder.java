package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FastLadder extends Module {
   public FastLadder() {
      super("FastLadder", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onMove(EventMove event) {
      if (event.getY() > 0.0D && Minecraft.thePlayer.isOnLadder()) {
         event.y = 0.287299999999994D;
      }

   }
}
