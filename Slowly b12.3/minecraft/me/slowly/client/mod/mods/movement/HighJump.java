package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;

public class HighJump extends Mod {
   private Value boost = new Value("HighJump_Boost", 0.5D, 0.1D, 5.0D, 0.05D);
   TimeHelper wait = new TimeHelper();

   public HighJump() {
      super("HighJump", Mod.Category.MOVEMENT, Colors.GREEN.c);
   }

   @EventTarget
   public void onUpdate(UpdateEvent event) {
      if (this.mc.thePlayer.onGround && this.mc.gameSettings.keyBindForward.pressed && this.wait.isDelayComplete(500L)) {
         this.mc.thePlayer.motionY = ((Double)this.boost.getValueState()).doubleValue();
         this.wait.reset();
      }

   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("HighJump Disband", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("HighJump Enable", ClientNotification.Type.SUCCESS);
   }
}

