package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Sneak extends Module {
   public Sneak() {
      super("Sneak", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
      } else if (event.state == EventState.POST) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
      }

   }
}
