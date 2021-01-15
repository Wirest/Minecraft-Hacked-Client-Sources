/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.net.JMSAppenderBase;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSender;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.naming.Context;
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
/*     */ public class JMSQueueAppender
/*     */   extends JMSAppenderBase<ILoggingEvent>
/*     */ {
/*  44 */   static int SUCCESSIVE_FAILURE_LIMIT = 3;
/*     */   
/*     */   String queueBindingName;
/*     */   
/*     */   String qcfBindingName;
/*     */   QueueConnection queueConnection;
/*     */   QueueSession queueSession;
/*     */   QueueSender queueSender;
/*  52 */   int successiveFailureCount = 0;
/*     */   
/*  54 */   private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueueConnectionFactoryBindingName(String qcfBindingName)
/*     */   {
/*  62 */     this.qcfBindingName = qcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getQueueConnectionFactoryBindingName()
/*     */   {
/*  69 */     return this.qcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueueBindingName(String queueBindingName)
/*     */   {
/*  77 */     this.queueBindingName = queueBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getQueueBindingName()
/*     */   {
/*  84 */     return this.queueBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*     */     try
/*     */     {
/*  94 */       Context jndi = buildJNDIContext();
/*     */       
/*     */ 
/*  97 */       QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)lookup(jndi, this.qcfBindingName);
/*     */       
/*     */ 
/* 100 */       if (this.userName != null) {
/* 101 */         this.queueConnection = queueConnectionFactory.createQueueConnection(this.userName, this.password);
/*     */       }
/*     */       else {
/* 104 */         this.queueConnection = queueConnectionFactory.createQueueConnection();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 110 */       this.queueSession = this.queueConnection.createQueueSession(false, 1);
/*     */       
/*     */ 
/*     */ 
/* 114 */       Queue queue = (Queue)lookup(jndi, this.queueBindingName);
/*     */       
/*     */ 
/* 117 */       this.queueSender = this.queueSession.createSender(queue);
/*     */       
/*     */ 
/* 120 */       this.queueConnection.start();
/*     */       
/* 122 */       jndi.close();
/*     */     } catch (Exception e) {
/* 124 */       addError("Error while activating options for appender named [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 128 */     if ((this.queueConnection != null) && (this.queueSession != null) && (this.queueSender != null))
/*     */     {
/* 130 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 140 */     if (!this.started) {
/* 141 */       return;
/*     */     }
/*     */     
/* 144 */     this.started = false;
/*     */     try
/*     */     {
/* 147 */       if (this.queueSession != null) {
/* 148 */         this.queueSession.close();
/*     */       }
/* 150 */       if (this.queueConnection != null) {
/* 151 */         this.queueConnection.close();
/*     */       }
/*     */     } catch (Exception e) {
/* 154 */       addError("Error while closing JMSAppender [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 158 */     this.queueSender = null;
/* 159 */     this.queueSession = null;
/* 160 */     this.queueConnection = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(ILoggingEvent event)
/*     */   {
/* 168 */     if (!isStarted()) {
/* 169 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 173 */       ObjectMessage msg = this.queueSession.createObjectMessage();
/* 174 */       Serializable so = this.pst.transform(event);
/* 175 */       msg.setObject(so);
/* 176 */       this.queueSender.send(msg);
/* 177 */       this.successiveFailureCount = 0;
/*     */     } catch (Exception e) {
/* 179 */       this.successiveFailureCount += 1;
/* 180 */       if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
/* 181 */         stop();
/*     */       }
/* 183 */       addError("Could not send message in JMSQueueAppender [" + this.name + "].", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueConnection getQueueConnection()
/*     */   {
/* 193 */     return this.queueConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueSession getQueueSession()
/*     */   {
/* 201 */     return this.queueSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueSender getQueueSender()
/*     */   {
/* 209 */     return this.queueSender;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\JMSQueueAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */