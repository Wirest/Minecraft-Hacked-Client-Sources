package me.existdev.exist.gui.account;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import me.existdev.exist.Exist;
import me.existdev.exist.file.files.AltFile;
import me.existdev.exist.gui.account.Alt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltLoginThread extends Thread {
   // $FF: synthetic field
   private final Minecraft mc = Minecraft.getMinecraft();
   // $FF: synthetic field
   private final String password;
   // $FF: synthetic field
   private String status;
   // $FF: synthetic field
   private final String username;

   // $FF: synthetic method
   public AltLoginThread(String username, String password) {
      super("Alt Login Thread");
      this.username = username;
      this.password = password;
      this.status = "§eWaiting...";
   }

   // $FF: synthetic method
   private final Session createSession(String username, String password) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         auth.logIn();
         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationException var6) {
         return null;
      }
   }

   // $FF: synthetic method
   public String getStatus() {
      return this.status;
   }

   // $FF: synthetic method
   public void run() {
      if(this.password.equals("")) {
         this.mc.session = new Session(this.username, "", "", "mojang");
         this.status = "§aLogged in. (" + this.username + " - offline name)";
      } else {
         this.status = "§1Logging in...";
         Session auth = this.createSession(this.username, this.password);
         if(auth == null) {
            this.status = "§4Login failed!";
         } else {
            Exist.altManager.setLastAlt(new Alt(this.username, this.password));
            AltFile var10000 = Exist.altFile;
            AltFile.saveLastAlt();
            this.status = "§aLogged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
         }

      }
   }

   // $FF: synthetic method
   public void setStatus(String status) {
      this.status = status;
   }
}
