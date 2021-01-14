package moonx.ohare.client.gui.clickgui.components.impl;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends Component {
    private final NumberValue numberValue;
    private boolean sliding;
    public NumberComponent(NumberValue numberValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;
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
        final float length = MathHelper.floor_double(((numberValue.getValue()).floatValue() - numberValue.getMinimum().floatValue()) / (numberValue.getMaximum().floatValue() - numberValue.getMinimum().floatValue()) * (getWidth() - 6));
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
        RenderUtil.drawRect(getPosX() + 2, getPosY() + 2, getWidth() - 4, getHeight() - 4, new Color(5, 5, 5, 255).getRGB());
        RenderUtil.drawRect(getPosX() + 3, getPosY() + 3, length, getHeight() - 6, 0xffF136DB);
        if (sliding) {
            if (numberValue.getValue() instanceof Double) {
                numberValue.setValue(round(((mouseX - (getPosX() + 3)) * (numberValue.getMaximum().doubleValue() - numberValue.getMinimum().doubleValue()) / (getWidth() - 6) + numberValue.getMinimum().doubleValue()), 2));
            } else if (numberValue.getValue() instanceof Float) {
                numberValue.setValue((float) round(((mouseX - (getPosX() + 3)) * (numberValue.getMaximum().floatValue() - numberValue.getMinimum().floatValue()) / (getWidth() - 6) + numberValue.getMinimum().floatValue()), 2));
            } else if (numberValue.getValue() instanceof Long) {
                numberValue.setValue((long) round(((mouseX - (getPosX() + 3)) * (numberValue.getMaximum().longValue() - numberValue.getMinimum().longValue()) / (getWidth() - 6) + numberValue.getMinimum().longValue()), 2));
            } else if (numberValue.getValue() instanceof Integer) {
                numberValue.setValue((int) round((((mouseX - getPosX() + 3)) * (numberValue.getMaximum().intValue() - numberValue.getMinimum().intValue()) / (getWidth() - 6) + numberValue.getMinimum().intValue()), 2));
            } else if (numberValue.getValue() instanceof Short) {
                numberValue.setValue((short) round((((mouseX - getPosX() + 3)) * (numberValue.getMaximum().shortValue() - numberValue.getMinimum().shortValue()) / (getWidth() - 6) + numberValue.getMinimum().shortValue()), 2));
            } else if (numberValue.getValue() instanceof Byte) {
                numberValue.setValue((byte) round((((mouseX - getPosX() + 3)) * (numberValue.getMaximum().byteValue() - numberValue.getMinimum().byteValue()) / (getWidth() - 6) + numberValue.getMinimum().byteValue()), 2));
            }
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.8f, 0.8f, 0.8f);
        Minecraft.getMinecraft().fontRendererObj.drawString(getLabel() + ": " + getNumberValue().getValue(), (getPosX() + (getWidth() / 2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(getLabel() + ": " + numberValue.getValue()) / 2) * 1.25f + 8, (getPosY() + 5)*1.25f, -1);
        GL11.glPopMatrix();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 2, getPosY() + 2, getWidth() - 4, getHeight() - 4);
        if (button == 0) {
            if (hovered) {
                sliding = true;
            }
        }
    }
    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && sliding) sliding = false;
    }
    private double round(final double val, final int places) {
        final double v = Math.round(val / numberValue.getInc().doubleValue()) * numberValue.getInc().doubleValue();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public NumberValue getNumberValue() {
        return numberValue;
    }
    
}
