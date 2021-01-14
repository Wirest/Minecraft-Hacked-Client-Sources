package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;
import store.shadowclient.client.hud.draggablehud.hudmods.ModDraggable;

public class ModFPS extends ModDraggable {
	private ScreenPosition pos = ScreenPosition.fromRelativePosition(-2.3, 0.36);

	@Override
	public int getWidth() {
		return 50;
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		if(Shadow.instance.moduleManager.getModuleByName("HUD").isToggled()) {
			if(Shadow.instance.settingsManager.getSettingByName("FPS").getValBoolean()) {
				font.drawString("[FPS]: " + mc.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), rainbow(counter));
			}
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString("[FPS]: " + mc.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), rainbow(counter));
		super.renderDummy(pos);
	}
	@Override
	public void save(ScreenPosition pos) {
		this.pos = pos;
	}

	@Override
	public ScreenPosition load() {
		return pos;
	}
	int counter = 0;

	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
