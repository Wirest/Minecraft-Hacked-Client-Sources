package org.m0jang.crystal.Mod.Collection.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class Hypixel extends SubModule {
   public Hypixel() {
      super("Hypixel", "Fly");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Minecraft.thePlayer.motionY = 0.0D;
      Minecraft.thePlayer.onGround = true;

      for(int i = 0; i < 3; ++i) {
         Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-12D, Minecraft.thePlayer.posZ);
         if (Minecraft.thePlayer.ticksExisted % 3 == 0) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0E-12D, Minecraft.thePlayer.posZ);
         }
      }

   }
}
