/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import ch.qos.logback.core.util.Duration;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.SocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSocketAppender<E>
/*     */   extends AppenderBase<E>
/*     */   implements SocketConnector.ExceptionHandler
/*     */ {
/*     */   public static final int DEFAULT_PORT = 4560;
/*     */   public static final int DEFAULT_RECONNECTION_DELAY = 30000;
/*     */   public static final int DEFAULT_QUEUE_SIZE = 128;
/*     */   private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
/*     */   private static final int DEFAULT_EVENT_DELAY_TIMEOUT = 100;
/*     */   private final ObjectWriterFactory objectWriterFactory;
/*     */   private final QueueFactory queueFactory;
/*     */   private String remoteHost;
/*  79 */   private int port = 4560;
/*     */   private InetAddress address;
/*  81 */   private Duration reconnectionDelay = new Duration(30000L);
/*  82 */   private int queueSize = 128;
/*  83 */   private int acceptConnectionTimeout = 5000;
/*  84 */   private Duration eventDelayLimit = new Duration(100L);
/*     */   
/*     */   private BlockingDeque<E> deque;
/*     */   
/*     */   private String peerId;
/*     */   
/*     */   private SocketConnector connector;
/*     */   
/*     */   private Future<?> task;
/*     */   private volatile Socket socket;
/*     */   
/*     */   protected AbstractSocketAppender()
/*     */   {
/*  97 */     this(new QueueFactory(), new ObjectWriterFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   AbstractSocketAppender(QueueFactory queueFactory, ObjectWriterFactory objectWriterFactory)
/*     */   {
/* 104 */     this.objectWriterFactory = objectWriterFactory;
/* 105 */     this.queueFactory = queueFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 112 */     if (isStarted()) return;
/* 113 */     int errorCount = 0;
/* 114 */     if (this.port <= 0) {
/* 115 */       errorCount++;
/* 116 */       addError("No port was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_port");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 121 */     if (this.remoteHost == null) {
/* 122 */       errorCount++;
/* 123 */       addError("No remote host was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_host");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 128 */     if (this.queueSize == 0) {
/* 129 */       addWarn("Queue size of zero is deprecated, use a size of one to indicate synchronous processing");
/*     */     }
/*     */     
/* 132 */     if (this.queueSize < 0) {
/* 133 */       errorCount++;
/* 134 */       addError("Queue size must be greater than zero");
/*     */     }
/*     */     
/* 137 */     if (errorCount == 0) {
/*     */       try {
/* 139 */         this.address = InetAddress.getByName(this.remoteHost);
/*     */       } catch (UnknownHostException ex) {
/* 141 */         addError("unknown host: " + this.remoteHost);
/* 142 */         errorCount++;
/*     */       }
/*     */     }
/*     */     
/* 146 */     if (errorCount == 0) {
/* 147 */       this.deque = this.queueFactory.newLinkedBlockingDeque(this.queueSize);
/* 148 */       this.peerId = ("remote peer " + this.remoteHost + ":" + this.port + ": ");
/* 149 */       this.connector = createConnector(this.address, this.port, 0, this.reconnectionDelay.getMilliseconds());
/* 150 */       this.task = getContext().getExecutorService().submit(new Runnable()
/*     */       {
/*     */         public void run() {
/* 153 */           AbstractSocketAppender.this.connectSocketAndDispatchEvents();
/*     */         }
/* 155 */       });
/* 156 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 165 */     if (!isStarted()) return;
/* 166 */     CloseUtil.closeQuietly(this.socket);
/* 167 */     this.task.cancel(true);
/* 168 */     super.stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void append(E event)
/*     */   {
/* 176 */     if ((event == null) || (!isStarted())) return;
/*     */     try
/*     */     {
/* 179 */       boolean inserted = this.deque.offer(event, this.eventDelayLimit.getMilliseconds(), TimeUnit.MILLISECONDS);
/* 180 */       if (!inserted) {
/* 181 */         addInfo("Dropping event due to timeout limit of [" + this.eventDelayLimit + "] being exceeded");
/*     */       }
/*     */     } catch (InterruptedException e) {
/* 184 */       addError("Interrupted while appending event to SocketAppender", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void connectSocketAndDispatchEvents() {
/*     */     try {
/* 190 */       while (socketConnectionCouldBeEstablished()) {
/*     */         try {
/* 192 */           ObjectWriter objectWriter = createObjectWriterForSocket();
/* 193 */           addInfo(this.peerId + "connection established");
/* 194 */           dispatchEvents(objectWriter);
/*     */         } catch (IOException ex) {
/* 196 */           addInfo(this.peerId + "connection failed: " + ex);
/*     */         } finally {
/* 198 */           CloseUtil.closeQuietly(this.socket);
/* 199 */           this.socket = null;
/* 200 */           addInfo(this.peerId + "connection closed");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InterruptedException ex) {}
/*     */     
/* 206 */     addInfo("shutting down");
/*     */   }
/*     */   
/*     */   private boolean socketConnectionCouldBeEstablished() throws InterruptedException {
/* 210 */     return (this.socket = this.connector.call()) != null;
/*     */   }
/*     */   
/*     */   private ObjectWriter createObjectWriterForSocket() throws IOException {
/* 214 */     this.socket.setSoTimeout(this.acceptConnectionTimeout);
/* 215 */     ObjectWriter objectWriter = this.objectWriterFactory.newAutoFlushingObjectWriter(this.socket.getOutputStream());
/* 216 */     this.socket.setSoTimeout(0);
/* 217 */     return objectWriter;
/*     */   }
/*     */   
/*     */   private SocketConnector createConnector(InetAddress address, int port, int initialDelay, long retryDelay) {
/* 221 */     SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);
/* 222 */     connector.setExceptionHandler(this);
/* 223 */     connector.setSocketFactory(getSocketFactory());
/* 224 */     return connector;
/*     */   }
/*     */   
/*     */   private void dispatchEvents(ObjectWriter objectWriter) throws InterruptedException, IOException {
/*     */     for (;;) {
/* 229 */       E event = this.deque.takeFirst();
/* 230 */       postProcessEvent(event);
/* 231 */       Serializable serializableEvent = getPST().transform(event);
/*     */       try {
/* 233 */         objectWriter.write(serializableEvent);
/*     */       } catch (IOException e) {
/* 235 */         tryReAddingEventToFrontOfQueue(event);
/* 236 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryReAddingEventToFrontOfQueue(E event) {
/* 242 */     boolean wasInserted = this.deque.offerFirst(event);
/* 243 */     if (!wasInserted) {
/* 244 */       addInfo("Dropping event due to socket connection error and maxed out deque capacity");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void connectionFailed(SocketConnector connector, Exception ex)
/*     */   {
/* 252 */     if ((ex instanceof InterruptedException)) {
/* 253 */       addInfo("connector interrupted");
/* 254 */     } else if ((ex instanceof ConnectException)) {
/* 255 */       addInfo(this.peerId + "connection refused");
/*     */     } else {
/* 257 */       addInfo(this.peerId + ex);
/*     */     }
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
/*     */   protected SocketConnector newConnector(InetAddress address, int port, long initialDelay, long retryDelay)
/*     */   {
/* 275 */     return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SocketFactory getSocketFactory()
/*     */   {
/* 284 */     return SocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void postProcessEvent(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract PreSerializationTransformer<E> getPST();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoteHost(String host)
/*     */   {
/* 306 */     this.remoteHost = host;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRemoteHost()
/*     */   {
/* 313 */     return this.remoteHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/* 321 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 328 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReconnectionDelay(Duration delay)
/*     */   {
/* 340 */     this.reconnectionDelay = delay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Duration getReconnectionDelay()
/*     */   {
/* 347 */     return this.reconnectionDelay;
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
/*     */   public void setQueueSize(int queueSize)
/*     */   {
/* 363 */     this.queueSize = queueSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getQueueSize()
/*     */   {
/* 370 */     return this.queueSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEventDelayLimit(Duration eventDelayLimit)
/*     */   {
/* 381 */     this.eventDelayLimit = eventDelayLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Duration getEventDelayLimit()
/*     */   {
/* 388 */     return this.eventDelayLimit;
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
/*     */   void setAcceptConnectionTimeout(int acceptConnectionTimeout)
/*     */   {
/* 401 */     this.acceptConnectionTimeout = acceptConnectionTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\AbstractSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */