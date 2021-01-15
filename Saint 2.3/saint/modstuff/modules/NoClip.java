package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.eventstuff.Event;
import saint.eventstuff.events.EntityUpdates;
import saint.eventstuff.events.InsideBlock;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PushOut;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class NoClip extends Module {
   public NoClip() {
      super("NoClip", -9868951, ModManager.Category.EXPLOITS);
      this.setTag("No Clip");
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
      if (event instanceof EntityUpdates) {
         if (mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.noClip = true;
         }
      } else if (event instanceof PreMotion) {
         mc.thePlayer.motionY = 1.0E-6D;
         if (this.isInsideBlock()) {
            this.setColor(-2448096);
         } else {
            this.setColor(-9868951);
         }

         if (mc.gameSettings.keyBindSneak.pressed) {
            mc.thePlayer.motionY = -0.15D;
         } else if (mc.gameSettings.keyBindJump.pressed) {
            mc.thePlayer.motionY = 0.15D;
         }

         this.isInsideBlock();
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof S08PacketPlayerPosLook && this.isInsideBlock()) {
            sent.setCancelled(true);
         }
      } else if (event instanceof PushOut) {
         ((PushOut)event).setCancelled(true);
      } else if (event instanceof InsideBlock) {
         ((InsideBlock)event).setCancelled(true);
      }

   }
}
