package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class Nuker extends Module {
   public TimeHelper delay = new TimeHelper();
   public List teleported = new ArrayList();

   public Nuker() {
      super("Nuker", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      this.teleported.clear();
   }

   @EventTarget
   public void onTick(EventUpdate event) {
      if (Minecraft.theWorld != null && event.state == EventState.POST) {
         byte radius = 10;

         for(int y = 10; y >= -radius; --y) {
            for(int x = -radius; x <= radius; ++x) {
               for(int z = -radius; z <= radius; ++z) {
                  BlockPos pos = new BlockPos(Minecraft.thePlayer.posX - 0.5D + (double)x, Minecraft.thePlayer.posY - 0.5D + (double)y, Minecraft.thePlayer.posZ - 0.5D + (double)z);
                  Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
                  if (this.getFacingDirection(pos) != null && !(block instanceof BlockAir)) {
                     this.eraseBlock(pos, this.getFacingDirection(pos));
                  }
               }
            }
         }

      }
   }

   public void eraseBlock(BlockPos pos, EnumFacing facing) {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
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
