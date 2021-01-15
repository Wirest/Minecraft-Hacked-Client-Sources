/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.core.spi.FilterReply;
/*    */ import org.slf4j.Marker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DuplicateMessageFilter
/*    */   extends TurboFilter
/*    */ {
/*    */   public static final int DEFAULT_CACHE_SIZE = 100;
/*    */   public static final int DEFAULT_ALLOWED_REPETITIONS = 5;
/* 41 */   public int allowedRepetitions = 5;
/* 42 */   public int cacheSize = 100;
/*    */   
/*    */   private LRUMessageCache msgCache;
/*    */   
/*    */   public void start()
/*    */   {
/* 48 */     this.msgCache = new LRUMessageCache(this.cacheSize);
/* 49 */     super.start();
/*    */   }
/*    */   
/*    */   public void stop()
/*    */   {
/* 54 */     this.msgCache.clear();
/* 55 */     this.msgCache = null;
/* 56 */     super.stop();
/*    */   }
/*    */   
/*    */ 
/*    */   public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*    */   {
/* 62 */     int count = this.msgCache.getMessageCountAndThenIncrement(format);
/* 63 */     if (count <= this.allowedRepetitions) {
/* 64 */       return FilterReply.NEUTRAL;
/*    */     }
/* 66 */     return FilterReply.DENY;
/*    */   }
/*    */   
/*    */   public int getAllowedRepetitions()
/*    */   {
/* 71 */     return this.allowedRepetitions;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAllowedRepetitions(int allowedRepetitions)
/*    */   {
/* 80 */     this.allowedRepetitions = allowedRepetitions;
/*    */   }
/*    */   
/*    */   public int getCacheSize() {
/* 84 */     return this.cacheSize;
/*    */   }
/*    */   
/*    */   public void setCacheSize(int cacheSize) {
/* 88 */     this.cacheSize = cacheSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\DuplicateMessageFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */