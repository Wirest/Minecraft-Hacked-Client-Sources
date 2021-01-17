// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.crash;

import net.minecraft.util.ReportedException;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import optifine.CrashReporter;
import org.apache.commons.io.IOUtils;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.ArrayUtils;
import optifine.Reflector;
import net.minecraft.world.gen.layer.IntCache;
import java.util.Iterator;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import optifine.CrashReportCpu;
import java.util.concurrent.Callable;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class CrashReport
{
    private static final Logger logger;
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory theReportCategory;
    private final List crashReportSections;
    private File crashReportFile;
    private boolean field_85059_f;
    private StackTraceElement[] stacktrace;
    private static final String __OBFID = "CL_00000990";
    private boolean reported;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public CrashReport(final String descriptionIn, final Throwable causeThrowable) {
        this.theReportCategory = new CrashReportCategory(this, "System Details");
        this.crashReportSections = Lists.newArrayList();
        this.field_85059_f = true;
        this.stacktrace = new StackTraceElement[0];
        this.reported = false;
        this.description = descriptionIn;
        this.cause = causeThrowable;
        this.populateEnvironment();
    }
    
    private void populateEnvironment() {
        this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable() {
            private static final String __OBFID = "CL_00001197";
            
            @Override
            public String call() {
                return "1.8.8";
            }
        });
        this.theReportCategory.addCrashSectionCallable("Operating System", new Callable() {
            private static final String __OBFID = "CL_00001222";
            
            @Override
            public String call() {
                return String.valueOf(System.getProperty("os.name")) + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.theReportCategory.addCrashSectionCallable("CPU", new CrashReportCpu());
        this.theReportCategory.addCrashSectionCallable("Java Version", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.version")) + ", " + System.getProperty("java.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable() {
            private static final String __OBFID = "CL_00001275";
            
            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.vm.name")) + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Memory", new Callable() {
            private static final String __OBFID = "CL_00001302";
            
            @Override
            public String call() {
                final Runtime runtime = Runtime.getRuntime();
                final long i = runtime.maxMemory();
                final long j = runtime.totalMemory();
                final long k = runtime.freeMemory();
                final long l = i / 1024L / 1024L;
                final long i2 = j / 1024L / 1024L;
                final long j2 = k / 1024L / 1024L;
                return String.valueOf(k) + " bytes (" + j2 + " MB) / " + j + " bytes (" + i2 + " MB) up to " + i + " bytes (" + l + " MB)";
            }
        });
        this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable() {
            private static final String __OBFID = "CL_00001329";
            
            @Override
            public String call() {
                final RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                final List list = runtimemxbean.getInputArguments();
                int i = 0;
                final StringBuilder stringbuilder = new StringBuilder();
                for (final Object s : list) {
                    if (((String)s).startsWith("-X")) {
                        if (i++ > 0) {
                            stringbuilder.append(" ");
                        }
                        stringbuilder.append(s);
                    }
                }
                return String.format("%d total; %s", i, stringbuilder.toString());
            }
        });
        this.theReportCategory.addCrashSectionCallable("IntCache", new Callable() {
            private static final String __OBFID = "CL_00001355";
            
            @Override
            public String call() throws Exception {
                return IntCache.getCacheSizes();
            }
        });
        if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, this, this.theReportCategory);
        }
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Throwable getCrashCause() {
        return this.cause;
    }
    
    public void getSectionsInStringBuilder(final StringBuilder builder) {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
            this.stacktrace = ArrayUtils.subarray(this.crashReportSections.get(0).getStackTrace(), 0, 1);
        }
        if (this.stacktrace != null && this.stacktrace.length > 0) {
            builder.append("-- Head --\n");
            builder.append("Stacktrace:\n");
            StackTraceElement[] stacktrace;
            for (int length = (stacktrace = this.stacktrace).length, i = 0; i < length; ++i) {
                final StackTraceElement stacktraceelement = stacktrace[i];
                builder.append("\t").append("at ").append(stacktraceelement.toString());
                builder.append("\n");
            }
            builder.append("\n");
        }
        for (final Object crashreportcategory : this.crashReportSections) {
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
            }
            else if (object instanceof StackOverflowError) {
                object = new StackOverflowError(this.description);
            }
            else if (object instanceof OutOfMemoryError) {
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
        }
        finally {
            IOUtils.closeQuietly(stringwriter);
            IOUtils.closeQuietly(printwriter);
        }
        IOUtils.closeQuietly(stringwriter);
        IOUtils.closeQuietly(printwriter);
        return s;
    }
    
    public String getCompleteReport() {
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport(this, this.theReportCategory);
        }
        final StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        Reflector.call(Reflector.BlamingTransformer_onCrash, stringbuilder);
        Reflector.call(Reflector.CoreModManager_onCrash, stringbuilder);
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append(new SimpleDateFormat().format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int i = 0; i < 87; ++i) {
            stringbuilder.append("-");
        }
        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }
    
    public File getFile() {
        return this.crashReportFile;
    }
    
    public boolean saveToFile(final File toFile) {
        if (this.crashReportFile != null) {
            return false;
        }
        if (toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
        }
        try {
            final FileWriter filewriter = new FileWriter(toFile);
            filewriter.write(this.getCompleteReport());
            filewriter.close();
            this.crashReportFile = toFile;
            return true;
        }
        catch (Throwable throwable) {
            CrashReport.logger.error("Could not save crash report to " + toFile, throwable);
            return false;
        }
    }
    
    public CrashReportCategory getCategory() {
        return this.theReportCategory;
    }
    
    public CrashReportCategory makeCategory(final String name) {
        return this.makeCategoryDepth(name, 1);
    }
    
    public CrashReportCategory makeCategoryDepth(final String categoryName, final int stacktraceLength) {
        final CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
        if (this.field_85059_f) {
            final int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
            final StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement2 = null;
            final int j = astacktraceelement.length - i;
            if (j < 0) {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
            }
            if (astacktraceelement != null && j >= 0 && j < astacktraceelement.length) {
                stacktraceelement = astacktraceelement[j];
                if (astacktraceelement.length + 1 - i < astacktraceelement.length) {
                    stacktraceelement2 = astacktraceelement[astacktraceelement.length + 1 - i];
                }
            }
            this.field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement2);
            if (i > 0 && !this.crashReportSections.isEmpty()) {
                final CrashReportCategory crashreportcategory2 = this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashreportcategory2.trimStackTraceEntriesFromBottom(i);
            }
            else if (astacktraceelement != null && astacktraceelement.length >= i && j >= 0 && j < astacktraceelement.length) {
                System.arraycopy(astacktraceelement, 0, this.stacktrace = new StackTraceElement[j], 0, this.stacktrace.length);
            }
            else {
                this.field_85059_f = false;
            }
        }
        this.crashReportSections.add(crashreportcategory);
        return crashreportcategory;
    }
    
    private static String getWittyComment() {
        final String[] astring = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
        try {
            return astring[(int)(System.nanoTime() % astring.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }
    
    public static CrashReport makeCrashReport(final Throwable causeIn, final String descriptionIn) {
        CrashReport crashreport;
        if (causeIn instanceof ReportedException) {
            crashreport = ((ReportedException)causeIn).getCrashReport();
        }
        else {
            crashreport = new CrashReport(descriptionIn, causeIn);
        }
        return crashreport;
    }
}
