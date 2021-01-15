/*    */ package rip.jutting.polaris.module;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ 
/*    */ 
/*    */ public class ModuleComparator2
/*    */   implements Comparator<Module>
/*    */ {
/*    */   public int compare(Module o1, Module o2)
/*    */   {
/* 13 */     if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.getDisplayName()) < Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.getDisplayName())) {
/* 14 */       return 1;
/*    */     }
/* 16 */     if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.getDisplayName()) > Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.getDisplayName())) {
/* 17 */       return -1;
/*    */     }
/* 19 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\ModuleComparator2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */