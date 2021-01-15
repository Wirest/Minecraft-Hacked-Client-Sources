package nivia.utils.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import nivia.Pandora;
import nivia.managers.FriendManager;
import nivia.modules.combat.KillAura;
import nivia.modules.render.GUI;
import nivia.utils.Helper;
import nivia.utils.MinecraftFontRenderer;
import nivia.utils.Wrapper;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
	public static MinecraftFontRenderer comfortaa = new MinecraftFontRenderer(new Font("Comfortaa", Font.PLAIN, 29), true ,true);
	public static MinecraftFontRenderer futuristic = new MinecraftFontRenderer(new Font("Forgotten Futuristic Rg ", Font.BOLD, 29), true ,true);
	public static MinecraftFontRenderer helvetica = new MinecraftFontRenderer(new Font("Helvetica Neue", Font.PLAIN, 29), true ,true);
	public static MinecraftFontRenderer raleway = new MinecraftFontRenderer(new Font("Raleway", Font.PLAIN, 29), true ,true);
	public static MinecraftFontRenderer cgothic = new MinecraftFontRenderer(new Font("Century Gothic", Font.BOLD, 29), true ,true);
	public static MinecraftFontRenderer cgothicgui = new MinecraftFontRenderer(new Font("Century Gothic", Font.PLAIN, 24), true, true);

	public static Map<UUID, ResourceLocation> cache = new HashMap<UUID, ResourceLocation>();

	
    public static void drawCenteredString(String s, int x, int y, int colour) {
        x -= Wrapper.getMinecraft().fontRendererObj.getStringWidth(s) / 2;
        Wrapper.getMinecraft().fontRendererObj.drawStringWithShadow(s, x, y, colour);
    }
	public static void drawRect(double x1, double y1, double x2, double y2, int color) {
		Gui.drawRect(x1, y1, x2, y2, color);
	}
    public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
        drawRect(x1, y1, x2, y2, outline);
        drawRect(x1+1, y1+1, x2-1, y2-1, inline);
        drawRect(x1+2, y1+2, x2-2, y2-2, background);
    }
    public static void drawRectWH(double x, double y, double width, double height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }
    public static void drawBorderedRect(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
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
    public static  void drawRainbowRect(double x, double y, double x2, double y2) {
        double width = (x2 - x);
        for (int i = 0; i <= (width); i++) {
            Color color = new Color(Color.HSBtoRGB((float)((Wrapper.getPlayer().ticksExisted / width)/2 + (Math.sin(i / width * 0.6))) % 1.0f, 0.5f, 1.0f));
            drawRect(x+i, y, x+i+1,y2,color.getRGB());
        }
    }
    public static int getIntFromColor(int r, int g, int b){
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    public static int getIntFromColor(Color color){
        return color.getRGB();
    }
    public static Color getColorFromHex(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new Color(r, g, b);
    }
    
	public static class R2DUtils{
		public void prepareScissorBox(float x, float y, float x2, float y2)
		  {
		    ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		    int factor = scale.getScaleFactor();
		    GL11.glScissor((int)(x * factor), (int)((scale.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
		  }
		public static ResourceLocation loadHead(final EntityPlayer player) {
			if (cache.containsKey(player.getUniqueID())) {
				return cache.get(player.getUniqueID());
			}
			final ResourceLocation head = new ResourceLocation("heads/" + player.getName());
			final ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, String.format("https://minotar.net/helm/%s/64.png", player.getName()), null, null);
			Minecraft.getMinecraft().getTextureManager().loadTexture(head, textureHead);
			cache.put(player.getUniqueID(), head);
			return head;
		}
		
        public static void drawCenteredString(String s, int x, int y, int colour) {
            x -= Wrapper.getMinecraft().fontRendererObj.getStringWidth(s) / 2;
            Wrapper.getMinecraft().fontRendererObj.drawStringWithShadow(s, x, y, colour);
        }
        public int flippedFactor() {
        	switch(this.scaledRes().getScaleFactor()) {
        		case 0: return 4;
        		case 1: return 4;
        		case 2: return 3;
        		case 3: return 2;
        		case 4: return 1; 
        		default: return 1;
        	}
        }
        public float factorDivider() {
        	switch(this.scaledRes().getScaleFactor()) {
    		case 1: return 4;
    		case 2: return 2f;
    		case 3: return 1.33f;
    		case 4: return 1; 
    		default: return 1;
    	}
        }
    	public static void drawRect(double x1, double y1, double x2, double y2, int color) {
    		Gui.drawRect(x1, y1, x2, y2, color);
    	}
        public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
            drawRect(x1, y1, x2, y2, outline);
            drawRect(x1+1, y1+1, x2-1, y2-1, inline);
            drawRect(x1+2, y1+2, x2-2, y2-2, background);
        }
        public static void drawRectWH(double x, double y, double width, double height, int color) {
            Gui.drawRect(x, y, x + width, y + height, color);
        }
        public static void drawBorderedRect(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
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
        public static  void drawRainbowRect(double x, double y, double x2, double y2) {
            double width = (x2 - x);
            for (int i = 0; i <= (width); i++) {
                Color color = new Color(Color.HSBtoRGB((float)((Wrapper.getPlayer().ticksExisted / width)/2 + (Math.sin(i / width * 0.6))) % 1.0f, 0.5f, 1.0f));
                drawRect(x+i, y, x+i+1,y2,color.getRGB());
            }
        }
        public static int getIntFromColor(int r, int g, int b){
            r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
            g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
            b = b & 0x000000FF; //Mask out anything not blue.

            return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
        }
        public static int getIntFromColor(Color color){
            return color.getRGB();
        }
        public static Color getColorFromHex(int hex) {
            int r = (hex & 0xFF0000) >> 16;
            int g = (hex & 0xFF00) >> 8;
            int b = (hex & 0xFF);
            return new Color(r, g, b);
        }
		public static double[] getScreenCoords(final double x, final double y, final double z) {
			final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
			final IntBuffer viewport = BufferUtils.createIntBuffer(16);
			final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
			final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
			GL11.glGetFloat(2982, modelView);
			GL11.glGetFloat(2983, projection);
			GL11.glGetInteger(2978, viewport);
			final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
			if (result) {
				final double[] coords = { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
				return coords;
			}
			return null;
		}
		public void drawStringWithShadow(String text, float x, float y, int color){
            GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
            if(gui.font.value) {
				GL11.glPushMatrix();
				GL11.glScaled(0.5,0.5,0.5);
				x *= 2;
            	y *= 2;
				int inc = 0;
				if(Pandora.testFont.equals(comfortaa)) inc = 3;
				if(Pandora.testFont.equals(raleway)) inc = 1;
                Pandora.testFont.drawStringWithShadow(text,x,y + inc , color);
				GL11.glPopMatrix();
			} else
                Helper.mc().fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
		public void drawStringWithShadow(String text, float x, float y, int color, MinecraftFontRenderer font) {
			try {
				GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
				if (gui.font.value) {
					GL11.glPushMatrix();
					GL11.glScaled(0.5, 0.5, 0.5);
					x *= 2;
					y *= 2;
					font.drawStringWithShadow(text, x, y, color);
					GL11.glPopMatrix();
				} else
					Helper.mc().fontRendererObj.drawStringWithShadow(text, x, y, color);
			} catch (NullPointerException e){
				Helper.mc().fontRendererObj.drawStringWithShadow(text, x, y, color);
			}
		}
		public  int getStringWidth(String text) {
			GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
			if(gui.font.value)
				return (Pandora.testFont.getStringWidth(text) / 2);
			else return Helper.mc().fontRendererObj.getStringWidth(text);
		}

		public int getStringWidth(String text, int increase) {
			GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
			if(gui.font.value)
				return (Pandora.testFont.getStringWidth(text) / 2) + increase;
			else return Helper.mc().fontRendererObj.getStringWidth(text);
		}
		public int getStringWidth(String text, boolean divide) {
			GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
			if(gui.font.value)
				return (Pandora.testFont.getStringWidth(text) / (divide ? 2 : 0));
			else return Helper.mc().fontRendererObj.getStringWidth(text);
		}

		public void drawCentredStringWithShadow(final String s, float x, float y, final int colour) {
	        x -= getStringWidth(s) / 2;
	        drawStringWithShadow(s, x, y, colour);
	    }

        public void drawCustomStringWithShadow(String text, float x, float y, int color){
			GL11.glPushMatrix();
			GL11.glScaled(0.5,0.5,0.5);
			x *= 2;
			y *= 2;
			int inc = 0;
			if(Pandora.testFont.equals(comfortaa)) inc = 3;
			if(Pandora.testFont.equals(raleway)) inc = 1;
			Pandora.testFont.drawStringWithShadow(text,x,y + inc , color);
			GL11.glPopMatrix();
        }

		public void drawSmallString(String s, float x, float y, int color)
	    {
	        GL11.glPushMatrix();
	        GL11.glScalef(0.5F, 0.5F, 0.5F);
	        drawStringWithShadow(s, x * 2, y * 2, color);
	        GL11.glPopMatrix();
	    }
		public void drawSmallString(String s, float x, float y, int color, float ratio)
	    {
	        GL11.glPushMatrix();
	        GL11.glScalef(ratio,ratio,ratio);
	        drawStringWithShadow(s, x * 2, y * 2, color);
	        GL11.glPopMatrix();
	    }
		public static void drawVerticalLine(final int x, final int y, final int height, final int color) {
			drawRect(x, y, x + 1, height, color);
		}
		    
		public static void drawHorizontalLine(final int x, final int y, final int width, final int color) {
		    drawRect(x, y, width, y + 1, color);
		}
		public static void drawFineBorderedRect(int x, int y, int x1, int y1, final int bord, final int color) {
	        GL11.glScaled(0.5, 0.5, 0.5);
	        x *= 2;
	        y *= 2;
	        x1 *= 2;
	        y1 *= 2;
	        drawRect(x + 1, y + 1, x1, y1, color);
	        drawVerticalLine(x, y, y1, bord);
	        drawVerticalLine(x1, y, y1, bord);
	        drawHorizontalLine(x + 1, y, x1, bord);
	        drawHorizontalLine(x, y1, x1 + 1, bord);
	        GL11.glScaled(2.0, 2.0, 2.0);
	    }
		public ScaledResolution scaledRes(){
			return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		}
		public void drawCustomImage(double x, double y, double xwidth, double ywidth, ResourceLocation image){
			// Op opengl by soirr
			double par1 = x + xwidth;
			double par2 = y + ywidth;
	    	GL11.glDisable(GL11.GL_DEPTH_TEST);
	    	GL11.glDepthMask(false);
	    	OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
	    	Minecraft.getMinecraft().getTextureManager().bindTexture(image);
	    	Tessellator var3 = Tessellator.getInstance();
	    	WorldRenderer var4 = var3.getWorldRenderer();
	    	var4.startDrawingQuads();
	    	
	    	var4.addVertexWithUV(x, par2, 0.0D, 0.0D, 1.0D);
	    	var4.addVertexWithUV(par1, par2, 0.0D, 1.0D, 1.0D);
	    	var4.addVertexWithUV(par1, y, 0.0D, 1.0D, 0.0D);
	    	var4.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
	    	var3.draw();
	    	GL11.glDepthMask(true);
	    	GL11.glEnable(GL11.GL_DEPTH_TEST);
	    	GL11.glEnable(GL11.GL_ALPHA_TEST);
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		public void drawCustomImage(double x, double y, double xwidth, double ywidth, ResourceLocation image, int[] color){

			double par1 = x + xwidth;
			double par2 = y + ywidth;
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glColor4f(color[0], color[1], color[2], color[3]);
			Minecraft.getMinecraft().getTextureManager().bindTexture(image);
			Tessellator var3 = Tessellator.getInstance();
			WorldRenderer var4 = var3.getWorldRenderer();
			var4.startDrawingQuads();

			var4.addVertexWithUV(x, par2, 0.0D, 0.0D, 1.0D);
			var4.addVertexWithUV(par1, par2, 0.0D, 1.0D, 1.0D);
			var4.addVertexWithUV(par1, y, 0.0D, 1.0D, 0.0D);
			var4.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
			var3.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		/**
	     * Enables 2D GL constants for 2D rendering.
	     */
	    public static void enableGL2D() {
	        glDisable(GL_DEPTH_TEST);
	        glEnable(GL_BLEND);
	        glDisable(GL_TEXTURE_2D);
	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	        glDepthMask(true);
	        glEnable(GL_LINE_SMOOTH);
	        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
	    }

	    /**
	     * Disables 2D GL constants for 2D rendering.
	     */
	    public static void disableGL2D() {
	        glEnable(GL_TEXTURE_2D);
	        glDisable(GL_BLEND);
	        glEnable(GL_DEPTH_TEST);
	        glDisable(GL_LINE_SMOOTH);
	        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
	        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
	    }
	    /**
	     * Draws a rectangle at the coordinates specified with the hexadecimal color.
	     */
	    public static void drawRect(float x, float y, float x1, float y1, int color) {
	        enableGL2D();
	        Helper.colorUtils().glColor(color);
	        drawRect(x, y, x1, y1);
	        disableGL2D();
	    }

	    /**
	     * Draws a bordered rectangle at the coordinates specified with the hexadecimal color.
	     */
	    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
	        enableGL2D();
	        Helper.colorUtils().glColor(internalColor);
	        drawRect(x + width, y + width, x1 - width, y1 - width);
	        Helper.colorUtils().glColor(borderColor);
	        drawRect(x + width, y, x1 - width, y + width);
	        drawRect(x, y, x + width, y1);
	        drawRect(x1 - width, y, x1, y1);
	        drawRect(x + width, y1 - width, x1 - width, y1);
	        disableGL2D();	
	        
	    }

	    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
	        enableGL2D();
	        x *= 2; x1 *= 2; y *= 2; y1 *= 2;
	        glScalef(0.5F, 0.5F, 0.5F);
	        drawVLine(x, y, y1 , borderC);
	        drawVLine(x1 - 1, y , y1, borderC);
	        drawHLine(x, x1 - 1, y, borderC);
	        drawHLine(x, x1 - 2, y1 -1, borderC);
	        drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
	        glScalef(2.0F, 2.0F, 2.0F);
	        disableGL2D();
	    }
	    /**
	     * Draws a rectangle with a vertical gradient between the specified colors.
	     */
	    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
	        enableGL2D();
	        glShadeModel(GL_SMOOTH);
	        glBegin(GL_QUADS);
	        Helper.colorUtils().glColor(topColor);
	        glVertex2f(x, y1);
	        glVertex2f(x1, y1);
	        Helper.colorUtils().glColor(bottomColor);
	        glVertex2f(x1, y);
	        glVertex2f(x, y);
	        glEnd();
	        glShadeModel(GL_FLAT);
	        disableGL2D();
	    }
	    public static void drawHLine(float x, float y, float x1, int y1) {
	        if (y < x) {
	            float var5 = x;
	            x = y;
	            y = var5;
	        }
	        drawRect(x, x1, y + 1, x1 + 1, y1);
	    }

	    public static void drawVLine(float x, float y, float x1, int y1) {
	        if (x1 < y) {
	            float var5 = y;
	            y = x1;
	            x1 = var5;
	        }
	        drawRect(x, y + 1, x + 1, x1, y1);
	    }
	    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
	        if (y < x) {
	            float var5 = x;
	            x = y;
	            y = var5;
	        }
	        drawGradientRect(x, x1, y + 1, x1 + 1, y1, y2);
	    }
	    public static void drawRect(float x, float y, float x1, float y1) {
	        glBegin(GL_QUADS);
	        glVertex2f(x, y1);
	        glVertex2f(x1, y1);
	        glVertex2f(x1, y);
	        glVertex2f(x, y);
	        glEnd();
	    }
        public static void drawTri(double x1, double y1, double x2, double y2,
                                   double x3, double y3, double width, Color c){
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);
            Helper.colorUtils().glColor(c);
            GL11.glLineWidth((float) width);
            GL11.glBegin(3);
            GL11.glVertex2d(x1, y1);
            GL11.glVertex2d(x2, y2);
            GL11.glVertex2d(x3, y3);
            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
        }
        public static void drawFilledCircle(int x, int y, double radius, Color c){
            Helper.colorUtils().glColor(c);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.001F);
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer render = tess.getWorldRenderer();

            for(double i = 0; i < 360; i++) {
                double cs = i*Math.PI/180;
                double ps = (i-1)*Math.PI/180;
                double[] outer = new double[] {
                        Math.cos(cs) * radius, -Math.sin(cs) * radius,
                        Math.cos(ps) * radius, -Math.sin(ps) * radius
                };

                render.startDrawing(6);
                render.addVertex(x + outer[2], y + outer[3], 0);
                render.addVertex(x + outer[0], y + outer[1], 0);
                render.addVertex(x, y, 0);
                tess.draw();
            }

            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.disableAlpha();
            GL11.glEnable(GL11.GL_TEXTURE_2D);

        }
	}
	public static class R3DUtils{

		public static void startDrawing(){       
	        GL11.glEnable(3042);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glEnable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(2929);
            Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks,0);
	    }
	    public static void stopDrawing(){
	        GL11.glDisable(3042);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glEnable(2929);
	    }
        public void drawRombo(double x, double y, double z){
            //made this my self <33
            Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 0);
            y += 1;
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x+ 0.5,y,z);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d( x,y,z+ 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z+ 0.5);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x+ 0.5, y, z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z- 0.5);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x- 0.5, y, z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x- 0.5,y,z);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d( x,y,z- 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x+ 0.5,y,z);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d( x,y,z+ 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z+ 0.5);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x+ 0.5, y, z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z- 0.5);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x- 0.5, y, z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x- 0.5,y,z);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d( x,y,z- 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z- 0.5);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x+ 0.5,y,z );
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x+ 0.5 ,y,z);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x , y, z- 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z+ 0.5);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x- 0.5,y,z );
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x- 0.5 ,y,z);
            GL11.glVertex3d(x,y + 1,z);
            GL11.glVertex3d(x , y, z+ 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z- 0.5);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x+ 0.5,y,z );
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x+ 0.5 ,y,z);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x , y, z- 0.5);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x,y,z+ 0.5);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x- 0.5,y,z );
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex3d(x- 0.5 ,y,z);
            GL11.glVertex3d(x,y - 1,z);
            GL11.glVertex3d(x , y, z+ 0.5);
            GL11.glEnd();
        }
        public static void RenderLivingEntityBox(Entity entity, float partialTicks, boolean otherMode) {
            GlStateManager.pushMatrix();
            Helper.get3DUtils().startDrawing();
           
            final double posX = entity.lastTickPosX
                    + (entity.posX - entity.lastTickPosX)
                    * partialTicks - RenderManager.renderPosX;
            final double posY = entity.lastTickPosY
                    + (entity.posY - entity.lastTickPosY)
                    * partialTicks - RenderManager.renderPosY;
            final double posZ = entity.lastTickPosZ
                    + (entity.posZ - entity.lastTickPosZ)
                    * partialTicks - RenderManager.renderPosZ;

            boolean isPlayer = entity instanceof EntityPlayer;
            int bordercolor = 0xFF000000;
            if (FriendManager.isFriend(entity.getName()))
                bordercolor = 0xFF03BEFF * 20;
            if(KillAura.attackList.contains(entity))
                bordercolor = 0xFFE8A238;
            Helper.colorUtils().glColor(bordercolor, 0.4F);
            if(otherMode){
                RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(
                        entity.boundingBox.minX - (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.minZ - (isPlayer ? 0.12 : 0) - entity.posZ+ (posZ),
                        entity.boundingBox.maxX + (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.maxY+ (isPlayer ? 0.2 : 0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.maxZ+ (isPlayer ? 0.12 : 0) - entity.posZ + (posZ)
                ), -1);
                drawLines(new AxisAlignedBB(
                        entity.boundingBox.minX - (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.minZ - (isPlayer ? 0.12 : 0) - entity.posZ+ (posZ),
                        entity.boundingBox.maxX + (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.maxY+ (isPlayer ? 0.2 : 0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.maxZ+ (isPlayer ? 0.12 : 0) - entity.posZ + (posZ)
                ));
            } else {
                drawOutlinedBox(new AxisAlignedBB(
                        entity.boundingBox.minX - (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.minZ - (isPlayer ? 0.12 : 0) - entity.posZ + (posZ),
                        entity.boundingBox.maxX + (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.maxY + (isPlayer ? 0.2 : 0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0) - entity.posZ + (posZ)
                ));

                Helper.colorUtils().glColor(bordercolor, 0.15F);
                drawBoundingBox(new AxisAlignedBB(
                        entity.boundingBox.minX - (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.minZ - (isPlayer ? 0.12 : 0) - entity.posZ + (posZ),
                        entity.boundingBox.maxX + (isPlayer ? 0.12 : 0) - entity.posX + (posX),
                        entity.boundingBox.maxY + (isPlayer ? 0.2 : 0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
                        entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0) - entity.posZ + (posZ)
                ));
            }
            Helper.get3DUtils().stopDrawing();
            GlStateManager.popMatrix();
        }
        
        public static void drawCenteredString(String s, int x, int y, int colour) {
            x -= Wrapper.getMinecraft().fontRendererObj.getStringWidth(s) / 2;
            Wrapper.getMinecraft().fontRendererObj.drawStringWithShadow(s, x, y, colour);
        }
    	public static void drawRect(double x1, double y1, double x2, double y2, int color) {
    		Gui.drawRect(x1, y1, x2, y2, color);
    	}
        public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
            drawRect(x1, y1, x2, y2, outline);
            drawRect(x1+1, y1+1, x2-1, y2-1, inline);
            drawRect(x1+2, y1+2, x2-2, y2-2, background);
        }
        public static void drawRectWH(double x, double y, double width, double height, int color) {
            Gui.drawRect(x, y, x + width, y + height, color);
        }
        public static void drawBorderedRect(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
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
        public static  void drawRainbowRect(double x, double y, double x2, double y2) {
            double width = (x2 - x);
            for (int i = 0; i <= (width); i++) {
                Color color = new Color(Color.HSBtoRGB((float)((Wrapper.getPlayer().ticksExisted / width)/2 + (Math.sin(i / width * 0.6))) % 1.0f, 0.5f, 1.0f));
                drawRect(x+i, y, x+i+1,y2,color.getRGB());
            }
        }
        public static int getIntFromColor(int r, int g, int b){
            r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
            g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
            b = b & 0x000000FF; //Mask out anything not blue.

            return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
        }
        public static int getIntFromColor(Color color){
            return color.getRGB();
        }
        public static Color getColorFromHex(int hex) {
            int r = (hex & 0xFF0000) >> 16;
            int g = (hex & 0xFF00) >> 8;
            int b = (hex & 0xFF);
            return new Color(r, g, b);
        }
        public static void drawLines(AxisAlignedBB bb) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
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
		public static void drawOutlinedBox(AxisAlignedBB box){
	        if (box == null) 
	            return;

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
		public static void drawBoundingBox(AxisAlignedBB aabb) {
	        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
	        Tessellator tessellator = Tessellator.getInstance();
            Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 0);
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

        public static int getBlockColor(Block block) {
            int color;
            switch(Block.getIdFromBlock(block)) {
                case 14:
                case 41:
                    color = 0x99fcee4b;
                    break;
                case 42:
                case 15:
                    color = 0x99c0c0c0;
                    break;
                case 16:
                case 173:
                    color = 0x99373737;
                    break;
                case 21:
                case 22:
                    color = 0x991746c5;
                    break;
                case 49:
                    color = 0x993c3056;
                    break;
                case 54:
                case 146:
                    color = 0x99ffbf00;
                    break;
                case 56:
                case 57:
                case 138:
                    color = 0x995decf5;
                    break;
                case 61:
                case 62:
                    color = 0x99fe2ef7;
                    break;
                case 73:
                case 74:
                case 152:
                    color = 0x99ff0000;
                    break;
                case 89:
                    color = 0x99EBE04E;
                    break;
                case 129:
                case 133:
                    color = 0x9917dd62;
                    break;
                case 130:
                    color = 0x99df01d7;
                    break;
                case 52:
                    color = 0x30067034;
                    break;
                default: color = 0x99FFFFFF;
            }

            return color == 0 ? 0x30161147 : color;
        }
	}
	public static class ColorUtils{

		public int RGBtoHEX(int r, int g, int b, int a){
			return (a << 24) + (r << 16) + (g << 8) + b;
		}

		public Color getRainbow(final long offset, final float fade) {
	        final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
	        final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
	        final Color c = new Color((int)color);
	        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
	    }
        public static Color glColor(int color, float alpha) {
            int hex = color;
            float red = (hex >> 16 & 255) / 255.0F;
            float green = (hex >> 8 & 255) / 255.0F;
            float blue = (hex & 255) / 255.0F;
            GL11.glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
        }
		public void glColor(Color color) {
	        glColor4f((color.getRed() / 255F), (color.getGreen() / 255F), (color.getBlue() / 255F), (color.getAlpha() / 255F));
	    }
	    public Color glColor(int hex) {
	        float alpha = (hex >> 24 & 255) / 256.0F;
	        float red = (hex >> 16 & 255) / 255.0F;
	        float green = (hex >> 8 & 255) / 255.0F;
	        float blue = (hex & 255) / 255.0F;
	        glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
	    }
	    public Color glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
	        float red = (1 / 255.0F) * redRGB;
	        float green = (1 / 255.0F) * greenRGB;
	        float blue = (1 / 255.0F) * blueRGB;
	        glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
	    }
		public int darker(int hex) {
			Color meme = Color.getColor(null , hex);
			return meme.darker().getRGB();
		}
		public static int darker(Color color, double fraction) {
			int red = (int) Math.round(color.getRed() * (1.0 - fraction));
			int green = (int) Math.round(color.getGreen() * (1.0 - fraction));
			int blue = (int) Math.round(color.getBlue() * (1.0 - fraction));

			if (red < 0) red = 0;
			else if (red > 255) red = 255;
			if (green < 0) green = 0;
			else if (green > 255) green = 255;
			if (blue < 0) blue = 0;
			else if (blue > 255) blue = 255;
			int alpha = color.getAlpha();
			return new Color(red, green, blue, alpha).getRGB();
		}
        public int brighter(int hex) {
            Color meme = Color.getColor(null , hex);
            return meme.brighter().getRGB();
        }
		public static int brighter(Color color, double fraction) {
			int red = (int) Math.round(color.getRed() * (1.0 + fraction));
			int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
			int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));
			if (red < 0) red = 0;
			else if (red > 255) red = 255;
			if (green < 0) green = 0;
			else if (green > 255) green = 255;
			if (blue < 0) blue = 0;
			else if (blue > 255) blue = 255;
			int alpha = color.getAlpha();
			return new Color(red, green, blue, alpha).getRGB();
		}
		public static int transparency(int color, double alpha) {
	        Color c = new Color(color);
	        float r = ((float) 1f / 255f) * c.getRed();
	        float g = ((float) 1f / 255f) * c.getGreen();
	        float b = ((float) 1f / 255f) * c.getBlue();
	        return new Color(r, g, b, (float) alpha).getRGB();
	    }
	    public static float[] getRGBA(int color) {
	        float a = (color >> 24 & 255) / 255f;
	        float r = (color >> 16 & 255) / 255f;
	        float g = (color >> 8 & 255) / 255f;
	        float b = (color & 255) / 255f;
	        return new float[] {r, g, b, a};
	    }
	    public static int intFromHex(String hex) {
	        try {	           	        	
	            return Integer.parseInt(hex, 15);
	        } catch (NumberFormatException e) {
	            return 0xFFFFFFFF;
	        }
	    }
		public static Color blend(Color color1, Color color2, double ratio) {
			float r = (float) ratio;
			float ir = (float) 1.0 - r;

			float rgb1[] = new float[3];
			float rgb2[] = new float[3];

			color1.getColorComponents(rgb1);
			color2.getColorComponents(rgb2);

			Color color = new Color(rgb1[0] * r + rgb2[0] * ir,
					rgb1[1] * r + rgb2[1] * ir,
					rgb1[2] * r + rgb2[2] * ir);

			return color;
		}
	    public static String hexFromInt(int color) {
	        return hexFromInt(new Color(color));
	    }
	    public static String hexFromInt(Color color) {
	        return Integer.toHexString(color.getRGB()).substring(2);
	    }
	}
	
	
	public static final class Stencil
    {
      private static final Stencil INSTANCE = new Stencil();
      private final HashMap<Integer, Stencil.StencilFunc> stencilFuncs = new HashMap<>();
      private int layers = 1;     
      private boolean renderMask;
      /**
       * @author Andrew
       */
      public static Stencil getInstance()
      {
        return INSTANCE;
      }
      public void setRenderMask(boolean renderMask)
      {
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
      public void startLayer()
      {
        if (layers == 1) {
          GL11.glClearStencil(0);
          GL11.glClear(1024);
        }
        GL11.glEnable(2960);
        layers += 1;
        if (layers > getMaximumLayers()) {
          System.out.println("StencilUtil: Reached maximum amount of layers!");
          layers = 1;
          return;
        }
      }     
      public void stopLayer()
      {
        if (layers == 1) {
          System.out.println("StencilUtil: No layers found!");
          return;
        }
        layers -= 1;
        if (layers == 1) {
          GL11.glDisable(2960);
        } else {
          Stencil.StencilFunc lastStencilFunc = (Stencil.StencilFunc)stencilFuncs.remove(Integer.valueOf(layers));
          if (lastStencilFunc != null) {
            lastStencilFunc.use();
          }
        }
      }    
      public void clear()
      {
        GL11.glClearStencil(0);
        GL11.glClear(1024);
        stencilFuncs.clear();
        layers = 1;
      }   
      public void setBuffer()
      {
        setStencilFunc(new Stencil.StencilFunc(this, !renderMask ? 512 : 519, layers, getMaximumLayers(), 7681, 7680, 7680));
      }      
      public void setBuffer(boolean set)
      {
        setStencilFunc(new Stencil.StencilFunc(this, !renderMask ? 512 : 519, set ? layers : layers - 1, getMaximumLayers(), 7681, 7681, 7681));
      }     
      public void cropOutside()
      {
        setStencilFunc(new Stencil.StencilFunc(this, 517, layers, getMaximumLayers(), 7680, 7680, 7680));
      }     
      public void cropInside()
      {
        setStencilFunc(new Stencil.StencilFunc(this, 514, layers, getMaximumLayers(), 7680, 7680, 7680));
      }     
      public void setStencilFunc(Stencil.StencilFunc stencilFunc)
      {
        GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
        GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
        stencilFuncs.put(Integer.valueOf(layers), stencilFunc);
      }     
      public Stencil.StencilFunc getStencilFunc()
      {
        return (Stencil.StencilFunc)stencilFuncs.get(Integer.valueOf(layers));
      }   
      public int getLayer()
      {
        return layers;
      }     
      public int getStencilBufferSize()
      {
        return GL11.glGetInteger(3415);
      }
      public int getMaximumLayers()
      {
        return (int)(Math.pow(2.0D, getStencilBufferSize()) - 1.0D);
      }
     public void createCirlce(double x, double y, double radius)
      {
        GL11.glBegin(6);
        for (int i = 0; i <= 360; i++) {
          double sin = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
          double cos = Math.cos(i * 3.141592653589793D / 180.0D) * radius;
          GL11.glVertex2d(x + sin, y + cos);
        }
        GL11.glEnd();
      }
     public void createRect(double x, double y, double x2, double y2)
      {
        GL11.glBegin(7);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
      }

    public static class StencilFunc
     {
       public static  int func_func;
       public static  int func_ref;
       public static  int func_mask;
       public static  int op_fail;
       public static  int op_zfail;
       public static  int op_zpass;
       
       public StencilFunc(Stencil paramStencil, int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
         this.func_func = func_func;
         this.func_ref = func_ref;
         this.func_mask = func_mask;
         this.op_fail = op_fail;
         this.op_zfail = op_zfail;
         this.op_zpass = op_zpass;
       }
       
       public void use() {
         GL11.glStencilFunc(func_func, func_ref, func_mask);
         GL11.glStencilOp(op_fail, op_zfail, op_zpass);
       }
     }        
    }
    public static class ShaderUtil {

        public static int TYPE_FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
        public static int TYPE_VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;

        public static void bindShader(String shaderCode, int shaderType){
            int shader;
            try {
                shader = createShader(shaderCode, shaderType);
            } catch (Exception e) {
                return;
            }

            int program = 0;

            program = ARBShaderObjects.glCreateProgramObjectARB();

            if(program == 0)
                return;

        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
            ARBShaderObjects.glAttachObjectARB(program, shader);

            ARBShaderObjects.glLinkProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(program));
                return;
            }

            ARBShaderObjects.glValidateProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(program));
                return;
            }

            ARBShaderObjects.glUseProgramObjectARB(program);

        }

        public static int bindShader(int shader){
            int program = 0;

            program = ARBShaderObjects.glCreateProgramObjectARB();

            if(program == 0)
                return 0;

        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
            ARBShaderObjects.glAttachObjectARB(program, shader);

            ARBShaderObjects.glLinkProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(program));
                return program;
            }

            ARBShaderObjects.glValidateProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(program));
                return program;
            }

            ARBShaderObjects.glUseProgramObjectARB(program);

            return program;

        }

        public static void unbindShader(){
            ARBShaderObjects.glUseProgramObjectARB(0);
        }

        public static int createShader(String shaderCode, int shaderType) throws Exception {
            int shader = 0;
            try {
                //Create shader program
                shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

                if(shader == 0)
                    return 0;

                //Load and compile shader src
                ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
                ARBShaderObjects.glCompileShaderARB(shader);

                //Check for errors
                if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                    throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

                return shader;
            }
            catch(Exception exc) {
                ARBShaderObjects.glDeleteObjectARB(shader);
                throw exc;
            }
        }

        private static String getLogInfo(int obj) {
            return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
        }

    }
    public static class Camera {

       private final Minecraft mc = Minecraft.getMinecraft();
       private net.minecraft.util.Timer timer;
       private double posX;
       private double posY;
       private double posZ;
       private float rotationYaw;
       private float rotationPitch;
       public Camera(Entity entity) {
          if(this.timer == null) 
             this.timer = this.mc.timer;        
          this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
          this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
          this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
          this.setRotationYaw(entity.rotationYaw);
          this.setRotationPitch(entity.rotationPitch);
          if(entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
             EntityPlayer living1 = (EntityPlayer)entity;
             this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
             this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
          } else if(entity instanceof EntityLivingBase) {
             EntityLivingBase living = (EntityLivingBase)entity;
             this.setRotationYaw(living.rotationYawHead);
          }
       }

       public Camera(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch) {
          this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
          this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
          this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
          this.setRotationYaw(entity.rotationYaw);
          this.setRotationPitch(entity.rotationPitch);
          if(entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
             EntityPlayer player = (EntityPlayer)entity;
             this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
             this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
          }
          this.posX += offsetX;
          this.posY += offsetY;
          this.posZ += offsetZ;
          this.rotationYaw = (float)((double)this.rotationYaw + offsetRotationYaw);
          this.rotationPitch = (float)((double)this.rotationPitch + offsetRotationPitch);
       }
       public Camera(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
          this.setPosX(posX);
          this.posY = posY;
          this.posZ = posZ;
          this.setRotationYaw(rotationYaw);
          this.setRotationPitch(rotationPitch);
       }
       public double getPosX() {
          return this.posX;
       }
       public void setPosX(double posX) {
          this.posX = posX;
       }
       public double getPosY() {
          return this.posY;
       }

       public void setPosY(double posY) {
          this.posY = posY;
       }
       public double getPosZ() {
          return this.posZ;
       }
       public void setPosZ(double posZ) {
          this.posZ = posZ;
       }
       public float getRotationYaw() {
          return this.rotationYaw;
       }
       public void setRotationYaw(float rotationYaw) {
          this.rotationYaw = rotationYaw;
       }
       public float getRotationPitch() {
          return this.rotationPitch;
       }
       public void setRotationPitch(float rotationPitch) {
          this.rotationPitch = rotationPitch;
       }

       public static float[] getRotation(double posX1, double posY1, double posZ1, double posX2, double posY2, double posZ2) {
          float[] rotation = new float[2];
          double diffX = posX2 - posX1;
          double diffZ = posZ2 - posZ1;
          double diffY = posY2 - posY1;
          double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
          double pitch = -Math.toDegrees(Math.atan(diffY / dist));
          rotation[1] = (float)pitch;
          double yaw = 0.0D;
          if(diffZ >= 0.0D && diffX >= 0.0D) {
             yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
          } else if(diffZ >= 0.0D && diffX <= 0.0D) {
             yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
          } else if(diffZ <= 0.0D && diffX >= 0.0D) {
             yaw = -90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
          } else if(diffZ <= 0.0D && diffX <= 0.0D) {
             yaw = 90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
          }
          rotation[0] = (float)yaw;
          return rotation;
       }
    }
}
