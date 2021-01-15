package saint;

import jaco.mp3.player.MP3Player;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import saint.altstuff.AltManager;
import saint.clickgui.ClickGui;
import saint.comandstuff.Command;
import saint.comandstuff.CommandManager;
import saint.eventstuff.EventManager;
import saint.filestuff.FileManager;
import saint.friendstuff.FriendManager;
import saint.irc.SendingThread;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.notificationstuff.NotificationManager;
import saint.protection.Account;
import saint.tabgui.TabGui;
import saint.threads.AutoDestroyerThread;
import saint.threads.NewestVersion;
import saint.utilities.ListenerUtil;
import saint.utilities.Logger;
import saint.valuestuff.ValueManager;
import saint.wallhackstuff.WallhackManager;

public class Saint {
   private static Account account;
   private static boolean newerVersionAvailable;
   private static float version = 2.3F;
   private static String authors = "Andrew";
   private static EventManager eventManager = new EventManager();
   private static ModManager modManager = new ModManager();
   private static final File directory = new File(System.getProperty("user.home"), "Saint");
   private static final FileManager fileManager = new FileManager();
   private static final ValueManager valueManager = new ValueManager();
   private static final FriendManager friendManager = new FriendManager();
   private static final CommandManager commandManager = new CommandManager();
   private static final AltManager altManager = new AltManager();
   private static final NotificationManager notManager = new NotificationManager();
   private static final WallhackManager wallManager = new WallhackManager();
   private static ClickGui clickGui;
   private static SendingThread webUtil;
   private static TabGui tabGUI;
   private static ListenerUtil listenerUtil;
   private static MP3Player player;

   public static Account getAccount() {
      return account;
   }

   public static ListenerUtil getListenerUtil() {
      return listenerUtil;
   }

   public static SendingThread getWebUtil() {
      return webUtil;
   }

   public static File getDirectory() {
      return directory;
   }

   public static AltManager getAltManager() {
      return altManager;
   }

   public static final TabGui getTabGUI() {
      return tabGUI;
   }

   public static final ClickGui getClickGui() {
      return clickGui;
   }

   public static EventManager getEventManager() {
      return eventManager;
   }

   public static WallhackManager getWallManager() {
      return wallManager;
   }

   public static ModManager getModuleManager() {
      return modManager;
   }

   public static NotificationManager getNotificationManager() {
      return notManager;
   }

   public static float getVersion() {
      return version;
   }

   public static FriendManager getFriendManager() {
      return friendManager;
   }

   public static FileManager getFileManager() {
      return fileManager;
   }

   public static ValueManager getValueManager() {
      return valueManager;
   }

   public static CommandManager getCommandManager() {
      return commandManager;
   }

   public static final String getAuthors() {
      return authors;
   }

   public static boolean isLatestVersion() {
      return !newerVersionAvailable;
   }

   public static MP3Player getPlayer() {
      return player;
   }

   public static void setup() {
      Display.setTitle("Minecraft 1.8");
      Logger.writeConsole("Started loading Saint");
      Logger.writeConsole("Version " + version);
      if (!directory.isDirectory()) {
         directory.mkdirs();
      }

      (new NewestVersion()).start();
      (new AutoDestroyerThread()).start();

      try {
         player = new MP3Player(new URL[]{new URL("http://www.andrewthehax0r.xyz/song.mp3")});
      } catch (MalformedURLException var1) {
         var1.printStackTrace();
      }

      eventManager.setup();
      valueManager.setup();
      commandManager.setup();
      modManager.setup();
      friendManager.setup();
      altManager.setup();
      notManager.setup();
      clickGui = new ClickGui();
      tabGUI = new TabGui(Minecraft.getMinecraft());
      fileManager.setup();
      listenerUtil = new ListenerUtil();
      webUtil = new SendingThread();
      webUtil.setSending(true);
      webUtil.start();
      Collections.sort(modManager.getContentList(), new Comparator() {
         public int compare(Module mod1, Module mod2) {
            return mod1.getName().compareTo(mod2.getName());
         }
      });
      Collections.sort(commandManager.getContentList(), new Comparator() {
         public int compare(Command mod1, Command mod2) {
            return mod1.getCommand().compareTo(mod2.getCommand());
         }
      });
      playSound(player);
   }

   public static void setAccount(Account account) {
      Saint.account = account;
   }

   public static void setNewerVersionAvailable(boolean newerVersionAvailable) {
      Saint.newerVersionAvailable = newerVersionAvailable;
   }

   private static void playSound(MP3Player player) {
      if (player != null) {
         player.play();
      }

   }
}
