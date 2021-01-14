package rip.autumn.core.registry.impl;

import rip.autumn.command.CommandManager;
import rip.autumn.config.ConfigManager;
import rip.autumn.core.registry.Registry;
import rip.autumn.file.FileManager;
import rip.autumn.friend.FriendManager;
import rip.autumn.module.ModuleManager;

public final class ManagerRegistry implements Registry {
   public final ConfigManager configManager = new ConfigManager();
   public final FileManager fileManager = new FileManager();
   public final FriendManager friendManager = new FriendManager();
   public final ModuleManager moduleManager = new ModuleManager();
   public final CommandManager commandManager = new CommandManager();
}
