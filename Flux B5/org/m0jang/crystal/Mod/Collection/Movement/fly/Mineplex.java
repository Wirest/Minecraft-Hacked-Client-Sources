package org.m0jang.crystal.Mod.Collection.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.TimeHelper;

public class Mineplex extends SubModule {
   int slot = 0;
   TimeHelper up = new TimeHelper();

   public Mineplex() {
      super("Mineplex", "Fly");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      this.slot = 0;
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (event instanceof EventUpdate) {
         if (this.slot >= 0 && this.slot <= 8) {
            Minecraft.thePlayer.inventory.currentItem = this.slot;
            if (Minecraft.thePlayer.getHeldItem() != null) {
               ++this.slot;
            }
         }

         this.mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0D, Minecraft.thePlayer.posZ), EnumFacing.DOWN, new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0D, Minecraft.thePlayer.posZ));
         if (Minecraft.thePlayer.isMoving()) {
            Minecraft.thePlayer.setSpeed(0.256D);
         }

         Minecraft.thePlayer.motionY = 0.0D;
         if (Minecraft.gameSettings.keyBindJump.pressed) {
            Minecraft.thePlayer.motionY = 0.0D;
         }

         Minecraft.thePlayer.onGround = true;
      }

   }
}
