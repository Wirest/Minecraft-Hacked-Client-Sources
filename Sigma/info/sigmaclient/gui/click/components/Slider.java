package info.sigmaclient.gui.click.components;

import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.Client;
import info.sigmaclient.module.Module;

/**
 * Created by cool1 on 1/21/2017.
 */
public class Slider {

    public Module module;
    public float x;
    public float y;
    public String name;
    public Setting setting;
    public CategoryPanel panel;
    public boolean dragging;
    public double dragX;
    public double lastDragX;

    public Slider(CategoryPanel panel, float x, float y, Setting setting) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.setting = setting;
        panel.categoryButton.panel.theme.SliderContructor(this, panel);
    }

    public Slider(CategoryPanel panel, float x, float y, Setting setting, Module module) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.setting = setting;
        this.module = module;
        panel.categoryButton.panel.theme.SliderContructor(this, panel);
    }

    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.SliderDraw(this, x, y, this.panel);
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.SliderMouseClicked(this, x, y, button, this.panel);
        }
    }

    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.SliderMouseMovedOrUp(this, x, y, button, this.panel);
        }
    }

}
