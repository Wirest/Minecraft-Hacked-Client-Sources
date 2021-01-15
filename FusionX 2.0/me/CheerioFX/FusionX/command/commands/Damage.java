// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Damage extends Command
{
    private double damage;
    
    public Damage() {
        this.damage = 0.0;
    }
    
    @Override
    public String getAlias() {
        return "damage";
    }
    
    @Override
    public String getDescription() {
        return "damages the player.";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "damage <amount>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        this.damage = Float.parseFloat(args[0]);
        for (int i = 0; i < 80.0 + 40.0 * (this.damage - 0.5); ++i) {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + 0.049, Wrapper.mc.thePlayer.posZ, false));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ, false));
        }
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ, true));
        FusionX.addChatMessage("You have been damaged successfully!");
        FusionX.addChatMessage("(Damage: " + this.damage + " Hearts)");
    }
}
