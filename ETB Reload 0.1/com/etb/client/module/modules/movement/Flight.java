package com.etb.client.module.modules.movement;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.MotionEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class Flight extends Module {
    private int level = 1;
    private TimerUtil timer = new TimerUtil();
    private boolean decreasing2, hypixelboost, canboost,nigga;
    private double lastDist, moveSpeed, starty;
    private EnumValue<Modes> mode = new EnumValue("Mode", Modes.Hypixel);
    private BooleanValue viewbob = new BooleanValue("ViewBob", true);
    private NumberValue<Double> flyspeed = new NumberValue("FlySpeed", 2.0, 0.8, 3.5, 0.1);
    private float timervalue;

    public Flight() {
        super("Flight", Category.MOVEMENT, new Color(33, 120, 255, 255).getRGB());
        setDescription("Be a little birdie");
        addValues(viewbob, flyspeed, mode);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        setSpeed(0);
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.motionY = 0;
        nigga = false;
    }

    @Override
    public void onEnable() {
        if (mc.theWorld != null && mc.thePlayer != null) {
            nigga = false;
            level = 1;
            moveSpeed = 0.1D;
            lastDist = 0.0D;
            if (mode.getValue() == Modes.HypixelFast) {
                canboost = true;
                {
                    double motionY = 0.40123128;
                    timervalue = 1.0F;
                    if (mc.thePlayer.onGround) {
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.isCollidedVertically) {
                            if (mc.thePlayer.isPotionActive(Potion.jump))
                                motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            mc.thePlayer.motionY = motionY;
                        }
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.28E-10D, mc.thePlayer.posZ);
                        level = 1;
                        moveSpeed = 0.1D;
                        hypixelboost = true;
                        lastDist = 0.0D;
                    } else {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.26E-10D, mc.thePlayer.posZ);
                    }
                    timer.reset();
                }
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            if (viewbob.getValue()) mc.thePlayer.cameraYaw = 0.15f;
            switch (mode.getValue()) {
                case Hypixel:
                case HypixelFast:
                    lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
                    if (canboost && hypixelboost) {
                        timervalue += decreasing2 ? -0.01 : 0.05;
                        if (timervalue >= 1.4) {
                            decreasing2 = true;
                        }
                        if (timervalue <= 0.9) {
                            decreasing2 = false;
                        }
                        if (timer.reach(2000)) {
                            canboost = false;
                        }
                    }
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
                        mc.thePlayer.motionY = 0.8;
                        mc.thePlayer.motionX *= 0.1;
                        mc.thePlayer.motionZ *= 0.1;
                    }
                    if ((mc.thePlayer.ticksExisted % 2) == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtils.getRandomInRange(0.00000000000001235423532523523532521, 0.0000000000000123542353252352353252 * 10), mc.thePlayer.posZ);
                    }
                    mc.thePlayer.motionY = 0;
                    break;
                case HypixelDamage:
                    if (mc.thePlayer.fallDistance > 0 && !nigga) {
                        nigga = true;
                    }
                    if (nigga) {
                        lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
                        if (canboost && hypixelboost) {
                            timervalue += decreasing2 ? -0.01 : 0.05;
                            if (timervalue >= 1.4) {
                                decreasing2 = true;
                            }
                            if (timervalue <= 0.9) {
                                decreasing2 = false;
                            }
                            if (timer.reach(2000)) {
                                canboost = false;
                            }
                        }
                        if (mc.gameSettings.keyBindJump.isKeyDown()) {
                            mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
                            mc.thePlayer.motionY = 0.8;
                            mc.thePlayer.motionX *= 0.1;
                            mc.thePlayer.motionZ *= 0.1;
                        }
                        if ((mc.thePlayer.ticksExisted % 2) == 0) {
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtils.getRandomInRange(0.00000000000001235423532523523532521, 0.0000000000000123542353252352353252 * 10), mc.thePlayer.posZ);
                        }
                        mc.thePlayer.motionY = 0;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        float yaw = mc.thePlayer.rotationYaw;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        double forward = mc.thePlayer.movementInput.moveForward;
        double mx = -Math.sin(Math.toRadians(yaw)), mz = Math.cos(Math.toRadians(yaw));
        switch (mode.getValue()) {
            case HypixelFast:
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                if (hypixelboost) {
                    if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                        if (level == 2) {
                            level = 3;
                            moveSpeed *= 2.1499999D;
                        } else if (level == 3) {
                            level = 4;
                            double difference = 0.73D * (lastDist - getBaseMoveSpeed());
                            moveSpeed = lastDist - difference;
                        } else {
                            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                level = 1;
                            }
                            moveSpeed = lastDist - lastDist / 159.0D;
                        }
                    } else {
                        level = 2;
                        double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.706 : 2.034;
                        moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                    }
                    moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                    event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                    event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                    if (forward == 0.0F && strafe == 0.0F) {
                        event.setX(0.0);
                        event.setZ(0.0);
                    }
                    if (timer.reach(1700) && hypixelboost) {
                        hypixelboost = false;
                    }
                }
                break;
            case HypixelDamage:
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                double motionY = 0.40123128;
                if (level != 1) {
                    if (level == 2) {
                        if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround && !nigga) {
                            mc.thePlayer.damagePlayer();
                            level = 3;
                            if (mc.thePlayer.isPotionActive(Potion.jump)) motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(mc.thePlayer.motionY = motionY);
                            moveSpeed *= 2.149;
                            nigga = true;
                        }
                    } else if (level == 3) {
                        level = 4;
                        double difference =  0.23D * (lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - difference;
                    } else {
                        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                            level = 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                    }
                } else {
                    level = 2;
                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.526 : 2.034;
                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                break;
            case Vanilla:
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) {
                    setMoveSpeed(event, flyspeed.getValue());
                }
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0.085;
                mc.thePlayer.jumpMovementFactor = 2;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY += 1;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY -= 1;
                }
                break;
            default:
                break;
        }
    }

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    private void setSpeed(double speed) {
        mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    private float getDirection() {
        float var1 = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        } else {
            forward = 1.0f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }

    private void setMoveSpeed(MotionEvent event, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            event.setX(0);
            event.setZ(0);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += forward > 0.0D ? -45 : 45;
                } else if (strafe < 0.0D) {
                    yaw += forward > 0.0D ? 45 : -45;
                }

                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }


    private float getSpeed() {
        return (float) Math.sqrt((mc.thePlayer.motionX * mc.thePlayer.motionX) + (mc.thePlayer.motionZ * mc.thePlayer.motionZ));
    }

    public enum Modes {
        Hypixel, HypixelFast, HypixelDamage, Vanilla
    }
}
