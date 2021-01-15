// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class GhostClientSettings extends Command
{
    @Override
    public String getAlias() {
        return "ghostclient";
    }
    
    @Override
    public String getDescription() {
        return "Changes Your GhostClient settings";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "Ghostclient DisableClient <true/false>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("DisableClient")) {
            GhostClient.setDisableAllHacks(Boolean.parseBoolean(args[1]));
            FusionX.addChatMessage("Your GhostClient Disable Client settings has been set to " + GhostClient.isDisableAllHacks() + "!");
        }
        else {
            FusionX.addChatMessage("Wrong Command Usage! Correct Usage: " + FusionX.prefix + "Ghostclient DisableClient <true/false>");
        }
    }
}
