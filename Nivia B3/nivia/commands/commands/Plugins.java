package nivia.commands.commands;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketReceive;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class Plugins extends Command {
    public Plugins() {
        super("Plugins", "Lists the server's plugins.", null, false, "pl", "pluginfinder", "plugindiscovery", "plugin", "serverplugins", "pd");
    }
    private Timer timer = new Timer();

    @Override
    public void execute(String message, String[] arguments) {
        this.timer.reset();
        Pandora.getEventManager().register(this);
        this.mc.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
        Logger.logChat("Discovering plugins. Please wait...");
    }
    @EventTarget
    public void onReceive(EventPacketReceive packet){
        if(packet.getPacket() instanceof S3APacketTabComplete) {
            S3APacketTabComplete packetTabComplete = (S3APacketTabComplete) packet.getPacket();
            String[] commands = packetTabComplete.func_149630_c();
            String message = "";
            int size = 0;
            String[] var7 = commands;
            int var8 = commands.length;
            for (int var9 = 0; var9 < var8; ++var9) {
                String command = var7[var9];
                String pluginName = command.split(":")[0].substring(1);
                if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
                    ++size;
                    if (message.isEmpty()) {
                        message = message + pluginName;
                    } else {
                        message = message + EnumChatFormatting.GRAY + ", " + EnumChatFormatting.GREEN + pluginName + EnumChatFormatting.GRAY;
                    }
                }
            }

            Logger.logChat("Plugins: " + (message.isEmpty() ? "none." : "(" + size + "): " + EnumChatFormatting.GREEN + message + EnumChatFormatting.GRAY + "."));
            packet.setCancelled(true);
            Pandora.getEventManager().unregister(this);
        }
    }
}
