/*      */ package net.minecraft.command.server;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.command.CommandException;
/*      */ import net.minecraft.command.CommandResultStats.Type;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.command.SyntaxErrorException;
/*      */ import net.minecraft.command.WrongUsageException;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.nbt.JsonToNBT;
/*      */ import net.minecraft.nbt.NBTException;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTUtil;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team.EnumVisible;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ 
/*      */ public class CommandScoreboard extends net.minecraft.command.CommandBase
/*      */ {
/*      */   public String getCommandName()
/*      */   {
/*   41 */     return "scoreboard";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRequiredPermissionLevel()
/*      */   {
/*   49 */     return 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCommandUsage(ICommandSender sender)
/*      */   {
/*   57 */     return "commands.scoreboard.usage";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void processCommand(ICommandSender sender, String[] args)
/*      */     throws CommandException
/*      */   {
/*   65 */     if (!func_175780_b(sender, args))
/*      */     {
/*   67 */       if (args.length < 1)
/*      */       {
/*   69 */         throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*   73 */       if (args[0].equalsIgnoreCase("objectives"))
/*      */       {
/*   75 */         if (args.length == 1)
/*      */         {
/*   77 */           throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
/*      */         }
/*      */         
/*   80 */         if (args[1].equalsIgnoreCase("list"))
/*      */         {
/*   82 */           listObjectives(sender);
/*      */         }
/*   84 */         else if (args[1].equalsIgnoreCase("add"))
/*      */         {
/*   86 */           if (args.length < 4)
/*      */           {
/*   88 */             throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
/*      */           }
/*      */           
/*   91 */           addObjective(sender, args, 2);
/*      */         }
/*   93 */         else if (args[1].equalsIgnoreCase("remove"))
/*      */         {
/*   95 */           if (args.length != 3)
/*      */           {
/*   97 */             throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*  100 */           removeObjective(sender, args[2]);
/*      */         }
/*      */         else
/*      */         {
/*  104 */           if (!args[1].equalsIgnoreCase("setdisplay"))
/*      */           {
/*  106 */             throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
/*      */           }
/*      */           
/*  109 */           if ((args.length != 3) && (args.length != 4))
/*      */           {
/*  111 */             throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
/*      */           }
/*      */           
/*  114 */           setObjectiveDisplay(sender, args, 2);
/*      */         }
/*      */       }
/*  117 */       else if (args[0].equalsIgnoreCase("players"))
/*      */       {
/*  119 */         if (args.length == 1)
/*      */         {
/*  121 */           throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
/*      */         }
/*      */         
/*  124 */         if (args[1].equalsIgnoreCase("list"))
/*      */         {
/*  126 */           if (args.length > 3)
/*      */           {
/*  128 */             throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
/*      */           }
/*      */           
/*  131 */           listPlayers(sender, args, 2);
/*      */         }
/*  133 */         else if (args[1].equalsIgnoreCase("add"))
/*      */         {
/*  135 */           if (args.length < 5)
/*      */           {
/*  137 */             throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
/*      */           }
/*      */           
/*  140 */           setPlayer(sender, args, 2);
/*      */         }
/*  142 */         else if (args[1].equalsIgnoreCase("remove"))
/*      */         {
/*  144 */           if (args.length < 5)
/*      */           {
/*  146 */             throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*  149 */           setPlayer(sender, args, 2);
/*      */         }
/*  151 */         else if (args[1].equalsIgnoreCase("set"))
/*      */         {
/*  153 */           if (args.length < 5)
/*      */           {
/*  155 */             throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
/*      */           }
/*      */           
/*  158 */           setPlayer(sender, args, 2);
/*      */         }
/*  160 */         else if (args[1].equalsIgnoreCase("reset"))
/*      */         {
/*  162 */           if ((args.length != 3) && (args.length != 4))
/*      */           {
/*  164 */             throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
/*      */           }
/*      */           
/*  167 */           resetPlayers(sender, args, 2);
/*      */         }
/*  169 */         else if (args[1].equalsIgnoreCase("enable"))
/*      */         {
/*  171 */           if (args.length != 4)
/*      */           {
/*  173 */             throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
/*      */           }
/*      */           
/*  176 */           func_175779_n(sender, args, 2);
/*      */         }
/*  178 */         else if (args[1].equalsIgnoreCase("test"))
/*      */         {
/*  180 */           if ((args.length != 5) && (args.length != 6))
/*      */           {
/*  182 */             throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
/*      */           }
/*      */           
/*  185 */           func_175781_o(sender, args, 2);
/*      */         }
/*      */         else
/*      */         {
/*  189 */           if (!args[1].equalsIgnoreCase("operation"))
/*      */           {
/*  191 */             throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
/*      */           }
/*      */           
/*  194 */           if (args.length != 7)
/*      */           {
/*  196 */             throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
/*      */           }
/*      */           
/*  199 */           func_175778_p(sender, args, 2);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  204 */         if (!args[0].equalsIgnoreCase("teams"))
/*      */         {
/*  206 */           throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
/*      */         }
/*      */         
/*  209 */         if (args.length == 1)
/*      */         {
/*  211 */           throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
/*      */         }
/*      */         
/*  214 */         if (args[1].equalsIgnoreCase("list"))
/*      */         {
/*  216 */           if (args.length > 3)
/*      */           {
/*  218 */             throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
/*      */           }
/*      */           
/*  221 */           listTeams(sender, args, 2);
/*      */         }
/*  223 */         else if (args[1].equalsIgnoreCase("add"))
/*      */         {
/*  225 */           if (args.length < 3)
/*      */           {
/*  227 */             throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
/*      */           }
/*      */           
/*  230 */           addTeam(sender, args, 2);
/*      */         }
/*  232 */         else if (args[1].equalsIgnoreCase("remove"))
/*      */         {
/*  234 */           if (args.length != 3)
/*      */           {
/*  236 */             throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
/*      */           }
/*      */           
/*  239 */           removeTeam(sender, args, 2);
/*      */         }
/*  241 */         else if (args[1].equalsIgnoreCase("empty"))
/*      */         {
/*  243 */           if (args.length != 3)
/*      */           {
/*  245 */             throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
/*      */           }
/*      */           
/*  248 */           emptyTeam(sender, args, 2);
/*      */         }
/*  250 */         else if (args[1].equalsIgnoreCase("join"))
/*      */         {
/*  252 */           if ((args.length < 4) && ((args.length != 3) || (!(sender instanceof EntityPlayer))))
/*      */           {
/*  254 */             throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
/*      */           }
/*      */           
/*  257 */           joinTeam(sender, args, 2);
/*      */         }
/*  259 */         else if (args[1].equalsIgnoreCase("leave"))
/*      */         {
/*  261 */           if ((args.length < 3) && (!(sender instanceof EntityPlayer)))
/*      */           {
/*  263 */             throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
/*      */           }
/*      */           
/*  266 */           leaveTeam(sender, args, 2);
/*      */         }
/*      */         else
/*      */         {
/*  270 */           if (!args[1].equalsIgnoreCase("option"))
/*      */           {
/*  272 */             throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
/*      */           }
/*      */           
/*  275 */           if ((args.length != 4) && (args.length != 5))
/*      */           {
/*  277 */             throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */           }
/*      */           
/*  280 */           setTeamOption(sender, args, 2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean func_175780_b(ICommandSender p_175780_1_, String[] p_175780_2_)
/*      */     throws CommandException
/*      */   {
/*  289 */     int i = -1;
/*      */     
/*  291 */     for (int j = 0; j < p_175780_2_.length; j++)
/*      */     {
/*  293 */       if ((isUsernameIndex(p_175780_2_, j)) && ("*".equals(p_175780_2_[j])))
/*      */       {
/*  295 */         if (i >= 0)
/*      */         {
/*  297 */           throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
/*      */         }
/*      */         
/*  300 */         i = j;
/*      */       }
/*      */     }
/*      */     
/*  304 */     if (i < 0)
/*      */     {
/*  306 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  310 */     List<String> list1 = Lists.newArrayList(getScoreboard().getObjectiveNames());
/*  311 */     String s = p_175780_2_[i];
/*  312 */     List<String> list = Lists.newArrayList();
/*      */     
/*  314 */     for (String s1 : list1)
/*      */     {
/*  316 */       p_175780_2_[i] = s1;
/*      */       
/*      */       try
/*      */       {
/*  320 */         processCommand(p_175780_1_, p_175780_2_);
/*  321 */         list.add(s1);
/*      */       }
/*      */       catch (CommandException commandexception)
/*      */       {
/*  325 */         ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
/*  326 */         chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  327 */         p_175780_1_.addChatMessage(chatcomponenttranslation);
/*      */       }
/*      */     }
/*      */     
/*  331 */     p_175780_2_[i] = s;
/*  332 */     p_175780_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*      */     
/*  334 */     if (list.size() == 0)
/*      */     {
/*  336 */       throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  340 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Scoreboard getScoreboard()
/*      */   {
/*  347 */     return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*      */   }
/*      */   
/*      */   protected ScoreObjective getObjective(String name, boolean edit) throws CommandException
/*      */   {
/*  352 */     Scoreboard scoreboard = getScoreboard();
/*  353 */     ScoreObjective scoreobjective = scoreboard.getObjective(name);
/*      */     
/*  355 */     if (scoreobjective == null)
/*      */     {
/*  357 */       throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { name });
/*      */     }
/*  359 */     if ((edit) && (scoreobjective.getCriteria().isReadOnly()))
/*      */     {
/*  361 */       throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { name });
/*      */     }
/*      */     
/*      */ 
/*  365 */     return scoreobjective;
/*      */   }
/*      */   
/*      */   protected ScorePlayerTeam getTeam(String name)
/*      */     throws CommandException
/*      */   {
/*  371 */     Scoreboard scoreboard = getScoreboard();
/*  372 */     ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(name);
/*      */     
/*  374 */     if (scoreplayerteam == null)
/*      */     {
/*  376 */       throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { name });
/*      */     }
/*      */     
/*      */ 
/*  380 */     return scoreplayerteam;
/*      */   }
/*      */   
/*      */   protected void addObjective(ICommandSender sender, String[] args, int index)
/*      */     throws CommandException
/*      */   {
/*  386 */     String s = args[(index++)];
/*  387 */     String s1 = args[(index++)];
/*  388 */     Scoreboard scoreboard = getScoreboard();
/*  389 */     IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(s1);
/*      */     
/*  391 */     if (iscoreobjectivecriteria == null)
/*      */     {
/*  393 */       throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s1 });
/*      */     }
/*  395 */     if (scoreboard.getObjective(s) != null)
/*      */     {
/*  397 */       throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s });
/*      */     }
/*  399 */     if (s.length() > 16)
/*      */     {
/*  401 */       throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, Integer.valueOf(16) });
/*      */     }
/*  403 */     if (s.length() == 0)
/*      */     {
/*  405 */       throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  409 */     if (args.length > index)
/*      */     {
/*  411 */       String s2 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();
/*      */       
/*  413 */       if (s2.length() > 32)
/*      */       {
/*  415 */         throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s2, Integer.valueOf(32) });
/*      */       }
/*      */       
/*  418 */       if (s2.length() > 0)
/*      */       {
/*  420 */         scoreboard.addScoreObjective(s, iscoreobjectivecriteria).setDisplayName(s2);
/*      */       }
/*      */       else
/*      */       {
/*  424 */         scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  429 */       scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/*      */     }
/*      */     
/*  432 */     notifyOperators(sender, this, "commands.scoreboard.objectives.add.success", new Object[] { s });
/*      */   }
/*      */   
/*      */   protected void addTeam(ICommandSender sender, String[] args, int index)
/*      */     throws CommandException
/*      */   {
/*  438 */     String s = args[(index++)];
/*  439 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  441 */     if (scoreboard.getTeam(s) != null)
/*      */     {
/*  443 */       throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s });
/*      */     }
/*  445 */     if (s.length() > 16)
/*      */     {
/*  447 */       throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, Integer.valueOf(16) });
/*      */     }
/*  449 */     if (s.length() == 0)
/*      */     {
/*  451 */       throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  455 */     if (args.length > index)
/*      */     {
/*  457 */       String s1 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();
/*      */       
/*  459 */       if (s1.length() > 32)
/*      */       {
/*  461 */         throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s1, Integer.valueOf(32) });
/*      */       }
/*      */       
/*  464 */       if (s1.length() > 0)
/*      */       {
/*  466 */         scoreboard.createTeam(s).setTeamName(s1);
/*      */       }
/*      */       else
/*      */       {
/*  470 */         scoreboard.createTeam(s);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  475 */       scoreboard.createTeam(s);
/*      */     }
/*      */     
/*  478 */     notifyOperators(sender, this, "commands.scoreboard.teams.add.success", new Object[] { s });
/*      */   }
/*      */   
/*      */   protected void setTeamOption(ICommandSender sender, String[] args, int index)
/*      */     throws CommandException
/*      */   {
/*  484 */     ScorePlayerTeam scoreplayerteam = getTeam(args[(index++)]);
/*      */     
/*  486 */     if (scoreplayerteam != null)
/*      */     {
/*  488 */       String s = args[(index++)].toLowerCase();
/*      */       
/*  490 */       if ((!s.equalsIgnoreCase("color")) && (!s.equalsIgnoreCase("friendlyfire")) && (!s.equalsIgnoreCase("seeFriendlyInvisibles")) && (!s.equalsIgnoreCase("nametagVisibility")) && (!s.equalsIgnoreCase("deathMessageVisibility")))
/*      */       {
/*  492 */         throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */       }
/*  494 */       if (args.length == 4)
/*      */       {
/*  496 */         if (s.equalsIgnoreCase("color"))
/*      */         {
/*  498 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
/*      */         }
/*  500 */         if ((!s.equalsIgnoreCase("friendlyfire")) && (!s.equalsIgnoreCase("seeFriendlyInvisibles")))
/*      */         {
/*  502 */           if ((!s.equalsIgnoreCase("nametagVisibility")) && (!s.equalsIgnoreCase("deathMessageVisibility")))
/*      */           {
/*  504 */             throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
/*      */           }
/*      */           
/*      */ 
/*  508 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  513 */         throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  518 */       String s1 = args[index];
/*      */       
/*  520 */       if (s.equalsIgnoreCase("color"))
/*      */       {
/*  522 */         EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s1);
/*      */         
/*  524 */         if ((enumchatformatting == null) || (enumchatformatting.isFancyStyling()))
/*      */         {
/*  526 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
/*      */         }
/*      */         
/*  529 */         scoreplayerteam.setChatFormat(enumchatformatting);
/*  530 */         scoreplayerteam.setNamePrefix(enumchatformatting.toString());
/*  531 */         scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
/*      */       }
/*  533 */       else if (s.equalsIgnoreCase("friendlyfire"))
/*      */       {
/*  535 */         if ((!s1.equalsIgnoreCase("true")) && (!s1.equalsIgnoreCase("false")))
/*      */         {
/*  537 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */         }
/*      */         
/*  540 */         scoreplayerteam.setAllowFriendlyFire(s1.equalsIgnoreCase("true"));
/*      */       }
/*  542 */       else if (s.equalsIgnoreCase("seeFriendlyInvisibles"))
/*      */       {
/*  544 */         if ((!s1.equalsIgnoreCase("true")) && (!s1.equalsIgnoreCase("false")))
/*      */         {
/*  546 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(Arrays.asList(new String[] { "true", "false" })) });
/*      */         }
/*      */         
/*  549 */         scoreplayerteam.setSeeFriendlyInvisiblesEnabled(s1.equalsIgnoreCase("true"));
/*      */       }
/*  551 */       else if (s.equalsIgnoreCase("nametagVisibility"))
/*      */       {
/*  553 */         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(s1);
/*      */         
/*  555 */         if (team$enumvisible == null)
/*      */         {
/*  557 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         }
/*      */         
/*  560 */         scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*      */       }
/*  562 */       else if (s.equalsIgnoreCase("deathMessageVisibility"))
/*      */       {
/*  564 */         Team.EnumVisible team$enumvisible1 = Team.EnumVisible.func_178824_a(s1);
/*      */         
/*  566 */         if (team$enumvisible1 == null)
/*      */         {
/*  568 */           throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString(Team.EnumVisible.func_178825_a()) });
/*      */         }
/*      */         
/*  571 */         scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
/*      */       }
/*      */       
/*  574 */       notifyOperators(sender, this, "commands.scoreboard.teams.option.success", new Object[] { s, scoreplayerteam.getRegisteredName(), s1 });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void removeTeam(ICommandSender p_147194_1_, String[] p_147194_2_, int p_147194_3_)
/*      */     throws CommandException
/*      */   {
/*  581 */     Scoreboard scoreboard = getScoreboard();
/*  582 */     ScorePlayerTeam scoreplayerteam = getTeam(p_147194_2_[p_147194_3_]);
/*      */     
/*  584 */     if (scoreplayerteam != null)
/*      */     {
/*  586 */       scoreboard.removeTeam(scoreplayerteam);
/*  587 */       notifyOperators(p_147194_1_, this, "commands.scoreboard.teams.remove.success", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void listTeams(ICommandSender p_147186_1_, String[] p_147186_2_, int p_147186_3_) throws CommandException
/*      */   {
/*  593 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  595 */     if (p_147186_2_.length > p_147186_3_)
/*      */     {
/*  597 */       ScorePlayerTeam scoreplayerteam = getTeam(p_147186_2_[p_147186_3_]);
/*      */       
/*  599 */       if (scoreplayerteam == null)
/*      */       {
/*  601 */         return;
/*      */       }
/*      */       
/*  604 */       Collection<String> collection = scoreplayerteam.getMembershipCollection();
/*  605 */       p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
/*      */       
/*  607 */       if (collection.size() <= 0)
/*      */       {
/*  609 */         throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */       }
/*      */       
/*  612 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { Integer.valueOf(collection.size()), scoreplayerteam.getRegisteredName() });
/*  613 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  614 */       p_147186_1_.addChatMessage(chatcomponenttranslation);
/*  615 */       p_147186_1_.addChatMessage(new ChatComponentText(joinNiceString(collection.toArray())));
/*      */     }
/*      */     else
/*      */     {
/*  619 */       Collection<ScorePlayerTeam> collection1 = scoreboard.getTeams();
/*  620 */       p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection1.size());
/*      */       
/*  622 */       if (collection1.size() <= 0)
/*      */       {
/*  624 */         throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
/*      */       }
/*      */       
/*  627 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(collection1.size()) });
/*  628 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  629 */       p_147186_1_.addChatMessage(chatcomponenttranslation1);
/*      */       
/*  631 */       for (ScorePlayerTeam scoreplayerteam1 : collection1)
/*      */       {
/*  633 */         p_147186_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreplayerteam1.getRegisteredName(), scoreplayerteam1.getTeamName(), Integer.valueOf(scoreplayerteam1.getMembershipCollection().size()) }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void joinTeam(ICommandSender p_147190_1_, String[] p_147190_2_, int p_147190_3_) throws CommandException
/*      */   {
/*  640 */     Scoreboard scoreboard = getScoreboard();
/*  641 */     String s = p_147190_2_[(p_147190_3_++)];
/*  642 */     Set<String> set = Sets.newHashSet();
/*  643 */     Set<String> set1 = Sets.newHashSet();
/*      */     
/*  645 */     if (((p_147190_1_ instanceof EntityPlayer)) && (p_147190_3_ == p_147190_2_.length))
/*      */     {
/*  647 */       s4 = getCommandSenderAsPlayer(p_147190_1_).getName();
/*      */       
/*  649 */       if (scoreboard.addPlayerToTeam(s4, s))
/*      */       {
/*  651 */         set.add(s4);
/*      */       }
/*      */       else
/*      */       {
/*  655 */         set1.add(s4);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  660 */       while (p_147190_3_ < p_147190_2_.length) {
/*      */         String s4;
/*  662 */         String s1 = p_147190_2_[(p_147190_3_++)];
/*      */         
/*  664 */         if (s1.startsWith("@"))
/*      */         {
/*  666 */           for (Entity entity : func_175763_c(p_147190_1_, s1))
/*      */           {
/*  668 */             String s3 = getEntityName(p_147190_1_, entity.getUniqueID().toString());
/*      */             
/*  670 */             if (scoreboard.addPlayerToTeam(s3, s))
/*      */             {
/*  672 */               set.add(s3);
/*      */             }
/*      */             else
/*      */             {
/*  676 */               set1.add(s3);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  682 */           String s2 = getEntityName(p_147190_1_, s1);
/*      */           
/*  684 */           if (scoreboard.addPlayerToTeam(s2, s))
/*      */           {
/*  686 */             set.add(s2);
/*      */           }
/*      */           else
/*      */           {
/*  690 */             set1.add(s2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  696 */     if (!set.isEmpty())
/*      */     {
/*  698 */       p_147190_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
/*  699 */       notifyOperators(p_147190_1_, this, "commands.scoreboard.teams.join.success", new Object[] { Integer.valueOf(set.size()), s, joinNiceString(set.toArray(new String[set.size()])) });
/*      */     }
/*      */     
/*  702 */     if (!set1.isEmpty())
/*      */     {
/*  704 */       throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { Integer.valueOf(set1.size()), s, joinNiceString(set1.toArray(new String[set1.size()])) });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void leaveTeam(ICommandSender p_147199_1_, String[] p_147199_2_, int p_147199_3_) throws CommandException
/*      */   {
/*  710 */     Scoreboard scoreboard = getScoreboard();
/*  711 */     Set<String> set = Sets.newHashSet();
/*  712 */     Set<String> set1 = Sets.newHashSet();
/*      */     
/*  714 */     if (((p_147199_1_ instanceof EntityPlayer)) && (p_147199_3_ == p_147199_2_.length))
/*      */     {
/*  716 */       s3 = getCommandSenderAsPlayer(p_147199_1_).getName();
/*      */       
/*  718 */       if (scoreboard.removePlayerFromTeams(s3))
/*      */       {
/*  720 */         set.add(s3);
/*      */       }
/*      */       else
/*      */       {
/*  724 */         set1.add(s3);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  729 */       while (p_147199_3_ < p_147199_2_.length) {
/*      */         String s3;
/*  731 */         String s = p_147199_2_[(p_147199_3_++)];
/*      */         
/*  733 */         if (s.startsWith("@"))
/*      */         {
/*  735 */           for (Entity entity : func_175763_c(p_147199_1_, s))
/*      */           {
/*  737 */             String s2 = getEntityName(p_147199_1_, entity.getUniqueID().toString());
/*      */             
/*  739 */             if (scoreboard.removePlayerFromTeams(s2))
/*      */             {
/*  741 */               set.add(s2);
/*      */             }
/*      */             else
/*      */             {
/*  745 */               set1.add(s2);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  751 */           String s1 = getEntityName(p_147199_1_, s);
/*      */           
/*  753 */           if (scoreboard.removePlayerFromTeams(s1))
/*      */           {
/*  755 */             set.add(s1);
/*      */           }
/*      */           else
/*      */           {
/*  759 */             set1.add(s1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  765 */     if (!set.isEmpty())
/*      */     {
/*  767 */       p_147199_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
/*  768 */       notifyOperators(p_147199_1_, this, "commands.scoreboard.teams.leave.success", new Object[] { Integer.valueOf(set.size()), joinNiceString(set.toArray(new String[set.size()])) });
/*      */     }
/*      */     
/*  771 */     if (!set1.isEmpty())
/*      */     {
/*  773 */       throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { Integer.valueOf(set1.size()), joinNiceString(set1.toArray(new String[set1.size()])) });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void emptyTeam(ICommandSender p_147188_1_, String[] p_147188_2_, int p_147188_3_) throws CommandException
/*      */   {
/*  779 */     Scoreboard scoreboard = getScoreboard();
/*  780 */     ScorePlayerTeam scoreplayerteam = getTeam(p_147188_2_[p_147188_3_]);
/*      */     
/*  782 */     if (scoreplayerteam != null)
/*      */     {
/*  784 */       Collection<String> collection = Lists.newArrayList(scoreplayerteam.getMembershipCollection());
/*  785 */       p_147188_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());
/*      */       
/*  787 */       if (collection.isEmpty())
/*      */       {
/*  789 */         throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreplayerteam.getRegisteredName() });
/*      */       }
/*      */       
/*      */ 
/*  793 */       for (String s : collection)
/*      */       {
/*  795 */         scoreboard.removePlayerFromTeam(s, scoreplayerteam);
/*      */       }
/*      */       
/*  798 */       notifyOperators(p_147188_1_, this, "commands.scoreboard.teams.empty.success", new Object[] { Integer.valueOf(collection.size()), scoreplayerteam.getRegisteredName() });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void removeObjective(ICommandSender p_147191_1_, String p_147191_2_)
/*      */     throws CommandException
/*      */   {
/*  805 */     Scoreboard scoreboard = getScoreboard();
/*  806 */     ScoreObjective scoreobjective = getObjective(p_147191_2_, false);
/*  807 */     scoreboard.removeObjective(scoreobjective);
/*  808 */     notifyOperators(p_147191_1_, this, "commands.scoreboard.objectives.remove.success", new Object[] { p_147191_2_ });
/*      */   }
/*      */   
/*      */   protected void listObjectives(ICommandSender p_147196_1_) throws CommandException
/*      */   {
/*  813 */     Scoreboard scoreboard = getScoreboard();
/*  814 */     Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();
/*      */     
/*  816 */     if (collection.size() <= 0)
/*      */     {
/*  818 */       throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  822 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(collection.size()) });
/*  823 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  824 */     p_147196_1_.addChatMessage(chatcomponenttranslation);
/*      */     
/*  826 */     for (ScoreObjective scoreobjective : collection)
/*      */     {
/*  828 */       p_147196_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName() }));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setObjectiveDisplay(ICommandSender p_147198_1_, String[] p_147198_2_, int p_147198_3_)
/*      */     throws CommandException
/*      */   {
/*  835 */     Scoreboard scoreboard = getScoreboard();
/*  836 */     String s = p_147198_2_[(p_147198_3_++)];
/*  837 */     int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
/*  838 */     ScoreObjective scoreobjective = null;
/*      */     
/*  840 */     if (p_147198_2_.length == 4)
/*      */     {
/*  842 */       scoreobjective = getObjective(p_147198_2_[p_147198_3_], false);
/*      */     }
/*      */     
/*  845 */     if (i < 0)
/*      */     {
/*  847 */       throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s });
/*      */     }
/*      */     
/*      */ 
/*  851 */     scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
/*      */     
/*  853 */     if (scoreobjective != null)
/*      */     {
/*  855 */       notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] { Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName() });
/*      */     }
/*      */     else
/*      */     {
/*  859 */       notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] { Scoreboard.getObjectiveDisplaySlot(i) });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void listPlayers(ICommandSender p_147195_1_, String[] p_147195_2_, int p_147195_3_)
/*      */     throws CommandException
/*      */   {
/*  866 */     Scoreboard scoreboard = getScoreboard();
/*      */     
/*  868 */     if (p_147195_2_.length > p_147195_3_)
/*      */     {
/*  870 */       String s = getEntityName(p_147195_1_, p_147195_2_[p_147195_3_]);
/*  871 */       Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
/*  872 */       p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
/*      */       
/*  874 */       if (map.size() <= 0)
/*      */       {
/*  876 */         throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s });
/*      */       }
/*      */       
/*  879 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { Integer.valueOf(map.size()), s });
/*  880 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  881 */       p_147195_1_.addChatMessage(chatcomponenttranslation);
/*      */       
/*  883 */       for (Score score : map.values())
/*      */       {
/*  885 */         p_147195_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { Integer.valueOf(score.getScorePoints()), score.getObjective().getDisplayName(), score.getObjective().getName() }));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  890 */       Collection<String> collection = scoreboard.getObjectiveNames();
/*  891 */       p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
/*      */       
/*  893 */       if (collection.size() <= 0)
/*      */       {
/*  895 */         throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
/*      */       }
/*      */       
/*  898 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(collection.size()) });
/*  899 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  900 */       p_147195_1_.addChatMessage(chatcomponenttranslation1);
/*  901 */       p_147195_1_.addChatMessage(new ChatComponentText(joinNiceString(collection.toArray())));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setPlayer(ICommandSender p_147197_1_, String[] p_147197_2_, int p_147197_3_) throws CommandException
/*      */   {
/*  907 */     String s = p_147197_2_[(p_147197_3_ - 1)];
/*  908 */     int i = p_147197_3_;
/*  909 */     String s1 = getEntityName(p_147197_1_, p_147197_2_[(p_147197_3_++)]);
/*      */     
/*  911 */     if (s1.length() > 40)
/*      */     {
/*  913 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s1, Integer.valueOf(40) });
/*      */     }
/*      */     
/*      */ 
/*  917 */     ScoreObjective scoreobjective = getObjective(p_147197_2_[(p_147197_3_++)], true);
/*  918 */     int j = s.equalsIgnoreCase("set") ? parseInt(p_147197_2_[(p_147197_3_++)]) : parseInt(p_147197_2_[(p_147197_3_++)], 0);
/*      */     
/*  920 */     if (p_147197_2_.length > p_147197_3_)
/*      */     {
/*  922 */       Entity entity = func_175768_b(p_147197_1_, p_147197_2_[i]);
/*      */       
/*      */       try
/*      */       {
/*  926 */         NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(buildString(p_147197_2_, p_147197_3_));
/*  927 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  928 */         entity.writeToNBT(nbttagcompound1);
/*      */         
/*  930 */         if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true))
/*      */         {
/*  932 */           throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s1 });
/*      */         }
/*      */       }
/*      */       catch (NBTException nbtexception)
/*      */       {
/*  937 */         throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { nbtexception.getMessage() });
/*      */       }
/*      */     }
/*      */     
/*  941 */     Scoreboard scoreboard = getScoreboard();
/*  942 */     Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
/*      */     
/*  944 */     if (s.equalsIgnoreCase("set"))
/*      */     {
/*  946 */       score.setScorePoints(j);
/*      */     }
/*  948 */     else if (s.equalsIgnoreCase("add"))
/*      */     {
/*  950 */       score.increseScore(j);
/*      */     }
/*      */     else
/*      */     {
/*  954 */       score.decreaseScore(j);
/*      */     }
/*      */     
/*  957 */     notifyOperators(p_147197_1_, this, "commands.scoreboard.players.set.success", new Object[] { scoreobjective.getName(), s1, Integer.valueOf(score.getScorePoints()) });
/*      */   }
/*      */   
/*      */   protected void resetPlayers(ICommandSender p_147187_1_, String[] p_147187_2_, int p_147187_3_)
/*      */     throws CommandException
/*      */   {
/*  963 */     Scoreboard scoreboard = getScoreboard();
/*  964 */     String s = getEntityName(p_147187_1_, p_147187_2_[(p_147187_3_++)]);
/*      */     
/*  966 */     if (p_147187_2_.length > p_147187_3_)
/*      */     {
/*  968 */       ScoreObjective scoreobjective = getObjective(p_147187_2_[(p_147187_3_++)], false);
/*  969 */       scoreboard.removeObjectiveFromEntity(s, scoreobjective);
/*  970 */       notifyOperators(p_147187_1_, this, "commands.scoreboard.players.resetscore.success", new Object[] { scoreobjective.getName(), s });
/*      */     }
/*      */     else
/*      */     {
/*  974 */       scoreboard.removeObjectiveFromEntity(s, null);
/*  975 */       notifyOperators(p_147187_1_, this, "commands.scoreboard.players.reset.success", new Object[] { s });
/*      */     }
/*      */   }
/*      */   
/*      */   protected void func_175779_n(ICommandSender p_175779_1_, String[] p_175779_2_, int p_175779_3_) throws CommandException
/*      */   {
/*  981 */     Scoreboard scoreboard = getScoreboard();
/*  982 */     String s = getPlayerName(p_175779_1_, p_175779_2_[(p_175779_3_++)]);
/*      */     
/*  984 */     if (s.length() > 40)
/*      */     {
/*  986 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/*      */     
/*      */ 
/*  990 */     ScoreObjective scoreobjective = getObjective(p_175779_2_[p_175779_3_], false);
/*      */     
/*  992 */     if (scoreobjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER)
/*      */     {
/*  994 */       throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreobjective.getName() });
/*      */     }
/*      */     
/*      */ 
/*  998 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*  999 */     score.setLocked(false);
/* 1000 */     notifyOperators(p_175779_1_, this, "commands.scoreboard.players.enable.success", new Object[] { scoreobjective.getName(), s });
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_175781_o(ICommandSender p_175781_1_, String[] p_175781_2_, int p_175781_3_)
/*      */     throws CommandException
/*      */   {
/* 1007 */     Scoreboard scoreboard = getScoreboard();
/* 1008 */     String s = getEntityName(p_175781_1_, p_175781_2_[(p_175781_3_++)]);
/*      */     
/* 1010 */     if (s.length() > 40)
/*      */     {
/* 1012 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/*      */     
/*      */ 
/* 1016 */     ScoreObjective scoreobjective = getObjective(p_175781_2_[(p_175781_3_++)], false);
/*      */     
/* 1018 */     if (!scoreboard.entityHasObjective(s, scoreobjective))
/*      */     {
/* 1020 */       throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreobjective.getName(), s });
/*      */     }
/*      */     
/*      */ 
/* 1024 */     int i = p_175781_2_[p_175781_3_].equals("*") ? Integer.MIN_VALUE : parseInt(p_175781_2_[p_175781_3_]);
/* 1025 */     p_175781_3_++;
/* 1026 */     int j = (p_175781_3_ < p_175781_2_.length) && (!p_175781_2_[p_175781_3_].equals("*")) ? parseInt(p_175781_2_[p_175781_3_], i) : Integer.MAX_VALUE;
/* 1027 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*      */     
/* 1029 */     if ((score.getScorePoints() >= i) && (score.getScorePoints() <= j))
/*      */     {
/* 1031 */       notifyOperators(p_175781_1_, this, "commands.scoreboard.players.test.success", new Object[] { Integer.valueOf(score.getScorePoints()), Integer.valueOf(i), Integer.valueOf(j) });
/*      */     }
/*      */     else
/*      */     {
/* 1035 */       throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { Integer.valueOf(score.getScorePoints()), Integer.valueOf(i), Integer.valueOf(j) });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_175778_p(ICommandSender p_175778_1_, String[] p_175778_2_, int p_175778_3_)
/*      */     throws CommandException
/*      */   {
/* 1043 */     Scoreboard scoreboard = getScoreboard();
/* 1044 */     String s = getEntityName(p_175778_1_, p_175778_2_[(p_175778_3_++)]);
/* 1045 */     ScoreObjective scoreobjective = getObjective(p_175778_2_[(p_175778_3_++)], true);
/* 1046 */     String s1 = p_175778_2_[(p_175778_3_++)];
/* 1047 */     String s2 = getEntityName(p_175778_1_, p_175778_2_[(p_175778_3_++)]);
/* 1048 */     ScoreObjective scoreobjective1 = getObjective(p_175778_2_[p_175778_3_], false);
/*      */     
/* 1050 */     if (s.length() > 40)
/*      */     {
/* 1052 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40) });
/*      */     }
/* 1054 */     if (s2.length() > 40)
/*      */     {
/* 1056 */       throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, Integer.valueOf(40) });
/*      */     }
/*      */     
/*      */ 
/* 1060 */     Score score = scoreboard.getValueFromObjective(s, scoreobjective);
/*      */     
/* 1062 */     if (!scoreboard.entityHasObjective(s2, scoreobjective1))
/*      */     {
/* 1064 */       throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreobjective1.getName(), s2 });
/*      */     }
/*      */     
/*      */ 
/* 1068 */     Score score1 = scoreboard.getValueFromObjective(s2, scoreobjective1);
/*      */     
/* 1070 */     if (s1.equals("+="))
/*      */     {
/* 1072 */       score.setScorePoints(score.getScorePoints() + score1.getScorePoints());
/*      */     }
/* 1074 */     else if (s1.equals("-="))
/*      */     {
/* 1076 */       score.setScorePoints(score.getScorePoints() - score1.getScorePoints());
/*      */     }
/* 1078 */     else if (s1.equals("*="))
/*      */     {
/* 1080 */       score.setScorePoints(score.getScorePoints() * score1.getScorePoints());
/*      */     }
/* 1082 */     else if (s1.equals("/="))
/*      */     {
/* 1084 */       if (score1.getScorePoints() != 0)
/*      */       {
/* 1086 */         score.setScorePoints(score.getScorePoints() / score1.getScorePoints());
/*      */       }
/*      */     }
/* 1089 */     else if (s1.equals("%="))
/*      */     {
/* 1091 */       if (score1.getScorePoints() != 0)
/*      */       {
/* 1093 */         score.setScorePoints(score.getScorePoints() % score1.getScorePoints());
/*      */       }
/*      */     }
/* 1096 */     else if (s1.equals("="))
/*      */     {
/* 1098 */       score.setScorePoints(score1.getScorePoints());
/*      */     }
/* 1100 */     else if (s1.equals("<"))
/*      */     {
/* 1102 */       score.setScorePoints(Math.min(score.getScorePoints(), score1.getScorePoints()));
/*      */     }
/* 1104 */     else if (s1.equals(">"))
/*      */     {
/* 1106 */       score.setScorePoints(Math.max(score.getScorePoints(), score1.getScorePoints()));
/*      */     }
/*      */     else
/*      */     {
/* 1110 */       if (!s1.equals("><"))
/*      */       {
/* 1112 */         throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s1 });
/*      */       }
/*      */       
/* 1115 */       int i = score.getScorePoints();
/* 1116 */       score.setScorePoints(score1.getScorePoints());
/* 1117 */       score1.setScorePoints(i);
/*      */     }
/*      */     
/* 1120 */     notifyOperators(p_175778_1_, this, "commands.scoreboard.players.operation.success", new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*      */   {
/* 1127 */     if (args.length == 1)
/*      */     {
/* 1129 */       return getListOfStringsMatchingLastWord(args, new String[] { "objectives", "players", "teams" });
/*      */     }
/*      */     
/*      */ 
/* 1133 */     if (args[0].equalsIgnoreCase("objectives"))
/*      */     {
/* 1135 */       if (args.length == 2)
/*      */       {
/* 1137 */         return getListOfStringsMatchingLastWord(args, new String[] { "list", "add", "remove", "setdisplay" });
/*      */       }
/*      */       
/* 1140 */       if (args[1].equalsIgnoreCase("add"))
/*      */       {
/* 1142 */         if (args.length == 4)
/*      */         {
/* 1144 */           Set<String> set = IScoreObjectiveCriteria.INSTANCES.keySet();
/* 1145 */           return getListOfStringsMatchingLastWord(args, set);
/*      */         }
/*      */       }
/* 1148 */       else if (args[1].equalsIgnoreCase("remove"))
/*      */       {
/* 1150 */         if (args.length == 3)
/*      */         {
/* 1152 */           return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */         }
/*      */       }
/* 1155 */       else if (args[1].equalsIgnoreCase("setdisplay"))
/*      */       {
/* 1157 */         if (args.length == 3)
/*      */         {
/* 1159 */           return getListOfStringsMatchingLastWord(args, Scoreboard.getDisplaySlotStrings());
/*      */         }
/*      */         
/* 1162 */         if (args.length == 4)
/*      */         {
/* 1164 */           return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */         }
/*      */       }
/*      */     }
/* 1168 */     else if (args[0].equalsIgnoreCase("players"))
/*      */     {
/* 1170 */       if (args.length == 2)
/*      */       {
/* 1172 */         return getListOfStringsMatchingLastWord(args, new String[] { "set", "add", "remove", "reset", "list", "enable", "test", "operation" });
/*      */       }
/*      */       
/* 1175 */       if ((!args[1].equalsIgnoreCase("set")) && (!args[1].equalsIgnoreCase("add")) && (!args[1].equalsIgnoreCase("remove")) && (!args[1].equalsIgnoreCase("reset")))
/*      */       {
/* 1177 */         if (args[1].equalsIgnoreCase("enable"))
/*      */         {
/* 1179 */           if (args.length == 3)
/*      */           {
/* 1181 */             return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */           }
/*      */           
/* 1184 */           if (args.length == 4)
/*      */           {
/* 1186 */             return getListOfStringsMatchingLastWord(args, func_175782_e());
/*      */           }
/*      */         }
/* 1189 */         else if ((!args[1].equalsIgnoreCase("list")) && (!args[1].equalsIgnoreCase("test")))
/*      */         {
/* 1191 */           if (args[1].equalsIgnoreCase("operation"))
/*      */           {
/* 1193 */             if (args.length == 3)
/*      */             {
/* 1195 */               return getListOfStringsMatchingLastWord(args, getScoreboard().getObjectiveNames());
/*      */             }
/*      */             
/* 1198 */             if (args.length == 4)
/*      */             {
/* 1200 */               return getListOfStringsMatchingLastWord(args, func_147184_a(true));
/*      */             }
/*      */             
/* 1203 */             if (args.length == 5)
/*      */             {
/* 1205 */               return getListOfStringsMatchingLastWord(args, new String[] { "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><" });
/*      */             }
/*      */             
/* 1208 */             if (args.length == 6)
/*      */             {
/* 1210 */               return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */             }
/*      */             
/* 1213 */             if (args.length == 7)
/*      */             {
/* 1215 */               return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1221 */           if (args.length == 3)
/*      */           {
/* 1223 */             return getListOfStringsMatchingLastWord(args, getScoreboard().getObjectiveNames());
/*      */           }
/*      */           
/* 1226 */           if ((args.length == 4) && (args[1].equalsIgnoreCase("test")))
/*      */           {
/* 1228 */             return getListOfStringsMatchingLastWord(args, func_147184_a(false));
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1234 */         if (args.length == 3)
/*      */         {
/* 1236 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */         
/* 1239 */         if (args.length == 4)
/*      */         {
/* 1241 */           return getListOfStringsMatchingLastWord(args, func_147184_a(true));
/*      */         }
/*      */       }
/*      */     }
/* 1245 */     else if (args[0].equalsIgnoreCase("teams"))
/*      */     {
/* 1247 */       if (args.length == 2)
/*      */       {
/* 1249 */         return getListOfStringsMatchingLastWord(args, new String[] { "add", "remove", "join", "leave", "empty", "list", "option" });
/*      */       }
/*      */       
/* 1252 */       if (args[1].equalsIgnoreCase("join"))
/*      */       {
/* 1254 */         if (args.length == 3)
/*      */         {
/* 1256 */           return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */         }
/*      */         
/* 1259 */         if (args.length >= 4)
/*      */         {
/* 1261 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1266 */         if (args[1].equalsIgnoreCase("leave"))
/*      */         {
/* 1268 */           return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*      */         }
/*      */         
/* 1271 */         if ((!args[1].equalsIgnoreCase("empty")) && (!args[1].equalsIgnoreCase("list")) && (!args[1].equalsIgnoreCase("remove")))
/*      */         {
/* 1273 */           if (args[1].equalsIgnoreCase("option"))
/*      */           {
/* 1275 */             if (args.length == 3)
/*      */             {
/* 1277 */               return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */             }
/*      */             
/* 1280 */             if (args.length == 4)
/*      */             {
/* 1282 */               return getListOfStringsMatchingLastWord(args, new String[] { "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility" });
/*      */             }
/*      */             
/* 1285 */             if (args.length == 5)
/*      */             {
/* 1287 */               if (args[3].equalsIgnoreCase("color"))
/*      */               {
/* 1289 */                 return getListOfStringsMatchingLastWord(args, EnumChatFormatting.getValidValues(true, false));
/*      */               }
/*      */               
/* 1292 */               if ((args[3].equalsIgnoreCase("nametagVisibility")) || (args[3].equalsIgnoreCase("deathMessageVisibility")))
/*      */               {
/* 1294 */                 return getListOfStringsMatchingLastWord(args, Team.EnumVisible.func_178825_a());
/*      */               }
/*      */               
/* 1297 */               if ((args[3].equalsIgnoreCase("friendlyfire")) || (args[3].equalsIgnoreCase("seeFriendlyInvisibles")))
/*      */               {
/* 1299 */                 return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1304 */         else if (args.length == 3)
/*      */         {
/* 1306 */           return getListOfStringsMatchingLastWord(args, getScoreboard().getTeamNames());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1311 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected List<String> func_147184_a(boolean p_147184_1_)
/*      */   {
/* 1317 */     Collection<ScoreObjective> collection = getScoreboard().getScoreObjectives();
/* 1318 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1320 */     for (ScoreObjective scoreobjective : collection)
/*      */     {
/* 1322 */       if ((!p_147184_1_) || (!scoreobjective.getCriteria().isReadOnly()))
/*      */       {
/* 1324 */         list.add(scoreobjective.getName());
/*      */       }
/*      */     }
/*      */     
/* 1328 */     return list;
/*      */   }
/*      */   
/*      */   protected List<String> func_175782_e()
/*      */   {
/* 1333 */     Collection<ScoreObjective> collection = getScoreboard().getScoreObjectives();
/* 1334 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1336 */     for (ScoreObjective scoreobjective : collection)
/*      */     {
/* 1338 */       if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
/*      */       {
/* 1340 */         list.add(scoreobjective.getName());
/*      */       }
/*      */     }
/*      */     
/* 1344 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUsernameIndex(String[] args, int index)
/*      */   {
/* 1352 */     return index == 2;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */