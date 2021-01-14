package info.sigmaclient.gui.click.components;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Rotate;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.module.Module;

/**
 * Created by cool1 on 1/21/2017.
 */
public class Button {

    public Rotate rotate = new Rotate(0);
    public Translate translate = new Translate(0, 0);
    public float x;
    public float y;
    public String name;
    public CategoryPanel panel;
    public boolean enabled;
    public Module module;
    public boolean isBinding;

    public Button(final CategoryPanel panel, final String name, final float x, final float y, Module module) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.module = module;
        this.enabled = module.isEnabled();
        panel.categoryButton.panel.theme.buttonContructor(this, this.panel);
    }

    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (panel.visible) {
                theme.buttonDraw(this, x, y, this.panel);
            }
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.buttonMouseClicked(this, x, y, button, this.panel);
        }
    }

    public void keyPressed(int key) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.buttonKeyPressed(this, key);
        }
    }

}
