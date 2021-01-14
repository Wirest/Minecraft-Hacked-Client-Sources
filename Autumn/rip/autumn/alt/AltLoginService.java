package rip.autumn.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import rip.autumn.notification.NotificationPublisher;
import rip.autumn.notification.NotificationType;

public final class AltLoginService extends Thread {
   private final String password;
   private final String username;
   private String status;
   private final Minecraft mc = Minecraft.getMinecraft();

   public AltLoginService(String username, String password) {
      super("Alt Login Thread");
      this.username = username;
      this.password = password;
      this.status = EnumChatFormatting.GRAY + "Waiting...";
   }

   private Session createSession(String username, String password) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         auth.logIn();
         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void run() {
      if (this.password.equals("")) {
         this.mc.session = new Session(this.username, "", "", "mojang");
         this.status = EnumChatFormatting.GREEN + "Logged in as " + this.username + ".";
      } else {
         this.status = EnumChatFormatting.GREEN + "Logging in...";
         Session auth = this.createSession(this.username, this.password);
         if (auth == null) {
            NotificationPublisher.queue("Login Failure", "Failed to login to account!", NotificationType.ERROR);
            this.status = EnumChatFormatting.RED + "Failed to login!";
         } else {
            this.status = EnumChatFormatting.GREEN + "Logged in as " + auth.getUsername() + ".";
            NotificationPublisher.queue("Login Success", "Successfully logged in as " + auth.getUsername(), NotificationType.SUCCESS);
            this.mc.session = auth;
         }

      }
   }
}
