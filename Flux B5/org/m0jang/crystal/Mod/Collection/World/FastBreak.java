package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FastBreak extends Module {
   public FastBreak() {
      super("FastBreak", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      Minecraft.thePlayer.removePotionEffect(Potion.digSpeed.getId());
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.mc.playerController.blockHitDelay = 0;
      boolean item = Minecraft.thePlayer.getCurrentEquippedItem() == null;
      Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, item ? 1 : 0));
   }
}
