/*    */ package rip.jutting.polaris.ui.config;
/*    */ 
/*    */ public class Config implements Label
/*    */ {
/*    */   private final String label;
/*    */   private final String name;
/*    */   
/*    */   public Config(String label, String name) {
/*  9 */     this.label = label;
/* 10 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String getLabel()
/*    */   {
/* 15 */     return this.label;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\config\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */