package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class TerrainSpeed extends Module {
   private final Value fastice = new Value("terrainspeed_fastice", true);
   private final Value fastladder = new Value("terrainspeed_fastladder", true);
   private int state = 0;

   public TerrainSpeed() {
      super("TerrainSpeed");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("terrainspeed", "<fastice/fastladder>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("fastice")) {
               TerrainSpeed.this.fastice.setValueState(!(Boolean)TerrainSpeed.this.fastice.getValueState());
               Logger.writeChat("Terrain Speed will " + ((Boolean)TerrainSpeed.this.fastice.getValueState() ? "now" : "no longer") + " speed up on ice.");
            } else if (message.split(" ")[1].equalsIgnoreCase("fastladder")) {
               TerrainSpeed.this.fastladder.setValueState(!(Boolean)TerrainSpeed.this.fastladder.getValueState());
               Logger.writeChat("Terrain Speed will " + ((Boolean)TerrainSpeed.this.fastladder.getValueState() ? "now" : "no longer") + " go fast on ladders.");
            } else {
               Logger.writeChat("Option not valid! Available options: fastice, fastladder.");
            }

         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;
         if ((Boolean)this.fastladder.getValueState()) {
            boolean movementInput = mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindLeft.pressed;
            int posX = MathHelper.floor_double(mc.thePlayer.posX);
            int minY = MathHelper.floor_double(mc.thePlayer.boundingBox.minY);
            int maxY = MathHelper.floor_double(mc.thePlayer.boundingBox.minY + 1.0D);
            int posZ = MathHelper.floor_double(mc.thePlayer.posZ);
            if (movementInput && mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isInWater()) {
               Block block = mc.theWorld.getBlockState(new BlockPos(posX, minY, posZ)).getBlock();
               if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                  block = mc.theWorld.getBlockState(new BlockPos(posX, maxY, posZ)).getBlock();
                  if (block instanceof BlockLadder || block instanceof BlockVine) {
                     mc.thePlayer.motionY = 0.5D;
                  }
               }
            }

            if (mc.thePlayer.isOnLadder() && movementInput && mc.thePlayer.isCollidedHorizontally) {
               EntityPlayerSP var10000 = mc.thePlayer;
               var10000.motionY *= 2.25D;
            }
         }
      } else if (event instanceof PlayerMovement) {
         PlayerMovement movement = (PlayerMovement)event;
         if ((Boolean)this.fastice.getValueState() && BlockHelper.isOnIce()) {
            Blocks.ice.slipperiness = 0.6F;
            Blocks.packed_ice.slipperiness = 0.6F;
            movement.setX(movement.getX() * 2.5D);
            movement.setZ(movement.getZ() * 2.5D);
         } else {
            Blocks.ice.slipperiness = 0.98F;
            Blocks.packed_ice.slipperiness = 0.98F;
         }
      }

   }

   private int getAboveLadders() {
      int ladders = 0;

      for(int dist = 1; dist < 256; ++dist) {
         BlockPos bpos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + (double)dist, mc.thePlayer.posZ);
         Block block = mc.theWorld.getBlockState(bpos).getBlock();
         if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
            break;
         }

         ++ladders;
      }

      return ladders;
   }
}
