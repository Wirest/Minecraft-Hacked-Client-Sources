/*    */ package net.minecraft.dispenser;
/*    */ 
/*    */ public class PositionImpl implements IPosition
/*    */ {
/*    */   protected final double x;
/*    */   protected final double y;
/*    */   protected final double z;
/*    */   
/*    */   public PositionImpl(double xCoord, double yCoord, double zCoord)
/*    */   {
/* 11 */     this.x = xCoord;
/* 12 */     this.y = yCoord;
/* 13 */     this.z = zCoord;
/*    */   }
/*    */   
/*    */   public double getX()
/*    */   {
/* 18 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY()
/*    */   {
/* 23 */     return this.y;
/*    */   }
/*    */   
/*    */   public double getZ()
/*    */   {
/* 28 */     return this.z;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\dispenser\PositionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */