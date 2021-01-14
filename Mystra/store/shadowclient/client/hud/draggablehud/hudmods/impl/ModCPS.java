package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;
import store.shadowclient.client.hud.draggablehud.hudmods.ModDraggable;

public class ModCPS extends ModDraggable {
	private List<Long> clicks = new ArrayList<Long>();
	private boolean wasPressed;
	private long lastPressed;
	private ScreenPosition pos = ScreenPosition.fromRelativePosition(-2.3, 0.39);

	@Override
	public int getWidth() {
		return font.getStringWidth("00 CPS");
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		if(Shadow.instance.moduleManager.getModuleByName("HUD").isToggled()) {
			if(Shadow.instance.settingsManager.getSettingByName("CPS").getValBoolean()) {
				final boolean pressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);

				if(pressed != this.wasPressed) {
					this.lastPressed = System.currentTimeMillis();
					this.wasPressed = pressed;
					if(pressed) {
						this.clicks.add(this.lastPressed);
					}
				}
				font.drawString("[CPS]: " + getCPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), rainbow(counter));
			}
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString("[CPS]: " + getCPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), rainbow(counter));
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

	private int getCPS() {
		final long time = System.currentTimeMillis();
		this.clicks.removeIf(aLong -> aLong + 1000 < time);
		return this.clicks.size();
	}

	int counter = 0;

	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
