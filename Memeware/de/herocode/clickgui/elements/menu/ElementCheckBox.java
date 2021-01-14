package de.herocode.clickgui.elements.menu;

import de.herocode.clickgui.elements.Element;
import de.herocode.clickgui.elements.ModuleButton;
import de.herocode.clickgui.util.ColorUtil;
import de.herocode.clickgui.util.FontUtil;
import me.memewaredevs.client.option.BooleanOption;
import me.memewaredevs.client.option.Option;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * Made by HeroCode
 * it's free to use
 * but you have to credit me
 *
 * @author HeroCode
 */
public class ElementCheckBox extends Element {
    /*
     * Konstrukor
     */
    public ElementCheckBox(ModuleButton iparent, Option iset) {
        parent = iparent;
        set = iset;
        super.setup();
    }

    /*
     * Rendern des Elements
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        BooleanOption set = (BooleanOption) this.set;
        Color temp = ColorUtil.getClickGUIColor();
        int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
        Gui.drawRect(x, y, x + width, y + height, 0xff1a1a1a);

        FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg), y + FontUtil.getFontHeight() / 2 - 0.5, 0xffffffff);
        Gui.drawRect(x + 1, y + 2, x + 12, y + 13, set.getValue() ? color : 0xff000000);
        if (isCheckHovered(mouseX, mouseY))
            Gui.drawRect(x + 1, y + 2, x + 12, y + 13, 0x55111111);
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        BooleanOption set = (BooleanOption) this.set;
        if (mouseButton == 0 && isCheckHovered(mouseX, mouseY)) {
            set.setValue(!set.getValue());
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean isCheckHovered(int mouseX, int mouseY) {
        return mouseX >= x + 1 && mouseX <= x + 12 && mouseY >= y + 2 && mouseY <= y + 13;
    }
}
