package info.sigmaclient.management.agora.component;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.management.FontManager;
import info.sigmaclient.management.agora.GuiAgora;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AgoraLightButton extends GuiButton {

    boolean hovered;
    private final ResourceLocation moonIcon1 = new ResourceLocation("textures/moon1.png");
    private final ResourceLocation moonIcon2 = new ResourceLocation("textures/moon2.png");
    public AgoraLightButton(int buttonId, int x, int y, String name) {
        super(buttonId, x, y, name);
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = name;
        this.width = 26;
        this.height = 26;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        hovered = mouseX >= xPosition && mouseX <= xPosition + width && mouseY > yPosition && mouseY <= yPosition + height;
        int s = res.getScaleFactor();
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        if(GuiAgora.dark){
        	 mc.getTextureManager().bindTexture(moonIcon1);
        }else{
        	 mc.getTextureManager().bindTexture(moonIcon2);
        }
        drawScaledTexturedModalRect(xPosition, yPosition, 0, 0, 24, 24, 11);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && hovered;
    }

 


}
