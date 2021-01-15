/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.List;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import net.minecraft.profiler.Profiler.Result;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandDebug extends CommandBase
/*     */ {
/*  16 */   private static final Logger logger = ;
/*     */   
/*     */   private long field_147206_b;
/*     */   
/*     */   private int field_147207_c;
/*     */   
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/*  25 */     return "debug";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  33 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  41 */     return "commands.debug.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  49 */     if (args.length < 1)
/*     */     {
/*  51 */       throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  55 */     if (args[0].equals("start"))
/*     */     {
/*  57 */       if (args.length != 1)
/*     */       {
/*  59 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  62 */       notifyOperators(sender, this, "commands.debug.start", new Object[0]);
/*  63 */       MinecraftServer.getServer().enableProfiling();
/*  64 */       this.field_147206_b = MinecraftServer.getCurrentTimeMillis();
/*  65 */       this.field_147207_c = MinecraftServer.getServer().getTickCounter();
/*     */     }
/*     */     else
/*     */     {
/*  69 */       if (!args[0].equals("stop"))
/*     */       {
/*  71 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  74 */       if (args.length != 1)
/*     */       {
/*  76 */         throw new WrongUsageException("commands.debug.usage", new Object[0]);
/*     */       }
/*     */       
/*  79 */       if (!MinecraftServer.getServer().theProfiler.profilingEnabled)
/*     */       {
/*  81 */         throw new CommandException("commands.debug.notStarted", new Object[0]);
/*     */       }
/*     */       
/*  84 */       long i = MinecraftServer.getCurrentTimeMillis();
/*  85 */       int j = MinecraftServer.getServer().getTickCounter();
/*  86 */       long k = i - this.field_147206_b;
/*  87 */       int l = j - this.field_147207_c;
/*  88 */       func_147205_a(k, l);
/*  89 */       MinecraftServer.getServer().theProfiler.profilingEnabled = false;
/*  90 */       notifyOperators(sender, this, "commands.debug.stop", new Object[] { Float.valueOf((float)k / 1000.0F), Integer.valueOf(l) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_147205_a(long p_147205_1_, int p_147205_3_)
/*     */   {
/*  97 */     File file1 = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new java.util.Date()) + ".txt");
/*  98 */     file1.getParentFile().mkdirs();
/*     */     
/*     */     try
/*     */     {
/* 102 */       FileWriter filewriter = new FileWriter(file1);
/* 103 */       filewriter.write(func_147204_b(p_147205_1_, p_147205_3_));
/* 104 */       filewriter.close();
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 108 */       logger.error("Could not save profiler results to " + file1, throwable);
/*     */     }
/*     */   }
/*     */   
/*     */   private String func_147204_b(long p_147204_1_, int p_147204_3_)
/*     */   {
/* 114 */     StringBuilder stringbuilder = new StringBuilder();
/* 115 */     stringbuilder.append("---- Minecraft Profiler Results ----\n");
/* 116 */     stringbuilder.append("// ");
/* 117 */     stringbuilder.append(func_147203_d());
/* 118 */     stringbuilder.append("\n\n");
/* 119 */     stringbuilder.append("Time span: ").append(p_147204_1_).append(" ms\n");
/* 120 */     stringbuilder.append("Tick span: ").append(p_147204_3_).append(" ticks\n");
/* 121 */     stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[] { Float.valueOf(p_147204_3_ / ((float)p_147204_1_ / 1000.0F)) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
/* 122 */     stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
/* 123 */     func_147202_a(0, "root", stringbuilder);
/* 124 */     stringbuilder.append("--- END PROFILE DUMP ---\n\n");
/* 125 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   private void func_147202_a(int p_147202_1_, String p_147202_2_, StringBuilder p_147202_3_)
/*     */   {
/* 130 */     List<Profiler.Result> list = MinecraftServer.getServer().theProfiler.getProfilingData(p_147202_2_);
/*     */     
/* 132 */     if ((list != null) && (list.size() >= 3))
/*     */     {
/* 134 */       for (int i = 1; i < list.size(); i++)
/*     */       {
/* 136 */         Profiler.Result profiler$result = (Profiler.Result)list.get(i);
/* 137 */         p_147202_3_.append(String.format("[%02d] ", new Object[] { Integer.valueOf(p_147202_1_) }));
/*     */         
/* 139 */         for (int j = 0; j < p_147202_1_; j++)
/*     */         {
/* 141 */           p_147202_3_.append(" ");
/*     */         }
/*     */         
/* 144 */         p_147202_3_.append(profiler$result.field_76331_c).append(" - ").append(String.format("%.2f", new Object[] { Double.valueOf(profiler$result.field_76332_a) })).append("%/").append(String.format("%.2f", new Object[] { Double.valueOf(profiler$result.field_76330_b) })).append("%\n");
/*     */         
/* 146 */         if (!profiler$result.field_76331_c.equals("unspecified"))
/*     */         {
/*     */           try
/*     */           {
/* 150 */             func_147202_a(p_147202_1_ + 1, p_147202_2_ + "." + profiler$result.field_76331_c, p_147202_3_);
/*     */           }
/*     */           catch (Exception exception)
/*     */           {
/* 154 */             p_147202_3_.append("[[ EXCEPTION ").append(exception).append(" ]]");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static String func_147203_d()
/*     */   {
/* 163 */     String[] astring = { "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
/*     */     
/*     */     try
/*     */     {
/* 167 */       return astring[((int)(System.nanoTime() % astring.length))];
/*     */     }
/*     */     catch (Throwable var2) {}
/*     */     
/* 171 */     return "Witty comment unavailable :(";
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 177 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "start", "stop" }) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */