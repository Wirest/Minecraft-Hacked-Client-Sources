/**
 * Time: 5:26:04 PM
 * Date: Jan 10, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.MathUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author cool1
 */
public class PingSpoof extends Module {

    private info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();
    private List<Packet> packetList = new CopyOnWriteArrayList<>();
    private String WAIT = "WAIT";

    public PingSpoof(ModuleData data) {
        super(data);
        settings.put(WAIT, new Setting<>(WAIT, 15, "Seconds to wait before sending packets again.", 1, 5, 30));
    }


    @Override
    @RegisterEvent(events = {EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C00PacketKeepAlive && mc.thePlayer.isEntityAlive()) {
                packetList.add(ep.getPacket());
                event.setCancelled(true);
            }
            if (timer.delay(1000 * ((Number) settings.get(WAIT).getValue()).intValue())) {
                if (!packetList.isEmpty()) {
                    int i = 0;
                    double totalPackets = MathUtils.getIncremental(Math.random() * 10, 1);
                    for (Packet packet : packetList) {
                        if (i < totalPackets) {
                            i++;
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
                            packetList.remove(packet);
                        }
                    }
                }
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C00PacketKeepAlive(10000));
                timer.reset();
            }
        }
    }
}

