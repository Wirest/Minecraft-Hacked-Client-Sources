package rip.autumn.module.impl.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.utils.Stopwatch;

@Label("Scaffold")
@Bind("Y")
@Category(ModuleCategory.WORLD)
@Aliases({"scaffold", "scaffoldwalk", "magiccarpet"})
public final class ScaffoldMod extends Module {
   private final List invalidBlocks;
   private final List validBlocks;
   private final BlockPos[] blockPositions;
   private final EnumFacing[] facings;
   private final Stopwatch towerStopwatch;
   private final Random rng;
   private float[] angles;
   private boolean rotating;
   private int slot;

   public ScaffoldMod() {
      this.invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web);
      this.validBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
      this.blockPositions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
      this.facings = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH};
      this.towerStopwatch = new Stopwatch();
      this.rng = new Random();
      this.angles = new float[2];
   }

   public void onEnabled() {
      this.angles[0] = 999.0F;
      this.angles[1] = 999.0F;
      this.towerStopwatch.reset();
      this.slot = mc.thePlayer.inventory.currentItem;
   }

   public void onDisabled() {
      mc.thePlayer.inventory.currentItem = this.slot;
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      WorldClient world = mc.theWorld;
      double yDif = 1.0D;
      ScaffoldMod.BlockData data = null;

      for(double posY = player.posY - 1.0D; posY > 0.0D; --posY) {
         ScaffoldMod.BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
         if (newData != null) {
            yDif = player.posY - posY;
            if (yDif <= 3.0D) {
               data = newData;
               break;
            }
         }
      }

      int slot = -1;
      int blockCount = 0;

      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = player.inventory.getStackInSlot(i);
         if (itemStack != null) {
            int stackSize = itemStack.stackSize;
            if (this.isValidItem(itemStack.getItem()) && stackSize > blockCount) {
               blockCount = stackSize;
               slot = i;
            }
         }
      }

      if (slot == -1) {
      }

      if (data != null && slot != -1) {
         BlockPos pos = data.pos;
         Block block = world.getBlockState(pos.offset(data.face)).getBlock();
         Vec3 hitVec = this.getVec3(data);
         if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
            return;
         }

         switch(event.getType()) {
         case PRE:
            event.setPitch(79.44F);
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               if (!player.isMoving()) {
                  player.motionX = 0.0D;
                  player.motionY = 0.41982D;
                  player.motionZ = 0.0D;
                  if (this.towerStopwatch.elapsed(1500L)) {
                     player.motionY = -0.28D;
                     this.towerStopwatch.reset();
                  }
               }
            } else {
               this.towerStopwatch.reset();
            }
            break;
         case POST:
            int last = player.inventory.currentItem;
            player.inventory.currentItem = slot;
            if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos, data.face, hitVec)) {
               player.swingItem();
            }

            player.inventory.currentItem = last;
         }
      }

   }

   private boolean isBlockUnder(double yOffset) {
      EntityPlayerSP player = mc.thePlayer;
      return !this.validBlocks.contains(mc.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
   }

   private Vec3 getVec3(ScaffoldMod.BlockData data) {
      BlockPos pos = data.pos;
      EnumFacing face = data.face;
      double x = (double)pos.getX() + 0.5D;
      double y = (double)pos.getY() + 0.5D;
      double z = (double)pos.getZ() + 0.5D;
      x += (double)face.getFrontOffsetX() / 2.0D;
      z += (double)face.getFrontOffsetZ() / 2.0D;
      y += (double)face.getFrontOffsetY() / 2.0D;
      if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
         y += this.randomNumber(0.49D, 0.5D);
      } else {
         x += this.randomNumber(0.3D, -0.3D);
         z += this.randomNumber(0.3D, -0.3D);
      }

      if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
         z += this.randomNumber(0.3D, -0.3D);
      }

      if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
         x += this.randomNumber(0.3D, -0.3D);
      }

      return new Vec3(x, y, z);
   }

   private double randomNumber(double max, double min) {
      return Math.random() * (max - min) + min;
   }

   private ScaffoldMod.BlockData getBlockData(BlockPos pos) {
      BlockPos[] blockPositions = this.blockPositions;
      EnumFacing[] facings = this.facings;
      WorldClient world = mc.theWorld;
      BlockPos posBelow = new BlockPos(0, -1, 0);
      List validBlocks = this.validBlocks;
      if (!validBlocks.contains(world.getBlockState(pos.add(posBelow)).getBlock())) {
         return new ScaffoldMod.BlockData(pos.add(posBelow), EnumFacing.UP);
      } else {
         int i = 0;

         for(int blockPositionsLength = blockPositions.length; i < blockPositionsLength; ++i) {
            BlockPos blockPos = pos.add(blockPositions[i]);
            if (!validBlocks.contains(world.getBlockState(blockPos).getBlock())) {
               return new ScaffoldMod.BlockData(blockPos, facings[i]);
            }

            for(int i1 = 0; i1 < blockPositionsLength; ++i1) {
               BlockPos blockPos1 = pos.add(blockPositions[i1]);
               BlockPos blockPos2 = blockPos.add(blockPositions[i1]);
               if (!validBlocks.contains(world.getBlockState(blockPos1).getBlock())) {
                  return new ScaffoldMod.BlockData(blockPos1, facings[i1]);
               }

               if (!validBlocks.contains(world.getBlockState(blockPos2).getBlock())) {
                  return new ScaffoldMod.BlockData(blockPos2, facings[i1]);
               }
            }
         }

         return null;
      }
   }

   private boolean isValidItem(Item item) {
      if (item instanceof ItemBlock) {
         ItemBlock iBlock = (ItemBlock)item;
         Block block = iBlock.getBlock();
         return !this.invalidBlocks.contains(block);
      } else {
         return false;
      }
   }

   public static ScaffoldMod getInstance() {
      return (ScaffoldMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(ScaffoldMod.class);
   }

   private static class BlockData {
      public final BlockPos pos;
      public final EnumFacing face;

      private BlockData(BlockPos pos, EnumFacing face) {
         this.pos = pos;
         this.face = face;
      }
   }
}
