// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.client.gui.GuiScreen;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class oldGui extends Module
{
    public oldGui() {
        super("oldGui", 24, Category.GUI, false);
    }
    
    @Override
    public void onToggle() {
        Wrapper.mc.displayGuiScreen(FusionX.theClient.getGui());
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
