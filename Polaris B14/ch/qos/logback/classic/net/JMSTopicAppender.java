/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.net.JMSAppenderBase;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
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
/*     */ public class JMSTopicAppender
/*     */   extends JMSAppenderBase<ILoggingEvent>
/*     */ {
/*  44 */   static int SUCCESSIVE_FAILURE_LIMIT = 3;
/*     */   
/*     */   String topicBindingName;
/*     */   
/*     */   String tcfBindingName;
/*     */   TopicConnection topicConnection;
/*     */   TopicSession topicSession;
/*     */   TopicPublisher topicPublisher;
/*  52 */   int successiveFailureCount = 0;
/*     */   
/*  54 */   private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTopicConnectionFactoryBindingName(String tcfBindingName)
/*     */   {
/*  62 */     this.tcfBindingName = tcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTopicConnectionFactoryBindingName()
/*     */   {
/*  69 */     return this.tcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTopicBindingName(String topicBindingName)
/*     */   {
/*  77 */     this.topicBindingName = topicBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTopicBindingName()
/*     */   {
/*  84 */     return this.topicBindingName;
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
/*  97 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(jndi, this.tcfBindingName);
/*     */       
/*     */ 
/* 100 */       if (this.userName != null) {
/* 101 */         this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
/*     */       }
/*     */       else {
/* 104 */         this.topicConnection = topicConnectionFactory.createTopicConnection();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 110 */       this.topicSession = this.topicConnection.createTopicSession(false, 1);
/*     */       
/*     */ 
/*     */ 
/* 114 */       Topic topic = (Topic)lookup(jndi, this.topicBindingName);
/*     */       
/*     */ 
/* 117 */       this.topicPublisher = this.topicSession.createPublisher(topic);
/*     */       
/*     */ 
/* 120 */       this.topicConnection.start();
/*     */       
/* 122 */       jndi.close();
/*     */     } catch (Exception e) {
/* 124 */       addError("Error while activating options for appender named [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 128 */     if ((this.topicConnection != null) && (this.topicSession != null) && (this.topicPublisher != null))
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
/* 147 */       if (this.topicSession != null) {
/* 148 */         this.topicSession.close();
/*     */       }
/* 150 */       if (this.topicConnection != null) {
/* 151 */         this.topicConnection.close();
/*     */       }
/*     */     } catch (Exception e) {
/* 154 */       addError("Error while closing JMSAppender [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 158 */     this.topicPublisher = null;
/* 159 */     this.topicSession = null;
/* 160 */     this.topicConnection = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(ILoggingEvent event)
/*     */   {
/* 169 */     if (!isStarted()) {
/* 170 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 174 */       ObjectMessage msg = this.topicSession.createObjectMessage();
/* 175 */       Serializable so = this.pst.transform(event);
/* 176 */       msg.setObject(so);
/* 177 */       this.topicPublisher.publish(msg);
/* 178 */       this.successiveFailureCount = 0;
/*     */     } catch (Exception e) {
/* 180 */       this.successiveFailureCount += 1;
/* 181 */       if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
/* 182 */         stop();
/*     */       }
/* 184 */       addError("Could not publish message in JMSTopicAppender [" + this.name + "].", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicConnection getTopicConnection()
/*     */   {
/* 193 */     return this.topicConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicSession getTopicSession()
/*     */   {
/* 201 */     return this.topicSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicPublisher getTopicPublisher()
/*     */   {
/* 209 */     return this.topicPublisher;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\JMSTopicAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */