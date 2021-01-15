/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MessageFormatter;
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
/*     */ public class LoggingEventVO
/*     */   implements ILoggingEvent, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6553722650255690312L;
/*     */   private static final int NULL_ARGUMENT_ARRAY = -1;
/*     */   private static final String NULL_ARGUMENT_ARRAY_ELEMENT = "NULL_ARGUMENT_ARRAY_ELEMENT";
/*     */   private String threadName;
/*     */   private String loggerName;
/*     */   private LoggerContextVO loggerContextVO;
/*     */   private transient Level level;
/*     */   private String message;
/*     */   private transient String formattedMessage;
/*     */   private transient Object[] argumentArray;
/*     */   private ThrowableProxyVO throwableProxy;
/*     */   private StackTraceElement[] callerDataArray;
/*     */   private Marker marker;
/*     */   private Map<String, String> mdcPropertyMap;
/*     */   private long timeStamp;
/*     */   
/*     */   public static LoggingEventVO build(ILoggingEvent le)
/*     */   {
/*  63 */     LoggingEventVO ledo = new LoggingEventVO();
/*  64 */     ledo.loggerName = le.getLoggerName();
/*  65 */     ledo.loggerContextVO = le.getLoggerContextVO();
/*  66 */     ledo.threadName = le.getThreadName();
/*  67 */     ledo.level = le.getLevel();
/*  68 */     ledo.message = le.getMessage();
/*  69 */     ledo.argumentArray = le.getArgumentArray();
/*  70 */     ledo.marker = le.getMarker();
/*  71 */     ledo.mdcPropertyMap = le.getMDCPropertyMap();
/*  72 */     ledo.timeStamp = le.getTimeStamp();
/*  73 */     ledo.throwableProxy = ThrowableProxyVO.build(le.getThrowableProxy());
/*     */     
/*     */ 
/*  76 */     if (le.hasCallerData()) {
/*  77 */       ledo.callerDataArray = le.getCallerData();
/*     */     }
/*  79 */     return ledo;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/*  83 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public LoggerContextVO getLoggerContextVO() {
/*  87 */     return this.loggerContextVO;
/*     */   }
/*     */   
/*     */   public String getLoggerName() {
/*  91 */     return this.loggerName;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/*  95 */     return this.level;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  99 */     return this.message;
/*     */   }
/*     */   
/*     */   public String getFormattedMessage() {
/* 103 */     if (this.formattedMessage != null) {
/* 104 */       return this.formattedMessage;
/*     */     }
/*     */     
/* 107 */     if (this.argumentArray != null) {
/* 108 */       this.formattedMessage = MessageFormatter.arrayFormat(this.message, this.argumentArray).getMessage();
/*     */     }
/*     */     else {
/* 111 */       this.formattedMessage = this.message;
/*     */     }
/*     */     
/* 114 */     return this.formattedMessage;
/*     */   }
/*     */   
/*     */   public Object[] getArgumentArray() {
/* 118 */     return this.argumentArray;
/*     */   }
/*     */   
/*     */   public IThrowableProxy getThrowableProxy() {
/* 122 */     return this.throwableProxy;
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getCallerData() {
/* 126 */     return this.callerDataArray;
/*     */   }
/*     */   
/*     */   public boolean hasCallerData() {
/* 130 */     return this.callerDataArray != null;
/*     */   }
/*     */   
/*     */   public Marker getMarker() {
/* 134 */     return this.marker;
/*     */   }
/*     */   
/*     */   public long getTimeStamp() {
/* 138 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   public long getContextBirthTime() {
/* 142 */     return this.loggerContextVO.getBirthTime();
/*     */   }
/*     */   
/*     */   public LoggerContextVO getContextLoggerRemoteView() {
/* 146 */     return this.loggerContextVO;
/*     */   }
/*     */   
/*     */   public Map<String, String> getMDCPropertyMap() {
/* 150 */     return this.mdcPropertyMap;
/*     */   }
/*     */   
/* 153 */   public Map<String, String> getMdc() { return this.mdcPropertyMap; }
/*     */   
/*     */   public void prepareForDeferredProcessing() {}
/*     */   
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 160 */     out.defaultWriteObject();
/* 161 */     out.writeInt(this.level.levelInt);
/* 162 */     if (this.argumentArray != null) {
/* 163 */       int len = this.argumentArray.length;
/* 164 */       out.writeInt(len);
/* 165 */       for (int i = 0; i < this.argumentArray.length; i++) {
/* 166 */         if (this.argumentArray[i] != null) {
/* 167 */           out.writeObject(this.argumentArray[i].toString());
/*     */         } else {
/* 169 */           out.writeObject("NULL_ARGUMENT_ARRAY_ELEMENT");
/*     */         }
/*     */       }
/*     */     } else {
/* 173 */       out.writeInt(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 180 */     in.defaultReadObject();
/* 181 */     int levelInt = in.readInt();
/* 182 */     this.level = Level.toLevel(levelInt);
/*     */     
/* 184 */     int argArrayLen = in.readInt();
/* 185 */     if (argArrayLen != -1) {
/* 186 */       this.argumentArray = new String[argArrayLen];
/* 187 */       for (int i = 0; i < argArrayLen; i++) {
/* 188 */         Object val = in.readObject();
/* 189 */         if (!"NULL_ARGUMENT_ARRAY_ELEMENT".equals(val)) {
/* 190 */           this.argumentArray[i] = val;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 198 */     int prime = 31;
/* 199 */     int result = 1;
/* 200 */     result = 31 * result + (this.message == null ? 0 : this.message.hashCode());
/* 201 */     result = 31 * result + (this.threadName == null ? 0 : this.threadName.hashCode());
/*     */     
/* 203 */     result = 31 * result + (int)(this.timeStamp ^ this.timeStamp >>> 32);
/* 204 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 209 */     if (this == obj)
/* 210 */       return true;
/* 211 */     if (obj == null)
/* 212 */       return false;
/* 213 */     if (getClass() != obj.getClass())
/* 214 */       return false;
/* 215 */     LoggingEventVO other = (LoggingEventVO)obj;
/* 216 */     if (this.message == null) {
/* 217 */       if (other.message != null)
/* 218 */         return false;
/* 219 */     } else if (!this.message.equals(other.message)) {
/* 220 */       return false;
/*     */     }
/* 222 */     if (this.loggerName == null) {
/* 223 */       if (other.loggerName != null)
/* 224 */         return false;
/* 225 */     } else if (!this.loggerName.equals(other.loggerName)) {
/* 226 */       return false;
/*     */     }
/* 228 */     if (this.threadName == null) {
/* 229 */       if (other.threadName != null)
/* 230 */         return false;
/* 231 */     } else if (!this.threadName.equals(other.threadName))
/* 232 */       return false;
/* 233 */     if (this.timeStamp != other.timeStamp) {
/* 234 */       return false;
/*     */     }
/* 236 */     if (this.marker == null) {
/* 237 */       if (other.marker != null)
/* 238 */         return false;
/* 239 */     } else if (!this.marker.equals(other.marker)) {
/* 240 */       return false;
/*     */     }
/* 242 */     if (this.mdcPropertyMap == null) {
/* 243 */       if (other.mdcPropertyMap != null)
/* 244 */         return false;
/* 245 */     } else if (!this.mdcPropertyMap.equals(other.mdcPropertyMap))
/* 246 */       return false;
/* 247 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggingEventVO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */