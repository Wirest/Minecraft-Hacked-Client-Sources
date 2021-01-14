package store.shadowclient.client.module.combat;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class SwordAnimation extends Module {
	public SwordAnimation() {
		super("SwordAnimation", 0, Category.MISC);

		ArrayList<String> options = new ArrayList<>();
		options.add("EXHIBITION");
		options.add("SLIDE");
		options.add("HIGH");
        options.add("OLD");
        Shadow.instance.settingsManager.rSetting(new Setting("Animation Mode", this, "EXHIBITION", options));
        
        Shadow.instance.settingsManager.rSetting(new Setting("Swing Speed", this, 6.0D, 2.0D, 12.0D, false));
        Shadow.instance.settingsManager.rSetting(new Setting("X", this, 0.0D, -1.0D, 1.0D, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Y", this, 0.15D, -1.0D, 1.0D, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Z", this, 0.0D, -1.0D, 1.0D, false));
	}
	
	public void onUpdate(EventUpdate event) {
		String mode = Shadow.instance.settingsManager.getSettingByName("Animation Mode").getValString();
		
		if(mode.equalsIgnoreCase("EXHIBITION")) {
			this.setDisplayName("SwordAnimation §7| EXHIBITION");
		}
		
		if(mode.equalsIgnoreCase("SLIDE")) {
			this.setDisplayName("SwordAnimation §7| SLIDE");
		}
		
		if(mode.equalsIgnoreCase("HIGH")) {
			this.setDisplayName("SwordAnimation §7| HIGH");
		}
		
		if(mode.equalsIgnoreCase("OLD")) {
			this.setDisplayName("SwordAnimation §7| OLD");
		}
	}
}
