// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.module.modules.Spambot;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class SpamBot extends Command
{
    public static String message;
    
    @Override
    public String getAlias() {
        return "spam";
    }
    
    @Override
    public String getDescription() {
        return "Allows player to change the default spam message.(Spambot Mod)";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "spam set <MESSAGE> | " + FusionX.prefix + "spam delay <DELAY> | " + FusionX.prefix + "spam mode <Mode>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("set")) {
            SpamBot.message = args[1];
            if (args[1].contains("_")) {
                SpamBot.message = args[1].replaceAll("_", " ");
            }
            FusionX.spammessage = SpamBot.message;
            FusionX.addChatMessage("Spam Message has been set! Also Remember to Use UnderScores For Spaces");
        }
        else if (args[0].equalsIgnoreCase("delay")) {
            FusionX.td = Float.parseFloat(args[1]);
            FusionX.addChatMessage("Spam Delay Has Been Set To " + FusionX.td + " Seconds");
        }
        else if (args[0].equalsIgnoreCase("mode")) {
            if (args[1].equalsIgnoreCase("Bypass")) {
                FusionX.td = 3.1f;
                Spambot.bp = true;
                FusionX.addChatMessage("Spam Mode Has Been Set To Bypass");
            }
            else if (args[1].equalsIgnoreCase("Default")) {
                FusionX.td = 1.0f;
                Spambot.bp = false;
                FusionX.addChatMessage("Spam Mode Has Been Set To Default");
            }
            else if (args[1].equalsIgnoreCase("insane")) {
                FusionX.td = 0.01f;
                Spambot.bp = false;
                FusionX.addChatMessage("Spam Mode Has Been Set To Insane");
            }
            else {
                FusionX.addChatMessage("Unknown Mode. Correct Modes: Bypass, Default, Insane");
            }
        }
        else {
            FusionX.addChatMessage("Wrong Command Usage! Correct Usage: " + FusionX.prefix + "spam set [MESSAGE], or " + FusionX.prefix + "spam delay [DELAY], or" + FusionX.prefix + "spam mode [Mode]");
        }
    }
}
