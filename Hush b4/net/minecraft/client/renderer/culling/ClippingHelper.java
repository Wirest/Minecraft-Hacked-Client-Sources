// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.culling;

public class ClippingHelper
{
    public float[][] frustum;
    public float[] projectionMatrix;
    public float[] modelviewMatrix;
    public float[] clippingMatrix;
    private static final String __OBFID = "CL_00000977";
    
    public ClippingHelper() {
        this.frustum = new float[6][4];
        this.projectionMatrix = new float[16];
        this.modelviewMatrix = new float[16];
        this.clippingMatrix = new float[16];
    }
    
    private float dot(final float[] p_dot_1_, final float p_dot_2_, final float p_dot_3_, final float p_dot_4_) {
        return p_dot_1_[0] * p_dot_2_ + p_dot_1_[1] * p_dot_3_ + p_dot_1_[2] * p_dot_4_ + p_dot_1_[3];
    }
    
    public boolean isBoxInFrustum(final double p_78553_1_, final double p_78553_3_, final double p_78553_5_, final double p_78553_7_, final double p_78553_9_, final double p_78553_11_) {
        final float f = (float)p_78553_1_;
        final float f2 = (float)p_78553_3_;
        final float f3 = (float)p_78553_5_;
        final float f4 = (float)p_78553_7_;
        final float f5 = (float)p_78553_9_;
        final float f6 = (float)p_78553_11_;
        for (int i = 0; i < 6; ++i) {
            final float[] afloat = this.frustum[i];
            if (this.dot(afloat, f, f2, f3) <= 0.0f && this.dot(afloat, f4, f2, f3) <= 0.0f && this.dot(afloat, f, f5, f3) <= 0.0f && this.dot(afloat, f4, f5, f3) <= 0.0f && this.dot(afloat, f, f2, f6) <= 0.0f && this.dot(afloat, f4, f2, f6) <= 0.0f && this.dot(afloat, f, f5, f6) <= 0.0f && this.dot(afloat, f4, f5, f6) <= 0.0f) {
                return false;
            }
        }
        return true;
    }
}
