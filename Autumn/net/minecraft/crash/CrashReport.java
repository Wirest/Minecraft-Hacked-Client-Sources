package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import optfine.CrashReporter;
import optfine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrashReport {
   private static final Logger logger = LogManager.getLogger();
   private final String description;
   private final Throwable cause;
   private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
   private final List crashReportSections = Lists.newArrayList();
   private File crashReportFile;
   private boolean field_85059_f = true;
   private StackTraceElement[] stacktrace = new StackTraceElement[0];
   private static final String __OBFID = "CL_00000990";
   private boolean reported = false;

   public CrashReport(String descriptionIn, Throwable causeThrowable) {
      this.description = descriptionIn;
      this.cause = causeThrowable;
      this.populateEnvironment();
   }

   private void populateEnvironment() {
      this.theReportCategory.addCrashSectionCallable("Minecraft Version", () -> {
         return "1.8.8";
      });
      this.theReportCategory.addCrashSectionCallable("Operating System", () -> {
         return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
      });
      this.theReportCategory.addCrashSectionCallable("CPU", OpenGlHelper::func_183029_j);
      this.theReportCategory.addCrashSectionCallable("Java Version", () -> {
         return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
      });
      this.theReportCategory.addCrashSectionCallable("Java VM Version", () -> {
         return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
      });
      this.theReportCategory.addCrashSectionCallable("Memory", () -> {
         Runtime runtime = Runtime.getRuntime();
         long i = runtime.maxMemory();
         long j = runtime.totalMemory();
         long k = runtime.freeMemory();
         long l = i / 1024L / 1024L;
         long i1 = j / 1024L / 1024L;
         long j1 = k / 1024L / 1024L;
         return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
      });
      this.theReportCategory.addCrashSectionCallable("JVM Flags", () -> {
         RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
         List list = runtimemxbean.getInputArguments();
         StringBuilder stringbuilder = new StringBuilder();
         int i = 0;
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            String s = (String)var4.next();
            if (s.startsWith("-X")) {
               if (i++ > 0) {
                  stringbuilder.append(" ");
               }

               stringbuilder.append(s);
            }
         }

         return String.format("%d total; %s", i, stringbuilder.toString());
      });
      this.theReportCategory.addCrashSectionCallable("IntCache", IntCache::getCacheSizes);
      if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
         Object object = Reflector.call(Reflector.FMLCommonHandler_instance);
         Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, this, this.theReportCategory);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public Throwable getCrashCause() {
      return this.cause;
   }

   public void getSectionsInStringBuilder(StringBuilder builder) {
      if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
         this.stacktrace = (StackTraceElement[])((StackTraceElement[])((StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1)));
      }

      if (this.stacktrace != null && this.stacktrace.length > 0) {
         builder.append("-- Head --\n");
         builder.append("Stacktrace:\n");
         StackTraceElement[] var2 = this.stacktrace;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            StackTraceElement stacktraceelement = var2[var4];
            builder.append("\t").append("at ").append(stacktraceelement.toString());
            builder.append("\n");
         }

         builder.append("\n");
      }

      Iterator var6 = this.crashReportSections.iterator();

      while(var6.hasNext()) {
         Object crashreportcategory = var6.next();
         ((CrashReportCategory)crashreportcategory).appendToStringBuilder(builder);
         builder.append("\n\n");
      }

      this.theReportCategory.appendToStringBuilder(builder);
   }

   public String getCauseStackTraceOrString() {
      StringWriter stringwriter = null;
      PrintWriter printwriter = null;
      Object object = this.cause;
      if (((Throwable)object).getMessage() == null) {
         if (object instanceof NullPointerException) {
            object = new NullPointerException(this.description);
         } else if (object instanceof StackOverflowError) {
            object = new StackOverflowError(this.description);
         } else if (object instanceof OutOfMemoryError) {
            object = new OutOfMemoryError(this.description);
         }

         ((Throwable)object).setStackTrace(this.cause.getStackTrace());
      }

      String s = ((Throwable)object).toString();

      try {
         stringwriter = new StringWriter();
         printwriter = new PrintWriter(stringwriter);
         ((Throwable)object).printStackTrace(printwriter);
         s = stringwriter.toString();
      } finally {
         IOUtils.closeQuietly(stringwriter);
         IOUtils.closeQuietly(printwriter);
      }

      return s;
   }

   public String getCompleteReport() {
      if (!this.reported) {
         this.reported = true;
         CrashReporter.onCrashReport(this);
      }

      StringBuilder stringbuilder = new StringBuilder();
      stringbuilder.append("---- Minecraft Crash Report ----\n");
      stringbuilder.append("// ");
      stringbuilder.append(getWittyComment());
      stringbuilder.append("\n\n");
      stringbuilder.append("Time: ");
      stringbuilder.append((new SimpleDateFormat()).format(new Date()));
      stringbuilder.append("\n");
      stringbuilder.append("Description: ");
      stringbuilder.append(this.description);
      stringbuilder.append("\n\n");
      stringbuilder.append(this.getCauseStackTraceOrString());
      stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

      for(int i = 0; i < 87; ++i) {
         stringbuilder.append("-");
      }

      stringbuilder.append("\n\n");
      this.getSectionsInStringBuilder(stringbuilder);
      return stringbuilder.toString();
   }

   public File getFile() {
      return this.crashReportFile;
   }

   public boolean saveToFile(File toFile) {
      if (this.crashReportFile != null) {
         return false;
      } else {
         if (toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
         }

         try {
            FileWriter filewriter = new FileWriter(toFile);
            filewriter.write(this.getCompleteReport());
            filewriter.close();
            this.crashReportFile = toFile;
            return true;
         } catch (Throwable var3) {
            logger.error("Could not save crash report to " + toFile, var3);
            return false;
         }
      }
   }

   public CrashReportCategory getCategory() {
      return this.theReportCategory;
   }

   public CrashReportCategory makeCategory(String name) {
      return this.makeCategoryDepth(name, 1);
   }

   public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
      CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
      if (this.field_85059_f) {
         int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
         StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
         StackTraceElement stacktraceelement = null;
         StackTraceElement stacktraceelement1 = null;
         int j = astacktraceelement.length - i;
         if (j < 0) {
            System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
         }

         if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length) {
            stacktraceelement = astacktraceelement[j];
            if (astacktraceelement.length + 1 - i < astacktraceelement.length) {
               stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
            }
         }

         this.field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
         if (i > 0 && !this.crashReportSections.isEmpty()) {
            CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
            crashreportcategory1.trimStackTraceEntriesFromBottom(i);
         } else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length) {
            this.stacktrace = new StackTraceElement[j];
            System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
         } else {
            this.field_85059_f = false;
         }
      }

      this.crashReportSections.add(crashreportcategory);
      return crashreportcategory;
   }

   private static String getWittyComment() {
      String[] astring = new String[]{"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

      try {
         return astring[(int)(System.nanoTime() % (long)astring.length)];
      } catch (Throwable var2) {
         return "Witty comment unavailable :(";
      }
   }

   public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
      CrashReport crashreport;
      if (causeIn instanceof ReportedException) {
         crashreport = ((ReportedException)causeIn).getCrashReport();
      } else {
         crashreport = new CrashReport(descriptionIn, causeIn);
      }

      return crashreport;
   }
}
