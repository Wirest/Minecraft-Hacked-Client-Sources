package cheatware;

import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import org.lwjgl.opengl.Display;

import cheatware.event.EventManager;
import cheatware.event.EventTarget;
import cheatware.event.events.EventKey;
import cheatware.module.ModuleManager;

public class Cheatware {
    public String name = "CheatWare", version = "1", creator = "Destiny, Vladmyr";

    public static Cheatware instance = new Cheatware();

    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ClickGUI clickGui;
    
    public boolean destructed;

    public void startClient() {
        settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        clickGui = new ClickGUI();
        destructed = false;

        System.out.println("[" + name + "] Starting client, b" + version + ", created by " + creator);
        Display.setTitle(name + " b" + version);

        eventManager.register(this);
    }
    
    public void reloadClient() {
    	settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        clickGui = new ClickGUI();
        System.out.println("Reloaded CheatWare...");
    }

    public void stopClient() {
        eventManager.unregister(this);
    }
    
    public void selfDestruct() {
    	moduleManager = null;
    	settingsManager = null;
    	eventManager = null;
    	clickGui = null;
    	destructed = true;
        Display.setTitle("Minecraft 1.8.8");
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
}
