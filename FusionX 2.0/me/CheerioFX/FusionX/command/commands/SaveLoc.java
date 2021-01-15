// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.module.modules.TPLocation;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;

public class SaveLoc extends Command
{
    @Override
    public String getAlias() {
        return "saveloc";
    }
    
    @Override
    public String getDescription() {
        return "Saves Your Location For the TP Mod";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "saveloc";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final BlockPos theLoc = TPLocation.loc = Wrapper.mc.thePlayer.getPosition();
        TPLocation.locationHasBeenSet = true;
        FusionX.addChatMessage("Your Location Has Been Saved!(" + TPLocation.loc.getX() + ", " + TPLocation.loc.getY() + ", " + TPLocation.loc.getZ() + ")");
    }
}
