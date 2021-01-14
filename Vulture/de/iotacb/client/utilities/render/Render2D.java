package de.iotacb.client.utilities.render;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.ARBTextureMultisample;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.math.Vec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import optifine.CapeUtils;

public class Render2D {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public final void push() {
		GL11.glPushMatrix();
	}
	
	public final void pop() {
		GL11.glPopMatrix();
	}
	
	public final void enable(int glTarget) {
		GL11.glEnable(glTarget);
	}
	
	public final void disable(int glTarget) {
		GL11.glDisable(glTarget);
	}
	
	public final void start() {
		enable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		disable(GL11.GL_TEXTURE_2D);
		disable(GL11.GL_CULL_FACE);
		GlStateManager.disableAlpha();
	}
	
    public final void stop() {
    	GlStateManager.enableAlpha();
        enable(GL11.GL_CULL_FACE);
        enable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_BLEND);
        color(Color.white);
    }
    
    public final void startSmooth() {
    	enable(GL11.GL_POLYGON_SMOOTH);
    	enable(GL11.GL_LINE_SMOOTH);
    	enable(GL11.GL_POINT_SMOOTH);
    }
    
    public final void endSmooth() {
    	disable(GL11.GL_POINT_SMOOTH);
    	disable(GL11.GL_LINE_SMOOTH);
    	disable(GL11.GL_POLYGON_SMOOTH);
    }
    
    public final void begin(int glMode) {
    	GL11.glBegin(glMode);
    }
    
    public final void end() {
    	GL11.glEnd();
    }
    
    public final void vertex(double x, double y) {
    	GL11.glVertex2d(x, y);
    }
    
    public final void translate(double x, double y) {
    	GL11.glTranslated(x, y, 0);
    }
    
    public final void scale(double x, double y) {
    	GL11.glScaled(x, y, 1);
    }
    
    public final void rotate(double x, double y, double z, double angle) {
    	GL11.glRotated(angle, x, y, z);
    }
    
    public final void color(double red, double green, double blue, double alpha) {
    	GL11.glColor4d(red, green, blue, alpha);
    }
    
    public final void color(double red, double green, double blue) {
        color(red, green, blue, 1);
    }
    
    public final void color(Color color) {
    	if (color == null)
    		color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }
    
    public final void lineWidth(double width) {
    	GL11.glLineWidth((float)width);
    }
    
    public final void rect(double x, double y, double width, double height, boolean filled, Color color) {
    	start();
    	if (color != null)
    		color(color);
    	begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINES);
    	{
    		vertex(x, y);
    		vertex(x + width, y);
    		vertex(x + width, y + height);
    		vertex(x, y + height);
    		if (!filled)  {
    			vertex(x, y);
    			vertex(x, y + height);
    			vertex(x + width, y);
    			vertex(x + width, y + height);
    		}
    	}
    	end();
    	stop();
    }
    
    public final void rect(double x, double y, double width, double height, boolean filled) {
    	rect(x, y, width, height, filled, null);
    }
    
    public final void rect(double x, double y, double width, double height, Color color) {
    	rect(x, y, width, height, true, color);
    }
    
    public final void rect(double x, double y, double width, double height) {
    	rect(x, y, width, height, true, null);
    }
    
    public final void rectCentered(double x, double y, double width, double height, boolean filled, Color color) {
    	x -= width / 2;
    	y -= height / 2;
    	rect(x, y, width, height, filled, color);
    }
    
    public final void rectCentered(double x, double y, double width, double height, boolean filled) {
    	x -= width / 2;
    	y -= height / 2;
    	rect(x, y, width, height, filled, null);
    }
    
    public final void rectCentered(double x, double y, double width, double height, Color color) {
    	x -= width / 2;
    	y -= height / 2;
    	rect(x, y, width, height, true, color);
    }
    
    public final void rectCentered(double x, double y, double width, double height) {
    	x -= width / 2;
    	y -= height / 2;
    	rect(x, y, width, height, true, null);
    }
    
    public final void gradient(double x, double y, double width, double height, boolean filled, Color color1, Color color2) {
    	start();
    	GL11.glShadeModel(GL11.GL_SMOOTH);
    	GlStateManager.disableAlpha();
    	if (color1 != null)
    		color(color1);
    	begin(filled ? GL11.GL_QUADS : GL11.GL_LINES);
    	{
    		vertex(x, y);
    		vertex(x + width, y);
        	if (color2 != null)
        		color(color2);
    		vertex(x + width, y + height);
    		vertex(x, y + height);
    		if (!filled)  {
    			vertex(x, y);
    			vertex(x, y + height);
    			vertex(x + width, y);
    			vertex(x + width, y + height);
    		}
    	}
    	end();
    	GlStateManager.enableAlpha();
    	GL11.glShadeModel(GL11.GL_FLAT);
    	stop();
    }
    
    public final void gradient(double x, double y, double width, double height, Color color1, Color color2) {
    	gradient(x, y, width, height, true, color1, color2);
    }
    
    public final void gradientCentered(double x, double y, double width, double height, Color color1, Color color2) {
    	x -= width / 2;
    	y -= height / 2;
    	gradient(x, y, width, height, true, color1, color2);
    }
    
    public final void gradientSideways(double x, double y, double width, double height, boolean filled, Color color1, Color color2) {
    	start();
    	GL11.glShadeModel(GL11.GL_SMOOTH);
    	GlStateManager.disableAlpha();
    	if (color1 != null)
    		color(color1);
    	begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINES);
    	{
    		vertex(x, y);
    		vertex(x, y + height);
        	if (color2 != null)
        		color(color2);
    		vertex(x + width, y + height);
    		vertex(x + width, y);
    	}
    	end();
    	GlStateManager.enableAlpha();
    	GL11.glShadeModel(GL11.GL_FLAT);
    	stop();
    }
    
    public final void gradientSideways(double x, double y, double width, double height, Color color1, Color color2) {
    	gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public final void gradientSidewaysCentered(double x, double y, double width, double height, Color color1, Color color2) {
    	x -= width / 2;
    	y -= height / 2;
    	gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public final void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
    	sideLength /= 2;
    	start();
    	if (color != null)
    		color(color);
    	if (!filled) GL11.glLineWidth(1);
    	GL11.glEnable(GL11.GL_LINE_SMOOTH);
    	begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
    	{
    		for (double i = 0; i <= amountOfSides; i++) {
    			double angle = i * (Math.PI * 2) / amountOfSides;
    			vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
    		}
    	}
    	end();
    	GL11.glDisable(GL11.GL_LINE_SMOOTH);
    	stop();
    }
    
    public final void polygon(double x, double y, double sideLength, int amountOfSides, boolean filled) {
    	polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public final void polygon(double x, double y, double sideLength, int amountOfSides, Color color) {
    	polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public final void polygon(double x, double y, double sideLength, int amountOfSides) {
    	polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public final void polygonCentered(double x, double y, double sideLength, int amountOfSides, boolean filled, Color color) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, amountOfSides, filled, color);
    }
    
    public final void polygonCentered(double x, double y, double sideLength, int amountOfSides, boolean filled) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public final void polygonCentered(double x, double y, double sideLength, int amountOfSides, Color color) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public final void polygonCentered(double x, double y, double sideLength, int amountOfSides) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public final void circle(double x, double y, double radius, boolean filled, Color color) {
    	polygon(x, y, radius, 360, filled, color);
    }
    
    public final void circle(double x, double y, double radius, boolean filled) {
    	polygon(x, y, radius, 360, filled);
    }
    
    public final void circle(double x, double y, double radius, Color color) {
    	polygon(x, y, radius, 360, color);
    }
    
    public final void circle(double x, double y, double radius) {
    	polygon(x, y, radius, 360);
    }
    
    public final void circleCentered(double x, double y, double radius, boolean filled, Color color) {
    	x -= radius / 2;
    	y -= radius / 2;
    	polygon(x, y, radius, 360, filled, color);
    }
    
    public final void circleCentered(double x, double y, double radius, boolean filled) {
    	x -= radius / 2;
    	y -= radius / 2;
    	polygon(x, y, radius, 360, filled);
    }
    
    public final void circleCentered(double x, double y, double radius, Color color) {
    	x -= radius / 2;
    	y -= radius / 2;
    	polygon(x, y, radius, 360, color);
    }
    
    public final void circleCentered(double x, double y, double radius) {
    	x -= radius / 2;
    	y -= radius / 2;
    	polygon(x, y, radius, 360);
    }
    
    public final void triangle(double x, double y, double sideLength, boolean filled, Color color) {
    	polygon(x, y, sideLength, 3, filled, color);
    }
    
    public final void triangle(double x, double y, double sideLength, boolean filled) {
    	polygon(x, y, sideLength, 3, filled);
    }
    
    public final void triangle(double x, double y, double sideLength, Color color) {
    	polygon(x, y, sideLength, 3, color);
    }
    
    public final void triangle(double x, double y, double sideLength) {
    	polygon(x, y, sideLength, 3);
    }
    
    public final void triangleCentered(double x, double y, double sideLength, boolean filled, Color color) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, 3, filled, color);
    }
    
    public final void triangleCentered(double x, double y, double sideLength, boolean filled) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, 3, filled);
    }
    
    public final void triangleCentered(double x, double y, double sideLength, Color color) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, 3, color);
    }
    
    public final void triangleCentered(double x, double y, double sideLength) {
    	x -= sideLength / 2;
    	y -= sideLength / 2;
    	polygon(x, y, sideLength, 3);
    }
    
    public final void line(double firstX, double firstY, double secondX, double secondY, double lineWidth, Color color) {
    	start();
    	if (color != null)
    		color(color);
    	lineWidth(lineWidth <= 1 ? 1 : lineWidth);
    	GL11.glEnable(GL11.GL_LINE_SMOOTH);
    	begin(GL11.GL_LINES);
    	{
    		vertex(firstX, firstY);
    		vertex(secondX, secondY);
    	}
    	end();
    	GL11.glDisable(GL11.GL_LINE_SMOOTH);
    	stop();
    }
    
    public final void line(double firstX, double firstY, double secondX, double secondY, double lineWidth) {
    	line(firstX, firstY, secondX, secondY, lineWidth, null);
    }
    
    public final void line(double firstX, double firstY, double secondX, double secondY, Color color) {
    	line(firstX, firstY, secondX, secondY, 0, color);
    }
    
    public final void line(double firstX, double firstY, double secondX, double secondY) {
    	line(firstX, firstY, secondX, secondY, 0, null);
    }
    
    public final void line(Vec firstPoint, Vec secondPoint, double lineWidth, Color color) {
    	line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), lineWidth, color);
    }
    
    public final void line(Vec firstPoint, Vec secondPoint, Color color) {
    	line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), color);
    }
    
    public final void line(Vec firstPoint, Vec secondPoint) {
    	line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY());
    }
    
    public final void image(ResourceLocation imageLocation, double x, double y, double width, double height) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    	enable(GL11.GL_BLEND);
    	GlStateManager.disableAlpha();
    	OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    	MC.getTextureManager().bindTexture(imageLocation);
    	Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    	GlStateManager.enableAlpha();
    	disable(GL11.GL_BLEND);
    }
    
    public final void imageCentered(ResourceLocation imageLocation, double x, double y, double width, double height) {
    	x -= width / 2;
    	y -= height / 2;
    	image(imageLocation, x, y, width, height);
    }
    
    public final void scissor(double x, double y, double width, double height) {
    	ScaledResolution sr = new ScaledResolution(MC);
    	final double scale = sr.getScaleFactor();
    	
    	y = sr.getScaledHeight() - y;
    	
    	x *= scale;
    	y *= scale;
    	width *= scale;
    	height *= scale;
    	
    	GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }
    
    public final void outlineInlinedGradientRect(double x, double y, double width, double height, double inlineOffset, Color color1, Color color2) {
    	gradient(x, y, width, inlineOffset, color1, color2);
    	gradient(x, y + height - inlineOffset, width, inlineOffset, color2, color1);
    	gradientSideways(x, y, inlineOffset, height, color1, color2);
    	gradientSideways(x + width - inlineOffset, y, inlineOffset, height, color2, color1);
    }
    
    /**
     * ClickGui shit
     */
    
    public final void roundedRect(double x, double y, double width, double height, double edgeRadius, Color color) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Top left and right circles
    	circle(x, y, edgeRadius, color);
    	circle(x + width - edgeRadius / 2, y, edgeRadius, color);
    	
    	// Bottom left and right circles
    	circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color);
    	circle(x, y + height - edgeRadius / 2, edgeRadius, color);
    	
    	// Main block
    	rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
    	
    	// Horizontal bars
    	rect(x, y + halfRadius, edgeRadius / 2, height - halfRadius, color);
    	rect(x + width, y + halfRadius, edgeRadius / 2, height - halfRadius, color);
    	
    	// Vertical bars
    	rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
    	rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public final void roundedRectTop(double x, double y, double width, double height, double edgeRadius, Color color) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Top left and right circles
    	circle(x, y, edgeRadius, color);
    	circle(x + width - edgeRadius / 2, y, edgeRadius, color);
    	// Main block
    	rect(x, y + halfRadius, width + halfRadius, height, color);
    	
    	// Vertical bar
    	rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
    }
    
    public final void roundedRectBottom(double x, double y, double width, double height, double edgeRadius, Color color) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Bottom left and right circles
    	circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color);
    	circle(x, y + height - edgeRadius / 2, edgeRadius, color);
    	
    	// Main block
    	rect(x, y, width + halfRadius, height, color);
    	
    	// Vertical bar
    	rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public final void roundedRectRight(double x, double y, double width, double height, double edgeRadius, Color color1, Color color2) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Top left and right circles
    	circle(x + width - edgeRadius / 2, y, edgeRadius, color2);
    	circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color2);
    	
    	// Main block
    	gradientSideways(x, y, width, height + halfRadius, color1, color2);
    	
    	// Vertical bar
    	rect(x + width, y + halfRadius, 5, height - halfRadius, color2);
    }
    
    public final void roundedRectRightTop(double x, double y, double width, double height, double edgeRadius, Color color1, Color color2) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Top left and right circles
    	circle(x + width - edgeRadius / 2, y, edgeRadius, color2);
    	
    	// Main block
    	gradientSideways(x, y, width, height + halfRadius, color1, color2);
    	
    	// Vertical bar
    	rect(x + width, y + halfRadius, 5, height, color2);
    }
    
    public final void roundedRectRightBottom(double x, double y, double width, double height, double edgeRadius, Color color1, Color color2) {
    	final double halfRadius = edgeRadius / 2;
    	width -= halfRadius;
    	height -= halfRadius;
    	
    	// Bottom left and right circles
    	circle(x + width - edgeRadius / 2, y + height - edgeRadius / 2, edgeRadius, color2);
    	
    	// Main block
    	gradientSideways(x, y, width, height + halfRadius, color1, color2);
    	// Vertical bar
    	rect(x + width, y, 5, height, color2);
    }
	
}
