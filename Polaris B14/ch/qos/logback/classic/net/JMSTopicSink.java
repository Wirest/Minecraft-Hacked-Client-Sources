/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.TopicSubscriber;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class JMSTopicSink
/*     */   implements MessageListener
/*     */ {
/*  48 */   private Logger logger = (Logger)LoggerFactory.getLogger(JMSTopicSink.class);
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  51 */     if (args.length < 2) {
/*  52 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  55 */     String tcfBindingName = args[0];
/*  56 */     String topicBindingName = args[1];
/*  57 */     String username = null;
/*  58 */     String password = null;
/*  59 */     if (args.length == 4) {
/*  60 */       username = args[2];
/*  61 */       password = args[3];
/*     */     }
/*     */     
/*  64 */     LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
/*     */     
/*  66 */     new ContextInitializer(loggerContext).autoConfig();
/*     */     
/*  68 */     new JMSTopicSink(tcfBindingName, topicBindingName, username, password);
/*     */     
/*  70 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*  72 */     System.out.println("Type \"exit\" to quit JMSTopicSink.");
/*     */     for (;;) {
/*  74 */       String s = stdin.readLine();
/*  75 */       if (s.equalsIgnoreCase("exit")) {
/*  76 */         System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */         
/*  78 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JMSTopicSink(String tcfBindingName, String topicBindingName, String username, String password)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       Properties env = new Properties();
/*  88 */       env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
/*  89 */       env.put("java.naming.provider.url", "tcp://localhost:61616");
/*  90 */       Context ctx = new InitialContext(env);
/*     */       
/*  92 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);
/*     */       
/*  94 */       System.out.println("Topic Cnx Factory found");
/*  95 */       Topic topic = (Topic)ctx.lookup(topicBindingName);
/*  96 */       System.out.println("Topic found: " + topic.getTopicName());
/*     */       
/*  98 */       TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
/*     */       
/* 100 */       System.out.println("Topic Connection created");
/*     */       
/* 102 */       TopicSession topicSession = topicConnection.createTopicSession(false, 1);
/*     */       
/*     */ 
/* 105 */       TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
/*     */       
/* 107 */       topicSubscriber.setMessageListener(this);
/*     */       
/* 109 */       topicConnection.start();
/* 110 */       System.out.println("Topic Connection started");
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       this.logger.error("Could not read JMS message.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMessage(Message message)
/*     */   {
/*     */     try {
/* 120 */       if ((message instanceof ObjectMessage)) {
/* 121 */         ObjectMessage objectMessage = (ObjectMessage)message;
/* 122 */         ILoggingEvent event = (ILoggingEvent)objectMessage.getObject();
/* 123 */         Logger log = (Logger)LoggerFactory.getLogger(event.getLoggerName());
/* 124 */         log.callAppenders(event);
/*     */       } else {
/* 126 */         this.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
/*     */       }
/*     */     }
/*     */     catch (JMSException jmse) {
/* 130 */       this.logger.error("Exception thrown while processing incoming message.", jmse);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object lookup(Context ctx, String name) throws NamingException
/*     */   {
/*     */     try {
/* 137 */       return ctx.lookup(name);
/*     */     } catch (NameNotFoundException e) {
/* 139 */       this.logger.error("Could not find name [" + name + "].");
/* 140 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/* 145 */     System.err.println(msg);
/* 146 */     System.err.println("Usage: java " + JMSTopicSink.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName Username Password");
/*     */     
/*     */ 
/*     */ 
/* 150 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\JMSTopicSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */