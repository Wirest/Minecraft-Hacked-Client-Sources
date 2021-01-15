package saint.modstuff.modules;

import net.minecraft.network.play.client.C0APacketAnimation;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Derp extends Module {
   private Value crazybastard = new Value("derp_crazybastard", true);
   private Value goatfucker = new Value("derp_goatfucker", false);
   private Value bop = new Value("derp_bop", false);
   private float spin;
   private float fuck;
   private float anal;
   private float dick;

   public Derp() {
      super("Derp", -14513374, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("derp", "<crazybastard/goatfucker/bop>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("crazybastard")) {
               Derp.this.goatfucker.setValueState(false);
               Derp.this.bop.setValueState(false);
               Derp.this.crazybastard.setValueState(true);
               Logger.writeChat("Derp Mode set to Crazy Bastard!");
            } else if (message.split(" ")[1].equalsIgnoreCase("goatfucker")) {
               Derp.this.crazybastard.setValueState(false);
               Derp.this.bop.setValueState(false);
               Derp.this.goatfucker.setValueState(true);
               Logger.writeChat("Derp Mode set to Goat Fucker!");
            } else if (message.split(" ")[1].equalsIgnoreCase("bop")) {
               Derp.this.crazybastard.setValueState(false);
               Derp.this.goatfucker.setValueState(false);
               Derp.this.bop.setValueState(true);
               Logger.writeChat("Derp Mode set to Bop!");
            } else {
               Logger.writeChat("Option not valid! Available options: crazybastard, goatfucker, bop.");
            }

         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;
         KillAura aura = (KillAura)Saint.getModuleManager().getModuleUsingName("killaura");
         if (aura.getTargets().isEmpty()) {
            if ((Boolean)this.crazybastard.getValueState()) {
               this.spin += 20.0F;
               if (this.spin > 180.0F) {
                  this.spin = -180.0F;
               } else if (this.spin < -180.0F) {
                  this.spin = 180.0F;
               }

               pre.setYaw(this.spin);
               pre.setPitch(180.0F);
               mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            } else if ((Boolean)this.goatfucker.getValueState()) {
               this.fuck += 10.0F;
               if (this.fuck > 360.0F) {
                  this.fuck = -360.0F;
               } else if (this.fuck < -360.0F) {
                  this.fuck = 360.0F;
               }

               pre.setPitch(this.fuck);
               this.anal += 10.0F;
               if (this.anal > 50.0F) {
                  this.anal = -50.0F;
               } else if (this.anal < -50.0F) {
                  this.anal = 50.0F;
               }

               pre.setYaw(this.anal);

               for(int i = 0; i < 2; ++i) {
                  mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
               }
            } else if ((Boolean)this.bop.getValueState()) {
               this.dick += 3.0F;
               if (this.dick > 360.0F) {
                  this.dick = -360.0F;
               } else if (this.dick < -360.0F) {
                  this.dick = 360.0F;
               }

               pre.setPitch(this.dick);
            }
         }
      }

   }
}
