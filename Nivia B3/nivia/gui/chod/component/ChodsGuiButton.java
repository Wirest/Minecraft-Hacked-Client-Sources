package nivia.gui.chod.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import nivia.gui.chod.ChodsGui;
import nivia.modules.Module.Category;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;

public class ChodsGuiButton {

    private int posX, posY;
    private int width, height;
    private Category category;
    private ResourceLocation buttonImage;
    private boolean hover;
    private ChodsGui parent;

    public int textColor    = RenderUtils.getIntFromColor(new Color(151, 157, 157));
    public static int hoverColor = RenderUtils.getIntFromColor(new Color(75, 177, 219));

    public int insideColor1 = RenderUtils.getIntFromColor(new Color(33, 33, 33));
    public int insideColor2 = RenderUtils.getIntFromColor(new Color(66, 66, 66));

    private int hoverTime = 0;

    public ChodsGuiButton(int x, int y, int widthIn, int heightIn, Category category, ResourceLocation resourceLocation, ChodsGui parent) {
        this.posX = x;
        this.posY = y;
        this.width = widthIn;
        this.height = heightIn;
        this.category = category;
        this.buttonImage = resourceLocation;
        this.parent = parent;
    }

    public void drawButton(int mouseX, int mouseY) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        float scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        float scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();

        hover = isInside(scaledMouseX, scaledMouseY, posX, posY, width, height);
        float target = 255;
        float dist = Math.abs(target - hoverTime);
        float change = dist / 10;
        if (this.hover) {
            hoverTime += change;
        }
        else if (!this.hover) {
            hoverTime -= change;
        }
        hoverTime = Math.min(255, hoverTime);
        hoverTime = Math.max(0, hoverTime);

        Color color = new Color(75, 177, 219, hoverTime);

        RenderUtils.drawBorderedRect(posX, posY, posX + width, posY + height, 1, insideColor2, insideColor1);

    	RenderUtils.helvetica.drawStringWithShadow(getText(), (this.posX - RenderUtils.helvetica.getStringWidth(getText())/2) + this.width / 2, this.posY + height - 25, textColor);

        int size = 120;

        if (hoverTime < 200) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(buttonImage);
            Gui.drawModalRectWithCustomSizedTexture(posX + 20, posY + 10, 0, 0, size, size, size, size);
        }

        if (hoverTime > 0) {
        	RenderUtils.helvetica.drawStringWithShadow(getText(), (this.posX -RenderUtils.helvetica.getStringWidth(getText())/2) + this.width / 2, this.posY + height - 25, color.getRGB());
            Minecraft.getMinecraft().getTextureManager().bindTexture(buttonImage);
        }
        Gui.drawModalRectWithCustomSizedTexture(posX + 20, posY + 10, 0, 0, size, size, size, size);
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (hover && parent.selectedScreen == null) {
            Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.1f, 1.0f);
            parent.selectedScreen = new ChodsGuiScreen(parent.backgroundWidth, parent.backgroundHeight, category, parent);
        }
    }

    public boolean isInside(float pointX, float pointY, float x, float y, int width, int height) {
        return (pointX >= x && pointX <= x + width) && (pointY >= y && pointY <= y + height);
    }

    public String getText() {
        return category == null ? "Config" : category.getName();
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosY(int y) {
        this.posY = y;
    }

    public int getPosY() {
        return posY;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }
}
