/*    */ package rip.jutting.polaris.module;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModuleComparator
/*    */   implements Comparator<Module>
/*    */ {
/*    */   public int compare(Module o1, Module o2)
/*    */   {
/* 14 */     CFontRenderer font = FontLoaders.vardana12;
/*    */     
/* 16 */     if (font.getStringWidth(o1.getDisplayName()) < font.getStringWidth(o2.getDisplayName())) {
/* 17 */       return 1;
/*    */     }
/* 19 */     if (font.getStringWidth(o1.getDisplayName()) > font.getStringWidth(o2.getDisplayName())) {
/* 20 */       return -1;
/*    */     }
/* 22 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\ModuleComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */