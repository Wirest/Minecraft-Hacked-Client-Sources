package moonx.ohare.client.gui.clickgui.components;

import java.util.ArrayList;

public class Component {
    private float posX, posY, offsetX, offsetY, width, height;
    private String label;
    private ArrayList<Component> subComponents = new ArrayList<>();
    private boolean hidden = false;
    public Component(String label, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        this.label = label;
        this.posX = posX + offsetX;
        this.posY = posY + offsetY;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void init() {
        subComponents.forEach(Component::init);
    }

    public void onFrameMoved(float x, float y) {
        this.posX = x + offsetX;
        this.posY = y + offsetY;
        subComponents.forEach(subComponents -> subComponents.onFrameMoved(getPosX(), getPosY()));
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
    }

    public void onMouseReleased(int mouseX, int mouseY, int button) {
    }

    public void onKeyTyped(char character, int key) {
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<Component> getSubComponents() {
        return subComponents;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
