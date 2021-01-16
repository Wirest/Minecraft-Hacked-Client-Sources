package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import org.m0jang.crystal.Events.EventRender2D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class InvMove extends Module {
   public InvMove() {
      super("InventoryMove", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
         if (Keyboard.isKeyDown(200)) {
            Minecraft.thePlayer.rotationPitch -= 2.0F;
         }

         if (Keyboard.isKeyDown(208)) {
            Minecraft.thePlayer.rotationPitch += 2.0F;
         }

         if (Keyboard.isKeyDown(203)) {
            Minecraft.thePlayer.rotationYaw -= 3.0F;
         }

         if (Keyboard.isKeyDown(205)) {
            Minecraft.thePlayer.rotationYaw += 3.0F;
         }

         Minecraft.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindForward.getKeyCode());
         Minecraft.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindBack.getKeyCode());
         Minecraft.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindLeft.getKeyCode());
         Minecraft.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindRight.getKeyCode());
         Minecraft.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindJump.getKeyCode());
      }

   }
}
