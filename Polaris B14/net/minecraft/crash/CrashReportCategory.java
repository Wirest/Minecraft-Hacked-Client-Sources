/*     */ package net.minecraft.crash;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CrashReportCategory
/*     */ {
/*     */   private final CrashReport crashReport;
/*     */   private final String name;
/*  14 */   private final List<Entry> children = Lists.newArrayList();
/*  15 */   private StackTraceElement[] stackTrace = new StackTraceElement[0];
/*     */   
/*     */   public CrashReportCategory(CrashReport report, String name)
/*     */   {
/*  19 */     this.crashReport = report;
/*  20 */     this.name = name;
/*     */   }
/*     */   
/*     */   public static String getCoordinateInfo(double x, double y, double z)
/*     */   {
/*  25 */     return String.format("%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), getCoordinateInfo(new BlockPos(x, y, z)) });
/*     */   }
/*     */   
/*     */   public static String getCoordinateInfo(BlockPos pos)
/*     */   {
/*  30 */     int i = pos.getX();
/*  31 */     int j = pos.getY();
/*  32 */     int k = pos.getZ();
/*  33 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*     */     try
/*     */     {
/*  37 */       stringbuilder.append(String.format("World: (%d,%d,%d)", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k) }));
/*     */     }
/*     */     catch (Throwable var17)
/*     */     {
/*  41 */       stringbuilder.append("(Error finding world loc)");
/*     */     }
/*     */     
/*  44 */     stringbuilder.append(", ");
/*     */     
/*     */     try
/*     */     {
/*  48 */       int l = i >> 4;
/*  49 */       int i1 = k >> 4;
/*  50 */       int j1 = i & 0xF;
/*  51 */       int k1 = j >> 4;
/*  52 */       int l1 = k & 0xF;
/*  53 */       int i2 = l << 4;
/*  54 */       int j2 = i1 << 4;
/*  55 */       int k2 = (l + 1 << 4) - 1;
/*  56 */       int l2 = (i1 + 1 << 4) - 1;
/*  57 */       stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2) }));
/*     */     }
/*     */     catch (Throwable var16)
/*     */     {
/*  61 */       stringbuilder.append("(Error finding chunk loc)");
/*     */     }
/*     */     
/*  64 */     stringbuilder.append(", ");
/*     */     
/*     */     try
/*     */     {
/*  68 */       int j3 = i >> 9;
/*  69 */       int k3 = k >> 9;
/*  70 */       int l3 = j3 << 5;
/*  71 */       int i4 = k3 << 5;
/*  72 */       int j4 = (j3 + 1 << 5) - 1;
/*  73 */       int k4 = (k3 + 1 << 5) - 1;
/*  74 */       int l4 = j3 << 9;
/*  75 */       int i5 = k3 << 9;
/*  76 */       int j5 = (j3 + 1 << 9) - 1;
/*  77 */       int i3 = (k3 + 1 << 9) - 1;
/*  78 */       stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(j3), Integer.valueOf(k3), Integer.valueOf(l3), Integer.valueOf(i4), Integer.valueOf(j4), Integer.valueOf(k4), Integer.valueOf(l4), Integer.valueOf(i5), Integer.valueOf(j5), Integer.valueOf(i3) }));
/*     */     }
/*     */     catch (Throwable var15)
/*     */     {
/*  82 */       stringbuilder.append("(Error finding world loc)");
/*     */     }
/*     */     
/*  85 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCrashSectionCallable(String sectionName, Callable<String> callable)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       addCrashSection(sectionName, callable.call());
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*  99 */       addCrashSectionThrowable(sectionName, throwable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCrashSection(String sectionName, Object value)
/*     */   {
/* 108 */     this.children.add(new Entry(sectionName, value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCrashSectionThrowable(String sectionName, Throwable throwable)
/*     */   {
/* 116 */     addCrashSection(sectionName, throwable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPrunedStackTrace(int size)
/*     */   {
/* 125 */     StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
/*     */     
/* 127 */     if (astacktraceelement.length <= 0)
/*     */     {
/* 129 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 133 */     this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - size];
/* 134 */     System.arraycopy(astacktraceelement, 3 + size, this.stackTrace, 0, this.stackTrace.length);
/* 135 */     return this.stackTrace.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2)
/*     */   {
/* 144 */     if ((this.stackTrace.length != 0) && (s1 != null))
/*     */     {
/* 146 */       StackTraceElement stacktraceelement = this.stackTrace[0];
/*     */       
/* 148 */       if ((stacktraceelement.isNativeMethod() == s1.isNativeMethod()) && (stacktraceelement.getClassName().equals(s1.getClassName())) && (stacktraceelement.getFileName().equals(s1.getFileName())) && (stacktraceelement.getMethodName().equals(s1.getMethodName())))
/*     */       {
/* 150 */         if ((s2 != null ? 1 : 0) != (this.stackTrace.length > 1 ? 1 : 0))
/*     */         {
/* 152 */           return false;
/*     */         }
/* 154 */         if ((s2 != null) && (!this.stackTrace[1].equals(s2)))
/*     */         {
/* 156 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 160 */         this.stackTrace[0] = s1;
/* 161 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 166 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 171 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trimStackTraceEntriesFromBottom(int amount)
/*     */   {
/* 180 */     StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
/* 181 */     System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
/* 182 */     this.stackTrace = astacktraceelement;
/*     */   }
/*     */   
/*     */   public void appendToStringBuilder(StringBuilder builder)
/*     */   {
/* 187 */     builder.append("-- ").append(this.name).append(" --\n");
/* 188 */     builder.append("Details:");
/*     */     
/* 190 */     for (Entry crashreportcategory$entry : this.children)
/*     */     {
/* 192 */       builder.append("\n\t");
/* 193 */       builder.append(crashreportcategory$entry.getKey());
/* 194 */       builder.append(": ");
/* 195 */       builder.append(crashreportcategory$entry.getValue());
/*     */     }
/*     */     
/* 198 */     if ((this.stackTrace != null) && (this.stackTrace.length > 0))
/*     */     {
/* 200 */       builder.append("\nStacktrace:");
/*     */       StackTraceElement[] arrayOfStackTraceElement;
/* 202 */       int j = (arrayOfStackTraceElement = this.stackTrace).length; for (int i = 0; i < j; i++) { StackTraceElement stacktraceelement = arrayOfStackTraceElement[i];
/*     */         
/* 204 */         builder.append("\n\tat ");
/* 205 */         builder.append(stacktraceelement.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStackTrace()
/*     */   {
/* 212 */     return this.stackTrace;
/*     */   }
/*     */   
/*     */   public static void addBlockInfo(CrashReportCategory category, BlockPos pos, final Block blockIn, int blockData)
/*     */   {
/* 217 */     int i = Block.getIdFromBlock(blockIn);
/* 218 */     category.addCrashSectionCallable("Block type", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/*     */         try
/*     */         {
/* 224 */           return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(this.val$i), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName() });
/*     */         }
/*     */         catch (Throwable var2) {}
/*     */         
/* 228 */         return "ID #" + this.val$i;
/*     */       }
/*     */       
/* 231 */     });
/* 232 */     category.addCrashSectionCallable("Block data value", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 236 */         if (this.val$blockData < 0)
/*     */         {
/* 238 */           return "Unknown? (Got " + this.val$blockData + ")";
/*     */         }
/*     */         
/*     */ 
/* 242 */         String s = String.format("%4s", new Object[] { Integer.toBinaryString(this.val$blockData) }).replace(" ", "0");
/* 243 */         return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(this.val$blockData), s });
/*     */       }
/*     */       
/* 246 */     });
/* 247 */     category.addCrashSectionCallable("Block location", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 251 */         return CrashReportCategory.getCoordinateInfo(CrashReportCategory.this);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static void addBlockInfo(CrashReportCategory category, BlockPos pos, IBlockState state)
/*     */   {
/* 258 */     category.addCrashSectionCallable("Block", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 262 */         return CrashReportCategory.this.toString();
/*     */       }
/* 264 */     });
/* 265 */     category.addCrashSectionCallable("Block location", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 269 */         return CrashReportCategory.getCoordinateInfo(CrashReportCategory.this);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   static class Entry
/*     */   {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public Entry(String key, Object value)
/*     */     {
/* 281 */       this.key = key;
/*     */       
/* 283 */       if (value == null)
/*     */       {
/* 285 */         this.value = "~~NULL~~";
/*     */       }
/* 287 */       else if ((value instanceof Throwable))
/*     */       {
/* 289 */         Throwable throwable = (Throwable)value;
/* 290 */         this.value = ("~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
/*     */       }
/*     */       else
/*     */       {
/* 294 */         this.value = value.toString();
/*     */       }
/*     */     }
/*     */     
/*     */     public String getKey()
/*     */     {
/* 300 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getValue()
/*     */     {
/* 305 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\crash\CrashReportCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */