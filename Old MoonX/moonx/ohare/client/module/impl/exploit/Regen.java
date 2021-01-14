package moonx.ohare.client.module.impl.exploit;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Regen extends Module {
    private boolean teleport, teleported, sendback, niggay;
    private double prevx, prevy, prevz;
    private float preyaw, prepitch;
    private int moveUnder;
    private TimerUtil timerUtil = new TimerUtil();
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.FAITHFUL);
    private NumberValue<Float> speed = new NumberValue<>("Speed", 25f, 1f, 100f, 1f);

    public Regen() {
        super("Regen", Category.EXPLOITS, new Color(0x4DAE99).getRGB());
        setDescription("Regenerate health.");
    }

    @Override
    public void onEnable() {
        teleport = teleported = sendback = false;
        prevx = prevy = prevz = 0;
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        switch (mode.getValue()) {
            case FAITHFUL:
                if (getMc().thePlayer.getHealth() <= 6 && !teleport) {
                    prevx = getMc().thePlayer.posX;
                    prevy = getMc().thePlayer.posY;
                    prevz = getMc().thePlayer.posZ;
                    preyaw = getMc().thePlayer.rotationYaw;
                    prepitch = getMc().thePlayer.rotationPitch;
                    teleport = true;
                }
                if (teleport && !teleported) {
                    getMc().thePlayer.getEntityBoundingBox().offsetAndUpdate(0, -999, 0);
                    Printer.print("Healed!");
                    teleported = true;
                }
                if ((teleport && getMc().thePlayer.getHealth() > 6 && !sendback) || !getMc().thePlayer.isEntityAlive()) {
                    teleport = false;
                    teleported = false;
                    if (getMc().thePlayer.isEntityAlive()) sendback = true;
                    timerUtil.reset();
                }
                break;
            case NORMAL:
                if (getMc().thePlayer.getHealth() < getMc().thePlayer.getMaxHealth() && getMc().thePlayer.onGround) {
                    for (int i = 0; i < speed.getValue(); ++i) {
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, getMc().thePlayer.onGround));
                    }
                }
                break;
            case EXPERIMENTAL:
                if (getMc().thePlayer.getHealth() < getMc().thePlayer.getMaxHealth() - 3 && getMc().thePlayer.getFoodStats().getFoodLevel() > 17 && getMc().thePlayer.onGround) {
                    for (int i = 0; i < speed.getValue(); ++i) {
                       // getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0CPacketInput(getMc().thePlayer.moveStrafing, getMc().thePlayer.moveForward, getMc().thePlayer.movementInput.jump, getMc().thePlayer.movementInput.sneak));
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, getMc().thePlayer.onGround));
                    }
                }
                break;
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case FAITHFUL:
                if (sendback && event.isSending()) {
                    if (event.getPacket() instanceof C03PacketPlayer) {
                        if (!getMc().thePlayer.isMoving() && getMc().thePlayer.posY == getMc().thePlayer.lastTickPosY) {
                            event.setCanceled(true);
                        }
                        C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                        getMc().thePlayer.motionY = getMc().thePlayer.motionZ = getMc().thePlayer.motionX = 0;
                        if (event.getPacket() instanceof C03PacketPlayer) {
                            if (!niggay) {
                                packet.setX(prevx);
                                packet.setY(prevy);
                                packet.setZ(prevz);
                                getMc().thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(prevx, prevy, prevz, true));
                                getMc().thePlayer.setPosition(prevx, prevy, prevz);
                                niggay = true;
                                moveUnder = 2;
                            }
                        }
                        if (timerUtil.reach(500)) {
                            sendback = false;
                            niggay = false;
                            prevx = prevy = prevz = 0;
                            getMc().thePlayer.rotationYaw = preyaw;
                            getMc().thePlayer.rotationPitch = prepitch;
                            getMc().renderGlobal.loadRenderers();
                            timerUtil.reset();
                        }
                    }
                } else {
                    if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                        moveUnder = 1;
                    }
                    if (event.getPacket() instanceof S02PacketChat) {
                        S02PacketChat packet = (S02PacketChat) event.getPacket();
                        if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
                            event.setCanceled(true);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Handler
    public void onTick(TickEvent event) {
        if (mode.getValue() == Mode.FAITHFUL) {
            if (getMc().thePlayer != null && moveUnder == 1 && sendback) {
                if (getMc().thePlayer.getDistanceSq(prevx, prevy, prevz) > 1) {
                    getMc().thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(prevx, prevy, prevz, false));
                    getMc().thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, prevy, Double.NEGATIVE_INFINITY, true));
                    moveUnder = 0;
                }
            }
        }
    }

    public enum Mode {
        NORMAL, EXPERIMENTAL, FAITHFUL
    }
    private boolean CanHeal() {
        return getMc().thePlayer.getHealth() < 20 && getMc().thePlayer.getFoodStats().getFoodLevel() >= 19 && getMc().thePlayer.onGround;
    }
}
