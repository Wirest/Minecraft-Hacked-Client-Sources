// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class HUD extends Module
{
    public HUD() {
        super("HUD", "HUD", 16777215, 0, Category.RENDER);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Zodiac");
        mode.add("Hush");
        Client.instance.settingManager.rSetting(new Setting("Mode", "HUDMode", this, "Hush", mode));
        Client.instance.settingManager.rSetting(new Setting("Watermark", this, true));
        Client.instance.settingManager.rSetting(new Setting("ArrayList", this, true));
        Client.instance.settingManager.rSetting(new Setting("Hotbar", this, true));
        Client.instance.settingManager.rSetting(new Setting("FPS", this, true));
        Client.instance.settingManager.rSetting(new Setting("Cords", this, true));
        Client.instance.settingManager.rSetting(new Setting("Name & Version", this, true));
        Client.instance.settingManager.rSetting(new Setting("ArraySettings", this, true));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            this.Zodiac();
        }
        else if (Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            this.Hush();
        }
    }
    
    private void Zodiac() {
    }
    
    private void Hush() {
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        if (Client.instance.moduleManager.getModuleName("HUD").isEnabled()) {
            this.toggle();
        }
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
