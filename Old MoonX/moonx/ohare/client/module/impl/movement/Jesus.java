package moonx.ohare.client.module.impl.movement;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.BoundingBoxEvent;
import moonx.ohare.client.event.impl.player.MotionEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.MoveUtil;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public class Jesus extends Module {
    private EnumValue<modegay> GAY = new EnumValue<>("Mode", modegay.VANILLA);
    private boolean wasWater;
    private TimerUtil timer = new TimerUtil();
    public Jesus() {
        super("Jesus", Category.MOVEMENT, new Color(142, 248, 255).getRGB());
        setDescription("Walking on water like a jew");
    }

    public enum modegay {
        VANILLA, NCP, DOLPHIN, DEV
    }
    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer != null) {
            setSuffix(StringUtils.capitalize(GAY.getValue().name().toLowerCase()));
            switch (GAY.getValue()) {
                case DEV:
                    if (event.isPre()) {
                        if (isInLiquid() && !getMc().thePlayer.isSneaking()) {
                            if (getMc().thePlayer.ticksExisted % 2 == 0) {
                                getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 0.005 + MathUtils.getRandomInRange(0.001, 0.01),getMc().thePlayer.posZ);
                              //  getMc().thePlayer.motionY = 0.005 + MathUtils.getRandomInRange(0.001, 0.005);
                            }
                            getMc().thePlayer.motionX *= 0.975;
                            getMc().thePlayer.motionZ *= 0.975;
                            event.setOnGround(false);
                            getMc().thePlayer.onGround = true;
                        }
                    }
                    break;
                case VANILLA:
                    if (event.isPre()) {
                        if (isOnLiquid()) {
                            event.setOnGround(false);
                        }
                        if (!event.isPre() || (getMc().thePlayer.isBurning() && isOnWater())) return;
                        if (isInLiquid() && !getMc().gameSettings.keyBindSneak.isKeyDown() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance < 3) {
                            getMc().thePlayer.motionY = 0.1;
                        }
                    }
                    break;
                case NCP:
                    if (!event.isPre() || (getMc().thePlayer.isBurning() && isOnWater())) return;
                    if (isInLiquid() && !getMc().gameSettings.keyBindSneak.isKeyDown() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance < 3) {
                        getMc().thePlayer.motionY = 0.1;
                    }
                    break;
                case DOLPHIN:
                    if (event.isPre()) {
                        if (getMc().thePlayer.isSneaking()) {
                            return;
                        }
                        if (getMc().thePlayer.onGround || getMc().thePlayer.isOnLadder()) {
                            wasWater = false;
                        }
                        if (wasWater && getMc().thePlayer.motionY > 0.0) {
                            if (getMc().thePlayer.motionY < 0.03) {
                                getMc().thePlayer.motionY *= 1.2;
                                getMc().thePlayer.motionY += 0.067;
                            } else if (getMc().thePlayer.motionY < 0.05) {
                                getMc().thePlayer.motionY *= 1.2;
                                getMc().thePlayer.motionY += 0.06;
                            } else if (getMc().thePlayer.motionY < 0.07) {
                                getMc().thePlayer.motionY *= 1.2;
                                getMc().thePlayer.motionY += 0.057;
                            } else if (getMc().thePlayer.motionY < 0.11) {
                                getMc().thePlayer.motionY *= 1.2;
                                getMc().thePlayer.motionY += 0.0534;
                            } else {
                                getMc().thePlayer.motionY += 0.0517;
                            }
                        }
                        if (wasWater && getMc().thePlayer.motionY < 0.0 && getMc().thePlayer.motionY > -0.3) {
                            getMc().thePlayer.motionY += 0.04;
                        }
                        if (!isOnLiquid()) {
                            return;
                        }
                        if (MathHelper.ceiling_double_int(getMc().thePlayer.posY) != getMc().thePlayer.posY + 1.0E-6) {
                            return;
                        }
                        getMc().thePlayer.motionY = 0.5;
                        setSpeed(getSpeed() * 1.35);
                        wasWater = true;
                    }

                    break;
            }
        }
    }

    @Handler
    public void onBoundingBox(BoundingBoxEvent event) {
        if (getMc().thePlayer != null) {
            Block block = getMc().theWorld.getBlockState(event.getBlockPos()).getBlock();
            switch (GAY.getValue()) {
                case DEV:
                    if (getMc().theWorld == null || getMc().thePlayer.fallDistance > 3 || (getMc().thePlayer.isBurning() && isOnWater()))
                        return;
                    if (!(block instanceof BlockLiquid) || getMc().thePlayer.isSneaking())
                        return;
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).contract(0, 0.25, 0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
                case VANILLA:
                    if (getMc().theWorld == null || getMc().thePlayer.fallDistance > 3 || (getMc().thePlayer.isBurning() && isOnWater()))
                        return;
                    if (!(block instanceof BlockLiquid) || isInLiquid() || getMc().thePlayer.isSneaking())
                        return;
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).contract(0, 0, 0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
                case DOLPHIN:
                    if (!(event.getBlock() instanceof BlockLiquid) || isInLiquid() || getMc().thePlayer.isSneaking() || (getMc().thePlayer.isBurning() && isOnWater())) {
                        return;
                    }
                    event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 1.0E-6, 0.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
                case NCP:
                    if (getMc().theWorld == null || getMc().thePlayer.fallDistance > 3 || (getMc().thePlayer.isBurning() && isOnWater()))
                        return;
                    if (!(block instanceof BlockLiquid) || isInLiquid() || getMc().thePlayer.isSneaking())
                        return;
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).contract(0, 0.000000000002000111, 0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
            }
        }
    }

    @Handler
    public void onPacketSent(PacketEvent event) {
        if (getMc().thePlayer != null) {
            switch (GAY.getValue()) {
                case NCP:
                    if (!(event.getPacket() instanceof C03PacketPlayer) || !event.isSending() || isInLiquid() || !isOnLiquid() || getMc().thePlayer.isSneaking() || getMc().thePlayer.fallDistance > 3 || (getMc().thePlayer.isBurning() && isOnWater()))
                        return;
                    C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                    if (event.getPacket() instanceof C03PacketPlayer && !getMc().thePlayer.isMoving()) event.setCanceled(true);
                    if (getMc().thePlayer.isSprinting() && getMc().thePlayer.isInLava() && isOnLiquid())
                        getMc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(getMc().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    getMc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(getMc().thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    packet.setY(packet.getPositionY() + (getMc().thePlayer.ticksExisted % 2 == 0 ? 0.000000000002000111 : 0));
                    packet.setOnGround(getMc().thePlayer.ticksExisted % 2 != 0);
                    break;
            }
        }
    }


    private boolean isOnLiquid() {
        final double y = getMc().thePlayer.posY - 0.015625;
        for (int x = MathHelper.floor_double(getMc().thePlayer.posX); x < MathHelper.ceiling_double_int(getMc().thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(getMc().thePlayer.posZ); z < MathHelper.ceiling_double_int(getMc().thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOnWater() {
        final double y = getMc().thePlayer.posY - 0.03;
        for (int x = MathHelper.floor_double(getMc().thePlayer.posX); x < MathHelper.ceiling_double_int(getMc().thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(getMc().thePlayer.posZ); z < MathHelper.ceiling_double_int(getMc().thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid && getMc().theWorld.getBlockState(pos).getBlock().getMaterial() == Material.water) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        final double y = getMc().thePlayer.posY + 0.01;
        for (int x = MathHelper.floor_double(getMc().thePlayer.posX); x < MathHelper.ceiling_double_int(getMc().thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(getMc().thePlayer.posZ); z < MathHelper.ceiling_double_int(getMc().thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int) y, z);
                if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private double getSpeed() {
        return Math.sqrt(getMc().thePlayer.motionX * getMc().thePlayer.motionX + getMc().thePlayer.motionZ * getMc().thePlayer.motionZ);
    }

    private void setSpeed(final double speed) {
        getMc().thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        getMc().thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }
    private boolean isBlockUnder() {
        for (int i = (int) (getMc().thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ);
            if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
    private float getDirection() {
        float direction = getMc().thePlayer.rotationYaw;
        if (getMc().thePlayer.moveForward < 0.0f) {
            direction += 180.0f;
        }
        float forward;
        if (getMc().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (getMc().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        } else {
            forward = 1.0f;
        }
        if (getMc().thePlayer.moveStrafing > 0.0f) {
            direction -= 90.0f * forward;
        } else if (getMc().thePlayer.moveStrafing < 0.0f) {
            direction += 90.0f * forward;
        }
        direction *= 0.017453292f;
        return direction;
    }


    public enum Mode {
        SOLID, BOUNCE
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        wasWater = false;
    }
}
