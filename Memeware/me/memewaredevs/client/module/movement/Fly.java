package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.EventCollide;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.CubeUtil;
import me.memewaredevs.client.util.blocks.misc.BlockUtil;
import me.memewaredevs.client.util.misc.Timer;
import me.memewaredevs.client.util.movement.MovementUtils;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import org.apache.commons.lang3.RandomUtils;

import java.util.function.Consumer;

public class Fly extends Module {

    private Timer timer = new Timer();

    private double x, y, z, mineplexSpeed;
    private boolean back, done;
    boolean down;
    private int counter;
    private double speed;

    public Fly() {
        super("Fly", 34, Module.Category.MOVEMENT);
        this.addDouble("Speed", 2.5, 0.1, 5.0);
        this.addModes("Vanilla", "PvP Temple", "Mineplex", "Hypixel", "Old NCP", "Cubecraft Slow", "Cubecraft Fast", "Collision");
        this.addBoolean("Anti-Kick", false);
    }

    @Handler
    public Consumer<MovementEvent> eventConsumer0 = event -> {

        if (isMode("Cubecraft Slow")) {
            PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(Speed.airSlot()));
            BlockUtil.placeHeldItemUnderPlayer();
            PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            PacketUtil.sendPacketSilent(new C18PacketSpectate(CubeUtil.CUBECRAFT_UUID));
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.ticksExisted % 4 == 0) {
                if(!mc.gameSettings.keyBindJump.getIsKeyPressed())
                    event.setY(0.00003);
                else
                    event.setY(1.0F);
            }
            else {
                MovementUtils.setMoveSpeed(event, MovementUtils.getBaseMoveSpeed());
                if(!mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                    mc.timer.timerSpeed = 1F;
                    event.setY(-0.00001);
                } else {
                    mc.timer.timerSpeed = 0.3F;
                    event.setY(-0.1);
                }

                if(mc.thePlayer.isSneaking()) {
                    mc.timer.timerSpeed = 0.1F;
                    event.setY(-0.6);
                }
            }
            counter++;
        }

        if (isMode("Cubecraft Fast")) {
            if (Speed.airSlot() == -10) {
                return;
            }
            PacketUtil.sendPacketSilent(new C18PacketSpectate(CubeUtil.CUBECRAFT_UUID));
            if (mc.thePlayer.ticksExisted % 3 != 0) {
                event.setY(-0.00015F);
            } else {
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(Speed.airSlot()));
                BlockUtil.placeHeldItemUnderPlayer();
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                MovementUtils.setMoveSpeed(event, 2.1);
                event.setY(0.0003F);
            }
            mc.thePlayer.motionY = 0;
        }

        if (isMode("Old NCP")) {
            if (mc.thePlayer.ticksExisted % 2 == 0)
                event.setY(-1.0E-9);
            else
                event.setY(1.0E-9);
            mc.thePlayer.motionY = 0;
        }

        if (isMode("PvP Temple")) {
            mc.timer.timerSpeed = 0.6F;
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                MovementUtils.setMoveSpeed(event, 7.02);
            }
            else {
                MovementUtils.setMoveSpeed(event, 0);
            }
            if (mc.thePlayer.ticksExisted % 10 == 0) {
                event.setY(-1F);
                down = true;
            }
            else {
                if (down) {
                    event.setY(1F);
                    down = false;
                }
                else {
                    event.setY(0);
                }
            }
            mc.thePlayer.motionY = 0;
        }

        if (isMode("Mineplex")) {
            if (Speed.airSlot() == -10){
                MovementUtils.setMoveSpeed(event, 0);
                return;
            }

            if (!done) {
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(Speed.airSlot()));
                BlockUtil.placeHeldItemUnderPlayer();
                MovementUtils.setMoveSpeed(event, back ? -mineplexSpeed : mineplexSpeed);
                back = !back;
                if (mc.thePlayer.isMovingOnGround() && mc.thePlayer.ticksExisted % 2 == 0) {
                    mineplexSpeed += RandomUtils.nextDouble(0.125D, 0.12505D);
                }
                if (mineplexSpeed >= getDouble("Speed") * 1.3 && mc.thePlayer.isCollidedVertically && mc.thePlayer.isMovingOnGround()) {
                    event.setY(mc.thePlayer.motionY = 0.42F);
                    MovementUtils.setMoveSpeed(event, 0);
                    done = true;
                    return;
                }
            } else {
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                if (mc.thePlayer.fallDistance == 0) {
                    event.setY(mc.thePlayer.motionY += 0.039);
                } else if (mc.thePlayer.fallDistance <= 1.4) {
                    event.setY(mc.thePlayer.motionY += 0.032);
                }
                MovementUtils.setMoveSpeed(event, mineplexSpeed *= 0.979);
                if (mc.thePlayer.isMoving() && mc.thePlayer.isCollidedVertically) {
                    done = false;
                }
            }
        }
    };

    @Handler
    public Consumer<UpdateEvent> eventConsumer1 = (event) -> {

        if (isMode("Hypixel") && event.isPre()) {
            mc.thePlayer.motionY = 0;

            double y = mc.thePlayer.posY + 1.0E-9;

            if (mc.thePlayer.ticksExisted % 2 == 0) {
                y += 1.0E-8;
            }

            event.setY(y);
        }

        if (isMode("AACv3.0.x")) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.motionY = 0.1;
                event.setGround(true);
            }
        }
        if (isMode("Vanilla")) {
            mc.timer.timerSpeed = 1f;
            if (event.isPre()) {
                float speed = getDouble("Speed").floatValue();
                MovementInput movementInput = mc.thePlayer.movementInput;
                mc.thePlayer.motionY = movementInput.isJumping() ? speed * 0.5F
                        : movementInput.isSneaking() ? -speed * 0.5F
                        : 0.0F;
                MovementUtils.setMoveSpeed(speed);
                if (getBool("Anti-Kick") && timer.delay(700.0f)) {
                    MovementUtils.fallPacket();
                    MovementUtils.ascendPacket();
                    timer.reset();
                }
            }
        }
    };

    @Handler
    public Consumer<EventCollide> eventConsumer2 = (event) -> {
        if (isMode("Collision") && !mc.thePlayer.isSneaking())
            event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
    };

    @Override
    public void onEnable() {
//        if(isMode("Hypixel"))
//            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        mineplexSpeed = 0;
        speed = 0;
        timer.reset();
        done = false;
        back = false;
        down = false;
    }

    @Override
    public void onDisable() {
        if (isMode("Cubecraft Fast") || isMode("Cubecraft Slow")) {
            mc.thePlayer.motionY -= 0.5F;
            MovementUtils.setMoveSpeed(0);
        }
        if (isMode("Mineplex"))
            PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

        MovementUtils.setMoveSpeed(0.15F);
        mc.timer.timerSpeed = 1.0F;
    }
}