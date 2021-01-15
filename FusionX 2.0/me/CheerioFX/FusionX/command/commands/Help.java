// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Help extends Command
{
    @Override
    public String getAlias() {
        return "help";
    }
    
    @Override
    public String getDescription() {
        return "(This is the Help page for commands.)";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "help <page>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("1")) {
            FusionX.addChatMessage(" ==== Help == Page 1/2 ====");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Help " + "(Displays this Help Page For Commands)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Prefix " + "(Change The Command Prefix)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Say " + "(Say Something in Public Chat)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Toggle " + "(Toggles a Mod)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Bind " + "(Changes Your Binds)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "SpamMessage " + "(Changes Your Spambot Settings)");
            FusionX.addChatMessage(" ==== Help == Page 1/2 ====");
        }
        else if (args[0].equalsIgnoreCase("2")) {
            FusionX.addChatMessage(" ==== Help == Page 2/2 ====");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Script " + "(runs a script)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "InstantStop " + "(Changes Your InstantStop Settings)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "GhostClient" + "(Changes Your GhostClient Settings)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "saveloc " + "(Saves your loc for the TPLoc Hack)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "vclip" + "(Teleports you up and down)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Say " + "(Say Something in Chat)");
            FusionX.addChatMessage(" ==== Help == Page 2/2 ====");
        }
        else {
            FusionX.addChatMessage(" ==== Help == Page 1/2 ====");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Help " + "(Displays this Help Page For Commands)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Prefix " + "(Change The Command Prefix)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Say " + "(Say Something in Public Chat)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Toggle " + "(Toggles a Mod)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "Bind " + "(Changes Your Binds)");
            FusionX.addChatMessage(String.valueOf(FusionX.prefix) + "SpamMessage " + "(Changes Your Spambot Settings)");
            FusionX.addChatMessage(" ==== Help == Page 1/2 ====");
        }
    }
}
