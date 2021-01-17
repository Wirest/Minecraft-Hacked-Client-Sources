// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.crash;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.concurrent.Callable;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import java.util.List;

public class CrashReportCategory
{
    private final CrashReport crashReport;
    private final String name;
    private final List<Entry> children;
    private StackTraceElement[] stackTrace;
    
    public CrashReportCategory(final CrashReport report, final String name) {
        this.children = (List<Entry>)Lists.newArrayList();
        this.stackTrace = new StackTraceElement[0];
        this.crashReport = report;
        this.name = name;
    }
    
    public static String getCoordinateInfo(final double x, final double y, final double z) {
        return String.format("%.2f,%.2f,%.2f - %s", x, y, z, getCoordinateInfo(new BlockPos(x, y, z)));
    }
    
    public static String getCoordinateInfo(final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        final StringBuilder stringbuilder = new StringBuilder();
        try {
            stringbuilder.append(String.format("World: (%d,%d,%d)", i, j, k));
        }
        catch (Throwable var17) {
            stringbuilder.append("(Error finding world loc)");
        }
        stringbuilder.append(", ");
        try {
            final int l = i >> 4;
            final int i2 = k >> 4;
            final int j2 = i & 0xF;
            final int k2 = j >> 4;
            final int l2 = k & 0xF;
            final int i3 = l << 4;
            final int j3 = i2 << 4;
            final int k3 = (l + 1 << 4) - 1;
            final int l3 = (i2 + 1 << 4) - 1;
            stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", j2, k2, l2, l, i2, i3, j3, k3, l3));
        }
        catch (Throwable var18) {
            stringbuilder.append("(Error finding chunk loc)");
        }
        stringbuilder.append(", ");
        try {
            final int j4 = i >> 9;
            final int k4 = k >> 9;
            final int l4 = j4 << 5;
            final int i4 = k4 << 5;
            final int j5 = (j4 + 1 << 5) - 1;
            final int k5 = (k4 + 1 << 5) - 1;
            final int l5 = j4 << 9;
            final int i5 = k4 << 9;
            final int j6 = (j4 + 1 << 9) - 1;
            final int i6 = (k4 + 1 << 9) - 1;
            stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", j4, k4, l4, i4, j5, k5, l5, i5, j6, i6));
        }
        catch (Throwable var19) {
            stringbuilder.append("(Error finding world loc)");
        }
        return stringbuilder.toString();
    }
    
    public void addCrashSectionCallable(final String sectionName, final Callable<String> callable) {
        try {
            this.addCrashSection(sectionName, callable.call());
        }
        catch (Throwable throwable) {
            this.addCrashSectionThrowable(sectionName, throwable);
        }
    }
    
    public void addCrashSection(final String sectionName, final Object value) {
        this.children.add(new Entry(sectionName, value));
    }
    
    public void addCrashSectionThrowable(final String sectionName, final Throwable throwable) {
        this.addCrashSection(sectionName, throwable);
    }
    
    public int getPrunedStackTrace(final int size) {
        final StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        if (astacktraceelement.length <= 0) {
            return 0;
        }
        System.arraycopy(astacktraceelement, 3 + size, this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - size], 0, this.stackTrace.length);
        return this.stackTrace.length;
    }
    
    public boolean firstTwoElementsOfStackTraceMatch(final StackTraceElement s1, final StackTraceElement s2) {
        if (this.stackTrace.length == 0 || s1 == null) {
            return false;
        }
        final StackTraceElement stacktraceelement = this.stackTrace[0];
        if (stacktraceelement.isNativeMethod() != s1.isNativeMethod() || !stacktraceelement.getClassName().equals(s1.getClassName()) || !stacktraceelement.getFileName().equals(s1.getFileName()) || !stacktraceelement.getMethodName().equals(s1.getMethodName())) {
            return false;
        }
        if (s2 != null != this.stackTrace.length > 1) {
            return false;
        }
        if (s2 != null && !this.stackTrace[1].equals(s2)) {
            return false;
        }
        this.stackTrace[0] = s1;
        return true;
    }
    
    public void trimStackTraceEntriesFromBottom(final int amount) {
        final StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
        System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
        this.stackTrace = astacktraceelement;
    }
    
    public void appendToStringBuilder(final StringBuilder builder) {
        builder.append("-- ").append(this.name).append(" --\n");
        builder.append("Details:");
        for (final Entry crashreportcategory$entry : this.children) {
            builder.append("\n\t");
            builder.append(crashreportcategory$entry.getKey());
            builder.append(": ");
            builder.append(crashreportcategory$entry.getValue());
        }
        if (this.stackTrace != null && this.stackTrace.length > 0) {
            builder.append("\nStacktrace:");
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = this.stackTrace).length, i = 0; i < length; ++i) {
                final StackTraceElement stacktraceelement = stackTrace[i];
                builder.append("\n\tat ");
                builder.append(stacktraceelement.toString());
            }
        }
    }
    
    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }
    
    public static void addBlockInfo(final CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
        final int i = Block.getIdFromBlock(blockIn);
        category.addCrashSectionCallable("Block type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return String.format("ID #%d (%s // %s)", i, blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
                }
                catch (Throwable var2) {
                    return "ID #" + i;
                }
            }
        });
        category.addCrashSectionCallable("Block data value", new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (blockData < 0) {
                    return "Unknown? (Got " + blockData + ")";
                }
                final String s = String.format("%4s", Integer.toBinaryString(blockData)).replace(" ", "0");
                return String.format("%1$d / 0x%1$X / 0b%2$s", blockData, s);
            }
        });
        category.addCrashSectionCallable("Block location", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
        });
    }
    
    public static void addBlockInfo(final CrashReportCategory category, final BlockPos pos, final IBlockState state) {
        category.addCrashSectionCallable("Block", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return state.toString();
            }
        });
        category.addCrashSectionCallable("Block location", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
        });
    }
    
    static class Entry
    {
        private final String key;
        private final String value;
        
        public Entry(final String key, final Object value) {
            this.key = key;
            if (value == null) {
                this.value = "~~NULL~~";
            }
            else if (value instanceof Throwable) {
                final Throwable throwable = (Throwable)value;
                this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            }
            else {
                this.value = value.toString();
            }
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
