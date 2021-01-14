package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Arithmo on 5/1/2017 at 11:47 PM.
 */
public class AntiFreeze extends Module {

    public AntiFreeze(ModuleData data) {
        super(data);
    }

    private List<Packet> packets = new CopyOnWriteArrayList<>();
    private boolean sending;
    private int delay;
    private int setBacks;
    private int ticksSinceLastSetBack;
    private boolean unstuck;

    @Override
    @RegisterEvent(events = {EventTick.class, EventPacket.class})
    public void onEvent(Event event) {
        //S08PacketPosLook
        if (event instanceof EventTick) {
            delay += 1;
            if (delay >= 1) {
                sending = true;
                sendPackets();
                delay = 0;
            }
        } else if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (sending) {
                return;
            }
            if (ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer || ep.getPacket() instanceof C08PacketPlayerBlockPlacement || ep.getPacket() instanceof C07PacketPlayerDigging || ep.getPacket() instanceof C02PacketUseEntity) {
                event.setCancelled(true);
            }
            boolean input = (mc.gameSettings.keyBindForward.isPressed()) || (mc.gameSettings.keyBindBack.isPressed()) || (mc.gameSettings.keyBindRight.isPressed()) || (mc.gameSettings.keyBindLeft.isPressed());
            if ((input) && ((ep.getPacket() instanceof C03PacketPlayer))) {
                packets.add(ep.getPacket());
            }
            if ((ep.getPacket() instanceof C02PacketUseEntity)) {
                packets.add(ep.getPacket());
                mc.thePlayer.rotationYaw -= 180.0F;
            }
        }
    }

    public void sendPackets() {
        if (packets.size() > 0) {
            for (Packet packet : packets) {
                if (((packet instanceof C02PacketUseEntity)) || ((packet instanceof C08PacketPlayerBlockPlacement)) || ((packet instanceof C07PacketPlayerDigging))) {
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(packet);
            }
        }
        packets.clear();
        sending = false;
    }

    public void onDisable() {
        super.onDisable();
        sending = true;
        sendPackets();
    }

}
