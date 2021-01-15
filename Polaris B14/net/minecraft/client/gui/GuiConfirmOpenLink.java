/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ 
/*    */ public class GuiConfirmOpenLink
/*    */   extends GuiYesNo
/*    */ {
/*    */   private final String openLinkWarning;
/*    */   private final String copyLinkButtonText;
/*    */   private final String linkText;
/* 14 */   private boolean showSecurityWarning = true;
/*    */   
/*    */   public GuiConfirmOpenLink(GuiYesNoCallback p_i1084_1_, String linkTextIn, int p_i1084_3_, boolean p_i1084_4_)
/*    */   {
/* 18 */     super(p_i1084_1_, I18n.format(p_i1084_4_ ? "chat.link.confirmTrusted" : "chat.link.confirm", new Object[0]), linkTextIn, p_i1084_3_);
/* 19 */     this.confirmButtonText = I18n.format(p_i1084_4_ ? "chat.link.open" : "gui.yes", new Object[0]);
/* 20 */     this.cancelButtonText = I18n.format(p_i1084_4_ ? "gui.cancel" : "gui.no", new Object[0]);
/* 21 */     this.copyLinkButtonText = I18n.format("chat.copy", new Object[0]);
/* 22 */     this.openLinkWarning = I18n.format("chat.link.warning", new Object[0]);
/* 23 */     this.linkText = linkTextIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initGui()
/*    */   {
/* 32 */     super.initGui();
/* 33 */     this.buttonList.clear();
/* 34 */     this.buttonList.add(new GuiButton(0, width / 2 - 50 - 105, height / 6 + 96, 100, 20, this.confirmButtonText));
/* 35 */     this.buttonList.add(new GuiButton(2, width / 2 - 50, height / 6 + 96, 100, 20, this.copyLinkButtonText));
/* 36 */     this.buttonList.add(new GuiButton(1, width / 2 - 50 + 105, height / 6 + 96, 100, 20, this.cancelButtonText));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void actionPerformed(GuiButton button)
/*    */     throws IOException
/*    */   {
/* 44 */     if (button.id == 2)
/*    */     {
/* 46 */       copyLinkToClipboard();
/*    */     }
/*    */     
/* 49 */     this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void copyLinkToClipboard()
/*    */   {
/* 57 */     setClipboardString(this.linkText);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*    */   {
/* 65 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */     
/* 67 */     if (this.showSecurityWarning)
/*    */     {
/* 69 */       drawCenteredString(this.fontRendererObj, this.openLinkWarning, width / 2, 110, 16764108);
/*    */     }
/*    */   }
/*    */   
/*    */   public void disableSecurityWarning()
/*    */   {
/* 75 */     this.showSecurityWarning = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiConfirmOpenLink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */