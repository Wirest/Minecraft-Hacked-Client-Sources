package cedo;

import cedo.command.CommandManager;
import cedo.config.ConfigManager;
import cedo.events.Event;
import cedo.events.listeners.EventChat;
import cedo.events.listeners.EventKey;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.modules.Module.Category;
import cedo.modules.combat.*;
import cedo.modules.customize.*;
import cedo.modules.exploit.Antibot;
import cedo.modules.exploit.FastPlace;
import cedo.modules.exploit.SigmaDelete;
import cedo.modules.hidden.Disabler;
import cedo.modules.movement.*;
import cedo.modules.player.*;
import cedo.modules.render.*;
import cedo.ui.HUD;
import cedo.ui.notifications.Notification;
import cedo.ui.notifications.NotificationManager;
import cedo.ui.notifications.NotificationType;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.time.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("rawtypes")

public class Fan {
    private static final Timer inventoryTimer = new Timer();
    public static String name = "Fan", version = "v4";
    public static String fullname = name + " " + version;
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static Fly fly;
    public static Reach reach;
    public static Chams chams;
    public static TargetStrafe targetStrafe;
    public static HUD hud = new HUD();
    public static ClickGUI clickGui;
    public static ESP esp;
    public static HudModule hudMod;
    public static Speed speed;
    public static Scaffold scaffold;
    public static Arraylist arraylist;
    public static TargetHUD targetHud;
    public static Animations effects;
    public static Killaura killaura;
    public static ChestESP chestesp;
    public static NotificationsMod notificationsMod;
    public static InventoryManager inventoryManager;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static TabGUI tabgui;
    public static File fanDir = new File(mc.mcDataDir, "Fan");
    public static TextureManager ctm;
    public static CommandManager commandmanager = new CommandManager();
    public static String backgroundLocation = "Fan/Background.jpg";
    public static NotificationManager notificationManager = new NotificationManager();
    public static Disabler disabler;
    public static ConfigManager cfgManager = new ConfigManager();
    public static Statistics statistics;
    public static ItemCustomization itemCustomization;

    public static List<Module> getModulesByCategory(Category c) {
        List<Module> modules = new ArrayList<>();

        for (Module m : cedo.Fan.modules) {
            if (m.getCategory() == c) {
                modules.add(m);
            }
        }

        return modules;
    }

    public static MinecraftFontRenderer getClientFont(String size, boolean bold) {
        MinecraftFontRenderer font = null;
        if (bold) {
            switch (size) {
                case "Medium":
                    switch (hudMod.font.getSelected()) {

                        case "Fan":
                            font = FontUtil.fanboldmedium;
                            break;

                        case "Moon":
                            font = FontUtil.moonfontboldmedium;
                            break;
                    }
                    break;
            }
        } else
            switch (size) {
                case "Medium":
                    switch (hudMod.font.getSelected()) {

                        case "Fan":
                            font = FontUtil.cleanmedium;
                            break;

                        case "Moon":
                            font = FontUtil.moonfontmedium;
                            break;
                    }
                    break;

                case "Small":
                    switch (hudMod.font.getSelected()) {

                        case "Fan":
                            font = FontUtil.cleanSmall;
                            break;

                        case "Moon":
                            font = FontUtil.moonfontsmall;
                            break;
                    }
                    break;

                case "Regular":
                    switch (hudMod.font.getSelected()) {

                        case "Fan":
                            font = FontUtil.clean;
                            break;

                        case "Moon":
                            font = FontUtil.moonfontregular;
                            break;
                    }
                    break;

                case "Big":
                    switch (hudMod.font.getSelected()){
                        case "Fan":
                            font = FontUtil.cleankindalarge;
                            break;

                        case "Moon":
                            font = FontUtil.moonfontbig;
                            break;
                    }
            }
        return font;
    }

    public static <T> T getModule(Class<T> module) {
        for (Module m : modules) {
            if (m.getClass().equals(module)) {
                return (T) m;
            }
        }

        return null;
    }

    public static Module getModuleByName(String string) {
        for (Module m : cedo.Fan.modules) {
            if (m.getName().equalsIgnoreCase(string)) {
                return m;
            }
        }

        return null;
    }

    public static void keyPress(int key) {
        onEvent(new EventKey(key));

        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }

    public static boolean invCooldownElapsed(long time) {
        return inventoryTimer.hasTimeElapsed(time, true);
    }

    public static void onEvent(Event e) {
        if (e instanceof EventPacket && e.isIncoming()) {
            if (((EventPacket) e).getPacket() instanceof S08PacketPlayerPosLook) {
                //disablerTimer.reset(); //Inside modules: if (e instanceof EventUpdate && Fan.disablerTimer.elapsed(1000, false))
                for (Module m : modules) {
                    if (m.disableOnLagback) {
                        if (m.isEnabled()) {
                            m.toggleSilent(); //Comment this out
                            //Notification.post(NotificationType.WARNING, "Lagback", m.getName() + " was " + "\247cdisabled\247r due to lagbacks.");
                            Notification.post(NotificationType.WARNING, "Lagback", m.getName() + " was " + "\247cdisabled\247r due to lagbacks.");
                        }
                    }
                }
            }
        }

        if (!Wrapper.authorized)
            return;

        if (e instanceof EventChat || e instanceof EventKey)
            commandmanager.onEvent(e);

        if (e instanceof EventRenderGUI)
            notificationManager.drawScreen(((EventRenderGUI) e).sr);

        if (Disabler.bypasses && mc.thePlayer != null)
            disabler.onEvent(e);

        for (Module m : modules) {
            m.keyCode = m.getKeyBind().getCode();

            if (!m.isToggled() || mc.thePlayer == null) {
                continue;
            }

            m.onEvent(e);
        }
    }

