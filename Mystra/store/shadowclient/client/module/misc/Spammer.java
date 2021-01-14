package store.shadowclient.client.module.misc;

import java.util.Random;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;

public class Spammer extends Module {

	TimeHelper delay = new TimeHelper();
	
	public Spammer() {
		super("Spammer", 0, Category.MISC);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Delay", this, 50, 0, 1000, false));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		Random r = new Random();
	    if(this.delay.isDelayComplete((float) ((Shadow.instance.settingsManager.getSettingByName("Delay").getValDouble()) * 1000L))) {
	    	this.mc.thePlayer.sendChatMessage("Shadow B1 | Made by Crystal [" + r.nextInt(346262) + "]");
	    	this.delay.reset();
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
