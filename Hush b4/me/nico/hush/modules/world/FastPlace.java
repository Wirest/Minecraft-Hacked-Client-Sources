// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.world;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class FastPlace extends Module
{
    public FastPlace() {
        super("FastPlace", "FastPlace", 580759, 0, Category.WORLD);
        Client.instance.settingManager.rSetting(new Setting("Delay §l\u2192", "Delay", this, 1.0, 0.0, 3.0, true));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        FastPlace.mc.rightClickDelayTimer = (int)Client.instance.settingManager.getSettingByName("Delay").getValDouble();
        this.setDisplayname("FastPlace[" + Client.instance.settingManager.getSettingByName("Delay").getValDouble() + "]");
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
