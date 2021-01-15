package saint.modstuff.modules;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;

public class ChestAura extends Module {
   public static final List openedBlocks = new CopyOnWriteArrayList();
   private final TimeHelper time = new TimeHelper();
   private boolean shouldBreak = false;
   BlockPos globalPos;

   public ChestAura() {
      super("ChestAura", -8388608, ModManager.Category.COMBAT);
      this.setTag("Chest Aura");
      Saint.getCommandManager().getContentList().add(new Command("chestauraclear", "none", new String[]{"clearchests", "cac"}) {
         public void run(String message) {
            ChestAura.openedBlocks.clear();
            Logger.writeChat("Cleared Chest Aura list!");
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (mc.currentScreen instanceof GuiContainer) {
            this.time.reset();
         }

         PreMotion pre = (PreMotion)event;
         int radius = 3;

         for(int y = radius; y >= -radius; --y) {
            for(int x = -radius; x <= radius; ++x) {
               for(int z = -radius; z <= radius; ++z) {
                  BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5D + (double)x, mc.thePlayer.posY - 0.5D + (double)y, mc.thePlayer.posZ - 0.5D + (double)z);
                  Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                  if (this.getFacingDirection(pos) != null && pos != null && !(mc.currentScreen instanceof GuiContainer) && mc.thePlayer.getDistance(mc.thePlayer.posX + (double)x, mc.thePlayer.posY + (double)y, mc.thePlayer.posZ + (double)z) < (double)mc.playerController.getBlockReachDistance() - 0.5D && block instanceof BlockChest) {
                     this.shouldBreak = true;
                     float[] rotations = BlockHelper.getBlockRotations((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                     pre.setYaw(rotations[0]);
                     pre.setPitch(rotations[1]);
                     this.globalPos = pos;
                     return;
                  }
               }
            }
         }
      } else if (event instanceof PostMotion && this.globalPos != null && !(mc.currentScreen instanceof GuiContainer) && !openedBlocks.contains(this.globalPos) && this.shouldBreak) {
         mc.thePlayer.swingItem();
         if (mc.playerController.blockHitDelay > 1) {
            mc.playerController.blockHitDelay = 1;
         }

         EnumFacing direction = this.getFacingDirection(this.globalPos);
         if (direction != null && this.time.hasReached(400L)) {
            mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), this.globalPos, direction, new Vec3((double)this.globalPos.getX(), (double)this.globalPos.getY(), (double)this.globalPos.getZ()));
            openedBlocks.add(this.globalPos);
            this.time.reset();
         }
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.theWorld != null) {
         openedBlocks.clear();
      }

   }

   private EnumFacing getFacingDirection(BlockPos pos) {
      EnumFacing direction = null;
      if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.UP;
      } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.DOWN;
      } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.EAST;
      } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.WEST;
      } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.SOUTH;
      } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.NORTH;
      }

      return direction;
   }
}
