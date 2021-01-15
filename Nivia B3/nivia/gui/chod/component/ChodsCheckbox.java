package nivia.gui.chod.component;

import net.minecraft.util.EnumChatFormatting;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;

public class ChodsCheckbox extends ChodsComponent {

    public static int selectedColor  = RenderUtils.getIntFromColor(new Color(57, 132, 164));
    public static int selectedColor2 = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int inline     = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background = RenderUtils.getIntFromColor(new Color(116, 121, 122));

    private Property<Boolean> value;
    private float hoverTime, enabledTime = 0F;

    public ChodsCheckbox(Module mod, Property<Boolean> value) {
        this.value = value;
        this.title = value.getName();
        this.mod = mod;
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();
        RenderUtils.drawRectWH(this.x + 6, this.y + 2, 20, 20, isInside(scaledMouseX, scaledMouseY, this.x + 4, this.y, this.width - 179, this.height) ? new Color(57, 57, 57).darker().getRGB() : inline);
        RenderUtils.drawRectWH(this.x + 7, this.y + 3, 18, 18, isInside(scaledMouseX, scaledMouseY, this.x + 4, this.y, this.width - 179, this.height) ? new Color(116, 121, 122).darker().getRGB() : background);
        if (value.value) {
            RenderUtils.drawRectWH(this.x + 9, this.y + 5, 14, 14, selectedColor);
            RenderUtils.drawRectWH(this.x + 10, this.y + 6, 12, 12, selectedColor2);
        }
        RenderUtils.cgothicgui.drawString(EnumChatFormatting.BOLD + title, this.x + 28, this.y + 6, 0xFFFFFFFF);
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();

        if (isInside(scaledMouseX, scaledMouseY)) {
            if (mouseButton == 0) {
                this.value.value = (!this.value.value);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
