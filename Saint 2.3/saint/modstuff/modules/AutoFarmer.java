package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSoulSand;
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
import saint.valuestuff.Value;

public class AutoFarmer extends Module {
   private BlockPos blockBreaking;
   private TimeHelper time = new TimeHelper();
   private final Value harvest = new Value("autofarm_harvest", true);
   private final Value plant = new Value("autofarm_plant", false);

   public AutoFarmer() {
      super("AutoFarmer", -6697729, ModManager.Category.WORLD);
      this.setTag("Auto Farmer");
      Saint.getCommandManager().getContentList().add(new Command("farmermode", "<harvest/plant>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("harvest")) {
               AutoFarmer.this.plant.setValueState(false);
               AutoFarmer.this.harvest.setValueState(true);
               Logger.writeChat("Farmer Mode set to Harvest!");
               AutoFarmer.this.setColor(-6697729);
            } else if (message.split(" ")[1].equalsIgnoreCase("plant")) {
               AutoFarmer.this.harvest.setValueState(false);
               AutoFarmer.this.plant.setValueState(true);
               Logger.writeChat("Farmer Mode set to Plant!");
               AutoFarmer.this.setColor(-13210);
            } else {
               Logger.writeChat("Option not valid! Available options: harvest/plant.");
            }

         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.theWorld != null) {
         this.blockBreaking = null;
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;

         for(int y = 6; y >= -1; --y) {
            for(int x = -6; x <= 6; ++x) {
               for(int z = -6; z <= 6; ++z) {
                  if (x != 0 || z != 0) {
                     BlockPos pos = new BlockPos(mc.thePlayer.posX + (double)x, mc.thePlayer.posY + (double)y, mc.thePlayer.posZ + (double)z);
                     if (this.getFacingDirection(pos) != null) {
                        if ((Boolean)this.harvest.getValueState()) {
                           if (!this.blockChecks(mc.theWorld.getBlockState(pos).getBlock(), pos)) {
                              continue;
                           }
                        } else if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockSoulSand) || mc.theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() instanceof BlockNetherWart) {
                           continue;
                        }

                        if (mc.thePlayer.getDistance(mc.thePlayer.posX + (double)x, mc.thePlayer.posY + (double)y, mc.thePlayer.posZ + (double)z) < (double)mc.playerController.getBlockReachDistance() - 0.5D) {
                           float[] rotations = BlockHelper.getBlockRotations((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                           pre.setYaw(rotations[0]);
                           pre.setPitch(rotations[1]);
                           this.blockBreaking = pos;
                           return;
                        }
                     }
                  }
               }
            }
         }

         this.blockBreaking = null;
      } else if (event instanceof PostMotion && this.blockBreaking != null) {
         mc.thePlayer.swingItem();
         if (mc.playerController.blockHitDelay > 1) {
            mc.playerController.blockHitDelay = 1;
         }

         EnumFacing direction = this.getFacingDirection(this.blockBreaking);
         if (direction != null) {
            if ((Boolean)this.harvest.getValueState()) {
               mc.playerController.func_180512_c(this.blockBreaking, direction);
            } else if ((Boolean)this.plant.getValueState() && this.time.hasReached(75L)) {
               mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), this.blockBreaking, direction, new Vec3((double)this.blockBreaking.getX(), (double)this.blockBreaking.getY(), (double)this.blockBreaking.getZ()));
               this.time.reset();
            }
         }
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

   private boolean blockChecks(Block block, BlockPos pos) {
      if (block instanceof BlockCrops) {
         BlockCrops crops = (BlockCrops)block;
         return crops.getMetaFromState(mc.theWorld.getBlockState(pos)) == 7;
      } else if (block instanceof BlockNetherWart) {
         BlockNetherWart wart = (BlockNetherWart)block;
         return wart.getMetaFromState(mc.theWorld.getBlockState(pos)) == 3;
      } else {
         return false;
      }
   }
}
