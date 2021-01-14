package info.sigmaclient;

import info.sigmaclient.gui.altmanager.FileManager;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.gui.screen.GuiInvManager;
import info.sigmaclient.gui.screen.impl.mainmenu.ClientMainMenu;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.FontManager;
import info.sigmaclient.management.command.CommandManager;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.management.upgrade.UpgradeManager;
import info.sigmaclient.management.users.TTSystem;
import info.sigmaclient.management.users.UserManager;
import info.sigmaclient.management.waypoints.WaypointManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Client {
    public static Client instance;

    //Credits: LPK, Tojatta, Aristhena, OG

    // Client data
    public static String author = "Arithmo & Omikron";
    public static String version = "v3.9";
    public static String clientName = "Sigma";
    public static ArrayList<String> changelog = new ArrayList<>();
    public static ColorManager cm = new ColorManager();
    public static UserManager um;
    public static UpgradeManager upgradeManager;

    public static boolean isLowEndPC;
    public static boolean hasSetup;
    public static final int VERSION_CHECK = 3;

    // Managers
    private final ModuleManager moduleManager;

    private static FileManager fileManager;
    public static ClickGui clickGui;

    public static FileManager getFileManager() {
        return fileManager;
    }

    public static CommandManager commandManager;

    // Other data
    private File dataDirectory;
    private GuiScreen mainMenu = new ClientMainMenu();
    private boolean isHidden;

    public static FontManager fm = new FontManager();
    public static WaypointManager wm = new WaypointManager();

    /**
     * TODO: - Work on vanilla hidden mode - Work on making the renderers (help
     * UI vs frame UI) use the same class - Work on MP3 player (See above point)
     * - Add more themes
     **/

    public Client() throws Exception {
        Client.instance = this;
        commandManager = new CommandManager();
        moduleManager = new ModuleManager(Module.class);
        um = new UserManager();
        FriendManager.start();
        GuiInvManager.loadConfig();
    }

    public static boolean outdated;

    public static ClickGui getClickGui() {
        return clickGui;
    }

    public static void onCrash(String completeReport) {
        String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + um.getVersionString() + um.bytesToHex(TTSystem.getInstance().getTT()) + "/crash";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("USN", um.getUserSerialNumber());
            connection.setRequestProperty("Session", um.getSession());
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //writer.write("text=" + URLEncoder.encode(completeReport, "UTF-8"));
            writer.write(new String(Base64.encodeBase64(completeReport.getBytes("UTF-8"))).replaceAll("\n", ""));
            writer.flush();
            connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        commandManager.setup();
        dataDirectory = new File(Client.clientName);
        moduleManager.setup();
        (Client.fileManager = new FileManager()).loadFiles();
        clickGui = new ClickGui();
        Module.loadStatus();
        Module.loadSettings();
    }

    public static ModuleManager<Module> getModuleManager() {
        return instance.moduleManager;
    }

    public static File getDataDir() {
        return instance.dataDirectory;
    }

    public static boolean isHidden() {
        return instance.isHidden;
    }

    public static void setHidden(boolean hidden) {
        instance.isHidden = hidden;
        if (hidden) {
            instance.mainMenu = new GuiMainMenu();
        } else {
            instance.mainMenu = new ClientMainMenu();
        }
    }

}
