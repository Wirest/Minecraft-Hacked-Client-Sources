/*    */ package rip.jutting.polaris.ui.newconfig;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import rip.jutting.polaris.ui.login.PasswordField;
/*    */ 
/*    */ public class GuiRenameAlt extends GuiScreen
/*    */ {
/*    */   private final GuiAltManager manager;
/*    */   private GuiTextField nameField;
/*    */   private PasswordField pwField;
/*    */   private String status;
/*    */   
/*    */   public GuiRenameAlt(GuiAltManager manager)
/*    */   {
/* 20 */     this.status = (net.minecraft.util.EnumChatFormatting.GRAY + "Waiting...");
/* 21 */     this.manager = manager;
/*    */   }
/*    */   
/*    */   public void actionPerformed(GuiButton button) {
/* 25 */     switch (button.id) {
/*    */     case 1: 
/* 27 */       this.mc.displayGuiScreen(this.manager);
/* 28 */       break;
/*    */     
/*    */     case 0: 
/* 31 */       this.manager.selectedAlt.setMask(this.nameField.getText());
/* 32 */       this.manager.selectedAlt.setPassword(this.pwField.getText());
/* 33 */       this.status = "Edited!";
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */   public void drawScreen(int par1, int par2, float par3)
/*    */   {
/* 41 */     drawCoolBackground();
/* 42 */     drawCenteredString(this.fontRendererObj, "Edit Alt", width / 2, 10, -1);
/* 43 */     drawCenteredString(this.fontRendererObj, this.status, width / 2, 20, -1);
/* 44 */     this.nameField.drawTextBox();
/* 45 */     this.pwField.drawTextBox();
/* 46 */     if (this.nameField.getText().isEmpty()) {
/* 47 */       drawString(this.mc.fontRendererObj, "New name", width / 2 - 96, 66, -7829368);
/*    */     }
/* 49 */     if (this.pwField.getText().isEmpty()) {
/* 50 */       drawString(this.mc.fontRendererObj, "New password", width / 2 - 96, 106, -7829368);
/*    */     }
/* 52 */     super.drawScreen(par1, par2, par3);
/*    */   }
/*    */   
/*    */   public void initGui()
/*    */   {
/* 57 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
/* 58 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
/* 59 */     this.nameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
/* 60 */     this.pwField = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*    */   }
/*    */   
/*    */   protected void keyTyped(char par1, int par2)
/*    */   {
/* 65 */     this.nameField.textboxKeyTyped(par1, par2);
/* 66 */     this.pwField.textboxKeyTyped(par1, par2);
/* 67 */     if ((par1 == '\t') && ((this.nameField.isFocused()) || (this.pwField.isFocused()))) {
/* 68 */       this.nameField.setFocused(!this.nameField.isFocused());
/* 69 */       this.pwField.setFocused(!this.pwField.isFocused());
/*    */     }
/* 71 */     if (par1 == '\r') {
/* 72 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void mouseClicked(int par1, int par2, int par3)
/*    */   {
/*    */     try {
/* 79 */       super.mouseClicked(par1, par2, par3);
/*    */     }
/*    */     catch (IOException e) {
/* 82 */       e.printStackTrace();
/*    */     }
/* 84 */     this.nameField.mouseClicked(par1, par2, par3);
/* 85 */     this.pwField.mouseClicked(par1, par2, par3);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\GuiRenameAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */