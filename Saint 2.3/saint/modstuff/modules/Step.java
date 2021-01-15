package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PreStep;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Step extends Module {
   boolean viable;
   private final Value height = new Value("step_height", 1.0F);
   private final Value reverse = new Value("step_reverse", true);
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();
   private boolean offset = false;

   public Step() {
      super("Step");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("stepheight", "<height-value>", new String[]{"sh"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Step.this.height.setValueState((Float)Step.this.height.getDefaultValue());
            } else {
               Step.this.height.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)Step.this.height.getValueState() < 1.0F) {
               Step.this.height.setValueState(1.0F);
            }

            Logger.writeChat("Step Height set to: " + Step.this.height.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("stepreverse", "none", new String[]{"stepr", "sr"}) {
         public void run(String message) {
            Step.this.reverse.setValueState(!(Boolean)Step.this.reverse.getValueState());
            Logger.writeChat("Step will " + ((Boolean)Step.this.reverse.getValueState() ? "now" : "no longer") + " go reverse.");
         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null) {
         mc.thePlayer.stepHeight = (Float)this.height.getValueState();
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         mc.thePlayer.stepHeight = 0.5F;
      }

   }

   public Block getBlock(double offset) {
      return this.getBlock(mc.thePlayer.getEntityBoundingBox().offset(0.0D, offset, 0.0D));
   }

   public Block getBlock(AxisAlignedBB bb) {
      int y = (int)bb.minY;

      for(int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (block != null) {
               return block;
            }
         }
      }

      return null;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         Speed speed = (Speed)Saint.getModuleManager().getModuleUsingName("speed");
         boolean checks = mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && this.time.hasReached(speed.isEnabled() ? 75L : 5L);
         if (checks) {
            mc.thePlayer.stepHeight = (Float)this.height.getValueState();
         } else {
            mc.thePlayer.stepHeight = 0.5F;
         }
      } else if (event instanceof PlayerMovement) {
         PlayerMovement move = (PlayerMovement)event;
         if ((Boolean)this.reverse.getValueState() && !Saint.getModuleManager().getModuleUsingName("fly").isEnabled() && !mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0D && (double)mc.thePlayer.fallDistance < 0.2D && !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -1.0D, 0.0D)).isEmpty()) {
            move.setY(move.getY() - 1.0D);
         }
      } else if (event instanceof PreStep) {
         if (mc.thePlayer.stepHeight == (Float)this.height.getValueState()) {
            this.time.reset();
            this.offset = true;
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            if (this.offset) {
               player.y += 0.07D;
               this.offset = false;
            }
         }
      }

   }
}
