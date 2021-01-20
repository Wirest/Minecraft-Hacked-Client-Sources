package cedo.modules.movement;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.BypassUtil;
import cedo.util.movement.MovementUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("rawtypes")
public class Speed extends Module {
    ModeSetting mode = new ModeSetting("Type", "Strafe3", "Strafe3", "Strafe4", "Strafe5", "Strafe6");
    NumberSetting devSpeed = new NumberSetting("Speed", 0.56, 0.10, 1, 0.01);
    BooleanSetting devBoost = new BooleanSetting("Boost", true),
            devGround = new BooleanSetting("FastGround", true);
    int jumps;
    int change;
    double deceleration;
    long startTime;

    boolean wasGrounded, reset, lastDistanceReset;
    boolean gameType;
    double speed;
    double lastDist;
    double speedFactor;
    int idk;
    int stage;
    private double moveSpeed, moveSpeed2;
    private double G8Gay;


    public Speed() {
        super("Speed", Keyboard.KEY_V, Category.MOVEMENT);
        addSettings(mode, devSpeed, devBoost, devGround);
        disableOnLagback = true;
    }

    public void onEnable() {
        stage = 0;
        reset = true;
        speed = 0;
        moveSpeed2 = devSpeed.getValue() + 0.0000000023841858D;
        deceleration = devBoost.isEnabled() ? devSpeed.getValue() : 0;
        super.onEnable();
        startTime = System.currentTimeMillis();
    }

    public void onDisable() {
        mc.thePlayer.stepHeight = 0.625F;
        mc.timer.timerSpeed = 1f;
        super.onDisable();
        Fan.statistics.add("Speed Duration", (int) (System.currentTimeMillis() - startTime));
    }

    public void onEvent(Event e) {
        if(Fan.targetHud.size.is("Script")){
            this.setSuffix("\247cMichealXF");
        }else
        this.setSuffix(mode.getSelected());
        if(mode.is("Strafe6")){
            if(e instanceof EventMotion){
                if (mc.thePlayer.onGround)
                    ((EventMotion) e).setY(((EventMotion) e).getY() + RandomUtils.nextFloat(0.00001F, 0.00099F));
            }
            if(e instanceof EventMove){
                if (MovementUtil.isMoving() && !mc.thePlayer.isInWater()) {
                    double baseSpeed = MovementUtil.getBaseMoveSpeed();
                    if (mc.thePlayer.onGround) {
                        ((EventMove) e).setY(mc.thePlayer.motionY = MovementUtil.getJumpBoostModifier(0.42F));
                        speed = baseSpeed * 2.15F;
                        lastDistanceReset = true;
                    } else if (lastDistanceReset || mc.thePlayer.isCollidedHorizontally) {
                        speed -= 0.66 * (speed - baseSpeed);
                        lastDistanceReset = false;
                    } else {
                        speed -= speed / 159;
                    }
                    MovementUtil.setSpeed((EventMove) e, Math.max(speed, baseSpeed));
                    if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null) {
                        Fan.targetStrafe.strafe((EventMove) e, Math.max(speed, speedFactor));
                    } else {
                        ((EventMove) e).setSpeed(Math.max(speed, speedFactor));
                    }
                }
            }
        }
        if(mode.is("YPort")){
            if(e instanceof EventMotion){
                ((EventMotion) e).setY(((EventMotion) e).getY() + (lastDistanceReset ? 0.42F : 0.001));
            }
            if(e instanceof EventMove){
                if(!mc.thePlayer.onGround || mc.thePlayer.isCollidedHorizontally)
                    reset = true;

                if(mc.thePlayer.onGround && !mc.thePlayer.isCollidedHorizontally) {
                    lastDistanceReset = mc.thePlayer.ticksExisted % 2 == 0;
                    if (lastDistanceReset) {
                        speed *= 2.149;
                        mc.timer.timerSpeed = 1.075F;
                        reset = true;
                    } else if (reset) {
                        mc.timer.timerSpeed = 1F;
                        speed -= 0.66 * (speed - MovementUtil.getBaseMoveSpeed());
                        reset = false;
                    }
                    speed = Math.max(speed, MovementUtil.getBaseMoveSpeed());
                    MovementUtil.setSpeed((EventMove) e, speed);
                }
            }
        }
        if (mode.is("Strafe2")) {
            if (e instanceof EventMove) {
                EventMove event = (EventMove) e;

                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        event.setY(mc.thePlayer.motionY = getJumpHeight(Fan.getModule(Scaffold.class).isEnabled() ? 0.419999986886978D : 0.3999999873340128D));
                        speed = speedFactor * 2.1449999809265137D;
                    }

                    if (wasGrounded) {
                        speed = lastDist - 0.66D * (lastDist - speedFactor);
                    }

                    List<Double> speeds = new ArrayList<Double>();
                    speeds.add(lastDist - lastDist / 160.0D);
                    speeds.add(lastDist - (lastDist - speedFactor) / 33.3D);
                    speeds.add(lastDist - (lastDist - speedFactor) * 0.020000000000000018D);
                    speeds.sort(Double::compare);
                    speed = speeds.get(2) - 1.0E-5D;
                }

