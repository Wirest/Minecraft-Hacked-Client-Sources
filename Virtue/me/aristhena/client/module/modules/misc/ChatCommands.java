// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.event.EventTarget;
import me.aristhena.client.command.Command;
import me.aristhena.client.command.CommandManager;
import net.minecraft.network.play.client.C01PacketChatMessage;
import me.aristhena.event.events.PacketSendEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Chat Commands")
public class ChatCommands extends Module
{
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packet = (C01PacketChatMessage)event.getPacket();
            String message = packet.getMessage();
            if (message.startsWith(".")) {
                event.setCancelled(true);
                message = message.replace(".", "");
                final Command commandFromMessage = CommandManager.getCommandFromMessage(message);
                final String[] args = message.split(" ");
                commandFromMessage.runCommand(args);
            }
        }
    }
}
