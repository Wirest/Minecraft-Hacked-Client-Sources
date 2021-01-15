/*     */ package ch.qos.logback.classic.pattern;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.util.LevelToSyslogSeverity;
/*     */ import ch.qos.logback.core.net.SyslogAppenderBase;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
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
/*     */ public class SyslogStartConverter
/*     */   extends ClassicConverter
/*     */ {
/*  29 */   long lastTimestamp = -1L;
/*  30 */   String timesmapStr = null;
/*     */   SimpleDateFormat simpleMonthFormat;
/*     */   SimpleDateFormat simpleTimeFormat;
/*  33 */   private final Calendar calendar = Calendar.getInstance(Locale.US);
/*     */   String localHostName;
/*     */   int facility;
/*     */   
/*     */   public void start()
/*     */   {
/*  39 */     int errorCount = 0;
/*     */     
/*  41 */     String facilityStr = getFirstOption();
/*  42 */     if (facilityStr == null) {
/*  43 */       addError("was expecting a facility string as an option");
/*  44 */       return;
/*     */     }
/*     */     
/*  47 */     this.facility = SyslogAppenderBase.facilityStringToint(facilityStr);
/*     */     
/*  49 */     this.localHostName = getLocalHostname();
/*     */     try
/*     */     {
/*  52 */       this.simpleMonthFormat = new SimpleDateFormat("MMM", Locale.US);
/*  53 */       this.simpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
/*     */     } catch (IllegalArgumentException e) {
/*  55 */       addError("Could not instantiate SimpleDateFormat", e);
/*  56 */       errorCount++;
/*     */     }
/*     */     
/*  59 */     if (errorCount == 0) {
/*  60 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public String convert(ILoggingEvent event) {
/*  65 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  67 */     int pri = this.facility + LevelToSyslogSeverity.convert(event);
/*     */     
/*  69 */     sb.append("<");
/*  70 */     sb.append(pri);
/*  71 */     sb.append(">");
/*  72 */     sb.append(computeTimeStampString(event.getTimeStamp()));
/*  73 */     sb.append(' ');
/*  74 */     sb.append(this.localHostName);
/*  75 */     sb.append(' ');
/*     */     
/*  77 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalHostname()
/*     */   {
/*     */     try
/*     */     {
/*  88 */       InetAddress addr = InetAddress.getLocalHost();
/*  89 */       return addr.getHostName();
/*     */     } catch (UnknownHostException uhe) {
/*  91 */       addError("Could not determine local host name", uhe); }
/*  92 */     return "UNKNOWN_LOCALHOST";
/*     */   }
/*     */   
/*     */   String computeTimeStampString(long now)
/*     */   {
/*  97 */     synchronized (this)
/*     */     {
/*     */ 
/* 100 */       if (now / 1000L != this.lastTimestamp) {
/* 101 */         this.lastTimestamp = (now / 1000L);
/* 102 */         Date nowDate = new Date(now);
/* 103 */         this.calendar.setTime(nowDate);
/* 104 */         this.timesmapStr = String.format("%s %2d %s", new Object[] { this.simpleMonthFormat.format(nowDate), Integer.valueOf(this.calendar.get(5)), this.simpleTimeFormat.format(nowDate) });
/*     */       }
/*     */       
/* 107 */       return this.timesmapStr;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\SyslogStartConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */