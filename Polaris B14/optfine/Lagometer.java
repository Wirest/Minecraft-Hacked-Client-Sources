/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiIngame;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ 
/*     */ public class Lagometer
/*     */ {
/*     */   private static Minecraft mc;
/*     */   private static GameSettings gameSettings;
/*     */   private static Profiler profiler;
/*  19 */   public static boolean active = false;
/*  20 */   public static TimerNano timerTick = new TimerNano();
/*  21 */   public static TimerNano timerScheduledExecutables = new TimerNano();
/*  22 */   public static TimerNano timerChunkUpload = new TimerNano();
/*  23 */   public static TimerNano timerChunkUpdate = new TimerNano();
/*  24 */   public static TimerNano timerVisibility = new TimerNano();
/*  25 */   public static TimerNano timerTerrain = new TimerNano();
/*  26 */   public static TimerNano timerServer = new TimerNano();
/*  27 */   private static long[] timesFrame = new long['Ȁ'];
/*  28 */   private static long[] timesTick = new long['Ȁ'];
/*  29 */   private static long[] timesScheduledExecutables = new long['Ȁ'];
/*  30 */   private static long[] timesChunkUpload = new long['Ȁ'];
/*  31 */   private static long[] timesChunkUpdate = new long['Ȁ'];
/*  32 */   private static long[] timesVisibility = new long['Ȁ'];
/*  33 */   private static long[] timesTerrain = new long['Ȁ'];
/*  34 */   private static long[] timesServer = new long['Ȁ'];
/*  35 */   private static boolean[] gcs = new boolean['Ȁ'];
/*  36 */   private static int numRecordedFrameTimes = 0;
/*  37 */   private static long prevFrameTimeNano = -1L;
/*  38 */   private static long renderTimeNano = 0L;
/*  39 */   private static long memTimeStartMs = System.currentTimeMillis();
/*  40 */   private static long memStart = getMemoryUsed();
/*  41 */   private static long memTimeLast = memTimeStartMs;
/*  42 */   private static long memLast = memStart;
/*  43 */   private static long memTimeDiffMs = 1L;
/*  44 */   private static long memDiff = 0L;
/*  45 */   private static int memMbSec = 0;
/*     */   
/*     */   public static boolean updateMemoryAllocation()
/*     */   {
/*  49 */     long i = System.currentTimeMillis();
/*  50 */     long j = getMemoryUsed();
/*  51 */     boolean flag = false;
/*     */     
/*  53 */     if (j < memLast)
/*     */     {
/*  55 */       double d0 = memDiff / 1000000.0D;
/*  56 */       double d1 = memTimeDiffMs / 1000.0D;
/*  57 */       int k = (int)(d0 / d1);
/*     */       
/*  59 */       if (k > 0)
/*     */       {
/*  61 */         memMbSec = k;
/*     */       }
/*     */       
/*  64 */       memTimeStartMs = i;
/*  65 */       memStart = j;
/*  66 */       memTimeDiffMs = 0L;
/*  67 */       memDiff = 0L;
/*  68 */       flag = true;
/*     */     }
/*     */     else
/*     */     {
/*  72 */       memTimeDiffMs = i - memTimeStartMs;
/*  73 */       memDiff = j - memStart;
/*     */     }
/*     */     
/*  76 */     memTimeLast = i;
/*  77 */     memLast = j;
/*  78 */     return flag;
/*     */   }
/*     */   
/*     */   private static long getMemoryUsed()
/*     */   {
/*  83 */     Runtime runtime = Runtime.getRuntime();
/*  84 */     return runtime.totalMemory() - runtime.freeMemory();
/*     */   }
/*     */   
/*     */   public static void updateLagometer()
/*     */   {
/*  89 */     if (mc == null)
/*     */     {
/*  91 */       mc = Minecraft.getMinecraft();
/*  92 */       gameSettings = mc.gameSettings;
/*  93 */       profiler = mc.mcProfiler;
/*     */     }
/*     */     
/*  96 */     if ((gameSettings.showDebugInfo) && (gameSettings.ofLagometer))
/*     */     {
/*  98 */       active = true;
/*  99 */       long i = System.nanoTime();
/*     */       
/* 101 */       if (prevFrameTimeNano == -1L)
/*     */       {
/* 103 */         prevFrameTimeNano = i;
/*     */       }
/*     */       else
/*     */       {
/* 107 */         int j = numRecordedFrameTimes & timesFrame.length - 1;
/* 108 */         numRecordedFrameTimes += 1;
/* 109 */         boolean flag = updateMemoryAllocation();
/* 110 */         timesFrame[j] = (i - prevFrameTimeNano - renderTimeNano);
/* 111 */         timesTick[j] = timerTick.timeNano;
/* 112 */         timesScheduledExecutables[j] = timerScheduledExecutables.timeNano;
/* 113 */         timesChunkUpload[j] = timerChunkUpload.timeNano;
/* 114 */         timesChunkUpdate[j] = timerChunkUpdate.timeNano;
/* 115 */         timesVisibility[j] = timerVisibility.timeNano;
/* 116 */         timesTerrain[j] = timerTerrain.timeNano;
/* 117 */         timesServer[j] = timerServer.timeNano;
/* 118 */         gcs[j] = flag;
/* 119 */         timerTick.reset();
/* 120 */         timerScheduledExecutables.reset();
/* 121 */         timerVisibility.reset();
/* 122 */         timerChunkUpdate.reset();
/* 123 */         timerChunkUpload.reset();
/* 124 */         timerTerrain.reset();
/* 125 */         timerServer.reset();
/* 126 */         prevFrameTimeNano = System.nanoTime();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 131 */       active = false;
/* 132 */       prevFrameTimeNano = -1L;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void showLagometer(ScaledResolution p_showLagometer_0_)
/*     */   {
/* 138 */     if ((gameSettings != null) && (gameSettings.ofLagometer))
/*     */     {
/* 140 */       long i = System.nanoTime();
/* 141 */       GlStateManager.clear(256);
/* 142 */       GlStateManager.matrixMode(5889);
/* 143 */       GlStateManager.pushMatrix();
/* 144 */       GlStateManager.enableColorMaterial();
/* 145 */       GlStateManager.loadIdentity();
/* 146 */       GlStateManager.ortho(0.0D, mc.displayWidth, mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
/* 147 */       GlStateManager.matrixMode(5888);
/* 148 */       GlStateManager.pushMatrix();
/* 149 */       GlStateManager.loadIdentity();
/* 150 */       GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 151 */       org.lwjgl.opengl.GL11.glLineWidth(1.0F);
/* 152 */       GlStateManager.disableTexture2D();
/* 153 */       Tessellator tessellator = Tessellator.getInstance();
/* 154 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 155 */       worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
/*     */       
/* 157 */       for (int j = 0; j < timesFrame.length; j++)
/*     */       {
/* 159 */         int k = (j - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
/* 160 */         k += 155;
/* 161 */         float f = mc.displayHeight;
/* 162 */         long l = 0L;
/*     */         
/* 164 */         if (gcs[j] != 0)
/*     */         {
/* 166 */           renderTime(j, timesFrame[j], k, k / 2, 0, f, worldrenderer);
/*     */         }
/*     */         else
/*     */         {
/* 170 */           renderTime(j, timesFrame[j], k, k, k, f, worldrenderer);
/* 171 */           f -= (float)renderTime(j, timesServer[j], k / 2, k / 2, k / 2, f, worldrenderer);
/* 172 */           f -= (float)renderTime(j, timesTerrain[j], 0, k, 0, f, worldrenderer);
/* 173 */           f -= (float)renderTime(j, timesVisibility[j], k, k, 0, f, worldrenderer);
/* 174 */           f -= (float)renderTime(j, timesChunkUpdate[j], k, 0, 0, f, worldrenderer);
/* 175 */           f -= (float)renderTime(j, timesChunkUpload[j], k, 0, k, f, worldrenderer);
/* 176 */           f -= (float)renderTime(j, timesScheduledExecutables[j], 0, 0, k, f, worldrenderer);
/* 177 */           float f1 = f - (float)renderTime(j, timesTick[j], 0, k, k, f, worldrenderer);
/*     */         }
/*     */       }
/*     */       
/* 181 */       tessellator.draw();
/* 182 */       GlStateManager.matrixMode(5889);
/* 183 */       GlStateManager.popMatrix();
/* 184 */       GlStateManager.matrixMode(5888);
/* 185 */       GlStateManager.popMatrix();
/* 186 */       GlStateManager.enableTexture2D();
/* 187 */       float f1 = 1.0F - (float)((System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
/* 188 */       f1 = Config.limit(f1, 0.0F, 1.0F);
/* 189 */       int l1 = (int)(170.0F + f1 * 85.0F);
/* 190 */       int i2 = (int)(100.0F + f1 * 55.0F);
/* 191 */       int j2 = (int)(10.0F + f1 * 10.0F);
/* 192 */       int i1 = l1 << 16 | i2 << 8 | j2;
/* 193 */       int j1 = 512 / p_showLagometer_0_.getScaleFactor() + 2;
/* 194 */       int k1 = mc.displayHeight / p_showLagometer_0_.getScaleFactor() - 8;
/* 195 */       GuiIngame guiingame = mc.ingameGUI;
/* 196 */       GuiIngame.drawRect(j1 - 1, k1 - 1, j1 + 50, k1 + 10, -1605349296);
/* 197 */       mc.fontRendererObj.drawString(" " + memMbSec + " MB/s", j1, k1, i1);
/* 198 */       renderTimeNano = System.nanoTime() - i;
/*     */     }
/*     */   }
/*     */   
/*     */   private static long renderTime(int p_renderTime_0_, long p_renderTime_1_, int p_renderTime_3_, int p_renderTime_4_, int p_renderTime_5_, float p_renderTime_6_, WorldRenderer p_renderTime_7_)
/*     */   {
/* 204 */     long i = p_renderTime_1_ / 200000L;
/*     */     
/* 206 */     if (i < 3L)
/*     */     {
/* 208 */       return 0L;
/*     */     }
/*     */     
/*     */ 
/* 212 */     p_renderTime_7_.pos(p_renderTime_0_ + 0.5F, p_renderTime_6_ - (float)i + 0.5F, 0.0D).color(p_renderTime_3_, p_renderTime_4_, p_renderTime_5_, 255).endVertex();
/* 213 */     p_renderTime_7_.pos(p_renderTime_0_ + 0.5F, p_renderTime_6_ + 0.5F, 0.0D).color(p_renderTime_3_, p_renderTime_4_, p_renderTime_5_, 255).endVertex();
/* 214 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isActive()
/*     */   {
/* 220 */     return active;
/*     */   }
/*     */   
/*     */   public static class TimerNano
/*     */   {
/* 225 */     public long timeStartNano = 0L;
/* 226 */     public long timeNano = 0L;
/*     */     
/*     */     public void start()
/*     */     {
/* 230 */       if (Lagometer.active)
/*     */       {
/* 232 */         if (this.timeStartNano == 0L)
/*     */         {
/* 234 */           this.timeStartNano = System.nanoTime();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void end()
/*     */     {
/* 241 */       if (Lagometer.active)
/*     */       {
/* 243 */         if (this.timeStartNano != 0L)
/*     */         {
/* 245 */           this.timeNano += System.nanoTime() - this.timeStartNano;
/* 246 */           this.timeStartNano = 0L;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void reset()
/*     */     {
/* 253 */       this.timeNano = 0L;
/* 254 */       this.timeStartNano = 0L;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Lagometer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */