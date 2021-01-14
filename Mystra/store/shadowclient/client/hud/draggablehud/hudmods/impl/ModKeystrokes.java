package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;
import store.shadowclient.client.hud.draggablehud.hudmods.ModDraggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class ModKeystrokes extends ModDraggable {
public static enum KeystrokesMode {
		WASD(Key.W, Key.A, Key.S, Key.D),
		WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
		WASD_SPACE(Key.W, Key.A, Key.S, Key.D, new Key("§lSPACE", Minecraft.getMinecraft().gameSettings.keyBindJump, 10, 60, 88, 28)),
		WASD_MOUSE_SPACE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("§lSpace", Minecraft.getMinecraft().gameSettings.keyBindJump, 1, 61, 58, 16));

		private final Key[] keys;
		private int width = 0;
		private int height = 0;

		private KeystrokesMode(Key... keysIn) {
			this.keys = keysIn;

			for(Key key : keys) {
				this.width = Math.max(this.width, key.getX() + key.getWidth());
				this.height = Math.max(this.height, key.getY() + key.getHeight());
			}
		}
		public int getHeight() {
			return height;
		}
		public int getWidth() {
			return width;
		}
		public Key[] getKeys() {
			return keys;
		}
	}

	private static class Key {
		private static final Key W = new Key("§lW", Minecraft.getMinecraft().gameSettings.keyBindForward, 21, 1, 18, 18);
		private static final Key A = new Key("§lA", Minecraft.getMinecraft().gameSettings.keyBindLeft, 1, 21, 18, 18);
		private static final Key S = new Key("§lS", Minecraft.getMinecraft().gameSettings.keyBindBack, 21, 21, 18, 18);
		private static final Key D = new Key("§lD", Minecraft.getMinecraft().gameSettings.keyBindRight, 41, 21, 18, 18);

		private static final Key LMB = new Key("§lLMB", Minecraft.getMinecraft().gameSettings.keyBindAttack, 1, 41, 28, 18);
		private static final Key RMB = new Key("§lRMB", Minecraft.getMinecraft().gameSettings.keyBindUseItem, 31, 41, 28, 18);

		private final String name;
		private final KeyBinding keyBind;
		private final int x;
		private final int y;
		private final int width;
		private final int height;

		public Key(String name, KeyBinding keyBind, int x, int y, int width, int height) {
			this.name = name;
			this.keyBind = keyBind;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		public boolean isDown() {
			return keyBind.isKeyDown();
		}
		public String getName() {
			return name;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
	}
	private KeystrokesMode mode = KeystrokesMode.WASD_MOUSE_SPACE;
	private ScreenPosition pos = ScreenPosition.fromRelativePosition(-2.5, 0.2);
	public void setMode(KeystrokesMode mode) {
		this.mode = mode;
	}
	@Override
	public int getWidth() {
		return mode.getWidth();
	}
	@Override
	public int getHeight() {
		return mode.getHeight();
	}
	@Override
	public void render(ScreenPosition pos) {
		if(Shadow.instance.moduleManager.getModuleByName("HUD").isToggled()) {
			if(Shadow.instance.settingsManager.getSettingByName("Keystrokes").getValBoolean()) {
		for(Key key : mode.getKeys()) {
			int textWidth = font.getStringWidth(key.getName());

			Gui.drawRect(
					pos.getAbsoluteX() + key.getX(),
					pos.getAbsoluteY() + key.getY(),
					pos.getAbsoluteX() + key.getX() + key.getWidth(),
					pos.getAbsoluteY() + key.getY() + key.getHeight(),
					key.isDown() ? new Color(255, 255, 255, 102).getRGB() : new Color(0, 0, 0, 150).getRGB());

			font.drawString(
					key.getName(),
					pos.getAbsoluteX() + key.getX() + key.getWidth() / 2 - textWidth / 2,
					pos.getAbsoluteY() + key.getY() + key.getHeight() / 2 - 4,
					key.isDown() ? Color.BLACK.getRGB() : rainbow(counter));
				}
			}
		}
	}
	
	@Override
		public void renderDummy(ScreenPosition pos) {
		for(Key key : mode.getKeys()) {
			int textWidth = font.getStringWidth(key.getName());
				Gui.drawRect(
				pos.getAbsoluteX() + key.getX(),
				pos.getAbsoluteY() + key.getY(),
				pos.getAbsoluteX() + key.getX() + key.getWidth(),
				pos.getAbsoluteY() + key.getY() + key.getHeight(),
				key.isDown() ? new Color(255, 255, 255, 102).getRGB() : new Color(0, 0, 0, 150).getRGB());

		font.drawString(
				key.getName(),
				pos.getAbsoluteX() + key.getX() + key.getWidth() / 2 - textWidth / 2,
				pos.getAbsoluteY() + key.getY() + key.getHeight() / 2 - 4,
				key.isDown() ? Color.BLACK.getRGB() : rainbow(counter));
			super.renderDummy(pos);
		}
	
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
