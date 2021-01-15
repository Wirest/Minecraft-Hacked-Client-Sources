/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public abstract class CommandBase implements ICommand
/*     */ {
/*     */   private static IAdminCommand theAdmin;
/*     */   
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  31 */     return 4;
/*     */   }
/*     */   
/*     */   public List<String> getCommandAliases()
/*     */   {
/*  36 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCommandSenderUseCommand(ICommandSender sender)
/*     */   {
/*  44 */     return sender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/*  49 */     return null;
/*     */   }
/*     */   
/*     */   public static int parseInt(String input) throws NumberInvalidException
/*     */   {
/*     */     try
/*     */     {
/*  56 */       return Integer.parseInt(input);
/*     */     }
/*     */     catch (NumberFormatException var2)
/*     */     {
/*  60 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     }
/*     */   }
/*     */   
/*     */   public static int parseInt(String input, int min) throws NumberInvalidException
/*     */   {
/*  66 */     return parseInt(input, min, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static int parseInt(String input, int min, int max) throws NumberInvalidException
/*     */   {
/*  71 */     int i = parseInt(input);
/*     */     
/*  73 */     if (i < min)
/*     */     {
/*  75 */       throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(i), Integer.valueOf(min) });
/*     */     }
/*  77 */     if (i > max)
/*     */     {
/*  79 */       throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(i), Integer.valueOf(max) });
/*     */     }
/*     */     
/*     */ 
/*  83 */     return i;
/*     */   }
/*     */   
/*     */   public static long parseLong(String input)
/*     */     throws NumberInvalidException
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return Long.parseLong(input);
/*     */     }
/*     */     catch (NumberFormatException var2)
/*     */     {
/*  95 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     }
/*     */   }
/*     */   
/*     */   public static long parseLong(String input, long min, long max) throws NumberInvalidException
/*     */   {
/* 101 */     long i = parseLong(input);
/*     */     
/* 103 */     if (i < min)
/*     */     {
/* 105 */       throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Long.valueOf(i), Long.valueOf(min) });
/*     */     }
/* 107 */     if (i > max)
/*     */     {
/* 109 */       throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Long.valueOf(i), Long.valueOf(max) });
/*     */     }
/*     */     
/*     */ 
/* 113 */     return i;
/*     */   }
/*     */   
/*     */   public static BlockPos parseBlockPos(ICommandSender sender, String[] args, int startIndex, boolean centerBlock)
/*     */     throws NumberInvalidException
/*     */   {
/* 119 */     BlockPos blockpos = sender.getPosition();
/* 120 */     return new BlockPos(parseDouble(blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), parseDouble(blockpos.getY(), args[(startIndex + 1)], 0, 256, false), parseDouble(blockpos.getZ(), args[(startIndex + 2)], -30000000, 30000000, centerBlock));
/*     */   }
/*     */   
/*     */   public static double parseDouble(String input) throws NumberInvalidException
/*     */   {
/*     */     try
/*     */     {
/* 127 */       double d0 = Double.parseDouble(input);
/*     */       
/* 129 */       if (!Doubles.isFinite(d0))
/*     */       {
/* 131 */         throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */       }
/*     */       
/*     */ 
/* 135 */       return d0;
/*     */ 
/*     */     }
/*     */     catch (NumberFormatException var3)
/*     */     {
/* 140 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
/*     */     }
/*     */   }
/*     */   
/*     */   public static double parseDouble(String input, double min) throws NumberInvalidException
/*     */   {
/* 146 */     return parseDouble(input, min, Double.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static double parseDouble(String input, double min, double max) throws NumberInvalidException
/*     */   {
/* 151 */     double d0 = parseDouble(input);
/*     */     
/* 153 */     if (d0 < min)
/*     */     {
/* 155 */       throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Double.valueOf(min) });
/*     */     }
/* 157 */     if (d0 > max)
/*     */     {
/* 159 */       throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Double.valueOf(max) });
/*     */     }
/*     */     
/*     */ 
/* 163 */     return d0;
/*     */   }
/*     */   
/*     */   public static boolean parseBoolean(String input)
/*     */     throws CommandException
/*     */   {
/* 169 */     if ((!input.equals("true")) && (!input.equals("1")))
/*     */     {
/* 171 */       if ((!input.equals("false")) && (!input.equals("0")))
/*     */       {
/* 173 */         throw new CommandException("commands.generic.boolean.invalid", new Object[] { input });
/*     */       }
/*     */       
/*     */ 
/* 177 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 182 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender)
/*     */     throws PlayerNotFoundException
/*     */   {
/* 191 */     if ((sender instanceof EntityPlayerMP))
/*     */     {
/* 193 */       return (EntityPlayerMP)sender;
/*     */     }
/*     */     
/*     */ 
/* 197 */     throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
/*     */   }
/*     */   
/*     */   public static EntityPlayerMP getPlayer(ICommandSender sender, String username)
/*     */     throws PlayerNotFoundException
/*     */   {
/* 203 */     EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(sender, username);
/*     */     
/* 205 */     if (entityplayermp == null)
/*     */     {
/*     */       try
/*     */       {
/* 209 */         entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(UUID.fromString(username));
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 217 */     if (entityplayermp == null)
/*     */     {
/* 219 */       entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
/*     */     }
/*     */     
/* 222 */     if (entityplayermp == null)
/*     */     {
/* 224 */       throw new PlayerNotFoundException();
/*     */     }
/*     */     
/*     */ 
/* 228 */     return entityplayermp;
/*     */   }
/*     */   
/*     */   public static Entity func_175768_b(ICommandSender p_175768_0_, String p_175768_1_)
/*     */     throws EntityNotFoundException
/*     */   {
/* 234 */     return getEntity(p_175768_0_, p_175768_1_, Entity.class);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> T getEntity(ICommandSender commandSender, String p_175759_1_, Class<? extends T> p_175759_2_) throws EntityNotFoundException
/*     */   {
/* 239 */     Entity entity = PlayerSelector.matchOneEntity(commandSender, p_175759_1_, p_175759_2_);
/* 240 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/* 242 */     if (entity == null)
/*     */     {
/* 244 */       entity = minecraftserver.getConfigurationManager().getPlayerByUsername(p_175759_1_);
/*     */     }
/*     */     
/* 247 */     if (entity == null)
/*     */     {
/*     */       try
/*     */       {
/* 251 */         UUID uuid = UUID.fromString(p_175759_1_);
/* 252 */         entity = minecraftserver.getEntityFromUuid(uuid);
/*     */         
/* 254 */         if (entity == null)
/*     */         {
/* 256 */           entity = minecraftserver.getConfigurationManager().getPlayerByUUID(uuid);
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException var6)
/*     */       {
/* 261 */         throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/* 265 */     if ((entity != null) && (p_175759_2_.isAssignableFrom(entity.getClass())))
/*     */     {
/* 267 */       return entity;
/*     */     }
/*     */     
/*     */ 
/* 271 */     throw new EntityNotFoundException();
/*     */   }
/*     */   
/*     */   public static List<Entity> func_175763_c(ICommandSender p_175763_0_, String p_175763_1_)
/*     */     throws EntityNotFoundException
/*     */   {
/* 277 */     return PlayerSelector.hasArguments(p_175763_1_) ? PlayerSelector.matchEntities(p_175763_0_, p_175763_1_, Entity.class) : Lists.newArrayList(new Entity[] { func_175768_b(p_175763_0_, p_175763_1_) });
/*     */   }
/*     */   
/*     */   public static String getPlayerName(ICommandSender sender, String query) throws PlayerNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 284 */       return getPlayer(sender, query).getName();
/*     */     }
/*     */     catch (PlayerNotFoundException playernotfoundexception)
/*     */     {
/* 288 */       if (PlayerSelector.hasArguments(query))
/*     */       {
/* 290 */         throw playernotfoundexception;
/*     */       }
/*     */     }
/*     */     
/* 294 */     return query;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getEntityName(ICommandSender p_175758_0_, String p_175758_1_)
/*     */     throws EntityNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 307 */       return getPlayer(p_175758_0_, p_175758_1_).getName();
/*     */     }
/*     */     catch (PlayerNotFoundException var5)
/*     */     {
/*     */       try
/*     */       {
/* 313 */         return func_175768_b(p_175758_0_, p_175758_1_).getUniqueID().toString();
/*     */       }
/*     */       catch (EntityNotFoundException entitynotfoundexception)
/*     */       {
/* 317 */         if (PlayerSelector.hasArguments(p_175758_1_))
/*     */         {
/* 319 */           throw entitynotfoundexception;
/*     */         }
/*     */       }
/*     */     }
/* 323 */     return p_175758_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int p_147178_2_)
/*     */     throws CommandException, PlayerNotFoundException
/*     */   {
/* 331 */     return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
/*     */   }
/*     */   
/*     */   public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_) throws PlayerNotFoundException
/*     */   {
/* 336 */     IChatComponent ichatcomponent = new ChatComponentText("");
/*     */     
/* 338 */     for (int i = index; i < args.length; i++)
/*     */     {
/* 340 */       if (i > index)
/*     */       {
/* 342 */         ichatcomponent.appendText(" ");
/*     */       }
/*     */       
/* 345 */       IChatComponent ichatcomponent1 = new ChatComponentText(args[i]);
/*     */       
/* 347 */       if (p_147176_3_)
/*     */       {
/* 349 */         IChatComponent ichatcomponent2 = PlayerSelector.matchEntitiesToChatComponent(sender, args[i]);
/*     */         
/* 351 */         if (ichatcomponent2 == null)
/*     */         {
/* 353 */           if (PlayerSelector.hasArguments(args[i]))
/*     */           {
/* 355 */             throw new PlayerNotFoundException();
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 360 */           ichatcomponent1 = ichatcomponent2;
/*     */         }
/*     */       }
/*     */       
/* 364 */       ichatcomponent.appendSibling(ichatcomponent1);
/*     */     }
/*     */     
/* 367 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String buildString(String[] args, int startPos)
/*     */   {
/* 375 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 377 */     for (int i = startPos; i < args.length; i++)
/*     */     {
/* 379 */       if (i > startPos)
/*     */       {
/* 381 */         stringbuilder.append(" ");
/*     */       }
/*     */       
/* 384 */       String s = args[i];
/* 385 */       stringbuilder.append(s);
/*     */     }
/*     */     
/* 388 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   public static CoordinateArg parseCoordinate(double base, String p_175770_2_, boolean centerBlock) throws NumberInvalidException
/*     */   {
/* 393 */     return parseCoordinate(base, p_175770_2_, -30000000, 30000000, centerBlock);
/*     */   }
/*     */   
/*     */   public static CoordinateArg parseCoordinate(double p_175767_0_, String p_175767_2_, int min, int max, boolean centerBlock) throws NumberInvalidException
/*     */   {
/* 398 */     boolean flag = p_175767_2_.startsWith("~");
/*     */     
/* 400 */     if ((flag) && (Double.isNaN(p_175767_0_)))
/*     */     {
/* 402 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(p_175767_0_) });
/*     */     }
/*     */     
/*     */ 
/* 406 */     double d0 = 0.0D;
/*     */     
/* 408 */     if ((!flag) || (p_175767_2_.length() > 1))
/*     */     {
/* 410 */       boolean flag1 = p_175767_2_.contains(".");
/*     */       
/* 412 */       if (flag)
/*     */       {
/* 414 */         p_175767_2_ = p_175767_2_.substring(1);
/*     */       }
/*     */       
/* 417 */       d0 += parseDouble(p_175767_2_);
/*     */       
/* 419 */       if ((!flag1) && (!flag) && (centerBlock))
/*     */       {
/* 421 */         d0 += 0.5D;
/*     */       }
/*     */     }
/*     */     
/* 425 */     if ((min != 0) || (max != 0))
/*     */     {
/* 427 */       if (d0 < min)
/*     */       {
/* 429 */         throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Integer.valueOf(min) });
/*     */       }
/*     */       
/* 432 */       if (d0 > max)
/*     */       {
/* 434 */         throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Integer.valueOf(max) });
/*     */       }
/*     */     }
/*     */     
/* 438 */     return new CoordinateArg(d0 + (flag ? p_175767_0_ : 0.0D), d0, flag);
/*     */   }
/*     */   
/*     */   public static double parseDouble(double base, String input, boolean centerBlock)
/*     */     throws NumberInvalidException
/*     */   {
/* 444 */     return parseDouble(base, input, -30000000, 30000000, centerBlock);
/*     */   }
/*     */   
/*     */   public static double parseDouble(double base, String input, int min, int max, boolean centerBlock) throws NumberInvalidException
/*     */   {
/* 449 */     boolean flag = input.startsWith("~");
/*     */     
/* 451 */     if ((flag) && (Double.isNaN(base)))
/*     */     {
/* 453 */       throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(base) });
/*     */     }
/*     */     
/*     */ 
/* 457 */     double d0 = flag ? base : 0.0D;
/*     */     
/* 459 */     if ((!flag) || (input.length() > 1))
/*     */     {
/* 461 */       boolean flag1 = input.contains(".");
/*     */       
/* 463 */       if (flag)
/*     */       {
/* 465 */         input = input.substring(1);
/*     */       }
/*     */       
/* 468 */       d0 += parseDouble(input);
/*     */       
/* 470 */       if ((!flag1) && (!flag) && (centerBlock))
/*     */       {
/* 472 */         d0 += 0.5D;
/*     */       }
/*     */     }
/*     */     
/* 476 */     if ((min != 0) || (max != 0))
/*     */     {
/* 478 */       if (d0 < min)
/*     */       {
/* 480 */         throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { Double.valueOf(d0), Integer.valueOf(min) });
/*     */       }
/*     */       
/* 483 */       if (d0 > max)
/*     */       {
/* 485 */         throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { Double.valueOf(d0), Integer.valueOf(max) });
/*     */       }
/*     */     }
/*     */     
/* 489 */     return d0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Item getItemByText(ICommandSender sender, String id)
/*     */     throws NumberInvalidException
/*     */   {
/* 500 */     ResourceLocation resourcelocation = new ResourceLocation(id);
/* 501 */     Item item = (Item)Item.itemRegistry.getObject(resourcelocation);
/*     */     
/* 503 */     if (item == null)
/*     */     {
/* 505 */       throw new NumberInvalidException("commands.give.item.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */     
/*     */ 
/* 509 */     return item;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Block getBlockByText(ICommandSender sender, String id)
/*     */     throws NumberInvalidException
/*     */   {
/* 520 */     ResourceLocation resourcelocation = new ResourceLocation(id);
/*     */     
/* 522 */     if (!Block.blockRegistry.containsKey(resourcelocation))
/*     */     {
/* 524 */       throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */     
/*     */ 
/* 528 */     Block block = (Block)Block.blockRegistry.getObject(resourcelocation);
/*     */     
/* 530 */     if (block == null)
/*     */     {
/* 532 */       throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
/*     */     }
/*     */     
/*     */ 
/* 536 */     return block;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String joinNiceString(Object[] elements)
/*     */   {
/* 547 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 549 */     for (int i = 0; i < elements.length; i++)
/*     */     {
/* 551 */       String s = elements[i].toString();
/*     */       
/* 553 */       if (i > 0)
/*     */       {
/* 555 */         if (i == elements.length - 1)
/*     */         {
/* 557 */           stringbuilder.append(" and ");
/*     */         }
/*     */         else
/*     */         {
/* 561 */           stringbuilder.append(", ");
/*     */         }
/*     */       }
/*     */       
/* 565 */       stringbuilder.append(s);
/*     */     }
/*     */     
/* 568 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   public static IChatComponent join(List<IChatComponent> components)
/*     */   {
/* 573 */     IChatComponent ichatcomponent = new ChatComponentText("");
/*     */     
/* 575 */     for (int i = 0; i < components.size(); i++)
/*     */     {
/* 577 */       if (i > 0)
/*     */       {
/* 579 */         if (i == components.size() - 1)
/*     */         {
/* 581 */           ichatcomponent.appendText(" and ");
/*     */         }
/* 583 */         else if (i > 0)
/*     */         {
/* 585 */           ichatcomponent.appendText(", ");
/*     */         }
/*     */       }
/*     */       
/* 589 */       ichatcomponent.appendSibling((IChatComponent)components.get(i));
/*     */     }
/*     */     
/* 592 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String joinNiceStringFromCollection(Collection<String> strings)
/*     */   {
/* 602 */     return joinNiceString(strings.toArray(new String[strings.size()]));
/*     */   }
/*     */   
/*     */   public static List<String> func_175771_a(String[] p_175771_0_, int p_175771_1_, BlockPos p_175771_2_)
/*     */   {
/* 607 */     if (p_175771_2_ == null)
/*     */     {
/* 609 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 613 */     int i = p_175771_0_.length - 1;
/*     */     String s;
/*     */     String s;
/* 616 */     if (i == p_175771_1_)
/*     */     {
/* 618 */       s = Integer.toString(p_175771_2_.getX());
/*     */     } else { String s;
/* 620 */       if (i == p_175771_1_ + 1)
/*     */       {
/* 622 */         s = Integer.toString(p_175771_2_.getY());
/*     */       }
/*     */       else
/*     */       {
/* 626 */         if (i != p_175771_1_ + 2)
/*     */         {
/* 628 */           return null;
/*     */         }
/*     */         
/* 631 */         s = Integer.toString(p_175771_2_.getZ());
/*     */       }
/*     */     }
/* 634 */     return Lists.newArrayList(new String[] { s });
/*     */   }
/*     */   
/*     */ 
/*     */   public static List<String> func_181043_b(String[] p_181043_0_, int p_181043_1_, BlockPos p_181043_2_)
/*     */   {
/* 640 */     if (p_181043_2_ == null)
/*     */     {
/* 642 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 646 */     int i = p_181043_0_.length - 1;
/*     */     String s;
/*     */     String s;
/* 649 */     if (i == p_181043_1_)
/*     */     {
/* 651 */       s = Integer.toString(p_181043_2_.getX());
/*     */     }
/*     */     else
/*     */     {
/* 655 */       if (i != p_181043_1_ + 1)
/*     */       {
/* 657 */         return null;
/*     */       }
/*     */       
/* 660 */       s = Integer.toString(p_181043_2_.getZ());
/*     */     }
/*     */     
/* 663 */     return Lists.newArrayList(new String[] { s });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean doesStringStartWith(String original, String region)
/*     */   {
/* 672 */     return region.regionMatches(true, 0, original, 0, original.length());
/*     */   }
/*     */   
/*     */   public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities)
/*     */   {
/* 677 */     return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
/*     */   }
/*     */   
/*     */   public static List<String> getListOfStringsMatchingLastWord(String[] p_175762_0_, Collection<?> p_175762_1_)
/*     */   {
/* 682 */     String s = p_175762_0_[(p_175762_0_.length - 1)];
/* 683 */     List<String> list = Lists.newArrayList();
/*     */     
/* 685 */     if (!p_175762_1_.isEmpty())
/*     */     {
/* 687 */       for (String s1 : Iterables.transform(p_175762_1_, Functions.toStringFunction()))
/*     */       {
/* 689 */         if (doesStringStartWith(s, s1))
/*     */         {
/* 691 */           list.add(s1);
/*     */         }
/*     */       }
/*     */       
/* 695 */       if (list.isEmpty())
/*     */       {
/* 697 */         for (Object object : p_175762_1_)
/*     */         {
/* 699 */           if (((object instanceof ResourceLocation)) && (doesStringStartWith(s, ((ResourceLocation)object).getResourcePath())))
/*     */           {
/* 701 */             list.add(String.valueOf(object));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 707 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 715 */     return false;
/*     */   }
/*     */   
/*     */   public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object... msgParams)
/*     */   {
/* 720 */     notifyOperators(sender, command, 0, msgFormat, msgParams);
/*     */   }
/*     */   
/*     */   public static void notifyOperators(ICommandSender sender, ICommand command, int p_152374_2_, String msgFormat, Object... msgParams)
/*     */   {
/* 725 */     if (theAdmin != null)
/*     */     {
/* 727 */       theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setAdminCommander(IAdminCommand command)
/*     */   {
/* 736 */     theAdmin = command;
/*     */   }
/*     */   
/*     */   public int compareTo(ICommand p_compareTo_1_)
/*     */   {
/* 741 */     return getCommandName().compareTo(p_compareTo_1_.getCommandName());
/*     */   }
/*     */   
/*     */   public static class CoordinateArg
/*     */   {
/*     */     private final double field_179633_a;
/*     */     private final double field_179631_b;
/*     */     private final boolean field_179632_c;
/*     */     
/*     */     protected CoordinateArg(double p_i46051_1_, double p_i46051_3_, boolean p_i46051_5_)
/*     */     {
/* 752 */       this.field_179633_a = p_i46051_1_;
/* 753 */       this.field_179631_b = p_i46051_3_;
/* 754 */       this.field_179632_c = p_i46051_5_;
/*     */     }
/*     */     
/*     */     public double func_179628_a()
/*     */     {
/* 759 */       return this.field_179633_a;
/*     */     }
/*     */     
/*     */     public double func_179629_b()
/*     */     {
/* 764 */       return this.field_179631_b;
/*     */     }
/*     */     
/*     */     public boolean func_179630_c()
/*     */     {
/* 769 */       return this.field_179632_c;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */