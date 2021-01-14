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

public class AgoraBackButton extends GuiButton {

    boolean hovered;
    private final ResourceLocation backIcon0 = new ResourceLocation("textures/back0.png");
    private final ResourceLocation backIcon1 = new ResourceLocation("textures/back1.png");
    private final ResourceLocation backIcon2 = new ResourceLocation("textures/back2.png");
    public AgoraBackButton(int buttonId, int x, int y, String name) {
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
        if(!hovered){
        	mc.getTextureManager().bindTexture(GuiAgora.dark? backIcon1 : backIcon0);
        }else{
        	mc.getTextureManager().bindTexture(backIcon2);
        }
        drawScaledTexturedModalRect(xPosition, yPosition, 0, 0, 26, 26, 10);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && hovered;
    }

 


}
