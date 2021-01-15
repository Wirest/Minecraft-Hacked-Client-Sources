/*     */ package net.minecraft.profiler;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ 
/*     */ public class PlayerUsageSnooper
/*     */ {
/*  18 */   private final Map<String, Object> field_152773_a = Maps.newHashMap();
/*  19 */   private final Map<String, Object> field_152774_b = Maps.newHashMap();
/*  20 */   private final String uniqueID = UUID.randomUUID().toString();
/*     */   
/*     */ 
/*     */   private final URL serverUrl;
/*     */   
/*     */   private final IPlayerUsage playerStatsCollector;
/*     */   
/*  27 */   private final Timer threadTrigger = new Timer("Snooper Timer", true);
/*  28 */   private final Object syncLock = new Object();
/*     */   
/*     */   private final long minecraftStartTimeMilis;
/*     */   
/*     */   private boolean isRunning;
/*     */   private int selfCounter;
/*     */   
/*     */   public PlayerUsageSnooper(String p_i1563_1_, IPlayerUsage playerStatCollector, long startTime)
/*     */   {
/*     */     try
/*     */     {
/*  39 */       this.serverUrl = new URL("http://snoop.minecraft.net/" + p_i1563_1_ + "?version=" + 2);
/*     */     }
/*     */     catch (MalformedURLException var6)
/*     */     {
/*  43 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  46 */     this.playerStatsCollector = playerStatCollector;
/*  47 */     this.minecraftStartTimeMilis = startTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startSnooper()
/*     */   {
/*  55 */     if (!this.isRunning)
/*     */     {
/*  57 */       this.isRunning = true;
/*  58 */       func_152766_h();
/*  59 */       this.threadTrigger.schedule(new TimerTask()
/*     */       {
/*     */         public void run()
/*     */         {
/*  63 */           if (PlayerUsageSnooper.this.playerStatsCollector.isSnooperEnabled())
/*     */           {
/*     */ 
/*     */ 
/*  67 */             synchronized (PlayerUsageSnooper.this.syncLock)
/*     */             {
/*  69 */               Map<String, Object> map = Maps.newHashMap(PlayerUsageSnooper.this.field_152774_b);
/*     */               
/*  71 */               if (PlayerUsageSnooper.this.selfCounter == 0)
/*     */               {
/*  73 */                 map.putAll(PlayerUsageSnooper.this.field_152773_a);
/*     */               }
/*     */               
/*  76 */               map.put("snooper_count", Integer.valueOf(PlayerUsageSnooper.this.selfCounter++));
/*  77 */               map.put("snooper_token", PlayerUsageSnooper.this.uniqueID);
/*     */             }
/*     */             Map<String, Object> map;
/*  80 */             HttpUtil.postMap(PlayerUsageSnooper.this.serverUrl, map, true);
/*     */           }
/*     */         }
/*  83 */       }, 0L, 900000L);
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_152766_h()
/*     */   {
/*  89 */     addJvmArgsToSnooper();
/*  90 */     addClientStat("snooper_token", this.uniqueID);
/*  91 */     addStatToSnooper("snooper_token", this.uniqueID);
/*  92 */     addStatToSnooper("os_name", System.getProperty("os.name"));
/*  93 */     addStatToSnooper("os_version", System.getProperty("os.version"));
/*  94 */     addStatToSnooper("os_architecture", System.getProperty("os.arch"));
/*  95 */     addStatToSnooper("java_version", System.getProperty("java.version"));
/*  96 */     addClientStat("version", "1.8.8");
/*  97 */     this.playerStatsCollector.addServerTypeToSnooper(this);
/*     */   }
/*     */   
/*     */   private void addJvmArgsToSnooper()
/*     */   {
/* 102 */     RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
/* 103 */     List<String> list = runtimemxbean.getInputArguments();
/* 104 */     int i = 0;
/*     */     
/* 106 */     for (String s : list)
/*     */     {
/* 108 */       if (s.startsWith("-X"))
/*     */       {
/* 110 */         addClientStat("jvm_arg[" + i++ + "]", s);
/*     */       }
/*     */     }
/*     */     
/* 114 */     addClientStat("jvm_args", Integer.valueOf(i));
/*     */   }
/*     */   
/*     */   public void addMemoryStatsToSnooper()
/*     */   {
/* 119 */     addStatToSnooper("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
/* 120 */     addStatToSnooper("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
/* 121 */     addStatToSnooper("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
/* 122 */     addStatToSnooper("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
/* 123 */     this.playerStatsCollector.addServerStatsToSnooper(this);
/*     */   }
/*     */   
/*     */   public void addClientStat(String p_152768_1_, Object p_152768_2_)
/*     */   {
/* 128 */     synchronized (this.syncLock)
/*     */     {
/* 130 */       this.field_152774_b.put(p_152768_1_, p_152768_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addStatToSnooper(String p_152767_1_, Object p_152767_2_)
/*     */   {
/* 136 */     synchronized (this.syncLock)
/*     */     {
/* 138 */       this.field_152773_a.put(p_152767_1_, p_152767_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, String> getCurrentStats()
/*     */   {
/* 144 */     Map<String, String> map = Maps.newLinkedHashMap();
/*     */     
/* 146 */     synchronized (this.syncLock)
/*     */     {
/* 148 */       addMemoryStatsToSnooper();
/*     */       
/* 150 */       for (Map.Entry<String, Object> entry : this.field_152773_a.entrySet())
/*     */       {
/* 152 */         map.put((String)entry.getKey(), entry.getValue().toString());
/*     */       }
/*     */       
/* 155 */       for (Map.Entry<String, Object> entry1 : this.field_152774_b.entrySet())
/*     */       {
/* 157 */         map.put((String)entry1.getKey(), entry1.getValue().toString());
/*     */       }
/*     */       
/* 160 */       return map;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSnooperRunning()
/*     */   {
/* 166 */     return this.isRunning;
/*     */   }
/*     */   
/*     */   public void stopSnooper()
/*     */   {
/* 171 */     this.threadTrigger.cancel();
/*     */   }
/*     */   
/*     */   public String getUniqueID()
/*     */   {
/* 176 */     return this.uniqueID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMinecraftStartTimeMillis()
/*     */   {
/* 184 */     return this.minecraftStartTimeMilis;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\profiler\PlayerUsageSnooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */