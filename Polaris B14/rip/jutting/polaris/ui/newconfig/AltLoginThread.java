/*    */ package rip.jutting.polaris.ui.newconfig;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.Session;
/*    */ 
/*    */ public final class AltLoginThread extends Thread
/*    */ {
/*    */   private final String password;
/*    */   private String status;
/*    */   private final String username;
/*    */   private Minecraft mc;
/*    */   
/*    */   public AltLoginThread(String username, String password)
/*    */   {
/* 20 */     super("Alt Login Thread");
/* 21 */     this.mc = Minecraft.getMinecraft();
/* 22 */     this.username = username;
/* 23 */     this.password = password;
/* 24 */     this.status = (EnumChatFormatting.GRAY + "Waiting...");
/*    */   }
/*    */   
/*    */   private Session createSession(String username, String password) {
/* 28 */     YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(java.net.Proxy.NO_PROXY, "");
/* 29 */     YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(com.mojang.authlib.Agent.MINECRAFT);
/* 30 */     auth.setUsername(username);
/* 31 */     auth.setPassword(password);
/*    */     try {
/* 33 */       auth.logIn();
/* 34 */       return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
/*    */     }
/*    */     catch (AuthenticationException localAuthenticationException) {
/* 37 */       localAuthenticationException.printStackTrace(); }
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public String getStatus()
/*    */   {
/* 43 */     return this.status;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 48 */     if (this.password.equals("")) {
/* 49 */       this.mc.session = new Session(this.username, "", "", "mojang");
/* 50 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline name)");
/* 51 */       return;
/*    */     }
/* 53 */     this.status = (EnumChatFormatting.YELLOW + "Logging in...");
/* 54 */     Session auth = createSession(this.username, this.password);
/* 55 */     if (auth == null) {
/* 56 */       this.status = (EnumChatFormatting.RED + "Login failed!");
/*    */     }
/*    */     else {
/* 59 */       AltManager.lastAlt = new Alt(this.username, this.password);
/* 60 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
/* 61 */       this.mc.session = auth;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 66 */     this.status = status;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */