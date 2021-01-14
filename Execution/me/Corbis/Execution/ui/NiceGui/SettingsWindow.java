package me.Corbis.Execution.ui.NiceGui;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class SettingsWindow {
    int x = 200;
    int y = 100;
    Button parent;
    ArrayList<Component> components = new ArrayList<>();
    int yCount = 0;
    public SettingsWindow(Button parent) {
        yCount = 0;
        this.parent = parent;
        int count = 0;
        for(Setting set : Execution.instance.settingsManager.getSettingsByMod(parent.mod)){
            if(set.isCheck()){
                this.components.add(new CheckBox(x + 5, y + 5 + count * 20, set, this));

            }else if(set.isCombo()){
                this.components.add(new ComboBox(x + 5, y + 5 + count * 20, set, this));

            }else if(set.isSlider()){
                this.components.add(new Slider(x + 5, y + 5 + count * 20, set, this));

            }
            count++;

        }
    }

    public void draw(UnicodeFontRenderer ufr, int mouseX, int mouseY){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        RenderingUtil.drawRoundedRect(x, y, sr.getScaledWidth() - x, sr.getScaledHeight() - y, 5, new Color(240, 240, 240, 255));

        for(Component c : components){
            c.draw(ufr, mouseX, mouseY);
        }
    }

    public void mouseClicked(int mouseX, int mouseY){
        for(Component c : components){
            c.mouseClicked(mouseX, mouseY);
        }
    }

    public void mouseReleased(){
        for(Component c : components){
            c.mouseRelease();
        }
    }

    public Button getParent() {
        return parent;
    }

    public void setParent(Button parent) {
        this.parent = parent;
    }
}
