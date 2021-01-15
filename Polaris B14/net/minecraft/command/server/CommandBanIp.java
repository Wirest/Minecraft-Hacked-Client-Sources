/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.PlayerNotFoundException;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.BanList;
/*     */ import net.minecraft.server.management.IPBanEntry;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class CommandBanIp extends CommandBase
/*     */ {
/*  20 */   public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/*  27 */     return "ban-ip";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  35 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCommandSenderUseCommand(ICommandSender sender)
/*     */   {
/*  43 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer()) && (super.canCommandSenderUseCommand(sender));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  51 */     return "commands.banip.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  59 */     if ((args.length >= 1) && (args[0].length() > 1))
/*     */     {
/*  61 */       IChatComponent ichatcomponent = args.length >= 2 ? getChatComponentFromNthArg(sender, args, 1) : null;
/*  62 */       Matcher matcher = field_147211_a.matcher(args[0]);
/*     */       
/*  64 */       if (matcher.matches())
/*     */       {
/*  66 */         func_147210_a(sender, args[0], ichatcomponent == null ? null : ichatcomponent.getUnformattedText());
/*     */       }
/*     */       else
/*     */       {
/*  70 */         EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
/*     */         
/*  72 */         if (entityplayermp == null)
/*     */         {
/*  74 */           throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
/*     */         }
/*     */         
/*  77 */         func_147210_a(sender, entityplayermp.getPlayerIP(), ichatcomponent == null ? null : ichatcomponent.getUnformattedText());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  82 */       throw new net.minecraft.command.WrongUsageException("commands.banip.usage", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/*  88 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */   }
/*     */   
/*     */   protected void func_147210_a(ICommandSender p_147210_1_, String p_147210_2_, String p_147210_3_)
/*     */   {
/*  93 */     IPBanEntry ipbanentry = new IPBanEntry(p_147210_2_, null, p_147210_1_.getName(), null, p_147210_3_);
/*  94 */     MinecraftServer.getServer().getConfigurationManager().getBannedIPs().addEntry(ipbanentry);
/*  95 */     List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().getPlayersMatchingAddress(p_147210_2_);
/*  96 */     String[] astring = new String[list.size()];
/*  97 */     int i = 0;
/*     */     
/*  99 */     for (EntityPlayerMP entityplayermp : list)
/*     */     {
/* 101 */       entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
/* 102 */       astring[(i++)] = entityplayermp.getName();
/*     */     }
/*     */     
/* 105 */     if (list.isEmpty())
/*     */     {
/* 107 */       notifyOperators(p_147210_1_, this, "commands.banip.success", new Object[] { p_147210_2_ });
/*     */     }
/*     */     else
/*     */     {
/* 111 */       notifyOperators(p_147210_1_, this, "commands.banip.success.players", new Object[] { p_147210_2_, joinNiceString(astring) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandBanIp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */