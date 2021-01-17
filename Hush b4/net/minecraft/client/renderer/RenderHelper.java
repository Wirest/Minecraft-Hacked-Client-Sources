// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import net.minecraft.util.Vec3;
import java.nio.FloatBuffer;

public class RenderHelper
{
    private static FloatBuffer colorBuffer;
    private static final Vec3 LIGHT0_POS;
    private static final Vec3 LIGHT1_POS;
    
    static {
        RenderHelper.colorBuffer = GLAllocation.createDirectFloatBuffer(16);
        LIGHT0_POS = new Vec3(0.20000000298023224, 1.0, -0.699999988079071).normalize();
        LIGHT1_POS = new Vec3(-0.20000000298023224, 1.0, 0.699999988079071).normalize();
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
        final float f = 0.4f;
        final float f2 = 0.6f;
        final float f3 = 0.0f;
        GL11.glLight(16384, 4611, setColorBuffer(RenderHelper.LIGHT0_POS.xCoord, RenderHelper.LIGHT0_POS.yCoord, RenderHelper.LIGHT0_POS.zCoord, 0.0));
        GL11.glLight(16384, 4609, setColorBuffer(f2, f2, f2, 1.0f));
        GL11.glLight(16384, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16384, 4610, setColorBuffer(f3, f3, f3, 1.0f));
        GL11.glLight(16385, 4611, setColorBuffer(RenderHelper.LIGHT1_POS.xCoord, RenderHelper.LIGHT1_POS.yCoord, RenderHelper.LIGHT1_POS.zCoord, 0.0));
        GL11.glLight(16385, 4609, setColorBuffer(f2, f2, f2, 1.0f));
        GL11.glLight(16385, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16385, 4610, setColorBuffer(f3, f3, f3, 1.0f));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(2899, setColorBuffer(f, f, f, 1.0f));
    }
    
    private static FloatBuffer setColorBuffer(final double p_74517_0_, final double p_74517_2_, final double p_74517_4_, final double p_74517_6_) {
        return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }
    
    private static FloatBuffer setColorBuffer(final float p_74521_0_, final float p_74521_1_, final float p_74521_2_, final float p_74521_3_) {
        RenderHelper.colorBuffer.clear();
        RenderHelper.colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        RenderHelper.colorBuffer.flip();
        return RenderHelper.colorBuffer;
    }
    
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
