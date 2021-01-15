// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import cf.euphoria.euphorical.Events.EventPacketTake;
import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Utils.NetUtils;
import cf.euphoria.euphorical.Utils.TimeHelper;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

@Info(name = "pd", syntax = {}, help = "Tries to find server plugins")
public class PDCmd extends Cmd
{
    TimeHelper timer;
    
    public PDCmd() {
        this.timer = new TimeHelper();
    }
    
    @EventTarget
    public void onReceivePacket(final EventPacketTake event) {
        if (event.packet instanceof S3APacketTabComplete) {
            final S3APacketTabComplete packet = (S3APacketTabComplete)event.packet;
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
                        message = String.valueOf(message) + "§8, §7" + pluginName;
                    }
                }
            }
            if (!message.isEmpty()) {
                ChatUtils.sendMessageToPlayer("Plugins (" + size + "): §7" + message + "§8.");
            }
            else {
                ChatUtils.sendMessageToPlayer("Plugins: None Found!");
            }
            event.setCancelled(true);
            EventManager.unregister(this);
        }
        if (this.timer.hasPassed(20000.0)) {
            EventManager.unregister(this);
            ChatUtils.sendMessageToPlayer("Stopped listening for an S3APacketTabComplete! Took too long (20s)!");
        }
    }
    
    @Override
    public void execute(final String[] p0) throws Error {
        this.timer.reset();
        EventManager.register(this);
        NetUtils.sendPacket(new C14PacketTabComplete("/"));
        ChatUtils.sendMessageToPlayer("Listening for a S3APacketTabComplete for 20s!");
    }
}
