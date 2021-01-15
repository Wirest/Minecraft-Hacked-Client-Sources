// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.util;

import org.lwjgl.input.Mouse;
import java.awt.Point;
import java.awt.Color;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class RenderUtil
{
    public static void scissorBox(final int x, final int y, final int xend, final int yend) {
        final int width = xend - x;
        final int height = yend - y;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        final int factor = sr.getScaleFactor();
        final int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
        GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
    }
    
    public static void setupLineSmooth() {
        GL11.glEnable(3042);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glHint(3154, 4354);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(32925);
        GL11.glEnable(32926);
        GL11.glShadeModel(7425);
    }
    
    public static void drawLine(final double startX, final double startY, final double startZ, final double endX, final double endY, final double endZ, final float thickness) {
        GL11.glPushMatrix();
        setupLineSmooth();
        GL11.glLineWidth(thickness);
        GL11.glBegin(1);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(32925);
        GL11.glDisable(32926);
        GL11.glPopMatrix();
    }
    
    public static void drawTexturedModalRect(final int par1, final int par2, final int par3, final int par4, final int par5, final int par6) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(par1 + 0, par2 + par6, 0.0, (par3 + 0) * var7, (par4 + par6) * var8);
        var10.addVertexWithUV(par1 + par5, par2 + par6, 0.0, (par3 + par5) * var7, (par4 + par6) * var8);
        var10.addVertexWithUV(par1 + par5, par2 + 0, 0.0, (par3 + par5) * var7, (par4 + 0) * var8);
        var10.addVertexWithUV(par1 + 0, par2 + 0, 0.0, (par3 + 0) * var7, (par4 + 0) * var8);
        var9.draw();
    }
    
    public static void drawTexturedModalRect(final int textureId, final int posX, final int posY, final int width, final int height) {
        final double halfHeight = height / 2;
        final double halfWidth = width / 2;
        GL11.glDisable(2884);
        GL11.glBindTexture(3553, textureId);
        GL11.glPushMatrix();
        GL11.glTranslated(posX + halfWidth, posY + halfHeight, 0.0);
        GL11.glScalef((float)width, (float)height, 0.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(4);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(1.0, 1.0);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(-1.0, 1.0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(-1.0, -1.0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(-1.0, -1.0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(1.0, -1.0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(1.0, 1.0);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glBindTexture(3553, 0);
        GL11.glPopMatrix();
    }
    
    public static int interpolateColor(final int rgba1, final int rgba2, final float percent) {
        final int r1 = rgba1 & 0xFF;
        final int g1 = rgba1 >> 8 & 0xFF;
        final int b1 = rgba1 >> 16 & 0xFF;
        final int a1 = rgba1 >> 24 & 0xFF;
        final int r2 = rgba2 & 0xFF;
        final int g2 = rgba2 >> 8 & 0xFF;
        final int b2 = rgba2 >> 16 & 0xFF;
        final int a2 = rgba2 >> 24 & 0xFF;
        final int r3 = (int)((r1 < r2) ? (r1 + (r2 - r1) * percent) : (r2 + (r1 - r2) * percent));
        final int g3 = (int)((g1 < g2) ? (g1 + (g2 - g1) * percent) : (g2 + (g1 - g2) * percent));
        final int b3 = (int)((b1 < b2) ? (b1 + (b2 - b1) * percent) : (b2 + (b1 - b2) * percent));
        final int a3 = (int)((a1 < a2) ? (a1 + (a2 - a1) * percent) : (a2 + (a1 - a2) * percent));
        return r3 | g3 << 8 | b3 << 16 | a3 << 24;
    }
    
    public static void setColor(final Color c) {
        GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
    }
    
    public static Color toColor(final int rgba) {
        final int r = rgba & 0xFF;
        final int g = rgba >> 8 & 0xFF;
        final int b = rgba >> 16 & 0xFF;
        final int a = rgba >> 24 & 0xFF;
        return new Color(r, g, b, a);
    }
    
    public static int toRGBA(final Color c) {
        return c.getRed() | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
    }
    
    public static void setColor(final int rgba) {
        final int r = rgba & 0xFF;
        final int g = rgba >> 8 & 0xFF;
        final int b = rgba >> 16 & 0xFF;
        final int a = rgba >> 24 & 0xFF;
        GL11.glColor4b((byte)r, (byte)g, (byte)b, (byte)a);
    }
    
    public static Point calculateMouseLocation() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        int scale = minecraft.gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        int scaleFactor;
        for (scaleFactor = 0; scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320 && minecraft.displayHeight / (scaleFactor + 1) >= 240; ++scaleFactor) {}
        return new Point(Mouse.getX() / scaleFactor, minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1);
    }
}
