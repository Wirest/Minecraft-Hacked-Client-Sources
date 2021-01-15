/*     */ package rip.jutting.polaris.ui.login;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ 
/*     */ public final class GuiAltLogin extends GuiScreen
/*     */ {
/*     */   private PasswordField password;
/*     */   private final GuiScreen previousScreen;
/*     */   private AltLoginThread thread;
/*     */   private GuiTextField username;
/*     */   private GuiTextField comboBox;
/*     */   
/*     */   public GuiAltLogin(GuiScreen previousScreen)
/*     */   {
/*  30 */     this.previousScreen = previousScreen;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  35 */     switch (button.id) {
/*     */     case 1: 
/*  37 */       this.mc.displayGuiScreen(this.previousScreen);
/*  38 */       break;
/*     */     case 2: 
/*     */       try
/*     */       {
/*  42 */         URLConnection openConnection = new URL("http://51.38.119.240/altz/meme.php").openConnection();
/*  43 */         openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
/*     */         
/*  45 */         BufferedReader in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
/*     */         String str;
/*  47 */         while ((str = in.readLine()) != null) {
/*  48 */           String str = in.readLine().toString();
/*  49 */           this.comboBox.setText(str);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */         in.close();
/*     */       } catch (MalformedURLException e) {
/*  58 */         System.out.println("Malformed URL: " + e.getMessage());
/*     */       } catch (IOException e) {
/*  60 */         System.out.println("I/O Error: " + e.getMessage());
/*     */       }
/*     */     
/*     */ 
/*     */     case 0: 
/*  65 */       if ((!this.username.getText().isEmpty()) && (this.comboBox.getText().isEmpty())) {
/*  66 */         this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
/*  67 */         this.thread.start();
/*  68 */       } else if ((this.username.getText().isEmpty()) && (!this.comboBox.getText().isEmpty())) {
/*  69 */         String[] userpass = this.comboBox.getText().split(":");
/*  70 */         String username = userpass[0];
/*  71 */         String password = userpass[1];
/*  72 */         this.thread = new AltLoginThread(username, password);
/*  73 */         this.thread.start();
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int x2, int y2, float z2)
/*     */   {
/*  81 */     CFontRenderer font = FontLoaders.vardana12;
/*  82 */     drawCoolBackground();
/*  83 */     this.username.drawTextBox();
/*  84 */     this.password.drawTextBox();
/*  85 */     this.comboBox.drawTextBox();
/*  86 */     font.drawCenteredString("Alt Login", width / 2, 20.0F, -1);
/*  87 */     font.drawCenteredString(this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), width / 2, 29.0F, -1);
/*  88 */     if (this.username.getText().isEmpty()) {
/*  89 */       font.drawString("Username / E-Mail", width / 2 - 96, 67.0F, -7829368);
/*     */     }
/*  91 */     if (this.password.getText().isEmpty()) {
/*  92 */       font.drawString("Password", width / 2 - 96, 107.0F, -7829368);
/*     */     }
/*  94 */     if (this.comboBox.getText().isEmpty()) {
/*  95 */       font.drawString("User:Pass", width / 2 - 96, 147.0F, -7829368);
/*     */     }
/*  97 */     super.drawScreen(x2, y2, z2);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/* 102 */     int var3 = height / 4 + 24;
/* 103 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
/* 104 */     this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 36, "Generate Alt"));
/* 105 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 48, "Back"));
/* 106 */     this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
/* 107 */     this.comboBox = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
/* 108 */     this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/* 109 */     this.username.setFocused(true);
/* 110 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char character, int key)
/*     */   {
/*     */     try {
/* 116 */       super.keyTyped(character, key);
/*     */     }
/*     */     catch (IOException e) {
/* 119 */       e.printStackTrace();
/*     */     }
/* 121 */     if (character == '\t') {
/* 122 */       if ((!this.username.isFocused()) && (!this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 123 */         this.username.setFocused(true);
/* 124 */         this.password.setFocused(false);
/* 125 */         this.comboBox.setFocused(false);
/* 126 */       } else if ((this.username.isFocused()) && (!this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 127 */         this.password.setFocused(true);
/* 128 */         this.username.setFocused(false);
/* 129 */         this.comboBox.setFocused(false);
/* 130 */       } else if ((!this.username.isFocused()) && (this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 131 */         this.comboBox.setFocused(true);
/* 132 */         this.password.setFocused(false);
/* 133 */         this.username.setFocused(false);
/* 134 */       } else if (this.comboBox.isFocused()) {
/* 135 */         this.username.setFocused(true);
/* 136 */         this.comboBox.setFocused(false);
/*     */       }
/*     */     }
/* 139 */     if (character == '\r') {
/* 140 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/* 142 */     this.username.textboxKeyTyped(character, key);
/* 143 */     this.password.textboxKeyTyped(character, key);
/* 144 */     this.comboBox.textboxKeyTyped(character, key);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int x2, int y2, int button)
/*     */   {
/*     */     try {
/* 150 */       super.mouseClicked(x2, y2, button);
/*     */     }
/*     */     catch (IOException e) {
/* 153 */       e.printStackTrace();
/*     */     }
/* 155 */     this.username.mouseClicked(x2, y2, button);
/* 156 */     this.comboBox.mouseClicked(x2, y2, button);
/* 157 */     this.password.mouseClicked(x2, y2, button);
/*     */   }
/*     */   
/*     */   public void onGuiClosed()
/*     */   {
/* 162 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void updateScreen()
/*     */   {
/* 167 */     this.username.updateCursorCounter();
/* 168 */     this.comboBox.updateCursorCounter();
/* 169 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\login\GuiAltLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */