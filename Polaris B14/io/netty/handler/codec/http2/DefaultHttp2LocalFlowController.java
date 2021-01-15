/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DefaultHttp2LocalFlowController
/*     */   implements Http2LocalFlowController
/*     */ {
/*     */   private static final int DEFAULT_COMPOSITE_EXCEPTION_SIZE = 4;
/*     */   public static final float DEFAULT_WINDOW_UPDATE_RATIO = 0.5F;
/*     */   private final Http2Connection connection;
/*     */   private final Http2FrameWriter frameWriter;
/*     */   private volatile float windowUpdateRatio;
/*  48 */   private volatile int initialWindowSize = 65535;
/*     */   
/*     */   public DefaultHttp2LocalFlowController(Http2Connection connection, Http2FrameWriter frameWriter) {
/*  51 */     this(connection, frameWriter, 0.5F);
/*     */   }
/*     */   
/*     */   public DefaultHttp2LocalFlowController(Http2Connection connection, Http2FrameWriter frameWriter, float windowUpdateRatio)
/*     */   {
/*  56 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(connection, "connection"));
/*  57 */     this.frameWriter = ((Http2FrameWriter)ObjectUtil.checkNotNull(frameWriter, "frameWriter"));
/*  58 */     windowUpdateRatio(windowUpdateRatio);
/*     */     
/*     */ 
/*  61 */     Http2Stream connectionStream = connection.connectionStream();
/*  62 */     connectionStream.setProperty(FlowState.class, new FlowState(connectionStream, this.initialWindowSize));
/*     */     
/*     */ 
/*  65 */     connection.addListener(new Http2ConnectionAdapter()
/*     */     {
/*     */       public void streamAdded(Http2Stream stream) {
/*  68 */         stream.setProperty(DefaultHttp2LocalFlowController.FlowState.class, new DefaultHttp2LocalFlowController.FlowState(DefaultHttp2LocalFlowController.this, stream, 0));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void streamActive(Http2Stream stream)
/*     */       {
/*  75 */         DefaultHttp2LocalFlowController.this.state(stream).window(DefaultHttp2LocalFlowController.this.initialWindowSize);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void initialWindowSize(int newWindowSize) throws Http2Exception
/*     */   {
/*  82 */     int delta = newWindowSize - this.initialWindowSize;
/*  83 */     this.initialWindowSize = newWindowSize;
/*     */     
/*  85 */     Http2Exception.CompositeStreamException compositeException = null;
/*  86 */     for (Http2Stream stream : this.connection.activeStreams()) {
/*     */       try
/*     */       {
/*  89 */         FlowState state = state(stream);
/*  90 */         state.incrementFlowControlWindows(delta);
/*  91 */         state.incrementInitialStreamWindow(delta);
/*     */       } catch (Http2Exception.StreamException e) {
/*  93 */         if (compositeException == null) {
/*  94 */           compositeException = new Http2Exception.CompositeStreamException(e.error(), 4);
/*     */         }
/*  96 */         compositeException.add(e);
/*     */       }
/*     */     }
/*  99 */     if (compositeException != null) {
/* 100 */       throw compositeException;
/*     */     }
/*     */   }
/*     */   
/*     */   public int initialWindowSize()
/*     */   {
/* 106 */     return this.initialWindowSize;
/*     */   }
/*     */   
/*     */   public int windowSize(Http2Stream stream)
/*     */   {
/* 111 */     return state(stream).window();
/*     */   }
/*     */   
/*     */   public void incrementWindowSize(ChannelHandlerContext ctx, Http2Stream stream, int delta) throws Http2Exception
/*     */   {
/* 116 */     ObjectUtil.checkNotNull(ctx, "ctx");
/* 117 */     FlowState state = state(stream);
/*     */     
/*     */ 
/* 120 */     state.incrementInitialStreamWindow(delta);
/* 121 */     state.writeWindowUpdateIfNeeded(ctx);
/*     */   }
/*     */   
/*     */   public void consumeBytes(ChannelHandlerContext ctx, Http2Stream stream, int numBytes)
/*     */     throws Http2Exception
/*     */   {
/* 127 */     state(stream).consumeBytes(ctx, numBytes);
/*     */   }
/*     */   
/*     */   public int unconsumedBytes(Http2Stream stream)
/*     */   {
/* 132 */     return state(stream).unconsumedBytes();
/*     */   }
/*     */   
/*     */   private static void checkValidRatio(float ratio) {
/* 136 */     if ((Double.compare(ratio, 0.0D) <= 0) || (Double.compare(ratio, 1.0D) >= 0)) {
/* 137 */       throw new IllegalArgumentException("Invalid ratio: " + ratio);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void windowUpdateRatio(float ratio)
/*     */   {
/* 149 */     checkValidRatio(ratio);
/* 150 */     this.windowUpdateRatio = ratio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float windowUpdateRatio()
/*     */   {
/* 159 */     return this.windowUpdateRatio;
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
/*     */   public void windowUpdateRatio(ChannelHandlerContext ctx, Http2Stream stream, float ratio)
/*     */     throws Http2Exception
/*     */   {
/* 177 */     checkValidRatio(ratio);
/* 178 */     FlowState state = state(stream);
/* 179 */     state.windowUpdateRatio(ratio);
/* 180 */     state.writeWindowUpdateIfNeeded(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float windowUpdateRatio(Http2Stream stream)
/*     */     throws Http2Exception
/*     */   {
/* 190 */     return state(stream).windowUpdateRatio();
/*     */   }
/*     */   
/*     */   public void receiveFlowControlledFrame(ChannelHandlerContext ctx, Http2Stream stream, ByteBuf data, int padding, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/* 196 */     int dataLength = data.readableBytes() + padding;
/*     */     
/*     */ 
/* 199 */     connectionState().receiveFlowControlledFrame(dataLength);
/*     */     
/*     */ 
/* 202 */     FlowState state = state(stream);
/* 203 */     state.endOfStream(endOfStream);
/* 204 */     state.receiveFlowControlledFrame(dataLength);
/*     */   }
/*     */   
/*     */   private FlowState connectionState() {
/* 208 */     return state(this.connection.connectionStream());
/*     */   }
/*     */   
/*     */   private FlowState state(Http2Stream stream) {
/* 212 */     ObjectUtil.checkNotNull(stream, "stream");
/* 213 */     return (FlowState)stream.getProperty(FlowState.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class FlowState
/*     */   {
/*     */     private final Http2Stream stream;
/*     */     
/*     */ 
/*     */ 
/*     */     private int window;
/*     */     
/*     */ 
/*     */ 
/*     */     private int processedWindow;
/*     */     
/*     */ 
/*     */ 
/*     */     private volatile int initialStreamWindowSize;
/*     */     
/*     */ 
/*     */ 
/*     */     private volatile float streamWindowUpdateRatio;
/*     */     
/*     */ 
/*     */ 
/*     */     private int lowerBound;
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean endOfStream;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     FlowState(Http2Stream stream, int initialWindowSize)
/*     */     {
/* 252 */       this.stream = stream;
/* 253 */       window(initialWindowSize);
/* 254 */       this.streamWindowUpdateRatio = DefaultHttp2LocalFlowController.this.windowUpdateRatio;
/*     */     }
/*     */     
/*     */     int window() {
/* 258 */       return this.window;
/*     */     }
/*     */     
/*     */     void window(int initialWindowSize) {
/* 262 */       this.window = (this.processedWindow = this.initialStreamWindowSize = initialWindowSize);
/*     */     }
/*     */     
/*     */     void endOfStream(boolean endOfStream) {
/* 266 */       this.endOfStream = endOfStream;
/*     */     }
/*     */     
/*     */     float windowUpdateRatio() {
/* 270 */       return this.streamWindowUpdateRatio;
/*     */     }
/*     */     
/*     */     void windowUpdateRatio(float ratio) {
/* 274 */       this.streamWindowUpdateRatio = ratio;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void incrementInitialStreamWindow(int delta)
/*     */     {
/* 283 */       int newValue = (int)Math.min(2147483647L, Math.max(0L, this.initialStreamWindowSize + delta));
/*     */       
/* 285 */       delta = newValue - this.initialStreamWindowSize;
/*     */       
/* 287 */       this.initialStreamWindowSize += delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void incrementFlowControlWindows(int delta)
/*     */       throws Http2Exception
/*     */     {
/* 296 */       if ((delta > 0) && (this.window > Integer.MAX_VALUE - delta)) {
/* 297 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window overflowed for stream: %d", new Object[] { Integer.valueOf(this.stream.id()) });
/*     */       }
/*     */       
/*     */ 
/* 301 */       this.window += delta;
/* 302 */       this.processedWindow += delta;
/* 303 */       this.lowerBound = (delta < 0 ? delta : 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void receiveFlowControlledFrame(int dataLength)
/*     */       throws Http2Exception
/*     */     {
/* 312 */       assert (dataLength >= 0);
/*     */       
/*     */ 
/* 315 */       this.window -= dataLength;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 322 */       if (this.window < this.lowerBound) {
/* 323 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window exceeded for stream: %d", new Object[] { Integer.valueOf(this.stream.id()) });
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void returnProcessedBytes(int delta)
/*     */       throws Http2Exception
/*     */     {
/* 332 */       if (this.processedWindow - delta < this.window) {
/* 333 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d", new Object[] { Integer.valueOf(this.stream.id()) });
/*     */       }
/*     */       
/* 336 */       this.processedWindow -= delta;
/*     */     }
/*     */     
/*     */     void consumeBytes(ChannelHandlerContext ctx, int numBytes) throws Http2Exception {
/* 340 */       if (this.stream.id() == 0) {
/* 341 */         throw new UnsupportedOperationException("Returning bytes for the connection window is not supported");
/*     */       }
/* 343 */       if (numBytes <= 0) {
/* 344 */         throw new IllegalArgumentException("numBytes must be positive");
/*     */       }
/*     */       
/*     */ 
/* 348 */       FlowState connectionState = DefaultHttp2LocalFlowController.this.connectionState();
/* 349 */       connectionState.returnProcessedBytes(numBytes);
/* 350 */       connectionState.writeWindowUpdateIfNeeded(ctx);
/*     */       
/*     */ 
/* 353 */       returnProcessedBytes(numBytes);
/* 354 */       writeWindowUpdateIfNeeded(ctx);
/*     */     }
/*     */     
/*     */     int unconsumedBytes() {
/* 358 */       return this.processedWindow - this.window;
/*     */     }
/*     */     
/*     */ 
/*     */     void writeWindowUpdateIfNeeded(ChannelHandlerContext ctx)
/*     */       throws Http2Exception
/*     */     {
/* 365 */       if ((this.endOfStream) || (this.initialStreamWindowSize <= 0)) {
/* 366 */         return;
/*     */       }
/*     */       
/* 369 */       int threshold = (int)(this.initialStreamWindowSize * this.streamWindowUpdateRatio);
/* 370 */       if (this.processedWindow <= threshold) {
/* 371 */         writeWindowUpdate(ctx);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void writeWindowUpdate(ChannelHandlerContext ctx)
/*     */       throws Http2Exception
/*     */     {
/* 381 */       int deltaWindowSize = this.initialStreamWindowSize - this.processedWindow;
/*     */       try {
/* 383 */         incrementFlowControlWindows(deltaWindowSize);
/*     */       } catch (Throwable t) {
/* 385 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Attempting to return too many bytes for stream %d", new Object[] { Integer.valueOf(this.stream.id()) });
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 390 */       DefaultHttp2LocalFlowController.this.frameWriter.writeWindowUpdate(ctx, this.stream.id(), deltaWindowSize, ctx.newPromise());
/* 391 */       ctx.flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2LocalFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */