package me.ihaq.iClient.utils;

import java.awt.Font;
import java.util.Comparator;

import me.ihaq.iClient.Envy;
import net.minecraft.client.Minecraft;

public class ComparatorStrings implements Comparator<String> {
	
	@Override
	public int compare(String o1, String o2) {
		if (Envy.FONT_MANAGER.hud.getStringWidth(o1) <= Envy.FONT_MANAGER.hud.getStringWidth(o2)) {
			return 1;
		} else if (Envy.FONT_MANAGER.hud.getStringWidth(o1) >= Envy.FONT_MANAGER.hud.getStringWidth(o2)) {
			return -1;
		} else {
			return 0;
		}
	}
}
