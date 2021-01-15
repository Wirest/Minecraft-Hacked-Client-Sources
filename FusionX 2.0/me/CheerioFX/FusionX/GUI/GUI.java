// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI;

import net.minecraft.client.gui.GuiScreen;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class GUI extends Module
{
    public GUI() {
        super("ClickGUI", 54, Category.GUI);
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Sound", this, false));
    }
    
    @Override
    public void onEnable() {
        GUI.mc.displayGuiScreen(FusionX.theClient.clickgui);
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            this.toggleModule();
        }
        super.onUpdate();
    }
}
