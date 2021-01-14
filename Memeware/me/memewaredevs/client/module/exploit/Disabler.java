package me.memewaredevs.client.module.exploit;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.PacketInEvent;
import me.memewaredevs.client.event.events.PacketOutEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.ChatUtil;
import me.memewaredevs.client.util.misc.Timer;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;
import org.apache.commons.lang3.RandomUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class Disabler extends Module {

    private static final int VERUS_DISABLE_AUTOBAN_CHANNEL = 65536;
    private static final short VERUS_DISABLE_AUTOBAN_UID = 32767;
    private final Queue<Packet> packetQueue = new ConcurrentLinkedQueue();
    private final Timer timer = new Timer();
    private final Timer timer1 = new Timer();

    public Disabler(final String name, final int key, final Module.Category category) {
        super(name, key, category);
        this.addModes("Mineplex Combat", "Transaction", "Latest Verus", "PvPTemple", "OmegaCraft", "Timer", "AACv1.9.10", "Sloth", "Ghostly", "Watchdog", "Faithful Movement");
    }

    @Override
    public void onDisable() {
        packetQueue.clear();
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Handler
    public Consumer<PacketInEvent> packetInEventConsumer = (event) -> {
        if (isMode("Sloth")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();
                /**
                 * Works alongside with the spigot exploit.
                 * Exploit PlayerData bug (Player data gets set to null if the previous event is cancelled, and y difference <= 1.0E-4)
                 */
                playerPosLook.y += 1.0E-4;
            }
        }
    };

    @Handler
    public Consumer<UpdateEvent> eventConsumer = (event) -> {
        if (isMode("Latest Verus")) {
            if (timer.delay(490L)) {
                if (!packetQueue.isEmpty()) {
                    PacketUtil.sendPacketSilent(packetQueue.poll());
                }
                timer.reset();
            }
        }

        if (isMode("OmegaCraft")) {
            PacketUtil.sendPacketSilent(new C0FPacketConfirmTransaction(VERUS_DISABLE_AUTOBAN_CHANNEL, VERUS_DISABLE_AUTOBAN_UID, true));
        }

        if (isMode("PvPTemple")) { // Added by Auth
            event.setGround(false);
            event.setY(event.getY() + 0.42);
            event.setYaw(0);
            event.setPitch(0);
        }

        if (isMode("Watchdog")) {
            if (timer.delay(15000L)) {
                if (!packetQueue.isEmpty()) {
                    PacketUtil.sendPacketSilent(packetQueue.poll());
                }
                timer.reset();
            }
        }

    };

    @Handler
    public Consumer<PacketInEvent> eventConsumer0 = (event) -> {
        if (event.getPacket() instanceof S2APacketParticles) {
            event.cancel();
        }
    };

    @Handler
    public Consumer<PacketOutEvent> eventConsumer1 = (event) -> {

        if (isMode("Mineplex Combat"))
            if (event.getPacket() instanceof C00PacketKeepAlive) {
                C00PacketKeepAlive packetKeepAlive = (C00PacketKeepAlive) event.getPacket();
                packetKeepAlive.key -= RandomUtils.nextInt(3, 128);
            }

        if (isMode("OmegaCraft"))
            if (event.getPacket() instanceof C0FPacketConfirmTransaction)
                event.cancel();

        if (isMode("Latest Verus")) {
            if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                C0FPacketConfirmTransaction c0fPacketConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
                packetQueue.add(c0fPacketConfirmTransaction);
                event.cancel();
            }

            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
                if (mc.thePlayer.ticksExisted % 33 == 0) {
                    c03.y = -0.911;
                    c03.onGround = false;
                    c03.field_149480_h = false;
                }
            }

            if (mc.thePlayer != null && mc.thePlayer.ticksExisted <= 7) {
                timer.reset();
                packetQueue.clear();
            }
        }

        if (isMode("Timer") && event.getPacket() instanceof C03PacketPlayer) {
            if (mc.thePlayer.ticksExisted % 3 != 0) {
                event.cancel();
            }
        }
        if (isMode("AACv1.9.10")) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                PacketUtil.sendPacketSilent(new C0CPacketInput());
                C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                /**
                 * Resets all checks VL due to a bug in critical hit check
                 */
                packetPlayer.y += 7.0E-9D;
            }
        }

        if (isMode("Sloth")) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                if (!timer.delay(1000L)) {
                    for (int i = 0; i < 10; ++i) {
                        double y = i > 2 && i < 8 ? 11 : 120;
                        PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - y, mc.thePlayer.posZ, true));
                    }
                } else {
                    //set to default jump value so viper can stop fucking value patching it
                    packetPlayer.y += 0.42F;
                }
            }
        }
        if (isMode("Faithful Movement")) {
            if (mc.thePlayer != null && event.getPacket() instanceof C00PacketKeepAlive) {
                C00PacketKeepAlive packetKeepAlive = (C00PacketKeepAlive) event.getPacket();
                packetKeepAlive.key -= 31;
            }
        }


        if (isMode("Ghostly")) { // Ghostly Disabler added by Auth
            if (event.getPacket() instanceof C03PacketPlayer)
                PacketUtil.sendPacketSilent(new C0CPacketInput());
            if (event.getPacket() instanceof C0FPacketConfirmTransaction)
                event.cancel();
        }
        if (isMode("Watchdog")) {
//            PacketUtil.sendPacketSilent(new C0EPacketClickWindow());
            if (mc.getCurrentServerData().serverIP.contains("hypixel.net")) {
                if (mc.thePlayer.ticksExisted % 30 == 0) {
                    PlayerCapabilities pc = new PlayerCapabilities();
                    pc.isFlying = true;
                    pc.setFlySpeed(Float.NaN);
                    PacketUtil.sendPacketSilent(new C13PacketPlayerAbilities(pc));
                }
                System.out.println(event.getPacket());
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    event.setPacket(new C0FPacketConfirmTransaction(Integer.MIN_VALUE, Short.MAX_VALUE, true));
                }
            } else {
                ChatUtil.printChat("You must be on Hypixel to use Watchdog Disabler!");
                this.toggle();
            }
        }
    };
}
