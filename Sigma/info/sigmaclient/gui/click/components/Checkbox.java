package info.sigmaclient.gui.click.components;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;

/**
 * Created by cool1 on 1/21/2017.
 */
public class Checkbox {


    public Translate translate = new Translate(0, 0);
    public Expand expand = new Expand(0, 0, 0, 0);
    public CategoryPanel panel;
    public Module module;
    public boolean enabled;
    public float x;
    public float y;
    public String name;
    public Setting setting;

    public Checkbox(CategoryPanel panel, String name, float x, float y, Setting setting) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.setting = setting;
        this.enabled = ((Boolean) setting.getValue());
    }

    public Checkbox(CategoryPanel panel, String name, float x, float y, Setting setting, Module module) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.setting = setting;
        this.enabled = ((Boolean) setting.getValue());
        this.module = module;
    }

    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (panel.visible) {
                theme.checkBoxDraw(this, x, y, this.panel);
            }
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.checkBoxMouseClicked(this, x, y, button, this.panel);
        }
    }

}
