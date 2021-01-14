package de.iotacb.cu.core.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil {

    private static final Minecraft MC = Minecraft.getMinecraft();
    public static final RenderUtil INSTANCE = new RenderUtil();
    
    public float delta;

    /**
     * Sets up basic rendering parameters
     */
    public final void startRender() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Resets the rendering parameters
     */
    public final void stopRender() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        color(Color.white);
    }

    /**
     * Sets the current color using rgb values
     *
     * @param color
     */
    public final void color(final Color color) {
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
    }

    /**
     * Draws a rectangle at the given position with the given size and color
     *
     * @param posX
     * @param posY
     * @param width
     * @param height
     * @param color
     */
    public final void drawRect(final double posX, final double posY, final double width, final double height, final Color color) {
        startRender();
        color(color);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(posX, posY);
            GL11.glVertex2d(posX + width, posY);
            GL11.glVertex2d(posX + width, posY + height);
            GL11.glVertex2d(posX, posY + height);
        }
        GL11.glEnd();
        stopRender();
    }
    
    /**
     * http://slabode.exofire.net/circle_draw.shtml
     *
     * @param xPos
     * @param yPos
     * @param radius
     * @param color
     */
    public final void drawCircle(final double xPos, final double yPos, final double radius, final Color color) {
        startRender();
        color(color);
        GL11.glBegin(GL11.GL_POLYGON);
        {
            drawCircle(xPos, yPos, radius);
        }
        GL11.glEnd();

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			drawCircle(xPos, yPos, radius);
		}
    	GL11.glEnd();
        stopRender();
    }

    private final void drawCircle(final double xPos, final double yPos, final double radius) {
        final double theta = (2 * Math.PI / 360.0);
        final double tangetial_factor = Math.tan(theta);//calculate the tangential factor
        final double radial_factor = Math.cos(theta);//calculate the radial factor
        double x = radius;//we start at angle = 0
        double y = 0;
        for (int i = 0; i < 360; i++) {
            GL11.glVertex2d(x + xPos, y + yPos);

            //calculate the tangential vector
            //remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            double tx = -y;
            double ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
    }
    
    public final void drawSphere(final double x, final double y, final double z, final double segments) {
    	final double tau = Math.PI * 2;
    	
    	startRender();
    	
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		for (int h = 0; h < segments; h++) {
    			final double angle1 = (h + 1) * Math.PI / (segments + 1);
    			for (int v = 0; v <= segments; v++) {
    				final double angle2 = v * tau / segments;
    				
    				final double sX = Math.sin(angle1) * Math.cos(angle2);
    				final double sY = Math.cos(angle1);
    				final double sZ = Math.sin(angle1) * Math.sin(angle2);
    				GL11.glVertex3d(x + sX, y + sY, z + sZ);
    			}
    		}
    	}
    	GL11.glEnd();
    	
    	stopRender();
    }
    
    /**
     * Draws a rounded rect at the given position
     * @param x
     * @param y
     * @param width
     * @param height
     * @param rounding
     * @param color
     */
    public final void drawRoundedRect(double x, double y, double width, double height, final double rounding, final Color color) {
    	drawRect(x + rounding, y, width - rounding * 2, height, color);
    	drawRect(x, y + rounding, width, height - rounding * 2, color);
    	x += .5;
    	y += .5;
    	width -= 1;
    	height -= 1;
    	drawCircle(x + rounding, y + rounding, rounding, color);
    	drawCircle(x + width - rounding, y + rounding, rounding, color);
    	drawCircle(x + rounding, y + height - rounding, rounding, color);
    	drawCircle(x + width - rounding, y + height - rounding, rounding, color);
    }
    

    /**
     * Draws a the give image at the given position with the given size
     * @param imageLocation
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public final void image(final ResourceLocation imageLocation, final double x, final double y, final double width, final double height) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        MC.getTextureManager().bindTexture(imageLocation);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, (int) width, (int) height, (int) width, (int) height);
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }

    /**
     * Sets up the vertices for drawing an outlined bounding box
     *
     * @param bb
     */
    public final void makeOutlinedBoundingBox(final AxisAlignedBB bb) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * Sets up the vertices for a filled bounding box
     *
     * @param bb
     */
    public final void drawFilledBoundingBox(final AxisAlignedBB bb) {
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        }
        GL11.glEnd();
    }

}
