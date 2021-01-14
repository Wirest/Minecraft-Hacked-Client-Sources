package store.shadowclient.client.module.combat;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import io.netty.util.internal.ThreadLocalRandom;
import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class AutoClicker extends Module {

	private long lastClick;
	private long hold;
	
	private double speed;
	private double holdLenght;
	private double min;
	private double max;
	
	public AutoClicker() {
		super("AutoClicker", Keyboard.KEY_M, Category.COMBAT);
		
		Shadow.instance.settingsManager.rSetting(new Setting("MinCPS", this, 8, 1, 20, false));
		Shadow.instance.settingsManager.rSetting(new Setting("MaxCPS", this, 12, 1, 20, false));
	}
	
	@EventTarget
	public void onTick(ClientTickEvent e) {
		 if(Mouse.isButtonDown(0)) {
			 if(System.currentTimeMillis() - lastClick > speed * 1000) {
				  lastClick = System.currentTimeMillis();
				  if(hold < lastClick) {
					  hold = lastClick;
				  }
				  int key = mc.gameSettings.keyBindAttack.getKeyCode();
				  KeyBinding.setKeyBindState (key, true);
				  KeyBinding.onTick(key);
				  this.updateVals();
			 } else if (System.currentTimeMillis() - hold > holdLenght * 1000) {
				 KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				 this.updateVals();
			 }
		 }
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.updateVals();
	}
	
	private void updateVals() {
		min = Shadow.instance.settingsManager.getSettingByName("MinCPS").getValDouble();
		max = Shadow.instance.settingsManager.getSettingByName("MinCPS").getValDouble();
		if(min >= max) {
			max = min + 1;
		}
		speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
		holdLenght = speed / ThreadLocalRandom.current().nextDouble(min, max); 
	}
}
