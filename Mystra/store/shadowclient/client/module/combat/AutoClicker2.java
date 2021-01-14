package store.shadowclient.client.module.combat;

import java.util.ArrayList;
import java.util.Random;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;

public class AutoClicker2 extends Module {
	
	public TimeHelper time = new TimeHelper();

	public AutoClicker2() {
		super("AutoClicker2", 0, Category.COMBAT);
		ArrayList<String> options = new ArrayList();
	      options.add("Left");
	      options.add("Right");
	      Shadow.instance.settingsManager.rSetting(new Setting("ModInfo", this, true));
	      Shadow.instance.settingsManager.rSetting(new Setting("ACMode", this, "Left", options));
	      Shadow.instance.settingsManager.rSetting(new Setting("Clicks", this, 8.0D, 2.0D, 20.0D, true));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
	      int delay = (int) Shadow.instance.settingsManager.getSettingByName("Clicks").getValDouble();
	      if (Shadow.instance.settingsManager.getSettingByName("ModInfo").getValBoolean()) {
	         this.setDisplayName("AutoClicker §7" + delay);
	      } else {
	         this.setDisplayName("AutoClicker");
	      }

	      int mindelay = delay - 1;
	      int maxdelay = delay + 1;
	      int delaydiff = maxdelay - mindelay;
	      Random rdm = new Random();
	      int d = rdm.nextInt(delaydiff);
	      String mode = Shadow.instance.settingsManager.getSettingByName("ACMode").getValString();
	      if (this.time.hasReached((long)(1000 / (d + delay)))) {
	         if (mode.equalsIgnoreCase("left")) {
	            mc.clickMouse();
	         } else if (mode.equalsIgnoreCase("right")) {
	            mc.rightClickMouse();
	         }

	         this.time.reset();
	      }

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
