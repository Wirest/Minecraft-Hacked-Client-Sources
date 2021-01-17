// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.culling;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.FloatBuffer;

public class ClippingHelperImpl extends ClippingHelper
{
    private static ClippingHelperImpl instance;
    private FloatBuffer projectionMatrixBuffer;
    private FloatBuffer modelviewMatrixBuffer;
    private FloatBuffer field_78564_h;
    
    static {
        ClippingHelperImpl.instance = new ClippingHelperImpl();
    }
    
    public ClippingHelperImpl() {
        this.projectionMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.modelviewMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.field_78564_h = GLAllocation.createDirectFloatBuffer(16);
    }
    
    public static ClippingHelper getInstance() {
        ClippingHelperImpl.instance.init();
        return ClippingHelperImpl.instance;
    }
    
    private void normalize(final float[] p_180547_1_) {
        final float f = MathHelper.sqrt_float(p_180547_1_[0] * p_180547_1_[0] + p_180547_1_[1] * p_180547_1_[1] + p_180547_1_[2] * p_180547_1_[2]);
        final int n = 0;
        p_180547_1_[n] /= f;
        final int n2 = 1;
        p_180547_1_[n2] /= f;
        final int n3 = 2;
        p_180547_1_[n3] /= f;
        final int n4 = 3;
        p_180547_1_[n4] /= f;
    }
    
    public void init() {
        this.projectionMatrixBuffer.clear();
        this.modelviewMatrixBuffer.clear();
        this.field_78564_h.clear();
        GlStateManager.getFloat(2983, this.projectionMatrixBuffer);
        GlStateManager.getFloat(2982, this.modelviewMatrixBuffer);
        final float[] afloat = this.projectionMatrix;
        final float[] afloat2 = this.modelviewMatrix;
        this.projectionMatrixBuffer.flip().limit(16);
        this.projectionMatrixBuffer.get(afloat);
        this.modelviewMatrixBuffer.flip().limit(16);
        this.modelviewMatrixBuffer.get(afloat2);
        this.clippingMatrix[0] = afloat2[0] * afloat[0] + afloat2[1] * afloat[4] + afloat2[2] * afloat[8] + afloat2[3] * afloat[12];
        this.clippingMatrix[1] = afloat2[0] * afloat[1] + afloat2[1] * afloat[5] + afloat2[2] * afloat[9] + afloat2[3] * afloat[13];
        this.clippingMatrix[2] = afloat2[0] * afloat[2] + afloat2[1] * afloat[6] + afloat2[2] * afloat[10] + afloat2[3] * afloat[14];
        this.clippingMatrix[3] = afloat2[0] * afloat[3] + afloat2[1] * afloat[7] + afloat2[2] * afloat[11] + afloat2[3] * afloat[15];
        this.clippingMatrix[4] = afloat2[4] * afloat[0] + afloat2[5] * afloat[4] + afloat2[6] * afloat[8] + afloat2[7] * afloat[12];
        this.clippingMatrix[5] = afloat2[4] * afloat[1] + afloat2[5] * afloat[5] + afloat2[6] * afloat[9] + afloat2[7] * afloat[13];
        this.clippingMatrix[6] = afloat2[4] * afloat[2] + afloat2[5] * afloat[6] + afloat2[6] * afloat[10] + afloat2[7] * afloat[14];
        this.clippingMatrix[7] = afloat2[4] * afloat[3] + afloat2[5] * afloat[7] + afloat2[6] * afloat[11] + afloat2[7] * afloat[15];
        this.clippingMatrix[8] = afloat2[8] * afloat[0] + afloat2[9] * afloat[4] + afloat2[10] * afloat[8] + afloat2[11] * afloat[12];
        this.clippingMatrix[9] = afloat2[8] * afloat[1] + afloat2[9] * afloat[5] + afloat2[10] * afloat[9] + afloat2[11] * afloat[13];
        this.clippingMatrix[10] = afloat2[8] * afloat[2] + afloat2[9] * afloat[6] + afloat2[10] * afloat[10] + afloat2[11] * afloat[14];
        this.clippingMatrix[11] = afloat2[8] * afloat[3] + afloat2[9] * afloat[7] + afloat2[10] * afloat[11] + afloat2[11] * afloat[15];
        this.clippingMatrix[12] = afloat2[12] * afloat[0] + afloat2[13] * afloat[4] + afloat2[14] * afloat[8] + afloat2[15] * afloat[12];
        this.clippingMatrix[13] = afloat2[12] * afloat[1] + afloat2[13] * afloat[5] + afloat2[14] * afloat[9] + afloat2[15] * afloat[13];
        this.clippingMatrix[14] = afloat2[12] * afloat[2] + afloat2[13] * afloat[6] + afloat2[14] * afloat[10] + afloat2[15] * afloat[14];
        this.clippingMatrix[15] = afloat2[12] * afloat[3] + afloat2[13] * afloat[7] + afloat2[14] * afloat[11] + afloat2[15] * afloat[15];
        final float[] afloat3 = this.frustum[0];
        afloat3[0] = this.clippingMatrix[3] - this.clippingMatrix[0];
        afloat3[1] = this.clippingMatrix[7] - this.clippingMatrix[4];
        afloat3[2] = this.clippingMatrix[11] - this.clippingMatrix[8];
        afloat3[3] = this.clippingMatrix[15] - this.clippingMatrix[12];
        this.normalize(afloat3);
        final float[] afloat4 = this.frustum[1];
        afloat4[0] = this.clippingMatrix[3] + this.clippingMatrix[0];
        afloat4[1] = this.clippingMatrix[7] + this.clippingMatrix[4];
        afloat4[2] = this.clippingMatrix[11] + this.clippingMatrix[8];
        afloat4[3] = this.clippingMatrix[15] + this.clippingMatrix[12];
        this.normalize(afloat4);
        final float[] afloat5 = this.frustum[2];
        afloat5[0] = this.clippingMatrix[3] + this.clippingMatrix[1];
        afloat5[1] = this.clippingMatrix[7] + this.clippingMatrix[5];
        afloat5[2] = this.clippingMatrix[11] + this.clippingMatrix[9];
        afloat5[3] = this.clippingMatrix[15] + this.clippingMatrix[13];
        this.normalize(afloat5);
        final float[] afloat6 = this.frustum[3];
        afloat6[0] = this.clippingMatrix[3] - this.clippingMatrix[1];
        afloat6[1] = this.clippingMatrix[7] - this.clippingMatrix[5];
        afloat6[2] = this.clippingMatrix[11] - this.clippingMatrix[9];
        afloat6[3] = this.clippingMatrix[15] - this.clippingMatrix[13];
        this.normalize(afloat6);
        final float[] afloat7 = this.frustum[4];
        afloat7[0] = this.clippingMatrix[3] - this.clippingMatrix[2];
        afloat7[1] = this.clippingMatrix[7] - this.clippingMatrix[6];
        afloat7[2] = this.clippingMatrix[11] - this.clippingMatrix[10];
        afloat7[3] = this.clippingMatrix[15] - this.clippingMatrix[14];
        this.normalize(afloat7);
        final float[] afloat8 = this.frustum[5];
        afloat8[0] = this.clippingMatrix[3] + this.clippingMatrix[2];
        afloat8[1] = this.clippingMatrix[7] + this.clippingMatrix[6];
        afloat8[2] = this.clippingMatrix[11] + this.clippingMatrix[10];
        afloat8[3] = this.clippingMatrix[15] + this.clippingMatrix[14];
        this.normalize(afloat8);
    }
}
