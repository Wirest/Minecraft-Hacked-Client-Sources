/*     */ package ch.qos.logback.classic.turbo;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.slf4j.MDC;
/*     */ import org.slf4j.Marker;
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
/*     */ public class DynamicThresholdFilter
/*     */   extends TurboFilter
/*     */ {
/* 128 */   private Map<String, Level> valueLevelMap = new HashMap();
/* 129 */   private Level defaultThreshold = Level.ERROR;
/*     */   
/*     */   private String key;
/* 132 */   private FilterReply onHigherOrEqual = FilterReply.NEUTRAL;
/* 133 */   private FilterReply onLower = FilterReply.DENY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getKey()
/*     */   {
/* 141 */     return this.key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setKey(String key)
/*     */   {
/* 148 */     this.key = key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Level getDefaultThreshold()
/*     */   {
/* 157 */     return this.defaultThreshold;
/*     */   }
/*     */   
/*     */   public void setDefaultThreshold(Level defaultThreshold) {
/* 161 */     this.defaultThreshold = defaultThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterReply getOnHigherOrEqual()
/*     */   {
/* 171 */     return this.onHigherOrEqual;
/*     */   }
/*     */   
/*     */   public void setOnHigherOrEqual(FilterReply onHigherOrEqual) {
/* 175 */     this.onHigherOrEqual = onHigherOrEqual;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterReply getOnLower()
/*     */   {
/* 185 */     return this.onLower;
/*     */   }
/*     */   
/*     */   public void setOnLower(FilterReply onLower) {
/* 189 */     this.onLower = onLower;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addMDCValueLevelPair(MDCValueLevelPair mdcValueLevelPair)
/*     */   {
/* 196 */     if (this.valueLevelMap.containsKey(mdcValueLevelPair.getValue())) {
/* 197 */       addError(mdcValueLevelPair.getValue() + " has been already set");
/*     */     } else {
/* 199 */       this.valueLevelMap.put(mdcValueLevelPair.getValue(), mdcValueLevelPair.getLevel());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 209 */     if (this.key == null) {
/* 210 */       addError("No key name was specified");
/*     */     }
/* 212 */     super.start();
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable)
/*     */   {
/* 239 */     String mdcValue = MDC.get(this.key);
/* 240 */     if (!isStarted()) {
/* 241 */       return FilterReply.NEUTRAL;
/*     */     }
/*     */     
/* 244 */     Level levelAssociatedWithMDCValue = null;
/* 245 */     if (mdcValue != null) {
/* 246 */       levelAssociatedWithMDCValue = (Level)this.valueLevelMap.get(mdcValue);
/*     */     }
/* 248 */     if (levelAssociatedWithMDCValue == null) {
/* 249 */       levelAssociatedWithMDCValue = this.defaultThreshold;
/*     */     }
/* 251 */     if (level.isGreaterOrEqual(levelAssociatedWithMDCValue)) {
/* 252 */       return this.onHigherOrEqual;
/*     */     }
/* 254 */     return this.onLower;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\DynamicThresholdFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */