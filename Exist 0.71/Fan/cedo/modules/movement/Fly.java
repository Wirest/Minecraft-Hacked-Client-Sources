package cedo.modules.movement;

import cedo.Fan;
import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.notifications.Notification;
import cedo.ui.notifications.NotificationType;
import cedo.util.PacketUtil;
import cedo.util.movement.MovementUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Fly extends Module {
    public ModeSetting mode = new ModeSetting("Bypass", "Blinkless2", "Blink", "Blinkless", "Blinkless2", "TP");
    public NumberSetting speed = new NumberSetting("Speed", 0.6, 0.1, 1.5, 0.1),
            timerSpeed = new NumberSetting("Timer Speed", 2, 1, 10, 0.1),
            timerTicks = new NumberSetting("Timer Ticks", 25, 0, 200, 1),
            blinkPackets = new NumberSetting("Blink Packets", 30, 2, 200, 1);
    public double positionOffset, speedMult, stageTwoMult, startOffset, jumpModifier, damageOffset, damageY, damageYTwo, yDown, yUp;
    private int ticks, stage;
    private double lastDist, moveSpeed, y;
    private Vec3 lastPos;
    private boolean tp;
    private ArrayList<Packet<?>> packets = new ArrayList<>();

    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
        this.addSettings(mode, speed, timerSpeed, timerTicks);
        requiresDisabler = true;
        disableOnLagback = true;

        setFlyValues();
    }

    public void setFlyValues() {
        if (Wrapper.authorized) {
            positionOffset = 0.002957;
            speedMult = 2.14946758;
            stageTwoMult = 1.296775;
            startOffset = 0.00032488568;
            jumpModifier = 0.39999994D;
            damageOffset = 0.06011F;
            damageY = 0.000495765F;
            damageYTwo = 0.0049575F;
            yDown = -0.94965866F;
            yUp = 0.00049646;
        }
    }

    public void onEnable() {
        mc.timer.timerSpeed = 1.0F;

        if (!mc.thePlayer.onGround) {
            toggleSilent();
            Fan.notificationManager.removeLast(notification -> notification.getDescription().contains("Fly"));
            Notification.post(NotificationType.WARNING, "Couldn't toggle", "You must be on the ground to toggle Fly");
            return;
        }

        MovementUtil.setSpeed(y = lastDist = moveSpeed = mc.thePlayer.stepHeight = stage = ticks = 0);
        super.onEnable();

        disableOnLagback = true;
        if(mode.is("TP")) {
            disableOnLagback = false;
            NetHandlerPlayClient netHandler = mc.getNetHandler();
            PacketUtil.sendPacketNoEvent((new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true)));
            PacketUtil.sendPacketNoEvent((new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.21, mc.thePlayer.posZ, true)));
            PacketUtil.sendPacketNoEvent((new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, true)));
            lastPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            tp = false;
            packets.clear();
            mc.thePlayer.stepHeight = 0F;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

    public void onDisable() {
        if(!mode.is("TP")) {
            MovementUtil.setSpeed(mc.thePlayer.jumpMovementFactor = 0);
            mc.thePlayer.capabilities.allowFlying = mc.thePlayer.capabilities.isFlying = false;
            mc.timer.timerSpeed = 1f;
            mc.thePlayer.stepHeight = 0.625F;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
            super.onDisable();
        } else {
            PacketUtil.sendPacketNoEvent(new C0CPacketInput(0, 0, true, false));
            PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            //u do need this however
            for(Packet<?> packet : packets){
                PacketUtil.sendPacketNoEvent(packet);
            }
            packets.clear();
        }
    }

    public void onEvent(Event e) {
        if(!mode.is("TP")) {
            if (e instanceof EventUpdate) {
                if (mc.thePlayer.isSneaking())
                    mc.thePlayer.setSneaking(false);
                mc.thePlayer.capabilities.isCreativeMode = true;
            } else if (e instanceof EventMove) {
                EventMove event = (EventMove) e;

                if (MovementUtil.isMoving()) {
                    switch (stage) {
                        case 0:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                damage();
                                moveSpeed = 0.5 * speed.getValue();
                            }
                            break;
                        case 1:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                event.y = (mc.thePlayer.motionY = MovementUtil.getJumpBoostModifier(jumpModifier));
                            }
                            moveSpeed *= speedMult;
                            break;
                        case 2:
                            moveSpeed = stageTwoMult * speed.getValue();
                            break;
                        default:
                            moveSpeed = lastDist - lastDist / 159.0D;
                            break;
                    }
                    event.setSpeed(Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed()));
                    stage++;
                }
            } else if (e instanceof EventMotion) {
                EventMotion event = (EventMotion) e;

                if (stage < timerTicks.getValue())
                    mc.timer.timerSpeed = (float) timerSpeed.getValue();
                else
                    mc.timer.timerSpeed = 1;

                if (e.isPre()) {
                    if (stage > 2)
                        mc.thePlayer.motionY = 0;

                    mc.thePlayer.cameraYaw = 0.105F;
                    if (stage > 2) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - positionOffset, mc.thePlayer.posZ);
                        ticks++;
                        double offset = startOffset;
                        switch (ticks) {
                            case 1:
                                y *= yDown;
                                break;
                            case 2:
                            case 3:
                            case 4:
                                y += offset;
                                break;
                            case 5:
                                y += yUp;
                                ticks = 0;
                                break;
                        }
                        event.setY(mc.thePlayer.posY + y);
                    }
                } else if (stage > 2)
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + positionOffset, mc.thePlayer.posZ);

                if (e.isPre()) {
                    double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
                }
            }
            if (e instanceof EventPacket && e.isPre()) {
                EventPacket event = (EventPacket) e;

                if (e.isOutgoing()) {
                    if (stage == 0)
                        e.setCancelled(true);
                } else {
                    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                        Notification.post(NotificationType.WARNING, "Lagback", "Lagback detected, toggling Fly");
                        toggleSilent();
                    }
                }
            }
        } else {
            if (e instanceof EventPacket) {
                EventPacket event = (EventPacket) e;
                if (!tp) {
                    if (event.getPacket() instanceof C03PacketPlayer) {
                        C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
                        event.setCancelled(true);
                        if (c03.isMoving())
                            packets.add(c03);
                    }

                    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                        S08PacketPlayerPosLook pos = (S08PacketPlayerPosLook) event.getPacket();
                        lastPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(pos.getX(), pos.getY() + 0.000032287, pos.getZ(), false));
                        event.setCancelled(true);
                        tp = true;
                    }
                }
            } else if (e instanceof  EventMove) {
                EventMove event = (EventMove) e;
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                event.setY(mc.gameSettings.keyBindJump.isKeyDown() ? 0.42F : mc.gameSettings.keyBindSneak.isKeyDown() ? -0.42F : 0);
                if(!tp){
                    mc.thePlayer.motionY = 0.0D;
                }
                if (tp) {
                    ticks++;
                    if (ticks == 1) {
                        for (int i = 0; i < packets.size(); i += 3) {
                            final Packet<?> packet = packets.get(i);
                            PacketUtil.sendPacketNoEvent(packet);
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                        }
                        mc.thePlayer.setPosition(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord);
                        toggle();
                    }
                }

                MovementUtil.setSpeed(event, speed.getValue());
            }
        }
    }

    public void damage() {
        double offset = damageOffset;
        NetHandlerPlayClient netHandler = mc.getNetHandler();
        EntityPlayerSP player = mc.thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        for (int i = 0; i < (getMaxFallDist() / (offset - 0.005F)) + 1; i++) {
            PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(x, y + offset, z, false));
            PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(x, y + damageY, z, false));
            PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(x, y + damageYTwo + offset * 0.000001, z, false));
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
    }

    public float getMaxFallDist() {
        PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? (potioneffect.getAmplifier() + 1) : 0;
        return mc.thePlayer.getMaxFallHeight() + f;
    }
}
