// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class DankBobbing extends Module
{
    public static boolean enabled;
    boolean oldBobbingSetting;
    
    static {
        DankBobbing.enabled = false;
    }
    
    public DankBobbing() {
        super("DankBobbing", 0, Category.OTHER);
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Multiplier", this, 2.0, 0.0, 5.0, true));
    }
    
    public static double getMultiplier() {
        return FusionX.theClient.setmgr.getSetting("Multiplier").getValInt() * 10;
    }
    
    @Override
    public void onEnable() {
        this.oldBobbingSetting = DankBobbing.mc.gameSettings.viewBobbing;
        DankBobbing.mc.gameSettings.viewBobbing = true;
        DankBobbing.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        DankBobbing.mc.gameSettings.viewBobbing = this.oldBobbingSetting;
        DankBobbing.enabled = false;
        super.onDisable();
    }
}
