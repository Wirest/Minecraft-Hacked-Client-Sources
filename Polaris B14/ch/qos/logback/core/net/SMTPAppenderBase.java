/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import ch.qos.logback.core.boolex.EvaluationException;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.pattern.PatternLayoutBase;
/*     */ import ch.qos.logback.core.sift.DefaultDiscriminator;
/*     */ import ch.qos.logback.core.sift.Discriminator;
/*     */ import ch.qos.logback.core.spi.CyclicBufferTracker;
/*     */ import ch.qos.logback.core.util.ContentTypeUtil;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import javax.mail.Message.RecipientType;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.AddressException;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.naming.InitialContext;
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
/*     */ public abstract class SMTPAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*  64 */   static InternetAddress[] EMPTY_IA_ARRAY = new InternetAddress[0];
/*     */   static final int MAX_DELAY_BETWEEN_STATUS_MESSAGES = 1228800000;
/*     */   
/*     */   public SMTPAppenderBase() {
/*  68 */     this.lastTrackerStatusPrint = 0L;
/*  69 */     this.delayBetweenStatusMessages = 300000;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  74 */     this.toPatternLayoutList = new ArrayList();
/*     */     
/*  76 */     this.subjectStr = null;
/*     */     
/*  78 */     this.smtpPort = 25;
/*  79 */     this.starttls = false;
/*  80 */     this.ssl = false;
/*  81 */     this.sessionViaJNDI = false;
/*  82 */     this.jndiLocation = "java:comp/env/mail/Session";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     this.asynchronousSending = true;
/*     */     
/*  91 */     this.charsetEncoding = "UTF-8";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */     this.discriminator = new DefaultDiscriminator();
/*     */     
/*     */ 
/* 100 */     this.errorCount = 0;
/*     */   }
/*     */   
/*     */   long lastTrackerStatusPrint;
/*     */   int delayBetweenStatusMessages;
/*     */   protected Layout<E> subjectLayout;
/*     */   protected Layout<E> layout;
/*     */   private List<PatternLayoutBase<E>> toPatternLayoutList;
/*     */   private String from;
/*     */   private String subjectStr;
/*     */   private String smtpHost;
/*     */   private int smtpPort;
/*     */   private boolean starttls;
/*     */   private boolean ssl;
/*     */   protected abstract Layout<E> makeSubjectLayout(String paramString);
/*     */   
/*     */   public void start() {
/* 117 */     if (this.cbTracker == null) {
/* 118 */       this.cbTracker = new CyclicBufferTracker();
/*     */     }
/*     */     
/* 121 */     if (this.sessionViaJNDI) {
/* 122 */       this.session = lookupSessionInJNDI();
/*     */     } else {
/* 124 */       this.session = buildSessionFromProperties();
/*     */     }
/* 126 */     if (this.session == null) {
/* 127 */       addError("Failed to obtain javax.mail.Session. Cannot start.");
/* 128 */       return;
/*     */     }
/*     */     
/* 131 */     this.subjectLayout = makeSubjectLayout(this.subjectStr);
/*     */     
/* 133 */     this.started = true;
/*     */   }
/*     */   
/*     */   private Session lookupSessionInJNDI() {
/* 137 */     addInfo("Looking up javax.mail.Session at JNDI location [" + this.jndiLocation + "]");
/*     */     try {
/* 139 */       javax.naming.Context initialContext = new InitialContext();
/* 140 */       Object obj = initialContext.lookup(this.jndiLocation);
/* 141 */       return (Session)obj;
/*     */     } catch (Exception e) {
/* 143 */       addError("Failed to obtain javax.mail.Session from JNDI location [" + this.jndiLocation + "]"); }
/* 144 */     return null;
/*     */   }
/*     */   
/*     */   private Session buildSessionFromProperties()
/*     */   {
/* 149 */     Properties props = new Properties(OptionHelper.getSystemProperties());
/* 150 */     if (this.smtpHost != null) {
/* 151 */       props.put("mail.smtp.host", this.smtpHost);
/*     */     }
/* 153 */     props.put("mail.smtp.port", Integer.toString(this.smtpPort));
/*     */     
/* 155 */     if (this.localhost != null) {
/* 156 */       props.put("mail.smtp.localhost", this.localhost);
/*     */     }
/*     */     
/* 159 */     LoginAuthenticator loginAuthenticator = null;
/*     */     
/* 161 */     if (this.username != null) {
/* 162 */       loginAuthenticator = new LoginAuthenticator(this.username, this.password);
/* 163 */       props.put("mail.smtp.auth", "true");
/*     */     }
/*     */     
/* 166 */     if ((isSTARTTLS()) && (isSSL())) {
/* 167 */       addError("Both SSL and StartTLS cannot be enabled simultaneously");
/*     */     } else {
/* 169 */       if (isSTARTTLS())
/*     */       {
/* 171 */         props.put("mail.smtp.starttls.enable", "true");
/*     */       }
/* 173 */       if (isSSL()) {
/* 174 */         String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
/* 175 */         props.put("mail.smtp.socketFactory.port", Integer.toString(this.smtpPort));
/* 176 */         props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
/* 177 */         props.put("mail.smtp.socketFactory.fallback", "true");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 183 */     return Session.getInstance(props, loginAuthenticator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void append(E eventObject)
/*     */   {
/* 192 */     if (!checkEntryConditions()) {
/* 193 */       return;
/*     */     }
/*     */     
/* 196 */     String key = this.discriminator.getDiscriminatingValue(eventObject);
/* 197 */     long now = System.currentTimeMillis();
/* 198 */     CyclicBuffer<E> cb = (CyclicBuffer)this.cbTracker.getOrCreate(key, now);
/* 199 */     subAppend(cb, eventObject);
/*     */     try
/*     */     {
/* 202 */       if (this.eventEvaluator.evaluate(eventObject))
/*     */       {
/* 204 */         CyclicBuffer<E> cbClone = new CyclicBuffer(cb);
/*     */         
/* 206 */         cb.clear();
/*     */         
/* 208 */         if (this.asynchronousSending)
/*     */         {
/* 210 */           SMTPAppenderBase<E>.SenderRunnable senderRunnable = new SenderRunnable(cbClone, eventObject);
/* 211 */           this.context.getExecutorService().execute(senderRunnable);
/*     */         }
/*     */         else {
/* 214 */           sendBuffer(cbClone, eventObject);
/*     */         }
/*     */       }
/*     */     } catch (EvaluationException ex) {
/* 218 */       this.errorCount += 1;
/* 219 */       if (this.errorCount < 4) {
/* 220 */         addError("SMTPAppender's EventEvaluator threw an Exception-", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 225 */     if (eventMarksEndOfLife(eventObject)) {
/* 226 */       this.cbTracker.endOfLife(key);
/*     */     }
/*     */     
/* 229 */     this.cbTracker.removeStaleComponents(now);
/*     */     
/* 231 */     if (this.lastTrackerStatusPrint + this.delayBetweenStatusMessages < now) {
/* 232 */       addInfo("SMTPAppender [" + this.name + "] is tracking [" + this.cbTracker.getComponentCount() + "] buffers");
/* 233 */       this.lastTrackerStatusPrint = now;
/*     */       
/* 235 */       if (this.delayBetweenStatusMessages < 1228800000) {
/* 236 */         this.delayBetweenStatusMessages *= 4;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean eventMarksEndOfLife(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void subAppend(CyclicBuffer<E> paramCyclicBuffer, E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkEntryConditions()
/*     */   {
/* 254 */     if (!this.started) {
/* 255 */       addError("Attempting to append to a non-started appender: " + getName());
/*     */       
/* 257 */       return false;
/*     */     }
/*     */     
/* 260 */     if (this.eventEvaluator == null) {
/* 261 */       addError("No EventEvaluator is set for appender [" + this.name + "].");
/* 262 */       return false;
/*     */     }
/*     */     
/* 265 */     if (this.layout == null) {
/* 266 */       addError("No layout set for appender named [" + this.name + "]. For more information, please visit http://logback.qos.ch/codes.html#smtp_no_layout");
/*     */       
/*     */ 
/* 269 */       return false;
/*     */     }
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/* 275 */     this.started = false;
/*     */   }
/*     */   
/*     */   InternetAddress getAddress(String addressStr) {
/*     */     try {
/* 280 */       return new InternetAddress(addressStr);
/*     */     } catch (AddressException e) {
/* 282 */       addError("Could not parse address [" + addressStr + "].", e); }
/* 283 */     return null;
/*     */   }
/*     */   
/*     */   private List<InternetAddress> parseAddress(E event)
/*     */   {
/* 288 */     int len = this.toPatternLayoutList.size();
/*     */     
/* 290 */     List<InternetAddress> iaList = new ArrayList();
/*     */     
/* 292 */     for (int i = 0; i < len; i++) {
/*     */       try {
/* 294 */         PatternLayoutBase<E> emailPL = (PatternLayoutBase)this.toPatternLayoutList.get(i);
/* 295 */         String emailAdrr = emailPL.doLayout(event);
/* 296 */         if ((emailAdrr == null) || (emailAdrr.length() != 0))
/*     */         {
/*     */ 
/* 299 */           InternetAddress[] tmp = InternetAddress.parse(emailAdrr, true);
/* 300 */           iaList.addAll(Arrays.asList(tmp));
/*     */         }
/* 302 */       } catch (AddressException e) { addError("Could not parse email address for [" + this.toPatternLayoutList.get(i) + "] for event [" + event + "]", e);
/* 303 */         return iaList;
/*     */       }
/*     */     }
/*     */     
/* 307 */     return iaList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<PatternLayoutBase<E>> getToList()
/*     */   {
/* 314 */     return this.toPatternLayoutList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sendBuffer(CyclicBuffer<E> cb, E lastEventObject)
/*     */   {
/*     */     try
/*     */     {
/* 325 */       MimeBodyPart part = new MimeBodyPart();
/*     */       
/* 327 */       StringBuffer sbuf = new StringBuffer();
/*     */       
/* 329 */       String header = this.layout.getFileHeader();
/* 330 */       if (header != null) {
/* 331 */         sbuf.append(header);
/*     */       }
/* 333 */       String presentationHeader = this.layout.getPresentationHeader();
/* 334 */       if (presentationHeader != null) {
/* 335 */         sbuf.append(presentationHeader);
/*     */       }
/* 337 */       fillBuffer(cb, sbuf);
/* 338 */       String presentationFooter = this.layout.getPresentationFooter();
/* 339 */       if (presentationFooter != null) {
/* 340 */         sbuf.append(presentationFooter);
/*     */       }
/* 342 */       String footer = this.layout.getFileFooter();
/* 343 */       if (footer != null) {
/* 344 */         sbuf.append(footer);
/*     */       }
/*     */       
/* 347 */       String subjectStr = "Undefined subject";
/* 348 */       if (this.subjectLayout != null) {
/* 349 */         subjectStr = this.subjectLayout.doLayout(lastEventObject);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 354 */         int newLinePos = subjectStr != null ? subjectStr.indexOf('\n') : -1;
/* 355 */         if (newLinePos > -1) {
/* 356 */           subjectStr = subjectStr.substring(0, newLinePos);
/*     */         }
/*     */       }
/*     */       
/* 360 */       MimeMessage mimeMsg = new MimeMessage(this.session);
/*     */       
/* 362 */       if (this.from != null) {
/* 363 */         mimeMsg.setFrom(getAddress(this.from));
/*     */       } else {
/* 365 */         mimeMsg.setFrom();
/*     */       }
/*     */       
/* 368 */       mimeMsg.setSubject(subjectStr, this.charsetEncoding);
/*     */       
/* 370 */       List<InternetAddress> destinationAddresses = parseAddress(lastEventObject);
/* 371 */       if (destinationAddresses.isEmpty()) {
/* 372 */         addInfo("Empty destination address. Aborting email transmission");
/* 373 */         return;
/*     */       }
/*     */       
/* 376 */       InternetAddress[] toAddressArray = (InternetAddress[])destinationAddresses.toArray(EMPTY_IA_ARRAY);
/* 377 */       mimeMsg.setRecipients(Message.RecipientType.TO, toAddressArray);
/*     */       
/* 379 */       String contentType = this.layout.getContentType();
/*     */       
/* 381 */       if (ContentTypeUtil.isTextual(contentType)) {
/* 382 */         part.setText(sbuf.toString(), this.charsetEncoding, ContentTypeUtil.getSubType(contentType));
/*     */       }
/*     */       else {
/* 385 */         part.setContent(sbuf.toString(), this.layout.getContentType());
/*     */       }
/*     */       
/* 388 */       Multipart mp = new MimeMultipart();
/* 389 */       mp.addBodyPart(part);
/* 390 */       mimeMsg.setContent(mp);
/*     */       
/* 392 */       mimeMsg.setSentDate(new Date());
/* 393 */       addInfo("About to send out SMTP message \"" + subjectStr + "\" to " + Arrays.toString(toAddressArray));
/* 394 */       Transport.send(mimeMsg);
/*     */     } catch (Exception e) {
/* 396 */       addError("Error occurred while sending e-mail notification.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void fillBuffer(CyclicBuffer<E> paramCyclicBuffer, StringBuffer paramStringBuffer);
/*     */   
/*     */ 
/*     */   public String getFrom()
/*     */   {
/* 406 */     return this.from;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSubject()
/*     */   {
/* 413 */     return this.subjectStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFrom(String from)
/*     */   {
/* 421 */     this.from = from;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSubject(String subject)
/*     */   {
/* 429 */     this.subjectStr = subject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSMTPHost(String smtpHost)
/*     */   {
/* 438 */     setSmtpHost(smtpHost);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSmtpHost(String smtpHost)
/*     */   {
/* 446 */     this.smtpHost = smtpHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSMTPHost()
/*     */   {
/* 453 */     return getSmtpHost();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSmtpHost()
/*     */   {
/* 460 */     return this.smtpHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSMTPPort(int port)
/*     */   {
/* 469 */     setSmtpPort(port);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSmtpPort(int port)
/*     */   {
/* 478 */     this.smtpPort = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSMTPPort()
/*     */   {
/* 487 */     return getSmtpPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSmtpPort()
/*     */   {
/* 496 */     return this.smtpPort;
/*     */   }
/*     */   
/*     */ 
/* 500 */   public String getLocalhost() { return this.localhost; }
/*     */   
/*     */   private boolean sessionViaJNDI;
/*     */   private String jndiLocation;
/*     */   String username;
/*     */   String password;
/*     */   String localhost;
/*     */   boolean asynchronousSending;
/*     */   private String charsetEncoding;
/*     */   protected Session session;
/*     */   protected EventEvaluator<E> eventEvaluator;
/*     */   protected Discriminator<E> discriminator;
/*     */   protected CyclicBufferTracker<E> cbTracker;
/*     */   private int errorCount;
/* 514 */   public void setLocalhost(String localhost) { this.localhost = localhost; }
/*     */   
/*     */ 
/*     */   public CyclicBufferTracker<E> getCyclicBufferTracker() {
/* 518 */     return this.cbTracker;
/*     */   }
/*     */   
/*     */   public void setCyclicBufferTracker(CyclicBufferTracker<E> cbTracker) {
/* 522 */     this.cbTracker = cbTracker;
/*     */   }
/*     */   
/*     */   public Discriminator<E> getDiscriminator() {
/* 526 */     return this.discriminator;
/*     */   }
/*     */   
/*     */   public void setDiscriminator(Discriminator<E> discriminator) {
/* 530 */     this.discriminator = discriminator;
/*     */   }
/*     */   
/*     */   public boolean isAsynchronousSending() {
/* 534 */     return this.asynchronousSending;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsynchronousSending(boolean asynchronousSending)
/*     */   {
/* 545 */     this.asynchronousSending = asynchronousSending;
/*     */   }
/*     */   
/*     */   public void addTo(String to) {
/* 549 */     if ((to == null) || (to.length() == 0)) {
/* 550 */       throw new IllegalArgumentException("Null or empty <to> property");
/*     */     }
/* 552 */     PatternLayoutBase plb = makeNewToPatternLayout(to.trim());
/* 553 */     plb.setContext(this.context);
/* 554 */     plb.start();
/* 555 */     this.toPatternLayoutList.add(plb);
/*     */   }
/*     */   
/*     */   protected abstract PatternLayoutBase<E> makeNewToPatternLayout(String paramString);
/*     */   
/*     */   public List<String> getToAsListOfString() {
/* 561 */     List<String> toList = new ArrayList();
/* 562 */     for (PatternLayoutBase plb : this.toPatternLayoutList) {
/* 563 */       toList.add(plb.getPattern());
/*     */     }
/* 565 */     return toList;
/*     */   }
/*     */   
/*     */   public boolean isSTARTTLS() {
/* 569 */     return this.starttls;
/*     */   }
/*     */   
/*     */   public void setSTARTTLS(boolean startTLS) {
/* 573 */     this.starttls = startTLS;
/*     */   }
/*     */   
/*     */   public boolean isSSL() {
/* 577 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public void setSSL(boolean ssl) {
/* 581 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEvaluator(EventEvaluator<E> eventEvaluator)
/*     */   {
/* 591 */     this.eventEvaluator = eventEvaluator;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 595 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 599 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 603 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 607 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharsetEncoding()
/*     */   {
/* 615 */     return this.charsetEncoding;
/*     */   }
/*     */   
/*     */   public String getJndiLocation()
/*     */   {
/* 620 */     return this.jndiLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJndiLocation(String jndiLocation)
/*     */   {
/* 631 */     this.jndiLocation = jndiLocation;
/*     */   }
/*     */   
/*     */   public boolean isSessionViaJNDI() {
/* 635 */     return this.sessionViaJNDI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionViaJNDI(boolean sessionViaJNDI)
/*     */   {
/* 645 */     this.sessionViaJNDI = sessionViaJNDI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharsetEncoding(String charsetEncoding)
/*     */   {
/* 655 */     this.charsetEncoding = charsetEncoding;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout() {
/* 659 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/* 663 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   class SenderRunnable implements Runnable
/*     */   {
/*     */     final CyclicBuffer<E> cyclicBuffer;
/*     */     final E e;
/*     */     
/*     */     SenderRunnable(E cyclicBuffer) {
/* 672 */       this.cyclicBuffer = cyclicBuffer;
/* 673 */       this.e = e;
/*     */     }
/*     */     
/*     */     public void run() {
/* 677 */       SMTPAppenderBase.this.sendBuffer(this.cyclicBuffer, this.e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\SMTPAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */