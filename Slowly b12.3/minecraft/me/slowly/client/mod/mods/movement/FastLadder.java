package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;

public class FastLadder extends Mod {
   private Value mode = new Value("FastLadder", "Mode", 0);
   private Value speed = new Value("FastLadder_Speed", 0.2D, 0.1D, 1.0D);

   public FastLadder() {
      super("FastLadder", Mod.Category.MOVEMENT, Colors.GREEN.c);
      this.mode.mode.add("Vanilla");
      this.mode.mode.add("AAC");
   }

   public void disableValues() {
      this.speed.disabled = this.mode.isCurrentMode("AAC");
      super.disableValues();
   }

   @EventTarget
   public void onUpdate(UpdateEvent event) {
      this.showValue = this.mode;
      this.setColor(-16728065);
      double aacSpeed = 0.16999999433755875D;
      if (!this.mc.gameSettings.keyBindSneak.isKeyDown() && this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.isCollidedHorizontally) {
         if (this.mode.isCurrentMode("AAC")) {
            this.mc.thePlayer.motionY = aacSpeed;
         } else {
            this.mc.thePlayer.motionY = ((Double)this.speed.getValueState()).doubleValue();
         }
      }

   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("FastLadder Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("FastLadder Enable", ClientNotification.Type.SUCCESS);
   }
}
