// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import io.netty.bootstrap.AbstractBootstrap;
import java.util.Iterator;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Iterables;
import com.google.common.base.Charsets;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import java.net.UnknownHostException;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.S01PacketPong;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.Packet;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.INetHandlerStatusClient;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import java.util.Collections;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.NetworkManager;
import java.util.List;
import org.apache.logging.log4j.Logger;
import com.google.common.base.Splitter;

public class OldServerPinger
{
    private static final Splitter PING_RESPONSE_SPLITTER;
    private static final Logger logger;
    private final List<NetworkManager> pingDestinations;
    
    static {
        PING_RESPONSE_SPLITTER = Splitter.on('\0').limit(6);
        logger = LogManager.getLogger();
    }
    
    public OldServerPinger() {
        this.pingDestinations = Collections.synchronizedList((List<NetworkManager>)Lists.newArrayList());
    }
    
    public void ping(final ServerData server) throws UnknownHostException {
        final ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        final NetworkManager networkmanager = NetworkManager.func_181124_a(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort(), false);
        this.pingDestinations.add(networkmanager);
        server.serverMOTD = "Pinging...";
        server.pingToServer = -1L;
        server.playerList = null;
        networkmanager.setNetHandler(new INetHandlerStatusClient() {
            private boolean field_147403_d = false;
            private boolean field_183009_e = false;
            private long field_175092_e = 0L;
            
            @Override
            public void handleServerInfo(final S00PacketServerInfo packetIn) {
                if (this.field_183009_e) {
                    networkmanager.closeChannel(new ChatComponentText("Received unrequested status"));
                }
                else {
                    this.field_183009_e = true;
                    final ServerStatusResponse serverstatusresponse = packetIn.getResponse();
                    if (serverstatusresponse.getServerDescription() != null) {
                        server.serverMOTD = serverstatusresponse.getServerDescription().getFormattedText();
                    }
                    else {
                        server.serverMOTD = "";
                    }
                    if (serverstatusresponse.getProtocolVersionInfo() != null) {
                        server.gameVersion = serverstatusresponse.getProtocolVersionInfo().getName();
                        server.version = serverstatusresponse.getProtocolVersionInfo().getProtocol();
                    }
                    else {
                        server.gameVersion = "Old";
                        server.version = 0;
                    }
                    if (serverstatusresponse.getPlayerCountData() != null) {
                        server.populationInfo = new StringBuilder().append(EnumChatFormatting.GRAY).append(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount()).append(EnumChatFormatting.DARK_GRAY).append("/").append(EnumChatFormatting.GRAY).append(serverstatusresponse.getPlayerCountData().getMaxPlayers()).toString();
                        if (ArrayUtils.isNotEmpty(serverstatusresponse.getPlayerCountData().getPlayers())) {
                            final StringBuilder stringbuilder = new StringBuilder();
                            GameProfile[] players;
                            for (int length = (players = serverstatusresponse.getPlayerCountData().getPlayers()).length, i = 0; i < length; ++i) {
                                final GameProfile gameprofile = players[i];
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append(gameprofile.getName());
                            }
                            if (serverstatusresponse.getPlayerCountData().getPlayers().length < serverstatusresponse.getPlayerCountData().getOnlinePlayerCount()) {
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append("... and ").append(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() - serverstatusresponse.getPlayerCountData().getPlayers().length).append(" more ...");
                            }
                            server.playerList = stringbuilder.toString();
                        }
                    }
                    else {
                        server.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                    }
                    if (serverstatusresponse.getFavicon() != null) {
                        final String s = serverstatusresponse.getFavicon();
                        if (s.startsWith("data:image/png;base64,")) {
                            server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length()));
                        }
                        else {
                            OldServerPinger.logger.error("Invalid server icon (unknown format)");
                        }
                    }
                    else {
                        server.setBase64EncodedIconData(null);
                    }
                    this.field_175092_e = Minecraft.getSystemTime();
                    networkmanager.sendPacket(new C01PacketPing(this.field_175092_e));
                    this.field_147403_d = true;
                }
            }
            
            @Override
            public void handlePong(final S01PacketPong packetIn) {
                final long i = this.field_175092_e;
                final long j = Minecraft.getSystemTime();
                server.pingToServer = j - i;
                networkmanager.closeChannel(new ChatComponentText("Finished"));
            }
            
            @Override
            public void onDisconnect(final IChatComponent reason) {
                if (!this.field_147403_d) {
                    OldServerPinger.logger.error("Can't ping " + server.serverIP + ": " + reason.getUnformattedText());
                    server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
                    server.populationInfo = "";
                    OldServerPinger.this.tryCompatibilityPing(server);
                }
            }
        });
        try {
            networkmanager.sendPacket(new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS));
            networkmanager.sendPacket(new C00PacketServerQuery());
        }
        catch (Throwable throwable) {
            OldServerPinger.logger.error(throwable);
        }
    }
    
    private void tryCompatibilityPing(final ServerData server) {
        final ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        ((AbstractBootstrap<Bootstrap, C>)((AbstractBootstrap<Bootstrap, C>)new Bootstrap()).group(NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException ex) {}
                p_initChannel_1_.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
                        super.channelActive(p_channelActive_1_);
                        final ByteBuf bytebuf = Unpooled.buffer();
                        try {
                            bytebuf.writeByte(254);
                            bytebuf.writeByte(1);
                            bytebuf.writeByte(250);
                            char[] achar = "MC|PingHost".toCharArray();
                            bytebuf.writeShort(achar.length);
                            char[] array;
                            for (int length = (array = achar).length, i = 0; i < length; ++i) {
                                final char c0 = array[i];
                                bytebuf.writeChar(c0);
                            }
                            bytebuf.writeShort(7 + 2 * serveraddress.getIP().length());
                            bytebuf.writeByte(127);
                            achar = serveraddress.getIP().toCharArray();
                            bytebuf.writeShort(achar.length);
                            char[] array2;
                            for (int length2 = (array2 = achar).length, j = 0; j < length2; ++j) {
                                final char c2 = array2[j];
                                bytebuf.writeChar(c2);
                            }
                            bytebuf.writeInt(serveraddress.getPort());
                            p_channelActive_1_.channel().writeAndFlush(bytebuf).addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE_ON_FAILURE);
                        }
                        finally {
                            bytebuf.release();
                        }
                        bytebuf.release();
                    }
                    
                    @Override
                    protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final ByteBuf p_channelRead0_2_) throws Exception {
                        final short short1 = p_channelRead0_2_.readUnsignedByte();
                        if (short1 == 255) {
                            final String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                            final String[] astring = Iterables.toArray(OldServerPinger.PING_RESPONSE_SPLITTER.split(s), String.class);
                            if ("§1".equals(astring[0])) {
                                final int i = MathHelper.parseIntWithDefault(astring[1], 0);
                                final String s2 = astring[2];
                                final String s3 = astring[3];
                                final int j = MathHelper.parseIntWithDefault(astring[4], -1);
                                final int k = MathHelper.parseIntWithDefault(astring[5], -1);
                                server.version = -1;
                                server.gameVersion = s2;
                                server.serverMOTD = s3;
                                server.populationInfo = new StringBuilder().append(EnumChatFormatting.GRAY).append(j).append(EnumChatFormatting.DARK_GRAY).append("/").append(EnumChatFormatting.GRAY).append(k).toString();
                            }
                        }
                        p_channelRead0_1_.close();
                    }
                    
                    @Override
                    public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) throws Exception {
                        p_exceptionCaught_1_.close();
                    }
                });
            }
        }).channel(NioSocketChannel.class).connect(serveraddress.getIP(), serveraddress.getPort());
    }
    
    public void pingPendingNetworks() {
        synchronized (this.pingDestinations) {
            final Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkmanager = iterator.next();
                if (networkmanager.isChannelOpen()) {
                    networkmanager.processReceivedPackets();
                }
                else {
                    iterator.remove();
                    networkmanager.checkDisconnected();
                }
            }
        }
        // monitorexit(this.pingDestinations)
    }
    
    public void clearPendingNetworks() {
        synchronized (this.pingDestinations) {
            final Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkmanager = iterator.next();
                if (networkmanager.isChannelOpen()) {
                    iterator.remove();
                    networkmanager.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
        // monitorexit(this.pingDestinations)
    }
}
