/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import org.slf4j.Logger;
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
/*     */ public class SimpleSocketServer
/*     */   extends Thread
/*     */ {
/*  53 */   Logger logger = LoggerFactory.getLogger(SimpleSocketServer.class);
/*     */   
/*     */   private final int port;
/*     */   private final LoggerContext lc;
/*  57 */   private boolean closed = false;
/*     */   private ServerSocket serverSocket;
/*  59 */   private List<SocketNode> socketNodeList = new ArrayList();
/*     */   private CountDownLatch latch;
/*     */   
/*     */   public static void main(String[] argv)
/*     */     throws Exception
/*     */   {
/*  65 */     doMain(SimpleSocketServer.class, argv);
/*     */   }
/*     */   
/*     */   protected static void doMain(Class<? extends SimpleSocketServer> serverClass, String[] argv) throws Exception
/*     */   {
/*  70 */     int port = -1;
/*  71 */     if (argv.length == 2) {
/*  72 */       port = parsePortNumber(argv[0]);
/*     */     } else {
/*  74 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  77 */     String configFile = argv[1];
/*  78 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/*  79 */     configureLC(lc, configFile);
/*     */     
/*  81 */     SimpleSocketServer sss = new SimpleSocketServer(lc, port);
/*  82 */     sss.start();
/*     */   }
/*     */   
/*     */   public SimpleSocketServer(LoggerContext lc, int port) {
/*  86 */     this.lc = lc;
/*  87 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  93 */     String oldThreadName = Thread.currentThread().getName();
/*     */     
/*     */     try
/*     */     {
/*  97 */       String newThreadName = getServerThreadName();
/*  98 */       Thread.currentThread().setName(newThreadName);
/*     */       
/* 100 */       this.logger.info("Listening on port " + this.port);
/* 101 */       this.serverSocket = getServerSocketFactory().createServerSocket(this.port);
/* 102 */       while (!this.closed) {
/* 103 */         this.logger.info("Waiting to accept a new client.");
/* 104 */         signalAlmostReadiness();
/* 105 */         Socket socket = this.serverSocket.accept();
/* 106 */         this.logger.info("Connected to client at " + socket.getInetAddress());
/* 107 */         this.logger.info("Starting new socket node.");
/* 108 */         SocketNode newSocketNode = new SocketNode(this, socket, this.lc);
/* 109 */         synchronized (this.socketNodeList) {
/* 110 */           this.socketNodeList.add(newSocketNode);
/*     */         }
/* 112 */         String clientThreadName = getClientThreadName(socket);
/* 113 */         new Thread(newSocketNode, clientThreadName).start();
/*     */       }
/*     */     } catch (Exception e) {
/* 116 */       if (this.closed) {
/* 117 */         this.logger.info("Exception in run method for a closed server. This is normal.");
/*     */       } else {
/* 119 */         this.logger.error("Unexpected failure in run method", e);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 124 */       Thread.currentThread().setName(oldThreadName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getServerThreadName()
/*     */   {
/* 132 */     return String.format("Logback %s (port %d)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.port) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getClientThreadName(Socket socket)
/*     */   {
/* 139 */     return String.format("Logback SocketNode (client: %s)", new Object[] { socket.getRemoteSocketAddress() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServerSocketFactory getServerSocketFactory()
/*     */   {
/* 148 */     return ServerSocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void signalAlmostReadiness()
/*     */   {
/* 156 */     if ((this.latch != null) && (this.latch.getCount() != 0L))
/*     */     {
/* 158 */       this.latch.countDown();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setLatch(CountDownLatch latch)
/*     */   {
/* 167 */     this.latch = latch;
/*     */   }
/*     */   
/*     */ 
/*     */   public CountDownLatch getLatch()
/*     */   {
/* 173 */     return this.latch;
/*     */   }
/*     */   
/* 176 */   public boolean isClosed() { return this.closed; }
/*     */   
/*     */   public void close()
/*     */   {
/* 180 */     this.closed = true;
/* 181 */     if (this.serverSocket != null) {
/*     */       try {
/* 183 */         this.serverSocket.close();
/*     */       } catch (IOException e) {
/* 185 */         this.logger.error("Failed to close serverSocket", e);
/*     */       } finally {
/* 187 */         this.serverSocket = null;
/*     */       }
/*     */     }
/*     */     
/* 191 */     this.logger.info("closing this server");
/* 192 */     synchronized (this.socketNodeList) {
/* 193 */       for (SocketNode sn : this.socketNodeList) {
/* 194 */         sn.close();
/*     */       }
/*     */     }
/* 197 */     if (this.socketNodeList.size() != 0) {
/* 198 */       this.logger.warn("Was expecting a 0-sized socketNodeList after server shutdown");
/*     */     }
/*     */   }
/*     */   
/*     */   public void socketNodeClosing(SocketNode sn)
/*     */   {
/* 204 */     this.logger.debug("Removing {}", sn);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 209 */     synchronized (this.socketNodeList) {
/* 210 */       this.socketNodeList.remove(sn);
/*     */     }
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/* 215 */     System.err.println(msg);
/* 216 */     System.err.println("Usage: java " + SimpleSocketServer.class.getName() + " port configFile");
/*     */     
/* 218 */     System.exit(1);
/*     */   }
/*     */   
/*     */   static int parsePortNumber(String portStr) {
/*     */     try {
/* 223 */       return Integer.parseInt(portStr);
/*     */     } catch (NumberFormatException e) {
/* 225 */       e.printStackTrace();
/* 226 */       usage("Could not interpret port number [" + portStr + "].");
/*     */     }
/* 228 */     return -1;
/*     */   }
/*     */   
/*     */   public static void configureLC(LoggerContext lc, String configFile)
/*     */     throws JoranException
/*     */   {
/* 234 */     JoranConfigurator configurator = new JoranConfigurator();
/* 235 */     lc.reset();
/* 236 */     configurator.setContext(lc);
/* 237 */     configurator.doConfigure(configFile);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SimpleSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */