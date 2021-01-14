package cn.kody.debug.utils.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

import java.nio.FloatBuffer;

public class GLUtils
{
    private static FloatBuffer colorBuffer;
    private static final Vec3 LIGHT0_POS;
    private static final Vec3 LIGHT1_POS;
    
    public GLUtils() {
        super();
    }
    
    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight(0);
        GlStateManager.disableLight(1);
        GlStateManager.disableColorMaterial();
    }
    
    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        final float n = 0.4f;
        final float n2 = 0.6f;
        final float n3 = 0.0f;
        GL11.glLight(16384, 4611, setColorBuffer(GLUtils.LIGHT0_POS.xCoord, GLUtils.LIGHT0_POS.yCoord, GLUtils.LIGHT0_POS.zCoord, 0.0));
        GL11.glLight(16384, 4609, setColorBuffer(n2, n2, n2, 1.0f));
        GL11.glLight(16384, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16384, 4610, setColorBuffer(n3, n3, n3, 1.0f));
        GL11.glLight(16385, 4611, setColorBuffer(GLUtils.LIGHT1_POS.xCoord, GLUtils.LIGHT1_POS.yCoord, GLUtils.LIGHT1_POS.zCoord, 0.0));
        GL11.glLight(16385, 4609, setColorBuffer(n2, n2, n2, 1.0f));
        GL11.glLight(16385, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16385, 4610, setColorBuffer(n3, n3, n3, 1.0f));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(2899, setColorBuffer(n, n, n, 1.0f));
    }
    
    private static FloatBuffer setColorBuffer(final double p_setColorBuffer_0_, final double p_setColorBuffer_2_, final double p_setColorBuffer_4_, final double p_setColorBuffer_6_) {
        return setColorBuffer((float)p_setColorBuffer_0_, (float)p_setColorBuffer_2_, (float)p_setColorBuffer_4_, (float)p_setColorBuffer_6_);
    }
    
    private static FloatBuffer setColorBuffer(final float p_setColorBuffer_0_, final float p_setColorBuffer_1_, final float p_setColorBuffer_2_, final float p_setColorBuffer_3_) {
        GLUtils.colorBuffer.clear();
        GLUtils.colorBuffer.put(p_setColorBuffer_0_).put(p_setColorBuffer_1_).put(p_setColorBuffer_2_).put(p_setColorBuffer_3_);
        GLUtils.colorBuffer.flip();
        return GLUtils.colorBuffer;
    }
    
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    static {
        GLUtils.colorBuffer = GLAllocation.createDirectFloatBuffer(16);
        LIGHT0_POS = new Vec3(0.20000000298023224, 1.0, -0.699999988079071).normalize();
        LIGHT1_POS = new Vec3(-0.20000000298023224, 1.0, 0.699999988079071).normalize();
    }
}
