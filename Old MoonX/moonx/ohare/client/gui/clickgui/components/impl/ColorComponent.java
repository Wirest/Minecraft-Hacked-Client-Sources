package moonx.ohare.client.gui.clickgui.components.impl;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.ColorValue;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ColorComponent extends Component {
    private final ColorValue colorValue;
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
    public void init() {
        super.init();
    }

    @Override
    public void onFrameMoved(float x, float y) {
        super.onFrameMoved(x, y);
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        super.onDraw(mouseX, mouseY, partialTicks);
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
        Keyboard.enableRepeatEvents(true);
        hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(),getPosY(), getWidth(), getHeight());
        for (float i = 0; i < getWidth(); i++) {
            float posx = getPosX() + i;
            int color = Color.getHSBColor(i / 137, saturation, brightness).getRGB();
            RenderUtil.drawRect(posx, getPosY(), 1, getHeight(), color);
            if (mouseX == posx) {
                if (pressedhue) {
                    colorValue.setValue(color);
                    pos = i;
                }
            }
        }
        Minecraft.getMinecraft().fontRendererObj.drawString(getLabel(), getPosX() + getWidth() / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(getLabel()) / 2, getPosY() + 5, -1);
        RenderUtil.drawRect(getPosX() + pos, getPosY(), 2, getHeight(),  0xffffffff);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(),getPosY(), getWidth(), getHeight());
        if (button == 0) {
            if (hovered) {
                pressedhue = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        switch (button) {
            case 0:
                if (pressedhue) {
                    pressedhue = false;
                }
                break;
            default:
                break;
        }
    }

    public ColorValue getColorValue() {
        return colorValue;
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
}
