// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Say extends Command
{
    public static String message;
    
    @Override
    public String getAlias() {
        return "say";
    }
    
    @Override
    public String getDescription() {
        return "(Makes a player say something in chat)";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "say <MESSAGE>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Say.message = args[0];
        if (args[0].contains("_")) {
            Say.message = args[0].replaceAll("_", " ");
        }
        FusionX.addChatMessage("Message has been sent! Also Remember to Use UnderScores For Spaces");
        Wrapper.mc.thePlayer.sendChatMessage(Say.message);
    }
}
