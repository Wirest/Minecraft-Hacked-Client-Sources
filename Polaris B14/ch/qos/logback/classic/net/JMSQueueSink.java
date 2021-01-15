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
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSession;
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
/*     */ public class JMSQueueSink
/*     */   implements MessageListener
/*     */ {
/*  48 */   private Logger logger = (Logger)LoggerFactory.getLogger(JMSTopicSink.class);
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  51 */     if (args.length < 2) {
/*  52 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  55 */     String qcfBindingName = args[0];
/*  56 */     String queueBindingName = args[1];
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
/*  68 */     new JMSQueueSink(qcfBindingName, queueBindingName, username, password);
/*     */     
/*  70 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*  72 */     System.out.println("Type \"exit\" to quit JMSQueueSink.");
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
/*     */   public JMSQueueSink(String qcfBindingName, String queueBindingName, String username, String password)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       Properties env = new Properties();
/*  88 */       env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
/*  89 */       env.put("java.naming.provider.url", "tcp://localhost:61616");
/*  90 */       Context ctx = new InitialContext(env);
/*     */       
/*  92 */       QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)lookup(ctx, qcfBindingName);
/*     */       
/*  94 */       System.out.println("Queue Cnx Factory found");
/*  95 */       Queue queue = (Queue)ctx.lookup(queueBindingName);
/*  96 */       System.out.println("Queue found: " + queue.getQueueName());
/*     */       
/*  98 */       QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(username, password);
/*     */       
/* 100 */       System.out.println("Queue Connection created");
/*     */       
/* 102 */       QueueSession queueSession = queueConnection.createQueueSession(false, 1);
/*     */       
/*     */ 
/* 105 */       MessageConsumer queueConsumer = queueSession.createConsumer(queue);
/*     */       
/* 107 */       queueConsumer.setMessageListener(this);
/*     */       
/* 109 */       queueConnection.start();
/* 110 */       System.out.println("Queue Connection started");
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
/* 146 */     System.err.println("Usage: java " + JMSQueueSink.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName Username Password");
/*     */     
/*     */ 
/*     */ 
/* 150 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\JMSQueueSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */