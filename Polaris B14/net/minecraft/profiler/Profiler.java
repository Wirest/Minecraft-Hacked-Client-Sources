/*     */ package net.minecraft.profiler;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import optfine.Config;
/*     */ import optfine.Lagometer;
/*     */ import optfine.Lagometer.TimerNano;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class Profiler
/*     */ {
/*  19 */   private static final Logger logger = ;
/*     */   
/*     */ 
/*  22 */   private final List sectionList = Lists.newArrayList();
/*     */   
/*     */ 
/*  25 */   private final List timestampList = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   public boolean profilingEnabled;
/*     */   
/*     */ 
/*  31 */   private String profilingSection = "";
/*     */   
/*     */ 
/*  34 */   private final Map profilingMap = Maps.newHashMap();
/*     */   private static final String __OBFID = "CL_00001497";
/*  36 */   public boolean profilerGlobalEnabled = true;
/*     */   private boolean profilerLocalEnabled;
/*     */   private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
/*     */   private static final String TICK = "tick";
/*     */   private static final String PRE_RENDER_ERRORS = "preRenderErrors";
/*     */   private static final String RENDER = "render";
/*     */   private static final String DISPLAY = "display";
/*  43 */   private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
/*  44 */   private static final int HASH_TICK = "tick".hashCode();
/*  45 */   private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
/*  46 */   private static final int HASH_RENDER = "render".hashCode();
/*  47 */   private static final int HASH_DISPLAY = "display".hashCode();
/*     */   
/*     */   public Profiler()
/*     */   {
/*  51 */     this.profilerLocalEnabled = this.profilerGlobalEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearProfiling()
/*     */   {
/*  59 */     this.profilingMap.clear();
/*  60 */     this.profilingSection = "";
/*  61 */     this.sectionList.clear();
/*  62 */     this.profilerLocalEnabled = this.profilerGlobalEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startSection(String name)
/*     */   {
/*  70 */     if (Lagometer.isActive())
/*     */     {
/*  72 */       int i = name.hashCode();
/*     */       
/*  74 */       if ((i == HASH_SCHEDULED_EXECUTABLES) && (name.equals("scheduledExecutables")))
/*     */       {
/*  76 */         Lagometer.timerScheduledExecutables.start();
/*     */       }
/*  78 */       else if ((i == HASH_TICK) && (name.equals("tick")) && (Config.isMinecraftThread()))
/*     */       {
/*  80 */         Lagometer.timerScheduledExecutables.end();
/*  81 */         Lagometer.timerTick.start();
/*     */       }
/*  83 */       else if ((i == HASH_PRE_RENDER_ERRORS) && (name.equals("preRenderErrors")))
/*     */       {
/*  85 */         Lagometer.timerTick.end();
/*     */       }
/*     */     }
/*     */     
/*  89 */     if (Config.isFastRender())
/*     */     {
/*  91 */       int j = name.hashCode();
/*     */       
/*  93 */       if ((j == HASH_RENDER) && (name.equals("render")))
/*     */       {
/*  95 */         net.minecraft.client.renderer.GlStateManager.clearEnabled = false;
/*     */       }
/*  97 */       else if ((j == HASH_DISPLAY) && (name.equals("display")))
/*     */       {
/*  99 */         net.minecraft.client.renderer.GlStateManager.clearEnabled = true;
/*     */       }
/*     */     }
/*     */     
/* 103 */     if (this.profilerLocalEnabled)
/*     */     {
/* 105 */       if (this.profilingEnabled)
/*     */       {
/* 107 */         if (this.profilingSection.length() > 0)
/*     */         {
/* 109 */           this.profilingSection += ".";
/*     */         }
/*     */         
/* 112 */         this.profilingSection += name;
/* 113 */         this.sectionList.add(this.profilingSection);
/* 114 */         this.timestampList.add(Long.valueOf(System.nanoTime()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endSection()
/*     */   {
/* 124 */     if (this.profilerLocalEnabled)
/*     */     {
/* 126 */       if (this.profilingEnabled)
/*     */       {
/* 128 */         long i = System.nanoTime();
/* 129 */         long j = ((Long)this.timestampList.remove(this.timestampList.size() - 1)).longValue();
/* 130 */         this.sectionList.remove(this.sectionList.size() - 1);
/* 131 */         long k = i - j;
/*     */         
/* 133 */         if (this.profilingMap.containsKey(this.profilingSection))
/*     */         {
/* 135 */           this.profilingMap.put(this.profilingSection, Long.valueOf(((Long)this.profilingMap.get(this.profilingSection)).longValue() + k));
/*     */         }
/*     */         else
/*     */         {
/* 139 */           this.profilingMap.put(this.profilingSection, Long.valueOf(k));
/*     */         }
/*     */         
/* 142 */         if (k > 100000000L)
/*     */         {
/* 144 */           logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + k / 1000000.0D + " ms");
/*     */         }
/*     */         
/* 147 */         this.profilingSection = (!this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List getProfilingData(String p_76321_1_)
/*     */   {
/* 157 */     this.profilerLocalEnabled = this.profilerGlobalEnabled;
/*     */     
/* 159 */     if (!this.profilerLocalEnabled)
/*     */     {
/* 161 */       return new ArrayList(Arrays.asList(new Result[] { new Result("root", 0.0D, 0.0D) }));
/*     */     }
/* 163 */     if (!this.profilingEnabled)
/*     */     {
/* 165 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 169 */     long i = this.profilingMap.containsKey("root") ? ((Long)this.profilingMap.get("root")).longValue() : 0L;
/* 170 */     long j = this.profilingMap.containsKey(p_76321_1_) ? ((Long)this.profilingMap.get(p_76321_1_)).longValue() : -1L;
/* 171 */     ArrayList arraylist = Lists.newArrayList();
/*     */     
/* 173 */     if (p_76321_1_.length() > 0)
/*     */     {
/* 175 */       p_76321_1_ = p_76321_1_ + ".";
/*     */     }
/*     */     
/* 178 */     long k = 0L;
/*     */     
/* 180 */     for (Object s : this.profilingMap.keySet())
/*     */     {
/* 182 */       if ((((String)s).length() > p_76321_1_.length()) && (((String)s).startsWith(p_76321_1_)) && (((String)s).indexOf(".", p_76321_1_.length() + 1) < 0))
/*     */       {
/* 184 */         k += ((Long)this.profilingMap.get(s)).longValue();
/*     */       }
/*     */     }
/*     */     
/* 188 */     float f = (float)k;
/*     */     
/* 190 */     if (k < j)
/*     */     {
/* 192 */       k = j;
/*     */     }
/*     */     
/* 195 */     if (i < k)
/*     */     {
/* 197 */       i = k;
/*     */     }
/*     */     
/* 200 */     for (Object s10 : this.profilingMap.keySet())
/*     */     {
/* 202 */       String s1 = (String)s10;
/*     */       
/* 204 */       if ((s1.length() > p_76321_1_.length()) && (s1.startsWith(p_76321_1_)) && (s1.indexOf(".", p_76321_1_.length() + 1) < 0))
/*     */       {
/* 206 */         long l = ((Long)this.profilingMap.get(s1)).longValue();
/* 207 */         double d0 = l * 100.0D / k;
/* 208 */         double d1 = l * 100.0D / i;
/* 209 */         String s2 = s1.substring(p_76321_1_.length());
/* 210 */         arraylist.add(new Result(s2, d0, d1));
/*     */       }
/*     */     }
/*     */     
/* 214 */     for (Object s3 : this.profilingMap.keySet())
/*     */     {
/* 216 */       this.profilingMap.put(s3, Long.valueOf(((Long)this.profilingMap.get(s3)).longValue() * 950L / 1000L));
/*     */     }
/*     */     
/* 219 */     if ((float)k > f)
/*     */     {
/* 221 */       arraylist.add(new Result("unspecified", ((float)k - f) * 100.0D / k, ((float)k - f) * 100.0D / i));
/*     */     }
/*     */     
/* 224 */     Collections.sort(arraylist);
/* 225 */     arraylist.add(0, new Result(p_76321_1_, 100.0D, k * 100.0D / i));
/* 226 */     return arraylist;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endStartSection(String name)
/*     */   {
/* 235 */     if (this.profilerLocalEnabled)
/*     */     {
/* 237 */       endSection();
/* 238 */       startSection(name);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getNameOfLastSection()
/*     */   {
/* 244 */     return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
/*     */   }
/*     */   
/*     */   public static final class Result implements Comparable
/*     */   {
/*     */     public double field_76332_a;
/*     */     public double field_76330_b;
/*     */     public String field_76331_c;
/*     */     private static final String __OBFID = "CL_00001498";
/*     */     
/*     */     public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_)
/*     */     {
/* 256 */       this.field_76331_c = p_i1554_1_;
/* 257 */       this.field_76332_a = p_i1554_2_;
/* 258 */       this.field_76330_b = p_i1554_4_;
/*     */     }
/*     */     
/*     */     public int compareTo(Result p_compareTo_1_)
/*     */     {
/* 263 */       return p_compareTo_1_.field_76332_a > this.field_76332_a ? 1 : p_compareTo_1_.field_76332_a < this.field_76332_a ? -1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c);
/*     */     }
/*     */     
/*     */     public int func_76329_a()
/*     */     {
/* 268 */       return (this.field_76331_c.hashCode() & 0xAAAAAA) + 4473924;
/*     */     }
/*     */     
/*     */     public int compareTo(Object p_compareTo_1_)
/*     */     {
/* 273 */       return compareTo((Result)p_compareTo_1_);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\profiler\Profiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */