/*    */ package net.minecraft.block.material;
/*    */ 
/*    */ public class MaterialLiquid extends Material
/*    */ {
/*    */   public MaterialLiquid(MapColor color)
/*    */   {
/*  7 */     super(color);
/*  8 */     setReplaceable();
/*  9 */     setNoPushMobility();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isLiquid()
/*    */   {
/* 17 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean blocksMovement()
/*    */   {
/* 25 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isSolid()
/*    */   {
/* 33 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\material\MaterialLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */