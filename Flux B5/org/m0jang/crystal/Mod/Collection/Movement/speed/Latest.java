package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.SubModule;

public class Latest extends SubModule {
   private double prevY;
   private boolean hop;
   private boolean move;

   public Latest() {
      super("Latest", "Speed");
   }

   @EventTarget
   public void onMotion(EventMove event) {
      if (this.hop && Minecraft.thePlayer.posY >= Minecraft.thePlayer.prevPosY + 0.399994D) {
         Minecraft.thePlayer.motionY = -100.0D;
         Minecraft.thePlayer.posY = Minecraft.thePlayer.prevPosY;
         this.hop = false;
      }

      if (!Minecraft.thePlayer.isCollidedHorizontally) {
         if (Minecraft.thePlayer.moveForward == 0.0F && Minecraft.thePlayer.moveStrafing == 0.0F) {
            Timer.timerSpeed = 1.09F;
            if (Minecraft.thePlayer.isCollidedVertically) {
               Timer.timerSpeed = 1.09F;
               Minecraft.thePlayer.jump();
               this.move = true;
            }

            if (this.move && Minecraft.thePlayer.isCollidedVertically) {
               Timer.timerSpeed = 1.09F;
               this.move = false;
            }
         }

         if (Minecraft.thePlayer.isCollidedVertically) {
            Timer.timerSpeed = 1.09F;
            Minecraft.thePlayer.motionX *= 0.464D;
            Minecraft.thePlayer.motionZ *= 0.464D;
            this.doMiniHop();
         }

         if (this.hop && !this.move && Minecraft.thePlayer.posY >= Minecraft.thePlayer.prevPosY + 0.399994D) {
            Timer.timerSpeed = 1.09F;
            Minecraft.thePlayer.motionY = -100.0D;
            Minecraft.thePlayer.posY = Minecraft.thePlayer.prevPosY;
            this.hop = false;
         }
      }

   }

   private void doMiniHop() {
      this.hop = true;
      Minecraft var10001 = this.mc;
      Minecraft.thePlayer.prevPosY = Minecraft.thePlayer.posY;
      Minecraft.thePlayer.jump();
   }
}
