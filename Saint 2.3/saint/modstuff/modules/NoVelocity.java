package saint.modstuff.modules;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class NoVelocity extends Module {
   private final Value velocity = new Value("novelocity_velocity", 0.0F);

   public NoVelocity() {
      super("NoVelocity", -4731698, ModManager.Category.PLAYER);
      this.setTag("No Velocity");
      Saint.getCommandManager().getContentList().add(new Command("novelocityvelocity", "<amount>", new String[]{"novelvel", "nvv"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               NoVelocity.this.velocity.setValueState((Float)NoVelocity.this.velocity.getDefaultValue());
            } else {
               NoVelocity.this.velocity.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)NoVelocity.this.velocity.getValueState() > 10.0F) {
               NoVelocity.this.velocity.setValueState(10.0F);
            } else if ((Float)NoVelocity.this.velocity.getValueState() < -10.0F) {
               NoVelocity.this.velocity.setValueState(-10.0F);
            }

            Logger.writeChat("No Velocity Velocity set to: " + NoVelocity.this.velocity.getValueState());
         }
      });
   }

   public void onEvent(Event e) {
      if (e instanceof RecievePacket) {
         RecievePacket event = (RecievePacket)e;
         if (event.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)event.getPacket()).func_149412_c() == mc.thePlayer.getEntityId()) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
            packet.field_149415_b = (int)((float)packet.field_149415_b * (Float)this.velocity.getValueState());
            packet.field_149416_c = (int)((float)packet.field_149416_c * (Float)this.velocity.getValueState());
            packet.field_149414_d = (int)((float)packet.field_149414_d * (Float)this.velocity.getValueState());
            if (packet.field_149415_b == 0 && packet.field_149416_c == 0 && packet.field_149414_d == 0) {
               event.setCancelled(true);
            }
         }

         if (event.getPacket() instanceof S27PacketExplosion) {
            S27PacketExplosion packet = (S27PacketExplosion)event.getPacket();
            packet.field_149152_f *= (Float)this.velocity.getValueState();
            packet.field_149153_g *= (Float)this.velocity.getValueState();
            packet.field_149159_h *= (Float)this.velocity.getValueState();
         }
      }

   }
}
