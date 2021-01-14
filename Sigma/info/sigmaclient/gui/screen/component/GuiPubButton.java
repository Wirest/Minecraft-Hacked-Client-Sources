package info.sigmaclient.gui.screen.component;

import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.ClientMainMenu;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPubButton extends GuiButton {

	private ResourceLocation icon;
	private ResourceLocation yt;
    Translate translate;
    public GuiPubButton(int buttonId,  int widthIn, int heightIn, String buttonText) {
        super(buttonId,  widthIn, heightIn, buttonText);
        this.xPosition = GuiScreen.width/2;
        this.yPosition =0;
        icon = new ResourceLocation("textures/menu/pub.jpg");
        yt = new ResourceLocation("textures/menu/yt.png");
        this.translate = new Translate(0, 0);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {   FontRenderer var4 = mc.fontRendererObj;
    float offset = (xPosition + width / 2);
    float yoffset = (yPosition + width / 2);
    
    hovered = mouseX >= xPosition - width/2 && mouseY >= yPosition && mouseX < xPosition + width/2 && mouseY < yPosition + height + translate.getY();
   
    int text = hovered ? Colors.getColor(255) : Colors.getColor(255);
    mouseDragged(mc, mouseX, mouseY);
    if(!hovered){
    	translate.interpolate(0, 0, 4);
    }else{
    	translate.interpolate(0, 110, 3);
    }
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.blendFunc(770, 771);
    GlStateManager.translate(0, translate.getY(), 1);
    int picWidth = 1192;
    int picHeight = 671;
    int smaller = 6;
    drawBorderedRect(xPosition - width/2 - 1, yPosition - picHeight/smaller + 9, xPosition + width/2, yPosition+height/2 + 1,2, Colors.getColor(255));
    
    TTFFontRenderer font = Client.fm.getFont("Verdana Bold 16");
    font.drawStringWithShadow(displayString, xPosition - font.getWidth(displayString)/2, yPosition+10 + font.getHeight(displayString)/2, text);
    GlStateManager.enableBlend();
    mc.getTextureManager().bindTexture(icon);
    drawModalRectWithCustomSizedTexture(xPosition - picWidth/smaller/2, -100, 0,5, picWidth/smaller, picHeight/smaller, picWidth/smaller, picHeight/smaller +10);
    
    mc.getTextureManager().bindTexture(yt);
    drawModalRectWithCustomSizedTexture(xPosition - picWidth/smaller/2, -100, 0, 0, picWidth/smaller, picHeight/smaller, picWidth/smaller, picHeight/smaller);
   
    GlStateManager.bindTexture(0);
    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();

    }

    public float getPosition(int index) {
    	return (float)Math.log(index+1)*38;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && mouseX >= xPosition - width/2 && mouseY >= yPosition && mouseX < xPosition + width/2 && mouseY < yPosition + height + translate.getY();
    }


}
