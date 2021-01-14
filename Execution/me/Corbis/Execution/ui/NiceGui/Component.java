package me.Corbis.Execution.ui.NiceGui;

import de.Hero.settings.Setting;
import me.Corbis.Execution.ui.UnicodeFontRenderer;

public class Component {

    int x, y;
    Setting set;
    SettingsWindow parent;

    public Component(int x, int y, Setting set, SettingsWindow parent) {
        this.x = x;
        this.y = y;
        this.set = set;
        this.parent = parent;
    }

    public void draw(UnicodeFontRenderer ufr, int mouseX, int mouseY){}

    public void mouseClicked(int mouseX, int mouseY){}

    public void mouseRelease(){}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Setting getSet() {
        return set;
    }

    public void setSet(Setting set) {
        this.set = set;
    }

    public SettingsWindow getParent() {
        return parent;
    }

    public void setParent(SettingsWindow parent) {
        this.parent = parent;
    }
}
