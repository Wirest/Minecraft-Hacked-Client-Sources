package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import net.minecraft.client.Minecraft;

public class EventMove extends Event {
	
	public EventMove(double x, double y, double z) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	public double x;
	public double y;
	public double z;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
    public void setSpeed(double moveSpeed) {
        float forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        float strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if ((double)forward == 0.0D && (double)strafe == 0.0D) {
            Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
            Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
        } else {
            if ((double)forward != 0.0D) {
                if (strafe > 0.0F) {
                    yaw += (float)((double)forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0F) {
                    yaw += (float)((double)forward > 0.0D ? 45 : -45);
                }

                strafe = 0.0F;
                if (forward > 0.0F) {
                    forward = 1.0F;
                } else if (forward < 0.0F) {
                    forward = -1.0F;
                }
            }

            double xDist = (double)forward * moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + (double)strafe * moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
            double zDist = (double)forward * moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - (double)strafe * moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
            this.setX(Minecraft.getMinecraft().thePlayer.motionX = xDist);
            this.setZ(Minecraft.getMinecraft().thePlayer.motionZ = zDist);
        }

    }
    
    public void setSpeed(double moveSpeed, float yaw) {
        float forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        float strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        if ((double)forward == 0.0D && (double)strafe == 0.0D) {
            Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
            Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
        } else {
            if ((double)forward != 0.0D) {
                if (strafe > 0.0F) {
                    yaw += (float)((double)forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0F) {
                    yaw += (float)((double)forward > 0.0D ? 45 : -45);
                }

                strafe = 0.0F;
                if (forward > 0.0F) {
                    forward = 1.0F;
                } else if (forward < 0.0F) {
                    forward = -1.0F;
                }
            }

            double xDist = (double)forward * moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + (double)strafe * moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
            double zDist = (double)forward * moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - (double)strafe * moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
            this.setX(Minecraft.getMinecraft().thePlayer.motionX = xDist);
            this.setZ(Minecraft.getMinecraft().thePlayer.motionZ = zDist);
        }

    }
    
}
