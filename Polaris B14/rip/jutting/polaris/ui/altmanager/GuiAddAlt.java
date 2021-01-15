/*     */ package rip.jutting.polaris.ui.altmanager;
/*     */ 
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.utils.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiAddAlt
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiAltManager manager;
/*     */   private PasswordField password;
/*     */   private static String status;
/*     */   private GuiTextField username;
/*     */   private GuiTextField comboBox;
/*     */   private static File ALTS_DIR;
/*     */   
/*     */   public GuiAddAlt(GuiAltManager manager)
/*     */   {
/*  39 */     ALTS_DIR = FileUtils.getConfigFile("Alts");
/*  40 */     status = EnumChatFormatting.GRAY + "Idle...";
/*  41 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  46 */     switch (button.id) {
/*     */     case 0: 
/*  48 */       if ((!this.username.getText().isEmpty()) && (this.comboBox.getText().isEmpty())) {
/*  49 */         AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
/*  50 */         login.start();
/*     */       } else {
/*  52 */         String[] userpass = this.comboBox.getText().split(":");
/*  53 */         String username = userpass[0];
/*  54 */         String password = userpass[1];
/*  55 */         AddAltThread login = new AddAltThread(username, password);
/*  56 */         login.start();
/*     */       }
/*  58 */       break;
/*     */     
/*     */     case 1: 
/*  61 */       this.mc.displayGuiScreen(this.manager);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawScreen(int i, int j, float f)
/*     */   {
/*  69 */     CFontRenderer font = FontLoaders.vardana12;
/*  70 */     drawCoolBackground();
/*  71 */     this.username.drawTextBox();
/*  72 */     this.password.drawTextBox();
/*  73 */     this.comboBox.drawTextBox();
/*  74 */     font.drawCenteredString("Add Alt", width / 2, 20.0F, -1);
/*  75 */     if (this.username.getText().isEmpty()) {
/*  76 */       font.drawString("Username / E-Mail", width / 2 - 96, 67.0F, -7829368);
/*     */     }
/*  78 */     if (this.password.getText().isEmpty()) {
/*  79 */       font.drawString("Password", width / 2 - 96, 107.0F, -7829368);
/*     */     }
/*  81 */     if (this.comboBox.getText().isEmpty()) {
/*  82 */       font.drawString("User:Pass", width / 2 - 96, 147.0F, -7829368);
/*     */     }
/*  84 */     font.drawCenteredString(status, width / 2, 30.0F, -1);
/*  85 */     super.drawScreen(i, j, f);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/*  90 */     Keyboard.enableRepeatEvents(true);
/*  91 */     this.buttonList.clear();
/*  92 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 22, "Login"));
/*  93 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 22, "Back"));
/*  94 */     this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 
/*  95 */       20);
/*  96 */     this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*  97 */     this.comboBox = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char par1, int par2)
/*     */   {
/* 102 */     if (par1 == '\t') {
/* 103 */       if ((!this.username.isFocused()) && (!this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 104 */         this.username.setFocused(true);
/* 105 */         this.password.setFocused(false);
/* 106 */         this.comboBox.setFocused(false);
/* 107 */       } else if ((this.username.isFocused()) && (!this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 108 */         this.password.setFocused(true);
/* 109 */         this.username.setFocused(false);
/* 110 */         this.comboBox.setFocused(false);
/* 111 */       } else if ((!this.username.isFocused()) && (this.password.isFocused()) && (!this.comboBox.isFocused())) {
/* 112 */         this.comboBox.setFocused(true);
/* 113 */         this.password.setFocused(false);
/* 114 */         this.username.setFocused(false);
/* 115 */       } else if (this.comboBox.isFocused()) {
/* 116 */         this.username.setFocused(true);
/* 117 */         this.comboBox.setFocused(false);
/*     */       }
/*     */     }
/* 120 */     if (par1 == '\r') {
/* 121 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/* 123 */     this.username.textboxKeyTyped(par1, par2);
/* 124 */     this.password.textboxKeyTyped(par1, par2);
/* 125 */     this.comboBox.textboxKeyTyped(par1, par2);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3)
/*     */   {
/*     */     try {
/* 131 */       super.mouseClicked(par1, par2, par3);
/*     */     } catch (IOException e) {
/* 133 */       e.printStackTrace();
/*     */     }
/* 135 */     this.username.mouseClicked(par1, par2, par3);
/* 136 */     this.password.mouseClicked(par1, par2, par3);
/* 137 */     this.comboBox.mouseClicked(par1, par2, par3);
/*     */   }
/*     */   
/*     */   static void setStatus(String status) {
/* 141 */     status = status;
/*     */   }
/*     */   
/*     */   private class AddAltThread extends Thread {
/*     */     private String password;
/*     */     private String username;
/*     */     
/*     */     public AddAltThread(String username, String password) {
/* 149 */       this.username = username;
/* 150 */       this.password = password;
/* 151 */       GuiAddAlt.setStatus(EnumChatFormatting.GRAY + "Idle...");
/*     */     }
/*     */     
/*     */     private void checkAndAddAlt(String username, String password) throws IOException {
/* 155 */       YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 156 */       YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service
/* 157 */         .createUserAuthentication(Agent.MINECRAFT);
/* 158 */       auth.setUsername(username);
/* 159 */       auth.setPassword(password);
/*     */       try {
/* 161 */         auth.logIn();
/* 162 */         AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName()));
/* 163 */         GuiAddAlt.this.save();
/* 164 */         GuiAddAlt.setStatus("Alt added. (" + username + ")");
/*     */       } catch (AuthenticationException e) {
/* 166 */         GuiAddAlt.setStatus(EnumChatFormatting.RED + "Alt failed!");
/* 167 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 173 */       if (this.password.equals("")) {
/* 174 */         AltManager.registry.add(new Alt(this.username, ""));
/* 175 */         GuiAddAlt.setStatus(EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
/* 176 */         return;
/*     */       }
/* 178 */       GuiAddAlt.setStatus(EnumChatFormatting.YELLOW + "Trying alt...");
/*     */       try {
/* 180 */         checkAndAddAlt(this.username, this.password);
/*     */       } catch (IOException e) {
/* 182 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void save() throws IOException {
/* 188 */     PrintWriter alts = new PrintWriter(new FileWriter(ALTS_DIR));
/* 189 */     for (Alt alt : AltManager.registry) {
/* 190 */       if (alt.getMask().equals("")) {
/* 191 */         alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword());
/*     */       }
/*     */       else {
/* 194 */         alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword() + ":" + alt.getMask());
/*     */       }
/*     */     }
/* 197 */     alts.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\GuiAddAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */