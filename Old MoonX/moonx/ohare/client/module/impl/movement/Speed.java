

package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.MotionEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.KillAura;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.MoveUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;


/**
 * made by oHare for Moon X
 *
 * @since 9/2/2019
 **/
public class Speed extends Module {
    private int stage = 1, stageOG = 1;
    private double moveSpeed, lastDist, moveSpeedOG, lastDistOG;
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.HYPIXEL);
    private NumberValue<Double> boost = new NumberValue<>("Boost", 1.2, 0.1, 5.0, 0.1);
    public static boolean strafeDirection;
    private int voidTicks;

    public Speed() {
        super("Speed", Category.MOVEMENT, new Color(0, 255, 0, 255).getRGB());
        setDescription("Zoomie zoom");
    }

    public enum Mode {
        HYPIXEL,HYPIXELYPORT, MINEPLEX, CUBECRAFT, CUBECRAFT_TP,CUBECRAFT_TEST, NCP, ONGROUND, VANILLA, BHOP, AGC, AAC
    }

    @Override
    public void onEnable() {
        if (getMc().thePlayer == null) return;
        lastDist = 0;
        moveSpeed = 0;
    }

    @Override
    public void onDisable() {
        if (getMc().thePlayer == null) return;
        getMc().timer.timerSpeed = 1f;
    }


    @Handler
    public void onPacket(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            lastDist = 0;
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        final boolean tick = getMc().thePlayer.ticksExisted % 2 == 0;
        lastDist = Math.sqrt(((getMc().thePlayer.posX - getMc().thePlayer.prevPosX) * (getMc().thePlayer.posX - getMc().thePlayer.prevPosX)) + ((getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ) * (getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ)));
        if (lastDist > 5) lastDist = 0.0D;
        switch (mode.getValue()) {
            case HYPIXEL:
                if (event.isPre()) {
                    if (event.getY() % 0.015625 == 0.0) {
                        event.setY(event.getY() + 5.3424E-4);
                        //event.setY(event.getY() + 0.00611131);
                        event.setOnGround(false);
                    }
                    if (getMc().thePlayer.motionY > 0.3) {
                        event.setOnGround(true);
                    }
                }
                break;
            case ONGROUND:
                getMc().thePlayer.cameraYaw = 0f;
                if (event.isPre()) {
                    if (getMc().thePlayer.isMoving()) {
                        if (getMc().thePlayer.onGround && !tick) {
                            event.setY(event.getY() + 0.41248);
                        }
                        if (tick) {
                            if (!getMc().thePlayer.onGround) getMc().thePlayer.motionY = -1.02345234623;
                        }
                        moveSpeedOG *= tick ? 2.12542 : 0.905;
                    }
                }
                break;
            case AAC:
                if (event.isPre()) {
                    if ((getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f)) {
                        for (double x = 0.0625; x < 2.5; x += 0.262) {
                            final double[] dir = getDirectionalSpeed(x);
                            getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX + dir[0], getMc().thePlayer.posY + 1.29, getMc().thePlayer.posZ + dir[1], getMc().thePlayer.onGround));
                        }
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX + getMc().thePlayer.motionX, -999, getMc().thePlayer.posZ + getMc().thePlayer.motionZ, getMc().thePlayer.onGround));
                    }
                }
                break;
            case AGC:
                if (event.isPre()) {
                    if (getMc().thePlayer.isMoving()) {

                    }
                }
                break;
            default:break;
        }
    }

    @Handler
    public void onMotion(MotionEvent event) {
        double forward = getMc().thePlayer.movementInput.moveForward, strafe = getMc().thePlayer.movementInput.moveStrafe, yaw = getMc().thePlayer.rotationYaw;
        if (getMc().thePlayer.isOnLiquid()) return;
        switch (mode.getValue()) {
            case AGC:
                getMc().timer.timerSpeed = 0.3f;
                if (getMc().thePlayer.isMoving()) {
                    for (int i = 0; i < 17; ++i) {
                        MoveUtil.TP(event, 0.27, 0);
                    }
                }
                /*if (getMc().thePlayer.isMoving()) {
                    if (getMc().thePlayer.onGround) {
                        MoveUtil.setJumpSpeed(1.02F);
                        event.setY(getMc().thePlayer.motionY = 0.405f);
                    }
                    else {
                        if (getMc().thePlayer.motionY > 0.3) {
                            //Printer.print(MoveUtil.getSpeed() + " transformed: " + MoveUtil.getSpeed() * 1.033);
                            MoveUtil.setSpeed(MoveUtil.getSpeed() * 1.03);
                        }
                        getMc().thePlayer.motionY -= 0.01499;
                        if (getMc().thePlayer.hurtResistantTime < 9) {
                            MoveUtil.setSpeed(MoveUtil.getSpeed());
                        }
                    }
                }*/
                break;
            case BHOP:
                setMoveSpeed(event, boost.getValue());
                if (getMc().thePlayer.isMoving()) {
                    if (getMc().thePlayer.onGround) {
                        event.setY(getMc().thePlayer.motionY = 0.42f);
                    }
                } else {
                    getMc().thePlayer.motionX = 0.0;
                    getMc().thePlayer.motionZ = 0.0;
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
                        if ((getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) && stage > 0) {
                            stageOG = getMc().thePlayer.moveForward == 0.0D && getMc().thePlayer.moveStrafing == 0.0D ? 0 : 1;
                        }
                        moveSpeedOG = lastDistOG - lastDistOG / 159.0213245D;
                        break;
                }
                moveSpeedOG = getMc().thePlayer.isInWater() ? 0 : Math.max(moveSpeedOG, getBaseMoveSpeed());
                setMoveSpeed(event, moveSpeed);
                ++stageOG;
                break;
            case HYPIXEL:
            case HYPIXELYPORT:
                if (mode.getValue() == Mode.HYPIXELYPORT && !getMc().thePlayer.isCollidedHorizontally) {
                    if (MathUtils.roundToPlace(getMc().thePlayer.posY - (int) getMc().thePlayer.posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                        event.setY((getMc().thePlayer.motionY = 0.2));
                    }
                    if (MathUtils.roundToPlace(getMc().thePlayer.posY - (int) getMc().thePlayer.posY, 3) == MathUtils.roundToPlace(0.6, 3)) {
                        event.setY((getMc().thePlayer.motionY = -0.2));
                    }
                    if (MathUtils.roundToPlace(getMc().thePlayer.posY - (int) getMc().thePlayer.posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                        event.setY((getMc().thePlayer.motionY = -0.3));
                    }
                }
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        lastDist = 0.0D;
                        float motionY = 0.4001f;
                        if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F) && getMc().thePlayer.onGround) {
                            if (getMc().thePlayer.isPotionActive(Potion.jump))
                                motionY += ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.099F);
                            event.setY(getMc().thePlayer.motionY = motionY);
                            if (!Moonx.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                                moveSpeed *= getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? (getMc().thePlayer.isPotionActive(Potion.jump) ? 1.95F : 2.05F) : 1.895F;
                            } else {
                                moveSpeed *= 1.4F;
                            }
                        } else if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F)) {

                        }
                        break;
                    case 3:
                        double boost = Moonx.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() ? 0.725 : (getMc().thePlayer.isPotionActive(Potion.moveSpeed) ? (getMc().thePlayer.isPotionActive(Potion.jump) ? 0.915f : 0.725f) :
                                0.71625f);
                        moveSpeed = lastDist - boost * (lastDist - getBaseMoveSpeed());
                        break;
                    default:
                        ++stage;
                        if ((getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) && stage > 0) {
                            stage = getMc().thePlayer.moveForward == 0.0F && getMc().thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 159D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                setMoveSpeed(event, moveSpeed);
                ++stage;
                break;
            case NCP:
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        double motionY = 0.4025;
                        if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F) && getMc().thePlayer.onGround) {
                            if (getMc().thePlayer.isPotionActive(Potion.jump))
                                motionY += ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(getMc().thePlayer.motionY = motionY);
                            moveSpeed *= 2;
                        }
                        break;
                    case 3:
                        moveSpeed = lastDist - (0.7 * (lastDist - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) && stage > 0) {
                            stage = getMc().thePlayer.moveForward == 0.0F && getMc().thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                setMoveSpeed(event, moveSpeed);
                ++stage;
                break;

            case MINEPLEX:
                double speed = 0;
                getMc().timer.timerSpeed = 1f;
                stage++;
                if (getMc().thePlayer.isCollidedHorizontally) {
                    stage = 50;
                }
                if (getMc().thePlayer.onGround && (getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f)) {
                    getMc().timer.timerSpeed = 3f;
                    getMc().thePlayer.jump();
                    event.setY(getMc().thePlayer.motionY = 0.42);
                    stage = 0;
                    speed = 0;
                }
                if (!getMc().thePlayer.onGround) {
                    if (getMc().thePlayer.motionY > -0.38) {
                        getMc().thePlayer.motionY += 0.023;
                    } else {
                        getMc().thePlayer.motionY += 0.01;
                    }
                    double slowdown = 0.006;
                    speed = (0.8 - (stage * slowdown));
                    if (speed < 0) speed = 0;
                }
                setMoveSpeed(event, speed);
                break;

            case VANILLA:
                setMoveSpeed(event, boost.getValue());
                break;
            case CUBECRAFT:
                setMoveSpeed(event, 2.2);
                getMc().timer.timerSpeed = 0.32f;
                break;
            case CUBECRAFT_TP:
                if (getMc().thePlayer.ticksExisted % 3 == 0) {
                    setMoveSpeed(event, 2.4);
                } else {
                    setMoveSpeed(event, 0.1576);
                }
                break;
            case CUBECRAFT_TEST:
                switch (stage) {
                    case 0:
                        ++stage;
                        lastDist = 0.0D;
                        break;
                    case 2:
                        double motionY = 0.41;
                        if ((getMc().thePlayer.moveForward != 0.0F || getMc().thePlayer.moveStrafing != 0.0F) && getMc().thePlayer.onGround) {
                            if (getMc().thePlayer.isPotionActive(Potion.jump))
                                motionY += ((getMc().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                            event.setY(getMc().thePlayer.motionY = motionY);
                            moveSpeed *= 1.7;
                        }
                        break;
                    case 3:
                        moveSpeed = lastDist - (0.7 * (lastDist - getBaseMoveSpeed()));
                        break;
                    default:
                        if ((getMc().theWorld.getCollidingBoundingBoxes(getMc().thePlayer, getMc().thePlayer.getEntityBoundingBox().offset(0.0D, getMc().thePlayer.motionY, 0.0D)).size() > 0 || getMc().thePlayer.isCollidedVertically) && stage > 0) {
                            stage = getMc().thePlayer.moveForward == 0.0F && getMc().thePlayer.moveStrafing == 0.0F ? 0 : 1;
                        }
                        moveSpeed = lastDist - lastDist / 115.0D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                setMoveSpeed(event, moveSpeed);
                ++stage;
                break;
            default:break;
        }
    }

    public float getRotationFromPosition(final double x, final double z) {
        final double xDiff = x - getMc().thePlayer.posX;
        final double zDiff = z - getMc().thePlayer.posZ;
        return (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0f;
    }

    public boolean inVoid() {
        for (int i = (int) Math.ceil(getMc().thePlayer.posY); i >= 0; i--) {
            if (getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ)).getBlock() != Blocks.air) {
                return false;
            }
        }
        return true;
    }

    private void setMoveSpeed(final MotionEvent event, final double speed) {
        voidTicks++;
        if (KillAura.target != null) {
            if (inVoid() && voidTicks > 4) {
                voidTicks = 0;
                strafeDirection = !strafeDirection;
            }
        }
        boolean shouldStrafe = Moonx.INSTANCE.getModuleManager().getModule("targetstrafe").isEnabled() && TargetStrafe.indexPos != null && TargetStrafe.target != null && !(!getMc().gameSettings.keyBindJump.isKeyDown() && TargetStrafe.spaceOnly.isEnabled());
        double forward = shouldStrafe ? ((Math.abs(getMc().thePlayer.movementInput.moveForward) > 0 || Math.abs(getMc().thePlayer.movementInput.moveStrafe) > 0) ? 1 : 0) : getMc().thePlayer.movementInput.moveForward;
        double strafe = shouldStrafe ? 0 : getMc().thePlayer.movementInput.moveStrafe;
        float yaw = shouldStrafe ? getRotationFromPosition(TargetStrafe.indexPos.xCoord, TargetStrafe.indexPos.zCoord) : getMc().thePlayer.rotationYaw;
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

    private double[] getDirectionalSpeed(final double speed) {
        float forward = getMc().thePlayer.movementInput.moveForward;
        float side = getMc().thePlayer.movementInput.moveStrafe;
        float yaw = getMc().thePlayer.prevRotationYaw + (getMc().thePlayer.rotationYaw - getMc().thePlayer.prevRotationYaw) * getMc().timer.renderPartialTicks;
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (getMc().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = getMc().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }
}

