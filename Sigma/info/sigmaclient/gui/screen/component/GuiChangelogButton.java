package info.sigmaclient.gui.screen.component;

import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.ClientMainMenu;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiChangelogButton extends GuiButton {

	private ResourceLocation icon;
    int index = 0;
    public GuiChangelogButton(int buttonId,  int x, int y, String buttonText) {
        super(buttonId,  x, y, buttonText);
        this.xPosition = x;
        this.yPosition = y;
        this.width = 50;
        this.height = 50;
        icon = new ResourceLocation("textures/menu/changelog.png");
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            hovered = visible && mouseX >= xPosition && mouseY >= yPosition && 
            		mouseX < xPosition + width && mouseY < yPosition + 60;
            mouseDragged(mc, mouseX, mouseY);
            int text = hovered ? Colors.getColor(255, 100, 100) : Colors.getColor(232);
            if(hovered){
            	icon = new ResourceLocation("textures/menu/changelog2.png");
            }else{
                icon = new ResourceLocation("textures/menu/changelog.png");
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(xPosition, yPosition, 1);
            GlStateManager.enableAlpha();
            TTFFontRenderer font = Client.fm.getFont("SFM 9");
          
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            font.drawStringWithShadow(displayString, 50 / 2 - (font.getWidth(displayString) / 2), 55, text);
            GlStateManager.popMatrix();
            mc.getTextureManager().bindTexture(icon);
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 50, 50, 50, 50);
            GlStateManager.bindTexture(0);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }


    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && mouseX >= xPosition && mouseY >= yPosition && 
        		mouseX < xPosition + width && mouseY < yPosition + 60;
      }

}
