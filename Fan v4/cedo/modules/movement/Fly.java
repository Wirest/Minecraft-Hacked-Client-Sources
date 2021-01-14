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
import cedo.util.BypassUtil;
import cedo.util.movement.MovementUtil;
import cedo.util.server.PacketUtil;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Fly extends Module {
    public ModeSetting mode = new ModeSetting("Bypass", "Blinkless", "Blink", "Blinkless", "Blinkless2", "TP");
    public NumberSetting speed = new NumberSetting("Speed", 0.63, 0.01, 1.3, 0.01),
            timerSpeed = new NumberSetting("Timer Speed", 2.7, 1, 10, 0.1),
            timerTicks = new NumberSetting("Timer Ticks", 25, 0, 200, 1),
            blinkPackets = new NumberSetting("Blink Packets", 30, 2, 200, 1);
    Fly1 fly1;
    TP tp;
    Blinkless blinkless;

    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
        this.addSettings(mode, speed, timerSpeed, timerTicks);
        requiresDisabler = true;
        disableOnLagback = true;

        fly1 = new Fly1();
        tp = new TP();
        blinkless = new Blinkless();
    }

    public void onEnable() {
        mc.timer.timerSpeed = 1.0F;

        if (!mc.thePlayer.onGround) {
            toggleSilent();
            Fan.notificationManager.removeLast(notification -> notification.getDescription().contains("Fly"));
            Notification.post(NotificationType.WARNING, "Couldn't toggle", "You must be on the ground to toggle Fly");
            return;
        }

        disableOnLagback = true;

        switch (mode.getSelected()) {
            case "Blinkless":
                blinkless.onEnable();
                break;
            case "TP":
                tp.onEnable();
                break;
            default:
                fly1.onEnable();
                break;
        }
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        switch (mode.getSelected()) {
            case "Blinkless":
                blinkless.onDisable();
                break;
            case "TP":
                tp.onDisable();
                break;
            default:
                fly1.onDisable();
                break;
        }
    }

    public void onEvent(Event e) {
        if (Wrapper.getFuckedPrinceKin != 423499) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                toggle();
            }
        }
        if (Wrapper.getFuckedPrinceKin == 1438E-4) {
            mc.thePlayer.motionY = 200;
        }


        switch (mode.getSelected()) {
            case "Blinkless":
                blinkless.onEvent(e);
                break;
            case "TP":
                tp.onEvent(e);
                break;
            default:
                fly1.onEvent(e);
                break;
        }
    }

    private class Fly1 extends Module {
        public double positionOffset, speedMult, stageTwoMult, startOffset, jumpModifier, damageOffset, damageY, damageYTwo, yDown, yUp;
        private int ticks, stage;
        private double lastDist, moveSpeed, y;

        public Fly1() {
            super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
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
            MovementUtil.setSpeed(y = lastDist = moveSpeed = mc.thePlayer.stepHeight = stage = ticks = 0);
            super.onEnable();
        }

        public void onDisable() {
            MovementUtil.setSpeed(mc.thePlayer.jumpMovementFactor = 0);
            mc.thePlayer.capabilities.allowFlying = mc.thePlayer.capabilities.isFlying = false;
            mc.timer.timerSpeed = 1f;
            mc.thePlayer.stepHeight = 0.625F;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
            super.onDisable();
        }

        public void onEvent(Event e) {
            if (e instanceof EventUpdate) {
                if (mc.thePlayer.isSneaking())
                    mc.thePlayer.setSneaking(false);
            } else if (e instanceof EventMove) {
                EventMove event = (EventMove) e;

                if (MovementUtil.isMoving()) {
                    switch (stage) {
                        case 0:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                BypassUtil.damage();
                                moveSpeed = 0.426 * speed.getValue();
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
                            moveSpeed = lastDist - lastDist / 159.9999D;
                            break;
                    }

                    if (stage < 1) {
                        mc.thePlayer.cameraYaw = 0.08F;
                    } else if (stage > 4) {
                        mc.thePlayer.cameraYaw = 0.105F;
                    }

                    if (Fan.targetStrafe.canStrafe()) {
                        Fan.targetStrafe.strafe(event, Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed()));
                    } else {
                        event.setSpeed(Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed()));
                    }
                    stage++;
                }
            } else if (e instanceof EventMotion) {
                EventMotion event = (EventMotion) e;

                if (stage < timerTicks.getValue())
                    mc.timer.timerSpeed = (float) timerSpeed.getValue();
                else
                    mc.timer.timerSpeed = 1;

                if (e.isPre()) {
                    if (stage > 1) {
                        if (mc.gameSettings.keyBindSneak.pressed) {
                            mc.thePlayer.motionY = -0.4F;
                            MovementUtil.setSpeed(0);
                        } else {
                            mc.thePlayer.motionY = 0;
                        }

                        if (!MovementUtil.isMoving()) {
                            MovementUtil.setSpeed(0);
                        }
                    }
                    ticks++;

                    switch (this.ticks) {
                        case 1: {
                            this.y *= -0.949999988079071D;
                            break;
                        }
                        case 2:
                        case 3:
                        case 4: {
                            this.y += 1.0e-5;
                            break;
                        }
                        case 5: {
                            this.y += 8e-7;
                            this.ticks = 0;
                            break;
                        }
                    }

                    event.setY(event.getY() + y);
                }

                if (e.isPre()) {
                    double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
                }
            }
            if (e instanceof EventPacket && e.isPre()) {
                EventPacket event = (EventPacket) e;

                /* else { //Redundant now
                    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                        Notification.post(NotificationType.WARNING, "Lagback", "Lagback detected, toggling Fly");
                        toggleSilent();
                    }
                }*/
            }
        }
    }

    private class TP extends Module {
        private final ArrayList<Packet<?>> packets = new ArrayList<>();
        public double positionOffset, speedMult, stageTwoMult, startOffset, jumpModifier, damageOffset, damageY, damageYTwo, yDown;
        private int ticks;
        private Vec3 lastPos;
        private boolean tp;

        public TP() {
            super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
        }

        public void onEnable() {
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

        public void onDisable() {
            PacketUtil.sendPacketNoEvent(new C0CPacketInput(0, 0, true, false));
            PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            for (Packet<?> packet : packets) {
                PacketUtil.sendPacketNoEvent(packet);
            }
            packets.clear();
        }

        public void onEvent(Event e) {
            if (e instanceof EventPacket) {
                EventPacket event = (EventPacket) e;
                if (!tp) {
                    if (event.getPacket() instanceof C03PacketPlayer) {
                        C03PacketPlayer c03 = event.getPacket();
                        event.setCancelled(true);
                        if (c03.isMoving())
                            packets.add(c03);
                    }

                    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                        S08PacketPlayerPosLook pos = event.getPacket();
                        lastPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(pos.getX(), pos.getY() + 0.000032287, pos.getZ(), false));
                        event.setCancelled(true);
                        tp = true;
                    }
                }
            } else if (e instanceof EventMove) {
                EventMove event = (EventMove) e;
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                event.setY(mc.gameSettings.keyBindJump.isKeyDown() ? 0.42F : mc.gameSettings.keyBindSneak.isKeyDown() ? -0.42F : 0);
                if (!tp) {
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

    private class Blinkless extends Module {
        /* private double movementSpeed, random;
        private boolean adjustSpeed, canSpeed, hasDamaged;*/

        public double moveSpeed;
        public double lastDist;
        public int stage;

        public Blinkless() {
            super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
        }

        public void onEnable() {
            stage = 0;
            /*BypassUtil.damage();
            canSpeed = false;
            random = BypassUtil.range(-0.05, 0.05);
            hasDamaged = false;*/
        }

        public void onDisable() {
            /*canSpeed = false;
            hasDamaged = false;*/
        }

        public void onEvent(Event e) {
            if (e instanceof EventMotion) {
                switch (stage) {
                    case 0:
                        mc.thePlayer.motionY = 0.42F;
                        moveSpeed = 0.55 * speed.getValue();

                        if (!mc.thePlayer.onGround) {
                            stage++;
                        }
                        break;

                    case 1:
                        mc.thePlayer.motionY = 0;

                        moveSpeed = moveSpeed - lastDist / 159.9999;

                        MovementUtil.setSpeed(Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed()));

                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            ((EventMotion) e).setY(((EventMotion) e).getY() + 0.0001);
                        } else if (mc.thePlayer.ticksExisted % 3 == 0) {
                            ((EventMotion) e).setY(((EventMotion) e).getY() - 0.0003);
                        }
                        break;
                }
            } else if (e instanceof EventUpdate) {
                double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
            }



           /* disableOnLagback = false;
            if (e instanceof EventMove) {
                if (mc.thePlayer.hurtTime > 0)
                    hasDamaged = true;
                EventMove event = (EventMove) e;
                if (!(mc.thePlayer.fallDistance <= 2f)) {
                    if (hasDamaged) {
                        if (MovementUtil.isMoving() && mc.thePlayer.onGround) {
                            if (speed.getValue() > .2f) {
                                mc.timer.timerSpeed = 0.7f;
                                BypassUtil.damage();
                            }
                            if (speed.getValue() < .1f)
                                event.setY(mc.thePlayer.motionY = MovementUtil.getJumpHeight(0.2f));
                            if (speed.getValue() > .1f) {
                                event.setY(mc.thePlayer.motionY = MovementUtil.getJumpHeight(0.42f));
                                movementSpeed = MovementUtil.getBaseMoveSpeed() * 2.15f;
                                adjustSpeed = true;
                                canSpeed = true;
                            } else {
                                event.setY(mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 2 == 0 ? -0.0006F : 0.0006F);
                            }
                        } else {
                            event.setY(mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 2 == 0 ? -0.0006F : 0.0006F);

                            if (mc.thePlayer.movementInput.jump && mc.thePlayer.ticksExisted % 8 == 0 && !mc.thePlayer.isSneaking())
                                event.setY(mc.thePlayer.motionY = 0.42F);
                            // ill turn this into 1 line later
                            if (mc.thePlayer.isSneaking() && mc.thePlayer.ticksExisted % 8 == 0)
                                event.setY(mc.thePlayer.motionY = -0.42F);

                            if (adjustSpeed) {
                                movementSpeed = 1f + ((speed.getValue() * 10) + random / 9.59f);
                                mc.timer.timerSpeed = (float) timerSpeed.getValue();
                                adjustSpeed = false;
                            } else {
                                if (speed.getValue() < .2f)
                                    movementSpeed -= movementSpeed / 19f;
                                else
                                    movementSpeed -= movementSpeed / 159f;
                            }

                            mc.timer.timerSpeed = Math.max(1.0F, mc.timer.timerSpeed - mc.timer.timerSpeed / 59f);

                            if (mc.timer.timerSpeed < 1.1f)
                                canSpeed = true;
                        }
                        MovementUtil.setSpeed(event, Math.max(MovementUtil.getBaseMoveSpeed(), movementSpeed));
                    }
                } else {
                    MovementUtil.setSpeed(event, (speed.getValue() * 10));
                    float newSpeed = (float) ((speed.getValue() * 10) * 0.425f);
                    event.setY(mc.gameSettings.keyBindJump.isKeyDown() ? newSpeed : mc.gameSettings.keyBindSneak.isKeyDown() ? -newSpeed : antiKick1 ? -0.1535f : 0);
                    mc.thePlayer.motionY = 0;
                }
            }*/
        }
    }
}