    public static void startup() {
        //new Thread(() -> Auth.validateUser()).start();

      /*  try {
            String unhashed = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash;
            md.update(unhashed.getBytes(StandardCharsets.ISO_8859_1), 0, unhashed.length());
            sha1hash = md.digest();

            StringBuilder buf = new StringBuilder();
            int i = 0;
            while (i < sha1hash.length) {
                int halfbyte = sha1hash[i] >>> 4 & 15;
                int two_halfs = 0;
                do {
                    if (halfbyte <= 9) {
                        buf.append((char) (48 + halfbyte));
                    } else {
                        buf.append((char) (97 + (halfbyte - 10)));
                    }
                    halfbyte = sha1hash[i] & 15;
                } while (two_halfs++ < 1);
                ++i;
            }

            hwid = buf.toString();
            StringSelection stringSelection = new StringSelection(hwid);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);

            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://intent.store/product/28/whitelist?hwid=" + hwid).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            ArrayList<String> response = new ArrayList<>();
            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }
            if (!response.contains("true") || response.contains("false")) {
                Minecraft.getMinecraft().entityRenderer = null;
                Minecraft.getMinecraft().gameSettings = null;
                Fan.hud = null;
                Wrapper.authorized = false;
                Wrapper.getFuckedPrinceKin = 1438E-4;
                return;
            } else {
                Wrapper.authorized = true;
                Wrapper.getFuckedPrinceKin = 423499;
            }
        } catch (Exception e) {
            Minecraft.getMinecraft().entityRenderer = null;
            Minecraft.getMinecraft().gameSettings = null;
            Fan.hud = null;
            Wrapper.authorized = false;
            Wrapper.getFuckedPrinceKin = 1438E-4;
            return;
        }*/

        Wrapper.authorized = true;
        Wrapper.getFuckedPrinceKin = 423499;

        Display.setTitle(fullname);
        SplashScreen.setProgress(2, 2, "Loading Modules");
        disabler = new Disabler();
        // modules.add(disabler = new Disabler());
        modules.add(fly = new Fly());
        modules.add(new AutoPot());
        modules.add(new LongJump());
        modules.add(new Sprint());
        modules.add(new FullBright());
        modules.add(new NoFall());
        modules.add(tabgui = new TabGUI());
        modules.add(killaura = new Killaura());
        modules.add(clickGui = new ClickGUI());
        modules.add(speed = new Speed());
        modules.add(new FastPlace());
        modules.add(new Nametags());
        modules.add(esp = new ESP());
        modules.add(inventoryManager = new InventoryManager());
        modules.add(chams = new Chams());
        modules.add(new NoBob());
        modules.add(hudMod = new HudModule());
        modules.add(new TimerMod());
        modules.add(new NoSlow());
        modules.add(scaffold = new Scaffold());
        modules.add(new AimAssist());
        modules.add(new AutoClicker());
        modules.add(new AntiKB());
        //modules.add(new PingSpoof());
        modules.add(new AutoHypixel());
        modules.add(new Antibot());
        //modules.add(new FastLadder());
        modules.add(new FastStairs());
        modules.add(new ChestAssist());
        modules.add(new AntiServer());
        modules.add(new ChestStealer());
        modules.add(new AutoArmor());
        modules.add(new AntiVoid());
        modules.add(effects = new Animations());
        modules.add(new InventoryMove());
        modules.add(new Step());
        modules.add(new Jesus());
        //modules.add(new LongJump());
        modules.add(new Criticals());
        // modules.add(new Tracers());
        // modules.add(new BadScaffold());
        //modules.add(new DevTest());
        modules.add(arraylist = new Arraylist());
        modules.add(notificationsMod = new NotificationsMod());
        // modules.add(new AutoJump());
        modules.add(targetHud = new TargetHUD());
        modules.add(new SigmaDelete());
        modules.add(new Phase());
        modules.add(chestesp = new ChestESP());
        modules.add(new esp2d());
        //modules.add(new Trajectories());
        modules.add(new WorldTime());
        modules.add(new Skeletons());
        modules.add(new Keystrokes2());
        modules.add(targetStrafe = new TargetStrafe());
        modules.add(new ModuleIndicator());
        modules.add(statistics = new Statistics());
        modules.add(reach = new Reach());
        modules.add(itemCustomization = new ItemCustomization());
        modules.add(new BedBreaker());

        if (cfgManager.config.exists()) {
            cfgManager.loadConfig();
        }

        Thread configDaemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cfgManager.saveConfig();
            }
        });
        configDaemon.setDaemon(true);
        configDaemon.start();
    }

    public static boolean shouldDisable() {
        return modules.stream().anyMatch(m -> m.requiresDisabler && m.isEnabled());
    }

    public static String getBackgroundLocation() {
        if (Fan.hudMod.backgroundMode.is("Fan")) {
            Fan.backgroundLocation = "Fan/Background.jpg";
        } else if (Fan.hudMod.backgroundMode.is("ZeroTwo1")) {
            Fan.backgroundLocation = "Fan/ZeroTwo.jpeg";
        } else if (Fan.hudMod.backgroundMode.is("ZeroTwo2")) {
            Fan.backgroundLocation = "Fan/basicbackground.png";
        }
        Fan.backgroundLocation = "Fan/ChristmasBG.png";
        return Fan.backgroundLocation;
    }
}
