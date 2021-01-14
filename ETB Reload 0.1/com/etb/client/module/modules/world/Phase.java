package com.etb.client.module.modules.world;

import java.awt.Color;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.game.TickEvent;
import com.etb.client.event.events.player.BoundingBoxEvent;
import com.etb.client.event.events.player.MotionEvent;
import com.etb.client.event.events.player.PushEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.render.InsideBlockRenderEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase extends Module {
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.Hypixel);
    private int moveUnder;
    private NumberValue<Double> distance = new NumberValue("Distance", 2.0, 0.0, 10.0, 0.1);
    private NumberValue<Double> vanillaspeed = new NumberValue("VanillaSpeed", 0.5, 0.0, 3.5, 0.1);
    private NumberValue<Double> verticalspeed = new NumberValue("VerticalSpeed", 0.5, 0.0, 3.5, 0.1);
    public static boolean phasing;
    private int delay;
    private int state;
    private boolean NCPSetup;
    private TimerUtil timer = new TimerUtil();

    public Phase() {
        super("Phase", Category.WORLD, new Color(25, 255, 255, 255).getRGB());
        setDescription("Noclip through blocks.");
        addValues(mode, distance, vanillaspeed, verticalspeed);
    }

    public enum Mode {
        Hypixel, Aris, Vanilla, NCP
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.thePlayer == null) return;
        if (mode.getValue() == Mode.Vanilla) {
            if (mc.thePlayer != null && moveUnder == 1) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                moveUnder = 0;
            }
            double multiplier = 1;
            double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
            double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
            double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
            if (mc.thePlayer.isCollidedHorizontally && !isInsideBlock() && mc.thePlayer.isMoving()) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, true));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
            }
        }
        if (mode.getValue() == Mode.NCP) {
            if (mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindSprint.isKeyDown()) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
            }
        }
    }

    @Subscribe
    public void run(BoundingBoxEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (mode.getValue() == Mode.Aris) {
            if (event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY && mc.thePlayer.isSneaking()) {
                event.setBoundingBox(null);
            }
        }
        if (mode.getValue() == Mode.Hypixel) {
            if (isInsideBlock()) {
                event.setBoundingBox(null);
            }
        }
        if (mode.getValue() == Mode.Vanilla) {
            if (isInsideBlock()) {
                event.setBoundingBox(null);
            }
        }
        if (mode.getValue() == Mode.NCP) {
            if (this.isInBlock(mc.thePlayer, 0.0f) && !mc.gameSettings.keyBindSprint.isKeyDown() && event.getBlockPos().getY() > mc.thePlayer.getEntityBoundingBox().minY - 0.4 && event.getBlockPos().getY() < mc.thePlayer.getEntityBoundingBox().maxY + 1.0) {
                mc.thePlayer.jumpMovementFactor = 0;
                event.setBoundingBox(null);
            }
            if (this.isInBlock(mc.thePlayer, 0.0f) && mc.gameSettings.keyBindSprint.isKeyDown()) {
                event.setBoundingBox(null);
            }
        }
    }

    @Subscribe
    public void onPacketSend(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getFormattedText().contains("You cannot go past the border.")) {
                event.setCanceled(true);
            }
        }
        if (mode.getValue() == Mode.Vanilla) {
            if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
        }
        if (mode.getValue() == Mode.NCP) {
            if (event.isSending()) {
                if (event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isMoving() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Subscribe
    public void onMove(MotionEvent event) {
        if (mode.getValue() == Mode.Hypixel) {
            if (isInsideBlock()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) event.setY(mc.thePlayer.motionY += 0.09f);
                else if (mc.gameSettings.keyBindSneak.isKeyDown()) event.setY(mc.thePlayer.motionY -= 0.00);
                else event.setY(mc.thePlayer.motionY = 0.0f);
                setMoveSpeed(event, 0.3);
            }
        }
        if (mode.getValue() == Mode.Aris) {
            if (isInsideBlock() && mc.thePlayer.isSneaking()) {
                final float yaw = mc.thePlayer.rotationYaw;
                phasing = true;
                mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(distance.getValue() * -Math.sin(Math.toRadians(yaw)), 0.0, distance.getValue() * Math.cos(Math.toRadians(yaw)));
            } else {
                phasing = false;
            }
        }
        if (mode.getValue() == Mode.Vanilla) {
            if (isInsideBlock()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = verticalspeed.getValue());
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = -verticalspeed.getValue());
                } else {
                    event.setY(mc.thePlayer.motionY = 0.0);
                }
                setMoveSpeed(event, vanillaspeed.getValue());
            }
        }
    }

    @Subscribe
    public void onPush(PushEvent event) {
        event.setCanceled(true);
    }

    @Subscribe
    public void onInside(InsideBlockRenderEvent event) {
        event.setCanceled(true);
    }

    private void setMoveSpeed(MotionEvent event, double speed) {
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

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mode.getValue() == Mode.Hypixel) {
            if (!event.isPre()) {
                double multiplier = 0.3;
                double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
                double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
                double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
                double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
                if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                    for (int i = 1; i < 10; ++i) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 8.988465674311579E307, mc.thePlayer.posZ, false));
                    }
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                }
            }
        }
        if (mode.getValue() == Mode.Vanilla) {
            if (mc.gameSettings.keyBindSneak.isPressed() && !isInsideBlock()) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
                moveUnder = 2;
            }
        }
        if (mode.getValue() == Mode.NCP) {
            final float dist = 2.0f;
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward != 0.0f) {
                ++this.delay;
                final String lowerCase;
                switch (lowerCase = mc.getRenderViewEntity().getHorizontalFacing().name().toLowerCase()) {
                    case "east": {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 9.999999747378752E-6, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        break;
                    }
                    case "west": {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - 9.999999747378752E-6, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        break;
                    }
                    case "north": {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 9.999999747378752E-6, false));
                        break;
                    }
                    case "south": {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 9.999999747378752E-6, false));
                        break;
                    }
                    default:
                        break;
                }
                if (this.delay >= 1) {
                    final String lowerCase2;
                    switch (lowerCase2 = mc.getRenderViewEntity().getHorizontalFacing().name().toLowerCase()) {
                        case "east": {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 2.0, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                            break;
                        }
                        case "west": {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - 2.0, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                            break;
                        }
                        case "north": {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 2.0, false));
                            break;
                        }
                        case "south": {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 2.0, false));
                            break;
                        }
                        default:
                            break;
                    }
                    this.delay = 0;
                }
            }
        }
    }
    private boolean isInBlock(Entity e, float offset) {
        for (int x = MathHelper.floor_double(e.getEntityBoundingBox().minX); x < MathHelper.floor_double(e.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(e.getEntityBoundingBox().minY); y < MathHelper.floor_double(e.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(e.getEntityBoundingBox().minZ); z < MathHelper.floor_double(e.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = mc.theWorld.getBlockState(new BlockPos(x, y + offset, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
                        final AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y + offset, z), mc.theWorld.getBlockState(new BlockPos(x, y + offset, z)));
                        if (boundingBox != null && e.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if ((boundingBox != null) && (mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void teleport(double dist) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += (forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += (forward > 0.0D ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1;
            } else if (forward < 0.0D) {
                forward = -1;
            }
        }
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0F));
        double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0F));
        mc.thePlayer.setPosition(x + xspeed, y, z + zspeed);

    }

    @Override
    public void onEnable() {
        phasing = false;
    }

    @Override
    public void onDisable() {
        this.delay = 0;
        this.NCPSetup = false;
        timer.reset();
    }
}
