package de.herocode.clickgui.elements;

import de.herocode.clickgui.Panel;
import de.herocode.clickgui.elements.menu.ElementCheckBox;
import de.herocode.clickgui.elements.menu.ElementComboBox;
import de.herocode.clickgui.elements.menu.ElementSlider;
import de.herocode.clickgui.util.ColorUtil;
import de.herocode.clickgui.util.FontUtil;
import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.option.BooleanOption;
import me.memewaredevs.client.option.NumberOption;
import me.memewaredevs.client.option.Option;
import me.memewaredevs.client.option.StringOption;
import me.memewaredevs.client.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Made by HeroCode
 * it's free to use
 * but you have to credit me
 *
 * @author HeroCode
 */
public class ModuleButton {
    public Module mod;
    public ArrayList<Element> menuelements;
    public de.herocode.clickgui.Panel parent;
    public double x;
    public double y;
    public double width;
    public double height;
    public boolean extended = false;
    public boolean listening = false;

    /*
     * Konstrukor
     */
    public ModuleButton(Module imod, Panel pl) {
        mod = imod;
        height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
        parent = pl;
        menuelements = new ArrayList<>();
        if (Memeware.INSTANCE.getSettingsManager().getSettingsByMod(imod) != null)
            for (Option s : Memeware.INSTANCE.getSettingsManager().getSettingsByMod(imod)) {
                if (s instanceof BooleanOption) {
                    menuelements.add(new ElementCheckBox(this, s));
                } else if (s instanceof NumberOption) {
                    menuelements.add(new ElementSlider(this, s));
                } else if (s instanceof StringOption) {
                    menuelements.add(new ElementComboBox(this, s));
                }
            }

    }

    /*
     * Rendern des Elements
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Color temp = ColorUtil.getClickGUIColor();
        int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();

        /*
         * Ist das Module an, wenn ja dann soll
         *  #ein neues Rechteck in Gr��e des Buttons den Knopf als Toggled kennzeichnen
         *  #sich der Text anders f�rben
         */
        int textcolor = 0xffafafaf;
        if (mod.isToggled()) {
            Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, color);
            textcolor = 0xffefefef;
        }

        /*
         * Ist die Maus �ber dem Element, wenn ja dann soll der Button sich anders f�rben
         */
        if (isHovered(mouseX, mouseY)) {
            Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, 0x55111111);
        }

        /*
         * Den Namen des Modules in die Mitte (x und y) rendern
         */
        FontUtil.drawTotalCenteredStringWithShadow(mod.getName(), x + width / 2, y + 1 + height / 2, textcolor);
    }

    /*
     * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
     * sollen alle anderen Versuche der Interaktion abgebrochen werden?
     */
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;

        /*
         * Rechtsklick, wenn ja dann Module togglen,
         */
        if (mouseButton == 0) {
            mod.toggle();
        } else if (mouseButton == 1) {
            /*
             * Wenn ein Settingsmenu existiert dann sollen alle Settingsmenus
             * geschlossen werden und dieses ge�ffnet/geschlossen werden
             */
            if (menuelements != null && menuelements.size() > 0) {
                boolean b = !this.extended;
                this.extended = b;
                if (extended) {
                    Minecraft.getMinecraft().thePlayer.playSound("tile.piston.out", 1f, 1f);
                } else {
                    Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 1f, 1f);
                }
            }
        } else if (mouseButton == 2) {
            /*
             * MidClick => Set keybind (wait for next key)
             */
            listening = true;
        }
        return true;
    }

    public boolean keyTyped(char typedChar, int keyCode) throws IOException {
        /*
         * Wenn listening, dann soll der n�chster Key (abgesehen 'ESCAPE') als Keybind f�r mod
         * danach soll nicht mehr gewartet werden!
         */
        if (listening) {
            if (keyCode != Keyboard.KEY_ESCAPE) {
                ChatUtil.printChat("Bound '" + mod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
                mod.setKeyBind(keyCode);
            } else {
                ChatUtil.printChat("Unbound '" + mod.getName() + "'");
                mod.setKeyBind(Keyboard.KEY_NONE);
            }
            listening = false;
            return true;
        }
        return false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

}
