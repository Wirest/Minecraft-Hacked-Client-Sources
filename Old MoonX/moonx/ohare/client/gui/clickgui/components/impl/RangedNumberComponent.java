package moonx.ohare.client.gui.clickgui.components.impl;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.RangedValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class RangedNumberComponent extends Component {
    private final RangedValue rangedValue;
    private boolean leftDown, rightDown;

    public RangedNumberComponent(RangedValue rangedValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(rangedValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.rangedValue = rangedValue;
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
        final float startX = getPosX() + 3;
        final float sliderwidth = getWidth() - 9;
        final float leftX = MathHelper.floor_double((rangedValue.getLeftVal().floatValue() - rangedValue.getMinimum().floatValue()) / (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) * sliderwidth);
        final float rightX = MathHelper.floor_double((rangedValue.getRightVal().floatValue() - rangedValue.getMinimum().floatValue()) / (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) * sliderwidth);
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
        RenderUtil.drawRect(getPosX() + 2, getPosY() + 2, getWidth() - 4, getHeight() - 4, new Color(5, 5, 5, 255).getRGB());
        RenderUtil.drawRect(startX + leftX, getPosY() + 3, rightX - leftX, getHeight() - 6, 0xffF136DB);
        RenderUtil.drawRect(startX + leftX, getPosY() + 3, 3, getHeight() - 6, new Color(0xffF136DB).darker().getRGB());
        RenderUtil.drawRect(startX + rightX, getPosY() + 3, 3, getHeight() - 6,  new Color(0xffF136DB).darker().getRGB());
        if (leftDown) {
            if (rangedValue.getLeftVal() instanceof Double) {
                rangedValue.setLeftVal(round(((mouseX - startX) * (rangedValue.getMaximum().doubleValue() - rangedValue.getMinimum().doubleValue()) / sliderwidth + rangedValue.getMinimum().doubleValue())));
            }
            if (rangedValue.getLeftVal() instanceof Float) {
                rangedValue.setLeftVal((float) round(((mouseX - startX) * (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) / sliderwidth + rangedValue.getMinimum().floatValue())));
            }
            if (rangedValue.getLeftVal() instanceof Long) {
                rangedValue.setLeftVal((long) round(((mouseX - startX) * (rangedValue.getMaximum().longValue() - rangedValue.getMinimum().longValue()) / sliderwidth + rangedValue.getMinimum().longValue())));
            }
            if (rangedValue.getLeftVal() instanceof Integer) {
                rangedValue.setLeftVal((int) round(((mouseX - startX) * (rangedValue.getMaximum().intValue() - rangedValue.getMinimum().intValue()) / sliderwidth + rangedValue.getMinimum().intValue())));
            }
            if (rangedValue.getLeftVal() instanceof Short) {
                rangedValue.setLeftVal((short) round(((mouseX - startX) * (rangedValue.getMaximum().shortValue() - rangedValue.getMinimum().shortValue()) / sliderwidth + rangedValue.getMinimum().shortValue())));
            }
            if (rangedValue.getLeftVal() instanceof Byte) {
                rangedValue.setLeftVal((byte) round(((mouseX - startX) * (rangedValue.getMaximum().byteValue() - rangedValue.getMinimum().byteValue()) / sliderwidth + rangedValue.getMinimum().byteValue())));
            }
        }
        if (rightDown) {
            if (rangedValue.getRightVal() instanceof Double) {
                rangedValue.setRightVal(round(((mouseX - startX) * (rangedValue.getMaximum().doubleValue() - rangedValue.getMinimum().doubleValue()) / sliderwidth + rangedValue.getMinimum().doubleValue())));
            }
            if (rangedValue.getRightVal() instanceof Float) {
                rangedValue.setRightVal((float) round(((mouseX - startX) * (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) / sliderwidth + rangedValue.getMinimum().floatValue())));
            }
            if (rangedValue.getRightVal() instanceof Long) {
                rangedValue.setRightVal((long) round(((mouseX - startX) * (rangedValue.getMaximum().longValue() - rangedValue.getMinimum().longValue()) / sliderwidth + rangedValue.getMinimum().longValue())));
            }
            if (rangedValue.getRightVal() instanceof Integer) {
                rangedValue.setRightVal((int) round(((mouseX - startX) * (rangedValue.getMaximum().intValue() - rangedValue.getMinimum().intValue()) / sliderwidth + rangedValue.getMinimum().intValue())));
            }
            if (rangedValue.getRightVal() instanceof Short) {
                rangedValue.setRightVal((short) round(((mouseX - startX) * (rangedValue.getMaximum().shortValue() - rangedValue.getMinimum().shortValue()) / sliderwidth + rangedValue.getMinimum().shortValue())));
            }
            if (rangedValue.getRightVal() instanceof Byte) {
                rangedValue.setRightVal((byte) round(((mouseX - startX) * (rangedValue.getMaximum().byteValue() - rangedValue.getMinimum().byteValue()) / sliderwidth + rangedValue.getMinimum().byteValue())));
            }
        }
        if (rangedValue.getInc() instanceof Double) {
            if (leftDown) {
                if (rangedValue.getLeftVal().doubleValue() > rangedValue.getRightVal().doubleValue() - rangedValue.getInc().doubleValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().doubleValue() - rangedValue.getInc().doubleValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().doubleValue() < rangedValue.getLeftVal().doubleValue() + rangedValue.getInc().doubleValue())
                    rangedValue.setRightVal(rangedValue.getLeftVal().doubleValue() + rangedValue.getInc().doubleValue());
            }
        }
        if (rangedValue.getInc() instanceof Float) {
            if (leftDown) {
                if (rangedValue.getLeftVal().floatValue() > rangedValue.getRightVal().floatValue() - rangedValue.getInc().floatValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().floatValue() - rangedValue.getInc().floatValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().floatValue() < rangedValue.getLeftVal().floatValue() + rangedValue.getInc().floatValue())
                    rangedValue.setRightVal(rangedValue.getLeftVal().floatValue() + rangedValue.getInc().floatValue());
            }
        }
        if (rangedValue.getInc() instanceof Long) {
            if (leftDown) {
                if (rangedValue.getLeftVal().longValue() > rangedValue.getRightVal().longValue() - rangedValue.getInc().longValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().longValue() - rangedValue.getInc().longValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().longValue() < rangedValue.getLeftVal().longValue() + rangedValue.getValue().longValue())
                    rangedValue.setRightVal(rangedValue.getLeftVal().longValue() + rangedValue.getInc().longValue());
            }
        }
        if (rangedValue.getInc() instanceof Integer) {
            if (leftDown) {
                if (rangedValue.getLeftVal().intValue() > rangedValue.getRightVal().intValue() - rangedValue.getInc().intValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().intValue() - rangedValue.getInc().intValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().intValue() < rangedValue.getLeftVal().intValue() + rangedValue.getInc().intValue())
                    rangedValue.setRightVal(rangedValue.getLeftVal().intValue() + rangedValue.getInc().intValue());
            }
        }
        if (rangedValue.getInc() instanceof Short) {
            if (leftDown) {
                if (rangedValue.getLeftVal().shortValue() > rangedValue.getRightVal().shortValue() - rangedValue.getInc().shortValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().shortValue() - rangedValue.getInc().shortValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().shortValue() < rangedValue.getLeftVal().shortValue() + rangedValue.getInc().shortValue())
                    rangedValue.setRightVal((short) (rangedValue.getLeftVal().shortValue() + rangedValue.getInc().shortValue()));
            }
        }
        if (rangedValue.getInc() instanceof Byte) {
            if (leftDown) {
                if (rangedValue.getLeftVal().byteValue() > rangedValue.getRightVal().byteValue() - rangedValue.getInc().byteValue())
                    rangedValue.setLeftVal(rangedValue.getRightVal().byteValue() - rangedValue.getInc().byteValue());
            }
            if (rightDown) {
                if (rangedValue.getRightVal().byteValue() < rangedValue.getLeftVal().byteValue() + rangedValue.getInc().byteValue())
                    rangedValue.setRightVal(rangedValue.getLeftVal().byteValue() + rangedValue.getInc().byteValue());
            }
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.8f, 0.8f, 0.8f);
        Minecraft.getMinecraft().fontRendererObj.drawString(getLabel() + ": " + rangedValue.getLeftVal() + "-" + rangedValue.getRightVal(), (getPosX() + (getWidth() / 2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(getLabel() + ": " + rangedValue.getLeftVal() + "-" + rangedValue.getRightVal()) / 2) * 1.25f + 8, (getPosY() + 5) * 1.25f, -1);
        GL11.glPopMatrix();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            if (hoveredLeft(mouseX, mouseY)) leftDown = true;
            else if (hoveredRight(mouseX, mouseY)) rightDown = true;
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0)
            leftDown = rightDown = false;
    }

    private boolean hoveredLeft(int mouseX, int mouseY) {
        final float leftX = MathHelper.floor_double((rangedValue.getLeftVal().floatValue() - rangedValue.getMinimum().floatValue()) / (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) * (getWidth() - 9));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 3 + leftX, getPosY() + 3, 3, getHeight() - 6);
    }

    private boolean hoveredRight(int mouseX, int mouseY) {
        final float rightX = MathHelper.floor_double((rangedValue.getRightVal().floatValue() - rangedValue.getMinimum().floatValue()) / (rangedValue.getMaximum().floatValue() - rangedValue.getMinimum().floatValue()) * (getWidth() - 9));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 3 + rightX, getPosY() + 3, 3, getHeight() - 6);
    }

    private double round(final double val) {
        final double v = Math.round(val / rangedValue.getInc().doubleValue()) * rangedValue.getInc().doubleValue();
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public RangedValue getRangedValue() {
        return rangedValue;
    }
}
