// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.server.MinecraftServer;
import com.google.common.base.Charsets;
import java.net.InetSocketAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PingResponseHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger;
    private NetworkSystem networkSystem;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public PingResponseHandler(final NetworkSystem networkSystemIn) {
        this.networkSystem = networkSystemIn;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext p_channelRead_1_, final Object p_channelRead_2_) throws Exception {
        final ByteBuf bytebuf = (ByteBuf)p_channelRead_2_;
        bytebuf.markReaderIndex();
        boolean flag = true;
        try {
            if (bytebuf.readUnsignedByte() == 254) {
                final InetSocketAddress inetsocketaddress = (InetSocketAddress)p_channelRead_1_.channel().remoteAddress();
                final MinecraftServer minecraftserver = this.networkSystem.getServer();
                final int i = bytebuf.readableBytes();
                switch (i) {
                    case 0: {
                        PingResponseHandler.logger.debug("Ping: (<1.3.x) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                        final String s2 = String.format("%s§%d§%d", minecraftserver.getMOTD(), minecraftserver.getCurrentPlayerCount(), minecraftserver.getMaxPlayers());
                        this.writeAndFlush(p_channelRead_1_, this.getStringBuffer(s2));
                        break;
                    }
                    case 1: {
                        if (bytebuf.readUnsignedByte() != 1) {
                            return;
                        }
                        PingResponseHandler.logger.debug("Ping: (1.4-1.5.x) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                        final String s3 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, minecraftserver.getMinecraftVersion(), minecraftserver.getMOTD(), minecraftserver.getCurrentPlayerCount(), minecraftserver.getMaxPlayers());
                        this.writeAndFlush(p_channelRead_1_, this.getStringBuffer(s3));
                        break;
                    }
                    default: {
                        boolean flag2 = bytebuf.readUnsignedByte() == 1;
                        flag2 &= (bytebuf.readUnsignedByte() == 250);
                        flag2 &= "MC|PingHost".equals(new String(bytebuf.readBytes(bytebuf.readShort() * 2).array(), Charsets.UTF_16BE));
                        final int j = bytebuf.readUnsignedShort();
                        flag2 &= (bytebuf.readUnsignedByte() >= 73);
                        flag2 &= (3 + bytebuf.readBytes(bytebuf.readShort() * 2).array().length + 4 == j);
                        flag2 &= (bytebuf.readInt() <= 65535);
                        flag2 &= (bytebuf.readableBytes() == 0);
                        if (!flag2) {
                            return;
                        }
                        PingResponseHandler.logger.debug("Ping: (1.6) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                        final String s4 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, minecraftserver.getMinecraftVersion(), minecraftserver.getMOTD(), minecraftserver.getCurrentPlayerCount(), minecraftserver.getMaxPlayers());
                        final ByteBuf bytebuf2 = this.getStringBuffer(s4);
                        try {
                            this.writeAndFlush(p_channelRead_1_, bytebuf2);
                        }
                        finally {
                            bytebuf2.release();
                        }
                        bytebuf2.release();
                        break;
                    }
                }
                bytebuf.release();
                flag = false;
                return;
            }
        }
        catch (RuntimeException var21) {
            return;
        }
        finally {
            if (flag) {
                bytebuf.resetReaderIndex();
                p_channelRead_1_.channel().pipeline().remove("legacy_query");
                p_channelRead_1_.fireChannelRead(p_channelRead_2_);
            }
        }
        if (flag) {
            bytebuf.resetReaderIndex();
            p_channelRead_1_.channel().pipeline().remove("legacy_query");
            p_channelRead_1_.fireChannelRead(p_channelRead_2_);
        }
    }
    
    private void writeAndFlush(final ChannelHandlerContext ctx, final ByteBuf data) {
        ctx.pipeline().firstContext().writeAndFlush(data).addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
    }
    
    private ByteBuf getStringBuffer(final String string) {
        final ByteBuf bytebuf = Unpooled.buffer();
        bytebuf.writeByte(255);
        final char[] achar = string.toCharArray();
        bytebuf.writeShort(achar.length);
        char[] array;
        for (int length = (array = achar).length, i = 0; i < length; ++i) {
            final char c0 = array[i];
            bytebuf.writeChar(c0);
        }
        return bytebuf;
    }
}
