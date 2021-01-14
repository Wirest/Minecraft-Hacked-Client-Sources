package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class FakeLag extends Module {
    TimeHelper timer = new TimeHelper();

    public FakeLag() {
        super("FakeLag", Keyboard.KEY_NONE, Category.RENDER);
    }

    List<Packet> packets = new ArrayList<>();

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mc.theWorld == null)
            return;
        if (timer.hasReached(1000) && (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging)) {
            packets.add(event.getPacket());
            event.setCancelled(true);
            if (timer.hasReached(1500)) {
                for (Packet packet : packets) {
                    mc.getNetHandler().addToSendQueueSilent(packet);
                }
                packets.clear();
                timer.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
        ;
    }
}
