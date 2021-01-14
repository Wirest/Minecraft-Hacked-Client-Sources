
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.PacketOutEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.misc.Timer;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.function.Consumer;

public class Criticals extends Module {

    private Timer critTimer = new Timer();
    private double groundSpoofDist = 0.001;

    public Criticals() {
        super("Criticals", 0, Module.Category.COMBAT);
        addModes("Ground Spoof", "Packet", "Mineplex");
    }

    @Handler
    public Consumer<PacketOutEvent> update = e -> {
        if (mc.thePlayer == null) {
            return;
        }
        if (isMode("Mineplex")) {
            if (e.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
                if (mc.thePlayer.isCollidedVertically && mc.thePlayer.ticksExisted % 2 == 0) {
                    packet.onGround = false;
                    if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                        PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(packet.x, packet.y + 1.02E-9, packet.z, false));
                    }
                }
            }
        }
        if (isMode("Cubecraft")) {
            if (e.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity) e.getPacket();
                if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (critTimer.delay(320)) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX, mc.thePlayer.posY + 0.1232225, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX, mc.thePlayer.posY + 1.0554E-9, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        critTimer.reset();
                    }
                }
            }
        }
    };
    
    @Handler
    public Consumer<UpdateEvent> eventConsumer = event -> {
        if (groundSpoofDist < 0.0001) {
            groundSpoofDist = 0.001;
        }
        if (isMode("GroundSpoof") && mc.thePlayer.isSwingInProgress && mc.thePlayer.isCollidedVertically) {
            event.setY(event.getY() + groundSpoofDist);
            event.setGround(false);
            groundSpoofDist -= 1.0E-11;
        }
    };

    @Override
    public void onEnable() {
        groundSpoofDist = 0.001;
    }

    @Override
    public void onDisable() {
        groundSpoofDist = 0.001;
    }
}
