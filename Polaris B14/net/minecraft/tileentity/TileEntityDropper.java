/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TileEntityDropper
/*    */   extends TileEntityDispenser
/*    */ {
/*    */   public String getName()
/*    */   {
/* 10 */     return hasCustomName() ? this.customName : "container.dropper";
/*    */   }
/*    */   
/*    */   public String getGuiID()
/*    */   {
/* 15 */     return "minecraft:dropper";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */