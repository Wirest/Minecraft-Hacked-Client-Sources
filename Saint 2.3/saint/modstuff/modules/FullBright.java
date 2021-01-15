package saint.modstuff.modules;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.modstuff.Module;

public class FullBright extends Module {
   public FullBright() {
      super("FullBright");
      this.setEnabled(true);
   }

   public void editTable(WorldClient world, float value) {
      if (world != null) {
         float[] light = world.provider.lightBrightnessTable;

         for(int index = 0; index < light.length; ++index) {
            if (light[index] <= value) {
               light[index] = value;
            }
         }

      }
   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.theWorld != null) {
         mc.thePlayer.removePotionEffect(Potion.nightVision.id);
         mc.gameSettings.gammaSetting = 0.5F;
      }

   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.theWorld != null && mc.thePlayer != null) {
         mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 2125));
         mc.gameSettings.gammaSetting = 99.0F;
      }

   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick) {
         mc.gameSettings.gammaSetting = 99.0F;
         if (mc.thePlayer != null && mc.theWorld != null) {
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 2125));
         }
      }

   }
}
