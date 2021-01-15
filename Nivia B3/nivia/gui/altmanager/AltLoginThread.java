package nivia.gui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import nivia.Pandora;

;

public final class AltLoginThread extends Thread
{
  private final String password;
  private String status;
  private final String username;
  private Minecraft mc = Minecraft.getMinecraft();
  
  public AltLoginThread(String username, String password)
  {
    super("Alt Login Thread");
    this.username = username;
    this.password = password;
    status = (EnumChatFormatting.GRAY + "Waiting...");
  }
  
  private Session createSession(String username, String password)
  {
    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(java.net.Proxy.NO_PROXY, "");
    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
    auth.setUsername(username);
    auth.setPassword(password);
    try
    {
      auth.logIn();
      
      return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    } catch (AuthenticationException localAuthenticationException) { localAuthenticationException.printStackTrace();}
    return null;
  }
  
  public String getStatus()
  {
    return status;
  }
  
  public void run()
  {
    if (password.equals(""))
    {
      mc.session = new Session(username, "", "", "mojang");
      status = (EnumChatFormatting.GREEN + "Logged in. (" + username + " - offline name)");
      return;
    }
    status = (EnumChatFormatting.AQUA + "Logging in...");
    Session auth = createSession(username, password);
    if (auth == null)
    {
      status = (EnumChatFormatting.RED + "Login failed!");
    }
    else
    {
      Pandora.getAltManager().lastAlt = (new Alt(username, password));
      status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
      mc.session = auth;
    }
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
}
