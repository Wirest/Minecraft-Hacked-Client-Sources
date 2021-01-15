package net.minecraft.client.renderer.culling;

public class ClippingHelper
{
    public float[][] frustum = new float[6][4];
    public float[] field_178625_b = new float[16];
    public float[] field_178626_c = new float[16];
    public float[] clippingMatrix = new float[16];
    private static final String __OBFID = "CL_00000977";

    private float dot(float[] p_178624_1_, float p_178624_2_, float p_178624_4_, float p_178624_6_)
    {
        return p_178624_1_[0] * p_178624_2_ + p_178624_1_[1] * p_178624_4_ + p_178624_1_[2] * p_178624_6_ + p_178624_1_[3];
    }

    /**
     * Returns true if the box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoxInFrustum(double p_78553_1_, double p_78553_3_, double p_78553_5_, double p_78553_7_, double p_78553_9_, double p_78553_11_)
    {
        float minXf = (float)p_78553_1_;
        float minYf = (float)p_78553_3_;
        float minZf = (float)p_78553_5_;
        float maxXf = (float)p_78553_7_;
        float maxYf = (float)p_78553_9_;
        float maxZf = (float)p_78553_11_;

        for (int var13 = 0; var13 < 6; ++var13)
        {
            float[] var14 = this.frustum[var13];

            if (this.dot(var14, minXf, minYf, minZf) <= 0.0F && this.dot(var14, maxXf, minYf, minZf) <= 0.0F && this.dot(var14, minXf, maxYf, minZf) <= 0.0F && this.dot(var14, maxXf, maxYf, minZf) <= 0.0F && this.dot(var14, minXf, minYf, maxZf) <= 0.0F && this.dot(var14, maxXf, minYf, maxZf) <= 0.0F && this.dot(var14, minXf, maxYf, maxZf) <= 0.0F && this.dot(var14, maxXf, maxYf, maxZf) <= 0.0F)
            {
                return false;
            }
        }

        return true;
    }
}
