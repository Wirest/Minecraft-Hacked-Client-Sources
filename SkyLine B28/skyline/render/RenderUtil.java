package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import java.awt.Color;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Mineman;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;
import skyline.specc.utils.Wrapper;

public class RenderUtil {
	protected static int glTextureId;

	private static final net.minecraft.client.renderer.culling.Frustrum frustum;

	static {
		RenderUtil.glTextureId = -1;
		frustum = new Frustrum();
	}
	public static double interpolate(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Mineman.getMinecraft().timer.renderPartialTicks;
	}

	public static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
		return lastI + (i - lastI) * ticks - ownI;
	}

	public static boolean isInFrustumView(final Entity ent) {
		final Entity current = Mineman.getMinecraft().getRenderViewEntity();
		final double x = interpolate(current.posX, current.lastTickPosX);
		final double y = interpolate(current.posY, current.lastTickPosY);
		final double z = interpolate(current.posZ, current.lastTickPosZ);
		RenderUtil.frustum.setPosition(x, y, z);
		return RenderUtil.frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
	}

	public static void drawESP(final EntityLivingBase ent, final float pTicks, final int hexColor, final int hexColorIn) {
		//System.out.println("Error");
		final double x = getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
		final double y = getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
		final double z = getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
		boundingBox(ent, x, y, z, hexColor, hexColorIn);
	}

	public static void boundingBox(final Entity entity, final double x, final double y, final double z, final int color, final int colorIn) {
		GlStateManager.pushMatrix();
		GL11.glLineWidth(1.0f);
		final AxisAlignedBB var11 = entity.getEntityBoundingBox();
		final AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
		if (color != 0) {
			GlStateManager.disableDepth();
			filledBox(var12, colorIn, true);
			GlStateManager.disableLighting();
			RenderGlobal.drawOutlinedBoundingBox(var12, color);
		}
		GlStateManager.popMatrix();
	}

	public static void filledBox(final AxisAlignedBB boundingBox, final int color, final boolean shouldColor) {
		GlStateManager.pushMatrix();
		final float var11 = (color >> 24 & 0xFF) / 255.0f;
		final float var12 = (color >> 16 & 0xFF) / 255.0f;
		final float var13 = (color >> 8 & 0xFF) / 255.0f;
		final float var14 = (color & 0xFF) / 255.0f;
		final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		if (shouldColor) {
			GlStateManager.color(var12, var13, var14, var11);
		}
		final byte draw = 7;
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		Tessellator.getInstance().draw();
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		Tessellator.getInstance().draw();
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		Tessellator.getInstance().draw();
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		Tessellator.getInstance().draw();
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		Tessellator.getInstance().draw();
		worldRenderer.startDrawing(draw);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		Tessellator.getInstance().draw();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	public static void draw2DCorner(final Entity e, final double posX, final double posY, final double posZ, final int color) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		GL11.glNormal3f(0.0f, 0.0f, 0.0f);
		GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.scale(-0.1, -0.1, 0.1);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GlStateManager.depthMask(true);
		drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
		drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
		drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
		drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
		drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
		drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
		drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
		drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
		drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
		drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
		drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
		drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
		drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
		drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
		drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
		drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GlStateManager.popMatrix();
	}



	public static void findError(final String name) {
		String string = GLU.gluErrorString(GL11.glGetError());
		if (!string.toLowerCase().contains("no error")) {
			string = String.valueOf(string) + " at '" + name + "'.";
			if (Wrapper.getPlayer() != null) {
				new ChatBuilder().appendText("Vital ", ChatColor.BLUE).appendText(": ", ChatColor.DARK_GRAY)
						.appendText("OpenGL error: " + string, ChatColor.RED).send();
			} else {
				System.out.println(string);
			}
		}
	}

	public static void setColor(final Color c) {
		GL11.glColor4d(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
	}

	public static void drawTri(final double x1, final double y1, final double x2, final double y2, final double x3,
			final double y3, final double width, final Color c) {
		GlStateManager.pushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glBlendFunc(770, 771);
		setColor(c);
		GL11.glLineWidth((float) width);
		GL11.glBegin(3);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x3, y3);
		GL11.glEnd();
		GlStateManager.popMatrix();
	}

	private static int getGlTextureId() {
		if (RenderUtil.glTextureId == -1) {
			RenderUtil.glTextureId = TextureUtil.glGenTextures();
		}
		return RenderUtil.glTextureId;
	}

	public static void drawCircle(final int x, final int y, final double radius, final Color c) {
		setColor(c);
		GlStateManager.alphaFunc(516, 0.001f);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		final Tessellator tess = Tessellator.getInstance();
		final WorldRenderer render = tess.getWorldRenderer();
		for (double i = 0.0; i < 360.0; ++i) {
			final double cs = i * 3.141592653589793 / 180.0;
			final double ps = (i - 1.0) * 3.141592653589793 / 180.0;
			final double[] outer = { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius,
					-Math.sin(ps) * radius };
			render.startDrawing(6);
			render.addVertex(x + outer[2], y + outer[3], 0.0);
			render.addVertex(x + outer[0], y + outer[1], 0.0);
			render.addVertex(x, y, 0.0);
			tess.draw();
		}
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture();
		GlStateManager.alphaFunc(516, 0.1f);
	}

	public static void drawBorderedRect(final float x, final float y, final float x1, final float y1, final float size,
			final Color mainColor, final Color borderColor) {
		drawRect(x, y, x1, y1, mainColor);
		drawRect(x - size, y - size, x1, y, borderColor);
		drawRect(x, y, x - size, y1, borderColor);
		drawRect(x1, y1, x1 + size, y - size, borderColor);
		drawRect(x - size, y1 + size, x1 + size, y1, borderColor);
	}

	public static void drawRect(float left, float top, float right, float bottom, final Color color) {
		if (left < right) {
			final float var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			final float var5 = top;
			top = bottom;
			bottom = var5;
		}
		final Tessellator var6 = Tessellator.getInstance();
		final WorldRenderer var7 = var6.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		setColor(color);
		var7.startDrawingQuads();
		var7.addVertex(left, bottom, 0.0);
		var7.addVertex(right, bottom, 0.0);
		var7.addVertex(right, top, 0.0);
		var7.addVertex(left, top, 0.0);
		var6.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		setColor(Color.WHITE);
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

	public static void drawBoundingBox(final AxisAlignedBB aa) {
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
	}

	public static void renderOne() {
		GL11.glPushAttrib(1048575);
		GL11.glDisable(3008);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glEnable(2960);
		GL11.glClear(1024);
		GL11.glClearStencil(15);
		GL11.glStencilFunc(512, 1, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glLineWidth(0.7f);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1032, 6913);
	}

	public static void renderTwo() {
		GL11.glStencilFunc(512, 0, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1032, 6914);
	}

	public static void renderThree() {
		GL11.glStencilFunc(514, 1, 15);
		GL11.glStencilOp(7680, 7680, 7680);
		GL11.glPolygonMode(1032, 6913);
	}
	public static void drawbRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
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
        drawRects(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRects(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRects(x, y + 1.0f, x + 1.0f, x1, y1);
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
    public static void drawRects(final double left, final double top, final double right, final double bottom, final int color) {
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

	public static void renderFour() {
		GL11.glDepthMask(false);
		GL11.glDisable(2929);
		GL11.glEnable(10754);
		GL11.glPolygonOffset(1.5f, -2000000.0f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
	}

	public static void renderFive() {
		GL11.glPolygonOffset(1.0f, 2000000.0f);
		GL11.glDisable(10754);
		GL11.glDisable(2960);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(3008);
		GL11.glPopAttrib();
	}

	public static void drawRectColor(final double d, final double e, final double f2, final double f3,
			final float alpha, final float red, final float green, final float blue) {
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glPushMatrix();
		GL11.glColor4f(alpha, red, green, blue);
		GL11.glBegin(7);
		GL11.glVertex2d(f2, e);
		GL11.glVertex2d(d, e);
		GL11.glVertex2d(d, f3);
		GL11.glVertex2d(f2, f3);
		GL11.glEnd();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static void drawRect(final double d, final double e, final double f2, final double f3, final float red,
			final float green, final float blue, final float alpha) {
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(7);
		GL11.glVertex2d(f2, e);
		GL11.glVertex2d(d, e);
		GL11.glVertex2d(d, f3);
		GL11.glVertex2d(f2, f3);
		GL11.glEnd();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static void drawRect(final double d, final double e, final double f2, final double f3,
			final int paramColor) {
		final float alpha = (paramColor >> 24 & 0xFF) / 255.0f;
		final float red = (paramColor >> 16 & 0xFF) / 255.0f;
		final float green = (paramColor >> 8 & 0xFF) / 255.0f;
		final float blue = (paramColor & 0xFF) / 255.0f;
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(7);
		GL11.glVertex2d(f2, e);
		GL11.glVertex2d(d, e);
		GL11.glVertex2d(d, f3);
		GL11.glVertex2d(f2, f3);
		GL11.glEnd();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

}
