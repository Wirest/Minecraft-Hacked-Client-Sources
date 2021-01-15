/*    */ package net.minecraft.client.renderer.culling;
/*    */ 
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ 
/*    */ public class Frustum implements ICamera
/*    */ {
/*    */   private ClippingHelper clippingHelper;
/*    */   private double xPosition;
/*    */   private double yPosition;
/*    */   private double zPosition;
/*    */   
/*    */   public Frustum()
/*    */   {
/* 14 */     this(ClippingHelperImpl.getInstance());
/*    */   }
/*    */   
/*    */   public Frustum(ClippingHelper p_i46196_1_)
/*    */   {
/* 19 */     this.clippingHelper = p_i46196_1_;
/*    */   }
/*    */   
/*    */   public void setPosition(double p_78547_1_, double p_78547_3_, double p_78547_5_)
/*    */   {
/* 24 */     this.xPosition = p_78547_1_;
/* 25 */     this.yPosition = p_78547_3_;
/* 26 */     this.zPosition = p_78547_5_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_, double p_78548_9_, double p_78548_11_)
/*    */   {
/* 34 */     return this.clippingHelper.isBoxInFrustum(p_78548_1_ - this.xPosition, p_78548_3_ - this.yPosition, p_78548_5_ - this.zPosition, p_78548_7_ - this.xPosition, p_78548_9_ - this.yPosition, p_78548_11_ - this.zPosition);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_)
/*    */   {
/* 42 */     return isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY, p_78546_1_.maxZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\culling\Frustum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */