package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import saint.eventstuff.Event;
import saint.eventstuff.events.Attack;
import saint.eventstuff.events.BlockBreaking;
import saint.eventstuff.events.PostAttack;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;

public class Reach extends Module {
   private double prevX;
   private double prevY;
   private double prevZ;
   private boolean viable = false;
   private double x;
   private double y;
   private double z;
   private EnumFacing side;
   private BlockPos pos;

   public Reach() {
      super("Reach", -4094073, ModManager.Category.EXPLOITS);
   }

   public void onEvent(Event event) {
      if (event instanceof saint.eventstuff.events.Reach) {
         ((saint.eventstuff.events.Reach)event).setReach(19.0F);
      } else if (event instanceof BlockBreaking && ((BlockBreaking)event).getState() == BlockBreaking.EnumBlock.CLICK) {
         this.side = ((BlockBreaking)event).getSide();
         this.pos = ((BlockBreaking)event).getPos();
         if (this.side != null && this.pos != null && mc.thePlayer.getDistance((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getX()) > 5.0D) {
            float dir = mc.thePlayer.rotationYaw;
            if (mc.thePlayer.moveForward < 0.0F) {
               dir += 180.0F;
            }

            if (mc.thePlayer.moveStrafing > 0.0F) {
               dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
            }

            if (mc.thePlayer.moveStrafing < 0.0F) {
               dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
            }

            double hOff = 10.0D;
            float xD = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
            float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
            this.blinkToPos(mc.thePlayer.getPosition(), new BlockPos(mc.thePlayer.posX + (double)xD, mc.thePlayer.posY, mc.thePlayer.posZ + (double)zD), true);
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, this.side));
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.side));
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.pos, this.side));
            this.blinkToPos(new BlockPos(mc.thePlayer.posX + (double)xD, mc.thePlayer.posY, mc.thePlayer.posZ + (double)zD), mc.thePlayer.getPosition(), true);
         }
      }

      if (event instanceof Attack) {
         this.prevX = mc.thePlayer.posX;
         this.prevY = mc.thePlayer.posY;
         this.prevZ = mc.thePlayer.posZ;
         this.blinkToPos(mc.thePlayer.getPosition(), ((Attack)event).getTarget().getPosition(), true);
      }

      if (event instanceof PostAttack) {
         this.blinkToPos(new BlockPos(this.prevX, this.prevY, this.prevZ), mc.thePlayer.getPosition(), true);
      }

   }

   public void blinkToPos(BlockPos fromPos, BlockPos toPos, boolean noCheat) {
      double x = (double)fromPos.getX();
      double y = (double)fromPos.getY();
      double z = (double)fromPos.getZ();
      double toX = (double)toPos.getX();
      double toY = (double)toPos.getY();
      double toZ = (double)toPos.getZ();

      for(Block getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ)); !getBlockAtPos.getMaterial().equals(Material.air); getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ))) {
         ++toY;
      }

      double allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);
      int loops = 0;
      boolean error = false;
      String goUpOrDown = "Neutral";
      if (y - toY < 0.0D) {
         goUpOrDown = "Up";
      }

      if (y - toY > 0.0D) {
         goUpOrDown = "Down";
      }

      while(allDifference > 7.0D) {
         allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);
         Block gbap = BlockHelper.getBlockAtPos(new BlockPos(x, y + 0.56D, z));
         if (!gbap.isPassable(mc.theWorld, new BlockPos(x, y + 0.56D, z))) {
            error = true;
            break;
         }

         if (loops > 10000) {
            error = true;
            break;
         }

         double differenceX = x - toX;
         double differenceY = y - toY;
         double differenceZ = z - toZ;
         double differenceXZ = Math.abs(differenceX) + Math.abs(differenceZ);
         boolean handleYFirst = goUpOrDown.equals("Up") && Math.abs(differenceY) > 0.0D;
         boolean handleYLast = goUpOrDown.equals("Down") && Math.abs(differenceY) > 0.0D && differenceXZ == 0.0D;
         if (handleYFirst) {
            if (Math.abs(differenceY) > 1.0D) {
               ++y;
            } else {
               y += Math.abs(differenceY);
            }
         } else {
            if (differenceX < 0.0D) {
               if (Math.abs(differenceX) > 1.0D) {
                  ++x;
               } else {
                  x += Math.abs(differenceX);
               }
            }

            if (differenceX > 0.0D) {
               if (Math.abs(differenceX) > 1.0D) {
                  --x;
               } else {
                  x -= Math.abs(differenceX);
               }
            }

            if (differenceZ < 0.0D) {
               if (Math.abs(differenceZ) > 1.0D) {
                  ++z;
               } else {
                  z += Math.abs(differenceZ);
               }
            }

            if (differenceZ > 0.0D) {
               if (Math.abs(differenceZ) > 1.0D) {
                  --z;
               } else {
                  z -= Math.abs(differenceZ);
               }
            }

            if (handleYLast) {
               if (Math.abs(differenceY) > 1.0D) {
                  --y;
               } else {
                  y -= Math.abs(differenceY);
               }
            }
         }

         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, mc.thePlayer.onGround));
         ++loops;
      }

   }
}
