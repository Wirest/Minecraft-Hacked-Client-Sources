package nivia.utils.utils;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import nivia.utils.Helper;

import java.awt.*;
import java.awt.event.InputEvent;

public class PlayerUtils {
	
	public static boolean isMoving() {
		if (Helper.player().motionX != 0 || Helper.player().motionY != 0 || Helper.player().motionZ != 0) 
			return true;
		 else return false;	
	}
    public boolean isInventoryFull() {
        for (int index = 9; index <= 44; index++) {
            ItemStack stack = Helper.mc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                return false;
        }

        return true;
    }
    public double getDistanceToFall(){
		double distance = 0;
		for(double i = Helper.player().posY; i > 0; i -= 0.1){
			if(i < 0)
				break;
			Block block = Helper.blockUtils().getBlock(new BlockPos(Helper.player().posX, i, Helper.player().posZ));
			if(block.getMaterial() != Material.air  && (block.isCollidable()) && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs || block instanceof BlockGlass || block instanceof BlockStainedGlass)){
				if(block instanceof BlockSlab)
					i -= 0.5;
				distance = i;
				break;
			}
		}
		return (Helper.player().posY - distance);
    }
	public boolean MovementInput(){
		return (Helper.mc().gameSettings.keyBindForward.getIsKeyPressed() || Helper.mc().gameSettings.keyBindBack.getIsKeyPressed() ||
		Helper.mc().gameSettings.keyBindLeft.getIsKeyPressed() || Helper.mc().gameSettings.keyBindRight.getIsKeyPressed() || ((Helper.mc().gameSettings.keyBindSneak.getIsKeyPressed() && !Helper.player().isCollidedVertically) || Helper.mc().gameSettings.keyBindJump.getIsKeyPressed()));
	}
    public float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
        EntitySnowball temp = new EntitySnowball(Helper.mc().theWorld);
        temp.posX = x + 0.5D;
        temp.posY = y - 2.7035252353;
        temp.posZ = z + 0.5D;
        temp.posX += (double)facing.getDirectionVec().getX() * 0.25;
        temp.posY += (double)facing.getDirectionVec().getY() * 0.25;
        temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25;
        return aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }

	public void doRealClick() throws AWTException {
		Robot robot = new Robot();
		
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
    public float[] aimAtLocation(double positionX, double positionY, double positionZ) {
        double x = positionX - Helper.mc().thePlayer.posX;
        double y = positionY - Helper.mc().thePlayer.posY;
        double z = positionZ - Helper.mc().thePlayer.posZ;
        double distance = (double) MathHelper.sqrt_double(x * x + z * z);
        return new float[]{(float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F, (float)(-(Math.atan2(y, distance) * 180.0D / 3.141592653589793D)) };
    }
    /**
     * @Author Klintos
     */
	public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
	    double curX = startPos[0];
	    double curY = startPos[1];
	    double curZ = startPos[2];
	    double endX = endPos.getX() + 0.5D;
	    double endY = endPos.getY() + 1.0D;
	    double endZ = endPos.getZ() + 0.5D;
	    
	    double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
	    int count = 0;
	    while (distance > slack) {
	      distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
	      
	      if (count > 120) {
	        break;
	      }
	      boolean next = false;
	      double diffX = curX - endX;
	      double diffY = curY - endY;
	      double diffZ = curZ - endZ;
	      
	      double offset = (count & 0x1) == 0 ? pOffset[0] : pOffset[1];
	      if (diffX < 0.0D) {
	        if (Math.abs(diffX) > offset) {
	          curX += offset;
	        } else {
	          curX += Math.abs(diffX);
	        }
	      }
	      if (diffX > 0.0D) {
	        if (Math.abs(diffX) > offset) {
	          curX -= offset;
	        } else {
	          curX -= Math.abs(diffX);
	        }
	      }
	      if (diffY < 0.0D) {
	        if (Math.abs(diffY) > 0.25D) {
	          curY += 0.25D;
	        } else {
	          curY += Math.abs(diffY);
	        }
	      }
	      if (diffY > 0.0D) {
	        if (Math.abs(diffY) > 0.25D) {
	          curY -= 0.25D;
	        } else {
	          curY -= Math.abs(diffY);
	        }
	      }
	      if (diffZ < 0.0D) {
	        if (Math.abs(diffZ) > offset) {
	          curZ += offset;
	        } else {
	          curZ += Math.abs(diffZ);
	        }
	      }
	      if (diffZ > 0.0D) {
	        if (Math.abs(diffZ) > offset) {
	          curZ -= offset;
	        } else {
	          curZ -= Math.abs(diffZ);
	        }
	      }
	      Minecraft.getMinecraft().getNetHandler().addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
	      count++;
	    }
	}

}
