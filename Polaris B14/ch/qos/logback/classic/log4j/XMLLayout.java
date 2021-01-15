/*     */ package ch.qos.logback.classic.log4j;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*     */ import ch.qos.logback.core.LayoutBase;
/*     */ import ch.qos.logback.core.helpers.Transform;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class XMLLayout
/*     */   extends LayoutBase<ILoggingEvent>
/*     */ {
/*  40 */   private final int DEFAULT_SIZE = 256;
/*  41 */   private final int UPPER_LIMIT = 2048;
/*     */   
/*  43 */   private StringBuilder buf = new StringBuilder(256);
/*  44 */   private boolean locationInfo = false;
/*  45 */   private boolean properties = false;
/*     */   
/*     */   public void start()
/*     */   {
/*  49 */     super.start();
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
/*     */   public void setLocationInfo(boolean flag)
/*     */   {
/*  63 */     this.locationInfo = flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getLocationInfo()
/*     */   {
/*  70 */     return this.locationInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperties(boolean flag)
/*     */   {
/*  81 */     this.properties = flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getProperties()
/*     */   {
/*  91 */     return this.properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String doLayout(ILoggingEvent event)
/*     */   {
/* 101 */     if (this.buf.capacity() > 2048) {
/* 102 */       this.buf = new StringBuilder(256);
/*     */     } else {
/* 104 */       this.buf.setLength(0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     this.buf.append("<log4j:event logger=\"");
/* 110 */     this.buf.append(Transform.escapeTags(event.getLoggerName()));
/* 111 */     this.buf.append("\"\r\n");
/* 112 */     this.buf.append("             timestamp=\"");
/* 113 */     this.buf.append(event.getTimeStamp());
/* 114 */     this.buf.append("\" level=\"");
/* 115 */     this.buf.append(event.getLevel());
/* 116 */     this.buf.append("\" thread=\"");
/* 117 */     this.buf.append(Transform.escapeTags(event.getThreadName()));
/* 118 */     this.buf.append("\">\r\n");
/*     */     
/* 120 */     this.buf.append("  <log4j:message>");
/* 121 */     this.buf.append(Transform.escapeTags(event.getFormattedMessage()));
/* 122 */     this.buf.append("</log4j:message>\r\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */     IThrowableProxy tp = event.getThrowableProxy();
/* 129 */     if (tp != null) {
/* 130 */       StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/* 131 */       this.buf.append("  <log4j:throwable><![CDATA[");
/* 132 */       for (StackTraceElementProxy step : stepArray) {
/* 133 */         this.buf.append('\t');
/* 134 */         this.buf.append(step.toString());
/* 135 */         this.buf.append("\r\n");
/*     */       }
/* 137 */       this.buf.append("]]></log4j:throwable>\r\n");
/*     */     }
/*     */     
/* 140 */     if (this.locationInfo) {
/* 141 */       StackTraceElement[] callerDataArray = event.getCallerData();
/* 142 */       if ((callerDataArray != null) && (callerDataArray.length > 0)) {
/* 143 */         StackTraceElement immediateCallerData = callerDataArray[0];
/* 144 */         this.buf.append("  <log4j:locationInfo class=\"");
/* 145 */         this.buf.append(immediateCallerData.getClassName());
/* 146 */         this.buf.append("\"\r\n");
/* 147 */         this.buf.append("                      method=\"");
/* 148 */         this.buf.append(Transform.escapeTags(immediateCallerData.getMethodName()));
/* 149 */         this.buf.append("\" file=\"");
/* 150 */         this.buf.append(Transform.escapeTags(immediateCallerData.getFileName()));
/* 151 */         this.buf.append("\" line=\"");
/* 152 */         this.buf.append(immediateCallerData.getLineNumber());
/* 153 */         this.buf.append("\"/>\r\n");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     if (getProperties()) {
/* 162 */       Map<String, String> propertyMap = event.getMDCPropertyMap();
/*     */       
/* 164 */       if ((propertyMap != null) && (propertyMap.size() != 0)) {
/* 165 */         Set<Map.Entry<String, String>> entrySet = propertyMap.entrySet();
/* 166 */         this.buf.append("  <log4j:properties>");
/* 167 */         for (Map.Entry<String, String> entry : entrySet) {
/* 168 */           this.buf.append("\r\n    <log4j:data");
/* 169 */           this.buf.append(" name='" + Transform.escapeTags((String)entry.getKey()) + "'");
/* 170 */           this.buf.append(" value='" + Transform.escapeTags((String)entry.getValue()) + "'");
/* 171 */           this.buf.append(" />");
/*     */         }
/* 173 */         this.buf.append("\r\n  </log4j:properties>");
/*     */       }
/*     */     }
/*     */     
/* 177 */     this.buf.append("\r\n</log4j:event>\r\n\r\n");
/*     */     
/* 179 */     return this.buf.toString();
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 184 */     return "text/xml";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\log4j\XMLLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */