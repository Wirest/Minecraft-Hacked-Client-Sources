package me.ihaq.iClient.modules.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.gui.clickgui.Frame;
import me.ihaq.iClient.modules.Module;
import net.minecraft.client.Minecraft;

public class ClickGui extends Module {

	private ArrayList<Frame> frames = new ArrayList<Frame>();

	public ClickGui() {
		super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER, "");
	}

	@Override
	public void onEnable() {
		mc.displayGuiScreen(new me.ihaq.iClient.gui.clickgui.ClickGui());

	}

	@Override
	public void onDisable() {
		this.setToggled(false);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (!this.isToggled()) {
			return;
		}
		if (this.isToggled() || Minecraft.getMinecraft().gameSettings.keyBindBack.pressed) {
			onDisable();
		}
	}
}