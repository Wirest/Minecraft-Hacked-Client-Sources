package dev.astroclient.client.ui.menu.click.component;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public abstract class Component {

    private final Category parent;
    private final String label;
    private float posX, posY, width, height;
    public Component(Category parent, String label, float posX, float posY, float width, float height) {
        this.parent = parent;
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);


    public Category getParent() {
        return parent;
    }

    public String getLabel() {
        return label;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void keyTyped(final char typedChar, final int key) {
    }
    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
