package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.InvUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class ChestAura extends Module {
   private static final List openedBlocks = new CopyOnWriteArrayList();
   private final TimeHelper time = new TimeHelper();
   private boolean shouldBreak = false;
   BlockPos globalPos;
   static boolean closed;

   public ChestAura() {
      super("ChestAura", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
      this.time.reset();
   }

   public void onDisable() {
      super.onDisable();
      if (Minecraft.theWorld != null) {
         openedBlocks.clear();
      }

   }

   @EventTarget
   public void onRespawn(EventRespawn eventRespawn) {
      this.setEnabled(false);
      ChatUtils.sendMessageToPlayer("Auto Disabled \247a" + this.getName() + "\247r by Respawn.");
   }

   @EventTarget
   private void onUpdatePre(EventUpdate event) {
      if ((Wrapper.mc.currentScreen instanceof GuiContainer || closed) && this.globalPos != null) {
         closed = false;
         openedBlocks.add(this.globalPos);
         if (this.searchNearChest(this.globalPos) != null) {
            openedBlocks.add(this.searchNearChest(this.globalPos));
         }
      }

      if (event.state == EventState.PRE) {
         if (Wrapper.mc.currentScreen instanceof GuiContainer || this.shouldBreak || !this.time.hasPassed(500.0D) || InvUtils.isFullInv() || Aura.isEntityValid(Aura.currentTarget, false)) {
            return;
         }

         this.time.reset();
         byte radius = 4;

         for(int y = 4; y >= -radius; --y) {
            for(int x = -radius; x <= radius; ++x) {
               for(int z = -radius; z <= radius; ++z) {
                  BlockPos pos = new BlockPos(Minecraft.thePlayer.posX - 0.5D + (double)x, Minecraft.thePlayer.posY - 0.5D + (double)y, Minecraft.thePlayer.posZ - 0.5D + (double)z);
                  Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
                  if (!openedBlocks.contains(pos) && this.getFacingDirection(pos) != null && Minecraft.thePlayer.getDistance(Minecraft.thePlayer.posX + (double)x, Minecraft.thePlayer.posY + (double)y, Minecraft.thePlayer.posZ + (double)z) < (double)Wrapper.mc.playerController.getBlockReachDistance() && block instanceof BlockChest) {
                     this.shouldBreak = true;
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
      if (event.state == EventState.POST && this.globalPos != null && !(Wrapper.mc.currentScreen instanceof GuiContainer) && this.shouldBreak && (direction = this.getFacingDirection(this.globalPos)) != null) {
         if (Wrapper.mc.playerController.blockHitDelay > 1) {
            Wrapper.mc.playerController.blockHitDelay = 1;
         }

         Minecraft.thePlayer.swingItem();
         Wrapper.mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getCurrentEquippedItem(), this.globalPos, direction, new Vec3((double)this.globalPos.getX(), (double)this.globalPos.getY(), (double)this.globalPos.getZ()));
         this.shouldBreak = false;
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

   private BlockPos searchNearChest(BlockPos src) {
      if (Minecraft.theWorld.getBlockState(src.add(0, 0, 1)).getBlock().getMaterial() == Minecraft.theWorld.getBlockState(src).getBlock().getMaterial()) {
         return src.add(0, 0, 1);
      } else if (Minecraft.theWorld.getBlockState(src.add(1, 0, 0)).getBlock().getMaterial() == Minecraft.theWorld.getBlockState(src).getBlock().getMaterial()) {
         return src.add(1, 0, 0);
      } else if (Minecraft.theWorld.getBlockState(src.add(-1, 0, 0)).getBlock().getMaterial() == Minecraft.theWorld.getBlockState(src).getBlock().getMaterial()) {
         return src.add(-1, 0, 0);
      } else {
         return Minecraft.theWorld.getBlockState(src.add(0, 0, -1)).getBlock().getMaterial() == Minecraft.theWorld.getBlockState(src).getBlock().getMaterial() ? src.add(0, 0, -1) : null;
      }
   }

   public static List getOpenedBlocks() {
      return openedBlocks;
   }

   public static void setClosed(boolean closed) {
      closed = closed;
   }
}
