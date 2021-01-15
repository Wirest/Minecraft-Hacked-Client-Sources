package me.aristhena.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Timer;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

import me.aristhena.client.friend.FriendManager;

public class SallosRender {
	   public static class R2DUtils
	   {
	        public static void enableGL2D() {
	            GL11.glDisable(2929);
	            GL11.glEnable(3042);
	            GL11.glDisable(3553);
	            GL11.glBlendFunc(770, 771);
	            GL11.glDepthMask(true);
	            GL11.glEnable(2848);
	            GL11.glHint(3154, 4354);
	            GL11.glHint(3155, 4354);
	        }
	        
	        public static void disableGL2D() {
	            GL11.glEnable(3553);
	            GL11.glDisable(3042);
	            GL11.glEnable(2929);
	            GL11.glDisable(2848);
	            GL11.glHint(3154, 4352);
	            GL11.glHint(3155, 4352);
	        }
	        
	        public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
	            enableGL2D();
	            Helper.colorUtils().glColor(color);
	            drawRect(x, y, x1, y1);
	            disableGL2D();
	        }
	        
	        public static void drawBorderedRect(final float x, final float y, final float x1, final float y1, final float width, final int borderColor) {
	            enableGL2D();
	            Helper.colorUtils().glColor(borderColor);
	            drawRect(x + width, y, x1 - width, y + width);
	            drawRect(x, y, x + width, y1);
	            drawRect(x1 - width, y, x1, y1);
	            drawRect(x + width, y1 - width, x1 - width, y1);
	            disableGL2D();
	        }
	        
