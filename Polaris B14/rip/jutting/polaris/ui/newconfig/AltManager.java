/*    */ package rip.jutting.polaris.ui.newconfig;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AltManager
/*    */ {
/*    */   public static Alt lastAlt;
/* 14 */   public static ArrayList<Alt> registry = new ArrayList();
/*    */   
/*    */   public AltManager()
/*    */   {
/* 18 */     registry = new ArrayList();
/*    */   }
/*    */   
/*    */   public ArrayList<Alt> getRegistry() {
/* 22 */     return registry;
/*    */   }
/*    */   
/*    */   public void setLastAlt(Alt alt) {
/* 26 */     lastAlt = alt;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\AltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */