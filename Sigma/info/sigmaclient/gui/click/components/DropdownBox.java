package info.sigmaclient.gui.click.components;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Options;

import java.util.ArrayList;

/**
 * Created by cool1 on 1/21/2017.
 */
public class DropdownBox {

    public Options option;
    public float x;
    public float y;
    public ArrayList<DropdownButton> buttons = new ArrayList();
    public CategoryPanel panel;
    public boolean active;
    public Module module;

    public DropdownBox(Options option, float x, float y, CategoryPanel panel, Module module) {
        this.option = option;
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.module = module;
        panel.categoryButton.panel.theme.dropDownContructor(this, x, y, this.panel);
    }

    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (panel.visible) {
                theme.dropDownDraw(this, x, y, this.panel);
            }
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownMouseClicked(this, x, y, button, this.panel);
        }
    }

}
