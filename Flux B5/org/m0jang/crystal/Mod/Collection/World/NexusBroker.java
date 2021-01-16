package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;
import org.m0jang.crystal.Utils.BlockUtils;

public class NexusBroker extends Module {
   private BlockPos globalPos;

   public NexusBroker() {
      super("NexusBroker", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onUpdatePre(EventUpdate event) {
      if (event.state == EventState.PRE) {
         this.globalPos = null;
         if (Aura.currentTarget != null) {
            return;
         }

         byte radius = 4;

         for(int y = 4; y >= -radius; --y) {
            for(int x = -radius; x <= radius; ++x) {
               for(int z = -radius; z <= radius; ++z) {
                  BlockPos pos = new BlockPos(Minecraft.thePlayer.posX - 0.5D + (double)x, Minecraft.thePlayer.posY - 0.5D + (double)y, Minecraft.thePlayer.posZ - 0.5D + (double)z);
                  Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
                  if (this.getFacingDirection(pos) != null && Minecraft.thePlayer.getDistance(Minecraft.thePlayer.posX + (double)x, Minecraft.thePlayer.posY + (double)y, Minecraft.thePlayer.posZ + (double)z) < (double)Wrapper.mc.playerController.getBlockReachDistance() && Block.getIdFromBlock(block) == 121) {
                     float[] rotations = BlockUtils.getBlockRotations((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                     event.yaw = rotations[0];
                     event.pitch = rotations[1];
                     this.globalPos = pos;
                     return;
                  }
               }
            }
         }
      }

      EnumFacing direction;
      if (event.state == EventState.POST && this.globalPos != null && !(Wrapper.mc.currentScreen instanceof GuiContainer) && (direction = this.getFacingDirection(this.globalPos)) != null) {
         if (Wrapper.mc.playerController.blockHitDelay > 1) {
            Wrapper.mc.playerController.blockHitDelay = 1;
         }

         Minecraft.thePlayer.swingItem();
         this.mc.playerController.func_180512_c(this.globalPos, direction);
      }

   }

   private EnumFacing getFacingDirection(BlockPos pos) {
      EnumFacing direction = null;
      if (!Minecraft.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.UP;
      } else if (!Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.DOWN;
      } else if (!Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.EAST;
      } else if (!Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.WEST;
      } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.SOUTH;
      } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.NORTH;
      }

      return direction;
   }
}
