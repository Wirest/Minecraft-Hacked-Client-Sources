package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.event.events.game.TickEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.utils.Printer;
import com.etb.client.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.apache.commons.lang3.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * made by oHare for oHareWare
 *
 * @since 6/11/2019
 **/
public class TeleportPacket extends Command {
    private int x, z;
    private boolean gotonigga, niggay, fags;
    private final Minecraft mc = Minecraft.getMinecraft();
    private int moveUnder;
    private TimerUtil timerUtil = new TimerUtil();
    public TeleportPacket() {
        super("TeleportPacket", new String[]{"tpp", "telep", "teleportpacket"}, "Teleport with packets.");
    }

    @Override
    public void onRun(final String[] args) {
        if (!gotonigga) {
            if (args.length > 1 && NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2])) {
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
                gotonigga = true;
                Printer.print("Teleporting to x:" + x + " z:" + z + ".");
                EventBus.getDefault().register(this);
                timerUtil.reset();
            } else {
                Printer.print("Invalid arguments.");
            }
        } else {
            Printer.print("Already active!");
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.thePlayer != null && moveUnder == 1) {
            if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) > 1) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 255, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                moveUnder = 0;
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                if (!mc.thePlayer.isMoving() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
                    event.setCanceled(true);
                }
                C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                if (gotonigga) {
                    mc.thePlayer.motionY = mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;
                        if (!niggay) {
                            packet.setY(255);
                            packet.setX(Double.POSITIVE_INFINITY);
                            packet.setZ(Double.POSITIVE_INFINITY);
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                            moveUnder = 2;
                            niggay = true;
                        }
                        if (niggay && !fags) {
                            if (timerUtil.reach(400)) {
                                packet.setX(x);
                                packet.setZ(z);
                                mc.thePlayer.setPosition(x, mc.thePlayer.posY, z);
                                fags = true;
                                timerUtil.reset();
                            }
                        } else {
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, 255, z, false));
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                            Printer.print("Finished you have arrived at x:" + x + " z:" + z);
                            gotonigga = false;
                            niggay = false;
                            fags = false;
                            mc.renderGlobal.loadRenderers();
                            EventBus.getDefault().unregister(this);
                        }
                    }
                }
        } else {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                if (packet.getChatComponent().getFormattedText().contains("You cannot go past the border.")) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
