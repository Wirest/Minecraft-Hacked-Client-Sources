package nivia.gui.chod.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import nivia.modules.Module;

import java.awt.*;

public abstract class ChodsComponent {

    protected Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer font;
    protected String title;
    protected int x, y;
    protected ChodsGuiScreen parent;
    protected int width, height;
    protected Module mod;

    public abstract void drawElement(int mouseX, int mouseY, float partialTicks);

    public abstract void onUpdate(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public void keyTyped(char typedChar, int keyCode) {}

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }
    public ChodsGuiScreen getParent() {
        return this.parent;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String getTitle() {
        return this.title;
    }
    public void setParent(ChodsGuiScreen parent) {
        this.parent = parent;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isInside(int pointX, int pointY) {
        return (pointX >= x && pointX <= x + width) && (pointY >= y && pointY <= y + height);
    }
    public boolean isInside(int pointX, int pointY, int x, int y, int width, int height) {
        return (pointX >= x && pointX <= x + width) && (pointY >= y && pointY <= y + height);
    }

    public Module getMod() {
        return mod;
    }
}
