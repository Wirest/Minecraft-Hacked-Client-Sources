package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.util.Timer;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.NetUtil;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

/**
 * Created by Arithmo on 5/7/2017 at 3:12 PM.
 */
public class PluginFinder extends Command {

    public PluginFinder(String[] names, String description) {
        super(names, description);
    }

    private Timer timer = new Timer();

    @Override
    public void fire(String[] args) {
        try {
            timer.reset();
            EventSystem.register(this);
            NetUtil.sendPacket(new C14PacketTabComplete("/"));
            ChatUtil.printChat(chatPrefix + "Listening for a S3APacketTabComplete for 20s!");
        } catch (Exception ceankoFucksOff) {
            ceankoFucksOff.printStackTrace();
        }
    }

    @RegisterEvent(events = {EventPacket.class})
    public void onEvent(Event event) {
        try {
            EventPacket ep = (EventPacket) event;
            if (ep.getPacket() instanceof S3APacketTabComplete) {
                S3APacketTabComplete packet = (S3APacketTabComplete) ep.getPacket();
                String[] commands = packet.func_149630_c();
                String message = "";
                int size = 0;

                for (String command : commands) {
                    String pluginName = command.split(":")[0].substring(1);

                    if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
                        size++;
                        if (message.isEmpty()) {
                            message += pluginName;
                        } else {
                            message += "\2478, \247a" + pluginName;
                        }
                    }
                }

                if (!message.isEmpty()) {
                    ChatUtil.printChat(chatPrefix + "\2477Plugins (\247f" + size + "\2477): \247a " + message + "\2477.");
                } else {
                    ChatUtil.printChat(chatPrefix + "Plugins: none.");
                }
                EventSystem.unregister(this);
                event.setCancelled(true);
            }

            if (timer.delay(20000)) {
                EventSystem.unregister(this);
                ChatUtil.printChat(chatPrefix + "Stopped listening for an S3APacketTabComplete! Took to long (20s)!");
            }
        } catch (Exception ceankoFucksOff) {
            ceankoFucksOff.printStackTrace();
        }
    }

    @Override
    public String getUsage() {
        return "pluginfinder";
    }

}
