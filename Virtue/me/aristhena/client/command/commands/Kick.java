// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.command.commands;

import net.minecraft.network.Packet;
import me.aristhena.utils.ClientUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.aristhena.client.command.Com;
import me.aristhena.client.command.Command;

@Com(names = { "kick", "k" })
public class Kick extends Command
{
    @Override
    public void runCommand(final String[] args) {
        ClientUtils.packet(new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
    }
    
    @Override
    public String getHelp() {
        return "Kick - kick <k> - Kicks you from most servers.";
    }
}
