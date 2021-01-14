package info.sigmaclient.module.impl.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Phase;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;


public class Disabler extends Module {
    String MODE = "MODE";
    boolean faithful = false, watchdog = false, erisium = false;
    Timer timer = new Timer();
    int lastKey;
    String mode;

    public Disabler(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Hypixel", new String[]{"Hypixel", "Faithful", "Erisium", "SagePvP", "ColdNetwork"}), "Disabler method."));
    }

    @Override
    public void onEnable() {
        if (mc.theWorld == null)
            return;
        erisium = false;
        timer.reset();
        mode = ((Options) settings.get(MODE).getValue()).getSelected();
        if (mode.equalsIgnoreCase("ViperMC")) {
            if (!MoveUtils.isOnGround(0.0001)) {
                this.toggle();
                Notifications.getManager().post("Disabler", "Try again on ground !", Notifications.Type.WARNING);
            }
        }
        if (mode.equalsIgnoreCase("Hypixel")) {
            if (MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.16, z, true));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.07, z, true));
                watchdog = true;
                Notifications.getManager().post("Disabler", "Wait 5s.", Notifications.Type.INFO);
            } else {
                watchdog = false;
            }
        } else if (mode.equalsIgnoreCase("Faithful")) {
            Notifications.getManager().post("Disabler", "Relog to disable Area 51.", Notifications.Type.INFO);
        }
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        if (mode == null) {
            mode = ((Options) settings.get(MODE).getValue()).getSelected();
        }
        if (!mode.equalsIgnoreCase(((Options) settings.get(MODE).getValue()).getSelected())) {

            mode = ((Options) settings.get(MODE).getValue()).getSelected();
            if (mode.equalsIgnoreCase("Faithful")) {
                Notifications.getManager().post("Disabler", "Relog to disable Area 51.", Notifications.Type.INFO);
            }
        }
        this.setSuffix(mode);
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                if (mc.thePlayer.ticksExisted <= 1) {
                    erisium = false;
                }
                if (mode.equalsIgnoreCase("ViperMC")) {
                    if (timer.delay(3000)) {
                        em.setY(mc.thePlayer.posY + 0.4);
                        return;
                    }
                    for (int i = 0; i < 10; ++i) {
                        boolean i2 = i > 2 && i < 8;
                        double x = i2 ? 0.2 : -0.2D;
                        C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX, mc.thePlayer.posY + x, mc.thePlayer.posZ, true);
                        mc.thePlayer.sendQueue.addToSendQueue(packet);
                    }
                }
                if (mode.equalsIgnoreCase("Hypixel"))
                    if (!watchdog) {
                        if (MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                            double x = mc.thePlayer.posX;
                            double y = mc.thePlayer.posY;
                            double z = mc.thePlayer.posZ;
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.16, z, true));
                            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.07, z, true));
                            watchdog = true;
                            Notifications.getManager().post("Disabler", "Please wait 5s.", Notifications.Type.INFO);
                        }
                    } else {
                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.motionZ = 0;
                        mc.thePlayer.jumpMovementFactor = 0;
                        mc.thePlayer.noClip = true;
                        mc.thePlayer.onGround = false;
                    }
                if (mode.equalsIgnoreCase("Erisium")) {
                    if (erisium && timer.delay(1500)) {
                        erisium = !erisium;
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C00PacketKeepAlive(lastKey));
                    }
                }

            }
        } else if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet p = ep.getPacket();
            if (ep.isPre()) {
                if (p instanceof C03PacketPlayer) {
                    if (mode.equalsIgnoreCase("Hypixel") && watchdog) {
                        ep.setCancelled(true);
                    }
                }
                if (p instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
                    pac.yaw = mc.thePlayer.rotationYaw;
                    pac.pitch = mc.thePlayer.rotationPitch;
                    if (watchdog && mode.equalsIgnoreCase("Hypixel")) {
                        Notifications.getManager().post("Disabler", "You can do what you want for 5s !", Notifications.Type.INFO);
                        this.toggle();
                    }
                }
                if (p instanceof S00PacketKeepAlive) {
                    S00PacketKeepAlive packet = (S00PacketKeepAlive) p;
                    int KEY = packet.func_149134_c();
                    if (mode.equalsIgnoreCase("Erisium")) {
                        timer.reset();
                        erisium = true;
                        lastKey = KEY;
                    }
                }
                if (p instanceof C00PacketKeepAlive) {
                    C00PacketKeepAlive packet = (C00PacketKeepAlive) p;
                    if (mode.equalsIgnoreCase("Faithful") || mode.equalsIgnoreCase("Erisium") || mode.equalsIgnoreCase("SagePvP") || mode.equalsIgnoreCase("ColdNetwork")) {
                        ep.setCancelled(true);
                    }
                }
                if (p instanceof C0FPacketConfirmTransaction) {
                    C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) p;
                    int id = packet.getId();
                    boolean accepted = packet.isAccepted();
                    int uid = packet.getUid();
                    if (id == 0 && accepted)
                        if (mode.equalsIgnoreCase("SagePvP") || mode.equalsIgnoreCase("ColdNetwork")) {
                            ep.setCancelled(true);
                        }

                }
            }
        }
    }
}
