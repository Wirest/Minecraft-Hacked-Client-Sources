package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Events.EventBBSet;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventPushOutOfBlocks;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class ArisPhase extends Module {
   public TimeHelper timer = new TimeHelper();
   boolean vanilla = false;
   private int resetNext;

   public ArisPhase() {
      super("ArisPhase", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onMotion(EventMove event) {
      if (this.vanilla) {
         --this.resetNext;
         double var12 = 0.0D;
         double zOff = 0.0D;
         double multiplier = 2.6D;
         double mx = Math.cos(Math.toRadians((double)(ClientUtils.player().rotationYaw + 90.0F)));
         double mz = Math.sin(Math.toRadians((double)(ClientUtils.player().rotationYaw + 90.0F)));
         MovementInput var10000 = ClientUtils.player().movementInput;
         double var13 = (double)MovementInput.moveForward * 2.6D * mx;
         MovementInput var10001 = ClientUtils.player().movementInput;
         var12 = var13 + (double)MovementInput.moveStrafe * 2.6D * mz;
         var10000 = ClientUtils.player().movementInput;
         var13 = (double)MovementInput.moveForward * 2.6D * mz;
         var10001 = ClientUtils.player().movementInput;
         zOff = var13 - (double)MovementInput.moveStrafe * 2.6D * mx;
         if (this.isInsideBlock() && ClientUtils.player().isSneaking()) {
            this.resetNext = 1;
         }

         if (this.resetNext > 0) {
            ClientUtils.player().boundingBox.offsetAndUpdate(var12, 0.0D, zOff);
         }
      } else if (this.timer.hasPassed(150.0D) && ClientUtils.player().isCollidedHorizontally) {
         float var121 = ClientUtils.player().rotationYaw;
         if (ClientUtils.player().moveForward < 0.0F) {
            var121 += 180.0F;
         }

         if (ClientUtils.player().moveStrafing > 0.0F) {
            var121 -= 90.0F * (ClientUtils.player().moveForward > 0.0F ? 0.5F : (ClientUtils.player().moveForward < 0.0F ? -0.5F : 1.0F));
         }

         if (ClientUtils.player().moveStrafing < 0.0F) {
            var121 += 90.0F * (ClientUtils.player().moveForward > 0.0F ? 0.5F : (ClientUtils.player().moveForward < 0.0F ? -0.5F : 1.0F));
         }

         double horizontalMultiplier = 0.3D;
         double xOffset = (double)((float)Math.cos((double)(var121 + 90.0F) * 3.141592653589793D / 180.0D)) * 0.3D;
         double zOffset = (double)((float)Math.sin((double)(var121 + 90.0F) * 3.141592653589793D / 180.0D)) * 0.3D;
         double yOffset = 0.0D;

         for(int i = 0; i < 3; ++i) {
            yOffset += 0.01D;
            ClientUtils.mc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY - yOffset, ClientUtils.player().posZ, ClientUtils.player().onGround));
            ClientUtils.mc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX + xOffset * (double)i, ClientUtils.player().posY, ClientUtils.player().posZ + zOffset * (double)i, ClientUtils.player().onGround));
         }
      } else if (!ClientUtils.player().isCollidedHorizontally) {
         this.timer.reset();
      }

   }

   @EventTarget
   public void onSetBB(EventBBSet event) {
      label29: {
         if (this.isInsideBlock()) {
            ClientUtils.mc();
            if (Minecraft.gameSettings.keyBindJump.pressed) {
               break label29;
            }
         }

         if (this.isInsideBlock() || event.boundingBox == null || event.boundingBox.maxY <= ClientUtils.player().boundingBox.minY || !this.vanilla || !ClientUtils.player().isSneaking()) {
            return;
         }
      }

      event.boundingBox = null;
   }

   @EventTarget
   public void onPush(EventPushOutOfBlocks event) {
      if (this.isInsideBlock()) {
         event.setCancelled(true);
      }

   }

   private boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; ++z) {
               Minecraft.getMinecraft();
               Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  ClientUtils.mc();
                  WorldClient var10001 = Minecraft.theWorld;
                  BlockPos var10002 = new BlockPos(x, y, z);
                  ClientUtils.mc();
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(var10001, var10002, Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (block instanceof BlockHopper) {
                     boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
                  }

                  if (boundingBox != null && ClientUtils.player().boundingBox.intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }
}
