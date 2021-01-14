package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class CrashReportCategory {
   private final CrashReport crashReport;
   private final String name;
   private final List children = Lists.newArrayList();
   private StackTraceElement[] stackTrace = new StackTraceElement[0];

   public CrashReportCategory(CrashReport report, String name) {
      this.crashReport = report;
      this.name = name;
   }

   public static String getCoordinateInfo(double x, double y, double z) {
      return String.format("%.2f,%.2f,%.2f - %s", x, y, z, getCoordinateInfo(new BlockPos(x, y, z)));
   }

   public static String getCoordinateInfo(BlockPos pos) {
      int i = pos.getX();
      int j = pos.getY();
      int k = pos.getZ();
      StringBuilder stringbuilder = new StringBuilder();

      try {
         stringbuilder.append(String.format("World: (%d,%d,%d)", i, j, k));
      } catch (Throwable var17) {
         stringbuilder.append("(Error finding world loc)");
      }

      stringbuilder.append(", ");

      int j3;
      int k3;
      int l3;
      int i4;
      int j4;
      int k4;
      int l4;
      int i5;
      int j5;
      try {
         j3 = i >> 4;
         k3 = k >> 4;
         l3 = i & 15;
         i4 = j >> 4;
         j4 = k & 15;
         k4 = j3 << 4;
         l4 = k3 << 4;
         i5 = (j3 + 1 << 4) - 1;
         j5 = (k3 + 1 << 4) - 1;
         stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", l3, i4, j4, j3, k3, k4, l4, i5, j5));
      } catch (Throwable var16) {
         stringbuilder.append("(Error finding chunk loc)");
      }

      stringbuilder.append(", ");

      try {
         j3 = i >> 9;
         k3 = k >> 9;
         l3 = j3 << 5;
         i4 = k3 << 5;
         j4 = (j3 + 1 << 5) - 1;
         k4 = (k3 + 1 << 5) - 1;
         l4 = j3 << 9;
         i5 = k3 << 9;
         j5 = (j3 + 1 << 9) - 1;
         int i3 = (k3 + 1 << 9) - 1;
         stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", j3, k3, l3, i4, j4, k4, l4, i5, j5, i3));
      } catch (Throwable var15) {
         stringbuilder.append("(Error finding world loc)");
      }

      return stringbuilder.toString();
   }

   public void addCrashSectionCallable(String sectionName, Callable callable) {
      try {
         this.addCrashSection(sectionName, callable.call());
      } catch (Throwable var4) {
         this.addCrashSectionThrowable(sectionName, var4);
      }

   }

   public void addCrashSection(String sectionName, Object value) {
      this.children.add(new CrashReportCategory.Entry(sectionName, value));
   }

   public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
      this.addCrashSection(sectionName, throwable);
   }

   public int getPrunedStackTrace(int size) {
      StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
      if (astacktraceelement.length <= 0) {
         return 0;
      } else {
         this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - size];
         System.arraycopy(astacktraceelement, 3 + size, this.stackTrace, 0, this.stackTrace.length);
         return this.stackTrace.length;
      }
   }

   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2) {
      if (this.stackTrace.length != 0 && s1 != null) {
         StackTraceElement stacktraceelement = this.stackTrace[0];
         if (stacktraceelement.isNativeMethod() == s1.isNativeMethod() && stacktraceelement.getClassName().equals(s1.getClassName()) && stacktraceelement.getFileName().equals(s1.getFileName()) && stacktraceelement.getMethodName().equals(s1.getMethodName())) {
            if (s2 != null != this.stackTrace.length > 1) {
               return false;
            } else if (s2 != null && !this.stackTrace[1].equals(s2)) {
               return false;
            } else {
               this.stackTrace[0] = s1;
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void trimStackTraceEntriesFromBottom(int amount) {
      StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
      System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
      this.stackTrace = astacktraceelement;
   }

   public void appendToStringBuilder(StringBuilder builder) {
      builder.append("-- ").append(this.name).append(" --\n");
      builder.append("Details:");
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         CrashReportCategory.Entry crashreportcategory$entry = (CrashReportCategory.Entry)var2.next();
         builder.append("\n\t");
         builder.append(crashreportcategory$entry.getKey());
         builder.append(": ");
         builder.append(crashreportcategory$entry.getValue());
      }

      if (this.stackTrace != null && this.stackTrace.length > 0) {
         builder.append("\nStacktrace:");
         StackTraceElement[] var6 = this.stackTrace;
         int var7 = var6.length;

         for(int var4 = 0; var4 < var7; ++var4) {
            StackTraceElement stacktraceelement = var6[var4];
            builder.append("\n\tat ");
            builder.append(stacktraceelement.toString());
         }
      }

   }

   public StackTraceElement[] getStackTrace() {
      return this.stackTrace;
   }

   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
      final int i = Block.getIdFromBlock(blockIn);
      category.addCrashSectionCallable("Block type", new Callable() {
         public String call() throws Exception {
            try {
               return String.format("ID #%d (%s // %s)", i, blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
            } catch (Throwable var2) {
               return "ID #" + i;
            }
         }
      });
      category.addCrashSectionCallable("Block data value", new Callable() {
         public String call() throws Exception {
            if (blockData < 0) {
               return "Unknown? (Got " + blockData + ")";
            } else {
               String s = String.format("%4s", Integer.toBinaryString(blockData)).replace(" ", "0");
               return String.format("%1$d / 0x%1$X / 0b%2$s", blockData, s);
            }
         }
      });
      category.addCrashSectionCallable("Block location", new Callable() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(pos);
         }
      });
   }

   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state) {
      category.addCrashSectionCallable("Block", new Callable() {
         public String call() throws Exception {
            return state.toString();
         }
      });
      category.addCrashSectionCallable("Block location", new Callable() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(pos);
         }
      });
   }

   static class Entry {
      private final String key;
      private final String value;

      public Entry(String key, Object value) {
         this.key = key;
         if (value == null) {
            this.value = "~~NULL~~";
         } else if (value instanceof Throwable) {
            Throwable throwable = (Throwable)value;
            this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
         } else {
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
