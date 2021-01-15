/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class CommandHelp
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  23 */     return "help";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  31 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  39 */     return "commands.help.usage";
/*     */   }
/*     */   
/*     */   public List<String> getCommandAliases()
/*     */   {
/*  44 */     return Arrays.asList(new String[] { "?" });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  52 */     List<ICommand> list = getSortedPossibleCommands(sender);
/*  53 */     int i = 7;
/*  54 */     int j = (list.size() - 1) / 7;
/*  55 */     int k = 0;
/*     */     
/*     */     try
/*     */     {
/*  59 */       k = args.length == 0 ? 0 : parseInt(args[0], 1, j + 1) - 1;
/*     */     }
/*     */     catch (NumberInvalidException numberinvalidexception)
/*     */     {
/*  63 */       Map<String, ICommand> map = getCommands();
/*  64 */       ICommand icommand = (ICommand)map.get(args[0]);
/*     */       
/*  66 */       if (icommand != null)
/*     */       {
/*  68 */         throw new WrongUsageException(icommand.getCommandUsage(sender), new Object[0]);
/*     */       }
/*     */       
/*  71 */       if (MathHelper.parseIntWithDefault(args[0], -1) != -1)
/*     */       {
/*  73 */         throw numberinvalidexception;
/*     */       }
/*     */       
/*  76 */       throw new CommandNotFoundException();
/*     */     }
/*     */     
/*  79 */     int l = Math.min((k + 1) * 7, list.size());
/*  80 */     ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.help.header", new Object[] { Integer.valueOf(k + 1), Integer.valueOf(j + 1) });
/*  81 */     chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  82 */     sender.addChatMessage(chatcomponenttranslation1);
/*     */     
/*  84 */     for (int i1 = k * 7; i1 < l; i1++)
/*     */     {
/*  86 */       ICommand icommand1 = (ICommand)list.get(i1);
/*  87 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(icommand1.getCommandUsage(sender), new Object[0]);
/*  88 */       chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
/*  89 */       sender.addChatMessage(chatcomponenttranslation);
/*     */     }
/*     */     
/*  92 */     if ((k == 0) && ((sender instanceof EntityPlayer)))
/*     */     {
/*  94 */       ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
/*  95 */       chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.GREEN);
/*  96 */       sender.addChatMessage(chatcomponenttranslation2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<ICommand> getSortedPossibleCommands(ICommandSender p_71534_1_)
/*     */   {
/* 102 */     List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
/* 103 */     Collections.sort(list);
/* 104 */     return list;
/*     */   }
/*     */   
/*     */   protected Map<String, ICommand> getCommands()
/*     */   {
/* 109 */     return MinecraftServer.getServer().getCommandManager().getCommands();
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 114 */     if (args.length == 1)
/*     */     {
/* 116 */       Set<String> set = getCommands().keySet();
/* 117 */       return getListOfStringsMatchingLastWord(args, (String[])set.toArray(new String[set.size()]));
/*     */     }
/*     */     
/*     */ 
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandHelp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */