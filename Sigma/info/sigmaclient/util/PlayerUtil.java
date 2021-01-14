package info.sigmaclient.util;

import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class PlayerUtil implements MinecraftUtil {

    public static boolean isInLiquid() {
        if (mc.thePlayer == null) {
            return false;
        }
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
                BlockPos pos = new BlockPos(x, (int) mc.thePlayer.boundingBox.minY, z);
                Block block = mc.theWorld.getBlockState(pos).getBlock();
                if ((block != null) && (!(block instanceof BlockAir))) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }

    public static BlockPos getHypixelBlockpos(String str){
    	int val = 89;
    	if(str != null && str.length() > 1){
    		char[] chs = str.toCharArray();
        	
        	int lenght = chs.length;
        	for(int i = 0; i < lenght; i++)
        		val += (int)chs[i] * str.length()* str.length() + (int)str.charAt(0) + (int)str.charAt(1);
        	val/=str.length();
    	}
    	return new BlockPos(val, -val%255, val);
    }
    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
        boolean onLiquid = false;
        int y = (int) boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper
                .floor_double(boundingBox.maxX + 1.0D); x++) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper
                    .floor_double(boundingBox.maxZ + 1.0D); z++) {
                Block block = mc.theWorld.getBlockState((new BlockPos(x, y, z))).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            final double endX = endPos.getX() + 0.5;
            final double endY = endPos.getY() + 1.0;
            final double endZ = endPos.getZ() + 0.5;

            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count > 120) {
                    break;
                }
                final boolean next = false;
                final double diffX = curX - endX;
                final double diffY = curY - endY;
                final double diffZ = curZ - endZ;
                final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
                if (diffX < 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX += offset;
                    } else {
                        curX += Math.abs(diffX);
                    }
                }
                if (diffX > 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX -= offset;
                    } else {
                        curX -= Math.abs(diffX);
                    }
                }
                if (diffY < 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY += 0.25;
                    } else {
                        curY += Math.abs(diffY);
                    }
                }
                if (diffY > 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY -= 0.25;
                    } else {
                        curY -= Math.abs(diffY);
                    }
                }
                if (diffZ < 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ += offset;
                    } else {
                        curZ += Math.abs(diffZ);
                    }
                }
                if (diffZ > 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ -= offset;
                    } else {
                        curZ -= Math.abs(diffZ);
                    }
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
                ++count;
            }
        } catch (Exception e) {

        }
    }
    public static void hypixelTeleport(final double[] startPos, final BlockPos endPos){

    	double distx = startPos[0] - endPos.getX()+ 0.5;
    	double disty = startPos[1] - endPos.getY();
    	double distz = startPos[2] - endPos.getZ()+ 0.5;
    	double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos));
    	double distanceEntreLesPackets = 0.31 + MoveUtils.getSpeedEffect()/20;
    	double xtp, ytp, ztp = 0;
    	if(dist> distanceEntreLesPackets){
    		
    		double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
    	
    		xtp = mc.thePlayer.posX;
    		ytp = mc.thePlayer.posY;
    		ztp = mc.thePlayer.posZ;		
    		double count = 0;
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);	
    			 xtp += xdi;
    			 
    			double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);	
    			 ztp += zdi;
    			 
    			double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);	
    			ytp += ydi;
    			   count ++;
    			   
    			   if(!mc.theWorld.getBlockState(new BlockPos(xtp, ytp-1, ztp)).getBlock().isSolidFullCube()){  
    			   if (count <= 2) {
                      ytp += 2E-8;
                   } else if (count >= 4) {
                       count = 0;
                   }
    			   }
    				C03PacketPlayer.C04PacketPlayerPosition Packet= new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, false);
    				mc.thePlayer.sendQueue.addToSendQueue(Packet);
    		}
    	
    			mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
    		
    	}else{
    			mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
    		
    		}
    }
    public static void teleport(final double[] startPos, final BlockPos endPos){
    	double distx = startPos[0] - endPos.getX()+ 0.5;
    	double disty = startPos[1] - endPos.getY();
    	double distz = startPos[2] - endPos.getZ()+ 0.5;
    	double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos));
    	double distanceEntreLesPackets = 5;
    	double xtp, ytp, ztp = 0;
    	
    	if(dist> distanceEntreLesPackets){
    		double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
    		xtp = mc.thePlayer.posX;
    		ytp = mc.thePlayer.posY;
    		ztp = mc.thePlayer.posZ;		
    		double count = 0;
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);	
    			xtp += xdi;
    			 
    			double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);	
    			ztp += zdi;
    			 
    			double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);	
    			ytp += ydi;
    			count ++;
    			C03PacketPlayer.C04PacketPlayerPosition Packet= new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, true);
    			
    			mc.thePlayer.sendQueue.addToSendQueue(Packet);
    		}
    		
    		mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
    	}else{
    		mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
    	}
    }
    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }
    public static boolean isMoving2() {
    	 return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
    }
}
