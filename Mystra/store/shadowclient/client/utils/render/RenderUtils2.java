package store.shadowclient.client.utils.render;

import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;
import java.awt.*;
import net.minecraft.client.shader.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;

public class RenderUtils2 {
    public static void drawFilledBoundingBox(final AxisAlignedBB axisAlignedBB, final boolean b, final int n) {
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(n);
        if (b) {
            GL11.glDisable(2929);
        }
        color(n);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        instance.draw();
        if (b) {
            GL11.glEnable(2929);
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawFilledBoundingBox(final AxisAlignedBB axisAlignedBB, final boolean b, final int n, final float width) {
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(n);
        if (b) {
            GL11.glDisable(2929);
        }
        color(n);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        GL11.glLineWidth(width);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        instance.draw();
        if (b) {
            GL11.glEnable(2929);
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB axisAlignedBB, final boolean b, final int n) {
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(n);
        if (b) {
            GL11.glDisable(2929);
        }
        color(n);
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        instance.draw();
        if (b) {
            GL11.glEnable(2929);
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB axisAlignedBB, final boolean b, final int n, final float width) {
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(n);
        if (b) {
            GL11.glDisable(2929);
        }
        color(n);
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        GL11.glLineWidth(width);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        instance.draw();
        if (b) {
            GL11.glEnable(2929);
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedCircle(final float n, final float n2, final int n3, final int n4, final int n5) {
        final float n6 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n7 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n8 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n9 = (n5 >> 255) / 255.0f;
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(n7, n8, n9, n6);
        GL11.glEnable(2848);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        for (int i = 0; i <= 360; ++i) {
            worldRenderer.pos(n + Math.cos(Math.toRadians(i)) * n3, n2 + Math.sin(Math.toRadians(i)) * n3, 0.0).endVertex();
        }
        instance.draw();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawCircle(final double n, final double n2, final int n3, final int n4) {
        final float n5 = ((n4 & 0x18) >> 255) / 255.0f;
        final float n6 = ((n4 & 0x10) >> 255) / 255.0f;
        final float n7 = ((n4 & 0x8) >> 255) / 255.0f;
        final float n8 = (n4 >> 255) / 255.0f;
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(n6, n7, n8, n5);
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n, n2, 0.0).endVertex();
        for (int i = 0; i <= 360; ++i) {
            worldRenderer.pos(n + Math.sin(Math.toRadians(i)) * n3, n2 + Math.cos(Math.toRadians(i)) * n3, 0.0).endVertex();
        }
        instance.draw();
        GL11.glEnable(2848);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        for (int j = 0; j <= 360; ++j) {
            worldRenderer.pos(n + Math.sin(Math.toRadians(j)) * n3, n2 + Math.cos(Math.toRadians(j)) * n3, 0.0).endVertex();
        }
        instance.draw();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRoundedRect(float n, float n2, float n3, float n4, final int n5, final int n6) {
        final float n7 = ((n6 & 0x18) >> 255) / 255.0f;
        final float n8 = ((n6 & 0x10) >> 255) / 255.0f;
        final float n9 = ((n6 & 0x8) >> 255) / 255.0f;
        final float n10 = (n6 >> 255) / 255.0f;
        if (n < n3) {
            final float n11 = n;
            n = n3;
            n3 = n11;
        }
        if (n2 < n4) {
            final float n12 = n2;
            n2 = n4;
            n4 = n12;
        }
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(n8, n9, n10, n7);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n - n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n2, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n2, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n4, 0.0).endVertex();
        worldRenderer.pos(n - n5, n4, 0.0).endVertex();
        worldRenderer.pos(n3, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n3 + n5, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n3, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n, n4 + n5, 0.0).endVertex();
        worldRenderer.pos(n, n2 - n5, 0.0).endVertex();
        worldRenderer.pos(n - n5, n2 - n5, 0.0).endVertex();
        instance.draw();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n - n5, n2 - n5, 0.0).endVertex();
        for (int i = 0; i <= 90; ++i) {
            worldRenderer.pos(n + Math.cos(Math.toRadians(i)) * n5 - n5, n2 + Math.sin(Math.toRadians(i)) * n5 - n5, 0.0).endVertex();
        }
        instance.draw();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n - n5, n4 + n5, 0.0).endVertex();
        for (int j = 270; j <= 360; ++j) {
            worldRenderer.pos(n + Math.cos(Math.toRadians(j)) * n5 - n5, n4 + Math.sin(Math.toRadians(j)) * n5 + n5, 0.0).endVertex();
        }
        instance.draw();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n3 + n5, n4 + n5, 0.0).endVertex();
        for (int k = 180; k <= 270; ++k) {
            worldRenderer.pos(n3 + Math.cos(Math.toRadians(k)) * n5 + n5, n4 + Math.sin(Math.toRadians(k)) * n5 + n5, 0.0).endVertex();
        }
        instance.draw();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n3 + n5, n2 - n5, 0.0).endVertex();
        for (int l = 90; l <= 180; ++l) {
            worldRenderer.pos(n3 + Math.cos(Math.toRadians(l)) * n5 + n5, n2 + Math.sin(Math.toRadians(l)) * n5 - n5, 0.0).endVertex();
        }
        instance.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawGradientRectSideways(final float n, final float n2, final float n3, final float n4, final int n5, final int n6) {
        final float n7 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n8 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n9 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n10 = (n5 >> 255) / 255.0f;
        final float n11 = ((n6 & 0x18) >> 255) / 255.0f;
        final float n12 = ((n6 & 0x10) >> 255) / 255.0f;
        final float n13 = ((n6 & 0x8) >> 255) / 255.0f;
        final float n14 = (n6 >> 255) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(n3, n2, 0.0).color(n12, n13, n14, n11).endVertex();
        worldRenderer.pos(n, n2, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n4, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n3, n4, 0.0).color(n12, n13, n14, n11).endVertex();
        instance.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawGradientRect(final float n, final float n2, final float n3, final float n4, final int n5, final int n6) {
        final float n7 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n8 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n9 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n10 = (n5 >> 255) / 255.0f;
        final float n11 = ((n6 & 0x18) >> 255) / 255.0f;
        final float n12 = ((n6 & 0x10) >> 255) / 255.0f;
        final float n13 = ((n6 & 0x8) >> 255) / 255.0f;
        final float n14 = (n6 >> 255) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(n3, n2, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n2, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n4, 0.0).color(n12, n13, n14, n11).endVertex();
        worldRenderer.pos(n3, n4, 0.0).color(n12, n13, n14, n11).endVertex();
        instance.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawLine(final float n, final float n2, final float n3, final float n4, final float width, final int n5) {
        final float n6 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n7 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n8 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n9 = (n5 >> 255) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(n, n2, 0.0).color(n7, n8, n9, n6).endVertex();
        worldRenderer.pos(n3, n4, 0.0).color(n7, n8, n9, n6).endVertex();
        instance.draw();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawLine(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final float width, final int n7) {
        final float n8 = ((n7 & 0x18) >> 255) / 255.0f;
        final float n9 = ((n7 & 0x10) >> 255) / 255.0f;
        final float n10 = ((n7 & 0x8) >> 255) / 255.0f;
        final float n11 = (n7 >> 255) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GlStateManager.color(n9, n10, n11, n8);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n, n2, n3).endVertex();
        worldRenderer.pos(n4, n5, n6).endVertex();
        instance.draw();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    public static void drawOutlinedRect(final float n, final float n2, final float n3, final float n4, final float width, final int n5, final int n6) {
        final float n7 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n8 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n9 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n10 = (n5 >> 255) / 255.0f;
        drawRect(n, n2, n3, n4, n6);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(2, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(n, n2, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n3, n2, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n3, n4, 0.0).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n4, 0.0).color(n8, n9, n10, n7).endVertex();
        instance.draw();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRect(float n, float n2, float n3, float n4, final int n5) {
        if (n < n3) {
            final float n6 = n;
            n = n3;
            n3 = n6;
        }
        if (n2 < n4) {
            final float n7 = n2;
            n2 = n4;
            n4 = n7;
        }
        final float n8 = ((n5 & 0x18) >> 255) / 255.0f;
        final float n9 = ((n5 & 0x10) >> 255) / 255.0f;
        final float n10 = ((n5 & 0x8) >> 255) / 255.0f;
        final float n11 = (n5 >> 255) / 255.0f;
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(n9, n10, n11, n8);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n, n4, 0.0).endVertex();
        worldRenderer.pos(n3, n4, 0.0).endVertex();
        worldRenderer.pos(n3, n2, 0.0).endVertex();
        worldRenderer.pos(n, n2, 0.0).endVertex();
        instance.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void color(final int n) {
        GlStateManager.color(((n & 0x10) >> 255) / 255.0f, ((n & 0x8) >> 255) / 255.0f, (n >> 255) / 255.0f, ((n & 0x18) >> 255) / 255.0f);
    }
    
    public static int getColorToDistance(final Entity entity) {
        final double n = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        if (n >= 50.0) {
            return Color.GREEN.getRGB();
        }
        if (n < 50.0 && n >= 20.0) {
            return Color.YELLOW.getRGB();
        }
        return Color.RED.getRGB();
    }
    
    public static void drawFrameBuffer(final Framebuffer framebuffer) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final int scaledWidth = scaledResolution.getScaledWidth();
        final int scaledHeight = scaledResolution.getScaledHeight();
        GlStateManager.pushMatrix();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(0.0, scaledHeight, 0.0).tex(0.0, 0.0).endVertex();
        worldRenderer.pos(scaledWidth, scaledHeight, 0.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(scaledWidth, 0.0, 0.0).tex(1.0, 1.0).endVertex();
        instance.draw();
        GlStateManager.popMatrix();
    }
    
    public static void scissor(final int n, final int n2, final int n3, final int n4) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glScissor(n * scaledResolution.getScaleFactor(), (scaledResolution.getScaledHeight() - n4) * scaledResolution.getScaleFactor(), (n3 - n) * scaledResolution.getScaleFactor(), (n4 - n2) * scaledResolution.getScaleFactor());
    }
    
    public static int[] getMousePosition(final ScaledResolution scaledResolution) {
        final int scaledWidth = scaledResolution.getScaledWidth();
        final int scaledHeight = scaledResolution.getScaledHeight();
        return new int[] { -Minecraft.getMinecraft().displayWidth, Mouse.getY() * scaledHeight - -Minecraft.getMinecraft().displayHeight - 1 };
    }
}