package cedo.modules.hidden;

import cedo.Fan;
import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.modules.movement.Fly;
import cedo.util.BypassUtil;
import cedo.util.Logger;
import cedo.util.PacketUtil;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Disabler extends Module {

    public static boolean bypasses = true;
    public boolean wasEnabled;
    private int randomPacket = Math.round(BypassUtil.range(-2, 2));
    private int ticksExisted;
    public double offset, packetCount;

    List<Packet<?>> packets = new ArrayList<Packet<?>>();

    public Disabler() {
        super("Disabler", Keyboard.KEY_NONE, Category.EXPLOIT);

        setDisablerValues();
    }

    public void setDisablerValues() {
        if (Wrapper.authorized) {
            offset = 1.67346739E-7;
            packetCount = 30;
        }
    }

    public void toggle() {
    } //Overrides toggle method to render untoggleable

    public void setToggled(boolean toggled) {
    } //Overrides toggle method to render untoggleable

    public void onEvent(Event e) {
        cedo.modules.movement.Fly fly = Fan.fly;

        switch (fly.mode.getSelected()) {
            case "Blink" :
                if (e instanceof EventMotion && e.isPre()) {
                    EventMotion event = (EventMotion) e;
                    if (Fan.getModule(Fly.class).isEnabled()) { //&& mc.thePlayer.ticksExisted % 1 == 0) {
                        event.setY(event.getY() + offset);
                    }
                }
                if (e instanceof EventPacket && e.isPre() && e.isOutgoing()) {
                    if ((((EventPacket) e).getPacket() instanceof C03PacketPlayer) && (Fan.shouldDisable()/* || mc.thePlayer.isSpectator()*/)) {
                        wasEnabled = true;
                        packets.add(((EventPacket) e).getPacket());
                        e.setCancelled(true);

                        packetCount = Fan.fly.blinkPackets.getValue();

                        if (packets.size() >= packetCount) {
                            for (Packet<?> p : packets)
                                 PacketUtil.sendPacketNoEvent(p);
                             PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities());
                            //Logger.ingameInfo(String.format("send %s packets", packets.size()));
                            packets.clear();
                        }
                        if (mc.thePlayer.ticksExisted % 30 == 0) {
                            if (Fan.shouldDisable() || mc.thePlayer.isSpectator()) {
                                PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                                playerCapabilities.isFlying = true;
                                playerCapabilities.allowFlying = true;
                                playerCapabilities.setFlySpeed((float) BypassUtil.range(8.85343425, 9.85343425));
                                playerCapabilities.setPlayerWalkSpeed((float) BypassUtil.range(9, 9.8));
                                 PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                            }
                        }
                    } else if (wasEnabled && !Fan.shouldDisable()) {
                        wasEnabled = false;

                        for (Packet<?> p : packets)
                             PacketUtil.sendPacketNoEvent(p);

                        //Logger.ingameInfo(String.format("send %s packets", packets.size()));
                        packets.clear();
                    }
                }
                if (e instanceof EventPacket) { //Note that this runs regardless of Fan.shouldDisable(). This is intentional.
                    EventPacket event = (EventPacket) e;
                    if (e.isPre() && e.isOutgoing()) {
                        /*if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                            C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
                             PacketUtil.sendPacketNoEvent(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, packetConfirmTransaction.getUid(), false));
                            e.setCancelled(true);
                        }*/
                        if (event.getPacket() instanceof C00PacketKeepAlive) {
                             PacketUtil.sendPacketNoEvent(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
                            e.setCancelled(true);
                        }
                    } else if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                        S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
                        if (packet.getActionNumber() < 0) {
                            event.setCancelled(true);
                        }
                    }
                }
                    break;
                case "Blinkless2" :
                    if (e instanceof EventMotion && e.isPre()) {
                        EventMotion event = (EventMotion) e;
                        if (Fan.getModule(Fly.class).isEnabled()) { //&& mc.thePlayer.ticksExisted % 1 == 0) {
                            event.setY(event.getY() + offset);
                        }
                    }
                    if (e instanceof EventPacket && e.isPre() && e.isOutgoing()) {
                        if ((((EventPacket) e).getPacket() instanceof C03PacketPlayer) && (Fan.shouldDisable()/* || mc.thePlayer.isSpectator()*/)) {
                            wasEnabled = true;
                            //packets.add(((EventPacket) e).getPacket());
                            //e.setCancelled(true);

                            packetCount = Fan.fly.blinkPackets.getValue();

                            if (packets.size() >= packetCount) {
                                for (Packet<?> p : packets)
                                    PacketUtil.sendPacketNoEvent(p);
                                PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities());
                                //Logger.ingameInfo(String.format("send %s packets", packets.size()));
                                packets.clear();
                            }
                            if (mc.thePlayer.ticksExisted % 30 == 0) {
                                if (Fan.shouldDisable() || mc.thePlayer.isSpectator()) {
                                    PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                                    playerCapabilities.isFlying = true;
                                    playerCapabilities.allowFlying = true;
                                    playerCapabilities.setFlySpeed((float) BypassUtil.range(8.85343425, 9.85343425));
                                    playerCapabilities.setPlayerWalkSpeed((float) BypassUtil.range(9, 9.8));
                                    PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                                }
                            }
                        } else if (wasEnabled && !Fan.shouldDisable()) {
                            wasEnabled = false;

                            for (Packet<?> p : packets)
                                PacketUtil.sendPacketNoEvent(p);

                            //Logger.ingameInfo(String.format("send %s packets", packets.size()));
                            packets.clear();
                        }
                    }
                    if (e instanceof EventPacket) { //Note that this runs regardless of Fan.shouldDisable(). This is intentional.
                        EventPacket event = (EventPacket) e;
                        if (e.isPre() && e.isOutgoing()) {
                        /*if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                            C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
                             PacketUtil.sendPacketNoEvent(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, packetConfirmTransaction.getUid(), false));
                            e.setCancelled(true);
                        }*/
                            if (event.getPacket() instanceof C00PacketKeepAlive) {
                                PacketUtil.sendPacketNoEvent(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
                                e.setCancelled(true);
                            }
                        } else if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                            S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
                            if (packet.getActionNumber() < 0) {
                                event.setCancelled(true);
                            }
                        }
                    }
                break;
            case "Blinkless" :
                if (e instanceof EventPacket) { //Note that this runs regardless of Fan.shouldDisable(). This is intentional.
                    EventPacket event = (EventPacket) e;
                    if (e.isPre() && e.isOutgoing()) {
                        if (event.getPacket() instanceof C00PacketKeepAlive) {
                             PacketUtil.sendPacketNoEvent(new C00PacketKeepAlive(Integer.MIN_VALUE + new Random().nextInt(100)));
                            e.setCancelled(true);
                        }
                    } else {
                        if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                            S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
                            if (packet.getActionNumber() < 0) {
                                event.setCancelled(true);
                            }
                        }
                    }
                    if (event.getPacket() instanceof S39PacketPlayerAbilities && (Fan.shouldDisable() || mc.thePlayer.isSpectator())) {
                        /*randomPacket = Math.round(BypassUtil.range(-2, 2));
                        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                        playerCapabilities.isCreativeMode = true;
                        playerCapabilities.setFlySpeed((float) BypassUtil.range(1f, 9f));
                        playerCapabilities.isFlying = true;
                        playerCapabilities.allowFlying = true;
                        PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                        ticksExisted = mc.thePlayer.ticksExisted;
                        Logger.ingameInfo("c13 sent 2");*/
                        e.setCancelled(true);
                    }
                }
                /*if (e instanceof EventMotion && e.isPre()) {
                    if (mc.thePlayer.ticksExisted - ticksExisted > 25 && (Fan.shouldDisable() || mc.thePlayer.isSpectator())) {
                        randomPacket = Math.round(BypassUtil.range(-2, 2));
                        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                        playerCapabilities.isCreativeMode = true;
                        playerCapabilities.setFlySpeed((float) BypassUtil.range(9f, 15f));
                        playerCapabilities.isFlying = true;
                        playerCapabilities.allowFlying = true;
                        PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                        Logger.ingameInfo("c13 sent 1");
                        ticksExisted = mc.thePlayer.ticksExisted;
                    }
                }*/
                break;
        }
    }
}
