package nivia.gui.chod.component;

import net.minecraft.util.EnumChatFormatting;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChodsHueSlider extends ChodsComponent {

    private DoubleProperty value;
    private boolean drag = false;
    public static int selectedColor2 = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int inline                = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background            = RenderUtils.getIntFromColor(new Color(116, 121, 122));
    private int color;
    

    public ChodsHueSlider(Module mod, DoubleProperty value) {
        this.value = value;
        this.title = value.getName();
        this.mod = mod;
        color = (int) value.getValue();
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawBorderedRect(this.x + 6, this.y + 2, this.x + width, this.y + height - 2, 1, (int)this.value.getValue(), (int)this.value.getValue());
        double value = this.value.getValue();
        double min = this.value.min;
        double max = this.value.max;
        double sliderWidth = (this.width - 8) * (value - min) / (max - min);
        RenderUtils.cgothicgui.drawStringWithShadow(EnumChatFormatting.BOLD + title, this.x + 100 - Wrapper.getFontRenderer().getStringWidth(title) / 2 + 3, this.y + 6, -1);
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {
        int scaledMouseX = mouseX;
        if (drag && scaledMouseX < this.x + 200) {
        	double newVal = (Math.round(((scaledMouseX - x - 1) * (255 / 85))*100.0)/100.0);
            if (newVal > value.max) newVal = value.max;
            if (newVal < value.min) newVal = value.min; 
            double diff = Math.min(width, Math.max(0, scaledMouseX - x));
            if (diff == 0) {
                value.setValue(value.min);
                return;
            }
            double min = value.min;
            double max = value.max;
            value.setValue(Color.getHSBColor((float) (newVal / 255), 1, 1).getRGB());  
            color = (int)value.getValue();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();

        if (isInside(scaledMouseX, scaledMouseY, this.x, this.y + 1, this.width, this.height - 3))
            if (mouseButton == 0)
                this.drag = !this.drag;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
    private static double roundToPlace(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
