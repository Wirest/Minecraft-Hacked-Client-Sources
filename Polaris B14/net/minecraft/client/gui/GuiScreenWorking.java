/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class GuiScreenWorking extends GuiScreen implements net.minecraft.util.IProgressUpdate {
/*  6 */   private String field_146591_a = "";
/*  7 */   private String field_146589_f = "";
/*    */   private int progress;
/*    */   private boolean doneWorking;
/*    */   
/*    */   public void displaySavingString(String message) {
/* 12 */     resetProgressAndMessage(message);
/*    */   }
/*    */   
/*    */   public void resetProgressAndMessage(String message) {
/* 16 */     this.field_146591_a = message;
/* 17 */     displayLoadingString("Working...");
/*    */   }
/*    */   
/*    */   public void displayLoadingString(String message) {
/* 21 */     this.field_146589_f = message;
/* 22 */     setLoadingProgress(0);
/*    */   }
/*    */   
/*    */   public void setLoadingProgress(int progress) {
/* 26 */     this.progress = progress;
/*    */   }
/*    */   
/*    */   public void setDoneWorking() {
/* 30 */     this.doneWorking = true;
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 34 */     if (this.doneWorking) {
/* 35 */       if (!this.mc.func_181540_al()) {
/* 36 */         this.mc.displayGuiScreen(null);
/*    */       }
/*    */     } else {
/* 39 */       drawDefaultBackground();
/* 40 */       drawCenteredString(this.fontRendererObj, this.field_146591_a, width / 2, 70, 16777215);
/* 41 */       drawCenteredString(this.fontRendererObj, this.field_146589_f + " " + this.progress + "%", width / 2, 90, 16777215);
/* 42 */       super.drawScreen(mouseX, mouseY, partialTicks);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenWorking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */