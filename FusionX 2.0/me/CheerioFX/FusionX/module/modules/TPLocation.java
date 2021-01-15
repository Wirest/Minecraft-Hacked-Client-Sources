// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.PathFinder.GotoAI;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.module.Module;

public class TPLocation extends Module
{
    public static BlockPos loc;
    public static boolean locationHasBeenSet;
    private GotoAI ai;
    
    static {
        TPLocation.loc = null;
        TPLocation.locationHasBeenSet = false;
    }
    
    public TPLocation() {
        super("TPLoc", 49, Category.OTHER, false);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            this.setState(false);
        }
    }
    
    @Override
    public void onEnable() {
        if (!TPLocation.locationHasBeenSet || TPLocation.loc == null) {
            FusionX.addChatMessage("The Target Location Has Not Been Set! Type .saveloc To Set The Target Location To Your Location!");
        }
        else {
            (this.ai = new GotoAI(new BlockPos(TPLocation.loc.getX(), TPLocation.loc.getY(), TPLocation.loc.getZ()))).update("TP");
            if (this.ai.isDone() || this.ai.isFailed()) {
                if (this.ai.isFailed()) {
                    FusionX.addChatMessage("Teleport Failed.");
                }
                this.disable();
            }
            final FusionX theClient = FusionX.theClient;
            FusionX.addChatMessage("You've Been Teleported!(" + TPLocation.loc.getX() + ", " + TPLocation.loc.getY() + ", " + TPLocation.loc.getZ() + ")");
        }
        super.onEnable();
    }
    
    private void disable() {
        this.ai.stop();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
