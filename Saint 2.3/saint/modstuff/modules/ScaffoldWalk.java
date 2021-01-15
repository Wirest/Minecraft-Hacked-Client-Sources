package saint.modstuff.modules;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.Walking;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockData;
import saint.utilities.EntityHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class ScaffoldWalk extends Module {
   private final Value slowmode = new Value("scaffoldwalk_slowmode", false);
   private BlockData blockData = null;
   private TimeHelper time = new TimeHelper();

   public ScaffoldWalk() {
      super("ScaffoldWalk", -4539718, ModManager.Category.WORLD);
      this.setTag("Scaffold Walk");
      Saint.getCommandManager().getContentList().add(new Command("scaffoldwalkslowmode", "none", new String[]{"sfwslowmode", "swsm"}) {
         public void run(String message) {
            ScaffoldWalk.this.slowmode.setValueState(!(Boolean)ScaffoldWalk.this.slowmode.getValueState());
            Logger.writeChat("Scaffold Walk will " + ((Boolean)ScaffoldWalk.this.slowmode.getValueState() ? "now" : "no longer") + " slowly place the blocks.");
         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
      this.blockData = null;
   }

   public void onEvent(Event event) {
      float[] values;
      if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;
         this.blockData = null;
         if (mc.thePlayer.getHeldItem() != null && !mc.thePlayer.isSneaking() && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            BlockPos blockBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) {
               this.blockData = this.getBlockData(blockBelow);
               if (this.blockData != null) {
                  values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
                  pre.setYaw(values[0]);
                  pre.setPitch(values[1]);
               }
            }
         }
      } else if (event instanceof PostMotion) {
         if (this.blockData == null) {
            return;
         }

         if ((Boolean)this.slowmode.getValueState()) {
            if (!this.time.hasReached(500L)) {
               mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
               if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ()))) {
                  mc.thePlayer.swingItem();
               }

               mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            if (this.time.hasReached(750L)) {
               this.time.reset();
            }
         } else if (!(Boolean)this.slowmode.getValueState() && this.time.hasReached(75L)) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ()))) {
               mc.thePlayer.swingItem();
            }

            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            this.time.reset();
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            if (this.blockData == null) {
               return;
            }

            values = EntityHelper.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
            player.yaw = values[0];
            player.pitch = values[1];
         }
      } else if (event instanceof Walking) {
         ((Walking)event).setSafeWalk(true);
      }

   }

   public BlockData getBlockData(BlockPos pos) {
      if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
         return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
      } else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
         return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
      } else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
         return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
      } else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
         return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
      } else {
         return mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air ? new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null;
      }
   }
}
