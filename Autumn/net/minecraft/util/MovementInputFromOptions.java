package net.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.player.InventoryWalkMod;

public class MovementInputFromOptions extends MovementInput {
   private static final Minecraft mc = Minecraft.getMinecraft();
   private final GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings gameSettingsIn) {
      this.gameSettings = gameSettingsIn;
   }

   public void updatePlayerMoveState() {
      InventoryWalkMod inventoryWalk = (InventoryWalkMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(InventoryWalkMod.class);
      boolean safeGui = !(mc.currentScreen instanceof GuiChat);
      if (inventoryWalk.isEnabled() && safeGui) {
         this.moveStrafe = 0.0F;
         this.moveForward = 0.0F;
         if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
            ++this.moveForward;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
            --this.moveForward;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
            ++this.moveStrafe;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
            --this.moveStrafe;
         }

         this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
         this.sneak = Keyboard.isKeyDown(this.gameSettings.keyBindSneak.getKeyCode());
         if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
         }
      } else {
         this.moveStrafe = 0.0F;
         this.moveForward = 0.0F;
         if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++this.moveForward;
         }

         if (this.gameSettings.keyBindBack.isKeyDown()) {
            --this.moveForward;
         }

         if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++this.moveStrafe;
         }

         if (this.gameSettings.keyBindRight.isKeyDown()) {
            --this.moveStrafe;
         }

         this.jump = this.gameSettings.keyBindJump.isKeyDown();
         this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
         if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
         }
      }

   }
}
