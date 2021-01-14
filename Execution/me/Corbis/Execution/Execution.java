package me.Corbis.Execution;


import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.response.Response;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;

import de.Hero.settings.SettingsManager;
import me.Corbis.Execution.Command.CommandManager;
import me.Corbis.Execution.Configuration.SaveLoad;
import me.Corbis.Execution.Music.MusicManager;
import me.Corbis.Execution.Replay.Replay;
import me.Corbis.Execution.Replay.ReplayManager;
import me.Corbis.Execution.event.EventManager;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventKey;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.module.ModuleManager;
import me.Corbis.Execution.ui.AltManager.FileManager;
import me.Corbis.Execution.ui.AltManager.GuiAltManager;
import me.Corbis.Execution.ui.BanChecker;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationManager;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.ui.SoundVisualiser.Visualizer;
import me.Corbis.Execution.ui.TabGui.TabGui;
import me.Corbis.Execution.utils.PlayMusic;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Music;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Execution {
    public String license = "null", username = "null";
    public boolean authenticated = false;
    public String name = "Execution";
    public String version = "2.0 Recode";
    public static String APIKey = "";
    public static Execution instance = new Execution();
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public SettingsManager settingsManager;
    public FileManager fileManager;
    public SaveLoad saveLoad;
    public GuiAltManager guiAltLogin;
    public Visualizer soundVisualiser;
    public CommandManager commandManager;
    public TabGui tabGui;
    public static TheAlteningAuthentication auth;
    public BanChecker banChecker;
    public NotificationManager notificationManager;
    public MusicManager musicManager;
    public PlayMusic soundHandler;
    public boolean destroyed=  false;
    public boolean isReplay = false;
    public boolean isRecording = false;
    public ReplayManager replayManager;

    public void startClient() {

        if(check()) {
            this.settingsManager = new SettingsManager();
            eventManager = new EventManager();
            this.replayManager = new ReplayManager();
            isReplay = false;
            isRecording = false;
            moduleManager = new ModuleManager();
            Display.setTitle(name + " " + version);
            eventManager.register(this);
            this.fileManager = new FileManager();
            this.saveLoad = new SaveLoad();
            this.guiAltLogin = new GuiAltManager();
            this.soundVisualiser = new Visualizer();
            this.commandManager = new CommandManager();
            this.banChecker = new BanChecker();
            auth = new TheAlteningAuthentication(AlteningServiceType.MOJANG);
            tabGui = new TabGui();
            notificationManager = new NotificationManager();
            musicManager = new MusicManager();
            soundHandler = new PlayMusic();
        }


    }

    public void stopClient(){
        File directory = new File(Minecraft.getMinecraft().mcDataDir, "Execution");
        File lisence = new File(directory, "license.txt");
        if(lisence.exists()){

            if(lisence.delete()){
                System.out.println("a");
            }

        }
    }


    public static void switchService(AlteningServiceType type) {
        auth = new TheAlteningAuthentication(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static Execution getInstance() {
        return instance;
    }

    public static void setInstance(Execution instance) {
        Execution.instance = instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public SaveLoad getSaveLoad() {
        return saveLoad;
    }

    public void setSaveLoad(SaveLoad saveLoad) {
        this.saveLoad = saveLoad;
    }

    public void addChatMessage(String s) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6[§c!§6] (Execution) §a" + s));
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (Module m : moduleManager.getModules()) {
            if (m.getKey() == event.getKey()) {
                m.toggle();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(isReplay){
            replayManager.playReplay();
        }else if(isRecording){
            Replay current = replayManager.replays.get(0);
            current.recordSession(Minecraft.getMinecraft().theWorld,Minecraft.getMinecraft().thePlayer);
        }
    }


    public boolean check()  {
        String DEV_KEY = "IUBPwJOWpnoS4OzJx2yMm0b6lP0nkohR";
        final PastebinFactory factory = new PastebinFactory();
        final Pastebin pastebin = factory.createPastebin(DEV_KEY);
        final String pasteKey = "VjcEU8kf";
        final Response<String> pasteResponse = pastebin.getRawPaste(pasteKey);
        if (pasteResponse.hasError()) {
            System.out.println("Unable to read paste content!");
            return false;
        }

        String response = pasteResponse.get();
        try {
            System.out.println(getHwid());
            for (String s : response.split(":")) {
                if (s.equals(getHwid())) {
                    return true;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public static String getHwid() throws Exception {
        String hwid = SHA1(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        return hwid;
    }

    private static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 15;
            int var4 = 0;

            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }

                halfbyte = data[i] & 15;
            } while(var4++ < 1);
        }

        return buf.toString();
    }





}
