package info.sigmaclient.gui.click.components;

import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.Client;

/**
 * Created by cool1 on 1/21/2017.
 */
public class CategoryButton {

    public float x;
    public float y;
    public String name;
    public MainPanel panel;
    public boolean enabled;
    public CategoryPanel categoryPanel;

    public CategoryButton(MainPanel panel, String name, float x, float y) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        panel.theme.categoryButtonConstructor(this, this.panel);
    }

    public void draw(final float x, final float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonDraw(this, x, y);
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseClicked(this, this.panel, x, y, button);
        }
    }

    public void mouseReleased(final int x, final int y, final int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseReleased(this, x, y, button);
        }
    }

}
