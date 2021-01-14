package store.shadowclient.client.module.movement;


import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class FastLadder extends Module {

   public FastLadder() {
      super("FastLadder", 0, Category.MOVEMENT);
      
      Shadow.instance.settingsManager.rSetting(new Setting("Speed", this, 0.2D, 0.1D, 1.0D, false));
      
      ArrayList<String> options = new ArrayList<>();
      options.add("Vanilla");
      options.add("AAC");
      
      Shadow.instance.settingsManager.rSetting(new Setting("Ladder Mode", this, "AAC", options));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
	  String mode = Shadow.instance.settingsManager.getSettingByName("Ladder Mode").getValString();
      this.setDisplayName("FastLadder §7| " + mode);
      
      double aacSpeed = 0.16999999433755875D;
      if(!this.mc.gameSettings.keyBindSneak.isKeyDown() && this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.isCollidedHorizontally) {
         if(mode.equalsIgnoreCase("AAC")) {
            this.mc.thePlayer.motionY = aacSpeed;
         } else {
            this.mc.thePlayer.motionY = ((Double) Shadow.instance.settingsManager.getSettingByName("Speed").getValDouble());
         }
      }

   }
}
