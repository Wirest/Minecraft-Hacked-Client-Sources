package saint.modstuff.modules;

import java.util.Set;
import net.minecraft.entity.player.EnumPlayerModelParts;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.modstuff.Module;

public class SkinDerp extends Module {
   private Set original;

   public SkinDerp() {
      super("SkinDerp");
      this.setEnabled(true);
   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null && mc.thePlayer != null) {
         EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
         if (parts != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = parts.length;

            for(int i = 0; i < j; ++i) {
               EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
               mc.gameSettings.func_178878_a(part, true);
            }
         }
      }

   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick && mc.thePlayer != null) {
         EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
         if (parts != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = parts.length;

            for(int i = 0; i < j; ++i) {
               EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
               mc.gameSettings.func_178878_a(part, getRandom().nextBoolean());
            }
         }
      }

   }
}
