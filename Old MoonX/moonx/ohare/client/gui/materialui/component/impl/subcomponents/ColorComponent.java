package moonx.ohare.client.gui.materialui.component.impl.subcomponents;

import moonx.ohare.client.gui.materialui.component.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.Fonts;
import moonx.ohare.client.utils.value.impl.ColorValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ColorComponent extends Component {
    private ColorValue colorValue;
    private boolean pressedhue;
    private float pos, saturation, brightness;
    private boolean hovered;
    public ColorComponent(ColorValue colorValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(colorValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.colorValue = colorValue;
        float[] hsb = new float[3];
        final Color clr = new Color(colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        pos = 0;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        Fonts.clickGuiFont.drawStringWithShadow(getLabel(), getPosX(), getPosY() + 3, new Color(229, 229, 223, 255).getRGB());
        Keyboard.enableRepeatEvents(true);
        hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + getWidth() - 100,getPosY() + 4.0f, getWidth() - 70, 4);
        for (float i = 0; i < getWidth() - 70; i++) {
            int color = Color.getHSBColor(i / (getWidth() - 70), saturation, brightness).getRGB();
            RenderUtil.drawRect((getPosX() + getWidth() - 100) + i, getPosY() + 4.0f, 1, 4, color);
            if (mouseX == (int)(getPosX() + getWidth() - 100) + i) {
                if (pressedhue) {
                    colorValue.setValue(color);
                    pos = i;
                }
            }
        }
        RenderUtil.drawRect(getPosX() + getWidth() - 100 + pos, getPosY() + 3.5f, 1, 4.5f,  0xffffffff);
        RenderUtil.drawOutlinedRoundedRect(getPosX()+ getWidth() - 100.5f,getPosY() + 3.5f, getWidth() - 69,5,3,2,new Color(45, 45, 45, 255).getRGB());
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX()+ getWidth() - 100.5f,getPosY() + 3f, getWidth() - 69,5);
        if (button == 0) {
            if (hovered) {
                pressedhue = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            if (pressedhue) {
                pressedhue = false;
            }
        }
    }
    @Override
    public void onKeyTyped(char keyChar, int key) {
        super.onKeyTyped(keyChar, key);
        if (!hovered) return;
        switch (key) {
            case Keyboard.KEY_UP:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness + 0.01 <= 1) brightness += 0.01;
                } else {
                    if (saturation + 0.01 <= 1) saturation += 0.01;
                }
                break;
            case Keyboard.KEY_DOWN:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness - 0.01 >= 0) brightness -= 0.01;
                } else {
                    if (saturation - 0.01 >= 0) saturation -= 0.01;
                }
                break;
        }
    }
    public ColorValue getColorValue() {
        return colorValue;
    }
}
