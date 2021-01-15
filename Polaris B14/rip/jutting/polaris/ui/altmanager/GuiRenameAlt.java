/*    */ package rip.jutting.polaris.ui.altmanager;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiRenameAlt
/*    */   extends GuiScreen
/*    */ {
/*    */   private final GuiAltManager manager;
/*    */   private GuiTextField nameField;
/*    */   private PasswordField pwField;
/*    */   private String status;
/*    */   
/*    */   public GuiRenameAlt(GuiAltManager manager)
/*    */   {
/* 25 */     this.status = (EnumChatFormatting.GRAY + "Waiting...");
/* 26 */     this.manager = manager;
/*    */   }
/*    */   
/*    */   public void actionPerformed(GuiButton button) {
/* 30 */     switch (button.id) {
/*    */     case 1: 
/* 32 */       this.mc.displayGuiScreen(this.manager);
/* 33 */       break;
/*    */     
/*    */     case 0: 
/* 36 */       this.manager.selectedAlt.setMask(this.nameField.getText());
/* 37 */       this.manager.selectedAlt.setPassword(this.pwField.getText());
/* 38 */       this.status = "Edited!";
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */   public void drawScreen(int par1, int par2, float par3)
/*    */   {
/* 46 */     CFontRenderer font = FontLoaders.vardana12;
/* 47 */     drawCoolBackground();
/* 48 */     font.drawCenteredString("Edit Alt", width / 2, 10.0F, -1);
/* 49 */     font.drawCenteredString(this.status, width / 2, 20.0F, -1);
/* 50 */     this.nameField.drawTextBox();
/* 51 */     this.pwField.drawTextBox();
/* 52 */     if (this.nameField.getText().isEmpty()) {
/* 53 */       font.drawString("Username", width / 2 - 96, 67.0F, -7829368);
/*    */     }
/* 55 */     if (this.pwField.getText().isEmpty()) {
/* 56 */       font.drawString("Password", width / 2 - 96, 107.0F, -7829368);
/*    */     }
/* 58 */     super.drawScreen(par1, par2, par3);
/*    */   }
/*    */   
/*    */   public void initGui()
/*    */   {
/* 63 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
/* 64 */     this.buttonList
/* 65 */       .add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
/* 66 */     this.nameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 
/* 67 */       200, 20);
/* 68 */     this.pwField = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*    */   }
/*    */   
/*    */   protected void keyTyped(char par1, int par2)
/*    */   {
/* 73 */     this.nameField.textboxKeyTyped(par1, par2);
/* 74 */     this.pwField.textboxKeyTyped(par1, par2);
/* 75 */     if ((par1 == '\t') && ((this.nameField.isFocused()) || (this.pwField.isFocused()))) {
/* 76 */       this.nameField.setFocused(!this.nameField.isFocused());
/* 77 */       this.pwField.setFocused(!this.pwField.isFocused());
/*    */     }
/* 79 */     if (par1 == '\r') {
/* 80 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void mouseClicked(int par1, int par2, int par3)
/*    */   {
/*    */     try {
/* 87 */       super.mouseClicked(par1, par2, par3);
/*    */     } catch (IOException e) {
/* 89 */       e.printStackTrace();
/*    */     }
/* 91 */     this.nameField.mouseClicked(par1, par2, par3);
/* 92 */     this.pwField.mouseClicked(par1, par2, par3);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\GuiRenameAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */