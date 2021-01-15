/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import net.minecraft.util.EnumFacing.Axis;
/*    */ import org.lwjgl.util.vector.Vector3f;
/*    */ 
/*    */ public class BlockPartRotation
/*    */ {
/*    */   public final Vector3f origin;
/*    */   public final EnumFacing.Axis axis;
/*    */   public final float angle;
/*    */   public final boolean rescale;
/*    */   
/*    */   public BlockPartRotation(Vector3f originIn, EnumFacing.Axis axisIn, float angleIn, boolean rescaleIn)
/*    */   {
/* 15 */     this.origin = originIn;
/* 16 */     this.axis = axisIn;
/* 17 */     this.angle = angleIn;
/* 18 */     this.rescale = rescaleIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\BlockPartRotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */