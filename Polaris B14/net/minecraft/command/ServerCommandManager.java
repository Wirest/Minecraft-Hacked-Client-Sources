/*     */ package net.minecraft.command;
/*     */ 
/*     */ import net.minecraft.command.server.CommandAchievement;
/*     */ import net.minecraft.command.server.CommandBanIp;
/*     */ import net.minecraft.command.server.CommandBanPlayer;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.command.server.CommandBroadcast;
/*     */ import net.minecraft.command.server.CommandDeOp;
/*     */ import net.minecraft.command.server.CommandListBans;
/*     */ import net.minecraft.command.server.CommandListPlayers;
/*     */ import net.minecraft.command.server.CommandMessage;
/*     */ import net.minecraft.command.server.CommandOp;
/*     */ import net.minecraft.command.server.CommandPardonIp;
/*     */ import net.minecraft.command.server.CommandPardonPlayer;
/*     */ import net.minecraft.command.server.CommandPublishLocalServer;
/*     */ import net.minecraft.command.server.CommandSaveAll;
/*     */ import net.minecraft.command.server.CommandSaveOff;
/*     */ import net.minecraft.command.server.CommandSaveOn;
/*     */ import net.minecraft.command.server.CommandScoreboard;
/*     */ import net.minecraft.command.server.CommandSetBlock;
/*     */ import net.minecraft.command.server.CommandStop;
/*     */ import net.minecraft.command.server.CommandSummon;
/*     */ import net.minecraft.command.server.CommandTeleport;
/*     */ import net.minecraft.command.server.CommandTestFor;
/*     */ import net.minecraft.command.server.CommandTestForBlock;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.rcon.RConConsoleSource;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class ServerCommandManager extends CommandHandler implements IAdminCommand
/*     */ {
/*     */   public ServerCommandManager()
/*     */   {
/*  41 */     registerCommand(new CommandTime());
/*  42 */     registerCommand(new CommandGameMode());
/*  43 */     registerCommand(new CommandDifficulty());
/*  44 */     registerCommand(new CommandDefaultGameMode());
/*  45 */     registerCommand(new CommandKill());
/*  46 */     registerCommand(new CommandToggleDownfall());
/*  47 */     registerCommand(new CommandWeather());
/*  48 */     registerCommand(new CommandXP());
/*  49 */     registerCommand(new CommandTeleport());
/*  50 */     registerCommand(new CommandGive());
/*  51 */     registerCommand(new CommandReplaceItem());
/*  52 */     registerCommand(new CommandStats());
/*  53 */     registerCommand(new CommandEffect());
/*  54 */     registerCommand(new CommandEnchant());
/*  55 */     registerCommand(new CommandParticle());
/*  56 */     registerCommand(new net.minecraft.command.server.CommandEmote());
/*  57 */     registerCommand(new CommandShowSeed());
/*  58 */     registerCommand(new CommandHelp());
/*  59 */     registerCommand(new CommandDebug());
/*  60 */     registerCommand(new CommandMessage());
/*  61 */     registerCommand(new CommandBroadcast());
/*  62 */     registerCommand(new CommandSetSpawnpoint());
/*  63 */     registerCommand(new net.minecraft.command.server.CommandSetDefaultSpawnpoint());
/*  64 */     registerCommand(new CommandGameRule());
/*  65 */     registerCommand(new CommandClearInventory());
/*  66 */     registerCommand(new CommandTestFor());
/*  67 */     registerCommand(new CommandSpreadPlayers());
/*  68 */     registerCommand(new CommandPlaySound());
/*  69 */     registerCommand(new CommandScoreboard());
/*  70 */     registerCommand(new CommandExecuteAt());
/*  71 */     registerCommand(new CommandTrigger());
/*  72 */     registerCommand(new CommandAchievement());
/*  73 */     registerCommand(new CommandSummon());
/*  74 */     registerCommand(new CommandSetBlock());
/*  75 */     registerCommand(new CommandFill());
/*  76 */     registerCommand(new CommandClone());
/*  77 */     registerCommand(new CommandCompare());
/*  78 */     registerCommand(new CommandBlockData());
/*  79 */     registerCommand(new CommandTestForBlock());
/*  80 */     registerCommand(new net.minecraft.command.server.CommandMessageRaw());
/*  81 */     registerCommand(new CommandWorldBorder());
/*  82 */     registerCommand(new CommandTitle());
/*  83 */     registerCommand(new CommandEntityData());
/*     */     
/*  85 */     if (MinecraftServer.getServer().isDedicatedServer())
/*     */     {
/*  87 */       registerCommand(new CommandOp());
/*  88 */       registerCommand(new CommandDeOp());
/*  89 */       registerCommand(new CommandStop());
/*  90 */       registerCommand(new CommandSaveAll());
/*  91 */       registerCommand(new CommandSaveOff());
/*  92 */       registerCommand(new CommandSaveOn());
/*  93 */       registerCommand(new CommandBanIp());
/*  94 */       registerCommand(new CommandPardonIp());
/*  95 */       registerCommand(new CommandBanPlayer());
/*  96 */       registerCommand(new CommandListBans());
/*  97 */       registerCommand(new CommandPardonPlayer());
/*  98 */       registerCommand(new CommandServerKick());
/*  99 */       registerCommand(new CommandListPlayers());
/* 100 */       registerCommand(new net.minecraft.command.server.CommandWhitelist());
/* 101 */       registerCommand(new CommandSetPlayerTimeout());
/*     */     }
/*     */     else
/*     */     {
/* 105 */       registerCommand(new CommandPublishLocalServer());
/*     */     }
/*     */     
/* 108 */     CommandBase.setAdminCommander(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat, Object... msgParams)
/*     */   {
/* 116 */     boolean flag = true;
/* 117 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/* 119 */     if (!sender.sendCommandFeedback())
/*     */     {
/* 121 */       flag = false;
/*     */     }
/*     */     
/* 124 */     IChatComponent ichatcomponent = new ChatComponentTranslation("chat.type.admin", new Object[] { sender.getName(), new ChatComponentTranslation(msgFormat, msgParams) });
/* 125 */     ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
/* 126 */     ichatcomponent.getChatStyle().setItalic(Boolean.valueOf(true));
/*     */     
/* 128 */     if (flag)
/*     */     {
/* 130 */       for (EntityPlayer entityplayer : minecraftserver.getConfigurationManager().func_181057_v())
/*     */       {
/* 132 */         if ((entityplayer != sender) && (minecraftserver.getConfigurationManager().canSendCommands(entityplayer.getGameProfile())) && (command.canCommandSenderUseCommand(sender)))
/*     */         {
/* 134 */           boolean flag1 = ((sender instanceof MinecraftServer)) && (MinecraftServer.getServer().func_183002_r());
/* 135 */           boolean flag2 = ((sender instanceof RConConsoleSource)) && (MinecraftServer.getServer().func_181034_q());
/*     */           
/* 137 */           if ((flag1) || (flag2) || ((!(sender instanceof RConConsoleSource)) && (!(sender instanceof MinecraftServer))))
/*     */           {
/* 139 */             entityplayer.addChatMessage(ichatcomponent);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 145 */     if ((sender != minecraftserver) && (minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands")))
/*     */     {
/* 147 */       minecraftserver.addChatMessage(ichatcomponent);
/*     */     }
/*     */     
/* 150 */     boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
/*     */     
/* 152 */     if ((sender instanceof CommandBlockLogic))
/*     */     {
/* 154 */       flag3 = ((CommandBlockLogic)sender).shouldTrackOutput();
/*     */     }
/*     */     
/* 157 */     if ((((flags & 0x1) != 1) && (flag3)) || ((sender instanceof MinecraftServer)))
/*     */     {
/* 159 */       sender.addChatMessage(new ChatComponentTranslation(msgFormat, msgParams));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\ServerCommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */