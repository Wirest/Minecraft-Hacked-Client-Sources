package org.m0jang.crystal.Mod.Collection.Movement.fly;

import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;
import org.m0jang.crystal.Mod.SubModule;

public class Vanilla extends SubModule {
   public Vanilla() {
      super("Vanilla", "Fly");
   }

   public void onEnable() {
      if (Minecraft.theWorld != null) {
         if (this.mc.playerController.currentGameType != WorldSettings.GameType.CREATIVE && this.mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
            Minecraft.thePlayer.capabilities.allowFlying = true;
            Minecraft.thePlayer.capabilities.isFlying = true;
         }

      }
   }

   public void onDisable() {
      if (Minecraft.theWorld != null) {
         if (this.mc.playerController.currentGameType != WorldSettings.GameType.CREATIVE && this.mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
            Minecraft.thePlayer.capabilities.allowFlying = false;
            Minecraft.thePlayer.capabilities.isFlying = false;
         }

      }
   }
}
