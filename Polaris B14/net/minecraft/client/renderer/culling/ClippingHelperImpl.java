/*    */ package net.minecraft.client.renderer.culling;
/*    */ 
/*    */ import java.nio.Buffer;
/*    */ import java.nio.FloatBuffer;
/*    */ import net.minecraft.client.renderer.GLAllocation;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ 
/*    */ public class ClippingHelperImpl extends ClippingHelper
/*    */ {
/* 10 */   private static ClippingHelperImpl instance = new ClippingHelperImpl();
/* 11 */   private FloatBuffer projectionMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
/* 12 */   private FloatBuffer modelviewMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
/* 13 */   private FloatBuffer field_78564_h = GLAllocation.createDirectFloatBuffer(16);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ClippingHelper getInstance()
/*    */   {
/* 20 */     instance.init();
/* 21 */     return instance;
/*    */   }
/*    */   
/*    */   private void normalize(float[] p_180547_1_)
/*    */   {
/* 26 */     float f = net.minecraft.util.MathHelper.sqrt_float(p_180547_1_[0] * p_180547_1_[0] + p_180547_1_[1] * p_180547_1_[1] + p_180547_1_[2] * p_180547_1_[2]);
/* 27 */     p_180547_1_[0] /= f;
/* 28 */     p_180547_1_[1] /= f;
/* 29 */     p_180547_1_[2] /= f;
/* 30 */     p_180547_1_[3] /= f;
/*    */   }
/*    */   
/*    */   public void init()
/*    */   {
/* 35 */     this.projectionMatrixBuffer.clear();
/* 36 */     this.modelviewMatrixBuffer.clear();
/* 37 */     this.field_78564_h.clear();
/* 38 */     GlStateManager.getFloat(2983, this.projectionMatrixBuffer);
/* 39 */     GlStateManager.getFloat(2982, this.modelviewMatrixBuffer);
/* 40 */     float[] afloat = this.projectionMatrix;
/* 41 */     float[] afloat1 = this.modelviewMatrix;
/* 42 */     this.projectionMatrixBuffer.flip().limit(16);
/* 43 */     this.projectionMatrixBuffer.get(afloat);
/* 44 */     this.modelviewMatrixBuffer.flip().limit(16);
/* 45 */     this.modelviewMatrixBuffer.get(afloat1);
/* 46 */     this.clippingMatrix[0] = (afloat1[0] * afloat[0] + afloat1[1] * afloat[4] + afloat1[2] * afloat[8] + afloat1[3] * afloat[12]);
/* 47 */     this.clippingMatrix[1] = (afloat1[0] * afloat[1] + afloat1[1] * afloat[5] + afloat1[2] * afloat[9] + afloat1[3] * afloat[13]);
/* 48 */     this.clippingMatrix[2] = (afloat1[0] * afloat[2] + afloat1[1] * afloat[6] + afloat1[2] * afloat[10] + afloat1[3] * afloat[14]);
/* 49 */     this.clippingMatrix[3] = (afloat1[0] * afloat[3] + afloat1[1] * afloat[7] + afloat1[2] * afloat[11] + afloat1[3] * afloat[15]);
/* 50 */     this.clippingMatrix[4] = (afloat1[4] * afloat[0] + afloat1[5] * afloat[4] + afloat1[6] * afloat[8] + afloat1[7] * afloat[12]);
/* 51 */     this.clippingMatrix[5] = (afloat1[4] * afloat[1] + afloat1[5] * afloat[5] + afloat1[6] * afloat[9] + afloat1[7] * afloat[13]);
/* 52 */     this.clippingMatrix[6] = (afloat1[4] * afloat[2] + afloat1[5] * afloat[6] + afloat1[6] * afloat[10] + afloat1[7] * afloat[14]);
/* 53 */     this.clippingMatrix[7] = (afloat1[4] * afloat[3] + afloat1[5] * afloat[7] + afloat1[6] * afloat[11] + afloat1[7] * afloat[15]);
/* 54 */     this.clippingMatrix[8] = (afloat1[8] * afloat[0] + afloat1[9] * afloat[4] + afloat1[10] * afloat[8] + afloat1[11] * afloat[12]);
/* 55 */     this.clippingMatrix[9] = (afloat1[8] * afloat[1] + afloat1[9] * afloat[5] + afloat1[10] * afloat[9] + afloat1[11] * afloat[13]);
/* 56 */     this.clippingMatrix[10] = (afloat1[8] * afloat[2] + afloat1[9] * afloat[6] + afloat1[10] * afloat[10] + afloat1[11] * afloat[14]);
/* 57 */     this.clippingMatrix[11] = (afloat1[8] * afloat[3] + afloat1[9] * afloat[7] + afloat1[10] * afloat[11] + afloat1[11] * afloat[15]);
/* 58 */     this.clippingMatrix[12] = (afloat1[12] * afloat[0] + afloat1[13] * afloat[4] + afloat1[14] * afloat[8] + afloat1[15] * afloat[12]);
/* 59 */     this.clippingMatrix[13] = (afloat1[12] * afloat[1] + afloat1[13] * afloat[5] + afloat1[14] * afloat[9] + afloat1[15] * afloat[13]);
/* 60 */     this.clippingMatrix[14] = (afloat1[12] * afloat[2] + afloat1[13] * afloat[6] + afloat1[14] * afloat[10] + afloat1[15] * afloat[14]);
/* 61 */     this.clippingMatrix[15] = (afloat1[12] * afloat[3] + afloat1[13] * afloat[7] + afloat1[14] * afloat[11] + afloat1[15] * afloat[15]);
/* 62 */     float[] afloat2 = this.frustum[0];
/* 63 */     afloat2[0] = (this.clippingMatrix[3] - this.clippingMatrix[0]);
/* 64 */     afloat2[1] = (this.clippingMatrix[7] - this.clippingMatrix[4]);
/* 65 */     afloat2[2] = (this.clippingMatrix[11] - this.clippingMatrix[8]);
/* 66 */     afloat2[3] = (this.clippingMatrix[15] - this.clippingMatrix[12]);
/* 67 */     normalize(afloat2);
/* 68 */     float[] afloat3 = this.frustum[1];
/* 69 */     afloat3[0] = (this.clippingMatrix[3] + this.clippingMatrix[0]);
/* 70 */     afloat3[1] = (this.clippingMatrix[7] + this.clippingMatrix[4]);
/* 71 */     afloat3[2] = (this.clippingMatrix[11] + this.clippingMatrix[8]);
/* 72 */     afloat3[3] = (this.clippingMatrix[15] + this.clippingMatrix[12]);
/* 73 */     normalize(afloat3);
/* 74 */     float[] afloat4 = this.frustum[2];
/* 75 */     afloat4[0] = (this.clippingMatrix[3] + this.clippingMatrix[1]);
/* 76 */     afloat4[1] = (this.clippingMatrix[7] + this.clippingMatrix[5]);
/* 77 */     afloat4[2] = (this.clippingMatrix[11] + this.clippingMatrix[9]);
/* 78 */     afloat4[3] = (this.clippingMatrix[15] + this.clippingMatrix[13]);
/* 79 */     normalize(afloat4);
/* 80 */     float[] afloat5 = this.frustum[3];
/* 81 */     afloat5[0] = (this.clippingMatrix[3] - this.clippingMatrix[1]);
/* 82 */     afloat5[1] = (this.clippingMatrix[7] - this.clippingMatrix[5]);
/* 83 */     afloat5[2] = (this.clippingMatrix[11] - this.clippingMatrix[9]);
/* 84 */     afloat5[3] = (this.clippingMatrix[15] - this.clippingMatrix[13]);
/* 85 */     normalize(afloat5);
/* 86 */     float[] afloat6 = this.frustum[4];
/* 87 */     afloat6[0] = (this.clippingMatrix[3] - this.clippingMatrix[2]);
/* 88 */     afloat6[1] = (this.clippingMatrix[7] - this.clippingMatrix[6]);
/* 89 */     afloat6[2] = (this.clippingMatrix[11] - this.clippingMatrix[10]);
/* 90 */     afloat6[3] = (this.clippingMatrix[15] - this.clippingMatrix[14]);
/* 91 */     normalize(afloat6);
/* 92 */     float[] afloat7 = this.frustum[5];
/* 93 */     afloat7[0] = (this.clippingMatrix[3] + this.clippingMatrix[2]);
/* 94 */     afloat7[1] = (this.clippingMatrix[7] + this.clippingMatrix[6]);
/* 95 */     afloat7[2] = (this.clippingMatrix[11] + this.clippingMatrix[10]);
/* 96 */     afloat7[3] = (this.clippingMatrix[15] + this.clippingMatrix[14]);
/* 97 */     normalize(afloat7);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\culling\ClippingHelperImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */