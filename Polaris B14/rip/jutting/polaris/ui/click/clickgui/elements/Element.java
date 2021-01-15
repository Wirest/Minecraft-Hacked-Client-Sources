/*    */ package rip.jutting.polaris.ui.click.clickgui.elements;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import rip.jutting.polaris.ui.click.clickgui.ClickGUI;
/*    */ import rip.jutting.polaris.ui.click.clickgui.Panel;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Element
/*    */ {
/*    */   public ClickGUI clickgui;
/*    */   public ModuleButton parent;
/*    */   public Setting set;
/*    */   public double offset;
/*    */   public double x;
/*    */   public double y;
/*    */   public double width;
/*    */   public double height;
/*    */   public String setstrg;
/*    */   public boolean comboextended;
/*    */   
/*    */   public void setup()
/*    */   {
/* 32 */     this.clickgui = this.parent.parent.clickgui;
/*    */   }
/*    */   
/*    */   public void update() {
/* 36 */     CFontRenderer font = FontLoaders.vardana12;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */     this.x = (this.parent.x + this.parent.width + 2.0D);
/* 43 */     this.y = (this.parent.y + this.offset);
/* 44 */     this.width = (this.parent.width + 10.0D);
/* 45 */     this.height = 15.0D;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 51 */     String sname = this.set.getName();
/* 52 */     if (this.set.isCheck()) {
/* 53 */       this.setstrg = (sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length()));
/* 54 */       double textx = this.x + this.width - font.getStringWidth(this.setstrg);
/* 55 */       if (textx < this.x + 13.0D) {
/* 56 */         this.width += this.x + 13.0D - textx + 1.0D;
/*    */       }
/* 58 */     } else if (this.set.isCombo()) {
/* 59 */       this.height = (this.comboextended ? this.set.getOptions().size() * (font.getHeight() + 2) + 15 : 15);
/*    */       
/* 61 */       this.setstrg = (sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length()));
/* 62 */       int longest = font.getStringWidth(this.setstrg);
/* 63 */       for (String s : this.set.getOptions()) {
/* 64 */         int temp = font.getStringWidth(s);
/* 65 */         if (temp > longest) {
/* 66 */           longest = temp;
/*    */         }
/*    */       }
/* 69 */       double textx = this.x + this.width - longest;
/* 70 */       if (textx < this.x) {
/* 71 */         this.width += this.x - textx + 1.0D;
/*    */       }
/* 73 */     } else if (this.set.isSlider()) {
/* 74 */       this.setstrg = (sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length()));
/* 75 */       String displayval = Math.round(this.set.getValDouble() * 100.0D) / 100.0D;
/* 76 */       String displaymax = Math.round(this.set.getMax() * 100.0D) / 100.0D;
/* 77 */       double textx = this.x + this.width - font.getStringWidth(this.setstrg) - font.getStringWidth(displaymax) - 4.0D;
/* 78 */       if (textx < this.x) {
/* 79 */         this.width += this.x - textx + 1.0D;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
/*    */   
/*    */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 87 */     return isHovered(mouseX, mouseY);
/*    */   }
/*    */   
/*    */ 
/*    */   public void mouseReleased(int mouseX, int mouseY, int state) {}
/*    */   
/*    */   public boolean isHovered(int mouseX, int mouseY)
/*    */   {
/* 95 */     return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\elements\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */