package Blizzard.Mod.mods.render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Blizzard.Blizzard;
import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventRender2D;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;
import Blizzard.UI.tab.TabGui;

public class HUD extends Mod {

	static TabGui tab = new TabGui();
	static int color = new Color(222, 101, 33, 180).getRGB();

	public HUD() {
		super("HUD", "HUD", Keyboard.KEY_RSHIFT, Category.RENDER);

	}

	@EventTarget
	public void onUpdate(final EventRender2D event) {
		this.RenderHUD();
	}

	public static void RenderHUD() {
		// hold on i forgot the dam. it does not work i do not know why
		mc.getMinecraft().fontRendererObj.drawString("Blizzard " + Blizzard.Version, 2, 2, 0xffffff);
		tab.drawGui(2, 10);
		int count = 0;
		for (Mod mod : ModManager.getMods()) {

			if (!mod.isToggled())// junk@beihl.com:simple10
				continue;
			ArrayList<String> toRender = new ArrayList<>();
			toRender.sort(String::compareToIgnoreCase);
			toRender.sort((mod1, mod2) -> Integer.compare(mc.fontRendererObj.getStringWidth(mod2),
					(mc.fontRendererObj.getStringWidth(mod1))));
			mc.fontRendererObj.drawString("" + mod.getName(), 750, 2 + (10 * count), color);
			toRender.add(mod.getName());
			count++;
		}
	}
}
