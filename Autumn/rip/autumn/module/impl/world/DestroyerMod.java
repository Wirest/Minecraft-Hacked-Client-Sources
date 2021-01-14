package rip.autumn.module.impl.world;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;

@Label("Destroyer")
@Category(ModuleCategory.WORLD)
public final class DestroyerMod extends Module {
   private final EnumOption mode;
   public final DoubleOption radius;

   public DestroyerMod() {
      this.mode = new EnumOption("Mode", DestroyerMod.Mode.CAKE);
      this.radius = new DoubleOption("Radius", 5.0D, 3.0D, 6.0D, 1.0D);
      this.addOptions(new Option[]{this.mode, this.radius});
      this.setMode(this.mode);
   }

   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      if (this.mode.getValue() == DestroyerMod.Mode.CAKE && event.getPacket() instanceof S02PacketChat) {
         S02PacketChat packetChat = (S02PacketChat)event.getPacket();
         String text = packetChat.getChatComponent().getUnformattedText();
         if (text.contains("20 seconds") || text.contains("your own")) {
            event.setCancelled();
         }
      }

   }

   @Listener(TickEvent.class)
   public final void onTick(TickEvent event) {
      int range = ((Double)this.radius.getValue()).intValue();

      for(int x = -range; x < range; ++x) {
         for(int y = range; y > -range; --y) {
            for(int z = -range; z < range; ++z) {
               int xPos = (int)mc.thePlayer.posX + x;
               int yPos = (int)mc.thePlayer.posY + y;
               int zPos = (int)mc.thePlayer.posZ + z;
               BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
               Block block = mc.theWorld.getBlockState(blockPos).getBlock();
               if (block.getBlockState().getBlock() == Block.getBlockById(this.getId())) {
                  mc.getNetHandler().addToSendQueueSilent(new C0APacketAnimation());
                  mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                  mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
               }
            }
         }
      }

   }

   private final int getId() {
      switch((DestroyerMod.Mode)this.mode.getValue()) {
      case CAKE:
         return 92;
      case BED:
         return 26;
      default:
         return 0;
      }
   }

   private static enum Mode {
      CAKE,
      BED;
   }
}
