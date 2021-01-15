/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandHandler implements ICommandManager
/*     */ {
/*  19 */   private static final Logger logger = ;
/*  20 */   private final Map<String, ICommand> commandMap = Maps.newHashMap();
/*  21 */   private final Set<ICommand> commandSet = com.google.common.collect.Sets.newHashSet();
/*     */   
/*     */   public int executeCommand(ICommandSender sender, String rawCommand)
/*     */   {
/*  25 */     rawCommand = rawCommand.trim();
/*     */     
/*  27 */     if (rawCommand.startsWith("/"))
/*     */     {
/*  29 */       rawCommand = rawCommand.substring(1);
/*     */     }
/*     */     
/*  32 */     String[] astring = rawCommand.split(" ");
/*  33 */     String s = astring[0];
/*  34 */     astring = dropFirstString(astring);
/*  35 */     ICommand icommand = (ICommand)this.commandMap.get(s);
/*  36 */     int i = getUsernameIndex(icommand, astring);
/*  37 */     int j = 0;
/*     */     
/*  39 */     if (icommand == null)
/*     */     {
/*  41 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
/*  42 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  43 */       sender.addChatMessage(chatcomponenttranslation);
/*     */     }
/*  45 */     else if (icommand.canCommandSenderUseCommand(sender))
/*     */     {
/*  47 */       if (i > -1)
/*     */       {
/*  49 */         List<Entity> list = PlayerSelector.matchEntities(sender, astring[i], Entity.class);
/*  50 */         String s1 = astring[i];
/*  51 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*     */         
/*  53 */         for (Entity entity : list)
/*     */         {
/*  55 */           astring[i] = entity.getUniqueID().toString();
/*     */           
/*  57 */           if (tryExecute(sender, astring, icommand, rawCommand))
/*     */           {
/*  59 */             j++;
/*     */           }
/*     */         }
/*     */         
/*  63 */         astring[i] = s1;
/*     */       }
/*     */       else
/*     */       {
/*  67 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
/*     */         
/*  69 */         if (tryExecute(sender, astring, icommand, rawCommand))
/*     */         {
/*  71 */           j++;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  77 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
/*  78 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
/*  79 */       sender.addChatMessage(chatcomponenttranslation1);
/*     */     }
/*     */     
/*  82 */     sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
/*  83 */     return j;
/*     */   }
/*     */   
/*     */   protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input)
/*     */   {
/*     */     try
/*     */     {
/*  90 */       command.processCommand(sender, args);
/*  91 */       return true;
/*     */     }
/*     */     catch (WrongUsageException wrongusageexception)
/*     */     {
/*  95 */       ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage", new Object[] { new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()) });
/*  96 */       chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
/*  97 */       sender.addChatMessage(chatcomponenttranslation2);
/*     */     }
/*     */     catch (CommandException commandexception)
/*     */     {
/* 101 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
/* 102 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
/* 103 */       sender.addChatMessage(chatcomponenttranslation1);
/*     */     }
/*     */     catch (Throwable var9)
/*     */     {
/* 107 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
/* 108 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/* 109 */       sender.addChatMessage(chatcomponenttranslation);
/* 110 */       logger.warn("Couldn't process command: '" + input + "'");
/*     */     }
/*     */     
/* 113 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICommand registerCommand(ICommand command)
/*     */   {
/* 121 */     this.commandMap.put(command.getCommandName(), command);
/* 122 */     this.commandSet.add(command);
/*     */     
/* 124 */     for (String s : command.getCommandAliases())
/*     */     {
/* 126 */       ICommand icommand = (ICommand)this.commandMap.get(s);
/*     */       
/* 128 */       if ((icommand == null) || (!icommand.getCommandName().equals(s)))
/*     */       {
/* 130 */         this.commandMap.put(s, command);
/*     */       }
/*     */     }
/*     */     
/* 134 */     return command;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String[] dropFirstString(String[] input)
/*     */   {
/* 142 */     String[] astring = new String[input.length - 1];
/* 143 */     System.arraycopy(input, 1, astring, 0, input.length - 1);
/* 144 */     return astring;
/*     */   }
/*     */   
/*     */   public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos)
/*     */   {
/* 149 */     String[] astring = input.split(" ", -1);
/* 150 */     String s = astring[0];
/*     */     
/* 152 */     if (astring.length == 1)
/*     */     {
/* 154 */       List<String> list = Lists.newArrayList();
/*     */       
/* 156 */       for (Map.Entry<String, ICommand> entry : this.commandMap.entrySet())
/*     */       {
/* 158 */         if ((CommandBase.doesStringStartWith(s, (String)entry.getKey())) && (((ICommand)entry.getValue()).canCommandSenderUseCommand(sender)))
/*     */         {
/* 160 */           list.add((String)entry.getKey());
/*     */         }
/*     */       }
/*     */       
/* 164 */       return list;
/*     */     }
/*     */     
/*     */ 
/* 168 */     if (astring.length > 1)
/*     */     {
/* 170 */       ICommand icommand = (ICommand)this.commandMap.get(s);
/*     */       
/* 172 */       if ((icommand != null) && (icommand.canCommandSenderUseCommand(sender)))
/*     */       {
/* 174 */         return icommand.addTabCompletionOptions(sender, dropFirstString(astring), pos);
/*     */       }
/*     */     }
/*     */     
/* 178 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<ICommand> getPossibleCommands(ICommandSender sender)
/*     */   {
/* 184 */     List<ICommand> list = Lists.newArrayList();
/*     */     
/* 186 */     for (ICommand icommand : this.commandSet)
/*     */     {
/* 188 */       if (icommand.canCommandSenderUseCommand(sender))
/*     */       {
/* 190 */         list.add(icommand);
/*     */       }
/*     */     }
/*     */     
/* 194 */     return list;
/*     */   }
/*     */   
/*     */   public Map<String, ICommand> getCommands()
/*     */   {
/* 199 */     return this.commandMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getUsernameIndex(ICommand command, String[] args)
/*     */   {
/* 207 */     if (command == null)
/*     */     {
/* 209 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 213 */     for (int i = 0; i < args.length; i++)
/*     */     {
/* 215 */       if ((command.isUsernameIndex(args, i)) && (PlayerSelector.matchesMultiplePlayers(args[i])))
/*     */       {
/* 217 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 221 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */