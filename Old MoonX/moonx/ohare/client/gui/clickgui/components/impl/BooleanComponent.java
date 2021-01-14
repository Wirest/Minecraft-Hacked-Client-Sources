package moonx.ohare.client.gui.clickgui.components.impl;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class BooleanComponent extends Component {
    private final BooleanValue booleanValue;
    public BooleanComponent(BooleanValue booleanValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.booleanValue = booleanValue;
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
        RenderUtil.drawBorderedRect(getPosX() + 2, getPosY() + 2, 12,12,0.5f, new Color(0,0,0, 255).getRGB(),new Color(5,5,5, 255).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(getLabel(), getPosX() + 16, getPosY() + 6, -1);
        if (getBooleanValue().isEnabled())
            RenderUtil.drawCheckMark(getPosX() + 8, getPosY() + 2,10,0xffF136DB);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 2, getPosY() + 2, 12,12);
        if (button == 0) {
            if (hovered) {
                getBooleanValue().setEnabled(!getBooleanValue().isEnabled());
            }
        }
    }

    public BooleanValue getBooleanValue() {
        return booleanValue;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }
}
