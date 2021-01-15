/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandSpreadPlayers extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  27 */     return "spreadplayers";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  35 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  43 */     return "commands.spreadplayers.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  51 */     if (args.length < 6)
/*     */     {
/*  53 */       throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  57 */     int i = 0;
/*  58 */     BlockPos blockpos = sender.getPosition();
/*  59 */     double d0 = parseDouble(blockpos.getX(), args[(i++)], true);
/*  60 */     double d1 = parseDouble(blockpos.getZ(), args[(i++)], true);
/*  61 */     double d2 = parseDouble(args[(i++)], 0.0D);
/*  62 */     double d3 = parseDouble(args[(i++)], d2 + 1.0D);
/*  63 */     boolean flag = parseBoolean(args[(i++)]);
/*  64 */     List<Entity> list = Lists.newArrayList();
/*     */     
/*  66 */     while (i < args.length)
/*     */     {
/*  68 */       String s = args[(i++)];
/*     */       
/*  70 */       if (PlayerSelector.hasArguments(s))
/*     */       {
/*  72 */         List<Entity> list1 = PlayerSelector.matchEntities(sender, s, Entity.class);
/*     */         
/*  74 */         if (list1.size() == 0)
/*     */         {
/*  76 */           throw new EntityNotFoundException();
/*     */         }
/*     */         
/*  79 */         list.addAll(list1);
/*     */       }
/*     */       else
/*     */       {
/*  83 */         EntityPlayer entityplayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(s);
/*     */         
/*  85 */         if (entityplayer == null)
/*     */         {
/*  87 */           throw new PlayerNotFoundException();
/*     */         }
/*     */         
/*  90 */         list.add(entityplayer);
/*     */       }
/*     */     }
/*     */     
/*  94 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*     */     
/*  96 */     if (list.isEmpty())
/*     */     {
/*  98 */       throw new EntityNotFoundException();
/*     */     }
/*     */     
/*     */ 
/* 102 */     sender.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[] { Integer.valueOf(list.size()), Double.valueOf(d3), Double.valueOf(d0), Double.valueOf(d1), Double.valueOf(d2) }));
/* 103 */     func_110669_a(sender, list, new Position(d0, d1), d2, d3, ((Entity)list.get(0)).worldObj, flag);
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_110669_a(ICommandSender p_110669_1_, List<Entity> p_110669_2_, Position p_110669_3_, double p_110669_4_, double p_110669_6_, World worldIn, boolean p_110669_9_)
/*     */     throws CommandException
/*     */   {
/* 110 */     Random random = new Random();
/* 111 */     double d0 = p_110669_3_.field_111101_a - p_110669_6_;
/* 112 */     double d1 = p_110669_3_.field_111100_b - p_110669_6_;
/* 113 */     double d2 = p_110669_3_.field_111101_a + p_110669_6_;
/* 114 */     double d3 = p_110669_3_.field_111100_b + p_110669_6_;
/* 115 */     Position[] acommandspreadplayers$position = func_110670_a(random, p_110669_9_ ? func_110667_a(p_110669_2_) : p_110669_2_.size(), d0, d1, d2, d3);
/* 116 */     int i = func_110668_a(p_110669_3_, p_110669_4_, worldIn, random, d0, d1, d2, d3, acommandspreadplayers$position, p_110669_9_);
/* 117 */     double d4 = func_110671_a(p_110669_2_, worldIn, acommandspreadplayers$position, p_110669_9_);
/* 118 */     notifyOperators(p_110669_1_, this, "commands.spreadplayers.success." + (p_110669_9_ ? "teams" : "players"), new Object[] { Integer.valueOf(acommandspreadplayers$position.length), Double.valueOf(p_110669_3_.field_111101_a), Double.valueOf(p_110669_3_.field_111100_b) });
/*     */     
/* 120 */     if (acommandspreadplayers$position.length > 1)
/*     */     {
/* 122 */       p_110669_1_.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.info." + (p_110669_9_ ? "teams" : "players"), new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d4) }), Integer.valueOf(i) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private int func_110667_a(List<Entity> p_110667_1_)
/*     */   {
/* 128 */     Set<Team> set = Sets.newHashSet();
/*     */     
/* 130 */     for (Entity entity : p_110667_1_)
/*     */     {
/* 132 */       if ((entity instanceof EntityPlayer))
/*     */       {
/* 134 */         set.add(((EntityPlayer)entity).getTeam());
/*     */       }
/*     */       else
/*     */       {
/* 138 */         set.add(null);
/*     */       }
/*     */     }
/*     */     
/* 142 */     return set.size();
/*     */   }
/*     */   
/*     */   private int func_110668_a(Position p_110668_1_, double p_110668_2_, World worldIn, Random p_110668_5_, double p_110668_6_, double p_110668_8_, double p_110668_10_, double p_110668_12_, Position[] p_110668_14_, boolean p_110668_15_) throws CommandException
/*     */   {
/* 147 */     boolean flag = true;
/* 148 */     double d0 = 3.4028234663852886E38D;
/*     */     
/*     */ 
/* 151 */     for (int i = 0; (i < 10000) && (flag); i++)
/*     */     {
/* 153 */       flag = false;
/* 154 */       d0 = 3.4028234663852886E38D;
/*     */       Position commandspreadplayers$position;
/* 156 */       int k; Position commandspreadplayers$position1; for (int j = 0; j < p_110668_14_.length; j++)
/*     */       {
/* 158 */         commandspreadplayers$position = p_110668_14_[j];
/* 159 */         k = 0;
/* 160 */         commandspreadplayers$position1 = new Position();
/*     */         
/* 162 */         for (int l = 0; l < p_110668_14_.length; l++)
/*     */         {
/* 164 */           if (j != l)
/*     */           {
/* 166 */             Position commandspreadplayers$position2 = p_110668_14_[l];
/* 167 */             double d1 = commandspreadplayers$position.func_111099_a(commandspreadplayers$position2);
/* 168 */             d0 = Math.min(d1, d0);
/*     */             
/* 170 */             if (d1 < p_110668_2_)
/*     */             {
/* 172 */               k++;
/* 173 */               commandspreadplayers$position1.field_111101_a += commandspreadplayers$position2.field_111101_a - commandspreadplayers$position.field_111101_a;
/* 174 */               commandspreadplayers$position1.field_111100_b += commandspreadplayers$position2.field_111100_b - commandspreadplayers$position.field_111100_b;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 179 */         if (k > 0)
/*     */         {
/* 181 */           commandspreadplayers$position1.field_111101_a /= k;
/* 182 */           commandspreadplayers$position1.field_111100_b /= k;
/* 183 */           double d2 = commandspreadplayers$position1.func_111096_b();
/*     */           
/* 185 */           if (d2 > 0.0D)
/*     */           {
/* 187 */             commandspreadplayers$position1.func_111095_a();
/* 188 */             commandspreadplayers$position.func_111094_b(commandspreadplayers$position1);
/*     */           }
/*     */           else
/*     */           {
/* 192 */             commandspreadplayers$position.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
/*     */           }
/*     */           
/* 195 */           flag = true;
/*     */         }
/*     */         
/* 198 */         if (commandspreadplayers$position.func_111093_a(p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_))
/*     */         {
/* 200 */           flag = true;
/*     */         }
/*     */       }
/*     */       
/* 204 */       if (!flag)
/*     */       {
/* 206 */         k = (commandspreadplayers$position1 = p_110668_14_).length; for (commandspreadplayers$position = 0; commandspreadplayers$position < k; commandspreadplayers$position++) { Position commandspreadplayers$position3 = commandspreadplayers$position1[commandspreadplayers$position];
/*     */           
/* 208 */           if (!commandspreadplayers$position3.func_111098_b(worldIn))
/*     */           {
/* 210 */             commandspreadplayers$position3.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
/* 211 */             flag = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 217 */     if (i >= 10000)
/*     */     {
/* 219 */       throw new CommandException("commands.spreadplayers.failure." + (p_110668_15_ ? "teams" : "players"), new Object[] { Integer.valueOf(p_110668_14_.length), Double.valueOf(p_110668_1_.field_111101_a), Double.valueOf(p_110668_1_.field_111100_b), String.format("%.2f", new Object[] { Double.valueOf(d0) }) });
/*     */     }
/*     */     
/*     */ 
/* 223 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   private double func_110671_a(List<Entity> p_110671_1_, World worldIn, Position[] p_110671_3_, boolean p_110671_4_)
/*     */   {
/* 229 */     double d0 = 0.0D;
/* 230 */     int i = 0;
/* 231 */     Map<Team, Position> map = Maps.newHashMap();
/*     */     
/* 233 */     for (int j = 0; j < p_110671_1_.size(); j++)
/*     */     {
/* 235 */       Entity entity = (Entity)p_110671_1_.get(j);
/*     */       Position commandspreadplayers$position;
/*     */       Position commandspreadplayers$position;
/* 238 */       if (p_110671_4_)
/*     */       {
/* 240 */         Team team = (entity instanceof EntityPlayer) ? ((EntityPlayer)entity).getTeam() : null;
/*     */         
/* 242 */         if (!map.containsKey(team))
/*     */         {
/* 244 */           map.put(team, p_110671_3_[(i++)]);
/*     */         }
/*     */         
/* 247 */         commandspreadplayers$position = (Position)map.get(team);
/*     */       }
/*     */       else
/*     */       {
/* 251 */         commandspreadplayers$position = p_110671_3_[(i++)];
/*     */       }
/*     */       
/* 254 */       entity.setPositionAndUpdate(MathHelper.floor_double(commandspreadplayers$position.field_111101_a) + 0.5F, commandspreadplayers$position.func_111092_a(worldIn), MathHelper.floor_double(commandspreadplayers$position.field_111100_b) + 0.5D);
/* 255 */       double d2 = Double.MAX_VALUE;
/*     */       
/* 257 */       for (int k = 0; k < p_110671_3_.length; k++)
/*     */       {
/* 259 */         if (commandspreadplayers$position != p_110671_3_[k])
/*     */         {
/* 261 */           double d1 = commandspreadplayers$position.func_111099_a(p_110671_3_[k]);
/* 262 */           d2 = Math.min(d1, d2);
/*     */         }
/*     */       }
/*     */       
/* 266 */       d0 += d2;
/*     */     }
/*     */     
/* 269 */     d0 /= p_110671_1_.size();
/* 270 */     return d0;
/*     */   }
/*     */   
/*     */   private Position[] func_110670_a(Random p_110670_1_, int p_110670_2_, double p_110670_3_, double p_110670_5_, double p_110670_7_, double p_110670_9_)
/*     */   {
/* 275 */     Position[] acommandspreadplayers$position = new Position[p_110670_2_];
/*     */     
/* 277 */     for (int i = 0; i < acommandspreadplayers$position.length; i++)
/*     */     {
/* 279 */       Position commandspreadplayers$position = new Position();
/* 280 */       commandspreadplayers$position.func_111097_a(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
/* 281 */       acommandspreadplayers$position[i] = commandspreadplayers$position;
/*     */     }
/*     */     
/* 284 */     return acommandspreadplayers$position;
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 289 */     return (args.length >= 1) && (args.length <= 2) ? func_181043_b(args, 0, pos) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   static class Position
/*     */   {
/*     */     double field_111101_a;
/*     */     
/*     */     double field_111100_b;
/*     */     
/*     */     Position() {}
/*     */     
/*     */     Position(double p_i1358_1_, double p_i1358_3_)
/*     */     {
/* 303 */       this.field_111101_a = p_i1358_1_;
/* 304 */       this.field_111100_b = p_i1358_3_;
/*     */     }
/*     */     
/*     */     double func_111099_a(Position p_111099_1_)
/*     */     {
/* 309 */       double d0 = this.field_111101_a - p_111099_1_.field_111101_a;
/* 310 */       double d1 = this.field_111100_b - p_111099_1_.field_111100_b;
/* 311 */       return Math.sqrt(d0 * d0 + d1 * d1);
/*     */     }
/*     */     
/*     */     void func_111095_a()
/*     */     {
/* 316 */       double d0 = func_111096_b();
/* 317 */       this.field_111101_a /= d0;
/* 318 */       this.field_111100_b /= d0;
/*     */     }
/*     */     
/*     */     float func_111096_b()
/*     */     {
/* 323 */       return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
/*     */     }
/*     */     
/*     */     public void func_111094_b(Position p_111094_1_)
/*     */     {
/* 328 */       this.field_111101_a -= p_111094_1_.field_111101_a;
/* 329 */       this.field_111100_b -= p_111094_1_.field_111100_b;
/*     */     }
/*     */     
/*     */     public boolean func_111093_a(double p_111093_1_, double p_111093_3_, double p_111093_5_, double p_111093_7_)
/*     */     {
/* 334 */       boolean flag = false;
/*     */       
/* 336 */       if (this.field_111101_a < p_111093_1_)
/*     */       {
/* 338 */         this.field_111101_a = p_111093_1_;
/* 339 */         flag = true;
/*     */       }
/* 341 */       else if (this.field_111101_a > p_111093_5_)
/*     */       {
/* 343 */         this.field_111101_a = p_111093_5_;
/* 344 */         flag = true;
/*     */       }
/*     */       
/* 347 */       if (this.field_111100_b < p_111093_3_)
/*     */       {
/* 349 */         this.field_111100_b = p_111093_3_;
/* 350 */         flag = true;
/*     */       }
/* 352 */       else if (this.field_111100_b > p_111093_7_)
/*     */       {
/* 354 */         this.field_111100_b = p_111093_7_;
/* 355 */         flag = true;
/*     */       }
/*     */       
/* 358 */       return flag;
/*     */     }
/*     */     
/*     */     public int func_111092_a(World worldIn)
/*     */     {
/* 363 */       BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);
/*     */       
/* 365 */       while (blockpos.getY() > 0)
/*     */       {
/* 367 */         blockpos = blockpos.down();
/*     */         
/* 369 */         if (worldIn.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*     */         {
/* 371 */           return blockpos.getY() + 1;
/*     */         }
/*     */       }
/*     */       
/* 375 */       return 257;
/*     */     }
/*     */     
/*     */     public boolean func_111098_b(World worldIn)
/*     */     {
/* 380 */       BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);
/*     */       
/* 382 */       while (blockpos.getY() > 0)
/*     */       {
/* 384 */         blockpos = blockpos.down();
/* 385 */         Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */         
/* 387 */         if (material != Material.air)
/*     */         {
/* 389 */           return (!material.isLiquid()) && (material != Material.fire);
/*     */         }
/*     */       }
/*     */       
/* 393 */       return false;
/*     */     }
/*     */     
/*     */     public void func_111097_a(Random p_111097_1_, double p_111097_2_, double p_111097_4_, double p_111097_6_, double p_111097_8_)
/*     */     {
/* 398 */       this.field_111101_a = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_2_, p_111097_6_);
/* 399 */       this.field_111100_b = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_4_, p_111097_8_);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandSpreadPlayers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */