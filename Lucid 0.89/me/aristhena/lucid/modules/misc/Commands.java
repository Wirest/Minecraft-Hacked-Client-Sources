/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C01PacketChatMessage
 */
package me.aristhena.lucid.modules.misc;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketSendEvent;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

@Mod(enabled = true, shown = false)
public class Commands extends Module
{
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.packet instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packet = (C01PacketChatMessage)event.packet;
            final String message = packet.getMessage();
            if (message.startsWith(CommandManager.commandPrefix)) {
                event.setCancelled(true);
                final String[] args = message.split(" ");
                final Command commandFromMessage = CommandManager.getCommandFromMessage(message);
                commandFromMessage.runCommand(args);
            }
        }
    }
}

