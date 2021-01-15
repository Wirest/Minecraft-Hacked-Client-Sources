/*     */ package net.minecraft.crash;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.gen.layer.IntCache;
/*     */ import optfine.CrashReporter;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashReport
/*     */ {
/*  29 */   private static final Logger logger = ;
/*     */   
/*     */ 
/*     */   private final String description;
/*     */   
/*     */ 
/*     */   private final Throwable cause;
/*     */   
/*     */ 
/*  38 */   private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
/*     */   
/*     */ 
/*  41 */   private final List crashReportSections = Lists.newArrayList();
/*     */   
/*     */   private File crashReportFile;
/*     */   
/*  45 */   private boolean field_85059_f = true;
/*  46 */   private StackTraceElement[] stacktrace = new StackTraceElement[0];
/*     */   private static final String __OBFID = "CL_00000990";
/*  48 */   private boolean reported = false;
/*     */   
/*     */   public CrashReport(String descriptionIn, Throwable causeThrowable)
/*     */   {
/*  52 */     this.description = descriptionIn;
/*  53 */     this.cause = causeThrowable;
/*  54 */     populateEnvironment();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void populateEnvironment()
/*     */   {
/*  63 */     this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001197";
/*     */       
/*     */       public String call() {
/*  68 */         return "1.8.8";
/*     */       }
/*  70 */     });
/*  71 */     this.theReportCategory.addCrashSectionCallable("Operating System", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001222";
/*     */       
/*     */       public String call() {
/*  76 */         return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
/*     */       }
/*  78 */     });
/*  79 */     this.theReportCategory.addCrashSectionCallable("CPU", new CrashReport3(this));
/*  80 */     this.theReportCategory.addCrashSectionCallable("Java Version", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001248";
/*     */       
/*     */       public String call() {
/*  85 */         return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
/*     */       }
/*  87 */     });
/*  88 */     this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001275";
/*     */       
/*     */       public String call() {
/*  93 */         return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
/*     */       }
/*  95 */     });
/*  96 */     this.theReportCategory.addCrashSectionCallable("Memory", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001302";
/*     */       
/*     */       public String call() {
/* 101 */         Runtime runtime = Runtime.getRuntime();
/* 102 */         long i = runtime.maxMemory();
/* 103 */         long j = runtime.totalMemory();
/* 104 */         long k = runtime.freeMemory();
/* 105 */         long l = i / 1024L / 1024L;
/* 106 */         long i1 = j / 1024L / 1024L;
/* 107 */         long j1 = k / 1024L / 1024L;
/* 108 */         return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
/*     */       }
/* 110 */     });
/* 111 */     this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001329";
/*     */       
/*     */       public String call() throws Exception {
/* 116 */         RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
/* 117 */         List list = runtimemxbean.getInputArguments();
/* 118 */         int i = 0;
/* 119 */         StringBuilder stringbuilder = new StringBuilder();
/*     */         
/* 121 */         for (Object s : list)
/*     */         {
/* 123 */           if (((String)s).startsWith("-X"))
/*     */           {
/* 125 */             if (i++ > 0)
/*     */             {
/* 127 */               stringbuilder.append(" ");
/*     */             }
/*     */             
/* 130 */             stringbuilder.append(s);
/*     */           }
/*     */         }
/*     */         
/* 134 */         return String.format("%d total; %s", new Object[] { Integer.valueOf(i), stringbuilder.toString() });
/*     */       }
/* 136 */     });
/* 137 */     this.theReportCategory.addCrashSectionCallable("IntCache", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00001355";
/*     */       
/*     */       public Object call()
/*     */         throws Exception
/*     */       {
/* 144 */         return IntCache.getCacheSizes();
/*     */       }
/*     */     });
/*     */     
/* 148 */     if (Reflector.FMLCommonHandler_enhanceCrashReport.exists())
/*     */     {
/* 150 */       Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/* 151 */       Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, new Object[] { this, this.theReportCategory });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 160 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getCrashCause()
/*     */   {
/* 168 */     return this.cause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSectionsInStringBuilder(StringBuilder builder)
/*     */   {
/* 176 */     if (((this.stacktrace == null) || (this.stacktrace.length <= 0)) && (this.crashReportSections.size() > 0))
/*     */     {
/* 178 */       this.stacktrace = ((StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1));
/*     */     }
/*     */     
/* 181 */     if ((this.stacktrace != null) && (this.stacktrace.length > 0))
/*     */     {
/* 183 */       builder.append("-- Head --\n");
/* 184 */       builder.append("Stacktrace:\n");
/*     */       StackTraceElement[] arrayOfStackTraceElement;
/* 186 */       int j = (arrayOfStackTraceElement = this.stacktrace).length; for (int i = 0; i < j; i++) { StackTraceElement stacktraceelement = arrayOfStackTraceElement[i];
/*     */         
/* 188 */         builder.append("\t").append("at ").append(stacktraceelement.toString());
/* 189 */         builder.append("\n");
/*     */       }
/*     */       
/* 192 */       builder.append("\n");
/*     */     }
/*     */     
/* 195 */     for (Object crashreportcategory : this.crashReportSections)
/*     */     {
/* 197 */       ((CrashReportCategory)crashreportcategory).appendToStringBuilder(builder);
/* 198 */       builder.append("\n\n");
/*     */     }
/*     */     
/* 201 */     this.theReportCategory.appendToStringBuilder(builder);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String getCauseStackTraceOrString()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: aload_0
/*     */     //   5: getfield 82	net/minecraft/crash/CrashReport:cause	Ljava/lang/Throwable;
/*     */     //   8: astore_3
/*     */     //   9: aload_3
/*     */     //   10: checkcast 218	java/lang/Throwable
/*     */     //   13: invokevirtual 221	java/lang/Throwable:getMessage	()Ljava/lang/String;
/*     */     //   16: ifnonnull +80 -> 96
/*     */     //   19: aload_3
/*     */     //   20: instanceof 223
/*     */     //   23: ifeq +18 -> 41
/*     */     //   26: new 223	java/lang/NullPointerException
/*     */     //   29: dup
/*     */     //   30: aload_0
/*     */     //   31: getfield 80	net/minecraft/crash/CrashReport:description	Ljava/lang/String;
/*     */     //   34: invokespecial 226	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
/*     */     //   37: astore_3
/*     */     //   38: goto +44 -> 82
/*     */     //   41: aload_3
/*     */     //   42: instanceof 232
/*     */     //   45: ifeq +18 -> 63
/*     */     //   48: new 232	java/lang/StackOverflowError
/*     */     //   51: dup
/*     */     //   52: aload_0
/*     */     //   53: getfield 80	net/minecraft/crash/CrashReport:description	Ljava/lang/String;
/*     */     //   56: invokespecial 233	java/lang/StackOverflowError:<init>	(Ljava/lang/String;)V
/*     */     //   59: astore_3
/*     */     //   60: goto +22 -> 82
/*     */     //   63: aload_3
/*     */     //   64: instanceof 235
/*     */     //   67: ifeq +15 -> 82
/*     */     //   70: new 235	java/lang/OutOfMemoryError
/*     */     //   73: dup
/*     */     //   74: aload_0
/*     */     //   75: getfield 80	net/minecraft/crash/CrashReport:description	Ljava/lang/String;
/*     */     //   78: invokespecial 236	java/lang/OutOfMemoryError:<init>	(Ljava/lang/String;)V
/*     */     //   81: astore_3
/*     */     //   82: aload_3
/*     */     //   83: checkcast 218	java/lang/Throwable
/*     */     //   86: aload_0
/*     */     //   87: getfield 82	net/minecraft/crash/CrashReport:cause	Ljava/lang/Throwable;
/*     */     //   90: invokevirtual 237	java/lang/Throwable:getStackTrace	()[Ljava/lang/StackTraceElement;
/*     */     //   93: invokevirtual 241	java/lang/Throwable:setStackTrace	([Ljava/lang/StackTraceElement;)V
/*     */     //   96: aload_3
/*     */     //   97: checkcast 218	java/lang/Throwable
/*     */     //   100: invokevirtual 242	java/lang/Throwable:toString	()Ljava/lang/String;
/*     */     //   103: astore 4
/*     */     //   105: new 228	java/io/StringWriter
/*     */     //   108: dup
/*     */     //   109: invokespecial 243	java/io/StringWriter:<init>	()V
/*     */     //   112: astore_1
/*     */     //   113: new 230	java/io/PrintWriter
/*     */     //   116: dup
/*     */     //   117: aload_1
/*     */     //   118: invokespecial 246	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
/*     */     //   121: astore_2
/*     */     //   122: aload_3
/*     */     //   123: checkcast 218	java/lang/Throwable
/*     */     //   126: aload_2
/*     */     //   127: invokevirtual 250	java/lang/Throwable:printStackTrace	(Ljava/io/PrintWriter;)V
/*     */     //   130: aload_1
/*     */     //   131: invokevirtual 251	java/io/StringWriter:toString	()Ljava/lang/String;
/*     */     //   134: astore 4
/*     */     //   136: goto +16 -> 152
/*     */     //   139: astore 5
/*     */     //   141: aload_1
/*     */     //   142: invokestatic 258	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   145: aload_2
/*     */     //   146: invokestatic 258	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   149: aload 5
/*     */     //   151: athrow
/*     */     //   152: aload_1
/*     */     //   153: invokestatic 258	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   156: aload_2
/*     */     //   157: invokestatic 258	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   160: aload 4
/*     */     //   162: areturn
/*     */     // Line number table:
/*     */     //   Java source line #209	-> byte code offset #0
/*     */     //   Java source line #210	-> byte code offset #2
/*     */     //   Java source line #211	-> byte code offset #4
/*     */     //   Java source line #213	-> byte code offset #9
/*     */     //   Java source line #215	-> byte code offset #19
/*     */     //   Java source line #217	-> byte code offset #26
/*     */     //   Java source line #218	-> byte code offset #38
/*     */     //   Java source line #219	-> byte code offset #41
/*     */     //   Java source line #221	-> byte code offset #48
/*     */     //   Java source line #222	-> byte code offset #60
/*     */     //   Java source line #223	-> byte code offset #63
/*     */     //   Java source line #225	-> byte code offset #70
/*     */     //   Java source line #228	-> byte code offset #82
/*     */     //   Java source line #231	-> byte code offset #96
/*     */     //   Java source line #235	-> byte code offset #105
/*     */     //   Java source line #236	-> byte code offset #113
/*     */     //   Java source line #237	-> byte code offset #122
/*     */     //   Java source line #238	-> byte code offset #130
/*     */     //   Java source line #239	-> byte code offset #136
/*     */     //   Java source line #241	-> byte code offset #139
/*     */     //   Java source line #242	-> byte code offset #141
/*     */     //   Java source line #243	-> byte code offset #145
/*     */     //   Java source line #244	-> byte code offset #149
/*     */     //   Java source line #242	-> byte code offset #152
/*     */     //   Java source line #243	-> byte code offset #156
/*     */     //   Java source line #246	-> byte code offset #160
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	163	0	this	CrashReport
/*     */     //   1	152	1	stringwriter	java.io.StringWriter
/*     */     //   3	154	2	printwriter	java.io.PrintWriter
/*     */     //   8	115	3	object	Object
/*     */     //   103	58	4	s	String
/*     */     //   139	11	5	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   105	139	139	finally
/*     */   }
/*     */   
/*     */   public String getCompleteReport()
/*     */   {
/* 254 */     if (!this.reported)
/*     */     {
/* 256 */       this.reported = true;
/* 257 */       CrashReporter.onCrashReport(this);
/*     */     }
/*     */     
/* 260 */     StringBuilder stringbuilder = new StringBuilder();
/* 261 */     stringbuilder.append("---- Minecraft Crash Report ----\n");
/* 262 */     stringbuilder.append("// ");
/* 263 */     stringbuilder.append(getWittyComment());
/* 264 */     stringbuilder.append("\n\n");
/* 265 */     stringbuilder.append("Time: ");
/* 266 */     stringbuilder.append(new SimpleDateFormat().format(new Date()));
/* 267 */     stringbuilder.append("\n");
/* 268 */     stringbuilder.append("Description: ");
/* 269 */     stringbuilder.append(this.description);
/* 270 */     stringbuilder.append("\n\n");
/* 271 */     stringbuilder.append(getCauseStackTraceOrString());
/* 272 */     stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/*     */     
/* 274 */     for (int i = 0; i < 87; i++)
/*     */     {
/* 276 */       stringbuilder.append("-");
/*     */     }
/*     */     
/* 279 */     stringbuilder.append("\n\n");
/* 280 */     getSectionsInStringBuilder(stringbuilder);
/* 281 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */   {
/* 289 */     return this.crashReportFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveToFile(File toFile)
/*     */   {
/* 297 */     if (this.crashReportFile != null)
/*     */     {
/* 299 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 303 */     if (toFile.getParentFile() != null)
/*     */     {
/* 305 */       toFile.getParentFile().mkdirs();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 310 */       FileWriter filewriter = new FileWriter(toFile);
/* 311 */       filewriter.write(getCompleteReport());
/* 312 */       filewriter.close();
/* 313 */       this.crashReportFile = toFile;
/* 314 */       return true;
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 318 */       logger.error("Could not save crash report to " + toFile, throwable); }
/* 319 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CrashReportCategory getCategory()
/*     */   {
/* 326 */     return this.theReportCategory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrashReportCategory makeCategory(String name)
/*     */   {
/* 334 */     return makeCategoryDepth(name, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength)
/*     */   {
/* 342 */     CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
/*     */     
/* 344 */     if (this.field_85059_f)
/*     */     {
/* 346 */       int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
/* 347 */       StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
/* 348 */       StackTraceElement stacktraceelement = null;
/* 349 */       StackTraceElement stacktraceelement1 = null;
/* 350 */       int j = astacktraceelement.length - i;
/*     */       
/* 352 */       if (j < 0)
/*     */       {
/* 354 */         System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
/*     */       }
/*     */       
/* 357 */       if ((astacktraceelement != null) && (j >= 0) && (j < astacktraceelement.length))
/*     */       {
/* 359 */         stacktraceelement = astacktraceelement[j];
/*     */         
/* 361 */         if (astacktraceelement.length + 1 - i < astacktraceelement.length)
/*     */         {
/* 363 */           stacktraceelement1 = astacktraceelement[(astacktraceelement.length + 1 - i)];
/*     */         }
/*     */       }
/*     */       
/* 367 */       this.field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
/*     */       
/* 369 */       if ((i > 0) && (!this.crashReportSections.isEmpty()))
/*     */       {
/* 371 */         CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
/* 372 */         crashreportcategory1.trimStackTraceEntriesFromBottom(i);
/*     */       }
/* 374 */       else if ((astacktraceelement != null) && (astacktraceelement.length >= i) && (j >= 0) && (j < astacktraceelement.length))
/*     */       {
/* 376 */         this.stacktrace = new StackTraceElement[j];
/* 377 */         System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
/*     */       }
/*     */       else
/*     */       {
/* 381 */         this.field_85059_f = false;
/*     */       }
/*     */     }
/*     */     
/* 385 */     this.crashReportSections.add(crashreportcategory);
/* 386 */     return crashreportcategory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getWittyComment()
/*     */   {
/* 394 */     String[] astring = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
/*     */     
/*     */     try
/*     */     {
/* 398 */       return astring[((int)(System.nanoTime() % astring.length))];
/*     */     }
/*     */     catch (Throwable var2) {}
/*     */     
/* 402 */     return "Witty comment unavailable :(";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn)
/*     */   {
/*     */     CrashReport crashreport;
/*     */     
/*     */     CrashReport crashreport;
/*     */     
/* 413 */     if ((causeIn instanceof ReportedException))
/*     */     {
/* 415 */       crashreport = ((ReportedException)causeIn).getCrashReport();
/*     */     }
/*     */     else
/*     */     {
/* 419 */       crashreport = new CrashReport(descriptionIn, causeIn);
/*     */     }
/*     */     
/* 422 */     return crashreport;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\crash\CrashReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */