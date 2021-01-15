package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderHelper
{
    /** Float buffer used to set OpenGL material colors */
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final Vec3 field_82884_b = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
    private static final Vec3 field_82885_c = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();
    private static final String __OBFID = "CL_00000629";

    /**
     * Disables the OpenGL lighting properties enabled by enableStandardItemLighting
     */
    public static void disableStandardItemLighting()
    {
        GlStateManager.disableLighting();
        GlStateManager.disableBooleanStateAt(0);
        GlStateManager.disableBooleanStateAt(1);
        GlStateManager.disableColorMaterial();
    }

    /**
     * Sets the OpenGL lighting properties to the values used when rendering blocks as items
     */
    public static void enableStandardItemLighting()
    {
        GlStateManager.enableLighting();
        GlStateManager.enableBooleanStateAt(0);
        GlStateManager.enableBooleanStateAt(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        float var0 = 0.4F;
        float var1 = 0.6F;
        float var2 = 0.0F;
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(field_82884_b.xCoord, field_82884_b.yCoord, field_82884_b.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(field_82885_c.xCoord, field_82885_c.yCoord, field_82885_c.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(var0, var0, var0, 1.0F));
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_)
    {
        return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_)
    {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        return colorBuffer;
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2)
    {
      drawRect(x, y, x2, y2, col2);
      
      float f = (col1 >> 24 & 0xFF) / 255.0F;
      float f1 = (col1 >> 16 & 0xFF) / 255.0F;
      float f2 = (col1 >> 8 & 0xFF) / 255.0F;
      float f3 = (col1 & 0xFF) / 255.0F;
      
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      
      GL11.glPushMatrix();
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glLineWidth(l1);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
    }
    
    public static void drawRect(float g, float h, float i, float j, int col1)
    {
      float f = (col1 >> 24 & 0xFF) / 255.0F;
      float f1 = (col1 >> 16 & 0xFF) / 255.0F;
      float f2 = (col1 >> 8 & 0xFF) / 255.0F;
      float f3 = (col1 & 0xFF) / 255.0F;
      
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      
      GL11.glPushMatrix();
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(7);
      GL11.glVertex2d(i, h);
      GL11.glVertex2d(g, h);
      GL11.glVertex2d(g, j);
      GL11.glVertex2d(i, j);
      GL11.glEnd();
      GL11.glPopMatrix();
      
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
    }

	/**
     * Sets OpenGL lighting for rendering blocks as items inside GUI screens (such as containers).
     */
    public static void enableGUIStandardItemLighting()
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
