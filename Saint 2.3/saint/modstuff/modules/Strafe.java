package saint.modstuff.modules;

import net.minecraft.potion.Potion;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PlayerMovement;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Strafe extends Module {
   private Value air = new Value("strafe_air", false);

   public Strafe() {
      super("Strafe", -10027060, ModManager.Category.MOVEMENT);
      Saint.getCommandManager().getContentList().add(new Command("strafeair", "none", new String[]{"stair", "sta"}) {
         public void run(String message) {
            Strafe.this.air.setValueState(!(Boolean)Strafe.this.air.getValueState());
            Logger.writeChat("Strafe will " + ((Boolean)Strafe.this.air.getValueState() ? "now" : "no longer") + " strafe in the air.");
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PlayerMovement) {
         PlayerMovement movement = (PlayerMovement)event;
         if ((mc.thePlayer.onGround || (Boolean)this.air.getValueState()) && !mc.thePlayer.isInWater()) {
            float dir = mc.thePlayer.rotationYaw;
            if (mc.thePlayer.moveForward < 0.0F) {
               dir += 180.0F;
            }

            if (mc.thePlayer.moveStrafing > 0.0F) {
               dir -= 90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : (mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F));
            }

            if (mc.thePlayer.moveStrafing < 0.0F) {
               dir += 90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : (mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F));
            }

            double hOff = 0.221D;
            if (mc.thePlayer.isSprinting()) {
               hOff *= 1.3190000119209289D;
            }

            if (mc.thePlayer.isSneaking()) {
               hOff *= 0.3D;
            }

            if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null) {
               for(int i = 0; i < mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1; ++i) {
                  hOff *= 1.2000000029802322D;
               }
            }

            float xD = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
            float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
            Speed speed = (Speed)Saint.getModuleManager().getModuleUsingName("speed");
            if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed) && !speed.shouldSpeedUp(event)) {
               movement.setX((double)xD);
               movement.setZ((double)zD);
            }
         }
      }

   }
}
