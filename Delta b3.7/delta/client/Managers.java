/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.api.APLProvider
 *  me.xtrm.atlaspluginloader.api.event.IEventManager
 *  me.xtrm.delta.api.DeltaAPI
 *  me.xtrm.delta.api.command.ICommandManager
 *  me.xtrm.delta.api.module.IModuleManager
 *  me.xtrm.delta.api.setting.ISettingManager
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package delta.client;

import delta.Class200;
import delta.Class89;
import delta.command.CommandsManager;
import delta.files.FileManager;
import delta.friends.FriendsManager;
import delta.hooks.FMLBusHook;
import delta.module.ModulesManager;
import delta.utils.RpcUtils;
import me.xtrm.atlaspluginloader.api.APLProvider;
import me.xtrm.atlaspluginloader.api.event.IEventManager;
import me.xtrm.delta.api.DeltaAPI;
import me.xtrm.delta.api.command.ICommandManager;
import me.xtrm.delta.api.module.IModuleManager;
import me.xtrm.delta.api.setting.ISettingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Managers {
    public IEventManager eventManager;
    public FriendsManager friendsManager;
    public FileManager filesManager;
    public ISettingManager settingsManager;
    public ICommandManager commandsManager;
    public ModulesManager modulesManager;
    public FMLBusHook fmlBusHook;
    public RpcUtils rpc;

    public void _tagged() {
        Logger logger = LogManager.getLogger((String)"MasterManager");
        logger.info("Initializing ModuleManager...");
        this.modulesManager.registerModules();
        logger.info("Initializing CommandManager...");
        this.commandsManager.registerCommands();
    }

    public Managers() {
        Logger logger = LogManager.getLogger((String)"MasterManager");
        logger.info("Initializing FMLBusHook...");
        this.fmlBusHook = new FMLBusHook();
        this.fmlBusHook._campaign(new Class200());
        logger.info("Hooking EventManager...");
        this.eventManager = APLProvider.getPrimaryAPL().getEventManager();
        DeltaAPI.setEventManager((IEventManager)this.eventManager);
        logger.info("Constructing SettingsManager...");
        this.settingsManager = new Class89();
        DeltaAPI.setSettingManager((ISettingManager)this.settingsManager);
        logger.info("Constructing ModuleManager...");
        this.modulesManager = new ModulesManager();
        DeltaAPI.setModuleManager((IModuleManager)this.modulesManager);
        logger.info("Constructing CommandManager...");
        this.commandsManager = new CommandsManager();
        DeltaAPI.setCommandManager((ICommandManager)this.commandsManager);
        logger.info("Constructing FileManager...");
        this.filesManager = new FileManager();
        logger.info("Constructing FriendManager...");
        this.friendsManager = new FriendsManager();
    }

    public void _getting() {
        Logger logger = LogManager.getLogger((String)"MasterManager");
        logger.info("PostInitializing RPCManager...");
        this.rpc = new RpcUtils();
        logger.info("PostInitializing FileManager...");
        this.filesManager._watts();
    }

    public void _minds() {
        this.filesManager._medicine();
    }
}

