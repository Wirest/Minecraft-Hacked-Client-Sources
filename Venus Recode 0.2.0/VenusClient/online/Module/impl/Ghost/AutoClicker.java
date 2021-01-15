package VenusClient.online.Module.impl.Ghost;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.settings.KeyBinding;

public class AutoClicker extends Module{

	private long lastClick;
	private long hold;
	
	private double speed;
	private double holdLength;
	private double min;
	private double max;
	
	
	public AutoClicker() {
		super("AutoClicker", "AutoClicker", Category.GHOST, Keyboard.KEY_NONE);
	}
	
	@Override
	public void setup() {
		Client.instance.setmgr.rSetting(new Setting("MinCPS", this, 8, 1, 20, false));
		Client.instance.setmgr.rSetting(new Setting("MaxCPS", this, 12, 1, 20, false));
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if (Mouse.isButtonDown(0)) {
			if (System.currentTimeMillis() - lastClick > speed * 1000) {
				lastClick = System.currentTimeMillis();
				if (hold < lastClick) {
					hold = lastClick;
				}
				int key = mc.gameSettings.keyBindAttack.getKeyCode();
				KeyBinding.setKeyBindState(key, true);
				KeyBinding.onTick(key);
				this.updateVals();
			} else if (System.currentTimeMillis() - hold > holdLength * 1000) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				this.updateVals();
			}
		}
	}
	
	@Override
	protected void onEnable() {
		this.updateVals();
		super.onEnable();
	}
	
	private void updateVals() {
		min = Client.instance.setmgr.getSettingByName("MinCPS").getValDouble();
		max = Client.instance.setmgr.getSettingByName("MaxCPS").getValDouble();
		
		if (min >= max) {
			max = min + 1;
		}
		
		speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
		holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);
	}
	
}
