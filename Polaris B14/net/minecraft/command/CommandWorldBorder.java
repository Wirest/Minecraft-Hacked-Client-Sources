/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandWorldBorder
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  17 */     return "worldborder";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  25 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  33 */     return "commands.worldborder.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  41 */     if (args.length < 1)
/*     */     {
/*  43 */       throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  47 */     WorldBorder worldborder = getWorldBorder();
/*     */     
/*  49 */     if (args[0].equals("set"))
/*     */     {
/*  51 */       if ((args.length != 2) && (args.length != 3))
/*     */       {
/*  53 */         throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
/*     */       }
/*     */       
/*  56 */       double d0 = worldborder.getTargetSize();
/*  57 */       double d2 = parseDouble(args[1], 1.0D, 6.0E7D);
/*  58 */       long i = args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;
/*     */       
/*  60 */       if (i > 0L)
/*     */       {
/*  62 */         worldborder.setTransition(d0, d2, i);
/*     */         
/*  64 */         if (d0 > d2)
/*     */         {
/*  66 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
/*     */         }
/*     */         else
/*     */         {
/*  70 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  75 */         worldborder.setTransition(d2);
/*  76 */         notifyOperators(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }) });
/*     */       }
/*     */     }
/*  79 */     else if (args[0].equals("add"))
/*     */     {
/*  81 */       if ((args.length != 2) && (args.length != 3))
/*     */       {
/*  83 */         throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
/*     */       }
/*     */       
/*  86 */       double d4 = worldborder.getDiameter();
/*  87 */       double d8 = d4 + parseDouble(args[1], -d4, 6.0E7D - d4);
/*  88 */       long i1 = worldborder.getTimeUntilTarget() + (args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);
/*     */       
/*  90 */       if (i1 > 0L)
/*     */       {
/*  92 */         worldborder.setTransition(d4, d8, i1);
/*     */         
/*  94 */         if (d4 > d8)
/*     */         {
/*  96 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(i1 / 1000L) });
/*     */         }
/*     */         else
/*     */         {
/* 100 */           notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(i1 / 1000L) });
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 105 */         worldborder.setTransition(d8);
/* 106 */         notifyOperators(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }) });
/*     */       }
/*     */     }
/* 109 */     else if (args[0].equals("center"))
/*     */     {
/* 111 */       if (args.length != 3)
/*     */       {
/* 113 */         throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
/*     */       }
/*     */       
/* 116 */       BlockPos blockpos = sender.getPosition();
/* 117 */       double d1 = parseDouble(blockpos.getX() + 0.5D, args[1], true);
/* 118 */       double d3 = parseDouble(blockpos.getZ() + 0.5D, args[2], true);
/* 119 */       worldborder.setCenter(d1, d3);
/* 120 */       notifyOperators(sender, this, "commands.worldborder.center.success", new Object[] { Double.valueOf(d1), Double.valueOf(d3) });
/*     */     }
/* 122 */     else if (args[0].equals("damage"))
/*     */     {
/* 124 */       if (args.length < 2)
/*     */       {
/* 126 */         throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
/*     */       }
/*     */       
/* 129 */       if (args[1].equals("buffer"))
/*     */       {
/* 131 */         if (args.length != 3)
/*     */         {
/* 133 */           throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
/*     */         }
/*     */         
/* 136 */         double d5 = parseDouble(args[2], 0.0D);
/* 137 */         double d9 = worldborder.getDamageBuffer();
/* 138 */         worldborder.setDamageBuffer(d5);
/* 139 */         notifyOperators(sender, this, "commands.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d5) }), String.format("%.1f", new Object[] { Double.valueOf(d9) }) });
/*     */       }
/* 141 */       else if (args[1].equals("amount"))
/*     */       {
/* 143 */         if (args.length != 3)
/*     */         {
/* 145 */           throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
/*     */         }
/*     */         
/* 148 */         double d6 = parseDouble(args[2], 0.0D);
/* 149 */         double d10 = worldborder.getDamageAmount();
/* 150 */         worldborder.setDamageAmount(d6);
/* 151 */         notifyOperators(sender, this, "commands.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d6) }), String.format("%.2f", new Object[] { Double.valueOf(d10) }) });
/*     */       }
/*     */     }
/* 154 */     else if (args[0].equals("warning"))
/*     */     {
/* 156 */       if (args.length < 2)
/*     */       {
/* 158 */         throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
/*     */       }
/*     */       
/* 161 */       int j = parseInt(args[2], 0);
/*     */       
/* 163 */       if (args[1].equals("time"))
/*     */       {
/* 165 */         if (args.length != 3)
/*     */         {
/* 167 */           throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
/*     */         }
/*     */         
/* 170 */         int k = worldborder.getWarningTime();
/* 171 */         worldborder.setWarningTime(j);
/* 172 */         notifyOperators(sender, this, "commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k) });
/*     */       }
/* 174 */       else if (args[1].equals("distance"))
/*     */       {
/* 176 */         if (args.length != 3)
/*     */         {
/* 178 */           throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
/*     */         }
/*     */         
/* 181 */         int l = worldborder.getWarningDistance();
/* 182 */         worldborder.setWarningDistance(j);
/* 183 */         notifyOperators(sender, this, "commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(j), Integer.valueOf(l) });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 188 */       if (!args[0].equals("get"))
/*     */       {
/* 190 */         throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
/*     */       }
/*     */       
/* 193 */       double d7 = worldborder.getDiameter();
/* 194 */       sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(d7 + 0.5D));
/* 195 */       sender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(d7) }) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected WorldBorder getWorldBorder()
/*     */   {
/* 202 */     return net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldBorder();
/*     */   }
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 207 */     return (args.length == 2) && (args[0].equals("warning")) ? getListOfStringsMatchingLastWord(args, new String[] { "time", "distance" }) : (args.length >= 2) && (args.length <= 3) && (args[0].equals("center")) ? func_181043_b(args, 1, pos) : (args.length == 2) && (args[0].equals("damage")) ? getListOfStringsMatchingLastWord(args, new String[] { "buffer", "amount" }) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "set", "center", "damage", "warning", "add", "get" }) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */