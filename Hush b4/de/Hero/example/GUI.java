// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.example;

import net.minecraft.client.gui.GuiScreen;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class GUI extends Module
{
    public GUI() {
        super("ClickGui", "ClickGui", 16777215, 54, Category.CLIENT);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Zodiac");
        options.add("Hero");
        Client.instance.settingManager.rSetting(new Setting("Design", this, "Zodiac", options));
        Client.instance.settingManager.rSetting(new Setting("Sound", this, false));
        Client.instance.settingManager.rSetting(new Setting("Red", this, 0.0, 0.0, 255.0, true));
        Client.instance.settingManager.rSetting(new Setting("Green", this, 255.0, 0.0, 255.0, true));
        Client.instance.settingManager.rSetting(new Setting("Blue", this, 120.0, 0.0, 255.0, true));
    }
    
    @Override
    public void onEnable() {
        GUI.mc.displayGuiScreen(Client.instance.clickGUI);
        this.toggle();
        super.onEnable();
    }
}
