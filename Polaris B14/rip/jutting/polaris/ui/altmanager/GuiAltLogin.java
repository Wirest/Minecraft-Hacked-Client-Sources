/*     */ package rip.jutting.polaris.ui.altmanager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiAltLogin
/*     */   extends GuiScreen
/*     */ {
/*     */   private PasswordField password;
/*     */   private final GuiScreen previousScreen;
/*     */   private AltLoginThread thread;
/*     */   private GuiTextField username;
/*     */   
/*     */   public GuiAltLogin(GuiScreen previousScreen)
/*     */   {
/*  24 */     this.previousScreen = previousScreen;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  29 */     switch (button.id) {
/*     */     case 1: 
/*  31 */       this.mc.displayGuiScreen(this.previousScreen);
/*  32 */       break;
/*     */     
/*     */     case 0: 
/*  35 */       (this.thread = new AltLoginThread(this.username.getText(), this.password.getText())).start();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawScreen(int x, int y, float z)
/*     */   {
/*  43 */     drawCoolBackground();
/*  44 */     this.username.drawTextBox();
/*  45 */     this.password.drawTextBox();
/*  46 */     drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
/*  47 */     drawCenteredString(this.mc.fontRendererObj, 
/*  48 */       this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), 
/*  49 */       width / 2, 29, -1);
/*  50 */     if (this.username.getText().isEmpty()) {
/*  51 */       drawString(this.mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
/*     */     }
/*  53 */     if (this.password.getText().isEmpty()) {
/*  54 */       drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
/*     */     }
/*  56 */     super.drawScreen(x, y, z);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/*  61 */     int var3 = height / 4 + 24;
/*  62 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
/*  63 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
/*  64 */     this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  65 */     this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*  66 */     this.username.setFocused(true);
/*  67 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char character, int key)
/*     */   {
/*     */     try {
/*  73 */       super.keyTyped(character, key);
/*     */     } catch (IOException e) {
/*  75 */       e.printStackTrace();
/*     */     }
/*  77 */     if (character == '\t') {
/*  78 */       if ((!this.username.isFocused()) && (!this.password.isFocused())) {
/*  79 */         this.username.setFocused(true);
/*     */       } else {
/*  81 */         this.username.setFocused(this.password.isFocused());
/*  82 */         this.password.setFocused(!this.username.isFocused());
/*     */       }
/*     */     }
/*  85 */     if (character == '\r') {
/*  86 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*  88 */     this.username.textboxKeyTyped(character, key);
/*  89 */     this.password.textboxKeyTyped(character, key);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int x, int y, int button)
/*     */   {
/*     */     try {
/*  95 */       super.mouseClicked(x, y, button);
/*     */     } catch (IOException e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*  99 */     this.username.mouseClicked(x, y, button);
/* 100 */     this.password.mouseClicked(x, y, button);
/*     */   }
/*     */   
/*     */   public void onGuiClosed()
/*     */   {
/* 105 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void updateScreen()
/*     */   {
/* 110 */     this.username.updateCursorCounter();
/* 111 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\GuiAltLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */