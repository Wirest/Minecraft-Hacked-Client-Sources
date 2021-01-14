package rip.autumn.module.impl.world;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.utils.Stopwatch;

@Label("Chest Aura")
@Category(ModuleCategory.WORLD)
@Aliases({"chestaura", "cheststealer"})
public class ChestAura extends Module {
   private static final int REMOVE_SIZE = 128;
   public final DoubleOption range = new DoubleOption("Range", 4.0D, 3.0D, 6.0D, 0.05D);
   public final BoolOption autoOpen = new BoolOption("Auto Open", true);
   public final BoolOption autoSteal = new BoolOption("Auto Steal", true);
   public final DoubleOption delay = new DoubleOption("Delay", 150.0D, 0.0D, 500.0D, 5.0D);
   private final Set openedChests = new HashSet();
   private final Stopwatch openStopwatch = new Stopwatch();
   private final Stopwatch itemStealStopwatch = new Stopwatch();
   private AuraMod aura;

   public ChestAura() {
      this.addOptions(new Option[]{this.range, this.autoOpen, this.autoSteal, this.delay});
   }

   public void onEnabled() {
      if (this.aura == null) {
         this.aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      }

      this.openStopwatch.reset();
      this.itemStealStopwatch.reset();
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      boolean auraSafe = this.aura.getTarget() == null || !this.aura.isEnabled();
      if (auraSafe) {
         Minecraft minecraft = mc;
         EntityPlayerSP player = minecraft.thePlayer;
         WorldClient world = minecraft.theWorld;
         PlayerControllerMP controller = minecraft.playerController;
         boolean pre = event.isPre();
         int index;
         if (pre && minecraft.currentScreen instanceof GuiChest && this.autoSteal.getValue()) {
            GuiChest chest = (GuiChest)minecraft.currentScreen;
            if (this.isValidChest(chest)) {
               if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                  player.closeScreen();
                  this.itemStealStopwatch.reset();
                  this.openStopwatch.reset();
                  return;
               }

               for(index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                  ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                  if (stack != null && this.isValidItem(stack) && this.itemStealStopwatch.elapsed(((Double)this.delay.getValue()).longValue())) {
                     controller.windowClick(chest.inventorySlots.windowId, index, 0, 1, player);
                     this.itemStealStopwatch.reset();
                  }
               }
            }
         }

         if (!event.isPre() && minecraft.currentScreen == null && this.autoOpen.getValue()) {
            List loadedTileEntityList = world.loadedTileEntityList;
            index = 0;

            for(int loadedTileEntityListSize = loadedTileEntityList.size(); index < loadedTileEntityListSize; ++index) {
               TileEntity tile = (TileEntity)loadedTileEntityList.get(index);
               BlockPos pos = tile.getPos();
               if (tile instanceof TileEntityChest && this.getDistanceToBlockPos(pos) <= (Double)this.range.getValue() && !this.openedChests.contains(tile) && this.openStopwatch.elapsed(500L) && controller.onPlayerRightClick(player, world, player.getHeldItem(), pos, EnumFacing.DOWN, this.getVec(tile.getPos()))) {
                  minecraft.getNetHandler().addToSendQueueSilent(new C0APacketAnimation());
                  this.andAndEnsureSetSize(this.openedChests, tile);
                  this.openStopwatch.reset();
                  return;
               }
            }
         }
      }

   }

   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      if (event.getPacket() instanceof S18PacketEntityTeleport) {
      }

   }

   private void andAndEnsureSetSize(Set set, TileEntity chest) {
      if (set.size() > 128) {
         set.clear();
      }

      set.add(chest);
   }

   private Vec3 getVec(BlockPos pos) {
      return new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
   }

   private double getDistanceToBlockPos(BlockPos pos) {
      return mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
   }

   private boolean isValidChest(GuiChest chest) {
      return chest.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest") || chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("LOW");
   }

   private boolean isValidItem(ItemStack itemStack) {
      return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock;
   }

   private boolean isChestEmpty(GuiChest chest) {
      for(int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
         ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
         if (stack != null && this.isValidItem(stack)) {
            return false;
         }
      }

      return true;
   }

   private boolean isInventoryFull() {
      for(int index = 9; index <= 44; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack == null) {
            return false;
         }
      }

      return true;
   }
}
