/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.PlayerProfileCache;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.server.management.UserListWhitelist;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ 
/*     */ public class CommandWhitelist extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  21 */     return "whitelist";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  29 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  37 */     return "commands.whitelist.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  45 */     if (args.length < 1)
/*     */     {
/*  47 */       throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  51 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/*  53 */     if (args[0].equals("on"))
/*     */     {
/*  55 */       minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
/*  56 */       notifyOperators(sender, this, "commands.whitelist.enabled", new Object[0]);
/*     */     }
/*  58 */     else if (args[0].equals("off"))
/*     */     {
/*  60 */       minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
/*  61 */       notifyOperators(sender, this, "commands.whitelist.disabled", new Object[0]);
/*     */     }
/*  63 */     else if (args[0].equals("list"))
/*     */     {
/*  65 */       sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", new Object[] { Integer.valueOf(minecraftserver.getConfigurationManager().getWhitelistedPlayerNames().length), Integer.valueOf(minecraftserver.getConfigurationManager().getAvailablePlayerDat().length) }));
/*  66 */       String[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
/*  67 */       sender.addChatMessage(new ChatComponentText(joinNiceString(astring)));
/*     */     }
/*  69 */     else if (args[0].equals("add"))
/*     */     {
/*  71 */       if (args.length < 2)
/*     */       {
/*  73 */         throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
/*     */       }
/*     */       
/*  76 */       GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
/*     */       
/*  78 */       if (gameprofile == null)
/*     */       {
/*  80 */         throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] });
/*     */       }
/*     */       
/*  83 */       minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile);
/*  84 */       notifyOperators(sender, this, "commands.whitelist.add.success", new Object[] { args[1] });
/*     */     }
/*  86 */     else if (args[0].equals("remove"))
/*     */     {
/*  88 */       if (args.length < 2)
/*     */       {
/*  90 */         throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
/*     */       }
/*     */       
/*  93 */       GameProfile gameprofile1 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
/*     */       
/*  95 */       if (gameprofile1 == null)
/*     */       {
/*  97 */         throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] });
/*     */       }
/*     */       
/* 100 */       minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile1);
/* 101 */       notifyOperators(sender, this, "commands.whitelist.remove.success", new Object[] { args[1] });
/*     */     }
/* 103 */     else if (args[0].equals("reload"))
/*     */     {
/* 105 */       minecraftserver.getConfigurationManager().loadWhiteList();
/* 106 */       notifyOperators(sender, this, "commands.whitelist.reloaded", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 113 */     if (args.length == 1)
/*     */     {
/* 115 */       return getListOfStringsMatchingLastWord(args, new String[] { "on", "off", "list", "add", "remove", "reload" });
/*     */     }
/*     */     
/*     */ 
/* 119 */     if (args.length == 2)
/*     */     {
/* 121 */       if (args[0].equals("remove"))
/*     */       {
/* 123 */         return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
/*     */       }
/*     */       
/* 126 */       if (args[0].equals("add"))
/*     */       {
/* 128 */         return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
/*     */       }
/*     */     }
/*     */     
/* 132 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandWhitelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */