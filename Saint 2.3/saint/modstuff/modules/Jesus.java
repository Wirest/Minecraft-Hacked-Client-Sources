package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.TimeHelper;

public class Jesus extends Module {
   private int waterUpdate = 1;
   private int getoutofwater;
   private int timer = 0;
   private final TimeHelper time = new TimeHelper();
   private boolean nextTick;

   public Jesus() {
      super("Jesus", -8355712, ModManager.Category.MOVEMENT);
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBB) {
         if (mc.thePlayer == null) {
            return;
         }

         BlockBB block = (BlockBB)event;
         if (block.getBlock() instanceof BlockLiquid && !BlockHelper.isInLiquid() && !mc.thePlayer.isSneaking()) {
            try {
               if ((double)BlockLiquid.getLiquidHeightPercent(block.getBlock().getMetaFromState(mc.theWorld.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ())))) <= 0.15D) {
                  block.setBoundingBox(AxisAlignedBB.fromBounds((double)block.getX(), (double)block.getY(), (double)block.getZ(), (double)(block.getX() + 1), (double)(block.getY() + 1), (double)(block.getZ() + 1)));
               }
            } catch (Exception var5) {
            }
         }
      } else if (event instanceof PreMotion) {
         if (BlockHelper.isOnLiquid()) {
            this.setColor(-16728065);
         } else {
            this.setColor(-8355712);
         }

         if (BlockHelper.isInLiquid() && mc.thePlayer.isInsideOfMaterial(Material.air) && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.motionY = 0.08D;
         }
      } else if (event instanceof PacketSent) {
         boolean shouldSendPacket = !mc.thePlayer.onGround && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2.0D, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 3.0D, mc.thePlayer.posZ)) instanceof BlockLiquid;
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            if (shouldSendPacket) {
               player.field_149474_g = true;
            }

            if (BlockHelper.isOnLiquid()) {
               this.nextTick = !this.nextTick;
               if (this.nextTick) {
                  player.y -= 0.01D;
               }
            }
         }
      }

   }

   public boolean getColliding(int i) {
      if (!this.isEnabled()) {
         return false;
      } else {
         int mx = i;
         int mz = i;
         int max = i;
         int maz = i;
         if (mc.thePlayer.motionX > 0.01D) {
            mx = 0;
         } else if (mc.thePlayer.motionX < -0.01D) {
            max = 0;
         }

         if (mc.thePlayer.motionZ > 0.01D) {
            mz = 0;
         } else if (mc.thePlayer.motionZ < -0.01D) {
            maz = 0;
         }

         int xmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX - (double)mx);
         int ymin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY - 1.0D);
         int zmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ - (double)mz);
         int xmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX + (double)max);
         int ymax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY + 1.0D);
         int zmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ + (double)maz);

         for(int x = xmin; x <= xmax; ++x) {
            for(int y = ymin; y <= ymax; ++y) {
               for(int z = zmin; z <= zmax; ++z) {
                  Block block = BlockHelper.getBlockAtPos(new BlockPos(x, y, z));
                  if (block instanceof BlockLiquid && !mc.thePlayer.isInsideOfMaterial(Material.lava) && !mc.thePlayer.isInsideOfMaterial(Material.water)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }
}
