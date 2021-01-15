/*     */ package rip.jutting.polaris.ui.newconfig;
/*     */ 
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.ui.login.PasswordField;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiAddAlt
/*     */   extends GuiScreen
/*     */ {
/*     */   private final GuiAltManager manager;
/*     */   private PasswordField password;
/*     */   private static String status;
/*     */   private GuiTextField username;
/*     */   
/*     */   public GuiAddAlt(GuiAltManager manager)
/*     */   {
/*  31 */     status = EnumChatFormatting.GRAY + "Idle...";
/*  32 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  37 */     switch (button.id) {
/*     */     case 0: 
/*  39 */       AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
/*  40 */       login.start();
/*  41 */       break;
/*     */     
/*     */     case 1: 
/*  44 */       this.mc.displayGuiScreen(this.manager);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawScreen(int i, int j, float f)
/*     */   {
/*  52 */     drawCoolBackground();
/*  53 */     this.username.drawTextBox();
/*  54 */     this.password.drawTextBox();
/*  55 */     drawCenteredString(this.fontRendererObj, "Add Alt", width / 2, 20, -1);
/*  56 */     if (this.username.getText().isEmpty()) {
/*  57 */       drawString(this.mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
/*     */     }
/*  59 */     if (this.password.getText().isEmpty()) {
/*  60 */       drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
/*     */     }
/*  62 */     drawCenteredString(this.fontRendererObj, status, width / 2, 30, -1);
/*  63 */     super.drawScreen(i, j, f);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/*  68 */     Keyboard.enableRepeatEvents(true);
/*  69 */     this.buttonList.clear();
/*  70 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
/*  71 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
/*  72 */     this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  73 */     this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char par1, int par2)
/*     */   {
/*  78 */     this.username.textboxKeyTyped(par1, par2);
/*  79 */     this.password.textboxKeyTyped(par1, par2);
/*  80 */     if ((par1 == '\t') && ((this.username.isFocused()) || (this.password.isFocused()))) {
/*  81 */       this.username.setFocused(!this.username.isFocused());
/*  82 */       this.password.setFocused(!this.password.isFocused());
/*     */     }
/*  84 */     if (par1 == '\r') {
/*  85 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3)
/*     */   {
/*     */     try {
/*  92 */       super.mouseClicked(par1, par2, par3);
/*     */     }
/*     */     catch (IOException e) {
/*  95 */       e.printStackTrace();
/*     */     }
/*  97 */     this.username.mouseClicked(par1, par2, par3);
/*  98 */     this.password.mouseClicked(par1, par2, par3);
/*     */   }
/*     */   
/*     */   public static void access$0(String status) {
/* 102 */     status = status;
/*     */   }
/*     */   
/*     */   private class AddAltThread extends Thread
/*     */   {
/*     */     private final String password;
/*     */     private final String username;
/*     */     
/*     */     public AddAltThread(String username, String password) {
/* 111 */       this.username = username;
/* 112 */       this.password = password;
/* 113 */       GuiAddAlt.access$0(EnumChatFormatting.GRAY + "Idle...");
/*     */     }
/*     */     
/*     */     private final void checkAndAddAlt(String username, String password) throws IOException {
/* 117 */       YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 118 */       YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
/* 119 */       auth.setUsername(username);
/* 120 */       auth.setPassword(password);
/*     */       try {
/* 122 */         auth.logIn();
/* 123 */         AltManager.registry.add(new Alt(username, password));
/* 124 */         GuiAddAlt.access$0("Alt added. (" + username + ")");
/*     */       }
/*     */       catch (AuthenticationException e) {
/* 127 */         GuiAddAlt.access$0(EnumChatFormatting.RED + "Alt failed!");
/* 128 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 134 */       if (this.password.equals("")) {
/* 135 */         AltManager.registry.add(new Alt(this.username, ""));
/* 136 */         GuiAddAlt.access$0(EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
/* 137 */         return;
/*     */       }
/* 139 */       GuiAddAlt.access$0(EnumChatFormatting.YELLOW + "Trying alt...");
/*     */       try {
/* 141 */         checkAndAddAlt(this.username, this.password);
/*     */       }
/*     */       catch (IOException e) {
/* 144 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\GuiAddAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */