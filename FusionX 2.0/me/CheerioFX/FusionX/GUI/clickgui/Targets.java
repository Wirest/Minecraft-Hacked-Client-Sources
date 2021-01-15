// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI.clickgui;

import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Targets extends Module
{
    public Targets() {
        super("Targets", Category.SETTINGS);
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Players", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("Mobs", this, false));
        FusionX.theClient.setmgr.rSetting(new Setting("Animals", this, false));
        FusionX.theClient.setmgr.rSetting(new Setting("AntiBot", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("NoTeams", this, false));
    }
    
    public static boolean players() {
        return FusionX.theClient.setmgr.getSetting("players").getValBoolean();
    }
    
    public static boolean mobs() {
        return FusionX.theClient.setmgr.getSetting("mobs").getValBoolean();
    }
    
    public static boolean animals() {
        return FusionX.theClient.setmgr.getSetting("animals").getValBoolean();
    }
    
    public static boolean antibot() {
        return FusionX.theClient.setmgr.getSetting("antibot").getValBoolean();
    }
    
    public static boolean noteams() {
        return FusionX.theClient.setmgr.getSetting("noteams").getValBoolean();
    }
    
    @Override
    public void onUpdate() {
        if (!this.getState()) {
            this.setState(true);
        }
        super.onUpdate();
    }
}
