// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui.util;

import me.nico.hush.Client;
import java.awt.Color;

public class ColorUtil
{
    public static Color getClickGUIColor() {
        return new Color((int)Client.instance.settingManager.getSettingByName("Red").getValDouble(), (int)Client.instance.settingManager.getSettingByName("Green").getValDouble(), (int)Client.instance.settingManager.getSettingByName("Blue").getValDouble());
    }
}
