package dev.astroclient.client.ui.menu.click.component.components;

import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public abstract class Button extends Component {
    private float defaultHeight;

    public Button(Category parent, String label, float posX, float posY, float width, float height) {
        super(parent, label, posX, posY, width, height);
        this.defaultHeight = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(final char typedChar, final int key) {
    }

    public float getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(float defaultHeight) {
        this.defaultHeight = defaultHeight;
    }
}
