package store.shadowclient.client.management.command.commands;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventReceivePacket;
import store.shadowclient.client.management.command.Command;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

public class PluginFinder extends Command {

	private boolean listen = false;
	private TimeHelper time = new TimeHelper();
	private String[] localObject;
	
	public PluginFinder(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "plugins";
	}

	@Override
	public String getDescription() {
		return "Shows you the server plugins";
	}

	@Override
	public String getSyntax() {
		return ".plugins";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		Shadow.instance.addChatMessage("Finding plugins...");
	    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
	     this.listen = true;
	     this.time.reset();
		return line;
	}
	
	@EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof S3APacketTabComplete) {
            final S3APacketTabComplete packet = (S3APacketTabComplete)event.getPacket();
            final String[] commands = packet.func_149630_c();
            String message = "";
            int size = 0;
            String[] array;
            for (int length = (array = commands).length, i = 0; i < length; ++i) {
                final String command = array[i];
                final String pluginName = command.split(":")[0].substring(1);
                if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
                    ++size;
                    if (message.isEmpty()) {
                        message = String.valueOf(message) + pluginName;
                    }
                    else {
                        message = String.valueOf(message) + "§c, §f" + pluginName;
                    }
                }
            }
            if (!message.isEmpty()) {
            	Shadow.addChatMessage("Plugins Found §8(§c" + size + "§8)§f: " + message + "§c.");
            }
            else {
            	Shadow.addChatMessage("Plugins: none.");
            }
            event.setCancelled(true);
            EventManager.unregister((Object)this);
        }
        if (this.time.delay(20000.0)) {
            EventManager.unregister((Object)this);
            Shadow.addChatMessage("Server took too long to respond! (20s)");
        }
    }
}
