// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component;

public abstract class Component<T>
{
    private T parent;
    private double x;
    private double y;
    private double width;
    private double height;
    
    public Component(final T parent, final double x, final double y, final double width, final double height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public abstract void draw(final int p0, final int p1);
    
    public abstract void click(final int p0, final int p1, final int p2);
    
    public abstract void drag(final int p0, final int p1, final int p2);
    
    public abstract void release(final int p0, final int p1, final int p2);
    
    public abstract void keyPress(final int p0, final char p1);
    
    public boolean hovering(final int mouseX, final int mouseY) {
        return mouseX > this.getX() && mouseX < this.getX() + this.getWidth() && mouseY > this.getY() && mouseY < this.getY() + this.getHeight();
    }
    
    public T getParent() {
        return this.parent;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getWidth() {
        return this.width;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public void setParent(final T parent) {
        this.parent = parent;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setWidth(final double width) {
        this.width = width;
    }
    
    public void setHeight(final double height) {
        this.height = height;
    }
}
