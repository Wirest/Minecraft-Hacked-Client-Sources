package moonx.ohare.client.gui.materialui.component.impl.subcomponents;

import moonx.ohare.client.gui.materialui.component.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.Fonts;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends Component {
    private NumberValue numberValue;
    private boolean sliding;
    public NumberComponent(NumberValue numberValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        final float sliderWidth = MathHelper.floor_double(((numberValue.getValue()).floatValue() - numberValue.getMinimum().floatValue()) / (numberValue.getMaximum().floatValue() - numberValue.getMinimum().floatValue()) * (getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10));
        Fonts.clickGuiFont.drawStringWithShadow(getLabel(), getPosX(), getPosY() + 3, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawRoundedRect(getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4, getPosY() + 5, getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10, 2, 2, new Color(55, 55, 55, 255).getRGB());
        RenderUtil.drawRoundedRect(getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4, getPosY() + 5, sliderWidth, 2, 2, new Color(0xff689FFF).getRGB());
        RenderUtil.drawCircle(getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + sliderWidth + 3,getPosY() + 4f,4,new Color(0xff689FFF).getRGB());
        Fonts.clickGuiSmallFont.drawStringWithShadow(String.valueOf(numberValue.getValue()),getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + sliderWidth + 3,getPosY(),new Color(229, 229, 223, 255).getRGB());
        if (sliding) {
            if (numberValue.getValue() instanceof Double) {
                numberValue.setValue(round(((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4)) * (numberValue.getMaximum().doubleValue() - numberValue.getMinimum().doubleValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().doubleValue()), 2));
            } else if (numberValue.getValue() instanceof Float) {
                numberValue.setValue((float) round(((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4)) * (numberValue.getMaximum().floatValue() - numberValue.getMinimum().floatValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().floatValue()), 2));
            } else if (numberValue.getValue() instanceof Long) {
                numberValue.setValue((long) round(((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4)) * (numberValue.getMaximum().longValue() - numberValue.getMinimum().longValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().longValue()), 2));
            } else if (numberValue.getValue() instanceof Integer) {
                numberValue.setValue((int) round((((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4))) * (numberValue.getMaximum().intValue() - numberValue.getMinimum().intValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().intValue()), 2));
            } else if (numberValue.getValue() instanceof Short) {
                numberValue.setValue((short) round((((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4))) * (numberValue.getMaximum().shortValue() - numberValue.getMinimum().shortValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().shortValue()), 2));
            } else if (numberValue.getValue() instanceof Byte) {
                numberValue.setValue((byte) round((((mouseX - (getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 4))) * (numberValue.getMaximum().byteValue() - numberValue.getMinimum().byteValue()) / ((getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 10)) + numberValue.getMinimum().byteValue()), 2));
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + Fonts.clickGuiFont.getStringWidth(getLabel()) + 2, getPosY() + 5, getWidth() - Fonts.clickGuiFont.getStringWidth(getLabel()) - 8, 2);
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

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
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
