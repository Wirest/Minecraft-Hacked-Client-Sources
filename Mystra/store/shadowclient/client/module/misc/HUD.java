package store.shadowclient.client.module.misc;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class HUD extends Module {
	
	public HUD() {
		super("HUD", 0, Category.MISC);

		ArrayList<String> options = new ArrayList<>();
		options.add("New");
        options.add("Old");
        options.add("Old2");
        options.add("Novoline");
        Shadow.instance.settingsManager.rSetting(new Setting("ArrayLists", this, "Novoline", options));
        
        ArrayList<String> capes = new ArrayList<>();
        capes.add("Shadow");
        capes.add("Small");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Cape Mode", this, "Shadow", capes));
        
        ArrayList<String> theme = new ArrayList<>();
        theme.add("Shadow");
        theme.add("Cheese");
        Shadow.instance.settingsManager.rSetting(new Setting("Theme", this, "Shadow", theme));
        Shadow.instance.settingsManager.rSetting(new Setting("ArrayListFont", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("ArmorStatus", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Keystrokes", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Watermark", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("TabGui", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("FPS", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("CPS", this, false));
	}
}
