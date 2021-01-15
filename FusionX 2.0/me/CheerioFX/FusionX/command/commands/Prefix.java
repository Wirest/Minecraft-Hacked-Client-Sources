// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Prefix extends Command
{
    @Override
    public String getAlias() {
        return "prefix";
    }
    
    @Override
    public String getDescription() {
        return "(Allows The Player To Change Their FusionX Client Commands Prefix)";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "prefix set <prefix>" + " | " + FusionX.prefix + "prefix default";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("set")) {
            String prefix = args[1];
            if (args[1].contains("_")) {
                prefix = args[1].replaceAll("_", " ");
            }
            FusionX.prefix = prefix;
            FusionX.addChatMessage("Your Commands Prefix Has Been Set To " + prefix);
        }
        else if (args[0].equalsIgnoreCase("default")) {
            FusionX.prefix = ".";
            FusionX.addChatMessage("Your Commands Prefix Has Been Set To The Default Prefix(.)");
        }
        else {
            FusionX.addChatMessage("Wrong Command Usage! Correct Usage: " + FusionX.prefix + "prefix set <prefix>, or " + FusionX.prefix + "prefix default");
        }
    }
}
