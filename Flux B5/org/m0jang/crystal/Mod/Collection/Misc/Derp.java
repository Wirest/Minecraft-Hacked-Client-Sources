package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Derp extends Module {
   int yaw = 0;

   public Derp() {
      super("Derp", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget(0)
   private void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         ++this.yaw;
         if (this.yaw > 360) {
            this.yaw = 0;
         }

         event.yaw = (float)this.yaw;
         event.pitch = 90.0F;
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
      }

      if (event.state == EventState.PRE) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
      }

   }
}
