package nivia.gui.aclickgui;

/**
 * Created by Apex on 8/19/2016.
 */
public abstract class Element {

    public float realHeight = 0;
    private float posX;
    private float posY;
    private float width;
    private float height;
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {}
    public void mouseClicked(int mouseX, int mouseY, int button) {}
    public void mouseReleased(int mouseX, int mouseY, int button) {}
    public void keyTyped(char typedChar, int keyCode) {}

    public float getRealHeight() {
        return realHeight;
    }
    public void setRealHeight(float realHeight) {
        this.realHeight = realHeight;
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
