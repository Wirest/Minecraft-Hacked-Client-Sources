package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.eventstuff.events.InsideBlock;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PushOut;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class VerticalPhase extends Module {
   public VerticalPhase() {
      super("VerticalPhase", -9868951, ModManager.Category.EXPLOITS);
   }

   private boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
               Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (this.isInsideBlock()) {
            this.setColor(-2448096);
         } else {
            this.setColor(-9868951);
         }
      }

      if (event instanceof PostMotion) {
         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
         if (!mc.thePlayer.isCollidedVertically && this.isInsideBlock()) {
            this.setEnabled(false);
         }
      } else if (event instanceof BlockBB) {
         BlockBB bbox = (BlockBB)event;
         if ((double)bbox.getY() > mc.thePlayer.boundingBox.minY - 0.3D && (double)bbox.getY() < mc.thePlayer.boundingBox.maxY && !this.isInsideBlock() && mc.thePlayer.isCollidedHorizontally) {
            bbox.setBoundingBox((AxisAlignedBB)null);
         }
      } else if (event instanceof RecievePacket) {
         RecievePacket recive = (RecievePacket)event;
         if (recive.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)recive.getPacket()).func_149412_c() == mc.thePlayer.getEntityId() && this.isInsideBlock()) {
            recive.setCancelled(true);
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer var3 = (C03PacketPlayer)sent.getPacket();
         }
      } else if (event instanceof PushOut) {
         ((PushOut)event).setCancelled(true);
      } else if (event instanceof InsideBlock) {
         ((InsideBlock)event).setCancelled(true);
      }

   }
}
