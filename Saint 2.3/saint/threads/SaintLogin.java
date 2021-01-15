package saint.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import saint.Saint;
import saint.protection.Account;
import saint.screens.GuiSaintLogin;
import saint.utilities.MathUtils;

public class SaintLogin extends Thread {
   private final Minecraft mc;
   private final String username;
   private final String password;
   private String state;

   public SaintLogin(String username, String password) {
      super("" + MathUtils.getRandom(999999999));
      this.mc = Minecraft.getMinecraft();
      this.username = username;
      this.password = password;
      this.state = "§cYou must log in in order to use Saint";
   }

   public SaintLogin(Account account) {
      this(account.getUsername(), account.getPassword());
   }

   private Account createAccount() {
      try {
         URL url = new URL("http://saintclient.netii.net/check.php?user=" + this.username + "&pass=" + this.password);
         InetAddress ip = InetAddress.getByName(url.getHost());
         if (!ip.getHostAddress().equals("31.170.160.149")) {
            return null;
         } else {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            return Boolean.parseBoolean(line) ? new Account(this.username, this.password) : null;
         }
      } catch (MalformedURLException var6) {
         return null;
      } catch (UnknownHostException var7) {
         return null;
      } catch (IOException var8) {
         return null;
      }
   }

   public void run() {
      this.state = "§eLogging in...";
      Account account = this.createAccount();
      if (account == null) {
         this.state = "§cLogin failed!";
         if (!(this.mc.currentScreen instanceof GuiSaintLogin)) {
            this.mc.displayGuiScreen(new GuiSaintLogin((Account)null));
         }
      } else {
         Saint.setAccount(account);
         this.state = "§aLogged in.";
         Saint.getFileManager().getFileUsingName("saintaccount").saveFile();
      }

   }

   public String getStatee() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }
}
