// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.example;

import me.nico.hush.Client;
import de.Hero.clickgui.ClickGUI;
import me.nico.hush.modules.ModuleManager;
import de.Hero.settings.SettingsManager;

public class ClientMain
{
    public static SettingsManager setmgr;
    public static ModuleManager modulemgr;
    public static ClickGUI clickgui;
    
    public static void setupClient() {
        ClientMain.setmgr = new SettingsManager();
        Client.instance.moduleManager = new ModuleManager();
        ClientMain.clickgui = new ClickGUI();
    }
}
