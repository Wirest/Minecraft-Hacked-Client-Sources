package info.sigmaclient.gui.screen.component;

import info.sigmaclient.Client;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMenuButton extends GuiButton {

    private ResourceLocation icon;
    int index = 0;
    boolean movingUp;

    public GuiMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        icon = new ResourceLocation("textures/menu/" + displayString.toLowerCase() + ".png");
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            hovered = visible && mouseX >= xPosition + width / 2 && mouseY >= yPosition && mouseX < xPosition + width / 2 + 50 && mouseY < yPosition + 60;
            mouseDragged(mc, mouseX, mouseY);
            int text = hovered ? Colors.getColor(255) : Colors.getColor(232);
            if (hovered && index < 1) {
                movingUp = true;
            } else if (index == 14 && movingUp && !hovered) {
                movingUp = false;
            }

            if (movingUp && index < 14) {
                index++;
            } else if (index > 0 && !movingUp) {
                index--;
            }
            GlStateManager.pushMatrix();
            float offset = (xPosition + width / 2);
            GlStateManager.translate(offset, (yPosition + getPosition(index)), 1);
            GlStateManager.enableAlpha();
            TTFFontRenderer font = Client.fm.getFont("SFM 9");
            font.drawStringWithShadow(displayString, 50 / 2 - (font.getWidth(displayString) / 2), 55, text);
            GlStateManager.enableBlend();
            mc.getTextureManager().bindTexture(icon);
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 50, 50, 50, 50);
            GlStateManager.bindTexture(0);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }

    public float getPosition(int index) {
        if (movingUp) {
            return new float[]{0, -48.839F, -107.135F, -147.163F, -159.884F, -148.736F,
                    -128.329F,
                    -112.506F,
                    -107.611F,
                    -117.462F,
                    -123.848F,
                    -118.805F,
                    -120.371F,
                    -119.885F,
                    -120}[index] / 10;
        } else {
            return new float[]{0,
                    -0.115F,
                    0.371F,
                    -1.195F,
                    3.848F,
                    -2.538F,
                    -12.389F,
                    -7.494F,
                    8.329F,
                    28.736F,
                    39.884F,
                    27.163F,
                    -12.865F,
                    -71.161F,
                    -120}[index] / 10;
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && mouseX >= xPosition + width / 2 && mouseY >= yPosition && mouseX < xPosition + width / 2 + 50 && mouseY < yPosition + 60;
    }

	/*
    * -120,
 -71.161,
  -12.865,
   27.163,
   39.884,
   28.736,
   8.329,
   -7.494,
   -12.389,
   -2.538,
   3.848,
   -1.195,
   0.371,
   -0.115,
 0,

	* */

}
