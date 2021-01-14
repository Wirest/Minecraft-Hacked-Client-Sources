/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang3.RandomUtils
 */
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.event.events.PacketInEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.ChatUtil;
import me.memewaredevs.client.util.CubeUtil;
import me.memewaredevs.client.util.blocks.misc.BlockUtil;
import me.memewaredevs.client.util.movement.DynamicFriction;
import me.memewaredevs.client.util.movement.MovementUtils;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.apache.commons.lang3.RandomUtils;

import java.util.function.Consumer;

public class Speed extends Module {
    private DynamicFriction dynamicNCPFriction = new DynamicFriction();
    private double speed, currentDistance, lastDistance;
    public boolean prevOnGround;

    public Speed() {
        super("Speed", 33, Module.Category.MOVEMENT);
        addModes("Vanilla", "PvPTemple", "Mineplex", "Mineplex2", "Cubecraft Slow", "NCP Fast", "Cubecraft Fast", "Hypixel");
        addBoolean("Flag Check", true);
        addDouble("Vanilla", "Speed", 0.47, 0.25, 5);
    }

    @Handler
    public Consumer<PacketInEvent> eventConsumer0 = (event) -> {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && getBool("Flag Check")) {
            if (mc.thePlayer != null && mc.theWorld != null) {
                ChatUtil.printChat("Speed disabled because you flagged/got teleported!");
                toggle();
            }
        }

    };

    @Handler
    public Consumer<MovementEvent> eventConsumer1 = (event) -> {
        if (isMode("Mineplex2")) {
            if (airSlot() == -10){
                return;
            }
            if (mc.thePlayer.ticksExisted % 3 == 0 && mc.thePlayer.isMovingOnGround()) {
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(Speed.airSlot()));
                BlockUtil.placeHeldItemUnderPlayer();
                speed += 0.125F;
            }
            else {
                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                speed *= 0.98;
            }
            if (speed > 1.24) {
                speed = 1.24;
            }
            MovementUtils.setMoveSpeed(event, speed);
        }
        if (isMode("PvPTemple")) {
            if (mc.thePlayer.isMovingOnGround()) {
                event.setY(mc.thePlayer.motionY = 0.42F);
                speed = 0.5;
                prevOnGround = true;
            }
            else if (prevOnGround) {
                speed = 0.8;
                prevOnGround = false;
            }
            else {
                speed *= 0.932;
            }
            MovementUtils.setMoveSpeed(event, speed);
        }

        if (isMode("Vanilla")) {
            double max = this.getDouble("Vanilla", "Speed");
            MovementUtils.setMoveSpeed(event, max);
        }
        if (isMode("Cubecraft Slow")) {
            PacketUtil.sendPacketSilent(new C18PacketSpectate(CubeUtil.CUBECRAFT_UUID));
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.ticksExisted % 4 == 0) {
                MovementUtils.setMoveSpeed(event, MovementUtils.getBaseMoveSpeed() + 1.276444466666);
            }
            else {
                MovementUtils.setMoveSpeed(event, MovementUtils.getBaseMoveSpeed() + 1.0E-12);
            }
        }

        if (isMode("Cubecraft Fast")) {
            mc.gameSettings.keyBindJump.pressed = false;
            mc.thePlayer.cameraYaw = 0;
            if(mc.thePlayer.ticksExisted % 4 == 0)
                MovementUtils.setMoveSpeed(event, MovementUtils.getBaseMoveSpeed() + 1.55522225D);
            else
                MovementUtils.setMoveSpeed(event, MovementUtils.getBaseMoveSpeed());

            if (mc.thePlayer.isMoving() && mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 4 == 0)
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0922222333D * RandomUtils.nextDouble(0.75D, 1D), mc.thePlayer.posZ);

            if(mc.thePlayer.ticksExisted % 8 == 0)
                MovementUtils.offsetXZ(0.12115522522);
        }
        if (isMode("Mineplex")) {
            mc.timer.timerSpeed = 1.0F;
            if (mc.thePlayer.isMovingOnGround()) {
                mc.timer.timerSpeed = 1F;
                event.setY(mc.thePlayer.motionY = 0.4F);
                prevOnGround = true;
                currentDistance = speed;
                speed = 0;
            } else {
                mc.timer.timerSpeed = 1.0F;
                if (prevOnGround) {
                    speed = currentDistance + 0.56F;
                    prevOnGround = false;
                } else {
                    speed = lastDistance * (speed > 2.2 ? 0.975 : speed >= 1.5 ? 0.98 : 0.985);
                }
            }
            double max = this.getDouble("Vanilla", "Speed");
            MovementUtils.setMoveSpeed(event, Math.max(Math.min(speed, max), prevOnGround ? 0 : 0.42));
        }

        if (isMode("Vanilla")) {
            MovementUtils.setMoveSpeed(event, getDouble("Vanilla", "Speed"));
        }

        if (isMode("Hypixel")) {
            mc.timer.timerSpeed = 1F;

            if(mc.thePlayer.isMovingOnGround()) {
                mc.timer.timerSpeed = 1.25F;
                event.setY(mc.thePlayer.motionY = 0.3999175222222555F);
                speed = MovementUtils.getBaseMoveSpeed() * 2.1492287832;
                prevOnGround = true;
            }
            else {
                if (prevOnGround) {
                    speed -= 0.66333332222 * (speed - MovementUtils.getBaseMoveSpeed());
                    prevOnGround = false;
                }
                else {
                    speed -= speed / 160 - 1.0E-9;
                }
            }
            MovementUtils.setMoveSpeed(event, speed);
        }
        if (isMode("NCP Fast")) {
            this.dynamicNCPFriction.updateFriction(event, 0.3928999999, 1.14999, 159, 0.66);
        }
    };

    @Handler
    public Consumer<UpdateEvent> eventConsumer2 = (event) -> {
        if (event.isPre()) {
            if (isMode("Hypixel")) {
                event.setY(event.getY() + 1.73E-11D);
            }
            double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDistance = Math.hypot(x, z);
        }
    };

    public static int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        ChatUtil.printChat("Clear a hotbar slot.");
        return -10;
    }

    @Override
    public void onEnable() {
        speed = 0;
        prevOnGround = true;
        lastDistance = 0;
    }

    @Override
    public void onDisable() {
        if (isMode("Mineplex2")) {
            PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
        mc.thePlayer.jumpMovementFactor = 0.0F;
        MovementUtils.setMoveSpeed(0);
        mc.timer.timerSpeed = 1.0F;

        if (isMode("Cubecraft Fast") || isMode("Cubecraft Slow")) {
            mc.thePlayer.motionY = 0F;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2664444, mc.thePlayer.posZ);
        }
    }
}