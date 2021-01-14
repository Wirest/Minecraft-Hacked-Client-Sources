package de.Hero.clickgui.util;

import java.awt.Color;

import cheatware.Cheatware;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int) Cheatware.instance.settingsManager.getSettingByName("GuiRed").getValDouble(), (int)Cheatware.instance.settingsManager.getSettingByName("GuiGreen").getValDouble(), (int)Cheatware.instance.settingsManager.getSettingByName("GuiBlue").getValDouble());
	}
}
