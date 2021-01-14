package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.MotionEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.potion.Potion;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class LongJump extends Module {
    private double moveSpeed, lastDist;
    private int level;
    private NumberValue<Double> boostval = new NumberValue<>("Boost", 3.0D, 0.1D, 3.0D, 0.1D);
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.HYPIXEL);

    public LongJump() {
        super("LongJump", Category.MOVEMENT, new Color(163, 148, 255, 255).getRGB());
        setRenderLabel("Long Jump");
    }

    public enum Mode {
        HYPIXEL, MINEPLEX, CUBECRAFT, VANILLA
    }

    @Handler
    public void onMotion(MotionEvent event) {
        if (getMc().thePlayer == null) return;
        if (getMc().thePlayer.isOnLiquid() || getMc().thePlayer.isInLiquid()) return;
        switch (mode.getValue()) {
            case VANILLA:
                setMoveSpeed(event, boostval.getValue());
                if (getMc().thePlayer.isMoving()) {
                    if (getMc().thePlayer.onGround) {
                        event.setY(getMc().thePlayer.motionY = 0.41);
                    }
                } else {
                    getMc().thePlayer.motionX = 0.0;
                    getMc().thePlayer.motionZ = 0.0;
                }
                break;
            case HYPIXEL:
                double forward = getMc().thePlayer.movementInput.moveForward;
                double strafe = getMc().thePlayer.movementInput.moveStrafe;
                float yaw = getMc().thePlayer.rotationYaw;
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0);
                    event.setZ(0);
                }
                if (forward != 0 && strafe != 0) {
                    forward = forward * Math.sin(Math.PI / 4);
                    strafe = strafe * Math.cos(Math.PI / 4);
                }
                if (level != 1 || getMc().thePlayer.moveForward == 0.0F && getMc().thePlayer.moveStrafing == 0.0F) {
                    if (level == 2) {
                        ++level;
                        float motionY = 0.4025f;
                        if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F) && getMc().thePlayer.onGround) {
                            if (getMc().thePlayer.isPotionActive(Potion.jump))
                                motionY += ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(getMc().thePlayer.motionY = motionY);
                            moveSpeed *= 2.149;
                        }
                    } else if (level == 3) {
                        ++level;
                        double difference = 0.6963D * (lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - difference;
                    } else {
                        if (getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) {
                            if (level == 4) {
                                toggle();
                            }
                            level = 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                    }
                } else {
                    level = 2;
                    double boost = getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? boostval.getValue() : boostval.getValue() + 1.1;
                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                final double mx = -Math.sin(Math.toRadians(yaw));
                final double mz = Math.cos(Math.toRadians(yaw));
                event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                break;
            case MINEPLEX:
                if (getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f) {
                    double speed;
                    level++;
                    if (getMc().thePlayer.onGround) {
                        if (level > 7) {
                            toggle();
                            return;
                        }
                        event.setY(getMc().thePlayer.motionY = 0.42);
                        getMc().thePlayer.motionY += 0.098;
                        speed = 0;
                    } else {
                        if (getMc().thePlayer.motionY >= 0) {
                            getMc().thePlayer.motionY += 0.04;
                        }

                        if (getMc().thePlayer.motionY < 0) {
                            if (getMc().thePlayer.motionY > -0.3) {
                                getMc().thePlayer.motionY += 0.0331;
                            } else {
                                if (getMc().thePlayer.motionY > -0.38) {
                                    getMc().thePlayer.motionY += 0.031;
                                } else {
                                    getMc().thePlayer.motionY += 0.01;
                                }
                            }
                        }
                        double slowdown = 0.007;
                        speed = (0.815 - (level * slowdown));
                        if (speed < 0) speed = 0;
                    }
                    setMoveSpeed(event, speed);
                }
                break;
            case CUBECRAFT:
                if (getMc().thePlayer.onGround) {
                    getMc().timer.timerSpeed = 0.5f;
                    if (level <= 100) {
                        if (getMc().thePlayer.hurtResistantTime == 19) {
                            event.setY(getMc().thePlayer.motionY = 0.8F);
                            level = 1000;
                            getMc().timer.timerSpeed = 1f;
                        }
                    }
                    else {
                        toggle();
                    }
                    // event.setY(getMc().thePlayer.motionY = 0.11F);
                   // event.setY(getMc().thePlayer.motionY = -0.01);
                  //  getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 2, getMc().thePlayer.posZ);
                }
                if (!getMc().thePlayer.onGround) {
                    if (getMc().thePlayer.ticksExisted % 3 == 0) {
                        // event.setY(0);
                        setMoveSpeed(event, 2.4);
                        //getMc().timer.timerSpeed = 0.4f;
                    } else {
                        setMoveSpeed(event, 0.1576);
                    }
                }
                break;

        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (getMc().thePlayer.isOnLiquid() || getMc().thePlayer.isInLiquid()) return;
        if (!event.isPre()) {
            double xDist = getMc().thePlayer.posX - getMc().thePlayer.prevPosX;
            double zDist = getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }
        switch (mode.getValue()) {
            case CUBECRAFT:
                if (event.isPre()) {
                    level++;
                    if (getMc().thePlayer.onGround) {
                        if (level == 1) {
                            getMc().thePlayer.damagePlayer();
                        }
                    }
                    Printer.print(""+level);
                }
                break;
            default:
                break;
        }
    }

    private void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = getMc().thePlayer.movementInput.moveForward;
        double strafe = getMc().thePlayer.movementInput.moveStrafe;
        float yaw = getMc().thePlayer.rotationYaw;
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
        double n = 0.2873;
        if (getMc().thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (getMc().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public void onDisable() {
        getMc().timer.timerSpeed = 1F;
        if (getMc().thePlayer != null) {
            moveSpeed = getBaseMoveSpeed();
            getMc().thePlayer.motionX = 0;
            getMc().thePlayer.motionZ = 0;
        }
        lastDist = 0.0D;
    }

    @Override
    public void onEnable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        level = 0;
        lastDist = 0.0D;
    }
}
