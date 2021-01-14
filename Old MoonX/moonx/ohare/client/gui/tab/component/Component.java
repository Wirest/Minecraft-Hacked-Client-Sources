package moonx.ohare.client.gui.tab.component;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.module.impl.visuals.HUD;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class Component {
    private float posX, posY, width,height;
    private String label;
    public HUD hud;
    private boolean hidden = false;
    public Component(String label,float posX,float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
    }

    public void init() {

    }

    public void onDraw(ScaledResolution scaledResolution) {

    }

    public void onKeyPress(int key) {

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
