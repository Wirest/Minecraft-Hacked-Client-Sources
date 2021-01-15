/*     */ package io.netty.handler.logging;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogLevel;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class LoggingHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  40 */   private static final LogLevel DEFAULT_LEVEL = LogLevel.DEBUG;
/*     */   
/*  42 */   private static final String NEWLINE = StringUtil.NEWLINE;
/*     */   
/*  44 */   private static final String[] BYTE2HEX = new String['Ā'];
/*  45 */   private static final String[] HEXPADDING = new String[16];
/*  46 */   private static final String[] BYTEPADDING = new String[16];
/*  47 */   private static final char[] BYTE2CHAR = new char['Ā'];
/*  48 */   private static final String[] HEXDUMP_ROWPREFIXES = new String['က'];
/*     */   protected final InternalLogger logger;
/*     */   protected final InternalLogLevel internalLevel;
/*     */   private final LogLevel level;
/*     */   
/*     */   static {
/*  54 */     for (int i = 0; i < BYTE2HEX.length; i++) {
/*  55 */       BYTE2HEX[i] = (' ' + StringUtil.byteToHexStringPadded(i));
/*     */     }
/*     */     
/*     */ 
/*  59 */     for (i = 0; i < HEXPADDING.length; i++) {
/*  60 */       int padding = HEXPADDING.length - i;
/*  61 */       StringBuilder buf = new StringBuilder(padding * 3);
/*  62 */       for (int j = 0; j < padding; j++) {
/*  63 */         buf.append("   ");
/*     */       }
/*  65 */       HEXPADDING[i] = buf.toString();
/*     */     }
/*     */     
/*     */ 
/*  69 */     for (i = 0; i < BYTEPADDING.length; i++) {
/*  70 */       int padding = BYTEPADDING.length - i;
/*  71 */       StringBuilder buf = new StringBuilder(padding);
/*  72 */       for (int j = 0; j < padding; j++) {
/*  73 */         buf.append(' ');
/*     */       }
/*  75 */       BYTEPADDING[i] = buf.toString();
/*     */     }
/*     */     
/*     */ 
/*  79 */     for (i = 0; i < BYTE2CHAR.length; i++) {
/*  80 */       if ((i <= 31) || (i >= 127)) {
/*  81 */         BYTE2CHAR[i] = '.';
/*     */       } else {
/*  83 */         BYTE2CHAR[i] = ((char)i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  88 */     for (i = 0; i < HEXDUMP_ROWPREFIXES.length; i++) {
/*  89 */       StringBuilder buf = new StringBuilder(12);
/*  90 */       buf.append(NEWLINE);
/*  91 */       buf.append(Long.toHexString(i << 4 & 0xFFFFFFFF | 0x100000000));
/*  92 */       buf.setCharAt(buf.length() - 9, '|');
/*  93 */       buf.append('|');
/*  94 */       HEXDUMP_ROWPREFIXES[i] = buf.toString();
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
/*     */   public LoggingHandler()
/*     */   {
/* 107 */     this(DEFAULT_LEVEL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggingHandler(LogLevel level)
/*     */   {
/* 117 */     if (level == null) {
/* 118 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/* 121 */     this.logger = InternalLoggerFactory.getInstance(getClass());
/* 122 */     this.level = level;
/* 123 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggingHandler(Class<?> clazz)
/*     */   {
/* 133 */     this(clazz, DEFAULT_LEVEL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggingHandler(Class<?> clazz, LogLevel level)
/*     */   {
/* 143 */     if (clazz == null) {
/* 144 */       throw new NullPointerException("clazz");
/*     */     }
/* 146 */     if (level == null) {
/* 147 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/* 150 */     this.logger = InternalLoggerFactory.getInstance(clazz);
/* 151 */     this.level = level;
/* 152 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggingHandler(String name)
/*     */   {
/* 161 */     this(name, DEFAULT_LEVEL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggingHandler(String name, LogLevel level)
/*     */   {
/* 171 */     if (name == null) {
/* 172 */       throw new NullPointerException("name");
/*     */     }
/* 174 */     if (level == null) {
/* 175 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/* 178 */     this.logger = InternalLoggerFactory.getInstance(name);
/* 179 */     this.level = level;
/* 180 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public LogLevel level()
/*     */   {
/* 187 */     return this.level;
/*     */   }
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 192 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 193 */       this.logger.log(this.internalLevel, format(ctx, "REGISTERED"));
/*     */     }
/* 195 */     ctx.fireChannelRegistered();
/*     */   }
/*     */   
/*     */   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 200 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 201 */       this.logger.log(this.internalLevel, format(ctx, "UNREGISTERED"));
/*     */     }
/* 203 */     ctx.fireChannelUnregistered();
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 208 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 209 */       this.logger.log(this.internalLevel, format(ctx, "ACTIVE"));
/*     */     }
/* 211 */     ctx.fireChannelActive();
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 216 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 217 */       this.logger.log(this.internalLevel, format(ctx, "INACTIVE"));
/*     */     }
/* 219 */     ctx.fireChannelInactive();
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 224 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 225 */       this.logger.log(this.internalLevel, format(ctx, "EXCEPTION", cause), cause);
/*     */     }
/* 227 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
/*     */   {
/* 232 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 233 */       this.logger.log(this.internalLevel, format(ctx, "USER_EVENT", evt));
/*     */     }
/* 235 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*     */   {
/* 240 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 241 */       this.logger.log(this.internalLevel, format(ctx, "BIND", localAddress));
/*     */     }
/* 243 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 250 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 251 */       this.logger.log(this.internalLevel, format(ctx, "CONNECT", remoteAddress, localAddress));
/*     */     }
/* 253 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 258 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 259 */       this.logger.log(this.internalLevel, format(ctx, "DISCONNECT"));
/*     */     }
/* 261 */     ctx.disconnect(promise);
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 266 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 267 */       this.logger.log(this.internalLevel, format(ctx, "CLOSE"));
/*     */     }
/* 269 */     ctx.close(promise);
/*     */   }
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 274 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 275 */       this.logger.log(this.internalLevel, format(ctx, "DEREGISTER"));
/*     */     }
/* 277 */     ctx.deregister(promise);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 282 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 283 */       this.logger.log(this.internalLevel, format(ctx, "RECEIVED", msg));
/*     */     }
/* 285 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 290 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 291 */       this.logger.log(this.internalLevel, format(ctx, "WRITE", msg));
/*     */     }
/* 293 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 298 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 299 */       this.logger.log(this.internalLevel, format(ctx, "FLUSH"));
/*     */     }
/* 301 */     ctx.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String format(ChannelHandlerContext ctx, String eventName)
/*     */   {
/* 310 */     String chStr = ctx.channel().toString();
/* 311 */     return chStr.length() + 1 + eventName.length() + chStr + ' ' + eventName;
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
/*     */   protected String format(ChannelHandlerContext ctx, String eventName, Object arg)
/*     */   {
/* 325 */     if ((arg instanceof ByteBuf))
/* 326 */       return formatByteBuf(ctx, eventName, (ByteBuf)arg);
/* 327 */     if ((arg instanceof ByteBufHolder)) {
/* 328 */       return formatByteBufHolder(ctx, eventName, (ByteBufHolder)arg);
/*     */     }
/* 330 */     return formatSimple(ctx, eventName, arg);
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
/*     */   protected String format(ChannelHandlerContext ctx, String eventName, Object firstArg, Object secondArg)
/*     */   {
/* 343 */     if (secondArg == null) {
/* 344 */       return formatSimple(ctx, eventName, firstArg);
/*     */     }
/*     */     
/* 347 */     String chStr = ctx.channel().toString();
/* 348 */     String arg1Str = String.valueOf(firstArg);
/* 349 */     String arg2Str = secondArg.toString();
/* 350 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName + 2 + arg1Str.length() + 2 + arg2Str.length());
/*     */     
/* 352 */     buf.append(chStr).append(' ').append(eventName).append(": ").append(arg1Str).append(", ").append(arg2Str);
/* 353 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String formatByteBuf(ChannelHandlerContext ctx, String eventName, ByteBuf msg)
/*     */   {
/* 360 */     String chStr = ctx.channel().toString();
/* 361 */     int length = msg.readableBytes();
/* 362 */     if (length == 0) {
/* 363 */       StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 4);
/* 364 */       buf.append(chStr).append(' ').append(eventName).append(": 0B");
/* 365 */       return buf.toString();
/*     */     }
/* 367 */     int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
/* 368 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + 10 + 1 + 2 + rows * 80);
/*     */     
/* 370 */     buf.append(chStr).append(' ').append(eventName).append(": ").append(length).append('B');
/* 371 */     appendHexDump(buf, msg);
/*     */     
/* 373 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String formatByteBufHolder(ChannelHandlerContext ctx, String eventName, ByteBufHolder msg)
/*     */   {
/* 381 */     String chStr = ctx.channel().toString();
/* 382 */     String msgStr = msg.toString();
/* 383 */     ByteBuf content = msg.content();
/* 384 */     int length = content.readableBytes();
/* 385 */     if (length == 0) {
/* 386 */       StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 4);
/* 387 */       buf.append(chStr).append(' ').append(eventName).append(", ").append(msgStr).append(", 0B");
/* 388 */       return buf.toString();
/*     */     }
/* 390 */     int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
/* 391 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 2 + 10 + 1 + 2 + rows * 80);
/*     */     
/*     */ 
/* 394 */     buf.append(chStr).append(' ').append(eventName).append(": ").append(msgStr).append(", ").append(length).append('B');
/*     */     
/* 396 */     appendHexDump(buf, content);
/*     */     
/* 398 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void appendHexDump(StringBuilder dump, ByteBuf buf)
/*     */   {
/* 407 */     dump.append(NEWLINE + "         +-------------------------------------------------+" + NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + NEWLINE + "+--------+-------------------------------------------------+----------------+");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 412 */     int startIndex = buf.readerIndex();
/* 413 */     int endIndex = buf.writerIndex();
/* 414 */     int length = endIndex - startIndex;
/* 415 */     int fullRows = length >>> 4;
/* 416 */     int remainder = length & 0xF;
/*     */     
/*     */ 
/* 419 */     for (int row = 0; row < fullRows; row++) {
/* 420 */       int rowStartIndex = row << 4;
/*     */       
/*     */ 
/* 423 */       appendHexDumpRowPrefix(dump, row, rowStartIndex);
/*     */       
/*     */ 
/* 426 */       int rowEndIndex = rowStartIndex + 16;
/* 427 */       for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 428 */         dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
/*     */       }
/* 430 */       dump.append(" |");
/*     */       
/*     */ 
/* 433 */       for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 434 */         dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
/*     */       }
/* 436 */       dump.append('|');
/*     */     }
/*     */     
/*     */ 
/* 440 */     if (remainder != 0) {
/* 441 */       int rowStartIndex = fullRows << 4;
/* 442 */       appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);
/*     */       
/*     */ 
/* 445 */       int rowEndIndex = rowStartIndex + remainder;
/* 446 */       for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 447 */         dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
/*     */       }
/* 449 */       dump.append(HEXPADDING[remainder]);
/* 450 */       dump.append(" |");
/*     */       
/*     */ 
/* 453 */       for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 454 */         dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
/*     */       }
/* 456 */       dump.append(BYTEPADDING[remainder]);
/* 457 */       dump.append('|');
/*     */     }
/*     */     
/* 460 */     dump.append(NEWLINE + "+--------+-------------------------------------------------+----------------+");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex)
/*     */   {
/* 467 */     if (row < HEXDUMP_ROWPREFIXES.length) {
/* 468 */       dump.append(HEXDUMP_ROWPREFIXES[row]);
/*     */     } else {
/* 470 */       dump.append(NEWLINE);
/* 471 */       dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFF | 0x100000000));
/* 472 */       dump.setCharAt(dump.length() - 9, '|');
/* 473 */       dump.append('|');
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String formatSimple(ChannelHandlerContext ctx, String eventName, Object msg)
/*     */   {
/* 481 */     String chStr = ctx.channel().toString();
/* 482 */     String msgStr = String.valueOf(msg);
/* 483 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length());
/* 484 */     return chStr + ' ' + eventName + ": " + msgStr;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\logging\LoggingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */