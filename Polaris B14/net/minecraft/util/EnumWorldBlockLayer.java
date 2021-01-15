/*    */ package net.minecraft.util;
/*    */ 
/*    */ public enum EnumWorldBlockLayer
/*    */ {
/*  5 */   SOLID("Solid"), 
/*  6 */   CUTOUT_MIPPED("Mipped Cutout"), 
/*  7 */   CUTOUT("Cutout"), 
/*  8 */   TRANSLUCENT("Translucent");
/*    */   
/*    */   private final String layerName;
/*    */   
/*    */   private EnumWorldBlockLayer(String layerNameIn)
/*    */   {
/* 14 */     this.layerName = layerNameIn;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 19 */     return this.layerName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EnumWorldBlockLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */