/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class SyslogAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   static final String SYSLOG_LAYOUT_URL = "http://logback.qos.ch/codes.html#syslog_layout";
/*     */   static final int MAX_MESSAGE_SIZE_LIMIT = 65000;
/*     */   Layout<E> layout;
/*     */   String facilityStr;
/*     */   String syslogHost;
/*     */   protected String suffixPattern;
/*     */   SyslogOutputStream sos;
/*  44 */   int port = 514;
/*     */   int maxMessageSize;
/*     */   Charset charset;
/*     */   
/*     */   public void start() {
/*  49 */     int errorCount = 0;
/*  50 */     if (this.facilityStr == null) {
/*  51 */       addError("The Facility option is mandatory");
/*  52 */       errorCount++;
/*     */     }
/*     */     
/*  55 */     if (this.charset == null)
/*     */     {
/*     */ 
/*  58 */       this.charset = Charset.defaultCharset();
/*     */     }
/*     */     try
/*     */     {
/*  62 */       this.sos = createOutputStream();
/*     */       
/*  64 */       int systemDatagramSize = this.sos.getSendBufferSize();
/*  65 */       if (this.maxMessageSize == 0) {
/*  66 */         this.maxMessageSize = Math.min(systemDatagramSize, 65000);
/*  67 */         addInfo("Defaulting maxMessageSize to [" + this.maxMessageSize + "]");
/*  68 */       } else if (this.maxMessageSize > systemDatagramSize) {
/*  69 */         addWarn("maxMessageSize of [" + this.maxMessageSize + "] is larger than the system defined datagram size of [" + systemDatagramSize + "].");
/*  70 */         addWarn("This may result in dropped logs.");
/*     */       }
/*     */     } catch (UnknownHostException e) {
/*  73 */       addError("Could not create SyslogWriter", e);
/*  74 */       errorCount++;
/*     */     } catch (SocketException e) {
/*  76 */       addWarn("Failed to bind to a random datagram socket. Will try to reconnect later.", e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  81 */     if (this.layout == null) {
/*  82 */       this.layout = buildLayout();
/*     */     }
/*     */     
/*  85 */     if (errorCount == 0) {
/*  86 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract SyslogOutputStream createOutputStream() throws UnknownHostException, SocketException;
/*     */   
/*     */   public abstract Layout<E> buildLayout();
/*     */   
/*     */   public abstract int getSeverityForEvent(Object paramObject);
/*     */   
/*     */   protected void append(E eventObject)
/*     */   {
/*  98 */     if (!isStarted()) {
/*  99 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 103 */       String msg = this.layout.doLayout(eventObject);
/* 104 */       if (msg == null) {
/* 105 */         return;
/*     */       }
/* 107 */       if (msg.length() > this.maxMessageSize) {
/* 108 */         msg = msg.substring(0, this.maxMessageSize);
/*     */       }
/* 110 */       this.sos.write(msg.getBytes(this.charset));
/* 111 */       this.sos.flush();
/* 112 */       postProcess(eventObject, this.sos);
/*     */     } catch (IOException ioe) {
/* 114 */       addError("Failed to send diagram to " + this.syslogHost, ioe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postProcess(Object event, OutputStream sw) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int facilityStringToint(String facilityStr)
/*     */   {
/* 129 */     if ("KERN".equalsIgnoreCase(facilityStr))
/* 130 */       return 0;
/* 131 */     if ("USER".equalsIgnoreCase(facilityStr))
/* 132 */       return 8;
/* 133 */     if ("MAIL".equalsIgnoreCase(facilityStr))
/* 134 */       return 16;
/* 135 */     if ("DAEMON".equalsIgnoreCase(facilityStr))
/* 136 */       return 24;
/* 137 */     if ("AUTH".equalsIgnoreCase(facilityStr))
/* 138 */       return 32;
/* 139 */     if ("SYSLOG".equalsIgnoreCase(facilityStr))
/* 140 */       return 40;
/* 141 */     if ("LPR".equalsIgnoreCase(facilityStr))
/* 142 */       return 48;
/* 143 */     if ("NEWS".equalsIgnoreCase(facilityStr))
/* 144 */       return 56;
/* 145 */     if ("UUCP".equalsIgnoreCase(facilityStr))
/* 146 */       return 64;
/* 147 */     if ("CRON".equalsIgnoreCase(facilityStr))
/* 148 */       return 72;
/* 149 */     if ("AUTHPRIV".equalsIgnoreCase(facilityStr))
/* 150 */       return 80;
/* 151 */     if ("FTP".equalsIgnoreCase(facilityStr))
/* 152 */       return 88;
/* 153 */     if ("NTP".equalsIgnoreCase(facilityStr))
/* 154 */       return 96;
/* 155 */     if ("AUDIT".equalsIgnoreCase(facilityStr))
/* 156 */       return 104;
/* 157 */     if ("ALERT".equalsIgnoreCase(facilityStr))
/* 158 */       return 112;
/* 159 */     if ("CLOCK".equalsIgnoreCase(facilityStr))
/* 160 */       return 120;
/* 161 */     if ("LOCAL0".equalsIgnoreCase(facilityStr))
/* 162 */       return 128;
/* 163 */     if ("LOCAL1".equalsIgnoreCase(facilityStr))
/* 164 */       return 136;
/* 165 */     if ("LOCAL2".equalsIgnoreCase(facilityStr))
/* 166 */       return 144;
/* 167 */     if ("LOCAL3".equalsIgnoreCase(facilityStr))
/* 168 */       return 152;
/* 169 */     if ("LOCAL4".equalsIgnoreCase(facilityStr))
/* 170 */       return 160;
/* 171 */     if ("LOCAL5".equalsIgnoreCase(facilityStr))
/* 172 */       return 168;
/* 173 */     if ("LOCAL6".equalsIgnoreCase(facilityStr))
/* 174 */       return 176;
/* 175 */     if ("LOCAL7".equalsIgnoreCase(facilityStr)) {
/* 176 */       return 184;
/*     */     }
/* 178 */     throw new IllegalArgumentException(facilityStr + " is not a valid syslog facility string");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSyslogHost()
/*     */   {
/* 187 */     return this.syslogHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSyslogHost(String syslogHost)
/*     */   {
/* 197 */     this.syslogHost = syslogHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFacility()
/*     */   {
/* 206 */     return this.facilityStr;
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
/*     */   public void setFacility(String facilityStr)
/*     */   {
/* 220 */     if (facilityStr != null) {
/* 221 */       facilityStr = facilityStr.trim();
/*     */     }
/* 223 */     this.facilityStr = facilityStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 231 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/* 239 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxMessageSize()
/*     */   {
/* 247 */     return this.maxMessageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxMessageSize(int maxMessageSize)
/*     */   {
/* 258 */     this.maxMessageSize = maxMessageSize;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout() {
/* 262 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/* 266 */     addWarn("The layout of a SyslogAppender cannot be set directly. See also http://logback.qos.ch/codes.html#syslog_layout");
/*     */   }
/*     */   
/*     */ 
/*     */   public void stop()
/*     */   {
/* 272 */     if (this.sos != null) {
/* 273 */       this.sos.close();
/*     */     }
/* 275 */     super.stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSuffixPattern()
/*     */   {
/* 284 */     return this.suffixPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuffixPattern(String suffixPattern)
/*     */   {
/* 294 */     this.suffixPattern = suffixPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/* 302 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 311 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\SyslogAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */