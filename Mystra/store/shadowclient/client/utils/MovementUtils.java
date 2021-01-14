package store.shadowclient.client.utils;

import store.shadowclient.client.event.events.EventUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public final class MovementUtils extends MinecraftInstance {

    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }
    
    public final static void doStrafe(double speed) {
        if(!isMoving())  return;

        final double yaw = getYaw(true);
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public final static void doStrafe(double speed, double yaw) {
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static void setSpeed(EventUpdate event, double moveSpeed) {
        setSpeed(event, moveSpeed, mc.thePlayer.rotationYaw, (double)mc.thePlayer.movementInput.moveStrafe, (double)mc.thePlayer.movementInput.moveForward);
     }
    
    public final static double getYaw(boolean strafing) {
        float rotationYaw = strafing ? mc.thePlayer.rotationYawHead : mc.thePlayer.rotationYaw;
        float forward = 1F;

        final double moveForward = mc.thePlayer.movementInput.moveForward;
        final double moveStrafing = mc.thePlayer.movementInput.moveStrafe;
        final float yaw = mc.thePlayer.rotationYaw;
        
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
    
    public static void setSpeed(EventUpdate moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (pseudoForward != 0.0D) {
           if (pseudoStrafe > 0.0D) {
              yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? -45 : 45);
           } else if (pseudoStrafe < 0.0D) {
              yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? 45 : -45);
           }

           strafe = 0.0D;
           if (pseudoForward > 0.0D) {
              forward = 1.0D;
           } else if (pseudoForward < 0.0D) {
              forward = -1.0D;
           }
        }

        if (strafe > 0.0D) {
           strafe = 1.0D;
        } else if (strafe < 0.0D) {
           strafe = -1.0D;
        }

        double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
     }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
           baseSpeed *= 1.0D + 0.2D * (double)(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
     }
    
    public static boolean isOnGround2(final double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public final static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0);
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
           int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
           baseJumpHeight += (double)((float)(amplifier + 1) * 0.1F);
        }

        return baseJumpHeight;
     }

    public static boolean hasMotion() {
        return mc.thePlayer.motionX != 0D && mc.thePlayer.motionZ != 0D && mc.thePlayer.motionY != 0D;
    }

    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static boolean isOnGround(final double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(yaw) * length), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(yaw) * length));
    }
    
    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
    
    public static void setMotion(final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
        else {
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
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
}