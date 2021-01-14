package moonx.ohare.client.gui.materialui.plane;

public class Plane {
    private String label;
    private float posX, posY, lastPosX, lastPosY, width, height;
    private boolean dragging;

    public Plane(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void initializePlane() {

    }

    public void planeMoved(float movedX, float movedY) {

    }

    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {

    }

    public void onMouseReleased(int mouseX, int mouesY, int button) {
    }

    public void onKeyTyped(char character, int keyCode) {

    }

    public void onGuiClosed() {

    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public float getLastPosX() {
        return lastPosX;
    }

    public void setLastPosX(float lastPosX) {
        this.lastPosX = lastPosX;
    }

    public float getLastPosY() {
        return lastPosY;
    }

    public void setLastPosY(float lastPosY) {
        this.lastPosY = lastPosY;
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
