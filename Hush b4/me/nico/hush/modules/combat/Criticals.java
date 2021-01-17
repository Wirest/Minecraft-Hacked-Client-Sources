// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.combat;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Criticals extends Module
{
    public Criticals() {
        super("Criticals", "Criticals", 16777215, 0, Category.COMBAT);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Jump");
        mode.add("Packet");
        Client.instance.settingManager.rSetting(new Setting("Mode", "CriticalsMode", this, "Jump", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("CriticalsMode").getValString().equalsIgnoreCase("Jump")) {
            this.Jump();
        }
        else if (Client.instance.settingManager.getSettingByName("CriticalsMode").getValString().equalsIgnoreCase("Packet")) {
            this.Packet();
        }
    }
    
    private void Jump() {
    }
    
    private void Packet() {
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
