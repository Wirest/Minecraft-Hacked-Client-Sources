// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class ClearSaves extends Module
{
    public ClearSaves() {
        super("ClearSaves", Category.OTHER);
    }
    
    @Override
    public void onEnable() {
        FusionX.theClient.fileManager.clearAll();
        FusionX.addChatMessage("Your Saves has Been Cleared! Restart the Client to Reset to Default Settings!");
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            this.setState(false);
        }
        super.onUpdate();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
