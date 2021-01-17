// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiVideoSettings;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class TooltipManager
{
    private GuiScreen guiScreen;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public TooltipManager(final GuiScreen p_i97_1_) {
        this.guiScreen = null;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.guiScreen = p_i97_1_;
    }
    
    public void drawTooltips(final int p_drawTooltips_1_, final int p_drawTooltips_2_, final List p_drawTooltips_3_) {
        if (Math.abs(p_drawTooltips_1_ - this.lastMouseX) <= 5 && Math.abs(p_drawTooltips_2_ - this.lastMouseY) <= 5) {
            final int i = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + i) {
                final int j = this.guiScreen.width / 2 - 150;
                int k = this.guiScreen.height / 6 - 7;
                if (p_drawTooltips_2_ <= k + 98) {
                    k += 105;
                }
                final int l = j + 150 + 150;
                final int i2 = k + 84 + 10;
                final GuiButton guibutton = this.getSelectedButton(p_drawTooltips_1_, p_drawTooltips_2_, p_drawTooltips_3_);
                if (guibutton instanceof IOptionControl) {
                    final IOptionControl ioptioncontrol = (IOptionControl)guibutton;
                    final GameSettings.Options gamesettings$options = ioptioncontrol.getOption();
                    final String[] astring = getTooltipLines(gamesettings$options);
                    if (astring == null) {
                        return;
                    }
                    GuiVideoSettings.drawGradientRect(this.guiScreen, j, k, l, i2, -536870912, -536870912);
                    for (int j2 = 0; j2 < astring.length; ++j2) {
                        final String s = astring[j2];
                        int k2 = 14540253;
                        if (s.endsWith("!")) {
                            k2 = 16719904;
                        }
                        final FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
                        fontrenderer.drawStringWithShadow(s, (float)(j + 5), (float)(k + 5 + j2 * 11), k2);
                    }
                }
            }
        }
        else {
            this.lastMouseX = p_drawTooltips_1_;
            this.lastMouseY = p_drawTooltips_2_;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }
    
    private GuiButton getSelectedButton(final int p_getSelectedButton_1_, final int p_getSelectedButton_2_, final List p_getSelectedButton_3_) {
        for (int i = 0; i < p_getSelectedButton_3_.size(); ++i) {
            final GuiButton guibutton = p_getSelectedButton_3_.get(i);
            final int j = GuiVideoSettings.getButtonWidth(guibutton);
            final int k = GuiVideoSettings.getButtonHeight(guibutton);
            final boolean flag = p_getSelectedButton_1_ >= guibutton.xPosition && p_getSelectedButton_2_ >= guibutton.yPosition && p_getSelectedButton_1_ < guibutton.xPosition + j && p_getSelectedButton_2_ < guibutton.yPosition + k;
            if (flag) {
                return guibutton;
            }
        }
        return null;
    }
    
    private static String[] getTooltipLines(final GameSettings.Options p_getTooltipLines_0_) {
        return getTooltipLines(p_getTooltipLines_0_.getEnumString());
    }
    
    private static String[] getTooltipLines(final String p_getTooltipLines_0_) {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; ++i) {
            final String s = String.valueOf(p_getTooltipLines_0_) + ".tooltip." + (i + 1);
            final String s2 = Lang.get(s, null);
            if (s2 == null) {
                break;
            }
            list.add(s2);
        }
        if (list.size() <= 0) {
            return null;
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
}
