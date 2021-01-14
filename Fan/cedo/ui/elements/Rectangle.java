package cedo.ui.elements;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

public class Rectangle extends GuiScreen { // TODO Slightly WIP, needs application and testing | have a y2 setter, in order to have functionality to match normal lower level rectangles.

    protected double x, y, width, height;
    protected int mouseStartX, mouseStartY, mouseStartButton = -1;
    protected boolean hidden, draggable, dragging, mouseDown, childrenHidden;
    protected Color color;
    protected ArrayList<Rectangle> children = new ArrayList<>();
    protected double[] dragOffset;

    //TODO make child based off an offset instead of always below

    public Rectangle(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Rectangle() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.color = new Color(0, 0, 0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        for (Rectangle child : children) {
            child.setX(x);
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        for (Rectangle child : children) {
            child.setY(y + height);
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        double currentY = y + height;
        for (Rectangle child : children) {
            child.setY(currentY);
            currentY += child.getHeight();
        }
    }

    public boolean isHidden() {
        return hidden;

    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
//		if(hidden) //Commented out as it is unnecessary as the parent will not call the child's draw method if parent is hidden. However, it could be cool to uncomment this code in order to be able to quickly collapse all children by collapsing the parent.
//			hideChildren();
    }

    public void setChildrenHidden(boolean hidden) {
        childrenHidden = hidden;
        for (Rectangle child : children) {
            child.setHidden(hidden);
        }
    }

    public boolean isDraggable() {
        return draggable;
    }

    public Rectangle setDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public Rectangle setParent(Rectangle parent) {
        parent.addChild(this);
        return this;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Rectangle> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Rectangle> children) {
        this.children = children;
        double currentY = this.y + height;
        for (Rectangle child : children) {
            child.setX(this.x);
            child.setY(currentY);
            currentY += child.getHeight();
            child.setHidden(childrenHidden);
            child.setDraggable(false);
        }
    }

    public void addChild(Rectangle child) {
        double currentY = y + height;
        for (Rectangle child1 : children) {
            currentY += child1.getHeight();
        }
        this.children.add(child);
        child.setY(currentY);
        child.setX(x);
        child.setHidden(childrenHidden);
        child.setDraggable(false);
    }

    public void draw(int mouseX, int mouseY) {
        if (!hidden) {
            for (Rectangle child : children) {
                child.draw(mouseX, mouseY);
            }

            if (dragging) {
                setX(mouseX - mouseStartX);
                setY(mouseY - mouseStartY);
            }
            drawRect(x, y, x + width, y + height, color.getRGB());
            double[] relativeCoords = getRelativeCoords(mouseX, mouseY);
            if (draggable && mouseStartButton == 0 && Math.pow(mouseStartX - relativeCoords[0], 2) + Math.pow(mouseStartY - relativeCoords[1], 2) > 2) {
                dragging = true;
            }
        }
    }

    public boolean isInside(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    public double[] getCenter() {
        return new double[]{x + (width / 2), y + (height / 2)};
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        for (Rectangle child : children) {
            child.mouseClicked(mouseX, mouseY, button);
        }

        mouseDown = true;
        if (isInside(mouseX, mouseY)) {
            double[] relativeCoords = getRelativeCoords(mouseX, mouseY);
            mouseStartX = (int) relativeCoords[0];
            mouseStartY = (int) relativeCoords[1];
            mouseStartButton = button;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        for (Rectangle child : children) {
            child.mouseReleased(mouseX, mouseY, button);
        }

        mouseDown = false;
        if (!dragging && isInside(mouseX, mouseY)) {
            onClick();
        }

        dragging = false;
        mouseStartX = -1;
        mouseStartY = -1;
        mouseStartButton = -1;
    }

    public void onClick() {
    }

    protected double[] getRelativeCoords(int x, int y) {
        //if(!isPosInRectangle(x, y))
        //	return null;
        return new double[]{(double) x - this.x, (double) y - this.y};
    }
}
