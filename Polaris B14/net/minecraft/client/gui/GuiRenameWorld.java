/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.world.storage.ISaveFormat;
/*    */ 
/*    */ public class GuiRenameWorld extends GuiScreen
/*    */ {
/*    */   private GuiScreen parentScreen;
/*    */   private GuiTextField field_146583_f;
/*    */   private final String saveName;
/*    */   
/*    */   public GuiRenameWorld(GuiScreen parentScreenIn, String saveNameIn)
/*    */   {
/* 15 */     this.parentScreen = parentScreenIn;
/* 16 */     this.saveName = saveNameIn;
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 20 */     this.field_146583_f.updateCursorCounter();
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 24 */     org.lwjgl.input.Keyboard.enableRepeatEvents(true);
/* 25 */     this.buttonList.clear();
/* 26 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectWorld.renameButton", new Object[0])));
/* 27 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/* 28 */     ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 29 */     net.minecraft.world.storage.WorldInfo worldinfo = isaveformat.getWorldInfo(this.saveName);
/* 30 */     String s = worldinfo.getWorldName();
/* 31 */     this.field_146583_f = new GuiTextField(2, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
/* 32 */     this.field_146583_f.setFocused(true);
/* 33 */     this.field_146583_f.setText(s);
/*    */   }
/*    */   
/*    */   public void onGuiClosed() {
/* 37 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 41 */     if (button.enabled) {
/* 42 */       if (button.id == 1) {
/* 43 */         this.mc.displayGuiScreen(this.parentScreen);
/* 44 */       } else if (button.id == 0) {
/* 45 */         ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 46 */         isaveformat.renameWorld(this.saveName, this.field_146583_f.getText().trim());
/* 47 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException {
/* 53 */     this.field_146583_f.textboxKeyTyped(typedChar, keyCode);
/* 54 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.field_146583_f.getText().trim().length() > 0);
/* 55 */     if ((keyCode == 28) || (keyCode == 156)) {
/* 56 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws java.io.IOException {
/* 61 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 62 */     this.field_146583_f.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 66 */     drawDefaultBackground();
/* 67 */     drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.renameTitle", new Object[0]), width / 2, 20, 16777215);
/* 68 */     drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), width / 2 - 100, 47, 10526880);
/* 69 */     this.field_146583_f.drawTextBox();
/* 70 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiRenameWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */