package com.etb.client;

import java.io.File;

import com.etb.client.command.CommandManager;
import com.etb.client.config.ConfigManager;
import com.etb.client.friend.FriendManager;
import com.etb.client.gui.AntiStrike;
import com.etb.client.gui.alt.account.AccountManager;
import com.etb.client.gui.notification.NotificationManager;
import com.etb.client.module.ModuleManager;
import com.etb.client.utils.thealtening.AltService;
import com.etb.client.utils.thealtening.utilities.SSLVerification;

import net.minecraft.client.Minecraft;

public enum Client {
    INSTANCE;
    private File directory;
    private ModuleManager moduleManager = new ModuleManager();
    private CommandManager commandManager = new CommandManager();
    private SSLVerification sslVerification = new SSLVerification();
    private NotificationManager notificationManager = new NotificationManager();
    private AltService altService = new AltService();
    private FriendManager friendManager;
    private ConfigManager configManager;
    private AccountManager accountManager;
    private AntiStrike antistrike;
    public String clientName = "ETB";
    public double version = 0.1;
    public void start(){
        directory = new File(Minecraft.getMinecraft().mcDataDir,"ETB Reloaded");
        friendManager = new FriendManager(directory);
        friendManager.getFriendSaving().loadFile();
        moduleManager.setDirectory(new File(directory, "modules"));
        moduleManager.initialize();
        moduleManager.loadModules();
        commandManager.initialize();
        configManager = new ConfigManager(new File(directory, "configs"));
        configManager.load();
        sslVerification.verify();
        antistrike = new AntiStrike(directory);
        accountManager = new AccountManager(directory);
        accountManager.getAltSaving().loadFile();
        accountManager.getAltSaving().loadLastAltFile();
        accountManager.getAltSaving().loadAlteningTokenFile();
        System.out.println("client main class loaded.");
    }


    public void end(){
        if (!directory.exists())
            directory.mkdir();
        moduleManager.saveModules();
        friendManager.getFriendSaving().saveFile();
        accountManager.getAltSaving().saveFile();
        accountManager.getAltSaving().saveLastAltFile();
        accountManager.getAltSaving().saveAlteningTokenFile();
        antistrike.saveFile();
    }
    public void switchToMojang() {
        try {
            altService.switchService(AltService.EnumAltService.MOJANG);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Couldnt switch to modank altservice");
        }
    }
    public void switchToTheAltening() {
        try {
            altService.switchService(AltService.EnumAltService.THEALTENING);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Couldnt switch to altening altservice");
        }
    }
    public AccountManager getAccountManager() {
        return accountManager;
    }
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    public FriendManager getFriendManager() {
        return this.friendManager;
    }
    public AltService getAltService() {
        return altService;
    }
    public SSLVerification getSSLVerification() {
        return sslVerification;
    }
    public AntiStrike getAntiStrike() {
        return antistrike;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
}