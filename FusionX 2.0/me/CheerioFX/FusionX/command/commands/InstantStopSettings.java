// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import me.CheerioFX.FusionX.module.modules.InstantStop;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class InstantStopSettings extends Command
{
    @Override
    public String getAlias() {
        return "instantStop";
    }
    
    @Override
    public String getDescription() {
        return "Changes Your instantStop Hack Settings";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "InstantStop MotionY <true/false>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("MotionY")) {
            InstantStop.setMotionY(Boolean.parseBoolean(args[1]));
            FusionX.addChatMessage("InstantStop: MotionY: " + InstantStop.motionY);
        }
        else {
            FusionX.addChatMessage("Wrong Command Usage! Correct Usage: " + FusionX.prefix + "InstantStop MotionY <true/false>");
        }
    }
}
