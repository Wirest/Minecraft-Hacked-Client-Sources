package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Mod.Collection.Movement.Step;

public class KohiGround extends SubModule {
   public KohiGround() {
      super("KohiGround", "Speed");
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.isMoving()) {
         if (!(BlockHelper.getBlockUnderPlayer(Minecraft.thePlayer, 1.0D) instanceof BlockAir) && !Minecraft.gameSettings.keyBindJump.pressed) {
            Minecraft.thePlayer.setSpeed(Minecraft.thePlayer.getSpeed());
            if (event.state == EventState.PRE && !Crystal.INSTANCE.mods.get(Step.class).isEnabled()) {
               Minecraft.thePlayer.setSpeed(Minecraft.thePlayer.getSpeed() + 0.035D);
               if (Minecraft.thePlayer.onGround) {
                  Minecraft.thePlayer.motionY = 0.06347D;
               } else if (Minecraft.thePlayer.motionY < -0.05D) {
                  Minecraft.thePlayer.motionY = -0.5D;
                  Minecraft.thePlayer.setSpeed(0.6D);
               } else {
                  Timer var10000;
                  if (Minecraft.thePlayer.motionY > 0.01D && !Minecraft.thePlayer.onGround) {
                     var10000 = this.mc.timer;
                     Timer.timerSpeed = 1.0F;
                  } else if (Minecraft.thePlayer.onGround) {
                     var10000 = this.mc.timer;
                     Timer.timerSpeed = 1.0F;
                     Minecraft.thePlayer.setSpeed(0.9D);
                  } else {
                     var10000 = this.mc.timer;
                     Timer.timerSpeed = Minecraft.thePlayer.ticksExisted % 2 == 0 ? 1.0F : 0.8F;
                     Minecraft.thePlayer.setSpeed(1.1D);
                  }
               }
            }

         } else {
            Minecraft.thePlayer.setSpeed(0.0D);
            Minecraft.thePlayer.motionY = -5.0D;
         }
      }
   }
}
