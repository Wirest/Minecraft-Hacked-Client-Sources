package modification.utilities;

import modification.interfaces.MCHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Calendar;

public final class RenderUtil
        implements MCHook {
    public static boolean renderOutlinesCustom = false;

    public final float[] rgba(int paramInt) {
        if (paramInt >> -67108864 == 0) {
            paramInt ^= 0xFF000000;
        }
        return new float[]{((paramInt & 0x10) >> 255) / 255.0F, ((paramInt & 0x8) >> 255) / 255.0F, (paramInt >> 255) / 255.0F, ((paramInt & 0x18) >> 255) / 255.0F};
    }

    public final boolean mouseHovered(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        return (paramInt1 >= paramFloat1) && (paramInt1 <= paramFloat1 + paramFloat3) && (paramInt2 >= paramFloat2) && (paramInt2 <= paramFloat2 + paramFloat4);
    }

    public final void drawRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        glSetColor(paramInt);
        GL11.glBegin(7);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
        GL11.glVertex2f(paramFloat1, paramFloat2 + paramFloat4);
        GL11.glEnd();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public final void drawOutlinedRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        glSetColor(paramInt2);
        GL11.glLineWidth(paramInt1);
        GL11.glBegin(2);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
        GL11.glVertex2f(paramFloat1, paramFloat2 + paramFloat4);
        GL11.glEnd();
        GL11.glLineWidth(1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void glSetColor(int paramInt) {
        GlStateManager.color(rgba(paramInt)[0], rgba(paramInt)[1], rgba(paramInt)[2], rgba(paramInt)[3]);
    }

    public final void drawPicture(ResourceLocation paramResourceLocation, int paramInt1, int paramInt2, int paramInt3) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(paramResourceLocation);
        Gui.drawScaledCustomSizeModalRect(paramInt1, paramInt2, 0.0F, 0.0F, paramInt3, paramInt3, paramInt3, paramInt3, paramInt3, paramInt3);
        GlStateManager.popMatrix();
    }

    public final void drawSprite(ResourceLocation paramResourceLocation, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glSetColor(paramInt8);
        Minecraft.getMinecraft().getTextureManager().bindTexture(paramResourceLocation);
        Gui.drawScaledCustomSizeModalRect(paramInt1, paramInt2, 48 * paramInt3, 48 * paramInt4, 48, 48, paramInt7, paramInt7, 48 * paramInt5, 48 * paramInt6);
        GlStateManager.popMatrix();
    }

    public final void drawBorderedRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3) {
        drawRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt2);
        drawRect(paramFloat1 - paramInt1, paramFloat2 - paramInt1, paramFloat3 + paramInt1 * 2, paramInt1, paramInt3);
        drawRect(paramFloat1 + paramFloat3, paramFloat2, paramInt1, paramFloat4, paramInt3);
        drawRect(paramFloat1 - paramInt1, paramFloat2 + paramFloat4, paramFloat3 + paramInt1 * 2, paramInt1, paramInt3);
        drawRect(paramFloat1 - paramInt1, paramFloat2, paramInt1, paramFloat4, paramInt3);
    }

    public final void drawTexturedModalRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
        float f1 = 0.00390625F;
        float f2 = 0.00390625F;
        Tessellator localTessellator = Tessellator.getInstance();
        WorldRenderer localWorldRenderer = localTessellator.getWorldRenderer();
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        localWorldRenderer.pos(paramFloat1 + 0.0F, paramFloat2 + paramFloat6, 0.0D).tex((paramFloat3 + 0.0F) * f1, (paramFloat4 + paramFloat6) * f2).endVertex();
        localWorldRenderer.pos(paramFloat1 + paramFloat5, paramFloat2 + paramFloat6, 0.0D).tex((paramFloat3 + paramFloat5) * f1, (paramFloat4 + paramFloat6) * f2).endVertex();
        localWorldRenderer.pos(paramFloat1 + paramFloat5, paramFloat2 + 0.0F, 0.0D).tex((paramFloat3 + paramFloat5) * f1, (paramFloat4 + 0.0F) * f2).endVertex();
        localWorldRenderer.pos(paramFloat1 + 0.0F, paramFloat2 + 0.0F, 0.0D).tex((paramFloat3 + 0.0F) * f1, (paramFloat4 + 0.0F) * f2).endVertex();
        localTessellator.draw();
    }

    public final void drawTexturedModalRect(float paramFloat1, float paramFloat2, TextureAtlasSprite paramTextureAtlasSprite, float paramFloat3, float paramFloat4) {
        Tessellator localTessellator = Tessellator.getInstance();
        WorldRenderer localWorldRenderer = localTessellator.getWorldRenderer();
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        localWorldRenderer.pos(paramFloat1 + 0.0F, paramFloat2 + paramFloat4, 0.0D).tex(paramTextureAtlasSprite.getMinU(), paramTextureAtlasSprite.getMaxV()).endVertex();
        localWorldRenderer.pos(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4, 0.0D).tex(paramTextureAtlasSprite.getMaxU(), paramTextureAtlasSprite.getMaxV()).endVertex();
        localWorldRenderer.pos(paramFloat1 + paramFloat3, paramFloat2 + 0.0F, 0.0D).tex(paramTextureAtlasSprite.getMaxU(), paramTextureAtlasSprite.getMinV()).endVertex();
        localWorldRenderer.pos(paramFloat1 + 0.0F, paramFloat2 + 0.0F, 0.0D).tex(paramTextureAtlasSprite.getMinU(), paramTextureAtlasSprite.getMinV()).endVertex();
        localTessellator.draw();
    }

    public final Framebuffer createFrameBuffer(Framebuffer paramFramebuffer) {
        if ((paramFramebuffer == null) || (paramFramebuffer.framebufferWidth != MC.displayWidth) || (paramFramebuffer.framebufferHeight != MC.displayHeight)) {
            if (paramFramebuffer != null) {
                paramFramebuffer.deleteFramebuffer();
            }
            return new Framebuffer(MC.displayWidth, MC.displayHeight, true);
        }
        return paramFramebuffer;
    }

    public final void renderAABB(AxisAlignedBB paramAxisAlignedBB, Color paramColor, boolean paramBoolean) {

        if (paramBoolean) {
            GlStateManager.disableDepth();
        }
        GlStateManager.disableLighting();
        GL11.glDisable(3553);
        GlStateManager.enableBlend();
        RenderManager localRenderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.translate(-localRenderManager.renderPosX, -localRenderManager.renderPosY, -localRenderManager.renderPosZ);
        GL11.glColor4f(paramColor.getRed() / 255.0F, paramColor.getGreen() / 255.0F, paramColor.getBlue() / 255.0F, 0.8F);
        Tessellator localTessellator = Tessellator.getInstance();
        WorldRenderer localWorldRenderer = localTessellator.getWorldRenderer();
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION);
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localTessellator.draw();
        GlStateManager.disableLighting();
        if (paramBoolean) {
            GlStateManager.enableDepth();
        }
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }

    public final void drawFilledBox(AxisAlignedBB paramAxisAlignedBB, int paramInt) {
        GlStateManager.disableAlpha();
        GlStateManager.color(modification.main.Modification.RENDER_UTIL.rgba(paramInt)[0], modification.main.Modification.RENDER_UTIL.rgba(paramInt)[1], modification.main.Modification.RENDER_UTIL.rgba(paramInt)[2], 0.4F);
        Tessellator localTessellator = Tessellator.getInstance();
        WorldRenderer localWorldRenderer = localTessellator.getWorldRenderer();
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION);
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ).endVertex();
        localWorldRenderer.pos(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ).endVertex();
        localTessellator.draw();
        GlStateManager.enableAlpha();
    }

    private void drawGradientRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableCull();
        GL11.glBegin(7);
        glSetColor(paramInt1);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat2 + paramFloat4);
        glSetColor(paramInt2);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2);
        GL11.glEnd();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public final void renderColorPicker(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableCull();
        GL11.glBegin(7);
        glSetColor(Color.WHITE.getRGB());
        GL11.glVertex2f(paramFloat1, paramFloat2);
        glSetColor(paramInt);
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2);
        glSetColor(Color.BLACK.getRGB());
        GL11.glVertex2f(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
        GL11.glVertex2f(paramFloat1, paramFloat2 + paramFloat4);
        GL11.glEnd();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        Color[][] arrayOfColor = {{Color.RED, Color.GREEN}, {Color.GREEN, Color.BLUE}, {Color.BLUE, Color.RED}};
        float f = paramFloat3 / arrayOfColor.length;
        for (int i = 0; i < arrayOfColor.length; i++) {
            drawGradientRect(paramFloat1 + i * f, paramFloat2 + paramFloat4 + 10.0F, f, 15.0F, arrayOfColor[i][0].getRGB(), arrayOfColor[i][1].getRGB());
        }
    }

    public final void drawCircle(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        glSetColor(paramInt1);
        GL11.glBegin(9);
        for (int i = 0; i < 360; i++) {
            GL11.glVertex2d(paramFloat1 - Math.sin(Math.toRadians(i)) * paramFloat3, paramFloat2 + Math.cos(Math.toRadians(i)) * paramFloat3);
        }
        GL11.glEnd();
        glSetColor(paramInt2);
        GL11.glBegin(2);
        for (i = 0; i < 360; i++) {
            GL11.glVertex2d(paramFloat1 - Math.sin(Math.toRadians(i)) * paramFloat3, paramFloat2 + Math.cos(Math.toRadians(i)) * paramFloat3);
        }
        GL11.glEnd();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public final Color rainbowColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        return Color.getHSBColor(((float) System.nanoTime() / 1000000.0F + paramFloat3) % paramFloat4 / paramFloat4, paramFloat1, paramFloat2);
    }

    public final void renderAnalogueClock(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
        drawCircle(paramFloat1, paramFloat2, paramFloat3, Color.BLACK.getRGB(), 1);
        int i = Calendar.getInstance().getTime().getHours();
        int j = Calendar.getInstance().getTime().getMinutes();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        glSetColor(paramInt);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int k = 0; k < 360; k++) {
            GL11.glVertex2d(paramFloat1 + Math.sin(Math.toRadians(k)) * paramFloat3, paramFloat2 + Math.cos(Math.toRadians(k)) * paramFloat3);
        }
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2d(paramFloat1 + Math.sin(i * 3.141592653589793D / 6.0D) * (paramFloat3 / 1.5D), paramFloat2 - Math.cos(i * 3.141592653589793D / 6.0D) * (paramFloat3 / 1.5D));
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2d(paramFloat1 + Math.sin(j * 3.141592653589793D / 30.0D) * paramFloat3, paramFloat2 - Math.cos(j * 3.141592653589793D / 30.0D) * paramFloat3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}




