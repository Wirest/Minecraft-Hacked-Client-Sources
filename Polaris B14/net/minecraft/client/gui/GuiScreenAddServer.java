/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class GuiScreenAddServer extends GuiScreen
/*     */ {
/*     */   private final GuiScreen parentScreen;
/*     */   private final ServerData serverData;
/*     */   private GuiTextField serverIPField;
/*     */   private GuiTextField serverNameField;
/*     */   private GuiButton serverResourcePacks;
/*  16 */   private com.google.common.base.Predicate<String> field_181032_r = new com.google.common.base.Predicate() {
/*     */     public boolean apply(String p_apply_1_) {
/*  18 */       if (p_apply_1_.length() == 0) {
/*  19 */         return true;
/*     */       }
/*  21 */       String[] astring = p_apply_1_.split(":");
/*  22 */       if (astring.length == 0) {
/*  23 */         return true;
/*     */       }
/*     */       try {
/*  26 */         String s = java.net.IDN.toASCII(astring[0]);
/*  27 */         return true;
/*     */       } catch (IllegalArgumentException var4) {}
/*  29 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   public GuiScreenAddServer(GuiScreen p_i1033_1_, ServerData p_i1033_2_)
/*     */   {
/*  37 */     this.parentScreen = p_i1033_1_;
/*  38 */     this.serverData = p_i1033_2_;
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  42 */     this.serverNameField.updateCursorCounter();
/*  43 */     this.serverIPField.updateCursorCounter();
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  47 */     org.lwjgl.input.Keyboard.enableRepeatEvents(true);
/*  48 */     this.buttonList.clear();
/*  49 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 18, I18n.format("addServer.add", new Object[0])));
/*  50 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 18, I18n.format("gui.cancel", new Object[0])));
/*  51 */     this.buttonList.add(this.serverResourcePacks = new GuiButton(2, width / 2 - 100, height / 4 + 72, I18n.format("addServer.resourcePack", new Object[0]) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText()));
/*  52 */     this.serverNameField = new GuiTextField(0, this.fontRendererObj, width / 2 - 100, 66, 200, 20);
/*  53 */     this.serverNameField.setFocused(true);
/*  54 */     this.serverNameField.setText(this.serverData.serverName);
/*  55 */     this.serverIPField = new GuiTextField(1, this.fontRendererObj, width / 2 - 100, 106, 200, 20);
/*  56 */     this.serverIPField.setMaxStringLength(128);
/*  57 */     this.serverIPField.setText(this.serverData.serverIP);
/*  58 */     this.serverIPField.func_175205_a(this.field_181032_r);
/*  59 */     ((GuiButton)this.buttonList.get(0)).enabled = ((this.serverIPField.getText().length() > 0) && (this.serverIPField.getText().split(":").length > 0) && (this.serverNameField.getText().length() > 0));
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/*  63 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  67 */     if (button.enabled) {
/*  68 */       if (button.id == 2) {
/*  69 */         this.serverData.setResourceMode(net.minecraft.client.multiplayer.ServerData.ServerResourceMode.values()[((this.serverData.getResourceMode().ordinal() + 1) % net.minecraft.client.multiplayer.ServerData.ServerResourceMode.values().length)]);
/*  70 */         this.serverResourcePacks.displayString = (I18n.format("addServer.resourcePack", new Object[0]) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText());
/*  71 */       } else if (button.id == 1) {
/*  72 */         this.parentScreen.confirmClicked(false, 0);
/*  73 */       } else if (button.id == 0) {
/*  74 */         this.serverData.serverName = this.serverNameField.getText();
/*  75 */         this.serverData.serverIP = this.serverIPField.getText();
/*  76 */         this.parentScreen.confirmClicked(true, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  82 */     this.serverNameField.textboxKeyTyped(typedChar, keyCode);
/*  83 */     this.serverIPField.textboxKeyTyped(typedChar, keyCode);
/*  84 */     if (keyCode == 15) {
/*  85 */       this.serverNameField.setFocused(!this.serverNameField.isFocused());
/*  86 */       this.serverIPField.setFocused(!this.serverIPField.isFocused());
/*     */     }
/*     */     
/*  89 */     if ((keyCode == 28) || (keyCode == 156)) {
/*  90 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*     */     
/*  93 */     ((GuiButton)this.buttonList.get(0)).enabled = ((this.serverIPField.getText().length() > 0) && (this.serverIPField.getText().split(":").length > 0) && (this.serverNameField.getText().length() > 0));
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  97 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  98 */     this.serverIPField.mouseClicked(mouseX, mouseY, mouseButton);
/*  99 */     this.serverNameField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 103 */     drawDefaultBackground();
/* 104 */     drawCenteredString(this.fontRendererObj, I18n.format("addServer.title", new Object[0]), width / 2, 17, 16777215);
/* 105 */     drawString(this.fontRendererObj, I18n.format("addServer.enterName", new Object[0]), width / 2 - 100, 53, 10526880);
/* 106 */     drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), width / 2 - 100, 94, 10526880);
/* 107 */     this.serverNameField.drawTextBox();
/* 108 */     this.serverIPField.drawTextBox();
/* 109 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenAddServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */