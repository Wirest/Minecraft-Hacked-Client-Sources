/*     */ package rip.jutting.polaris.ui.protection;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiMainMenu;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.ui.login.PasswordField;
/*     */ 
/*     */ public final class GuiAuth
/*     */   extends GuiScreen
/*     */ {
/*  28 */   private static final ResourceLocation background = new ResourceLocation("polaris/background.jpg");
/*     */   static PasswordField key;
/*     */   static GuiScreen previousScreen;
/*     */   private AuthLoginThread thread;
/*     */   public static GuiTextField username;
/*     */   ExecutorService async;
/*     */   
/*     */   public GuiAuth()
/*     */   {
/*  37 */     this.async = Executors.newCachedThreadPool();
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*  41 */     keyPressed();
/*  42 */     switch (button.id) {
/*     */     case 2: 
/*  44 */       (this.thread = new AuthLoginThread(username.getText(), key.getText())).start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int x, int y, float z)
/*     */   {
/*  50 */     ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/*  51 */     this.mc.getTextureManager().bindTexture(new ResourceLocation("polaris/background.jpg"));
/*  52 */     Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
/*  53 */     drawGradientRect(0, 0, width, height, 495846, Integer.MIN_VALUE);
/*  54 */     keyPressed();
/*  55 */     if (Polaris.valid) {
/*  56 */       this.mc.displayGuiScreen(new GuiMainMenu());
/*     */     }
/*  58 */     CFontRenderer font = FontLoaders.vardana12;
/*     */     
/*  60 */     font.drawCenteredString(this.thread == null ? "" : this.thread.getStatus(), width / 13 + 290, 240.0F, 2);
/*  61 */     username.drawTextBox();
/*  62 */     key.drawTextBox();
/*  63 */     key.drawTextBox();
/*  64 */     if (username.getText().isEmpty()) {
/*  65 */       font.drawStringWithShadow("Username", width / 2 - 96, 67.0D, -7829368);
/*     */     }
/*     */     
/*  68 */     if (key.getText().isEmpty()) {
/*  69 */       font.drawStringWithShadow("Password", width / 2 - 96, 107.0D, -7829368);
/*     */     }
/*     */     
/*  72 */     super.drawScreen(x, y, z);
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  76 */     keyPressed();
/*  77 */     int var3 = height / 8 + 24;
/*  78 */     this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 42 - 25, "Login"));
/*  79 */     Minecraft mc = this.mc;
/*  80 */     Minecraft mc2 = this.mc;
/*  81 */     username = new GuiTextField(var3, Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  82 */     Minecraft mc3 = this.mc;
/*  83 */     key = new PasswordField(Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, 100, 200, 20);
/*  84 */     username.setFocused(true);
/*  85 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char character, int key) {
/*  89 */     keyPressed();
/*     */     try
/*     */     {
/*  92 */       super.keyTyped(character, key);
/*     */     } catch (IOException var4) {
/*  94 */       var4.printStackTrace();
/*     */     }
/*     */     
/*  97 */     if (character == '\t') {
/*  98 */       if ((!username.isFocused()) && (!key.isFocused())) {
/*  99 */         username.setFocused(true);
/*     */       } else {
/* 101 */         username.setFocused(key.isFocused());
/* 102 */         key.setFocused(!username.isFocused());
/*     */       }
/*     */     }
/*     */     
/* 106 */     if (character == '\r') {
/* 107 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*     */     
/* 110 */     username.textboxKeyTyped(character, key);
/* 111 */     key.textboxKeyTyped(character, key);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int x, int y, int button) {
/* 115 */     keyPressed();
/*     */     try
/*     */     {
/* 118 */       super.mouseClicked(x, y, button);
/*     */     } catch (IOException var5) {
/* 120 */       var5.printStackTrace();
/*     */     }
/*     */     
/* 123 */     username.mouseClicked(x, y, button);
/* 124 */     key.mouseClicked(x, y, button);
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/* 128 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/* 132 */     username.updateCursorCounter();
/* 133 */     key.updateCursorCounter();
/*     */   }
/*     */   
/*     */   public void keyPressed() {
/* 137 */     if ((Keyboard.isKeyDown(64)) || (Keyboard.isKeyDown(1))) {
/*     */       try {
/* 139 */         Desktop.getDesktop().browse(new URI(""));
/*     */       } catch (IOException var2) {
/* 141 */         var2.printStackTrace();
/*     */       } catch (URISyntaxException var3) {
/* 143 */         var3.printStackTrace();
/*     */       }
/*     */       
/* 146 */       this.mc.shutdown();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\protection\GuiAuth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */