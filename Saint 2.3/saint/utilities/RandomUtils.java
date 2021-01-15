package saint.utilities;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class RandomUtils {
   public static String loginPassword(String username, String password) {
      if (username != null && username.length() >= 0 && password != null && password.length() > 0) {
         YggdrasilAuthenticationService a = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
         YggdrasilUserAuthentication b = (YggdrasilUserAuthentication)a.createUserAuthentication(Agent.MINECRAFT);
         b.setUsername(username);
         b.setPassword(password);

         try {
            b.logIn();
            Minecraft.getMinecraft().session = new Session(b.getSelectedProfile().getName(), b.getSelectedProfile().getId().toString(), b.getAuthenticatedToken(), Agent.MINECRAFT.toString());
         } catch (AuthenticationException var5) {
            var5.printStackTrace();
         }

         return null;
      } else {
         return null;
      }
   }
}
