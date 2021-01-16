package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FlashSkin extends Module {
   private Random r = new Random();

   public FlashSkin() {
      super("FlashSkin", Category.Misc, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      EnumPlayerModelParts[] var4;
      int var3 = (var4 = EnumPlayerModelParts.values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         EnumPlayerModelParts part = var4[var2];
         Minecraft.gameSettings.func_178878_a(part, true);
      }

   }

   @EventTarget
   private void onTick(EventTick event) {
      EnumPlayerModelParts[] var5;
      int var4 = (var5 = EnumPlayerModelParts.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         EnumPlayerModelParts part = var5[var3];
         Minecraft.gameSettings.func_178878_a(part, this.r.nextBoolean());
      }

   }
}
