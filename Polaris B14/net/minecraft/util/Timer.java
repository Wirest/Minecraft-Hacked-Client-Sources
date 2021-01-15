/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Timer
/*     */ {
/*     */   float ticksPerSecond;
/*     */   private double lastHRTime;
/*     */   public int elapsedTicks;
/*     */   public float renderPartialTicks;
/*  30 */   public float timerSpeed = 1.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float elapsedPartialTicks;
/*     */   
/*     */ 
/*     */ 
/*     */   private long lastSyncSysClock;
/*     */   
/*     */ 
/*     */ 
/*     */   private long lastSyncHRClock;
/*     */   
/*     */ 
/*     */ 
/*     */   private long field_74285_i;
/*     */   
/*     */ 
/*     */ 
/*  51 */   public double timeSyncAdjustment = 1.0D;
/*     */   
/*     */   public Timer(float p_i1018_1_)
/*     */   {
/*  55 */     this.ticksPerSecond = p_i1018_1_;
/*  56 */     this.lastSyncSysClock = Minecraft.getSystemTime();
/*  57 */     this.lastSyncHRClock = (System.nanoTime() / 1000000L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTimer()
/*     */   {
/*  65 */     long i = Minecraft.getSystemTime();
/*  66 */     long j = i - this.lastSyncSysClock;
/*  67 */     long k = System.nanoTime() / 1000000L;
/*  68 */     double d0 = k / 1000.0D;
/*     */     
/*  70 */     if ((j <= 1000L) && (j >= 0L))
/*     */     {
/*  72 */       this.field_74285_i += j;
/*     */       
/*  74 */       if (this.field_74285_i > 1000L)
/*     */       {
/*  76 */         long l = k - this.lastSyncHRClock;
/*  77 */         double d1 = this.field_74285_i / l;
/*  78 */         this.timeSyncAdjustment += (d1 - this.timeSyncAdjustment) * 0.20000000298023224D;
/*  79 */         this.lastSyncHRClock = k;
/*  80 */         this.field_74285_i = 0L;
/*     */       }
/*     */       
/*  83 */       if (this.field_74285_i < 0L)
/*     */       {
/*  85 */         this.lastSyncHRClock = k;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  90 */       this.lastHRTime = d0;
/*     */     }
/*     */     
/*  93 */     this.lastSyncSysClock = i;
/*  94 */     double d2 = (d0 - this.lastHRTime) * this.timeSyncAdjustment;
/*  95 */     this.lastHRTime = d0;
/*  96 */     d2 = MathHelper.clamp_double(d2, 0.0D, 1.0D);
/*  97 */     this.elapsedPartialTicks = ((float)(this.elapsedPartialTicks + d2 * this.timerSpeed * this.ticksPerSecond));
/*  98 */     this.elapsedTicks = ((int)this.elapsedPartialTicks);
/*  99 */     this.elapsedPartialTicks -= this.elapsedTicks;
/*     */     
/* 101 */     if (this.elapsedTicks > 10)
/*     */     {
/* 103 */       this.elapsedTicks = 10;
/*     */     }
/*     */     
/* 106 */     this.renderPartialTicks = this.elapsedPartialTicks;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */