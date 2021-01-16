package org.m0jang.crystal.Utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.m0jang.crystal.UI.MCLeaks;

public class LoginUtils {
   private static final Logger logger = LogManager.getLogger();

   public static String login(String email, String password) {
      System.out.println("Logging in with [" + email + ":" + password + "]");
      String oldProxAd = System.getProperty("socksProxyHost");
      String oldProxPo = System.getProperty("socksProxyPort");
      System.clearProperty("socksProxyHost");
      System.clearProperty("socksProxyPort");
      YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
      authentication.setUsername(email);
      authentication.setPassword(password);

      try {
         authentication.logIn();
         Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), UserType.MOJANG.getName());
         if (oldProxAd != null) {
            System.setProperty("socksProxyHost", oldProxAd);
         }

         if (oldProxPo != null) {
            System.setProperty("socksProxyPort", oldProxPo);
         }

         MCLeaks.remove();
         return null;
      } catch (AuthenticationUnavailableException var7) {
         return "Cannot contact authentication server!";
      } catch (AuthenticationException var8) {
         return !var8.getMessage().contains("Invalid username or password.") && !var8.getMessage().toLowerCase().contains("account migrated") ? "Cannot contact authentication server!" : "Wrong password!";
      } catch (NullPointerException var9) {
         return "Wrong password!";
      }
   }

   public static void changeCrackedName(String newName) {
      Minecraft.getMinecraft().session = new Session(newName, "", "", UserType.MOJANG.getName());
   }

   public static String removeSpecialChar(String input) {
      return input.replaceAll("\\p{C}", "");
   }
}
