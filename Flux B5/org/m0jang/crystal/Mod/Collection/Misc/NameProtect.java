package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Events.EventDrawText;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class NameProtect extends Module {
   public NameProtect() {
      super("NameProtect", Category.Misc, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onDrawText(EventDrawText event) {
      if (event.text.contains(Minecraft.thePlayer.getName())) {
         event.text = event.text.replaceAll(Minecraft.thePlayer.getName(), "MeMe");
      }

   }
}
