package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingResponseHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LogManager.getLogger();
    private NetworkSystem networkSystem;
    private static final String __OBFID = "CL_00001444";

    public PingResponseHandler(NetworkSystem networkSystemIn)
    {
        this.networkSystem = networkSystemIn;
    }

    public void channelRead(ChannelHandlerContext p_channelRead_1_, Object p_channelRead_2_)
    {
        ByteBuf var3 = (ByteBuf)p_channelRead_2_;
        var3.markReaderIndex();
        boolean var4 = true;

        try
        {
            try
            {
                if (var3.readUnsignedByte() != 254)
                {
                    return;
                }

                InetSocketAddress var5 = (InetSocketAddress)p_channelRead_1_.channel().remoteAddress();
                MinecraftServer var6 = this.networkSystem.getServer();
                int var7 = var3.readableBytes();
                String var8;

                switch (var7)
                {
                    case 0:
                        logger.debug("Ping: (<1.3.x) from {}:{}", new Object[] {var5.getAddress(), Integer.valueOf(var5.getPort())});
                        var8 = String.format("%s\u00a7%d\u00a7%d", new Object[] {var6.getMOTD(), Integer.valueOf(var6.getCurrentPlayerCount()), Integer.valueOf(var6.getMaxPlayers())});
                        this.writeAndFlush(p_channelRead_1_, this.getStringBuffer(var8));
                        break;

                    case 1:
                        if (var3.readUnsignedByte() != 1)
                        {
                            return;
                        }

                        logger.debug("Ping: (1.4-1.5.x) from {}:{}", new Object[] {var5.getAddress(), Integer.valueOf(var5.getPort())});
                        var8 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] {Integer.valueOf(127), var6.getMinecraftVersion(), var6.getMOTD(), Integer.valueOf(var6.getCurrentPlayerCount()), Integer.valueOf(var6.getMaxPlayers())});
                        this.writeAndFlush(p_channelRead_1_, this.getStringBuffer(var8));
                        break;

                    default:
                        boolean var23 = var3.readUnsignedByte() == 1;
                        var23 &= var3.readUnsignedByte() == 250;
                        var23 &= "MC|PingHost".equals(new String(var3.readBytes(var3.readShort() * 2).array(), Charsets.UTF_16BE));
                        int var9 = var3.readUnsignedShort();
                        var23 &= var3.readUnsignedByte() >= 73;
                        var23 &= 3 + var3.readBytes(var3.readShort() * 2).array().length + 4 == var9;
                        var23 &= var3.readInt() <= 65535;
                        var23 &= var3.readableBytes() == 0;

                        if (!var23)
                        {
                            return;
                        }

                        logger.debug("Ping: (1.6) from {}:{}", new Object[] {var5.getAddress(), Integer.valueOf(var5.getPort())});
                        String var10 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] {Integer.valueOf(127), var6.getMinecraftVersion(), var6.getMOTD(), Integer.valueOf(var6.getCurrentPlayerCount()), Integer.valueOf(var6.getMaxPlayers())});
                        ByteBuf var11 = this.getStringBuffer(var10);

                        try
                        {
                            this.writeAndFlush(p_channelRead_1_, var11);
                        }
                        finally
                        {
                            var11.release();
                        }
                }

                var3.release();
                var4 = false;
            }
            catch (RuntimeException var21)
            {
                ;
            }
        }
        finally
        {
            if (var4)
            {
                var3.resetReaderIndex();
                p_channelRead_1_.channel().pipeline().remove("legacy_query");
                p_channelRead_1_.fireChannelRead(p_channelRead_2_);
            }
        }
    }

    private void writeAndFlush(ChannelHandlerContext ctx, ByteBuf data)
    {
        ctx.pipeline().firstContext().writeAndFlush(data).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf getStringBuffer(String string)
    {
        ByteBuf var2 = Unpooled.buffer();
        var2.writeByte(255);
        char[] var3 = string.toCharArray();
        var2.writeShort(var3.length);
        char[] var4 = var3;
        int var5 = var3.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            char var7 = var4[var6];
            var2.writeChar(var7);
        }

        return var2;
    }
}
