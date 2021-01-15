/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Queue;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ final class SpdySession
/*     */ {
/*  33 */   private final AtomicInteger activeLocalStreams = new AtomicInteger();
/*  34 */   private final AtomicInteger activeRemoteStreams = new AtomicInteger();
/*  35 */   private final Map<Integer, StreamState> activeStreams = PlatformDependent.newConcurrentHashMap();
/*  36 */   private final StreamComparator streamComparator = new StreamComparator();
/*     */   private final AtomicInteger sendWindowSize;
/*     */   private final AtomicInteger receiveWindowSize;
/*     */   
/*     */   SpdySession(int sendWindowSize, int receiveWindowSize) {
/*  41 */     this.sendWindowSize = new AtomicInteger(sendWindowSize);
/*  42 */     this.receiveWindowSize = new AtomicInteger(receiveWindowSize);
/*     */   }
/*     */   
/*     */   int numActiveStreams(boolean remote) {
/*  46 */     if (remote) {
/*  47 */       return this.activeRemoteStreams.get();
/*     */     }
/*  49 */     return this.activeLocalStreams.get();
/*     */   }
/*     */   
/*     */   boolean noActiveStreams()
/*     */   {
/*  54 */     return this.activeStreams.isEmpty();
/*     */   }
/*     */   
/*     */   boolean isActiveStream(int streamId) {
/*  58 */     return this.activeStreams.containsKey(Integer.valueOf(streamId));
/*     */   }
/*     */   
/*     */   Map<Integer, StreamState> activeStreams()
/*     */   {
/*  63 */     Map<Integer, StreamState> streams = new TreeMap(this.streamComparator);
/*  64 */     streams.putAll(this.activeStreams);
/*  65 */     return streams;
/*     */   }
/*     */   
/*     */ 
/*     */   void acceptStream(int streamId, byte priority, boolean remoteSideClosed, boolean localSideClosed, int sendWindowSize, int receiveWindowSize, boolean remote)
/*     */   {
/*  71 */     if ((!remoteSideClosed) || (!localSideClosed)) {
/*  72 */       StreamState state = (StreamState)this.activeStreams.put(Integer.valueOf(streamId), new StreamState(priority, remoteSideClosed, localSideClosed, sendWindowSize, receiveWindowSize));
/*     */       
/*  74 */       if (state == null) {
/*  75 */         if (remote) {
/*  76 */           this.activeRemoteStreams.incrementAndGet();
/*     */         } else {
/*  78 */           this.activeLocalStreams.incrementAndGet();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private StreamState removeActiveStream(int streamId, boolean remote) {
/*  85 */     StreamState state = (StreamState)this.activeStreams.remove(Integer.valueOf(streamId));
/*  86 */     if (state != null) {
/*  87 */       if (remote) {
/*  88 */         this.activeRemoteStreams.decrementAndGet();
/*     */       } else {
/*  90 */         this.activeLocalStreams.decrementAndGet();
/*     */       }
/*     */     }
/*  93 */     return state;
/*     */   }
/*     */   
/*     */   void removeStream(int streamId, Throwable cause, boolean remote) {
/*  97 */     StreamState state = removeActiveStream(streamId, remote);
/*  98 */     if (state != null) {
/*  99 */       state.clearPendingWrites(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isRemoteSideClosed(int streamId) {
/* 104 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 105 */     return (state == null) || (state.isRemoteSideClosed());
/*     */   }
/*     */   
/*     */   void closeRemoteSide(int streamId, boolean remote) {
/* 109 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 110 */     if (state != null) {
/* 111 */       state.closeRemoteSide();
/* 112 */       if (state.isLocalSideClosed()) {
/* 113 */         removeActiveStream(streamId, remote);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isLocalSideClosed(int streamId) {
/* 119 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 120 */     return (state == null) || (state.isLocalSideClosed());
/*     */   }
/*     */   
/*     */   void closeLocalSide(int streamId, boolean remote) {
/* 124 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 125 */     if (state != null) {
/* 126 */       state.closeLocalSide();
/* 127 */       if (state.isRemoteSideClosed()) {
/* 128 */         removeActiveStream(streamId, remote);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean hasReceivedReply(int streamId)
/*     */   {
/* 138 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 139 */     return (state != null) && (state.hasReceivedReply());
/*     */   }
/*     */   
/*     */   void receivedReply(int streamId) {
/* 143 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 144 */     if (state != null) {
/* 145 */       state.receivedReply();
/*     */     }
/*     */   }
/*     */   
/*     */   int getSendWindowSize(int streamId) {
/* 150 */     if (streamId == 0) {
/* 151 */       return this.sendWindowSize.get();
/*     */     }
/*     */     
/* 154 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 155 */     return state != null ? state.getSendWindowSize() : -1;
/*     */   }
/*     */   
/*     */   int updateSendWindowSize(int streamId, int deltaWindowSize) {
/* 159 */     if (streamId == 0) {
/* 160 */       return this.sendWindowSize.addAndGet(deltaWindowSize);
/*     */     }
/*     */     
/* 163 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 164 */     return state != null ? state.updateSendWindowSize(deltaWindowSize) : -1;
/*     */   }
/*     */   
/*     */   int updateReceiveWindowSize(int streamId, int deltaWindowSize) {
/* 168 */     if (streamId == 0) {
/* 169 */       return this.receiveWindowSize.addAndGet(deltaWindowSize);
/*     */     }
/*     */     
/* 172 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 173 */     if (state == null) {
/* 174 */       return -1;
/*     */     }
/* 176 */     if (deltaWindowSize > 0) {
/* 177 */       state.setReceiveWindowSizeLowerBound(0);
/*     */     }
/* 179 */     return state.updateReceiveWindowSize(deltaWindowSize);
/*     */   }
/*     */   
/*     */   int getReceiveWindowSizeLowerBound(int streamId) {
/* 183 */     if (streamId == 0) {
/* 184 */       return 0;
/*     */     }
/*     */     
/* 187 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 188 */     return state != null ? state.getReceiveWindowSizeLowerBound() : 0;
/*     */   }
/*     */   
/*     */   void updateAllSendWindowSizes(int deltaWindowSize) {
/* 192 */     for (StreamState state : this.activeStreams.values()) {
/* 193 */       state.updateSendWindowSize(deltaWindowSize);
/*     */     }
/*     */   }
/*     */   
/*     */   void updateAllReceiveWindowSizes(int deltaWindowSize) {
/* 198 */     for (StreamState state : this.activeStreams.values()) {
/* 199 */       state.updateReceiveWindowSize(deltaWindowSize);
/* 200 */       if (deltaWindowSize < 0) {
/* 201 */         state.setReceiveWindowSizeLowerBound(deltaWindowSize);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   boolean putPendingWrite(int streamId, PendingWrite pendingWrite) {
/* 207 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 208 */     return (state != null) && (state.putPendingWrite(pendingWrite));
/*     */   }
/*     */   
/*     */   PendingWrite getPendingWrite(int streamId) {
/* 212 */     if (streamId == 0) {
/* 213 */       for (Map.Entry<Integer, StreamState> e : activeStreams().entrySet()) {
/* 214 */         StreamState state = (StreamState)e.getValue();
/* 215 */         if (state.getSendWindowSize() > 0) {
/* 216 */           PendingWrite pendingWrite = state.getPendingWrite();
/* 217 */           if (pendingWrite != null) {
/* 218 */             return pendingWrite;
/*     */           }
/*     */         }
/*     */       }
/* 222 */       return null;
/*     */     }
/*     */     
/* 225 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 226 */     return state != null ? state.getPendingWrite() : null;
/*     */   }
/*     */   
/*     */   PendingWrite removePendingWrite(int streamId) {
/* 230 */     StreamState state = (StreamState)this.activeStreams.get(Integer.valueOf(streamId));
/* 231 */     return state != null ? state.removePendingWrite() : null;
/*     */   }
/*     */   
/*     */   private static final class StreamState
/*     */   {
/*     */     private final byte priority;
/*     */     private boolean remoteSideClosed;
/*     */     private boolean localSideClosed;
/*     */     private boolean receivedReply;
/*     */     private final AtomicInteger sendWindowSize;
/*     */     private final AtomicInteger receiveWindowSize;
/*     */     private int receiveWindowSizeLowerBound;
/* 243 */     private final Queue<SpdySession.PendingWrite> pendingWriteQueue = new ConcurrentLinkedQueue();
/*     */     
/*     */ 
/*     */     StreamState(byte priority, boolean remoteSideClosed, boolean localSideClosed, int sendWindowSize, int receiveWindowSize)
/*     */     {
/* 248 */       this.priority = priority;
/* 249 */       this.remoteSideClosed = remoteSideClosed;
/* 250 */       this.localSideClosed = localSideClosed;
/* 251 */       this.sendWindowSize = new AtomicInteger(sendWindowSize);
/* 252 */       this.receiveWindowSize = new AtomicInteger(receiveWindowSize);
/*     */     }
/*     */     
/*     */     byte getPriority() {
/* 256 */       return this.priority;
/*     */     }
/*     */     
/*     */     boolean isRemoteSideClosed() {
/* 260 */       return this.remoteSideClosed;
/*     */     }
/*     */     
/*     */     void closeRemoteSide() {
/* 264 */       this.remoteSideClosed = true;
/*     */     }
/*     */     
/*     */     boolean isLocalSideClosed() {
/* 268 */       return this.localSideClosed;
/*     */     }
/*     */     
/*     */     void closeLocalSide() {
/* 272 */       this.localSideClosed = true;
/*     */     }
/*     */     
/*     */     boolean hasReceivedReply() {
/* 276 */       return this.receivedReply;
/*     */     }
/*     */     
/*     */     void receivedReply() {
/* 280 */       this.receivedReply = true;
/*     */     }
/*     */     
/*     */     int getSendWindowSize() {
/* 284 */       return this.sendWindowSize.get();
/*     */     }
/*     */     
/*     */     int updateSendWindowSize(int deltaWindowSize) {
/* 288 */       return this.sendWindowSize.addAndGet(deltaWindowSize);
/*     */     }
/*     */     
/*     */     int updateReceiveWindowSize(int deltaWindowSize) {
/* 292 */       return this.receiveWindowSize.addAndGet(deltaWindowSize);
/*     */     }
/*     */     
/*     */     int getReceiveWindowSizeLowerBound() {
/* 296 */       return this.receiveWindowSizeLowerBound;
/*     */     }
/*     */     
/*     */     void setReceiveWindowSizeLowerBound(int receiveWindowSizeLowerBound) {
/* 300 */       this.receiveWindowSizeLowerBound = receiveWindowSizeLowerBound;
/*     */     }
/*     */     
/*     */     boolean putPendingWrite(SpdySession.PendingWrite msg) {
/* 304 */       return this.pendingWriteQueue.offer(msg);
/*     */     }
/*     */     
/*     */     SpdySession.PendingWrite getPendingWrite() {
/* 308 */       return (SpdySession.PendingWrite)this.pendingWriteQueue.peek();
/*     */     }
/*     */     
/*     */     SpdySession.PendingWrite removePendingWrite() {
/* 312 */       return (SpdySession.PendingWrite)this.pendingWriteQueue.poll();
/*     */     }
/*     */     
/*     */     void clearPendingWrites(Throwable cause) {
/*     */       for (;;) {
/* 317 */         SpdySession.PendingWrite pendingWrite = (SpdySession.PendingWrite)this.pendingWriteQueue.poll();
/* 318 */         if (pendingWrite == null) {
/*     */           break;
/*     */         }
/* 321 */         pendingWrite.fail(cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class StreamComparator implements Comparator<Integer>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1161471649740544848L;
/*     */     
/*     */     StreamComparator() {}
/*     */     
/*     */     public int compare(Integer id1, Integer id2)
/*     */     {
/* 334 */       SpdySession.StreamState state1 = (SpdySession.StreamState)SpdySession.this.activeStreams.get(id1);
/* 335 */       SpdySession.StreamState state2 = (SpdySession.StreamState)SpdySession.this.activeStreams.get(id2);
/*     */       
/* 337 */       int result = state1.getPriority() - state2.getPriority();
/* 338 */       if (result != 0) {
/* 339 */         return result;
/*     */       }
/*     */       
/* 342 */       return id1.intValue() - id2.intValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class PendingWrite {
/*     */     final SpdyDataFrame spdyDataFrame;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     PendingWrite(SpdyDataFrame spdyDataFrame, ChannelPromise promise) {
/* 351 */       this.spdyDataFrame = spdyDataFrame;
/* 352 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     void fail(Throwable cause) {
/* 356 */       this.spdyDataFrame.release();
/* 357 */       this.promise.setFailure(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdySession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */