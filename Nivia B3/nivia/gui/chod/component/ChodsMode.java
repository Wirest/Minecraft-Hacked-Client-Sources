package nivia.gui.chod.component;

import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;

public class ChodsMode extends ChodsComponent {

    public static int selectedColor  = RenderUtils.getIntFromColor(new Color(57, 132, 164));
    public static int selectedColor2 = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int inline     = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background = RenderUtils.getIntFromColor(new Color(116, 121, 122));

    private Enum[] modes;
    private Property mv;
    private float hoverTime, enabledTime = 0F;

    public ChodsMode(Module mod, Property modValue) {
        this.mv = modValue;
        this.title = String.valueOf(modValue.value);
        this.mod = mod;
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();
        RenderUtils.drawRectWH(this.x + 6, this.y + 2, 69, 20, inline);
        RenderUtils.drawRectWH(this.x + 7, this.y + 3, 67, 18, background);
        Wrapper.getFontRenderer().drawStringWithShadow("<", this.x + 7, this.y + 8, isInside(scaledMouseX, scaledMouseY, this.x + 6, this.y + 2, 33, 20) ? new Color(57, 132, 200).darker().getRGB() :new Color(57, 132, 200).getRGB());
        Wrapper.getFontRenderer().drawStringWithShadow(">", this.x + 70, this.y + 8, isInside(scaledMouseX, scaledMouseY, this.x + 40, this.y, this.width - 165, this.height) ? new Color(57, 132, 200).darker().getRGB() : new Color(57, 132, 200).getRGB());
        Wrapper.getFontRenderer().drawStringWithFagShadow(String.valueOf(mv.value), this.x + 41 - Wrapper.getFontRenderer().getStringWidth(String.valueOf(mv.value)) / 2, this.y + 8, -1);
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {

    }

    private static int index;
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();
        if (isInside(scaledMouseX, scaledMouseY, this.x, this.y, this.width - 160, this.height))
        if (mouseButton == 0) {
        	modes = (Enum[]) mv.value.getClass().getEnumConstants();
            if (index != 0)
                index--;
            else index = modes.length - 1;
            mv.value = modes[index];  
        }
        if (isInside(scaledMouseX, scaledMouseY, this.x + 40, this.y, this.width - 165, this.height))
        if (mouseButton == 0) {
       	 modes = (Enum[]) mv.value.getClass().getEnumConstants();
         if (index < modes.length - 1)
             index++;
         else index = 0;
         mv.value = modes[index];
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
