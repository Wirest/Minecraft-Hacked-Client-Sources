package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventStep;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.World.Phase;
import org.m0jang.crystal.Mod.Collection.World.Scaffold;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Values.Value;

public class Step extends Module {
   int stage = -1;
   boolean letStep;
   public static Value smooth;

   static {
      smooth = new Value("Step", Boolean.TYPE, "Smooth", true);
   }

   public Step() {
      super("Step", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
      this.stage = -1;
   }

   public void onDisable() {
      super.onDisable();
      this.stage = -1;
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         if (!Crystal.INSTANCE.getMods().get(Phase.class).isEnabled() && !Crystal.INSTANCE.getMods().get(Speed.class).isEnabled() && !Crystal.INSTANCE.getMods().get(Scaffold.class).isEnabled() && Minecraft.thePlayer.movementInput != null) {
            MovementInput var10000 = Minecraft.thePlayer.movementInput;
            if (!MovementInput.jump && Minecraft.thePlayer.isCollidedHorizontally && Minecraft.thePlayer.onGround && !BlockUtils.isInLiquid()) {
               Minecraft.thePlayer.stepHeight = 1.0F;
               return;
            }
         }

         Minecraft.thePlayer.stepHeight = 0.5F;
      }

   }

   @EventTarget
   public void onStep(EventStep event) {
      if (event.getStepHeight() > 0.5F && !Crystal.INSTANCE.getMods().get(Scaffold.class).isEnabled() && !Crystal.INSTANCE.getMods().get(Speed.class).isEnabled() && event.getStepHeight() <= 1.0F) {
         double height1 = (double)event.getStepHeight() * 0.42D;
         double height2 = (double)event.getStepHeight() * 0.75D;
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + height1, ClientUtils.z(), ClientUtils.player().onGround));
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + height2, ClientUtils.z(), ClientUtils.player().onGround));
         if (smooth.getBooleanValue()) {
            Timer.timerSpeed = 0.3F;
            (new Thread(new Runnable() {
               public void run() {
                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var2) {
                     ;
                  }

                  Timer.timerSpeed = 1.0F;
               }
            })).start();
         }
      }

   }
}
