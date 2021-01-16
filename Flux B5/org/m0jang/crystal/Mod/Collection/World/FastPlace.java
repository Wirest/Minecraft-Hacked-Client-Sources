package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import org.m0jang.crystal.Events.EventRightClick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FastPlace extends Module {
   public FastPlace() {
      super("FastPlace", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onRightClick(EventRightClick event) {
      this.mc.rightClickDelayTimer = 0;
   }
}
