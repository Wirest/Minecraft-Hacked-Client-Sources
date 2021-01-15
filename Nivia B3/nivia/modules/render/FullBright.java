package nivia.modules.render;

import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.modules.Module;

public class FullBright extends Module {

	public FullBright() {
		super("Fullbright", 0, 0, Category.RENDER, "Maxes out ingame brightness",
				new String[] { "fb", "fullb", "fbright" }, false);
	}

	float oldGamma;

	@EventTarget
	public void onTick(EventTick tick) {
		oldGamma = mc.gameSettings.gammaSetting - 10;
		this.mc.gameSettings.gammaSetting = 1000.0F;
		
		
	}

	@Override
	public void onDisable() {
		super.onDisable();
		
		this.mc.gameSettings.gammaSetting = oldGamma;
		mc.entityRenderer.updateRenderer();
	}
}
