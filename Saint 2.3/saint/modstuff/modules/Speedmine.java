package saint.modstuff.modules;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBreaking;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockData;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Speedmine extends Module {
   private final Value autotool = new Value("speedmine_autotool", true);
   private final Value instant = new Value("speedmine_instant", true);

   public Speedmine() {
      super("Speedmine", -12387247, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("speedmineautotool", "none", new String[]{"speedautotool", "smat"}) {
         public void run(String message) {
            Speedmine.this.autotool.setValueState(!(Boolean)Speedmine.this.autotool.getValueState());
            Logger.writeChat("Speed Mine will " + ((Boolean)Speedmine.this.autotool.getValueState() ? "now" : "no longer") + " automatically switch tools.");
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("speedmineinstant", "none", new String[]{"instantmine", "smi"}) {
         public void run(String message) {
            Speedmine.this.instant.setValueState(!(Boolean)Speedmine.this.instant.getValueState());
            Logger.writeChat("Speed Mine will " + ((Boolean)Speedmine.this.instant.getValueState() ? "now" : "no longer") + " mine blocks instantly.");
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBreaking) {
         BlockBreaking bb = (BlockBreaking)event;
         if (bb.getState() == BlockBreaking.EnumBlock.DAMAGE) {
            if (!Saint.getModuleManager().getModuleUsingName("civbreak").isEnabled()) {
               this.setColor(-5991988);
            }
         } else {
            this.setColor(-12387247);
         }

         if (bb.getState() == BlockBreaking.EnumBlock.CLICK) {
            if ((Boolean)this.instant.getValueState()) {
               for(int i = 0; i < 32; ++i) {
                  mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
               }

               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bb.getPos(), bb.getSide()));
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bb.getPos(), bb.getSide()));
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, bb.getPos(), bb.getSide()));
            }

            if ((Boolean)this.autotool.getValueState() && !Saint.getModuleManager().getModuleUsingName("nuker").isEnabled() && !Saint.getModuleManager().getModuleUsingName("civbreak").isEnabled()) {
               BlockHelper.bestTool(bb.getPos().getX(), bb.getPos().getY(), bb.getPos().getZ());
            }
         }
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
