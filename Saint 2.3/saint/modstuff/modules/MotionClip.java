package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.eventstuff.events.InsideBlock;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class MotionClip extends Module {
   private final Value offset = new Value("noclip_offset", -1.45D);
   private int ticks;

   public MotionClip() {
      super("MotionClip", -9868951, ModManager.Category.EXPLOITS);
      this.setTag("Motion Clip");
      Saint.getCommandManager().getContentList().add(new Command("motionclipoffset", "<offset>", new String[]{"mcoffset", "mco"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               MotionClip.this.offset.setValueState((Double)MotionClip.this.offset.getDefaultValue());
            } else {
               MotionClip.this.offset.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)MotionClip.this.offset.getValueState() < -1.45D) {
               MotionClip.this.offset.setValueState(-1.45D);
            } else if ((Double)MotionClip.this.offset.getValueState() > 1.45D) {
               MotionClip.this.offset.setValueState(1.45D);
            }

            Logger.writeChat("Motion Clip Offset set to: " + MotionClip.this.offset.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBB) {
         if (mc.thePlayer == null) {
            return;
         }

         BlockBB block = (BlockBB)event;
         if ((int)(mc.thePlayer.boundingBox.minY - 0.5D) < block.getY()) {
            block.setBoundingBox((AxisAlignedBB)null);
         }
      } else if (event instanceof PlayerMovement) {
         PlayerMovement movement = (PlayerMovement)event;
         if (BlockHelper.isInsideBlock(mc.thePlayer)) {
            movement.setX(movement.getX() / 2.0D);
            movement.setY(movement.getY() / 2.0D);
         }
      } else if (event instanceof PreMotion) {
         if (BlockHelper.isInsideBlock(mc.thePlayer)) {
            this.setColor(-2448096);
         } else {
            this.setColor(-9868951);
         }

         if (BlockHelper.isInsideBlock(mc.thePlayer)) {
            ++this.ticks;
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            if (this.ticks >= 5) {
               player.y -= (Double)this.offset.getValueState();
               this.ticks = 0;
            }
         }
      } else if (event instanceof InsideBlock) {
         ((InsideBlock)event).setCancelled(true);
      }

   }
}