                //Logger.ingameInfo(speedFactor + "");


                if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null) {
                    Fan.targetStrafe.strafe(event, Math.max(speed, speedFactor));
                } else {
                    event.setSpeed(Math.max(speed, speedFactor));
                }

                wasGrounded = mc.thePlayer.onGround;

            }
            if (e instanceof EventUpdate) {
                lastDist = MovementUtil.getLastDistance();
                double speed = gameType ? getSpeedPlusPotions(0.2D, 1) : getSpeedNetPotions(isInLiquid() ? 0.1D : 0.2D);
                speedFactor = speed * (!isOnLiquid() && !mc.thePlayer.isInsideOfMaterial(Material.vine) ? (mc.thePlayer.isSneaking() ? 0.8D : (isInLiquid() ? 0.54D : (BypassUtil.isBlockBelowSlippery() ? 2.4D : 1.0D))) : 0.5D);
            }
            if (e instanceof EventMotion && e.isPre()) {
                EventMotion event = (EventMotion) e;

                if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    event.setY(event.getY() + ThreadLocalRandom.current().nextDouble(1E-6));
                    idk = 0;
                }
            }
        }


        if (mode.is("Strafe1")) {
            if (e instanceof EventMove) {
                float groundSpeed1 = 0;
                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        deceleration *= 0.5;
                        if (devGround.isEnabled())
                            groundSpeed1 = 0.2f;
                    }
                }
                EventMove event = (EventMove) e;
                double speed = Math.max(getBaseMoveSpeed() + deceleration + groundSpeed1, getBaseMoveSpeed());

                if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null) {
                    Fan.targetStrafe.strafe(event, speed);
                } else {
                    event.setSpeed(speed);
                }
                //Logger.ingameInfo(Double.toString(speed));
                //Logger.ingameInfo(Double.toString(getBaseMoveSpeed()));
            }
            if (e instanceof EventMotion && e.isPre()) {
                ((EventMotion) e).setY(((EventMotion) e).getY() + 1.67346739E-7);
            }
            if (e instanceof EventMotion) {
                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.4099919;
                    }
                }
            }
        }


        if (mode.is("Strafe3")) {
            if (e instanceof EventMotion && e.isPre()) {
                ((EventMotion) e).setY(((EventMotion) e).getY() + 1.67346739E-7);

                if (isMoving() && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
            }
            if (MovementUtil.isMoving()) {
                // MoveUtils.strafe();
                float f1 = (float) (MovementUtil.getSpeed() < 0.5600000023841858D ? MovementUtil.getSpeed() / 1.0449999570846558D : 0.5600000023841858D);
                if ((mc.thePlayer.onGround) && (mc.thePlayer.isPotionActive(Potion.moveSpeed))) {
                    f1 *= (1.0F + 0.13F * (0x1 | mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()));
                }
                moveSpeed2 = lastDist - lastDist / 159D;
                G8Gay = lastDist - lastDist + f1;

                if (e instanceof EventMove) {
                    EventMove event = (EventMove) e;

                    if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null) {
                        Fan.targetStrafe.strafe(event, lastDist + lastDist + 0.135D + lastDist - moveSpeed + moveSpeed + G8Gay);
                        Fan.targetStrafe.strafe(event, f1);
                        Fan.targetStrafe.strafe(event, MovementUtil.getSpeed() + 0.0065D);
                    } else {
                        MovementUtil.setSpeed(event, lastDist + lastDist + 0.135D + lastDist - moveSpeed + moveSpeed + G8Gay);
                        MovementUtil.setSpeed(f1);
                        MovementUtil.setSpeed(MovementUtil.getSpeed() + 0.0065D);
                    }
                }
            } else {
                MovementUtil.setSpeed(MovementUtil.getSpeed());
            }
        }


        if (mode.is("StrafeUNUSED")) {
            if (e instanceof EventUpdate) {
                if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    mc.thePlayer.jump();
                }
            } else if (e instanceof EventMotion && e.isPre()) {
                if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    ((EventMotion) e).setY(((EventMotion) e).getY() + 1.67346739E-7);
                }
            } else if (e instanceof EventMove) {
                if (MovementUtil.isMoving()) {
                    EventMove event = (EventMove) e;
                    float f1 = (float) (MovementUtil.getSpeed() < 0.5600000023841858D ? MovementUtil.getSpeed() / 1.0449999570846558D : 0.5600000023841858D);
                    if ((mc.thePlayer.onGround) && (mc.thePlayer.isPotionActive(Potion.moveSpeed))) {
                        f1 *= (1.0F + 0.13F * (0x1 | mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()));
                    }
                    double moveSpeed3 = lastDist - lastDist / 159.0D;
                    G8Gay = lastDist - lastDist + f1;
                    float doggiegobyebrrrrrrrr = (float) (0.1 + 0.003F - 0.002 + 0.4);
                    if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null) {
                        Fan.targetStrafe.strafe(event, lastDist + lastDist + doggiegobyebrrrrrrrr + lastDist - moveSpeed3 + moveSpeed3 + G8Gay);
                        Fan.targetStrafe.strafe(event, f1);
                        Fan.targetStrafe.strafe(event, MovementUtil.getSpeed() + 0.0065D);
                    } else {
                        MovementUtil.setSpeed(event, lastDist + lastDist + doggiegobyebrrrrrrrr + lastDist - moveSpeed3 + moveSpeed3 + G8Gay);
                        MovementUtil.setSpeed(f1);
                        MovementUtil.setSpeed(MovementUtil.getSpeed() + 0.0065D);
                    }
                } else {
                    MovementUtil.setSpeed(MovementUtil.getSpeed());
                }
            }
        }


        if (mode.is("Strafe4")) {
            if (e instanceof EventUpdate) {
                if (MovementUtil.isMoving()) {
                    moveSpeed2 -= moveSpeed2 * 0.0901264592723945D;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        moveSpeed2 = devSpeed.getValue();
                    }
                }
            } else if (e instanceof EventMotion && e.isPre()) {
                if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    ((EventMotion) e).setY(((EventMotion) e).getY() + 1.67316789E-7);
                }
            } else if (e instanceof EventMove) {
                if (MovementUtil.isMoving()) {
                    if (Fan.targetStrafe.canStrafe() && Fan.targetStrafe.isEnabled() && Fan.targetStrafe != null)
                        Fan.targetStrafe.strafe((EventMove) e, Math.max(moveSpeed2, MovementUtil.getBaseMoveSpeed() - 0.05));
                    else MovementUtil.setSpeed(Math.max(moveSpeed2, MovementUtil.getBaseMoveSpeed() - 0.05));
                } else {
                    MovementUtil.setSpeed(MovementUtil.getSpeed());
                }
            }
        }

        if (mode.is("Strafe5")) {
            if (e instanceof EventMotion && e.isPre()) {
                if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    ((EventMotion) e).setY(mc.thePlayer.posY + 0.0007435D);
                }
            }


            if (e instanceof EventMove) {
                if (MovementUtil.isMoving()) {
                    switch (stage) {
                        case 2:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                mc.timer.timerSpeed = 1F;
                                ((EventMove) e).y = mc.thePlayer.motionY = MovementUtil.getJumpBoostModifier(0.39999994F);
                                moveSpeed *= 2F;
                            }
                            break;
                        case 3:
                            final double difference = 0.75F * (lastDist - MovementUtil.getBaseMoveSpeed());
                            moveSpeed = (lastDist - difference);
                            break;
                        default:
                            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0
                                    || (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround)) {
                                stage = 1;
                                mc.timer.timerSpeed = 1.085F;
                            }
                            moveSpeed = lastDist - lastDist / 159.0D;
                            break;
                    }

                    moveSpeed = Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed());

                    if (Fan.targetStrafe.canStrafe()) {
                        Fan.targetStrafe.strafe((EventMove) e, moveSpeed);
                    } else {
                        MovementUtil.setSpeed((EventMove) e, moveSpeed);
                    }
                    stage++;

                }
            }

            if (e instanceof EventMotion && e.isPre()) {
                double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            }


        }


    }


    public boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double) (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());
        }
        return baseSpeed;
    }

    public void setSpeed(float speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * (double) speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * (double) speed;
    }

    public double getJumpHeight(double speed) {
        return mc.thePlayer.isPotionActive(Potion.jump) ? speed + 0.1D * (double) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) : speed;
    }

    public double getSpeedNetPotions(double speed) {
        double moveSpeed = MovementUtil.defaultMoveSpeed();
        moveSpeed *= 1.0 + speed * (
                (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0) -
                        (mc.thePlayer.isPotionActive(Potion.moveSlowdown) ? mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1 : 0)
        );
        return moveSpeed;
    }

    public double getSpeedPlusPotions(double speed, int multiplier) {
        double moveSpeed = MovementUtil.defaultMoveSpeed();
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            moveSpeed *= 1.0 + speed * multiplier;
        }
        return moveSpeed;
    }


    public float getDirection() {
        float var1 = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        forward = mc.thePlayer.moveForward < 0.0f ? -0.5f : (mc.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f);
        if (mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= Math.PI / 180.0F;
    }

    private boolean canSprint() {
        return !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava();
    }

    public boolean isOnLiquid() {
        double y = mc.thePlayer.posY + 0.01;
        int x = MathHelper.floor_double(mc.thePlayer.posX);
        if (x < MathHelper.ceiling_double_int(mc.thePlayer.posX)) {
            int z = MathHelper.floor_double(mc.thePlayer.posZ);
            if (z < MathHelper.ceiling_double_int(mc.thePlayer.posZ)) {
                if (mc.theWorld.getBlockState(new BlockPos(x, (int) y, z)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
                ++z;
            }
            ++x;
        }
        return false;
    }

    public boolean isInLiquid() {
        double y = mc.thePlayer.posY - 0.1;
        int x = MathHelper.floor_double(mc.thePlayer.posX);
        if (x < MathHelper.ceiling_double_int(mc.thePlayer.posX)) {
            int z = MathHelper.floor_double(mc.thePlayer.posZ);
            if (z < MathHelper.ceiling_double_int(mc.thePlayer.posZ)) {
                if (mc.theWorld.getBlockState(new BlockPos(x, MathHelper.floor_double(y), z)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
                ++z;
            }
            ++x;
        }
        return false;
    }
}