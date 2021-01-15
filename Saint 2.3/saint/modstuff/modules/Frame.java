package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.EntityHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Frame extends Module {
   private final Value name = new Value("frame_player", "");
   private EntityPlayer player;

   public Frame() {
      super("Frame", -13382867, ModManager.Category.WORLD);
      this.setEnabled(false);
      this.setVisible(true);
      Saint.getCommandManager().getContentList().add(new Command("frame", "<name>", new String[]{"frp"}) {
         public void run(String message) {
            String[] playerName = message.split(" ");
            if (playerName.length == 1) {
               Logger.writeChat("Now framing §4nobody§f.");
               Frame.this.name.setValueState("");
               Frame.this.setTag("Frame");
               Frame.this.player = null;
            } else {
               boolean found = false;
               Iterator var5 = mc.theWorld.playerEntities.iterator();

               while(var5.hasNext()) {
                  Object o = var5.next();
                  Frame.this.player = (EntityPlayer)o;
                  if (playerName[1].equalsIgnoreCase(Frame.this.player.getName()) && !playerName[1].equalsIgnoreCase(mc.thePlayer.getName()) && Frame.this.player != mc.thePlayer) {
                     Frame.this.name.setValueState(Frame.this.player.getName());
                     Logger.writeChat("Now framing §2" + Frame.this.player.getName() + "§f.");
                     Frame.this.setTag("Frame§f:§7 " + Frame.this.player.getName());
                     if (!Frame.this.isEnabled()) {
                        Frame.this.toggle();
                     }

                     found = true;
                     break;
                  }
               }

               if (!found) {
                  Logger.writeChat("Can't find player \"" + playerName[1] + "\".");
                  Frame.this.player = null;
               }

            }
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && this.player != null) {
         Iterator var3 = mc.theWorld.playerEntities.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            EntityPlayer entity = (EntityPlayer)o;
            if (entity == this.player && entity.getDistanceToEntity(mc.thePlayer) <= 4.0F) {
               float[] rotations = EntityHelper.getEntityRotations(this.player, mc.thePlayer);
               entity.rotationYaw = rotations[0];
               entity.rotationYawHead = rotations[0];
               entity.rotationPitch = rotations[1];
               entity.swingItem();
            }
         }
      }

   }
}
