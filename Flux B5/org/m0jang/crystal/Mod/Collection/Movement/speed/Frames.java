package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.TimeHelper;

public class Frames extends SubModule {
   private boolean move;
   private boolean canChangeMotion;
   private TimeHelper timer;

   public Frames() {
      super("Frames", "Speed");
   }

   public void onEnable() {
      super.onEnable();
      this.timer = new TimeHelper();
      this.move = true;
      this.canChangeMotion = false;
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (event.state == EventState.POST) {
         if (!Minecraft.thePlayer.onGround) {
            if (this.canChangeMotion) {
               if (!this.move) {
                  EntityPlayerSP thePlayer = Minecraft.thePlayer;
                  thePlayer.motionX *= 4.6805D;
                  EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                  thePlayer2.motionZ *= 4.6805D;
                  this.move = true;
                  this.canChangeMotion = false;
               } else {
                  Minecraft.thePlayer.motionX = 0.0D;
                  Minecraft.thePlayer.motionZ = 0.0D;
                  this.move = false;
                  this.canChangeMotion = false;
               }
            }
         } else if ((Minecraft.thePlayer.moveForward != 0.0F || Minecraft.thePlayer.moveStrafing != 0.0F) && !Minecraft.thePlayer.isCollidedHorizontally) {
            Minecraft.thePlayer.jump();
            this.canChangeMotion = true;
         }
      }

   }
}
