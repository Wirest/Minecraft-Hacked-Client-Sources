/*     */ package rip.jutting.polaris.ui.newconfig;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.ui.login.PasswordField;
/*     */ 
/*     */ public final class GuiAltLogin extends GuiScreen
/*     */ {
/*     */   private PasswordField password;
/*     */   private final GuiScreen previousScreen;
/*     */   private AltLoginThread thread;
/*     */   private GuiTextField username;
/*     */   
/*     */   public GuiAltLogin(GuiScreen previousScreen)
/*     */   {
/*  22 */     this.previousScreen = previousScreen;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  27 */     switch (button.id) {
/*     */     case 1: 
/*  29 */       this.mc.displayGuiScreen(this.previousScreen);
/*  30 */       break;
/*     */     
/*     */     case 0: 
/*  33 */       (this.thread = new AltLoginThread(this.username.getText(), this.password.getText())).start();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawScreen(int x, int y, float z)
/*     */   {
/*  41 */     drawCoolBackground();
/*  42 */     this.username.drawTextBox();
/*  43 */     this.password.drawTextBox();
/*  44 */     drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
/*  45 */     drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
/*  46 */     if (this.username.getText().isEmpty()) {
/*  47 */       drawString(this.mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
/*     */     }
/*  49 */     if (this.password.getText().isEmpty()) {
/*  50 */       drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
/*     */     }
/*  52 */     super.drawScreen(x, y, z);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/*  57 */     int var3 = height / 4 + 24;
/*  58 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
/*  59 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
/*  60 */     this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  61 */     this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*  62 */     this.username.setFocused(true);
/*  63 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char character, int key)
/*     */   {
/*     */     try {
/*  69 */       super.keyTyped(character, key);
/*     */     }
/*     */     catch (IOException e) {
/*  72 */       e.printStackTrace();
/*     */     }
/*  74 */     if (character == '\t') {
/*  75 */       if ((!this.username.isFocused()) && (!this.password.isFocused())) {
/*  76 */         this.username.setFocused(true);
/*     */       }
/*     */       else {
/*  79 */         this.username.setFocused(this.password.isFocused());
/*  80 */         this.password.setFocused(!this.username.isFocused());
/*     */       }
/*     */     }
/*  83 */     if (character == '\r') {
/*  84 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*  86 */     this.username.textboxKeyTyped(character, key);
/*  87 */     this.password.textboxKeyTyped(character, key);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int x, int y, int button)
/*     */   {
/*     */     try {
/*  93 */       super.mouseClicked(x, y, button);
/*     */     }
/*     */     catch (IOException e) {
/*  96 */       e.printStackTrace();
/*     */     }
/*  98 */     this.username.mouseClicked(x, y, button);
/*  99 */     this.password.mouseClicked(x, y, button);
/*     */   }
/*     */   
/*     */   public void onGuiClosed()
/*     */   {
/* 104 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void updateScreen()
/*     */   {
/* 109 */     this.username.updateCursorCounter();
/* 110 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\GuiAltLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */