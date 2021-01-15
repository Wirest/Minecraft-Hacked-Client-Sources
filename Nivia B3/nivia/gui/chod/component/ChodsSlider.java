package nivia.gui.chod.component;

import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.modules.render.StorageESP;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChodsSlider extends ChodsComponent {

    private DoubleProperty value;
    private boolean drag = false;
    public static int selectedColor2 = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int inline                = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background            = RenderUtils.getIntFromColor(new Color(116, 121, 122));

    public ChodsSlider(Module mod, DoubleProperty value) {
        this.value = value;
        this.title = value.getName();
        this.mod = mod;
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawBorderedRect(this.x + 6, this.y + 2, this.x + width, this.y + height - 2, 1, background, inline);

        double value = this.value.getValue();
        double min = this.value.min;
        double max = this.value.max;
        double sliderWidth = (this.width - 8) * (value - min) / (max - min);

        RenderUtils.drawRect(x + 7, y + 3, x + 7 + (int)sliderWidth, y + height - 3, selectedColor2);
        RenderUtils.cgothicgui.drawStringWithShadow(EnumChatFormatting.BOLD + title+":", this.x + 10, this.y + 6, -1);
        RenderUtils.cgothicgui.drawStringWithShadow(EnumChatFormatting.BOLD + (String.valueOf(this.value.getValue())), this.x + width - Wrapper.getFontRenderer().getStringWidth(String.valueOf(this.value.getValue()))- 6, this.y + 6, -1);
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {
        int scaledMouseX = mouseX;
        if (drag) {
        	double newVal = (Math.round((value.min + (scaledMouseX - x - 1) * ((value.max - value.min) / 85))*100.0)/100.0);
            if (newVal > value.max) newVal = value.max;
            if (newVal < value.min) newVal = value.min; 
            double diff = Math.min(width, Math.max(0, scaledMouseX - x));
            if (diff == 0) {
                value.setValue(value.min);
                return;
            }
            double min = value.min;
            double max = value.max;

            if(this.value.max <= 10) {
                value.setValue(Double.valueOf(roundToPlace((((diff) / (width)) * (max - min)) + min, 1)));
            }else{
                value.setValue(Math.round(Double.valueOf(roundToPlace((((diff) / (width)) * (max - min)) + min, 2))));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();


        if (isInside(scaledMouseX, scaledMouseY, this.x, this.y + 1, this.width, this.height - 3)) {
            if (mouseButton == 0) {
                this.drag = !this.drag;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.UP);
        return bd.doubleValue();
    }
}
