package store.shadowclient.client.module.movement;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Step extends Module {
    public Step() {
        super("Step", Keyboard.KEY_L, Category.MOVEMENT);
        
        Shadow.instance.settingsManager.rSetting(new Setting("Step Height", this, 1.0F, 0.5F, 2F, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.thePlayer.stepHeight = (float) Shadow.instance.settingsManager.getSettingByName("Step Height").getValDouble();
    }

    @Override
    public void onDisable() {
    	mc.thePlayer.stepHeight = 0.5F;
        super.onDisable();
    }
}
