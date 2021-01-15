package com.ihl.client.gui;

public abstract class Component {

    public abstract void tick();

    public abstract void keyPress(int k, char c);
    public abstract void keyRelease(int k, char c);

    public abstract void mouseClicked(int button);
    public abstract void mouseReleased(int button);

    protected boolean mouseOver(double xx, double yy, double x1, double y1, double x2, double y2) {
        return xx >= x1 && yy >= y1 && xx < x2 && yy < y2;
    }

    public abstract void render();

}
