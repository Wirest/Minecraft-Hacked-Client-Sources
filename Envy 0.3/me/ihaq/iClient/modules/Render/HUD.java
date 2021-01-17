package me.ihaq.iClient.modules.Render;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventKeyboard;
import me.ihaq.iClient.event.events.EventRender2D;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.gui.clickgui.Frame;
import me.ihaq.iClient.gui.tabgui.TabGUI;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.Colors;
import me.ihaq.iClient.utils.ComparatorStrings;
import me.ihaq.iClient.utils.values.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class HUD extends Module {

	public BooleanValue tabGui = new BooleanValue(this, "TabGUI", "tabgui", Boolean.valueOf(true));
	public BooleanValue arrayList = new BooleanValue(this, "ArrayList", "arraylist", Boolean.valueOf(true));
	public BooleanValue direction = new BooleanValue(this, "Direction", "direction", Boolean.valueOf(true));
	public BooleanValue rainbow = new BooleanValue(this, "Rainbow", "rainbow", Boolean.valueOf(false));

	private ArrayList<String> mods = new ArrayList<String>();
	public static TabGUI tab;

	public HUD() {
		super("HUD", Keyboard.KEY_NONE, Category.RENDER, "");
		tab = new TabGUI();
	}

	@EventTarget
	public void onEvent(EventUpdate e) {
		if (!this.isToggled()) {
			return;
		}
		Colors.rainbow = (Boolean) rainbow.getValue() ? true : false;
	}

	@SuppressWarnings("incomplete-switch")
	@EventTarget
	public void onRender(EventRender2D e) {
		if (!this.isToggled()) {
			return;
		}

		Entity entity = this.mc.getRenderViewEntity();
		EnumFacing enumfacing = entity.getHorizontalFacing();
		String s = "Invalid";
		switch (enumfacing) {
		case NORTH:
			s = "NORTH";
			break;

		case SOUTH:
			s = "SOUTH";
			break;

		case WEST:
			s = "WEST";
			break;

		case EAST:
			s = "EAST";
		}

		double playerX = Math.round(mc.thePlayer.posX * 100.0) / 100.0;
		double playerY = Math.round(mc.thePlayer.posY * 100.0) / 100.0;
		double playerZ = Math.round(mc.thePlayer.posZ * 100.0) / 100.0;

		if (direction.getValue()) {
			Envy.FONT_MANAGER.hud.drawString(s, (GuiScreen.width / 2) - (Envy.FONT_MANAGER.hud.getStringWidth(s) / 2),
					2, Colors.getColor());
		}

		String ping = "Ping: \u00A7f" + getPing() + "ms";
		String coords = "XYZ: \u00A7f" + (int) playerX + "," + (int) playerY + "," +(int) playerZ;

		if (mc.ingameGUI.getChatGUI().getChatOpen()) {
			Envy.FONT_MANAGER.hud.drawString(ping + " - \u00A7r" + coords, 1, GuiScreen.height - 26, Colors.getColor());

		} else {
			Envy.FONT_MANAGER.hud.drawString(coords, 1, GuiScreen.height - 11, Colors.getColor());
			Envy.FONT_MANAGER.hud.drawString(ping, 1, GuiScreen.height - 23, Colors.getColor());
		}
		
		if ((Boolean) tabGui.getValue()) {
			tab.drawTabGui();
		}

		if (arrayList.getValue()) {
			renderArrayList();
		}

	}

	public int getPing() {
		int ping = 0;
		try {
			ping = (int) mc.thePlayer.sendQueue.getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
			return ping;
		} catch (Exception x) {
			ping = 0;
		}
		return ping;
	}

	private void renderArrayList() {
		mods.clear();
		int count = 5;
		for (Module module : Envy.MODULE_MANAGER.getModules()) {
			if (!module.isToggled())
				continue;
			mods.add(module.getName() + " " + module.getMode());
		}
		Collections.sort(mods, new ComparatorStrings());
		for (String m : mods) {
			int x = (GuiScreen.width - 5) - (Envy.FONT_MANAGER.arraylist.getStringWidth(m));
			int y = count;
			int x1 = (GuiScreen.width - 3);
			int y1 = count + 5;
			// drawRect(x - 2 , y - 2 , x1, y1 + 3 , -1610612736);
			Envy.FONT_MANAGER.arraylist.drawString(m, x, y, Colors.getColor());
			count += 10;
		}
	}

}