	        public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
	            enableGL2D();
	            x *= 2.0f;
	            x1 *= 2.0f;
	            y *= 2.0f;
	            y1 *= 2.0f;
	            GL11.glScalef(0.5f, 0.5f, 0.5f);
	            drawVLine(x, y, y1, borderC);
	            drawVLine(x1 - 1.0f, y, y1, borderC);
	            drawHLine(x, x1 - 1.0f, y, borderC);
	            drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
	            drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
	            GL11.glScalef(2.0f, 2.0f, 2.0f);
	            disableGL2D();
	        }
	        
	        public static void drawGradientRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
	            enableGL2D();
	            GL11.glShadeModel(7425);
	            GL11.glBegin(7);
	            Helper.colorUtils().glColor(topColor);
	            GL11.glVertex2f(x, y1);
	            GL11.glVertex2f(x1, y1);
	            Helper.colorUtils().glColor(bottomColor);
	            GL11.glVertex2f(x1, y);
	            GL11.glVertex2f(x, y);
	            GL11.glEnd();
	            GL11.glShadeModel(7424);
	            disableGL2D();
	        }
	        
	        public static void drawHLine(float x, float y, final float x1, final int y1) {
	            if (y < x) {
	                final float var5 = x;
	                x = y;
	                y = var5;
	            }
	            drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
	        }
	        
	        public static void drawVLine(final float x, float y, float x1, final int y1) {
	            if (x1 < y) {
	                final float var5 = y;
	                y = x1;
	                x1 = var5;
	            }
	            drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
	        }
	        
	        public static void drawHLine(float x, float y, final float x1, final int y1, final int y2) {
	            if (y < x) {
	                final float var5 = x;
	                x = y;
	                y = var5;
	            }
	            drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
	        }
	        
	        public static void drawRect(final float x, final float y, final float x1, final float y1) {
	            GL11.glBegin(7);
	            GL11.glVertex2f(x, y1);
	            GL11.glVertex2f(x1, y1);
	            GL11.glVertex2f(x1, y);
	            GL11.glVertex2f(x, y);
	            GL11.glEnd();
	        }
	        
	        public static void drawTri(final double x1, final double y1, final double x2, final double y2, final double x3, final double y3, final double width, final Color c) {
	            GL11.glEnable(3042);
	            GL11.glDisable(3553);
	            GL11.glEnable(2848);
	            GL11.glBlendFunc(770, 771);
	            Helper.colorUtils().glColor(c);
	            GL11.glLineWidth((float)width);
	            GL11.glBegin(3);
	            GL11.glVertex2d(x1, y1);
	            GL11.glVertex2d(x2, y2);
	            GL11.glVertex2d(x3, y3);
	            GL11.glEnd();
	            GL11.glDisable(2848);
	            GL11.glEnable(3553);
	            GL11.glDisable(3042);
	        }
	        
	        public static void drawFilledCircle(final int x, final int y, final double radius, final Color c) {
	            Helper.colorUtils().glColor(c);
	            GlStateManager.enableAlpha();
	            GlStateManager.enableBlend();
	            GL11.glDisable(3553);
	            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	            GlStateManager.alphaFunc(516, 0.001f);
	            final Tessellator tess = Tessellator.getInstance();
	            final WorldRenderer render = tess.getWorldRenderer();
	            for (double i = 0.0; i < 360.0; ++i) {
	                final double cs = i * 3.141592653589793 / 180.0;
	                final double ps = (i - 1.0) * 3.141592653589793 / 180.0;
	                final double[] outer = { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius };
	                render.startDrawing(6);
	                render.addVertex(x + outer[2], y + outer[3], 0.0);
	                render.addVertex(x + outer[0], y + outer[1], 0.0);
	                render.addVertex(x, y, 0.0);
	                tess.draw();
	            }
	            GlStateManager.disableBlend();
	            GlStateManager.alphaFunc(516, 0.1f);
	            GlStateManager.disableAlpha();
	            GL11.glEnable(3553);
	        }
	    }
	    
	    public static class R3DUtils
	    {
	        public static void startDrawing() {
	            GL11.glEnable(3042);
	            GL11.glEnable(3042);
	            GL11.glBlendFunc(770, 771);
	            GL11.glEnable(2848);
	            GL11.glDisable(3553);
	            GL11.glDisable(2929);
	            ClientUtils.mc().entityRenderer.setupCameraTransform(ClientUtils.mc().timer.renderPartialTicks, 0);
	        }
	        
	        public static void stopDrawing() {
	            GL11.glDisable(3042);
	            GL11.glEnable(3553);
	            GL11.glDisable(2848);
	            GL11.glDisable(3042);
	            GL11.glEnable(2929);
	        }
	        
	        public void drawRombo(final double x, double y, final double z) {
	            ClientUtils.mc().entityRenderer.setupCameraTransform(ClientUtils.mc().timer.renderPartialTicks, 0);
	            ++y;
	            GL11.glBegin(4);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glVertex3d(x, y + 1.0, z);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x + 0.5, y, z);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x, y, z - 0.5);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glEnd();
	            GL11.glBegin(4);
	            GL11.glVertex3d(x - 0.5, y, z);
	            GL11.glVertex3d(x, y - 1.0, z);
	            GL11.glVertex3d(x, y, z + 0.5);
	            GL11.glEnd();
	        }
	        
	        public static void RenderLivingEntityBox(final Entity entity, final float partialTicks, final boolean otherMode) {
	            GlStateManager.pushMatrix();
	            GL11.glBlendFunc(770, 771);
	            GL11.glLineWidth(1.5f);
	            GL11.glDisable(3553);
	            GL11.glDisable(2929);
	            GL11.glDepthMask(false);
	            GL11.glEnable(3042);
	            GL11.glBlendFunc(770, 771);
	            GL11.glEnable(2848);
	            GL11.glDisable(2896);
	            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
	            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
	            final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
	            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
	            final boolean isPlayer = entity instanceof EntityPlayer;
	            int bordercolor = 15261919;
	            if (FriendManager.isFriend(entity.getName())) {
	                bordercolor = -196416532;
	            }
	            Helper.colorUtils();
	            ColorUtils.glColor(bordercolor, 1.0f);
	            if (otherMode) {
	                RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ), -1);
	                drawLines(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
	            }
	            else {
	                drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
	                Helper.colorUtils();
	                ColorUtils.glColor(bordercolor, 0.15f);
	                drawBoundingBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
	            }
	            GL11.glEnable(2896);
	            GL11.glEnable(3553);
	            GL11.glEnable(2929);
	            GL11.glDepthMask(true);
	            GL11.glDisable(2848);
	            GL11.glDisable(3042);
	            GlStateManager.popMatrix();
	        }
	        
	        public static void drawLines(final AxisAlignedBB bb) {
	            final Tessellator tessellator = Tessellator.getInstance();
	            final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
	            worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
	            worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
	            worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
	            worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
	            tessellator.draw();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
	            worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawing(2);
	            worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
	            worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
	            tessellator.draw();
	        }
	        
	        public static void drawOutlinedBox(final AxisAlignedBB box) {
	            if (box == null) {
	                return;
	            }
	            ClientUtils.mc().entityRenderer.setupCameraTransform(ClientUtils.mc().timer.renderPartialTicks, 0);
	            GL11.glBegin(3);
	            GL11.glVertex3d(box.minX, box.minY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.minY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.minY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.minY, box.minZ);
	            GL11.glEnd();
	            GL11.glBegin(3);
	            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
	            GL11.glEnd();
	            GL11.glBegin(1);
	            GL11.glVertex3d(box.minX, box.minY, box.minZ);
	            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.minY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
	            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
	            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.minY, box.maxZ);
	            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
	            GL11.glEnd();
	        }
	        
	        public static void drawBoundingBox(final AxisAlignedBB aabb) {
	            final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
	            final Tessellator tessellator = Tessellator.getInstance();
	            ClientUtils.mc().entityRenderer.setupCameraTransform(ClientUtils.mc().timer.renderPartialTicks, 0);
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            tessellator.draw();
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            tessellator.draw();
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            tessellator.draw();
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            tessellator.draw();
	            worldRenderer.startDrawingQuads();
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
	            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
	            tessellator.draw();
	        }
	        
	        public static int getBlockColor(final Block block) {
	            int color = 0;
	            switch (Block.getIdFromBlock(block)) {
	                case 14:
	                case 41: {
	                    color = -1711477173;
	                    break;
	                }
	                case 15:
	                case 42: {
	                    color = -1715420992;
	                    break;
	                }
	                case 16:
	                case 173: {
	                    color = -1724434633;
	                    break;
	                }
	                case 21:
	                case 22: {
	                    color = -1726527803;
	                    break;
	                }
	                case 49: {
	                    color = -1724108714;
	                    break;
	                }
	                case 54:
	                case 146: {
	                    color = -1711292672;
	                    break;
	                }
	                case 56:
	                case 57:
	                case 138: {
	                    color = -1721897739;
	                    break;
	                }
	                case 61:
	                case 62: {
	                    color = -1711395081;
	                    break;
	                }
	                case 73:
	                case 74:
	                case 152: {
	                    color = -1711341568;
	                    break;
	                }
	                case 89: {
	                    color = -1712594866;
	                    break;
	                }
	                case 129:
	                case 133: {
	                    color = -1726489246;
	                    break;
	                }
	                case 130: {
	                    color = -1713438249;
	                    break;
	                }
	                case 52: {
	                    color = 805728308;
	                    break;
	                }
	                default: {
	                    color = -1711276033;
	                    break;
	                }
	            }
	            return (color == 0) ? 806752583 : color;
	        }
	    }
	    
	    public static class ColorUtils
	    {
	        public int RGBtoHEX(final int r, final int g, final int b, final int a) {
	            return (a << 24) + (r << 16) + (g << 8) + b;
	        }
	        
	        public Color getRainbow(final long offset, final float fade) {
	            final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
	            final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
	            final Color c = new Color((int)color);
	            return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
	        }
	        
	        public static Color glColor(final int color, final float alpha) {
	            final float red = (color >> 16 & 0xFF) / 255.0f;
	            final float green = (color >> 8 & 0xFF) / 255.0f;
	            final float blue = (color & 0xFF) / 255.0f;
	            GL11.glColor4f(red, green, blue, alpha);
	            return new Color(red, green, blue, alpha);
	        }
	        
	        public void glColor(final Color color) {
	            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
	        }
	        
	        public Color glColor(final int hex) {
	            final float alpha = (hex >> 24 & 0xFF) / 256.0f;
	            final float red = (hex >> 16 & 0xFF) / 255.0f;
	            final float green = (hex >> 8 & 0xFF) / 255.0f;
	            final float blue = (hex & 0xFF) / 255.0f;
	            GL11.glColor4f(red, green, blue, alpha);
	            return new Color(red, green, blue, alpha);
	        }
	        
	        public Color glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
	            final float red = 0.003921569f * redRGB;
	            final float green = 0.003921569f * greenRGB;
	            final float blue = 0.003921569f * blueRGB;
	            GL11.glColor4f(red, green, blue, alpha);
	            return new Color(red, green, blue, alpha);
	        }
	        
	        public static int transparency(final int color, final double alpha) {
	            final Color c = new Color(color);
	            final float r = 0.003921569f * c.getRed();
	            final float g = 0.003921569f * c.getGreen();
	            final float b = 0.003921569f * c.getBlue();
	            return new Color(r, g, b, (float)alpha).getRGB();
	        }
	        
	        public static float[] getRGBA(final int color) {
	            final float a = (color >> 24 & 0xFF) / 255.0f;
	            final float r = (color >> 16 & 0xFF) / 255.0f;
	            final float g = (color >> 8 & 0xFF) / 255.0f;
	            final float b = (color & 0xFF) / 255.0f;
	            return new float[] { r, g, b, a };
	        }
	        
	        public static int intFromHex(final String hex) {
	            try {
	                return Integer.parseInt(hex, 15);
	            }
	            catch (NumberFormatException e) {
	                return -1;
	            }
	        }
	        
	        public static String hexFromInt(final int color) {
	            return hexFromInt(new Color(color));
	        }
	        
	        public static String hexFromInt(final Color color) {
	            return Integer.toHexString(color.getRGB()).substring(2);
	        }
	    }
	    
	    public static final class Stencil
	    {
	        private static final Stencil INSTANCE;
	        private final HashMap<Integer, StencilFunc> stencilFuncs;
	        private int layers;
	        private boolean renderMask;
	        
	        static {
	            INSTANCE = new Stencil();
	        }
	        
	        public Stencil() {
	            this.stencilFuncs = new HashMap<Integer, StencilFunc>();
	            this.layers = 1;
	        }
	        
	        public static Stencil getInstance() {
	            return Stencil.INSTANCE;
	        }
	        
	        public void setRenderMask(final boolean renderMask) {
	            this.renderMask = renderMask;
	        }
	        
	        public static void checkSetupFBO() {
	            final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
	            if (fbo != null && fbo.depthBuffer > -1) {
	                EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
	                final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
	                EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
	                EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	                EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
	                EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	                fbo.depthBuffer = -1;
	            }
	        }
	        
	        public static void setupFBO(final Framebuffer fbo) {
	            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
	            final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
	            EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
	            EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
	            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	        }
	        
	        public void startLayer() {
	            if (this.layers == 1) {
	                GL11.glClearStencil(0);
	                GL11.glClear(1024);
	            }
	            GL11.glEnable(2960);
	            ++this.layers;
	            if (this.layers > this.getMaximumLayers()) {
	                System.out.println("StencilUtil: Reached maximum amount of layers!");
	                this.layers = 1;
	            }
	        }
	        
	        public void stopLayer() {
	            if (this.layers == 1) {
	                System.out.println("StencilUtil: No layers found!");
	                return;
	            }
	            --this.layers;
	            if (this.layers == 1) {
	                GL11.glDisable(2960);
	            }
	            else {
	                final StencilFunc lastStencilFunc = this.stencilFuncs.remove(this.layers);
	                if (lastStencilFunc != null) {
	                    lastStencilFunc.use();
	                }
	            }
	        }
	        
	        public void clear() {
	            GL11.glClearStencil(0);
	            GL11.glClear(1024);
	            this.stencilFuncs.clear();
	            this.layers = 1;
	        }
	        
	        public void setBuffer() {
	            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, this.getMaximumLayers(), 7681, 7680, 7680));
	        }
	        
	        public void setBuffer(final boolean set) {
	            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, set ? this.layers : (this.layers - 1), this.getMaximumLayers(), 7681, 7681, 7681));
	        }
	        
	        public void cropOutside() {
	            this.setStencilFunc(new StencilFunc(this, 517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
	        }
	        
	        public void cropInside() {
	            this.setStencilFunc(new StencilFunc(this, 514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
	        }
	        
	        public void setStencilFunc(final StencilFunc stencilFunc) {
	            GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
	            GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
	            this.stencilFuncs.put(this.layers, stencilFunc);
	        }
	        
	        public StencilFunc getStencilFunc() {
	            return this.stencilFuncs.get(this.layers);
	        }
	        
	        public int getLayer() {
	            return this.layers;
	        }
	        
	        public int getStencilBufferSize() {
	            return GL11.glGetInteger(3415);
	        }
	        
	        public int getMaximumLayers() {
	            return (int)(Math.pow(2.0, this.getStencilBufferSize()) - 1.0);
	        }
	        
	        public void createCirlce(final double x, final double y, final double radius) {
	            GL11.glBegin(6);
	            for (int i = 0; i <= 360; ++i) {
	                final double sin = Math.sin(i * 3.141592653589793 / 180.0) * radius;
	                final double cos = Math.cos(i * 3.141592653589793 / 180.0) * radius;
	                GL11.glVertex2d(x + sin, y + cos);
	            }
	            GL11.glEnd();
	        }
	        
	        public void createRect(final double x, final double y, final double x2, final double y2) {
	            GL11.glBegin(7);
	            GL11.glVertex2d(x, y2);
	            GL11.glVertex2d(x2, y2);
	            GL11.glVertex2d(x2, y);
	            GL11.glVertex2d(x, y);
	            GL11.glEnd();
	        }
	        
	        public static class StencilFunc
	        {
	            public static int func_func;
	            public static int func_ref;
	            public static int func_mask;
	            public static int op_fail;
	            public static int op_zfail;
	            public static int op_zpass;
	            
	            public StencilFunc(final Stencil paramStencil, final int func_func, final int func_ref, final int func_mask, final int op_fail, final int op_zfail, final int op_zpass) {
	                StencilFunc.func_func = func_func;
	                StencilFunc.func_ref = func_ref;
	                StencilFunc.func_mask = func_mask;
	                StencilFunc.op_fail = op_fail;
	                StencilFunc.op_zfail = op_zfail;
	                StencilFunc.op_zpass = op_zpass;
	            }
	            
	            public void use() {
	                GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
	                GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
	            }
	        }
	    }
	    
	    public static class Camera
	    {
	        private final Minecraft mc;
	        private Timer timer;
	        private double posX;
	        private double posY;
	        private double posZ;
	        private float rotationYaw;
	        private float rotationPitch;
	        
	        public Camera(final Entity entity) {
	            this.mc = Minecraft.getMinecraft();
	            if (this.timer == null) {
	                this.timer = this.mc.timer;
	            }
	            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
	            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
	            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
	            this.setRotationYaw(entity.rotationYaw);
	            this.setRotationPitch(entity.rotationPitch);
	            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
	                final EntityPlayer living1 = (EntityPlayer)entity;
	                this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
	                this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
	            }
	            else if (entity instanceof EntityLivingBase) {
	                final EntityLivingBase living2 = (EntityLivingBase)entity;
	                this.setRotationYaw(living2.rotationYawHead);
	            }
	        }
	        
	        public Camera(final Entity entity, final double offsetX, final double offsetY, final double offsetZ, final double offsetRotationYaw, final double offsetRotationPitch) {
	            this.mc = Minecraft.getMinecraft();
	            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
	            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
	            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
	            this.setRotationYaw(entity.rotationYaw);
	            this.setRotationPitch(entity.rotationPitch);
	            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
	                final EntityPlayer player = (EntityPlayer)entity;
	                this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
	                this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
	            }
	            this.posX += offsetX;
	            this.posY += offsetY;
	            this.posZ += offsetZ;
	            this.rotationYaw += (float)offsetRotationYaw;
	            this.rotationPitch += (float)offsetRotationPitch;
	        }
	        
	        public Camera(final double posX, final double posY, final double posZ, final float rotationYaw, final float rotationPitch) {
	            this.mc = Minecraft.getMinecraft();
	            this.setPosX(posX);
	            this.posY = posY;
	            this.posZ = posZ;
	            this.setRotationYaw(rotationYaw);
	            this.setRotationPitch(rotationPitch);
	        }
	        
	        public double getPosX() {
	            return this.posX;
	        }
	        
	        public void setPosX(final double posX) {
	            this.posX = posX;
	        }
	        
	        public double getPosY() {
	            return this.posY;
	        }
	        
	        public void setPosY(final double posY) {
	            this.posY = posY;
	        }
	        
	        public double getPosZ() {
	            return this.posZ;
	        }
	        
	        public void setPosZ(final double posZ) {
	            this.posZ = posZ;
	        }
	        
	        public float getRotationYaw() {
	            return this.rotationYaw;
	        }
	        
	        public void setRotationYaw(final float rotationYaw) {
	            this.rotationYaw = rotationYaw;
	        }
	        
	        public float getRotationPitch() {
	            return this.rotationPitch;
	        }
	        
	        public void setRotationPitch(final float rotationPitch) {
	            this.rotationPitch = rotationPitch;
	        }
	        
	        public static float[] getRotation(final double posX1, final double posY1, final double posZ1, final double posX2, final double posY2, final double posZ2) {
	            final float[] rotation = new float[2];
	            final double diffX = posX2 - posX1;
	            final double diffZ = posZ2 - posZ1;
	            final double diffY = posY2 - posY1;
	            final double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
	            final double pitch = -Math.toDegrees(Math.atan(diffY / dist));
	            rotation[1] = (float)pitch;
	            double yaw = 0.0;
	            if (diffZ >= 0.0 && diffX >= 0.0) {
	                yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
	            }
	            else if (diffZ >= 0.0 && diffX <= 0.0) {
	                yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
	            }
	            else if (diffZ <= 0.0 && diffX >= 0.0) {
	                yaw = -90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
	            }
	            else if (diffZ <= 0.0 && diffX <= 0.0) {
	                yaw = 90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
	            }
	            rotation[0] = (float)yaw;
	            return rotation;
	        }
	    }
	}

