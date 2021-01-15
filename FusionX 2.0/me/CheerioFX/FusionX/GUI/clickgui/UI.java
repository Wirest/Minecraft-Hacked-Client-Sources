// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI.clickgui;

import java.util.ArrayList;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class UI extends Module
{
    public UI() {
        super("UI", Category.SETTINGS);
    }
    
    public static boolean isTabGUI() {
        return FusionX.theClient.setmgr.getSetting("Tabgui").getValBoolean();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("TabGUI", this, true));
        final ArrayList<String> options = new ArrayList<String>();
        options.add("1");
        options.add("2");
        FusionX.theClient.setmgr.rSetting(new Setting("Theme", this, "1", options));
        FusionX.theClient.setmgr.rSetting(new Setting("GuiRed", this, 255.0, 0.0, 255.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("GuiGreen", this, 26.0, 0.0, 255.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("GuiBlue", this, 42.0, 0.0, 255.0, true));
    }
    
    @Override
    public void onUpdate() {
        if (!this.getState()) {
            this.setState(true);
        }
        super.onUpdate();
    }
}
