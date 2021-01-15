// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import java.util.Iterator;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class Toggle extends Command
{
    @Override
    public String getAlias() {
        return "toggle";
    }
    
    @Override
    public String getDescription() {
        return "Toggles a Mod";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "toggle <Module>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        boolean found = false;
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        for (final Module m : ModuleManager.getModules()) {
            String newArgs;
            if (args[0].contains("_")) {
                newArgs = args[0].replaceAll("_", " ");
            }
            else {
                newArgs = args[0];
            }
            if (newArgs.equalsIgnoreCase(m.getName())) {
                m.toggleModule();
                found = true;
                final FusionX theClient = FusionX.theClient;
                FusionX.addChatMessage(String.valueOf(m.getName()) + " was toggled!");
            }
        }
        if (!found) {
            final FusionX theClient2 = FusionX.theClient;
            FusionX.addChatMessage("Invalid Module!");
        }
    }
}
