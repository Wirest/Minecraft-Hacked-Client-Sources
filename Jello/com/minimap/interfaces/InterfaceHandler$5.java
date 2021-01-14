package com.minimap.interfaces;

import com.minimap.settings.*;

import org.lwjgl.opengl.*;

import com.mentalfrostbyte.jello.main.Jello;
import com.minimap.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import com.minimap.mods.*;
import com.minimap.minimap.*;
import java.util.*;

final class InterfaceHandler$5 extends Interface {
    public InterfaceHandler$5(String name, int w, int h, ModOptions option) {
		super(name, w, h, option);
		// TODO Auto-generated constructor stub
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
        //Minimap.zoom = InterfaceHandler.minimap.getZoom();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float sizeFix = bufferSize / 512.0f;
        //if (Minimap.usingFBO()) {
            InterfaceHandler.minimap.renderFrameToFBO(bufferSize, mapW, sizeFix, partial, true);
            Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
            Minimap.rotationFrameBuffer.bindFramebufferTexture();
            sizeFix = 1.0f;
        //}
        /*else {
            InterfaceHandler.minimap.updateMapFrame(bufferSize, partial);
            GL11.glScalef(sizeFix, sizeFix, 1.0f);
            Minimap.bindTextureBuffer(Minimap.mapTexture.getBuffer(bufferSize), bufferSize, bufferSize, Minimap.mapTexture.getGlTextureId());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, XaeroMinimap.getSettings().minimapOpacity / 100.0f);
        }*/
        GL11.glEnable(3008);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glScalef(1.0f / mapScale, 1.0f / mapScale, 1.0f);
        final int scaledX = (int)(this.x * mapScale);
        final int scaledY = (int)(this.y * mapScale);
        /*GlStateManager.translate(5, 130, 0);
        GlStateManager.scale(1.5f, 1.5f, 1);
        GlStateManager.translate(-5, -130, 0);*/
      //  InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(5, 130, 0, 0, 50, 50);
      /*  GlStateManager.translate(5, 130, 0);
        GlStateManager.scale(2/3f, 2/3f, 1);
        GlStateManager.translate(-5, -130, 0);*/
        //Jello.addChatMessage((int)((mapW / 2 + 1) / sizeFix)+"");
       /* GlStateManager.translate(5, 130, 0);
        GlStateManager.scale(1.3157894736842105263157894736842f, 1.3157894736842105263157894736842f, 1);
        GlStateManager.translate(-5, -130, 0);*/
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(5, 130, 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
        // Gui.INSTANCE.drawModalRectWithCustomSizedTexture(5, 130, 0,0, 75, 75, 375, 375);
        //InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(5, 130, 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
       // InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(5, 130, 0, 0, 75, 75);
       /* GlStateManager.translate(5, 130, 0);
        GlStateManager.scale(1/1.3157894736842105263157894736842f, 1/1.3157894736842105263157894736842f, 1);
        GlStateManager.translate(-5, -130, 0);*/
        if (!Minimap.usingFBO()) {
            //GL11.glScalef(1.0f / sizeFix, 1.0f / sizeFix, 1.0f);
            //GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        final int specH;
        final int specW = specH = mapW / 2 + 6;
        final double angle = Math.toRadians(Minimap.getRenderAngle());
        final double ps = Math.sin(3.141592653589793 - angle);
        final double pc = Math.cos(3.141592653589793 - angle);
        /*InterfaceHandler.mc.getTextureManager().bindTexture(InterfaceHandler.guiTextures);
        if (XaeroMinimap.getSettings().getLockNorth()) {
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
            final double arrowX = 2 * scaledX + 18 + (mapW + 1) / 2 + 0.5;
            final double arrowY = 2 * scaledY + 18 + (mapH + 1) / 2 + 0.5;
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            this.drawArrow(arrowX, arrowY + 1.0, new float[] { 0.0f, 0.0f, 0.0f, 0.5f });
            this.drawArrow(arrowX, arrowY, XaeroMinimap.getSettings().arrowColours[XaeroMinimap.getSettings().arrowColour]);
            GL11.glPopMatrix();
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4, scaledY + 9 - 4, 0, 0, 17, 15);
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4 + mapW / 2 - 8, scaledY + 9 - 4, 0, 15, 17, 15);
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4, scaledY + 9 - 4 + mapH / 2 - 6, 0, 30, 17, 15);
        InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4 + mapW / 2 - 8, scaledY + 9 - 4 + mapH / 2 - 6, 0, 45, 17, 15);
        for (int horLineLength = (mapW / 2 - 16) / 16, i = 0; i < horLineLength; ++i) {
            InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4 + 17 + i * 16, scaledY + 9 - 4, 0, 60, 16, 4);
            InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4 + 17 + i * 16, scaledY + 9 - 4 + mapH / 2 + 9 - 4, 0, 64, 16, 4);
        }
        for (int vertLineLength = (mapH / 2 - 14) / 5, j = 0; j < vertLineLength; ++j) {
            InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4, scaledY + 9 - 4 + 15 + j * 5, 0, 68, 4, 5);
            InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(scaledX + 9 - 4 + mapW / 2 + 9 - 4, scaledY + 9 - 4 + 15 + j * 5, 0, 73, 4, 5);
        }*/
       // GL11.glPushMatrix();
       // GlStateManager.scale(0.5f, 0.5f, 1.0f);
       // GlStateManager.translate((float)(2 * scaledX + specW - 6 + 18), (float)(2 * scaledY + specH - 6 + 18), 0.0f);
        /*if (XaeroMinimap.getSettings().compassOverWaypoints) {
            this.drawWaypoints(specW, specH, ps, pc, partial);
            this.drawCompass(specW, specH, ps, pc);
        }
        else {
            this.drawCompass(specW, specH, ps, pc);
            this.drawWaypoints(specW, specH, ps, pc, partial);
        }*/
       // GL11.glPopMatrix();
        /*if (XaeroMinimap.getSettings().getShowCoords()) {
            final int interfaceSize = this.getSize() / 2;
            String coords = Minimap.myFloor(InterfaceHandler.mc.thePlayer.posX) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posY) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posZ);
            if (InterfaceHandler.mc.fontRendererObj.getStringWidth(coords) >= interfaceSize) {
                final String stringLevel = "" + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posY);
                coords = Minimap.myFloor(InterfaceHandler.mc.thePlayer.posX) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posZ);
                this.underText.add(coords);
                this.underText.add(stringLevel);
            }
            else {
                this.underText.add(coords);
            }
        }
        if (XaeroMinimap.getSettings().showBiome) {
            final BlockPos pos = new BlockPos(InterfaceHandler.mc.getRenderViewEntity().posX, InterfaceHandler.mc.getRenderViewEntity().getEntityBoundingBox().minY, InterfaceHandler.mc.getRenderViewEntity().posZ);
            this.underText.add(InterfaceHandler.mc.theWorld.getChunkFromBlockCoords(pos).getBiome(pos, InterfaceHandler.mc.theWorld.getWorldChunkManager()).biomeName);
        }*/
      //  this.drawTextUnderMinimap(scaledX, scaledY, height, mapScale);
        //GL11.glScalef(mapScale, mapScale, 1.0f);
       // super.drawInterface(width, height, scale, partial);
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
