package com.etb.client.gui.clickui.component;

public class Component {
    private String name;
    private int posX, posY, width, height;

    public Component(String name, int posX, int posY, int width, int height) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void keyTyped(char character, int key)  {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {}

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
