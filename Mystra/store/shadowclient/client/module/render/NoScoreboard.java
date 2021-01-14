package store.shadowclient.client.module.render;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class NoScoreboard extends Module {

	public NoScoreboard() {
		super("NoScoreboard", 0, Category.RENDER);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Position", this, 0, -150, 200, false));
	}

}
