package de.herocode.clickgui.elements;

import de.herocode.clickgui.ClickGUI;
import de.herocode.clickgui.util.FontUtil;
import me.memewaredevs.client.option.BooleanOption;
import me.memewaredevs.client.option.NumberOption;
import me.memewaredevs.client.option.Option;
import me.memewaredevs.client.option.StringOption;

/**
 * Made by HeroCode
 * it's free to use
 * but you have to credit me
 *
 * @author HeroCode
 */
public class Element {
    public ClickGUI clickgui;
    public ModuleButton parent;
    public Option set;
    public double offset;
    public double x;
    public double y;
    public double width;
    public double height;

    public String setstrg;

    public boolean comboextended;

    public void setup() {
        clickgui = parent.parent.clickgui;
    }

    public void update() {
        /*
         * Richtig positionieren! Offset wird von ClickGUI aus bestimmt, sodass
         * nichts ineinander gerendert wird
         */
        x = parent.x + parent.width + 2;
        y = parent.y + offset;
        width = parent.width + 10;
        height = 15;

        /*
         * Title der Box bestimmen und falls n�tig die Breite der CheckBox
         * erweitern
         */
        String sname = set.getName();
        if (set instanceof BooleanOption) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            double textx = x + width - FontUtil.getStringWidth(this.setstrg);
            if (textx < x + 13) {
                width += (x + 13) - textx + 1;
            }
        } else if (set instanceof StringOption) {
            StringOption set = (StringOption) this.set;
            height = comboextended ? set.getValues().size() * (FontUtil.getFontHeight() + 2) + 15 : 15;

            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            int longest = FontUtil.getStringWidth(this.setstrg);
            for (String s : set.getValues()) {
                int temp = FontUtil.getStringWidth(s);
                if (temp > longest) {
                    longest = temp;
                }
            }
            double textx = x + width - longest;
            if (textx < x) {
                width += x - textx + 1;
            }
        } else if (set instanceof NumberOption) {
            NumberOption set = (NumberOption) this.set;
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            String displayval = "" + Math.round(set.getValue() * 100D) / 100D;
            String displaymax = "" + Math.round(set.getMax() * 100D) / 100D;
            double textx = x + width - FontUtil.getStringWidth(this.setstrg) - FontUtil.getStringWidth(displaymax) - 4;
            if (textx < x) {
                width += x - textx + 1;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return isHovered(mouseX, mouseY);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public boolean isHovered(int mouseX, int mouseY) {

        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
