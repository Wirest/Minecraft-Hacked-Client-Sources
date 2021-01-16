package org.m0jang.crystal.Mod.Collection.Movement.longjump;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class Kohi extends SubModule {
   public Kohi() {
      super("Kohi", "LongJump");
   }

   public void onDisable() {
      super.onDisable();
      Timer.timerSpeed = 1.0F;
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.isMoving()) {
         Minecraft.thePlayer.setSpeed(Minecraft.thePlayer.getSpeed());
         if (Minecraft.thePlayer.onGround) {
            Minecraft.thePlayer.motionY = 0.42D;
         }

         Timer var10000 = this.mc.timer;
         Timer.timerSpeed = 0.7F;
         if (BlockHelper.getBlockUnderPlayer(Minecraft.thePlayer, 0.6D) instanceof BlockAir) {
            Minecraft.thePlayer.setSpeed(1.15D);
            var10000 = this.mc.timer;
            Timer.timerSpeed = 1.1F;
            if (BlockHelper.getBlockUnderPlayer(Minecraft.thePlayer, 0.8D) instanceof BlockAir) {
               Minecraft.thePlayer.setSpeed(0.6D);
               Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + Minecraft.thePlayer.motionX * 2.0D, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + Minecraft.thePlayer.motionZ * 2.0D);
               Minecraft.thePlayer.isInWeb = true;
               var10000 = this.mc.timer;
               Timer.timerSpeed = 10.6F;
            } else {
               var10000 = this.mc.timer;
               Timer.timerSpeed = 1.0F;
               Minecraft.thePlayer.setSpeed(0.6D);
            }
         }

         if (Minecraft.thePlayer.motionY < -0.02745D && !(BlockHelper.getBlockUnderPlayer(Minecraft.thePlayer, 0.1D) instanceof BlockAir)) {
            Minecraft.thePlayer.setSpeed(0.0D);
            var10000 = this.mc.timer;
            Timer.timerSpeed = 1.0F;
         }

      }
   }
}
