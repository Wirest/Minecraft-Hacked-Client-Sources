
package me.memewaredevs.client.util.movement;

import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.module.combat.Aura;
import me.memewaredevs.client.module.combat.TargetStrafe;
import me.memewaredevs.client.util.combat.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public final class MovementUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setMoveSpeed(double moveSpeed) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        double forward = movementInput.getForward();
        boolean targetStrafe = TargetStrafe.canStrafe();
        if (targetStrafe) {
            if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= Module.getInstance(Aura.class).getDouble("Range") - 2.3) {
                forward = 0;
            } else {
                forward = 1;
            }
        }
        double strafe = targetStrafe ? TargetStrafe.dir : movementInput.getStrafe();
        double yaw = targetStrafe ? RotationUtils.getRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
        if (forward > 0) {
            forward = 1;
        } else if (forward < 0) {
            forward = -1;
        }
        if (strafe > 0) {
            strafe = 1;
        } else if (strafe < 0) {
            strafe = -1;
        }
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.thePlayer.motionX = 0.0;
            MovementUtils.mc.thePlayer.motionZ = 0.0;
        }
        if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.6398355709958845);
            strafe *= Math.cos(0.6398355709958845);
        }
        final double sin = -Math.sin(Math.toRadians(yaw));
        final double cos = Math.cos(Math.toRadians(yaw));
        MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * sin + strafe * moveSpeed * cos;
        MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * cos - strafe * moveSpeed * sin;
    }

    public static double getPlayerSpeedCurr() {
        return Math.hypot(MovementUtils.mc.thePlayer.motionX, MovementUtils.mc.thePlayer.motionZ);
    }

    public static void offsetXZ(final double speed) {
        final double dX = -Math.sin(getDirection()) * speed;
        final double dZ = Math.cos(getDirection()) * speed;
        //use setPosition so you don't get an ugly visual effect.
        mc.thePlayer.setPosition(mc.thePlayer.posX + dX, mc.thePlayer.posY, mc.thePlayer.posZ + dZ);
    }
    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        boolean targetStrafe = TargetStrafe.canStrafe();
        if (targetStrafe) {
            if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= Module.getInstance(Aura.class).getDouble("Range") - 2.3) {
                forward = 0;
            } else {
                forward = 1;
            }
        }
        double strafe = targetStrafe ? TargetStrafe.dir : mc.thePlayer.movementInput.getStrafe();
        double yaw = targetStrafe ? RotationUtils.getRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
        if (forward < 0F)
            forward = -0.5F;
        else if (forward > 0F)
            forward = 0.5F;

        if (strafe > 0F)
            rotationYaw -= 90F * forward;

        if (strafe < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double[] getDirXZ(double speed, double mult) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        double forward = movementInput.getForward();
        boolean targetStrafe = TargetStrafe.canStrafe();
        if (targetStrafe) {
            if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= Module.getInstance(Aura.class).getDouble("Range") - 2.3) {
                forward = 0;
            } else {
                forward = 1;
            }
        }
        double strafe = targetStrafe ? TargetStrafe.dir : movementInput.getStrafe();
        double yaw = targetStrafe ? RotationUtils.getRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
        if (forward > 0) {
            forward = 1;
        } else if (forward < 0) {
            forward = -1;
        }
        if (strafe > 0) {
            strafe = 1;
        } else if (strafe < 0) {
            strafe = -1;
        }
        if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.6398355709958845);
            strafe *= Math.cos(0.6398355709958845);
        }
        return new double[]{
                forward * speed * -Math.sin(Math.toRadians(yaw))
                        + strafe * speed * Math.cos(Math.toRadians(yaw)) * mult,
                forward * speed * Math.cos(Math.toRadians(yaw))
                        - strafe * speed * -Math.sin(Math.toRadians(yaw)) * mult};
    }

    public static void setMoveSpeed(MovementEvent event, double moveSpeed) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.getForward();
        boolean targetStrafe = TargetStrafe.canStrafe();
        if (targetStrafe) {
            if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= Module.getInstance(Aura.class).getDouble("Range") - 2.3) {
                moveForward = 0;
            } else {
                moveForward = 1;
            }
        }
        double moveStrafe = targetStrafe ? TargetStrafe.dir : movementInput.getStrafe();
        double yaw = targetStrafe ? RotationUtils.getRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            event.setX(0.0D);
            event.setZ(0.0D);
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    yaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    yaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            event.setX(moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin);
            event.setZ(moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos);
        }
    }

    public static double getGroundLevel() {
        for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (!isColliding(box) || !(box.minY <= mc.thePlayer.posY)) {
                continue;
            }
            return i;
        }
        return 0.0;
    }

    public static boolean isColliding(final AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }

    public static double fallPacket() {
        double i;
        for (i = mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
            if (i < getGroundLevel()) {
                i = getGroundLevel();
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
        return i;
    }

    public static void ascendPacket() {
        for (double i = getGroundLevel(); i < mc.thePlayer.posY; i += 8.0) {
            if (i > mc.thePlayer.posY) {
                i = mc.thePlayer.posY;
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
    }
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MovementUtils.mc.thePlayer != null && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed)
                    .getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
}
