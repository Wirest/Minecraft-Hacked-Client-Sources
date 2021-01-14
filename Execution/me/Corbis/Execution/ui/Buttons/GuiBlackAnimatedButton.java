package me.Corbis.Execution.ui.Buttons;

import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class GuiBlackAnimatedButton extends GuiButton {
    int lastX = xPosition;
    int targetX;
    int lastX2 = xPosition + width;
    int targetX2;
    int lastY = yPosition;
    int targetY;
    int lastY2 = yPosition + height;
    int targetY2;
    static UnicodeFontRenderer ufr;


    public GuiBlackAnimatedButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiBlackAnimatedButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY){
        if(ufr == null)
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 10 ,5);
        if(this.visible){
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if(this.hovered){
                targetX = xPosition + width + 5;
                targetX2 = xPosition;
                targetY = yPosition + height + 11;
                targetY2 = yPosition;

                if(lastX != targetX){
                    float diff = targetX - lastX;
                    targetX = lastX;
                    lastX += diff / 6;
                }
                if(lastX2 != targetX2){
                    float diff = targetX2 - lastX2;
                    targetX2 = lastX2;
                    lastX2 += diff / 6;
                }
                if(lastY != targetY){
                    float diff = targetY - lastY;
                    targetY = lastY;
                    lastY += diff / 12;
                }
                if(lastY2 != targetY2){
                    float diff = targetY2 - lastY2;
                    targetY2 = lastY2;
                    lastY2 += diff / 12;
                }

                Gui.drawRect(xPosition, yPosition, targetX, yPosition + 1, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition  + width, yPosition + height , targetX2, yPosition + height - 1, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition + width, targetY, xPosition + width + 1, yPosition, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition , targetY2, xPosition - 1, yPosition + height, new Color(0,0, 0, 255).getRGB());
            }else {
                targetX = xPosition;
                targetX2 = xPosition + width + 5;
                targetY = yPosition;
                targetY2 = yPosition + height + 11;

                if(lastX != targetX){
                    float diff = targetX - lastX;
                    targetX = lastX;
                    lastX += diff / 6;
                }
                if(lastX2 != targetX2){
                    float diff = targetX2 - lastX2;
                    targetX2 = lastX2;
                    lastX2 += diff / 6;
                }
                if(lastY != targetY){
                    float diff = targetY - lastY;
                    targetY = lastY;
                    lastY += diff / 12;
                }
                if(lastY2 != targetY2){
                    float diff = targetY2 - lastY2;
                    targetY2 = lastY2;
                    lastY2 += diff / 12;
                }
                Gui.drawRect(xPosition, yPosition, targetX, yPosition + 1, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition  + width, yPosition + height , targetX2, yPosition + height - 1, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition + width, targetY, xPosition + width + 1, yPosition, new Color(0,0, 0, 255).getRGB());
                Gui.drawRect(xPosition , targetY2, xPosition - 1, yPosition + height, new Color(0,0, 0, 255).getRGB());
            }
            ufr.drawCenteredString(this.displayString, (float) (xPosition + (((xPosition + width) - xPosition) / 2.0D)), (float) (yPosition + (((yPosition + height) - yPosition) / 2.0D) - 6), new Color(0,0, 0, 255).getRGB());


        }
    }
}
