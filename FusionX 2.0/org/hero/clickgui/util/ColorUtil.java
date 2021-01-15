// 
// Decompiled by Procyon v0.5.30
// 

package org.hero.clickgui.util;

import me.CheerioFX.FusionX.FusionX;
import java.awt.Color;

public class ColorUtil
{
    public static Color getClickGUIColor() {
        return new Color((int)FusionX.theClient.setmgr.getSettingByName("GuiRed").getValDouble(), (int)FusionX.theClient.setmgr.getSettingByName("GuiGreen").getValDouble(), (int)FusionX.theClient.setmgr.getSettingByName("GuiBlue").getValDouble());
    }
}
