package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Glide extends Module {
   private Value motion = new Value("glide_motion", 1.0F);
   private final TimeHelper time = new TimeHelper();
   private boolean shouldCancelPacket = false;
   private int ticks = 0;

   public Glide() {
      super("Glide", -9868951, ModManager.Category.MOVEMENT);
      Saint.getCommandManager().getContentList().add(new Command("glidemotion", "<factor>", new String[]{"glidem", "gm"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Glide.this.motion.setValueState((Float)Glide.this.motion.getDefaultValue());
            } else {
               Glide.this.motion.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)Glide.this.motion.getValueState() > 5.0F) {
               Glide.this.motion.setValueState(5.0F);
            } else if ((Float)Glide.this.motion.getValueState() < 0.0F) {
               Glide.this.motion.setValueState(1.0E-12F);
            }

            Logger.writeChat("Glide Motion Speed has been multiplied by: " + Glide.this.motion.getValueState());
         }
      });
   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         mc.timer.timerSpeed = 1.0F;
      }

   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null) {
         this.time.reset();
         if (mc.thePlayer.onGround) {
            double[] d = new double[]{0.2D, 0.24D};

            for(int a = 0; a < 100; ++a) {
               for(int i = 0; i < d.length; ++i) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d[i], mc.thePlayer.posZ, false));
               }
            }
         }
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (mc.thePlayer.onGround) {
            this.setColor(-9868951);
         } else {
            this.setColor(-12004916);
         }

         if (!mc.thePlayer.capabilities.isFlying && mc.thePlayer.fallDistance > 0.0F && !mc.thePlayer.isSneaking() && this.time.hasReached(500L)) {
            mc.thePlayer.motionY = -0.03127D;
         }

         if (mc.gameSettings.keyBindSneak.pressed) {
            mc.thePlayer.motionY = -0.5D;
         }
      } else if (event instanceof PlayerMovement && this.time.hasReached(500L)) {
         if (!mc.gameSettings.keyBindSneak.pressed) {
            ((PlayerMovement)event).setY(((PlayerMovement)event).getY() * (double)(Float)this.motion.getValueState());
         }
      } else if (event instanceof PacketSent) {
         PacketSent var2 = (PacketSent)event;
      }

   }
}
