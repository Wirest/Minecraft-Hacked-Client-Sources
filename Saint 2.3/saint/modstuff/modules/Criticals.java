package saint.modstuff.modules;

import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.Attack;
import saint.eventstuff.events.BlockBreaking;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Criticals extends Module {
   private final Value critsold = new Value("criticals_old", false);
   private final Value critsnew = new Value("criticals_new", true);
   int attacked = 0;
   int state = 0;
   private double fallDist;
   private boolean active;

   public Criticals() {
      super("Criticals", -351136, ModManager.Category.COMBAT);
      Saint.getCommandManager().getContentList().add(new Command("criticalsmode", "<new/old>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("new")) {
               Criticals.this.critsold.setValueState(false);
               Criticals.this.critsnew.setValueState(true);
               Logger.writeChat("Criticals Mode set to New!");
            } else if (message.split(" ")[1].equalsIgnoreCase("old")) {
               Criticals.this.critsnew.setValueState(false);
               Criticals.this.critsold.setValueState(true);
               Logger.writeChat("Criticals Mode set to Old!");
            } else {
               Logger.writeChat("Option not valid! Available options: new, old.");
            }

         }
      });
   }

   public boolean isSafe() {
      return mc.thePlayer.isInWater() || mc.thePlayer.isInsideOfMaterial(Material.lava) || mc.thePlayer.isOnLadder() || mc.thePlayer.getActivePotionEffects().contains(Potion.blindness) || mc.thePlayer.ridingEntity != null;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         this.setColor(this.active && (Boolean)this.critsold.getValueState() ? -351136 : -2894893);
         if ((Boolean)this.critsnew.getValueState()) {
            this.setColor(-351136);
         }

         PreMotion pre = (PreMotion)event;
         boolean var10000;
         if (!BlockHelper.isInLiquid() && !BlockHelper.isOnLiquid() && !mc.gameSettings.keyBindJump.pressed) {
            var10000 = true;
         } else {
            var10000 = false;
         }

         ++this.attacked;
         if ((Boolean)this.critsnew.getValueState()) {
            pre.setOnGround(false);
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            boolean onGr = mc.thePlayer.onGround;
            if (!this.isSafe()) {
               this.fallDist += (double)mc.thePlayer.fallDistance;
            }

            if (this.fallDist < 4.0D && !this.isSafe()) {
               if (this.fallDist > 0.0D) {
                  onGr = false;
                  this.active = true;
               } else {
                  this.active = false;
               }
            } else {
               onGr = true;
               this.active = false;
               this.fallDist = 0.0D;
               mc.thePlayer.fallDistance = 0.0F;
            }

            player.field_149474_g = (Boolean)this.critsold.getValueState() ? onGr : false;
         } else if (sent.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition)sent.getPacket();
            packet.field_149474_g = false;
         } else if (sent.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook)sent.getPacket();
            packet.field_149474_g = false;
         } else if (sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            C03PacketPlayer.C06PacketPlayerPosLook packet = (C03PacketPlayer.C06PacketPlayerPosLook)sent.getPacket();
            packet.field_149474_g = false;
         }
      } else if (event instanceof BlockBreaking) {
         if (((BlockBreaking)event).getState() == BlockBreaking.EnumBlock.CLICK && (Boolean)this.critsold.getValueState()) {
            mc.thePlayer.fallDistance = 4.0F;
            this.fallDist = 4.0D;
         }
      } else if (event instanceof Attack && this.attacked > 20 && (Boolean)this.critsnew.getValueState()) {
         this.isSafe();
      }

   }
}
