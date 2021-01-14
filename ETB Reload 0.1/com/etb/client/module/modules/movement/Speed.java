package com.etb.client.module.modules.movement;

import java.awt.Color;

import com.etb.client.utils.value.impl.NumberValue;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.MotionEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;

import net.minecraft.potion.Potion;

public class Speed extends Module {
    private int stage = 1, stageOG = 1;
    private double moveSpeed, lastDist, moveSpeedOG, lastDistOG;
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.HYPIXEL);
    private NumberValue<Double> boost = new NumberValue("Boost", 1.2, 0.1, 5.0, 0.1);


    public Speed() {
        super("Speed", Category.MOVEMENT, new Color(0, 255, 0, 255).getRGB());
        setDescription("Zoomie zoom");
        addValues(mode);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }


    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        final boolean tick = mc.thePlayer.ticksExisted % 2 == 0;
        if (mode.getValue() == Mode.ONGROUND) {
            if (event.isPre()) {
                if (mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0) {
                    if (mc.thePlayer.onGround && !tick) {
                        event.setY(event.getY() + 0.4124845687456423);
                    }
                    if (tick) {
                        if (!mc.thePlayer.onGround) mc.thePlayer.motionY = -1.02345234623;
                    }
                    moveSpeedOG *= tick ? 2.12542 : 0.905;
                }
            }
        } else if (mode.getValue() == Mode.HYPIXEL || mode.getValue() == Mode.NCP) {
            lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX)) + ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        double forward = mc.thePlayer.movementInput.moveForward, strafe = mc.thePlayer.movementInput.moveStrafe, yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        switch (mode.getValue()) {
            case BHOP:
                setMoveSpeed(event, 1.2);
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        event.setY(mc.thePlayer.motionY = 0.41);
                    }
                } else {
                    mc.thePlayer.motionX = 0.0;
                    mc.thePlayer.motionZ = 0.0;
                }
                break;
            case ONGROUND:
                switch (stageOG) {
                    case 0:
                        ++stageOG;
                        lastDistOG = 0.0D;
                        break;
                    case 2:
                        break;
                    case 3:
                        moveSpeedOG = lastDistOG - (0.720236434 * (lastDistOG - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stageOG = mc.thePlayer.moveForward == 0.0D && mc.thePlayer.moveStrafing == 0.0D ? 0 : 1;
                        }
                        moveSpeedOG = lastDistOG - lastDistOG / 159.0213245D;
                        break;
                }
                moveSpeedOG = mc.thePlayer.isInWater() ? 0 : Math.max(moveSpeedOG, getBaseMoveSpeed());
                double forward3 = mc.thePlayer.movementInput.moveForward;
                double strafe3 = mc.thePlayer.movementInput.moveStrafe;
                float yaw3 = mc.thePlayer.rotationYaw;
                if (forward3 == 0.0D && strafe3 == 0.0D) {
                } else if (forward3 != 0.0D) {
                    if (strafe3 >= 1.0D) {
                        yaw3 += forward3 > 0.0D ? -45.0F : 45.0F;
                        strafe3 = 0.0D;
                    } else if (strafe3 <= -1.0D) {
                        yaw3 += forward3 > 0.0D ? 45.0F : -45.0F;
                        strafe3 = 0.0D;
                    }
                    if (forward3 > 0.0D) {
                        forward3 = 1.0D;
                    } else if (forward3 < 0.0D) {
                        forward3 = -1.0D;
                    }
                }
                event.setX((forward3 * moveSpeedOG * Math.cos(Math.toRadians(yaw3 + 90.0D)) + strafe3 * moveSpeedOG * Math.sin(Math.toRadians(yaw3 + 90.0D))) * 0.99479567D);
                event.setZ((forward3 * moveSpeedOG * Math.sin(Math.toRadians(yaw3 + 90.0D)) - strafe3 * moveSpeedOG * Math.cos(Math.toRadians(yaw3 + 90.0D))) * 0.9946797684D);
                ++stageOG;
                break;
            case HYPIXEL:
            case NCP:
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        double motionY = 0.40123128;
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= 2.149;
                        }
                        break;
                    case 3:
                        moveSpeed = lastDist - ((mode.getValue() == Mode.NCP ? 0.7593473 : 0.7095) * (lastDist - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                event.setX((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
                event.setZ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
                ++stage;
                break;
            case VANILLA:
                setMoveSpeed(event, boost.getValue());
                break;
        }
    }

    private void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }

    public enum Mode {
        HYPIXEL, NCP, ONGROUND, VANILLA, BHOP
    }
}
