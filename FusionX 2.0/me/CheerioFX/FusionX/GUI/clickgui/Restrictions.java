// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI.clickgui;

import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Restrictions extends Module
{
    public Restrictions() {
        super("Restrictions", Category.SETTINGS);
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Badlion/Cubecraft", this, false));
        FusionX.theClient.setmgr.rSetting(new Setting("Arcane/VeltPvP", this, false));
    }
    
    public static boolean guardian() {
        return FusionX.theClient.setmgr.getSetting("arcane/veltpvp").getValBoolean();
    }
    
    public static boolean gcheat() {
        return FusionX.theClient.setmgr.getSetting("badlion/cubecraft").getValBoolean();
    }
    
    @Override
    public void onUpdate() {
        if (!this.getState()) {
            this.setState(true);
        }
        super.onUpdate();
    }
}
