package de.iotacb.client.module.modules.combat;

import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Mouse;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSword;

@ModuleInfo(name = "AutoClicker", description = "Automatically clicks for you", category = Category.COMBAT)
public class AutoClicker extends Module {

	private Timer timer;
	
	@Override
	public void onInit() {
		addValue(new Value("AutoClickerMax CPS", 12, new ValueMinMax(1, 20, 1)));
		addValue(new Value("AutoClickerMin CPS", 8, new ValueMinMax(1, 20, 1)));
		
		addValue(new Value("AutoClickerPlace", true));
		
		this.timer = new Timer();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getMc().currentScreen != null) return;
		if (Mouse.isButtonDown(0)) {
			getMc().leftClickCounter = 0;
			if (timer.delay(calculateCPS())) {
				getMc().clickMouse();
			}
		} else if (Mouse.isButtonDown(1)) {
			if ((getMc().thePlayer.getHeldItem() == null || !(getMc().thePlayer.getHeldItem().getItem() instanceof ItemBlock)) || !getValueByName("AutoClickerPlace").getBooleanValue()) return;
			if (timer.delay(calculateCPS())) {
				getMc().rightClickMouse();
			}
		}
	}
	
	private long calculateCPS() {
		if (getValueByName("AutoClickerMin CPS").getNumberValue() == getValueByName("AutoClickerMax CPS").getNumberValue()) return 20;
		return (long) (1000 / ThreadLocalRandom.current().nextDouble(getValueByName("AutoClickerMin CPS").getNumberValue(), getValueByName("AutoClickerMax CPS").getNumberValue()));
	}

}
