/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandStats extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  22 */     return "stats";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  30 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  38 */     return "commands.stats.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  46 */     if (args.length < 1)
/*     */     {
/*  48 */       throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */     }
/*     */     
/*     */     boolean flag;
/*     */     
/*     */     boolean flag;
/*  54 */     if (args[0].equals("entity"))
/*     */     {
/*  56 */       flag = false;
/*     */     }
/*     */     else
/*     */     {
/*  60 */       if (!args[0].equals("block"))
/*     */       {
/*  62 */         throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */       }
/*     */       
/*  65 */       flag = true;
/*     */     }
/*     */     
/*     */     int i;
/*     */     int i;
/*  70 */     if (flag)
/*     */     {
/*  72 */       if (args.length < 5)
/*     */       {
/*  74 */         throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
/*     */       }
/*     */       
/*  77 */       i = 4;
/*     */     }
/*     */     else
/*     */     {
/*  81 */       if (args.length < 3)
/*     */       {
/*  83 */         throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
/*     */       }
/*     */       
/*  86 */       i = 2;
/*     */     }
/*     */     
/*  89 */     String s = args[(i++)];
/*     */     
/*  91 */     if ("set".equals(s))
/*     */     {
/*  93 */       if (args.length < i + 3)
/*     */       {
/*  95 */         if (i == 5)
/*     */         {
/*  97 */           throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
/*     */         }
/*     */         
/* 100 */         throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 105 */       if (!"clear".equals(s))
/*     */       {
/* 107 */         throw new WrongUsageException("commands.stats.usage", new Object[0]);
/*     */       }
/*     */       
/* 110 */       if (args.length < i + 1)
/*     */       {
/* 112 */         if (i == 5)
/*     */         {
/* 114 */           throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
/*     */         }
/*     */         
/* 117 */         throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/* 121 */     CommandResultStats.Type commandresultstats$type = CommandResultStats.Type.getTypeByName(args[(i++)]);
/*     */     
/* 123 */     if (commandresultstats$type == null)
/*     */     {
/* 125 */       throw new CommandException("commands.stats.failed", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 129 */     World world = sender.getEntityWorld();
/*     */     CommandResultStats commandresultstats;
/*     */     CommandResultStats commandresultstats;
/* 132 */     if (flag)
/*     */     {
/* 134 */       BlockPos blockpos = parseBlockPos(sender, args, 1, false);
/* 135 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 137 */       if (tileentity == null)
/*     */       {
/* 139 */         throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       CommandResultStats commandresultstats;
/* 142 */       if ((tileentity instanceof TileEntityCommandBlock))
/*     */       {
/* 144 */         commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
/*     */       }
/*     */       else
/*     */       {
/* 148 */         if (!(tileentity instanceof TileEntitySign))
/*     */         {
/* 150 */           throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */         }
/*     */         
/* 153 */         commandresultstats = ((TileEntitySign)tileentity).getStats();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 158 */       Entity entity = func_175768_b(sender, args[1]);
/* 159 */       commandresultstats = entity.getCommandStats();
/*     */     }
/*     */     
/* 162 */     if ("set".equals(s))
/*     */     {
/* 164 */       String s1 = args[(i++)];
/* 165 */       String s2 = args[i];
/*     */       
/* 167 */       if ((s1.length() == 0) || (s2.length() == 0))
/*     */       {
/* 169 */         throw new CommandException("commands.stats.failed", new Object[0]);
/*     */       }
/*     */       
/* 172 */       CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, s1, s2);
/* 173 */       notifyOperators(sender, this, "commands.stats.success", new Object[] { commandresultstats$type.getTypeName(), s2, s1 });
/*     */     }
/* 175 */     else if ("clear".equals(s))
/*     */     {
/* 177 */       CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, null, null);
/* 178 */       notifyOperators(sender, this, "commands.stats.cleared", new Object[] { commandresultstats$type.getTypeName() });
/*     */     }
/*     */     
/* 181 */     if (flag)
/*     */     {
/* 183 */       BlockPos blockpos1 = parseBlockPos(sender, args, 1, false);
/* 184 */       TileEntity tileentity1 = world.getTileEntity(blockpos1);
/* 185 */       tileentity1.markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 193 */     return ((args.length != 3) || (!args[0].equals("entity"))) && ((args.length != 5) || (!args[0].equals("block"))) ? getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames()) : ((args.length != 4) || (!args[0].equals("entity"))) && ((args.length != 6) || (!args[0].equals("block"))) ? getListOfStringsMatchingLastWord(args, func_175777_e()) : ((args.length != 6) || (!args[0].equals("entity"))) && ((args.length != 8) || (!args[0].equals("block"))) ? null : (args.length >= 2) && (args.length <= 4) && (args[0].equals("block")) ? func_175771_a(args, 1, pos) : (args.length == 2) && (args[0].equals("entity")) ? getListOfStringsMatchingLastWord(args, func_175776_d()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "entity", "block" }) : getListOfStringsMatchingLastWord(args, new String[] { "set", "clear" });
/*     */   }
/*     */   
/*     */   protected String[] func_175776_d()
/*     */   {
/* 198 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */   protected List<String> func_175777_e()
/*     */   {
/* 203 */     Collection<ScoreObjective> collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
/* 204 */     List<String> list = Lists.newArrayList();
/*     */     
/* 206 */     for (ScoreObjective scoreobjective : collection)
/*     */     {
/* 208 */       if (!scoreobjective.getCriteria().isReadOnly())
/*     */       {
/* 210 */         list.add(scoreobjective.getName());
/*     */       }
/*     */     }
/*     */     
/* 214 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 222 */     return (args.length > 0) && (args[0].equals("entity")) && (index == 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */