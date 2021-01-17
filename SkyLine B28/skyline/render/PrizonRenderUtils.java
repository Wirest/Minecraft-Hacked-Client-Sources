package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import skyline.specc.SkyLine;
import skyline.specc.utils.Wrapper;

public class PrizonRenderUtils {
	public static int r1;
	public static int g1;
	public static boolean red;
	public static boolean green;
	public static void renderOne() {
		GL11.glPushAttrib(1048575);
		GL11.glDisable(3008);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(3.0f);
		GL11.glEnable(2848);
		GL11.glEnable(2960);
		GL11.glClear(1024);
		GL11.glClearStencil(15);
		GL11.glStencilFunc(512, 1, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1028, 6913);
	}

	public static void renderTwo() {
		GL11.glStencilFunc(512, 0, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1028, 6914);
	}

	public static void renderThree() {
		GL11.glStencilFunc(514, 1, 15);
		GL11.glStencilOp(7680, 7680, 7680);
		GL11.glPolygonMode(1028, 6913);
	}

	public static void drawFilledCircle(final double x, final double y, final double r, final int c) {
		final float f = (c >> 24 & 0xFF) / 255.0f;
		final float f2 = (c >> 16 & 0xFF) / 255.0f;
		final float f3 = (c >> 8 & 0xFF) / 255.0f;
		final float f4 = (c & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(6);
		for (int i = 0; i <= 360; ++i) {
			final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
			final double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}
		GL11.glEnd();
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
	}

	public static void drawChestESP(final double d1, final double d2, final double d3, final Color color) {
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(d1 + 1.0, d2 + 1.0, d3 + 1.0, d1, d2, d3), 9109504);
	}

