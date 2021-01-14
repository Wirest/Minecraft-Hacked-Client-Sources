package de.iotacb.client.minimap.interfaces;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import de.iotacb.client.minimap.Minimap;
import de.iotacb.client.minimap.XaeroMinimap;
import de.iotacb.client.minimap.settings.ModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;

public class InterfaceHandler$5 extends Interface {
    public InterfaceHandler$5(String name, int w, int h, ModOptions option) {
		super(name, w, h, option);
	}

	long lastFBOTry = 0L;
    int lastMinimapSize = 0;
    private ArrayList<String> underText = new ArrayList<String>();
    
    @Override
    public int getW(final int scale) {
        return this.getSize() / scale;
    }
    
    @Override
    public int getH(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getWC(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getHC(final int scale) {
        return this.getH(scale);
    }
    
    @Override
    public int getW0(final int scale) {
        return this.getW(scale);
    }
    
    @Override
    public int getH0(final int scale) {
        return this.getH(scale);
    }
    
    @Override
    public int getSize() {
        return InterfaceHandler.minimap.getMinimapWidth() + 36 + 2;
    }
    
    public void translatePosition(final int specW, final int specH, final double ps, final double pc, final double offx, final double offy) {
        final double Y = (pc * offx + ps * offy) * Minimap.zoom;
        double borderedX;
        final double X = borderedX = (ps * offx - pc * offy) * Minimap.zoom;
        double borderedY = Y;
        if (borderedX > specW) {
            borderedX = specW;
            borderedY = Y * specW / X;
        }
        else if (borderedX < -specW) {
            borderedX = -specW;
            borderedY = -Y * specW / X;
        }
        if (borderedY > specH) {
            borderedY = specH;
            borderedX = X * specH / Y;
        }
        else if (borderedY < -specH) {
            borderedY = -specH;
            borderedX = -X * specH / Y;
        }
        GL11.glPushMatrix();
        GlStateManager.translate(borderedX, borderedY, 0.0);
    }
    
    @Override
    public void drawInterface(final int width, final int height, final int scale, final float partial) {
        if (Minimap.loadedFBO && !OpenGlHelper.isFramebufferEnabled()) {
            Minimap.loadedFBO = false;
            Minimap.scalingFrameBuffer.deleteFramebuffer();
            Minimap.rotationFrameBuffer.deleteFramebuffer();
            Minimap.resetImage();
        }
        if (!Minimap.loadedFBO && !XaeroMinimap.getSettings().mapSafeMode && System.currentTimeMillis() - this.lastFBOTry > 1000L) {
            this.lastFBOTry = System.currentTimeMillis();
            Minimap.loadFrameBuffer();
        }
        if (XaeroMinimap.getSettings().getMinimapSize() != this.lastMinimapSize) {
            this.lastMinimapSize = XaeroMinimap.getSettings().getMinimapSize();
            Minimap.resetImage();
            Minimap.frameUpdateNeeded = Minimap.usingFBO();
        }
        final long before = System.currentTimeMillis();
        int bufferSize;
        if (Minimap.usingFBO()) {
            bufferSize = InterfaceHandler.minimap.getFBOBufferSize();
        }
        else {
            bufferSize = InterfaceHandler.minimap.getBufferSize();
        }
        final float mapScale = scale / 2.0f;
        final Minimap minimap = InterfaceHandler.minimap;
        final int minimapWidth = InterfaceHandler.minimap.getMinimapWidth();
        minimap.minimapWidth = minimapWidth;
        final int mapW = minimapWidth;
        final Minimap minimap2 = InterfaceHandler.minimap;
        final int minimapHeight = mapW;
        minimap2.minimapHeight = minimapHeight;
        final int mapH = minimapHeight;
        Minimap.frameUpdatePartialTicks = partial;
        InterfaceHandler.minimap.updateZoom();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float sizeFix = bufferSize / 512.0f;
        InterfaceHandler.minimap.renderFrameToFBO(bufferSize, mapW, sizeFix, partial, true);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        Minimap.rotationFrameBuffer.bindFramebufferTexture();
        sizeFix = 1.0f;
        GL11.glEnable(3008);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glScalef(1.0f / mapScale, 1.0f / mapScale, 1.0f);
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(8.5, 8.5, 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
        final int specH;
        final int specW = specH = mapW / 2 + 6;
        final double angle = Math.toRadians(Minimap.getRenderAngle());
        final double ps = Math.sin(3.141592653589793 - angle);
        final double pc = Math.cos(3.141592653589793 - angle);
    }
    
    private void drawArrow(final double arrowX, final double arrowY, final float[] colour) {
        GL11.glPushMatrix();
        GL11.glTranslated(arrowX, arrowY, 0.0);
        GlStateManager.rotate(InterfaceHandler.mc.thePlayer.rotationYaw, 0.0f, 0.0f, 1.0f);
        GL11.glScalef(0.5f * XaeroMinimap.getSettings().arrowScale, 0.5f * XaeroMinimap.getSettings().arrowScale, 1.0f);
        GL11.glTranslated(-13.0, -6.0, 0.0);
        GL11.glColor4f(colour[0], colour[1], colour[2], colour[3]);
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(0, 0, 49, 0, 26, 27);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public void drawTextUnderMinimap(final int scaledX, final int scaledY, final int height, final float mapScale) {
        final int interfaceSize = this.getSize() / 2;
        final int scaledHeight = (int)(height * mapScale);
        for (int i = 0; i < this.underText.size(); ++i) {
            final String s = this.underText.get(i);
            final int stringWidth = InterfaceHandler.mc.fontRendererObj.getStringWidth(s);
            final boolean under = scaledY + interfaceSize / 2 < scaledHeight / 2;
            final int stringY = scaledY + (under ? interfaceSize : -9) + i * 9 * (under ? 1 : -1);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s, (float)(scaledX + interfaceSize / 2 - stringWidth / 2), (float)stringY, Minimap.radarPlayers.hashCode());
        }
        this.underText.clear();
    }
    
    private void drawCompass(final int specW, final int specH, final double ps, final double pc) {
        final String[] nesw = { "N", "E", "S", "W" };
        for (int i = 0; i < 4; ++i) {
            final double offx = (i == 0 || i == 2) ? 0.0 : ((i == 1) ? 10000 : -10000);
            final double offy = (i == 1 || i == 3) ? 0.0 : ((i == 2) ? 10000 : -10000);
            this.translatePosition(specW, specH, ps, pc, offx, offy);
            GlStateManager.scale(2.0f, 2.0f, 1.0f);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(nesw[i], (float)(1 - InterfaceHandler.mc.fontRendererObj.getStringWidth(nesw[i]) / 2), -3.0f, Minimap.radarPlayers.hashCode());
            GL11.glPopMatrix();
        }
    }
}
