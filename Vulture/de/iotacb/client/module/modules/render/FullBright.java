package de.iotacb.client.module.modules.render;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Expo;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "FullBright", description = "Makes dark places bright", category = Category.RENDER)
public class FullBright extends Module {

	private float startGamma;
	private float currentGamma;
	
	private AnimationUtil animUtil;
	
	@Override
	public void onInit() {
		this.animUtil = new AnimationUtil(Expo.class);
	}

	@Override
	public void onEnable() {
		startGamma = getMc().gameSettings.gammaSetting;
		getMc().gameSettings.gammaSetting = 0;
	}

	@Override
	public void onDisable() {
		getMc().gameSettings.gammaSetting = 0;
		startGamma = 0;
		currentGamma = 0;
		animUtil.getProgression(0).reset();
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (getMc().thePlayer == null) return;
		
		currentGamma = (float) animUtil.easeOut(0, 0, 1, .5);
		currentGamma = MathHelper.clamp_float(currentGamma + .1F, 0, 1);
		
		getMc().gameSettings.gammaSetting = currentGamma;
	}
	
}