	public static void drawBlockESP(final double x, final double y, final double z, final float red, final float green,
			final float blue, final float alpha, final float lineRed, final float lineGreen, final float lineBlue,
			final float lineAlpha, final float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void renderFour(final Mineman mc, final Entity renderEntity) {
		float[] color = { 0.0f, 0.9f, 0.0f };
		Label_0237: {
			if (renderEntity instanceof EntityLivingBase) {
				final EntityLivingBase entity = (EntityLivingBase) renderEntity;
				final float distance = mc.thePlayer.getDistanceToEntity(entity);
				if (entity instanceof EntityPlayer) {
					SkyLine.getVital();
					if (SkyLine.getManagers().getFriendManager().hasFriend(entity.getName())) {
						color = new float[] { 0.3f, 0.7f, 1.0f };
						break Label_0237;
					}
				}
				if (entity.isInvisibleToPlayer(mc.thePlayer)) {
					color = new float[] { 1.0f, 0.9f, 0.0f };
				} else if (entity.hurtTime > 0) {
					color = new float[] { 1.0f, 0.66f, 0.0f };
				} else if (distance <= 3.9f) {
					color = new float[] { 0.9f, 0.0f, 0.0f };
				}
			} else {
				final float distance2 = mc.thePlayer.getDistanceToEntity(renderEntity);
				if (renderEntity.isInvisibleToPlayer(mc.thePlayer)) {
					color = new float[] { 1.0f, 0.9f, 0.0f };
				} else if (distance2 <= 3.9f) {
					color = new float[] { 0.9f, 0.0f, 0.0f };
				}
			}
		}
		GlStateManager.color(color[0], color[1], color[2], 1.0f);
		renderFour();
	}

	public static void renderFour() {
		GL11.glDepthMask(false);
		GL11.glDisable(2929);
		GL11.glEnable(10754);
		GL11.glPolygonOffset(1.0f, -2000000.0f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
	}

	public static void renderFive() {
		GL11.glPolygonOffset(1.0f, 2000000.0f);
		GL11.glDisable(10754);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2960);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glEnable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(3008);
		GL11.glPopAttrib();
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

	public static void drawFilledBox(final AxisAlignedBB bb) {
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
		worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
		worldRenderer.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
		tessellator.draw();
	}

	public static void drawRect(final double left, final double top, final double right, final double bottom,
			final int color) {
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		final float red = (color >> 16 & 0xFF) / 255.0f;
		final float green = (color >> 8 & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		final Tessellator var9 = Tessellator.getInstance();
		final WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		var10.startDrawingQuads();
		var10.addVertex(left, bottom, 0.0);
		var10.addVertex(right, bottom, 0.0);
		var10.addVertex(right, top, 0.0);
		var10.addVertex(left, top, 0.0);
		var9.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static void drawGradientRect(final double left, final double top, final double right, final double bottom,
			final int startColor, final int endColor) {
		final float var7 = (startColor >> 24 & 0xFF) / 255.0f;
		final float var8 = (startColor >> 16 & 0xFF) / 255.0f;
		final float var9 = (startColor >> 8 & 0xFF) / 255.0f;
		final float var10 = (startColor & 0xFF) / 255.0f;
		final float var11 = (endColor >> 24 & 0xFF) / 255.0f;
		final float var12 = (endColor >> 16 & 0xFF) / 255.0f;
		final float var13 = (endColor >> 8 & 0xFF) / 255.0f;
		final float var14 = (endColor & 0xFF) / 255.0f;
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		final Tessellator var15 = Tessellator.getInstance();
		final WorldRenderer var16 = var15.getWorldRenderer();
		var16.startDrawingQuads();
		var16.func_178960_a(var8, var9, var10, var7);
		var16.addVertex(right, top, 0.0);
		var16.addVertex(left, top, 0.0);
		var16.func_178960_a(var12, var13, var14, var11);
		var16.addVertex(left, bottom, 0.0);
		var16.addVertex(right, bottom, 0.0);
		var15.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
	}

	public static void drawBorderedRect(final double left, final double top, final double right, final double bottom,
			final float borderWidth, final int borderColor, final int color) {
		final float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		final float red = (borderColor >> 16 & 0xFF) / 255.0f;
		final float green = (borderColor >> 8 & 0xFF) / 255.0f;
		final float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		drawRect(left, top, right, bottom, color);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0f) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		tessellator.draw();
		GL11.glLineWidth(2.0f);
		if (borderWidth == 1.0f) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawHollowRect(final double left, final double top, final double right, final double bottom,
			final float borderWidth, final int borderColor) {
		final float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		final float red = (borderColor >> 16 & 0xFF) / 255.0f;
		final float green = (borderColor >> 8 & 0xFF) / 255.0f;
		final float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		GL11.glEnable(2848);
		GL11.glLineWidth(borderWidth);
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		tessellator.draw();
		GL11.glLineWidth(2.0f);
		GL11.glDisable(2848);
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedRect(final double left, final double top, final double right, final double bottom,
			final int borderColor, final int color) {
		drawBorderedRect(left, top, right, bottom, 1.0f, borderColor, color);
	}

	public static void drawBorderedGradientRect(final double left, final double top, final double right,
			final double bottom, final float borderWidth, final int borderColor, final int startColor,
			final int endColor) {
		final float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		final float red = (borderColor >> 16 & 0xFF) / 255.0f;
		final float green = (borderColor >> 8 & 0xFF) / 255.0f;
		final float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		drawGradientRect(left, top, right, bottom, startColor, endColor);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0f) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, top, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		tessellator.draw();
		GL11.glLineWidth(2.0f);
		if (borderWidth == 1.0f) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedGradientRect(final double left, final double top, final double right,
			final double bottom, final int borderColor, final int startColor, final int endColor) {
		drawBorderedGradientRect(left, top, right, bottom, 1.0f, borderColor, startColor, endColor);
	}

	public static ScaledResolution newScaledResolution() {
		return new ScaledResolution(Mineman.getMinecraft(), Mineman.getMinecraft().displayWidth,
				Mineman.getMinecraft().displayHeight);
	}

	/*
	 * 
	 * @Author Slox
	 * 
	 */

	public static void renderOne1() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(3);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glClearStencil(0xF);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
	}

	public static void renderTwo2() {
		GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}

	public static void renderThree3() {
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
	}

	public static void renderFour4(int r, int g, int b) {
		setColor(new Color(r, g, b));
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glPolygonOffset(1.0F, -2000000F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}

	public static void renderFive5() {
		GL11.glPolygonOffset(1.0F, 2000000F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glPopAttrib();
	}

	public static void setColor(Color c) {
		GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

	public static void drawOutlinedBoundingBox(final AxisAlignedBB aabb) {
		final WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		final Tessellator tessellator = Tessellator.instance;
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator.draw();
	}

	public static void drawBoundingBox(final AxisAlignedBB aabb) {
		final WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
		final Tessellator tessellator = Tessellator.instance;
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

	public static class ColorUtils {
		public static int RGBtoHEX(final int r, final int g, final int b, final int a) {
			return (a << 24) + (r << 16) + (g << 8) + b;
		}

		public static Color getRainbow(final long offset, final float fade) {
			final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
			final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))),
					16);
			final Color c = new Color((int) color);
			return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade,
					c.getAlpha() / 255.0f);
		}

		public static Color glColor(final int color, final float alpha) {
			final float red = (color >> 16 & 0xFF) / 255.0f;
			final float green = (color >> 8 & 0xFF) / 255.0f;
			final float blue = (color & 0xFF) / 255.0f;
			GL11.glColor4f(red, green, blue, alpha);
			return new Color(red, green, blue, alpha);
		}

		public void glColor(final Color color) {
			GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
					color.getAlpha() / 255.0f);
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

		public static int transparency(final float h, final double alpha) {
			final Color c = new Color((int)h);
			if(r1 < 1000 && !red){
				r1++;
			}else{
				red = true;
			}
			if(red && r1 > 0){
				r1--;
			}else if(red){
				red = false;
			}
			if(g1 < 1000 && !green){
				g1++;
			}else{
				green = true;
			}
			if(green && g1 > 0){
				g1--;
			}else if(red){
				green = false;
			}
			//Wrapper.getPlayer().addChatMessage(h + "");
			final double r = 0.003921569f * (((255+-(h/2)) - r1/20));
			final double g = 0.003921569f * ((150-(h) - g1/20));
			final float b = 0.003921569f * c.getBlue();
			return new Color((float)r, (float)g, 0, (float) alpha).getRGB();
		}

	}

	public static void drawCentredStringWithShadow(final String s, int x, final int y, final int colour) {
		x -= Wrapper.getFontRenderer().getStringWidth(s) / 2;
		Wrapper.getFontRenderer().drawStringWithShadow(s, x, y, colour);
	}

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

	public static void glColor(final int hex) {
		final float alpha = (hex >> 24 & 0xFF) / 255.0f;
		final float red = (hex >> 16 & 0xFF) / 255.0f;
		final float green = (hex >> 8 & 0xFF) / 255.0f;
		final float blue = (hex & 0xFF) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawBorderedRectReliant(final float x, final float y, final float x1, final float y1,
			final float lineWidth, final int inside, final int border) {
		enableGL2D();
		drawRect(x, y, x1, y1, inside);
		glColor(border);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(3);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		disableGL2D();
	}

	public static void drawBorderedRect(final double x, final double y, final double x2, final double y2,
			final double thickness, final int inside, final int outline) {
		double fix = 0.0;
		if (thickness < 1.0) {
			fix = 1.0;
		}
		drawRect(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
		drawRect(x, y + 1.0 - fix, x + thickness, y2, outline);
		drawRect(x, y, x2 - 1.0 + fix, y + thickness, outline);
		drawRect(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
		drawRect(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
	}

	public static void drawBorderedRect2(final double x, final double y, final double x2, final double y2,
			final double thickness, final int inside, final int outline) {
		double fix = 0.0;
		if (thickness < 1.0) {
			fix = 1.0;
		}
		drawRect(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
		drawRect(x, y + 1.0 - fix, x + thickness, y2, outline);
		drawRect(x, y, x2 - 1.0 + fix, y + thickness, outline);
		drawRect(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
		drawRect(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
	}

	public static int withAlpha(final int c, final float a) {
		final float r = (c >> 16 & 0xFF) / 255.0f;
		final float g = (c >> 8 & 0xFF) / 255.0f;
		final float b = (c & 0xFF) / 255.0f;
		return new Color(r, g, b, a).getRGB();
	}

	public static double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static void drawstring(final String str, final float x, final float y, final int color) {
		Mineman.getMinecraft().fontRendererObj.drawString(str, x, y, color);
	}
}