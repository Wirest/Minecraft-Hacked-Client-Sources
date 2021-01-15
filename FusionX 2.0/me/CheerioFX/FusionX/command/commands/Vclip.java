// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Vclip extends Command
{
    @Override
    public String getAlias() {
        return "vclip";
    }
    
    @Override
    public String getDescription() {
        return "tps the player up and down";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "vclip <blocks>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final double blocks = Double.parseDouble(args[0]);
        final boolean isNegative = blocks < 0.0;
        final double yPos = Wrapper.mc.thePlayer.posY + (blocks + 0.002);
        Wrapper.mc.thePlayer.setLocationAndAngles(Wrapper.mc.thePlayer.posX, yPos, Wrapper.mc.thePlayer.posZ, Wrapper.mc.thePlayer.rotationYaw, Wrapper.mc.thePlayer.rotationPitch);
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, yPos - (isNegative ? 0.1 : 0.0), Wrapper.mc.thePlayer.posZ, true));
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - (isNegative ? 1 : 0), Wrapper.mc.thePlayer.posZ, false));
        FusionX.addChatMessage("You Have Teleported Successfully!");
    }
}
