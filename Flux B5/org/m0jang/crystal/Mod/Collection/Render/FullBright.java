package org.m0jang.crystal.Mod.Collection.Render;

import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FullBright extends Module {
   private float oldVal;

   public FullBright() {
      super("FullBright", Category.Render, false);
   }

   public void onEnable() {
      this.oldVal = Minecraft.gameSettings.gammaSetting;
      Minecraft.gameSettings.gammaSetting = 2000.0F;
   }

   public void onDisable() {
      Minecraft.gameSettings.gammaSetting = this.oldVal;
   }
}
