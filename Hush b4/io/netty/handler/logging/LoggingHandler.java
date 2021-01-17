// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.logging;

import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;

@ChannelHandler.Sharable
public class LoggingHandler extends ChannelDuplexHandler
{
    private static final LogLevel DEFAULT_LEVEL;
    private static final String NEWLINE;
    private static final String[] BYTE2HEX;
    private static final String[] HEXPADDING;
    private static final String[] BYTEPADDING;
    private static final char[] BYTE2CHAR;
    protected final InternalLogger logger;
    protected final InternalLogLevel internalLevel;
    private final LogLevel level;
    
    public LoggingHandler() {
        this(LoggingHandler.DEFAULT_LEVEL);
    }
    
    public LoggingHandler(final LogLevel level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(this.getClass());
        this.level = level;
        this.internalLevel = level.toInternalLevel();
    }
    
    public LoggingHandler(final Class<?> clazz) {
        this(clazz, LoggingHandler.DEFAULT_LEVEL);
    }
    
    public LoggingHandler(final Class<?> clazz, final LogLevel level) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        if (level == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(clazz);
        this.level = level;
        this.internalLevel = level.toInternalLevel();
    }
    
    public LoggingHandler(final String name) {
        this(name, LoggingHandler.DEFAULT_LEVEL);
    }
    
    public LoggingHandler(final String name, final LogLevel level) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (level == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(name);
        this.level = level;
        this.internalLevel = level.toInternalLevel();
    }
    
    public LogLevel level() {
        return this.level;
    }
    
    protected String format(final ChannelHandlerContext ctx, final String message) {
        final String chStr = ctx.channel().toString();
        final StringBuilder buf = new StringBuilder(chStr.length() + message.length() + 1);
        buf.append(chStr);
        buf.append(' ');
        buf.append(message);
        return buf.toString();
    }
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "REGISTERED"));
        }
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "UNREGISTERED"));
        }
        super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "ACTIVE"));
        }
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "INACTIVE"));
        }
        super.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "EXCEPTION: " + cause), cause);
        }
        super.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "USER_EVENT: " + evt));
        }
        super.userEventTriggered(ctx, evt);
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "BIND(" + localAddress + ')'));
        }
        super.bind(ctx, localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "CONNECT(" + remoteAddress + ", " + localAddress + ')'));
        }
        super.connect(ctx, remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "DISCONNECT()"));
        }
        super.disconnect(ctx, promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "CLOSE()"));
        }
        super.close(ctx, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "DEREGISTER()"));
        }
        super.deregister(ctx, promise);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.logMessage(ctx, "RECEIVED", msg);
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.logMessage(ctx, "WRITE", msg);
        ctx.write(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, "FLUSH"));
        }
        ctx.flush();
    }
    
    private void logMessage(final ChannelHandlerContext ctx, final String eventName, final Object msg) {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(ctx, this.formatMessage(eventName, msg)));
        }
    }
    
    protected String formatMessage(final String eventName, final Object msg) {
        if (msg instanceof ByteBuf) {
            return this.formatByteBuf(eventName, (ByteBuf)msg);
        }
        if (msg instanceof ByteBufHolder) {
            return this.formatByteBufHolder(eventName, (ByteBufHolder)msg);
        }
        return this.formatNonByteBuf(eventName, msg);
    }
    
    protected String formatByteBuf(final String eventName, final ByteBuf buf) {
        final int length = buf.readableBytes();
        final int rows = length / 16 + ((length % 15 != 0) ? 1 : 0) + 4;
        final StringBuilder dump = new StringBuilder(rows * 80 + eventName.length() + 16);
        dump.append(eventName).append('(').append(length).append('B').append(')');
        dump.append(LoggingHandler.NEWLINE + "         +-------------------------------------------------+" + LoggingHandler.NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + LoggingHandler.NEWLINE + "+--------+-------------------------------------------------+----------------+");
        final int startIndex = buf.readerIndex();
        int endIndex;
        int i;
        for (endIndex = buf.writerIndex(), i = startIndex; i < endIndex; ++i) {
            final int relIdx = i - startIndex;
            final int relIdxMod16 = relIdx & 0xF;
            if (relIdxMod16 == 0) {
                dump.append(LoggingHandler.NEWLINE);
                dump.append(Long.toHexString(((long)relIdx & 0xFFFFFFFFL) | 0x100000000L));
                dump.setCharAt(dump.length() - 9, '|');
                dump.append('|');
            }
            dump.append(LoggingHandler.BYTE2HEX[buf.getUnsignedByte(i)]);
            if (relIdxMod16 == 15) {
                dump.append(" |");
                for (int j = i - 15; j <= i; ++j) {
                    dump.append(LoggingHandler.BYTE2CHAR[buf.getUnsignedByte(j)]);
                }
                dump.append('|');
            }
        }
        if ((i - startIndex & 0xF) != 0x0) {
            final int remainder = length & 0xF;
            dump.append(LoggingHandler.HEXPADDING[remainder]);
            dump.append(" |");
            for (int k = i - remainder; k < i; ++k) {
                dump.append(LoggingHandler.BYTE2CHAR[buf.getUnsignedByte(k)]);
            }
            dump.append(LoggingHandler.BYTEPADDING[remainder]);
            dump.append('|');
        }
        dump.append(LoggingHandler.NEWLINE + "+--------+-------------------------------------------------+----------------+");
        return dump.toString();
    }
    
    protected String formatNonByteBuf(final String eventName, final Object msg) {
        return eventName + ": " + msg;
    }
    
    protected String formatByteBufHolder(final String eventName, final ByteBufHolder msg) {
        return this.formatByteBuf(eventName, msg.content());
    }
    
    static {
        DEFAULT_LEVEL = LogLevel.DEBUG;
        NEWLINE = StringUtil.NEWLINE;
        BYTE2HEX = new String[256];
        HEXPADDING = new String[16];
        BYTEPADDING = new String[16];
        BYTE2CHAR = new char[256];
        for (int i = 0; i < LoggingHandler.BYTE2HEX.length; ++i) {
            LoggingHandler.BYTE2HEX[i] = ' ' + StringUtil.byteToHexStringPadded(i);
        }
        for (int i = 0; i < LoggingHandler.HEXPADDING.length; ++i) {
            final int padding = LoggingHandler.HEXPADDING.length - i;
            final StringBuilder buf = new StringBuilder(padding * 3);
            for (int j = 0; j < padding; ++j) {
                buf.append("   ");
            }
            LoggingHandler.HEXPADDING[i] = buf.toString();
        }
        for (int i = 0; i < LoggingHandler.BYTEPADDING.length; ++i) {
            final int padding = LoggingHandler.BYTEPADDING.length - i;
            final StringBuilder buf = new StringBuilder(padding);
            for (int j = 0; j < padding; ++j) {
                buf.append(' ');
            }
            LoggingHandler.BYTEPADDING[i] = buf.toString();
        }
        for (int i = 0; i < LoggingHandler.BYTE2CHAR.length; ++i) {
            if (i <= 31 || i >= 127) {
                LoggingHandler.BYTE2CHAR[i] = '.';
            }
            else {
                LoggingHandler.BYTE2CHAR[i] = (char)i;
            }
        }
    }
}
