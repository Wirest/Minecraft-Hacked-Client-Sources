package VenusClient.online.Utils.MainMenu;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class MainMenuUtil {

    public static void drawString(final double scale, final String text, final float xPos, final float yPos, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale,scale , scale);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, xPos, yPos, color);
        GlStateManager.popMatrix();
    }


    public static void drawImg(ResourceLocation loc, int posX, int posY, int width, int height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
        Gui.drawModalRectWithCustomSizedTexture(posX, posY, 0.0F, 0.0F, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
}
