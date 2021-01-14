package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;
import store.shadowclient.client.hud.draggablehud.hudmods.ModDraggable;

public class ModPerspective extends ModDraggable {
	public static boolean ticks;

	private boolean returnOnRelease = true; //hold down
	private boolean perspectiveToggled = false;

	private float cameraYaw = 0F;
	private float cameraPitch = 0F;
	private int previousPerspective = 0; //previous f5 state

	@EventTarget
	public void keyboardEvent(KeyEvent e) {
		if(e.getKey() == mc.gameSettings.PERSPECTIVE.getKeyCode()) {
			if(Keyboard.getEventKeyState()) {
				perspectiveToggled = !perspectiveToggled;

				cameraYaw = mc.thePlayer.rotationYaw;
				cameraPitch = mc.thePlayer.rotationPitch;

				if(perspectiveToggled) {
					previousPerspective = mc.gameSettings.thirdPersonView;
					mc.gameSettings.thirdPersonView = 1;
				}else {
					mc.gameSettings.thirdPersonView = previousPerspective;
				}
			}
			else if(returnOnRelease) {
				perspectiveToggled = false;
				mc.gameSettings.thirdPersonView = previousPerspective;
			}
		}

		if(e.getKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
			perspectiveToggled = false;
		}
	}

	public float getCameraYaw() {
		return perspectiveToggled ? cameraYaw : mc.thePlayer.rotationYaw;
	}

	public float getCameraPitch() {
		return perspectiveToggled ? cameraPitch : mc.thePlayer.rotationPitch;
	}

	public boolean overrideMouse() {
		if(mc.inGameHasFocus && Display.isActive()) {
			if(!perspectiveToggled) {
				return true;
			}

			mc.mouseHelper.mouseXYChange();
			float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			float f3 = (float) mc.mouseHelper.deltaX * f2;
			float f4 = (float) mc.mouseHelper.deltaY * f2;

			cameraYaw += f3 * 0.15F;
			cameraPitch += f4 * 0.15F;

			if (cameraPitch > 90) cameraPitch = 90;
			if (cameraPitch < -90) cameraPitch = -90;
		}

		return false;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void render(ScreenPosition pos) {
		if(perspectiveToggled) {
		}
	}

	@Override
	public void renderDummy(ScreenPosition pos) {
	}

	@Override
	public void save(ScreenPosition pos) {
	}

	@Override
	public ScreenPosition load() {
		return null;
	}

int counter = 0;

	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
