package store.shadowclient.client.module.render;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Ambiance extends Module {

	public Ambiance() {
		super("Ambiance", 0, Category.RENDER);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Time", this, 16000.0D, 1.0D, 24000.0D, false));
	}

}
