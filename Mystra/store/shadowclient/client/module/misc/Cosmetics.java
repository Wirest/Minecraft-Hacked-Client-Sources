package store.shadowclient.client.module.misc;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Cosmetics extends Module {
	
	public Cosmetics() {
		super("Cosmetics", 0, Category.MISC);
		
		ArrayList<String> options = new ArrayList<>();

		Shadow.instance.settingsManager.rSetting(new Setting("Hat", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Cape", this, true));
	}
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
