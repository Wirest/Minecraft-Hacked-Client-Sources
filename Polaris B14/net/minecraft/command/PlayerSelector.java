/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ItemInWorldManager;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ 
/*     */ public class PlayerSelector
/*     */ {
/*  42 */   private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
/*  53 */   private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(new String[] { "x", "y", "z", "dx", "dy", "dz", "rm", "r" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token)
/*     */   {
/*  60 */     return (EntityPlayerMP)matchOneEntity(sender, token, EntityPlayerMP.class);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class<? extends T> targetClass)
/*     */   {
/*  65 */     List<T> list = matchEntities(sender, token, targetClass);
/*  66 */     return list.size() == 1 ? (Entity)list.get(0) : null;
/*     */   }
/*     */   
/*     */   public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token)
/*     */   {
/*  71 */     List<Entity> list = matchEntities(sender, token, Entity.class);
/*     */     
/*  73 */     if (list.isEmpty())
/*     */     {
/*  75 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  79 */     List<IChatComponent> list1 = Lists.newArrayList();
/*     */     
/*  81 */     for (Entity entity : list)
/*     */     {
/*  83 */       list1.add(entity.getDisplayName());
/*     */     }
/*     */     
/*  86 */     return CommandBase.join(list1);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class<? extends T> targetClass)
/*     */   {
/*  92 */     Matcher matcher = tokenPattern.matcher(token);
/*     */     
/*  94 */     if ((matcher.matches()) && (sender.canCommandSenderUseCommand(1, "@")))
/*     */     {
/*  96 */       Map<String, String> map = getArgumentMap(matcher.group(2));
/*     */       
/*  98 */       if (!isEntityTypeValid(sender, map))
/*     */       {
/* 100 */         return Collections.emptyList();
/*     */       }
/*     */       
/*     */ 
/* 104 */       String s = matcher.group(1);
/* 105 */       BlockPos blockpos = func_179664_b(map, sender.getPosition());
/* 106 */       List<World> list = getWorlds(sender, map);
/* 107 */       List<T> list1 = Lists.newArrayList();
/*     */       
/* 109 */       for (World world : list)
/*     */       {
/* 111 */         if (world != null)
/*     */         {
/* 113 */           List<Predicate<Entity>> list2 = Lists.newArrayList();
/* 114 */           list2.addAll(func_179663_a(map, s));
/* 115 */           list2.addAll(func_179648_b(map));
/* 116 */           list2.addAll(func_179649_c(map));
/* 117 */           list2.addAll(func_179659_d(map));
/* 118 */           list2.addAll(func_179657_e(map));
/* 119 */           list2.addAll(func_179647_f(map));
/* 120 */           list2.addAll(func_180698_a(map, blockpos));
/* 121 */           list2.addAll(func_179662_g(map));
/* 122 */           list1.addAll(filterResults(map, targetClass, list2, s, world, blockpos));
/*     */         }
/*     */       }
/*     */       
/* 126 */       return func_179658_a(list1, map, sender, targetClass, s, blockpos);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 131 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */   private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap)
/*     */   {
/* 137 */     List<World> list = Lists.newArrayList();
/*     */     
/* 139 */     if (func_179665_h(argumentMap))
/*     */     {
/* 141 */       list.add(sender.getEntityWorld());
/*     */     }
/*     */     else
/*     */     {
/* 145 */       Collections.addAll(list, MinecraftServer.getServer().worldServers);
/*     */     }
/*     */     
/* 148 */     return list;
/*     */   }
/*     */   
/*     */   private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params)
/*     */   {
/* 153 */     String s = func_179651_b(params, "type");
/* 154 */     s = (s != null) && (s.startsWith("!")) ? s.substring(1) : s;
/*     */     
/* 156 */     if ((s != null) && (!EntityList.isStringValidEntityName(s)))
/*     */     {
/* 158 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] { s });
/* 159 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/* 160 */       commandSender.addChatMessage(chatcomponenttranslation);
/* 161 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 165 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_)
/*     */   {
/* 171 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 172 */     String s = func_179651_b(p_179663_0_, "type");
/* 173 */     final boolean flag = (s != null) && (s.startsWith("!"));
/*     */     
/* 175 */     if (flag)
/*     */     {
/* 177 */       s = s.substring(1);
/*     */     }
/*     */     
/* 180 */     boolean flag1 = !p_179663_1_.equals("e");
/* 181 */     boolean flag2 = (p_179663_1_.equals("r")) && (s != null);
/*     */     
/* 183 */     if (((s == null) || (!p_179663_1_.equals("e"))) && (!flag2))
/*     */     {
/* 185 */       if (flag1)
/*     */       {
/* 187 */         list.add(new Predicate()
/*     */         {
/*     */           public boolean apply(Entity p_apply_1_)
/*     */           {
/* 191 */             return p_apply_1_ instanceof EntityPlayer;
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 198 */       String s_f = s;
/* 199 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 203 */           return EntityList.isStringEntityName(p_apply_1_, PlayerSelector.this) ^ flag;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 208 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179648_b(Map<String, String> p_179648_0_)
/*     */   {
/* 213 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 214 */     int i = parseIntWithDefault(p_179648_0_, "lm", -1);
/* 215 */     final int j = parseIntWithDefault(p_179648_0_, "l", -1);
/*     */     
/* 217 */     if ((i > -1) || (j > -1))
/*     */     {
/* 219 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 223 */           if (!(p_apply_1_ instanceof EntityPlayerMP))
/*     */           {
/* 225 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 229 */           EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
/* 230 */           return ((this.val$i <= -1) || (entityplayermp.experienceLevel >= this.val$i)) && ((j <= -1) || (entityplayermp.experienceLevel <= j));
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */ 
/* 236 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179649_c(Map<String, String> p_179649_0_)
/*     */   {
/* 241 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 242 */     int i = parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
/*     */     
/* 244 */     if (i != WorldSettings.GameType.NOT_SET.getID())
/*     */     {
/* 246 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 250 */           if (!(p_apply_1_ instanceof EntityPlayerMP))
/*     */           {
/* 252 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 256 */           EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
/* 257 */           return entityplayermp.theItemInWorldManager.getGameType().getID() == this.val$i;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */ 
/* 263 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179659_d(Map<String, String> p_179659_0_)
/*     */   {
/* 268 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 269 */     String s = func_179651_b(p_179659_0_, "team");
/* 270 */     final boolean flag = (s != null) && (s.startsWith("!"));
/*     */     
/* 272 */     if (flag)
/*     */     {
/* 274 */       s = s.substring(1);
/*     */     }
/*     */     
/* 277 */     if (s != null)
/*     */     {
/* 279 */       String s_f = s;
/* 280 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 284 */           if (!(p_apply_1_ instanceof EntityLivingBase))
/*     */           {
/* 286 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 290 */           EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
/* 291 */           Team team = entitylivingbase.getTeam();
/* 292 */           String s1 = team == null ? "" : team.getRegisteredName();
/* 293 */           return s1.equals(PlayerSelector.this) ^ flag;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */ 
/* 299 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179657_e(Map<String, String> p_179657_0_)
/*     */   {
/* 304 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 305 */     Map<String, Integer> map = func_96560_a(p_179657_0_);
/*     */     
/* 307 */     if ((map != null) && (map.size() > 0))
/*     */     {
/* 309 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 313 */           Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*     */           
/* 315 */           for (Map.Entry<String, Integer> entry : PlayerSelector.this.entrySet())
/*     */           {
/* 317 */             String s = (String)entry.getKey();
/* 318 */             boolean flag = false;
/*     */             
/* 320 */             if ((s.endsWith("_min")) && (s.length() > 4))
/*     */             {
/* 322 */               flag = true;
/* 323 */               s = s.substring(0, s.length() - 4);
/*     */             }
/*     */             
/* 326 */             net.minecraft.scoreboard.ScoreObjective scoreobjective = scoreboard.getObjective(s);
/*     */             
/* 328 */             if (scoreobjective == null)
/*     */             {
/* 330 */               return false;
/*     */             }
/*     */             
/* 333 */             String s1 = (p_apply_1_ instanceof EntityPlayerMP) ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
/*     */             
/* 335 */             if (!scoreboard.entityHasObjective(s1, scoreobjective))
/*     */             {
/* 337 */               return false;
/*     */             }
/*     */             
/* 340 */             Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
/* 341 */             int i = score.getScorePoints();
/*     */             
/* 343 */             if ((i < ((Integer)entry.getValue()).intValue()) && (flag))
/*     */             {
/* 345 */               return false;
/*     */             }
/*     */             
/* 348 */             if ((i > ((Integer)entry.getValue()).intValue()) && (!flag))
/*     */             {
/* 350 */               return false;
/*     */             }
/*     */           }
/*     */           
/* 354 */           return true;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 359 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179647_f(Map<String, String> p_179647_0_)
/*     */   {
/* 364 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 365 */     String s = func_179651_b(p_179647_0_, "name");
/* 366 */     final boolean flag = (s != null) && (s.startsWith("!"));
/*     */     
/* 368 */     if (flag)
/*     */     {
/* 370 */       s = s.substring(1);
/*     */     }
/*     */     
/* 373 */     if (s != null)
/*     */     {
/* 375 */       String s_f = s;
/* 376 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 380 */           return p_apply_1_.getName().equals(PlayerSelector.this) ^ flag;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 385 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, BlockPos p_180698_1_)
/*     */   {
/* 390 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 391 */     final int i = parseIntWithDefault(p_180698_0_, "rm", -1);
/* 392 */     final int j = parseIntWithDefault(p_180698_0_, "r", -1);
/*     */     
/* 394 */     if ((p_180698_1_ != null) && ((i >= 0) || (j >= 0)))
/*     */     {
/* 396 */       final int k = i * i;
/* 397 */       final int l = j * j;
/* 398 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 402 */           int i1 = (int)p_apply_1_.getDistanceSqToCenter(PlayerSelector.this);
/* 403 */           return ((i < 0) || (i1 >= k)) && ((j < 0) || (i1 <= l));
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 408 */     return list;
/*     */   }
/*     */   
/*     */   private static List<Predicate<Entity>> func_179662_g(Map<String, String> p_179662_0_)
/*     */   {
/* 413 */     List<Predicate<Entity>> list = Lists.newArrayList();
/*     */     
/* 415 */     if ((p_179662_0_.containsKey("rym")) || (p_179662_0_.containsKey("ry")))
/*     */     {
/* 417 */       int i = func_179650_a(parseIntWithDefault(p_179662_0_, "rym", 0));
/* 418 */       final int j = func_179650_a(parseIntWithDefault(p_179662_0_, "ry", 359));
/* 419 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 423 */           int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationYaw));
/* 424 */           return (i1 >= this.val$i) || (i1 <= j);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 429 */     if ((p_179662_0_.containsKey("rxm")) || (p_179662_0_.containsKey("rx")))
/*     */     {
/* 431 */       int k = func_179650_a(parseIntWithDefault(p_179662_0_, "rxm", 0));
/* 432 */       final int l = func_179650_a(parseIntWithDefault(p_179662_0_, "rx", 359));
/* 433 */       list.add(new Predicate()
/*     */       {
/*     */         public boolean apply(Entity p_apply_1_)
/*     */         {
/* 437 */           int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationPitch));
/* 438 */           return (i1 >= this.val$k) || (i1 <= l);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 443 */     return list;
/*     */   }
/*     */   
/*     */   private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass, List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position)
/*     */   {
/* 448 */     List<T> list = Lists.newArrayList();
/* 449 */     String s = func_179651_b(params, "type");
/* 450 */     s = (s != null) && (s.startsWith("!")) ? s.substring(1) : s;
/* 451 */     boolean flag = !type.equals("e");
/* 452 */     boolean flag1 = (type.equals("r")) && (s != null);
/* 453 */     int i = parseIntWithDefault(params, "dx", 0);
/* 454 */     int j = parseIntWithDefault(params, "dy", 0);
/* 455 */     int k = parseIntWithDefault(params, "dz", 0);
/* 456 */     int l = parseIntWithDefault(params, "r", -1);
/* 457 */     Predicate<Entity> predicate = Predicates.and(inputList);
/* 458 */     Predicate<Entity> predicate1 = Predicates.and(EntitySelectors.selectAnything, predicate);
/*     */     
/* 460 */     if (position != null)
/*     */     {
/* 462 */       int i1 = worldIn.playerEntities.size();
/* 463 */       int j1 = worldIn.loadedEntityList.size();
/* 464 */       boolean flag2 = i1 < j1 / 16;
/*     */       
/* 466 */       if ((!params.containsKey("dx")) && (!params.containsKey("dy")) && (!params.containsKey("dz")))
/*     */       {
/* 468 */         if (l >= 0)
/*     */         {
/* 470 */           AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);
/*     */           
/* 472 */           if ((flag) && (flag2) && (!flag1))
/*     */           {
/* 474 */             list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */           }
/*     */           else
/*     */           {
/* 478 */             list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb1, predicate1));
/*     */           }
/*     */         }
/* 481 */         else if (type.equals("a"))
/*     */         {
/* 483 */           list.addAll(worldIn.getPlayers(entityClass, predicate));
/*     */         }
/* 485 */         else if ((!type.equals("p")) && ((!type.equals("r")) || (flag1)))
/*     */         {
/* 487 */           list.addAll(worldIn.getEntities(entityClass, predicate1));
/*     */         }
/*     */         else
/*     */         {
/* 491 */           list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 496 */         AxisAlignedBB axisalignedbb = func_179661_a(position, i, j, k);
/*     */         
/* 498 */         if ((flag) && (flag2) && (!flag1))
/*     */         {
/* 500 */           Predicate<Entity> predicate2 = new Predicate()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 504 */               return (p_apply_1_.posX < PlayerSelector.this.maxX) && (p_apply_1_.posY < PlayerSelector.this.maxY) && (p_apply_1_.posZ < PlayerSelector.this.maxZ);
/*     */             }
/* 506 */           };
/* 507 */           list.addAll(worldIn.getPlayers(entityClass, Predicates.and(predicate1, predicate2)));
/*     */         }
/*     */         else
/*     */         {
/* 511 */           list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
/*     */         }
/*     */       }
/*     */     }
/* 515 */     else if (type.equals("a"))
/*     */     {
/* 517 */       list.addAll(worldIn.getPlayers(entityClass, predicate));
/*     */     }
/* 519 */     else if ((!type.equals("p")) && ((!type.equals("r")) || (flag1)))
/*     */     {
/* 521 */       list.addAll(worldIn.getEntities(entityClass, predicate1));
/*     */     }
/*     */     else
/*     */     {
/* 525 */       list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */     }
/*     */     
/* 528 */     return list;
/*     */   }
/*     */   
/*     */   private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class<? extends T> p_179658_3_, String p_179658_4_, BlockPos p_179658_5_)
/*     */   {
/* 533 */     int i = parseIntWithDefault(p_179658_1_, "c", (!p_179658_4_.equals("a")) && (!p_179658_4_.equals("e")) ? 1 : 0);
/*     */     
/* 535 */     if ((!p_179658_4_.equals("p")) && (!p_179658_4_.equals("a")) && (!p_179658_4_.equals("e")))
/*     */     {
/* 537 */       if (p_179658_4_.equals("r"))
/*     */       {
/* 539 */         Collections.shuffle(p_179658_0_);
/*     */       }
/*     */     }
/* 542 */     else if (p_179658_5_ != null)
/*     */     {
/* 544 */       Collections.sort(p_179658_0_, new Comparator()
/*     */       {
/*     */         public int compare(Entity p_compare_1_, Entity p_compare_2_)
/*     */         {
/* 548 */           return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(PlayerSelector.this), p_compare_2_.getDistanceSq(PlayerSelector.this)).result();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 553 */     Entity entity = p_179658_2_.getCommandSenderEntity();
/*     */     
/* 555 */     if ((entity != null) && (p_179658_3_.isAssignableFrom(entity.getClass())) && (i == 1) && (p_179658_0_.contains(entity)) && (!"r".equals(p_179658_4_)))
/*     */     {
/* 557 */       p_179658_0_ = Lists.newArrayList(new Entity[] { entity });
/*     */     }
/*     */     
/* 560 */     if (i != 0)
/*     */     {
/* 562 */       if (i < 0)
/*     */       {
/* 564 */         Collections.reverse(p_179658_0_);
/*     */       }
/*     */       
/* 567 */       p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(i), p_179658_0_.size()));
/*     */     }
/*     */     
/* 570 */     return p_179658_0_;
/*     */   }
/*     */   
/*     */   private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_)
/*     */   {
/* 575 */     boolean flag = p_179661_1_ < 0;
/* 576 */     boolean flag1 = p_179661_2_ < 0;
/* 577 */     boolean flag2 = p_179661_3_ < 0;
/* 578 */     int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
/* 579 */     int j = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
/* 580 */     int k = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
/* 581 */     int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
/* 582 */     int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
/* 583 */     int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
/* 584 */     return new AxisAlignedBB(i, j, k, l, i1, j1);
/*     */   }
/*     */   
/*     */   public static int func_179650_a(int p_179650_0_)
/*     */   {
/* 589 */     p_179650_0_ %= 360;
/*     */     
/* 591 */     if (p_179650_0_ >= 160)
/*     */     {
/* 593 */       p_179650_0_ -= 360;
/*     */     }
/*     */     
/* 596 */     if (p_179650_0_ < 0)
/*     */     {
/* 598 */       p_179650_0_ += 360;
/*     */     }
/*     */     
/* 601 */     return p_179650_0_;
/*     */   }
/*     */   
/*     */   private static BlockPos func_179664_b(Map<String, String> p_179664_0_, BlockPos p_179664_1_)
/*     */   {
/* 606 */     return new BlockPos(parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
/*     */   }
/*     */   
/*     */   private static boolean func_179665_h(Map<String, String> p_179665_0_)
/*     */   {
/* 611 */     for (String s : WORLD_BINDING_ARGS)
/*     */     {
/* 613 */       if (p_179665_0_.containsKey(s))
/*     */       {
/* 615 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 619 */     return false;
/*     */   }
/*     */   
/*     */   private static int parseIntWithDefault(Map<String, String> p_179653_0_, String p_179653_1_, int p_179653_2_)
/*     */   {
/* 624 */     return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault((String)p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
/*     */   }
/*     */   
/*     */   private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_)
/*     */   {
/* 629 */     return (String)p_179651_0_.get(p_179651_1_);
/*     */   }
/*     */   
/*     */   public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_)
/*     */   {
/* 634 */     Map<String, Integer> map = Maps.newHashMap();
/*     */     
/* 636 */     for (String s : p_96560_0_.keySet())
/*     */     {
/* 638 */       if ((s.startsWith("score_")) && (s.length() > "score_".length()))
/*     */       {
/* 640 */         map.put(s.substring("score_".length()), Integer.valueOf(MathHelper.parseIntWithDefault((String)p_96560_0_.get(s), 1)));
/*     */       }
/*     */     }
/*     */     
/* 644 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean matchesMultiplePlayers(String p_82377_0_)
/*     */   {
/* 652 */     Matcher matcher = tokenPattern.matcher(p_82377_0_);
/*     */     
/* 654 */     if (!matcher.matches())
/*     */     {
/* 656 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 660 */     Map<String, String> map = getArgumentMap(matcher.group(2));
/* 661 */     String s = matcher.group(1);
/* 662 */     int i = (!"a".equals(s)) && (!"e".equals(s)) ? 1 : 0;
/* 663 */     return parseIntWithDefault(map, "c", i) != 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasArguments(String p_82378_0_)
/*     */   {
/* 672 */     return tokenPattern.matcher(p_82378_0_).matches();
/*     */   }
/*     */   
/*     */   private static Map<String, String> getArgumentMap(String argumentString)
/*     */   {
/* 677 */     Map<String, String> map = Maps.newHashMap();
/*     */     
/* 679 */     if (argumentString == null)
/*     */     {
/* 681 */       return map;
/*     */     }
/*     */     
/*     */ 
/* 685 */     int i = 0;
/* 686 */     int j = -1;
/*     */     
/* 688 */     for (Matcher matcher = intListPattern.matcher(argumentString); matcher.find(); j = matcher.end())
/*     */     {
/* 690 */       String s = null;
/*     */       
/* 692 */       switch (i++)
/*     */       {
/*     */       case 0: 
/* 695 */         s = "x";
/* 696 */         break;
/*     */       
/*     */       case 1: 
/* 699 */         s = "y";
/* 700 */         break;
/*     */       
/*     */       case 2: 
/* 703 */         s = "z";
/* 704 */         break;
/*     */       
/*     */       case 3: 
/* 707 */         s = "r";
/*     */       }
/*     */       
/* 710 */       if ((s != null) && (matcher.group(1).length() > 0))
/*     */       {
/* 712 */         map.put(s, matcher.group(1));
/*     */       }
/*     */     }
/*     */     
/* 716 */     if (j < argumentString.length())
/*     */     {
/* 718 */       Matcher matcher1 = keyValueListPattern.matcher(j == -1 ? argumentString : argumentString.substring(j));
/*     */       
/* 720 */       while (matcher1.find())
/*     */       {
/* 722 */         map.put(matcher1.group(1), matcher1.group(2));
/*     */       }
/*     */     }
/*     */     
/* 726 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\PlayerSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */