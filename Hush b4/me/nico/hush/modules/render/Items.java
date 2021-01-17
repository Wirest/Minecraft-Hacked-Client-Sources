// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import com.darkmagician6.eventapi.EventManager;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Items extends Module
{
    public Items() {
        super("Items", "Items", 2948717, 0, Category.RENDER);
        Client.instance.settingManager.rSetting(new Setting("X §l\u2192", "SizeX", this, 0.2, 0.1, 1.0, false));
        Client.instance.settingManager.rSetting(new Setting("Y §l\u2192", "SizeY", this, 0.2, 0.1, 1.0, false));
        Client.instance.settingManager.rSetting(new Setting("Z §l\u2192", "SizeZ", this, 0.2, 0.1, 1.0, false));
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
