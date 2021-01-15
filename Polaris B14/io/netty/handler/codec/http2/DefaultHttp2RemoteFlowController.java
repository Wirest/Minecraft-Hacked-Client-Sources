/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Queue;
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
/*     */ public class DefaultHttp2RemoteFlowController
/*     */   implements Http2RemoteFlowController
/*     */ {
/*  40 */   private static final Comparator<Http2Stream> WEIGHT_ORDER = new Comparator()
/*     */   {
/*     */     public int compare(Http2Stream o1, Http2Stream o2) {
/*  43 */       return o2.weight() - o1.weight();
/*     */     }
/*     */   };
/*     */   
/*     */   private final Http2Connection connection;
/*  48 */   private int initialWindowSize = 65535;
/*     */   private ChannelHandlerContext ctx;
/*     */   private boolean needFlush;
/*     */   
/*     */   public DefaultHttp2RemoteFlowController(Http2Connection connection) {
/*  53 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(connection, "connection"));
/*     */     
/*     */ 
/*  56 */     connection.connectionStream().setProperty(FlowState.class, new FlowState(connection.connectionStream(), this.initialWindowSize));
/*     */     
/*     */ 
/*     */ 
/*  60 */     connection.addListener(new Http2ConnectionAdapter()
/*     */     {
/*     */       public void streamAdded(Http2Stream stream)
/*     */       {
/*  64 */         stream.setProperty(DefaultHttp2RemoteFlowController.FlowState.class, new DefaultHttp2RemoteFlowController.FlowState(DefaultHttp2RemoteFlowController.this, stream, 0));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void streamActive(Http2Stream stream)
/*     */       {
/*  71 */         DefaultHttp2RemoteFlowController.state(stream).window(DefaultHttp2RemoteFlowController.this.initialWindowSize);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void streamInactive(Http2Stream stream)
/*     */       {
/*  78 */         DefaultHttp2RemoteFlowController.state(stream).clear();
/*     */       }
/*     */       
/*     */       public void priorityTreeParentChanged(Http2Stream stream, Http2Stream oldParent)
/*     */       {
/*  83 */         Http2Stream parent = stream.parent();
/*  84 */         if (parent != null) {
/*  85 */           int delta = DefaultHttp2RemoteFlowController.state(stream).streamableBytesForTree();
/*  86 */           if (delta != 0) {
/*  87 */             DefaultHttp2RemoteFlowController.state(parent).incrementStreamableBytesForTree(delta);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public void priorityTreeParentChanging(Http2Stream stream, Http2Stream newParent)
/*     */       {
/*  94 */         Http2Stream parent = stream.parent();
/*  95 */         if (parent != null) {
/*  96 */           int delta = -DefaultHttp2RemoteFlowController.state(stream).streamableBytesForTree();
/*  97 */           if (delta != 0) {
/*  98 */             DefaultHttp2RemoteFlowController.state(parent).incrementStreamableBytesForTree(delta);
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void initialWindowSize(int newWindowSize) throws Http2Exception
/*     */   {
/* 107 */     if (newWindowSize < 0) {
/* 108 */       throw new IllegalArgumentException("Invalid initial window size: " + newWindowSize);
/*     */     }
/*     */     
/* 111 */     int delta = newWindowSize - this.initialWindowSize;
/* 112 */     this.initialWindowSize = newWindowSize;
/* 113 */     for (Http2Stream stream : this.connection.activeStreams())
/*     */     {
/* 115 */       state(stream).incrementStreamWindow(delta);
/*     */     }
/*     */     
/* 118 */     if (delta > 0)
/*     */     {
/* 120 */       writePendingBytes();
/*     */     }
/*     */   }
/*     */   
/*     */   public int initialWindowSize()
/*     */   {
/* 126 */     return this.initialWindowSize;
/*     */   }
/*     */   
/*     */   public int windowSize(Http2Stream stream)
/*     */   {
/* 131 */     return state(stream).window();
/*     */   }
/*     */   
/*     */   public void incrementWindowSize(ChannelHandlerContext ctx, Http2Stream stream, int delta) throws Http2Exception
/*     */   {
/* 136 */     if (stream.id() == 0)
/*     */     {
/* 138 */       connectionState().incrementStreamWindow(delta);
/* 139 */       writePendingBytes();
/*     */     }
/*     */     else {
/* 142 */       FlowState state = state(stream);
/* 143 */       state.incrementStreamWindow(delta);
/* 144 */       state.writeBytes(state.writableWindow());
/* 145 */       flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void sendFlowControlled(ChannelHandlerContext ctx, Http2Stream stream, Http2RemoteFlowController.FlowControlled payload)
/*     */   {
/* 152 */     ObjectUtil.checkNotNull(ctx, "ctx");
/* 153 */     ObjectUtil.checkNotNull(payload, "payload");
/* 154 */     if ((this.ctx != null) && (this.ctx != ctx)) {
/* 155 */       throw new IllegalArgumentException("Writing data from multiple ChannelHandlerContexts is not supported");
/*     */     }
/*     */     
/* 158 */     this.ctx = ctx;
/*     */     try {
/* 160 */       FlowState state = state(stream);
/* 161 */       state.newFrame(payload);
/* 162 */       state.writeBytes(state.writableWindow());
/* 163 */       flush();
/*     */     } catch (Throwable e) {
/* 165 */       payload.error(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int streamableBytesForTree(Http2Stream stream)
/*     */   {
/* 174 */     return state(stream).streamableBytesForTree();
/*     */   }
/*     */   
/*     */   private static FlowState state(Http2Stream stream) {
/* 178 */     ObjectUtil.checkNotNull(stream, "stream");
/* 179 */     return (FlowState)stream.getProperty(FlowState.class);
/*     */   }
/*     */   
/*     */   private FlowState connectionState() {
/* 183 */     return state(this.connection.connectionStream());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int connectionWindow()
/*     */   {
/* 190 */     return connectionState().window();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void flush()
/*     */   {
/* 197 */     if (this.needFlush) {
/* 198 */       this.ctx.flush();
/* 199 */       this.needFlush = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writePendingBytes()
/*     */   {
/* 207 */     Http2Stream connectionStream = this.connection.connectionStream();
/* 208 */     int connectionWindow = state(connectionStream).window();
/*     */     
/* 210 */     if (connectionWindow > 0) {
/* 211 */       writeChildren(connectionStream, connectionWindow);
/* 212 */       for (Http2Stream stream : this.connection.activeStreams()) {
/* 213 */         writeChildNode(state(stream));
/*     */       }
/* 215 */       flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeChildren(Http2Stream parent, int connectionWindow)
/*     */   {
/* 226 */     FlowState state = state(parent);
/* 227 */     if (state.streamableBytesForTree() <= 0) {
/* 228 */       return 0;
/*     */     }
/* 230 */     int bytesAllocated = 0;
/*     */     
/*     */ 
/*     */ 
/* 234 */     if (state.streamableBytesForTree() <= connectionWindow) {
/* 235 */       for (Http2Stream child : parent.children()) {
/* 236 */         state = state(child);
/* 237 */         int bytesForChild = state.streamableBytes();
/*     */         
/* 239 */         if ((bytesForChild > 0) || (state.hasFrame())) {
/* 240 */           state.allocate(bytesForChild);
/* 241 */           writeChildNode(state);
/* 242 */           bytesAllocated += bytesForChild;
/* 243 */           connectionWindow -= bytesForChild;
/*     */         }
/* 245 */         int childBytesAllocated = writeChildren(child, connectionWindow);
/* 246 */         bytesAllocated += childBytesAllocated;
/* 247 */         connectionWindow -= childBytesAllocated;
/*     */       }
/* 249 */       return bytesAllocated;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 254 */     Http2Stream[] children = (Http2Stream[])parent.children().toArray(new Http2Stream[parent.numChildren()]);
/* 255 */     Arrays.sort(children, WEIGHT_ORDER);
/* 256 */     int totalWeight = parent.totalChildWeights();
/* 257 */     for (int tail = children.length; tail > 0;) {
/* 258 */       int head = 0;
/* 259 */       int nextTail = 0;
/* 260 */       int nextTotalWeight = 0;
/* 261 */       int nextConnectionWindow = connectionWindow;
/* 262 */       for (; (head < tail) && (nextConnectionWindow > 0); head++) {
/* 263 */         Http2Stream child = children[head];
/* 264 */         state = state(child);
/* 265 */         int weight = child.weight();
/* 266 */         double weightRatio = weight / totalWeight;
/*     */         
/* 268 */         int bytesForTree = Math.min(nextConnectionWindow, (int)Math.ceil(connectionWindow * weightRatio));
/* 269 */         int bytesForChild = Math.min(state.streamableBytes(), bytesForTree);
/*     */         
/* 271 */         if ((bytesForChild > 0) || (state.hasFrame())) {
/* 272 */           state.allocate(bytesForChild);
/* 273 */           bytesAllocated += bytesForChild;
/* 274 */           nextConnectionWindow -= bytesForChild;
/* 275 */           bytesForTree -= bytesForChild;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 280 */           if (state.streamableBytesForTree() - bytesForChild > 0) {
/* 281 */             children[(nextTail++)] = child;
/* 282 */             nextTotalWeight += weight;
/*     */           }
/* 284 */           if (state.streamableBytes() - bytesForChild == 0) {
/* 285 */             writeChildNode(state);
/*     */           }
/*     */         }
/*     */         
/* 289 */         if (bytesForTree > 0) {
/* 290 */           int childBytesAllocated = writeChildren(child, bytesForTree);
/* 291 */           bytesAllocated += childBytesAllocated;
/* 292 */           nextConnectionWindow -= childBytesAllocated;
/*     */         }
/*     */       }
/* 295 */       connectionWindow = nextConnectionWindow;
/* 296 */       totalWeight = nextTotalWeight;
/* 297 */       tail = nextTail;
/*     */     }
/*     */     
/* 300 */     return bytesAllocated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void writeChildNode(FlowState state)
/*     */   {
/* 307 */     state.writeBytes(state.allocated());
/* 308 */     state.resetAllocated();
/*     */   }
/*     */   
/*     */ 
/*     */   final class FlowState
/*     */   {
/*     */     private final Queue<Frame> pendingWriteQueue;
/*     */     private final Http2Stream stream;
/*     */     private int window;
/*     */     private int pendingBytes;
/*     */     private int streamableBytesForTree;
/*     */     private int allocated;
/*     */     
/*     */     FlowState(Http2Stream stream, int initialWindowSize)
/*     */     {
/* 323 */       this.stream = stream;
/* 324 */       window(initialWindowSize);
/* 325 */       this.pendingWriteQueue = new ArrayDeque(2);
/*     */     }
/*     */     
/*     */     int window() {
/* 329 */       return this.window;
/*     */     }
/*     */     
/*     */     void window(int initialWindowSize) {
/* 333 */       this.window = initialWindowSize;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void allocate(int bytes)
/*     */     {
/* 340 */       this.allocated += bytes;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     int allocated()
/*     */     {
/* 347 */       return this.allocated;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void resetAllocated()
/*     */     {
/* 354 */       this.allocated = 0;
/*     */     }
/*     */     
/*     */ 
/*     */     int incrementStreamWindow(int delta)
/*     */       throws Http2Exception
/*     */     {
/* 361 */       if ((delta > 0) && (Integer.MAX_VALUE - delta < this.window)) {
/* 362 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Window size overflow for stream: %d", new Object[] { Integer.valueOf(this.stream.id()) });
/*     */       }
/*     */       
/* 365 */       int previouslyStreamable = streamableBytes();
/* 366 */       this.window += delta;
/*     */       
/*     */ 
/* 369 */       int streamableDelta = streamableBytes() - previouslyStreamable;
/* 370 */       if (streamableDelta != 0) {
/* 371 */         incrementStreamableBytesForTree(streamableDelta);
/*     */       }
/* 373 */       return this.window;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     int writableWindow()
/*     */     {
/* 380 */       return Math.min(this.window, DefaultHttp2RemoteFlowController.this.connectionWindow());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int streamableBytes()
/*     */     {
/* 391 */       return Math.max(0, Math.min(this.pendingBytes, this.window));
/*     */     }
/*     */     
/*     */     int streamableBytesForTree() {
/* 395 */       return this.streamableBytesForTree;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Frame newFrame(Http2RemoteFlowController.FlowControlled payload)
/*     */     {
/* 403 */       Frame frame = new Frame(payload);
/* 404 */       this.pendingWriteQueue.offer(frame);
/* 405 */       return frame;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     boolean hasFrame()
/*     */     {
/* 412 */       return !this.pendingWriteQueue.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     Frame peek()
/*     */     {
/* 419 */       return (Frame)this.pendingWriteQueue.peek();
/*     */     }
/*     */     
/*     */ 
/*     */     void clear()
/*     */     {
/*     */       for (;;)
/*     */       {
/* 427 */         Frame frame = (Frame)this.pendingWriteQueue.poll();
/* 428 */         if (frame == null) {
/*     */           break;
/*     */         }
/* 431 */         frame.writeError(Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, "Stream closed before write could take place", new Object[0]));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int writeBytes(int bytes)
/*     */     {
/* 442 */       int bytesAttempted = 0;
/* 443 */       while (hasFrame()) {
/* 444 */         int maxBytes = Math.min(bytes - bytesAttempted, writableWindow());
/* 445 */         bytesAttempted += peek().write(maxBytes);
/* 446 */         if (bytes - bytesAttempted <= 0) {
/*     */           break;
/*     */         }
/*     */       }
/* 450 */       return bytesAttempted;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void incrementStreamableBytesForTree(int numBytes)
/*     */     {
/* 458 */       this.streamableBytesForTree += numBytes;
/* 459 */       if (!this.stream.isRoot()) {
/* 460 */         DefaultHttp2RemoteFlowController.state(this.stream.parent()).incrementStreamableBytesForTree(numBytes);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private final class Frame
/*     */     {
/*     */       final Http2RemoteFlowController.FlowControlled payload;
/*     */       
/*     */       Frame(Http2RemoteFlowController.FlowControlled payload)
/*     */       {
/* 471 */         this.payload = payload;
/*     */         
/* 473 */         incrementPendingBytes(payload.size());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private void incrementPendingBytes(int numBytes)
/*     */       {
/* 482 */         int previouslyStreamable = DefaultHttp2RemoteFlowController.FlowState.this.streamableBytes();
/* 483 */         DefaultHttp2RemoteFlowController.FlowState.access$312(DefaultHttp2RemoteFlowController.FlowState.this, numBytes);
/*     */         
/* 485 */         int delta = DefaultHttp2RemoteFlowController.FlowState.this.streamableBytes() - previouslyStreamable;
/* 486 */         if (delta != 0) {
/* 487 */           DefaultHttp2RemoteFlowController.FlowState.this.incrementStreamableBytesForTree(delta);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       int write(int allowedBytes)
/*     */       {
/* 498 */         int before = this.payload.size();
/* 499 */         DefaultHttp2RemoteFlowController.access$476(DefaultHttp2RemoteFlowController.this, this.payload.write(Math.max(0, allowedBytes)));
/* 500 */         int writtenBytes = before - this.payload.size();
/*     */         try {
/* 502 */           DefaultHttp2RemoteFlowController.this.connectionState().incrementStreamWindow(-writtenBytes);
/* 503 */           DefaultHttp2RemoteFlowController.FlowState.this.incrementStreamWindow(-writtenBytes);
/*     */         } catch (Http2Exception e) {
/* 505 */           throw new RuntimeException("Invalid window state when writing frame: " + e.getMessage(), e);
/*     */         }
/* 507 */         decrementPendingBytes(writtenBytes);
/* 508 */         if (this.payload.size() == 0) {
/* 509 */           DefaultHttp2RemoteFlowController.FlowState.this.pendingWriteQueue.remove();
/*     */         }
/* 511 */         return writtenBytes;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       void writeError(Http2Exception cause)
/*     */       {
/* 519 */         decrementPendingBytes(this.payload.size());
/* 520 */         this.payload.error(cause);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       void decrementPendingBytes(int bytes)
/*     */       {
/* 527 */         incrementPendingBytes(-bytes);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2RemoteFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */