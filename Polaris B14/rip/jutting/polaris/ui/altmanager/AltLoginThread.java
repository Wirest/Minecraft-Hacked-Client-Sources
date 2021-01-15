/*    */ package rip.jutting.polaris.ui.altmanager;
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
/*    */ 
/*    */ public class AltLoginThread
/*    */   extends Thread
/*    */ {
/*    */   private final String password;
/*    */   private String status;
/*    */   private final String username;
/*    */   private Minecraft mc;
/*    */   
/*    */   public AltLoginThread(String username, String password)
/*    */   {
/* 25 */     super("Alt Login Thread");
/* 26 */     this.mc = Minecraft.getMinecraft();
/* 27 */     this.username = username;
/* 28 */     this.password = password;
/* 29 */     this.status = (EnumChatFormatting.GRAY + "Waiting...");
/*    */   }
/*    */   
/*    */   private Session createSession(String username, String password) {
/* 33 */     YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 34 */     YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service
/* 35 */       .createUserAuthentication(Agent.MINECRAFT);
/* 36 */     auth.setUsername(username);
/* 37 */     auth.setPassword(password);
/*    */     try {
/* 39 */       auth.logIn();
/* 40 */       return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), 
/* 41 */         auth.getAuthenticatedToken(), "mojang");
/*    */     } catch (AuthenticationException localAuthenticationException) {
/* 43 */       localAuthenticationException.printStackTrace(); }
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public String getStatus()
/*    */   {
/* 49 */     return this.status;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 54 */     if (this.password.equals("")) {
/* 55 */       this.mc.session = new Session(this.username, "", "", "mojang");
/* 56 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline name)");
/*    */     }
/* 58 */     this.status = (EnumChatFormatting.YELLOW + "Logging in...");
/* 59 */     Session auth = createSession(this.username, this.password);
/* 60 */     if (auth == null) {
/* 61 */       this.status = (EnumChatFormatting.RED + "Login failed!");
/*    */     } else {
/* 63 */       AltManager.lastAlt = new Alt(this.username, this.password);
/* 64 */       this.status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
/* 65 */       this.mc.session = auth;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 70 */     this.status = status;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */