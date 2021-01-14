package modification.main;

import i.dupx.launcher.CLAPI;
import modification.managers.*;
import modification.utilities.*;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;

public final class Modification {
    public static final boolean DEV_MODE = true;
    public static final String[] AUTHORS = {"Jannick", "Luca"};
    public static final String NAME = "Eject";
    public static final String VERSION = "v3.5.3";
    public static final File DIRECTORY = new File(System.getenv("APPDATA"), "Eject");
    public static final File CONFIG_DIRECTORY = new File(DIRECTORY, "configurations");
    public static final AccountManager ACCOUNT_MANAGER = new AccountManager();
    public static final FileManager FILE_MANAGER = new FileManager();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static final EventManager EVENT_MANAGER = new EventManager();
    public static final ValueManager VALUE_MANAGER = new ValueManager();
    public static final CSGOGuiManager CSGO_GUI_MANAGER = new CSGOGuiManager();
    public static final ClickGuiManager CLICK_GUI_MANAGER = new ClickGuiManager();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static final FriendManager FRIEND_MANAGER = new FriendManager();
    public static final LogUtil LOG_UTIL = new LogUtil();
    public static final RenderUtil RENDER_UTIL = new RenderUtil();
    public static final SlideUtil SLIDE_UTIL = new SlideUtil();
    public static final ScissorUtil SCISSOR_UTIL = new ScissorUtil();
    public static final RotationUtil ROTATION_UTIL = new RotationUtil();
    public static final ItemUtil ITEM_UTIL = new ItemUtil();
    public static final RayTraceUtil RAY_TRACE_UTIL = new RayTraceUtil();
    public static final EntityUtil ENTITY_UTIL = new EntityUtil();
    public static final PrixUtil PRIX_UTIL = new PrixUtil();
    public static final IRCUtil IRC_UTIL = new IRCUtil();
    public static final DecryptionUtil DECRYPTION_UTIL = new DecryptionUtil();
    public static String user;
    public static String prixAccount;
    public static String rank;
    public static String apiToken;
    public static Color color;

    public final void initialize() {
        prixAccount = "";
        color = Color.CYAN;
        user = CLAPI.getCLUsername();
        checkRanks();
        Display.setTitle("Injecting modification...");
        if ((!DIRECTORY.exists()) && (DIRECTORY.mkdir())) {
            LOG_UTIL.sendConsoleMessage("Created directory successfully");
        }
        FILE_MANAGER.initialize();
        MODULE_MANAGER.initialize();
        CLICK_GUI_MANAGER.initialize();
        FILE_MANAGER.checkFiles();
        ACCOUNT_MANAGER.initialize();
        COMMAND_MANAGER.initialize();
        VALUE_MANAGER.initialize();
        CONFIG_MANAGER.initialize();
    }

    private void checkRanks() {
        switch (user) {
            case "Spone":
            case "Sponeee":
                rank = "Admin";
                break;
            case "Dream9919":
                rank = "Pro";
                break;
            case "Jannickel":
            case "Jannick1337":
                rank = "Dev";
                break;
            case "Serum":
                rank = "Friend";
                break;
            case "Dardy187":
                rank = "Staff";
                break;
            case "cladminhaze":
                rank = "Hase";
                break;
            default:
                rank = "User";
        }
    }
}




