/*    */ package rip.jutting.polaris.ui.login;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ import java.net.Proxy;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.Session;
/*    */ 
/*    */ public final class AltLoginThread extends Thread
/*    */ {
/*    */   private final String password;
/*    */   private String status;
/*    */   private final String username;
/* 19 */   private Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public AltLoginThread(String username, String password) {
/* 22 */     super("Alt Login Thread");
/* 23 */     this.username = username;
/* 24 */     this.password = password;
/* 25 */     this.status = (EnumChatFormatting.GRAY + "Waiting...");
/*    */   }
/*    */   
/*    */   private Session createSession(String username, String password) {
/* 29 */     YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 30 */     YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
/* 31 */     auth.setUsername(username);
/* 32 */     auth.setPassword(password);
/*    */     try {
/* 34 */       auth.logIn();
/* 35 */       return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
/*    */     }
/*    */     catch (AuthenticationException localAuthenticationException) {
/* 38 */       localAuthenticationException.printStackTrace(); }
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public String getStatus()
/*    */   {
/* 44 */     return this.status;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 49 */     if (this.password.equals("")) {
/* 50 */       this.mc.session = new Session(this.username, "", "", "mojang");
/* 51 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline name)");
/* 52 */       return;
/*    */     }
/* 54 */     this.status = (EnumChatFormatting.YELLOW + "Logging in...");
/* 55 */     Session auth = createSession(this.username, this.password);
/* 56 */     if (auth == null) {
/* 57 */       this.status = (EnumChatFormatting.RED + "Login failed!");
/*    */     } else {
/* 59 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
/* 60 */       this.mc.session = auth;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 65 */     this.status = status;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\login\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */