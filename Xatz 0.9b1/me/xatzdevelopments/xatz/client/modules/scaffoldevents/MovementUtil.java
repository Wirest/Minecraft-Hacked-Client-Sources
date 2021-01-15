package me.xatzdevelopments.xatz.client.modules.scaffoldevents;


import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MovementUtil {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public final double getSpeed() {
		return Math.sqrt(MC.thePlayer.motionX * MC.thePlayer.motionX + MC.thePlayer.motionZ * MC.thePlayer.motionZ);
	}
	
    public final boolean isMoving() {
        return MC.thePlayer != null && (MC.thePlayer.movementInput.moveForward != 0 || MC.thePlayer.movementInput.moveStrafe != 0);
    }
    
    public final void doStrafe(double speed) {
        if(!isMoving())  return;

        final double yaw = getYaw(true);
        MC.thePlayer.motionX = -Math.sin(yaw) * speed;
        MC.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public final void doStrafe(double speed, double yaw) {
        MC.thePlayer.motionX = -Math.sin(yaw) * speed;
        MC.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public final void doStrafe() {
    	doStrafe(getSpeed());
    }
    
    public final double getYaw(boolean strafing) {
        float rotationYaw = MC.thePlayer.rotationYaw;
        float forward = 1F;

        final double moveForward = MC.thePlayer.movementInput.moveForward;
        final double moveStrafing = MC.thePlayer.movementInput.moveStrafe;
        final float yaw = MC.thePlayer.rotationYaw;
        
        if (moveForward < 0) {
        	rotationYaw += 180F;
        }

        if (moveForward < 0) {
        	forward = -0.5F;
        } else if(moveForward > 0) {
        	forward = 0.5F;
        }

        if (moveStrafing > 0) {
        	rotationYaw -= 90F * forward;
        } else if(moveStrafing < 0) {
        	rotationYaw += 90F * forward;
        }

        return Math.toRadians(rotationYaw);
    }
    
    public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
        double forward = MC.thePlayer.movementInput.moveForward;
        double strafe = MC.thePlayer.movementInput.moveStrafe;
        float yaw = MC.thePlayer.rotationYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setMotionX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setMotionZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public final void forward(double length, double y) {
        final double yaw = getYaw(false);
        MC.thePlayer.setPosition(MC.thePlayer.posX + (-Math.sin(yaw) * length), MC.thePlayer.posY + y, MC.thePlayer.posZ + (Math.cos(yaw) * length));
    }
    
    public final void stop(boolean y) {
    	MC.thePlayer.motionX = 0;
    	MC.thePlayer.motionZ = 0;
    	if (y) MC.thePlayer.motionY = 0;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (MC.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MC.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public double getJumpBoostModifier(double baseJumpHeight) {
        if (MC.thePlayer.isPotionActive(Potion.jump)) {
            final int amplifier = MC.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }

}
