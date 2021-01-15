/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ public class DefaultChannelConfig
/*     */   implements ChannelConfig
/*     */ {
/*  35 */   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
/*  36 */   private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
/*     */   private static final AtomicIntegerFieldUpdater<DefaultChannelConfig> AUTOREAD_UPDATER;
/*     */   protected final Channel channel;
/*     */   
/*     */   static
/*     */   {
/*  43 */     AtomicIntegerFieldUpdater<DefaultChannelConfig> autoReadUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(DefaultChannelConfig.class, "autoRead");
/*     */     
/*  45 */     if (autoReadUpdater == null) {
/*  46 */       autoReadUpdater = AtomicIntegerFieldUpdater.newUpdater(DefaultChannelConfig.class, "autoRead");
/*     */     }
/*  48 */     AUTOREAD_UPDATER = autoReadUpdater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  53 */   private volatile ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
/*  54 */   private volatile RecvByteBufAllocator rcvBufAllocator = DEFAULT_RCVBUF_ALLOCATOR;
/*  55 */   private volatile MessageSizeEstimator msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
/*     */   
/*  57 */   private volatile int connectTimeoutMillis = 30000;
/*     */   private volatile int maxMessagesPerRead;
/*  59 */   private volatile int writeSpinCount = 16;
/*  60 */   private volatile int autoRead = 1;
/*  61 */   private volatile int writeBufferHighWaterMark = 65536;
/*  62 */   private volatile int writeBufferLowWaterMark = 32768;
/*     */   
/*     */   public DefaultChannelConfig(Channel channel) {
/*  65 */     if (channel == null) {
/*  66 */       throw new NullPointerException("channel");
/*     */     }
/*  68 */     this.channel = channel;
/*     */     
/*  70 */     if (((channel instanceof ServerChannel)) || ((channel instanceof AbstractNioByteChannel)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  75 */       this.maxMessagesPerRead = 16;
/*     */     } else {
/*  77 */       this.maxMessagesPerRead = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  83 */     return getOptions(null, new ChannelOption[] { ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, ChannelOption<?>... options)
/*     */   {
/*  92 */     if (result == null) {
/*  93 */       result = new IdentityHashMap();
/*     */     }
/*  95 */     for (ChannelOption<?> o : options) {
/*  96 */       result.put(o, getOption(o));
/*     */     }
/*  98 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean setOptions(Map<ChannelOption<?>, ?> options)
/*     */   {
/* 104 */     if (options == null) {
/* 105 */       throw new NullPointerException("options");
/*     */     }
/*     */     
/* 108 */     boolean setAllOptions = true;
/* 109 */     for (Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
/* 110 */       if (!setOption((ChannelOption)e.getKey(), e.getValue())) {
/* 111 */         setAllOptions = false;
/*     */       }
/*     */     }
/*     */     
/* 115 */     return setAllOptions;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/* 121 */     if (option == null) {
/* 122 */       throw new NullPointerException("option");
/*     */     }
/*     */     
/* 125 */     if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
/* 126 */       return Integer.valueOf(getConnectTimeoutMillis());
/*     */     }
/* 128 */     if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
/* 129 */       return Integer.valueOf(getMaxMessagesPerRead());
/*     */     }
/* 131 */     if (option == ChannelOption.WRITE_SPIN_COUNT) {
/* 132 */       return Integer.valueOf(getWriteSpinCount());
/*     */     }
/* 134 */     if (option == ChannelOption.ALLOCATOR) {
/* 135 */       return getAllocator();
/*     */     }
/* 137 */     if (option == ChannelOption.RCVBUF_ALLOCATOR) {
/* 138 */       return getRecvByteBufAllocator();
/*     */     }
/* 140 */     if (option == ChannelOption.AUTO_READ) {
/* 141 */       return Boolean.valueOf(isAutoRead());
/*     */     }
/* 143 */     if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
/* 144 */       return Integer.valueOf(getWriteBufferHighWaterMark());
/*     */     }
/* 146 */     if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
/* 147 */       return Integer.valueOf(getWriteBufferLowWaterMark());
/*     */     }
/* 149 */     if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
/* 150 */       return getMessageSizeEstimator();
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/* 157 */     validate(option, value);
/*     */     
/* 159 */     if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
/* 160 */       setConnectTimeoutMillis(((Integer)value).intValue());
/* 161 */     } else if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
/* 162 */       setMaxMessagesPerRead(((Integer)value).intValue());
/* 163 */     } else if (option == ChannelOption.WRITE_SPIN_COUNT) {
/* 164 */       setWriteSpinCount(((Integer)value).intValue());
/* 165 */     } else if (option == ChannelOption.ALLOCATOR) {
/* 166 */       setAllocator((ByteBufAllocator)value);
/* 167 */     } else if (option == ChannelOption.RCVBUF_ALLOCATOR) {
/* 168 */       setRecvByteBufAllocator((RecvByteBufAllocator)value);
/* 169 */     } else if (option == ChannelOption.AUTO_READ) {
/* 170 */       setAutoRead(((Boolean)value).booleanValue());
/* 171 */     } else if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
/* 172 */       setWriteBufferHighWaterMark(((Integer)value).intValue());
/* 173 */     } else if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
/* 174 */       setWriteBufferLowWaterMark(((Integer)value).intValue());
/* 175 */     } else if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
/* 176 */       setMessageSizeEstimator((MessageSizeEstimator)value);
/*     */     } else {
/* 178 */       return false;
/*     */     }
/*     */     
/* 181 */     return true;
/*     */   }
/*     */   
/*     */   protected <T> void validate(ChannelOption<T> option, T value) {
/* 185 */     if (option == null) {
/* 186 */       throw new NullPointerException("option");
/*     */     }
/* 188 */     option.validate(value);
/*     */   }
/*     */   
/*     */   public int getConnectTimeoutMillis()
/*     */   {
/* 193 */     return this.connectTimeoutMillis;
/*     */   }
/*     */   
/*     */   public ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 198 */     if (connectTimeoutMillis < 0) {
/* 199 */       throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", new Object[] { Integer.valueOf(connectTimeoutMillis) }));
/*     */     }
/*     */     
/* 202 */     this.connectTimeoutMillis = connectTimeoutMillis;
/* 203 */     return this;
/*     */   }
/*     */   
/*     */   public int getMaxMessagesPerRead()
/*     */   {
/* 208 */     return this.maxMessagesPerRead;
/*     */   }
/*     */   
/*     */   public ChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 213 */     if (maxMessagesPerRead <= 0) {
/* 214 */       throw new IllegalArgumentException("maxMessagesPerRead: " + maxMessagesPerRead + " (expected: > 0)");
/*     */     }
/* 216 */     this.maxMessagesPerRead = maxMessagesPerRead;
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public int getWriteSpinCount()
/*     */   {
/* 222 */     return this.writeSpinCount;
/*     */   }
/*     */   
/*     */   public ChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 227 */     if (writeSpinCount <= 0) {
/* 228 */       throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
/*     */     }
/*     */     
/* 231 */     this.writeSpinCount = writeSpinCount;
/* 232 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator getAllocator()
/*     */   {
/* 237 */     return this.allocator;
/*     */   }
/*     */   
/*     */   public ChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 242 */     if (allocator == null) {
/* 243 */       throw new NullPointerException("allocator");
/*     */     }
/* 245 */     this.allocator = allocator;
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public RecvByteBufAllocator getRecvByteBufAllocator()
/*     */   {
/* 251 */     return this.rcvBufAllocator;
/*     */   }
/*     */   
/*     */   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 256 */     if (allocator == null) {
/* 257 */       throw new NullPointerException("allocator");
/*     */     }
/* 259 */     this.rcvBufAllocator = allocator;
/* 260 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAutoRead()
/*     */   {
/* 265 */     return this.autoRead == 1;
/*     */   }
/*     */   
/*     */   public ChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 270 */     boolean oldAutoRead = AUTOREAD_UPDATER.getAndSet(this, autoRead ? 1 : 0) == 1;
/* 271 */     if ((autoRead) && (!oldAutoRead)) {
/* 272 */       this.channel.read();
/* 273 */     } else if ((!autoRead) && (oldAutoRead)) {
/* 274 */       autoReadCleared();
/*     */     }
/* 276 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getWriteBufferHighWaterMark()
/*     */   {
/* 287 */     return this.writeBufferHighWaterMark;
/*     */   }
/*     */   
/*     */   public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 292 */     if (writeBufferHighWaterMark < getWriteBufferLowWaterMark()) {
/* 293 */       throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + getWriteBufferLowWaterMark() + "): " + writeBufferHighWaterMark);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 298 */     if (writeBufferHighWaterMark < 0) {
/* 299 */       throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
/*     */     }
/*     */     
/* 302 */     this.writeBufferHighWaterMark = writeBufferHighWaterMark;
/* 303 */     return this;
/*     */   }
/*     */   
/*     */   public int getWriteBufferLowWaterMark()
/*     */   {
/* 308 */     return this.writeBufferLowWaterMark;
/*     */   }
/*     */   
/*     */   public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 313 */     if (writeBufferLowWaterMark > getWriteBufferHighWaterMark()) {
/* 314 */       throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + getWriteBufferHighWaterMark() + "): " + writeBufferLowWaterMark);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 319 */     if (writeBufferLowWaterMark < 0) {
/* 320 */       throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
/*     */     }
/*     */     
/* 323 */     this.writeBufferLowWaterMark = writeBufferLowWaterMark;
/* 324 */     return this;
/*     */   }
/*     */   
/*     */   public MessageSizeEstimator getMessageSizeEstimator()
/*     */   {
/* 329 */     return this.msgSizeEstimator;
/*     */   }
/*     */   
/*     */   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 334 */     if (estimator == null) {
/* 335 */       throw new NullPointerException("estimator");
/*     */     }
/* 337 */     this.msgSizeEstimator = estimator;
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   protected void autoReadCleared() {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */