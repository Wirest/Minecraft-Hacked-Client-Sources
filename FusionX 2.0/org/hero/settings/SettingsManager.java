// 
// Decompiled by Procyon v0.5.30
// 

package org.hero.settings;

import me.CheerioFX.FusionX.FusionX;
import java.util.Iterator;
import me.CheerioFX.FusionX.module.Module;
import java.util.ArrayList;

public class SettingsManager
{
    private ArrayList<Setting> settings;
    
    public SettingsManager() {
        this.settings = new ArrayList<Setting>();
    }
    
    public void rSetting(final Setting in) {
        this.settings.add(in);
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public ArrayList<Setting> getSettingsByMod(final Module mod) {
        final ArrayList<Setting> out = new ArrayList<Setting>();
        for (final Setting s : this.getSettings()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting set : this.getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[" + FusionX.theClient.Client_Name + "] Error Setting NOT found: '" + name + "'!");
        return null;
    }
    
    public Setting getSetting(final String name) {
        for (final Setting set : this.getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[" + FusionX.theClient.Client_Name + "] Error Setting NOT found: '" + name + "'!");
        return null;
    }
    
    public Setting getSetting(final Module mod, final String name) {
        final ArrayList<Setting> out = new ArrayList<Setting>();
        for (final Setting s : this.getSettings()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        for (final Setting set : out) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[" + FusionX.theClient.Client_Name + "] Error Setting NOT found: '" + name + "'!");
        return null;
    }
}
