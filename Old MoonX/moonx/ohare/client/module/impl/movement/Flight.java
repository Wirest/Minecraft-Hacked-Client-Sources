

package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.EventCollideUnderPlayer;
import moonx.ohare.client.event.impl.player.MotionEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.MoveUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class Flight extends Module {
    private TimerUtil timer = new TimerUtil();
    private EnumValue<Modes> mode = new EnumValue<>("Mode", Modes.HYPIXEL);
    private EnumValue<BoostModes> boostModes = new EnumValue<>("Boost Mode", BoostModes.NORMAL);
    private BooleanValue viewbob = new BooleanValue("ViewBob", true);
    private BooleanValue boost = new BooleanValue("Boost", true);
    private BooleanValue extra = new BooleanValue("Extra Boost", true, boost, "true");
    private NumberValue<Float> flyspeed = new NumberValue<>("Fly Speed", 2.0f, 0.1f, 3.0f, 0.1f);
    private double moveSpeed, lastDist, ascension;
    private int level, wait, xd, xdnewtest;

    public Flight() {
        super("Flight", Category.MOVEMENT, new Color(33, 120, 255, 255).getRGB());
        setDescription("Zoom around like an epic gamer.");
    }

    public enum Modes {
        HYPIXEL, VANILLA, CUBECRAFT, ANTIVIRUS, EXPERIMENTAL
    }

    public enum BoostModes {
        NORMAL, DAMAGE, WOWOMG
    }

    @Override
    public void onDisable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        getMc().timer.timerSpeed = 1f;
        getMc().thePlayer.motionX = getMc().thePlayer.motionZ = 0;
        moveSpeed = getBaseMoveSpeed();
        lastDist = 0.0D;
        xdnewtest = 0;
        ascension = 0;
        xd = 0;
    }

    @Handler
    public void onCollidegay(EventCollideUnderPlayer event) {
     //   event.getList().add(new AxisAlignedBB(getMc().thePlayer.posX, (int) getMc().thePlayer.posY, getMc().thePlayer.posZ));
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (!event.isPre()) {
            double xDist = getMc().thePlayer.posX - getMc().thePlayer.prevPosX;
            double zDist = getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }
        if (event.isPre()) {
            if (viewbob.getValue() && getMc().thePlayer.isMoving())
                getMc().thePlayer.cameraYaw = 0.089f;
            switch (mode.getValue()) {
                case CUBECRAFT:
                    if (event.isPre()) {
                        getMc().timer.timerSpeed = 0.8f;
                        wait++;
                        float y = (float) Math.floor(getMc().thePlayer.posY);
                        if (wait == 1 && !getMc().thePlayer.onGround) {
                            toggle();
                        }
                        if (event.isPre()) {
                            getMc().thePlayer.motionY = 0;
                            if (wait < 10) {
                                event.setY(y - 1);
                                getMc().thePlayer.motionY = -0.1;
                            } else if (wait > 12) {
                                level++;
                                if (level == 1) {
                                    event.setY(y + 0.72 + MathUtils.getRandomInRange(0.03, 0.05));
                                }
                                if (level == 2) {
                                    event.setY(y + 0.48 + MathUtils.getRandomInRange(0.03, 0.05));
                                }
                                if (level == 3) {
                                    event.setY(y + 0.24 + MathUtils.getRandomInRange(0.03, 0.05));
                                }
                                if (level == 4) {
                                    event.setY(y + MathUtils.getRandomInRange(0.03, 0.05));
                                }
                            }
                        }
                    }
                    break;
                case HYPIXEL:
                    if (event.isPre()) {
                        if (getMc().gameSettings.keyBindJump.isKeyDown()) {
                            getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 0.15, getMc().thePlayer.posZ);
                            getMc().thePlayer.motionY = 0.15;
                        } else if (getMc().gameSettings.keyBindSneak.isKeyDown()) {
                            getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY - 0.15, getMc().thePlayer.posZ);
                            getMc().thePlayer.motionY = -0.15;
                        } else getMc().thePlayer.motionY = 0;
                        if (getMc().getCurrentServerData() != null && getMc().getCurrentServerData().serverIP != null && getMc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                            event.setOnGround(true);
                        }
                        double result = 0.00000000334947 + MathUtils.getRandomInRange(0.00000000014947, 0.00000000064947);
                        if (getMc().thePlayer.ticksExisted % 3 == 0) {
                            event.setY(getMc().thePlayer.posY + result);
                            event.setOnGround(false);
                        }
                        /*if (getMc().thePlayer.ticksExisted % 3 == 0) {
                            ascension += value;
                        }
                        if (ascension > value * 7) {
                            event.setOnGround(false);
                            event.setY(getMc().thePlayer.posY + ascension);
                            ascension /= 1.125F;
                        }*/
                       // Printer.print(""+event.getY());
                        if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F) && getMc().thePlayer.onGround) {
                            if (!boost.isEnabled()) {
                                final float motionY = 0.42f + (getMc().thePlayer.isPotionActive(Potion.jump) ? ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
                                //getMc().thePlayer.motionY = motionY;
                             //   getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + motionY, getMc().thePlayer.posZ);
                            }
                            if ((boostModes.getValue() == BoostModes.DAMAGE || boostModes.getValue() == BoostModes.WOWOMG) && boost.isEnabled())
                                if (xd == 0) {
                                    getMc().thePlayer.damagePlayer();
                                    xd = 1;
                                }
                        }
                    }
                    break;
                case VANILLA:
                    if (getMc().gameSettings.keyBindJump.isKeyDown())
                        getMc().thePlayer.motionY = flyspeed.getValue() / 2;
                    else if (getMc().gameSettings.keyBindSneak.isKeyDown())
                        getMc().thePlayer.motionY = -flyspeed.getValue() / 2;
                    else getMc().thePlayer.motionY = 0;
                    break;
                case ANTIVIRUS:
                    if (getMc().thePlayer.fallDistance > 0.25 && !getMc().gameSettings.keyBindSneak.isKeyDown()) {
                        getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + getMc().thePlayer.fallDistance, getMc().thePlayer.posZ);
                        getMc().thePlayer.fallDistance = 0;
                    }
                    event.setOnGround(true);
                    getMc().thePlayer.motionY = -0.05;

                    if (getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.ticksExisted % 2 == 0) {
                        getMc().thePlayer.fallDistance = 2;
                    } else if (getMc().gameSettings.keyBindSneak.isKeyDown() && getMc().thePlayer.fallDistance > 0) {
                        getMc().thePlayer.motionY = -1;
                        getMc().thePlayer.fallDistance = 0;
                        //getMc().thePlayer.fallDistance = 1;
                        // getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY - 2, getMc().thePlayer.posZ);
                    }
                    break;
                case EXPERIMENTAL:
                    if (event.isPre()) {
                       // event.setOnGround(false);
                        //getMc().thePlayer.motionY = 0;
                        getMc().timer.timerSpeed = 1f;
                        if (getMc().thePlayer.onGround) {
                            if (getMc().thePlayer.ticksExisted % 11 == 0) {
                                double[] jumpValue = new double[]{0.0, 0.42f, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.12160004615784, 0.96636804164123, 0.73584067272827, 0.43152384527073, 0.05489334703208, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                                for (int i = 0; i < 3; ++i) {
                                    for (int length = jumpValue.length, j = 0; j < length; ++j) {
                                        getMc().getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + jumpValue[j], getMc().thePlayer.posZ, false));
                                    }
                                }
                            }
                            if (getMc().thePlayer.ticksExisted % 10 == 0) {
                                getMc().timer.timerSpeed = 0.35f;
                            }
                        }
                        if (getMc().thePlayer.hurtResistantTime == 19 && getMc().thePlayer.onGround) {
                            MoveUtil.setSpeed(5);
                            getMc().thePlayer.motionY = 3;
                            //moveSpeed = 8;
                        }
                       /* getMc().thePlayer.motionY = 0;
                        event.setOnGround(true);
                        getMc().thePlayer.setPosition(getMc().thePlayer.posX, (Math.round(getMc().thePlayer.posY / 0.015625) * 0.015625), getMc().thePlayer.posZ);
                        if (getMc().gameSettings.keyBindJump.isKeyDown()) {
                            getMc().thePlayer.motionY = getMc().gameSettings.keyBindJump.isKeyDown() ? 0.5f : 0f;
                        }*/
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Handler
    public void onMotion(MotionEvent event) {
        switch (mode.getValue()) {
            case EXPERIMENTAL:
                //setMoveSpeed(event, 0.24);
                //setMoveSpeed(event, 2);
                break;
            case ANTIVIRUS:
                if (!getMc().thePlayer.onGround) {
                    setMoveSpeed(event, 0.75);
                }
                break;
            case CUBECRAFT:
                getMc().timer.timerSpeed = 1f;
                setMoveSpeed(event, 0);
                if (wait > 12) {
                    if (level == 4) {
                        setMoveSpeed(event, 0.953532);
                        level = 0;
                    }
                    else {
                        setMoveSpeed(event, 0.121984218421847);
                    }
                }
                else {
                    setMoveSpeed(event, 0);
                }
                break;
            case VANILLA:
                setMoveSpeed(event, flyspeed.getValue());
                break;
            case HYPIXEL:
                if (extra.isEnabled() && boost.isEnabled()) {
                    if (!timer.reach(135) && timer.reach(20)) {
                        getMc().timer.timerSpeed = 3.5f;
                    } else {
                        getMc().timer.timerSpeed = 1f;
                    }
                    if (getMc().thePlayer.hurtResistantTime == 19 && level < 20) {
                        //getMc().timer.timerSpeed = 3.6f;
                        timer.reset();
                    }
                }
                if (boost.isEnabled()) {
                    final float motionY = 0.42f + (getMc().thePlayer.isPotionActive(Potion.jump) ? ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
                    switch (boostModes.getValue()) {
                        case NORMAL:
                        case DAMAGE:
                            if (getMc().thePlayer.isMoving()) {
                                if (level != 1) {
                                    if (level == 2) {
                                        //getMc().timer.timerSpeed = Math.max(1, 3.5F);
                                        ++level;
                                        moveSpeed *= getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? flyspeed.getValue() - 0.3 : flyspeed.getValue();
                                        //Printer.print("2: "+moveSpeed);
                                    } else if (level == 3) {
                                        ++level;
                                        double difference = (boostModes.getValue() == BoostModes.DAMAGE ? 0.01 : 0.1D) * (lastDist - getBaseMoveSpeed());
                                        moveSpeed = lastDist - difference;
                                    } else {
                                        level++;
                                        if (getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) {
                                            level = 1;
                                        }
                                        moveSpeed = lastDist - lastDist / 159D;
                                    }
                                } else if (getMc().thePlayer.hurtResistantTime == 19 || boostModes.getValue() == BoostModes.NORMAL) {
                                    event.setY(getMc().thePlayer.motionY = motionY);
                                    level = 2;
                                    double boost = getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.5 : 1.62;
                                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                                }
                            } else {
                                moveSpeed = 0;
                            }
                            //Printer.print("f: "+moveSpeed + " " + level);
                            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                            if (level == 1 && getMc().thePlayer.hurtResistantTime != 19 && boostModes.getValue() != BoostModes.NORMAL) moveSpeed = 0.011;
                            MoveUtil.setMoveSpeed(event, moveSpeed);
                            break;
                        case WOWOMG:
                            if (getMc().thePlayer.isMoving()) {
                                if (level != 1) {
                                    if (level == 2) {
                                        //getMc().timer.timerSpeed = Math.max(1, 3.5F);
                                        ++level;
                                        moveSpeed *= getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? flyspeed.getValue() - 0.3 : flyspeed.getValue();
                                        //Printer.print("2: "+moveSpeed);
                                    } else if (level == 3) {
                                        ++level;
                                        double difference = 0.01 * (lastDist - getBaseMoveSpeed());
                                        moveSpeed = lastDist - difference;
                                    } else {
                                        level++;
                                        if (getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) {
                                            level = 1;
                                        }
                                        moveSpeed = moveSpeed - moveSpeed / 159.9D;
                                    }
                                } else {
                                    event.setY(getMc().thePlayer.motionY = motionY);
                                    level = 2;
                                    double boost = getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.55 : 1.62;
                                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                                }
                            } else {
                                moveSpeed = 0;
                            }
                          //  Printer.print("f: "+moveSpeed + " " + level);
                            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                            MoveUtil.setMoveSpeed(event, moveSpeed);
                            break;
                    }
                } else {
                    MoveUtil.setMoveSpeed(event, getBaseMoveSpeed());
                    //  getMc().timer.timerSpeed = 0.5f;
                    //MoveUtil.TP(event, 0.28, 0);
                }
                break;
            default:
                break;
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
    public void onEnable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        wait = 0;
        level = 0;
        lastDist = 0.0D;
    }

    private void setMoveSpeed(MotionEvent event, double speed) {
        double forward = getMc().thePlayer.movementInput.moveForward;
        double strafe = getMc().thePlayer.movementInput.moveStrafe;
        float yaw = getMc().thePlayer.rotationYaw;
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
}

