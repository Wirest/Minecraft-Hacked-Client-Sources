package me.robbanrobbin.jigsaw.client.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class RenderTools {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();

	}

	public static void drawBoundingBox(AxisAlignedBB aa) {
		// return;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		;
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		;
		tessellator.draw();

	}

	public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue,
			float alpha, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha,
			float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
			float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red,
			float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glLineWidth(1);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawOutlinedEntityESPFixed(double x, double y, double z, double width, double height, float red,
			float green, float blue, float alpha, Entity e) {
		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glLineWidth(1);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red,
			float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawSolidEntityESPFixed(double x, double y, double z, double width, double height, float red,
			float green, float blue, float alpha, Entity e) {
		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
			float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glLineWidth(lineWdith);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha,
			float lineWdith) {
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(lineWdith);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(2);
		GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void draw3DLine(double x, double y, double z, double x1, double y1, double z1, float red, float green,
			float blue, float alpha, float lineWdith) {

		x = x - mc.getRenderManager().renderPosX;
		x1 = x1 - mc.getRenderManager().renderPosX;
		y = y - mc.getRenderManager().renderPosY;
		y1 = y1 - mc.getRenderManager().renderPosY;
		z = z - mc.getRenderManager().renderPosZ;
		z1 = z1 - mc.getRenderManager().renderPosZ;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(lineWdith);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(2);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x1, y1, z1);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void color4f(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void lineWidth(float width) {
		GL11.glLineWidth(width);
	}

	public static void glBegin(int mode) {
		GL11.glBegin(mode);
	}

	public static void glEnd() {
		GL11.glEnd();
	}

	public static void putVertex3d(double x, double y, double z) {
		GL11.glVertex3d(x, y, z);
	}

	public static void putVertex3d(Vec3 vec) {
		GL11.glVertex3d(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public static Vec3 getRenderPos(double x, double y, double z) {

		x = x - mc.getRenderManager().renderPosX;
		y = y - mc.getRenderManager().renderPosY;
		z = z - mc.getRenderManager().renderPosZ;

		return new Vec3(x, y, z);
	}

	public static void drawCircle(int x, int y, double r, float f1, float f2, float f3, float f) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int i = 0; i <= 360; i++) {
			double x2 = Math.sin(((i * Math.PI) / 180)) * r;
			double y2 = Math.cos(((i * Math.PI) / 180)) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawFilledCircle(int x, int y, double r, int c) {
		float f = ((c >> 24) & 0xff) / 255F;
		float f1 = ((c >> 16) & 0xff) / 255F;
		float f2 = ((c >> 8) & 0xff) / 255F;
		float f3 = (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		for (int i = 0; i <= 360; i++) {
			double x2 = Math.sin(((i * Math.PI) / 180)) * r;
			double y2 = Math.cos(((i * Math.PI) / 180)) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawSphere(double red, double green, double blue, double alpha, double x, double y, double z,
			float size, int slices, int stacks, float lWidth) {
		Sphere sphere = new Sphere();

		enableDefaults();
		GL11.glColor4d(red, green, blue, alpha);
		GL11.glTranslated(x, y, z);
		GL11.glLineWidth(lWidth);
		sphere.setDrawStyle(GLU.GLU_SILHOUETTE);
		sphere.draw(size, slices, stacks);
		disableDefaults();
	}

	public static void enableDefaults() {
		mc.entityRenderer.disableLightmap();
		GL11.glEnable(3042 /* GL_BLEND */);
		GL11.glDisable(3553 /* GL_TEXTURE_2D */);
		GL11.glDisable(2896 /* GL_LIGHTING */);
		// GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848 /* GL_LINE_SMOOTH */);
		GL11.glPushMatrix();
	}

	public static void disableDefaults() {
		GL11.glPopMatrix();
		GL11.glDisable(2848 /* GL_LINE_SMOOTH */);
		GL11.glDepthMask(true);
		// GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glEnable(3553 /* GL_TEXTURE_2D */);
		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glDisable(3042 /* GL_BLEND */);
		mc.entityRenderer.enableLightmap();
	}

}
