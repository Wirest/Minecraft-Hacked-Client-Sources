package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OldServerPinger {
    private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on('\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();

    /**
     * A list of NetworkManagers that have pending pings
     */
    private final List pingDestinations = Collections.synchronizedList(Lists.newArrayList());
    private static final String __OBFID = "CL_00000892";

    public void ping(final ServerData server) throws UnknownHostException {
        ServerAddress var2 = ServerAddress.func_78860_a(server.serverIP);
        final NetworkManager var3 = NetworkManager.provideLanClient(InetAddress.getByName(var2.getIP()), var2.getPort());
        this.pingDestinations.add(var3);
        server.serverMOTD = "Pinging...";
        server.pingToServer = -1L;
        server.playerList = null;
        var3.setNetHandler(new INetHandlerStatusClient() {
            private boolean field_147403_d = false;
            private long field_175092_e = 0L;
            private static final String __OBFID = "CL_00000893";

            public void handleServerInfo(S00PacketServerInfo packetIn) {
                ServerStatusResponse var2 = packetIn.func_149294_c();

                if (var2.getServerDescription() != null) {
                    server.serverMOTD = var2.getServerDescription().getFormattedText();
                } else {
                    server.serverMOTD = "";
                }

                if (var2.getProtocolVersionInfo() != null) {
                    server.gameVersion = var2.getProtocolVersionInfo().getName();
                    server.version = var2.getProtocolVersionInfo().getProtocol();
                } else {
                    server.gameVersion = "Old";
                    server.version = 0;
                }

                if (var2.getPlayerCountData() != null) {
                    server.populationInfo = EnumChatFormatting.GRAY + "" + var2.getPlayerCountData().getOnlinePlayerCount() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var2.getPlayerCountData().getMaxPlayers();

                    if (ArrayUtils.isNotEmpty(var2.getPlayerCountData().getPlayers())) {
                        StringBuilder var3x = new StringBuilder();
                        GameProfile[] var4 = var2.getPlayerCountData().getPlayers();
                        int var5 = var4.length;

                        for (int var6 = 0; var6 < var5; ++var6) {
                            GameProfile var7 = var4[var6];

                            if (var3x.length() > 0) {
                                var3x.append("\n");
                            }

                            var3x.append(var7.getName());
                        }

                        if (var2.getPlayerCountData().getPlayers().length < var2.getPlayerCountData().getOnlinePlayerCount()) {
                            if (var3x.length() > 0) {
                                var3x.append("\n");
                            }

                            var3x.append("... and ").append(var2.getPlayerCountData().getOnlinePlayerCount() - var2.getPlayerCountData().getPlayers().length).append(" more ...");
                        }

                        server.playerList = var3x.toString();
                    }
                } else {
                    server.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                }

                if (var2.getFavicon() != null) {
                    String var8 = var2.getFavicon();

                    if (var8.startsWith("data:image/png;base64,")) {
                        server.setBase64EncodedIconData(var8.substring("data:image/png;base64,".length()));
                    } else {
                        OldServerPinger.logger.error("Invalid server icon (unknown format)");
                    }
                } else {
                    server.setBase64EncodedIconData((String) null);
                }

                this.field_175092_e = Minecraft.getSystemTime();
                var3.sendPacket(new C01PacketPing(this.field_175092_e));
                this.field_147403_d = true;
            }

            public void handlePong(S01PacketPong packetIn) {
                long var2 = this.field_175092_e;
                long var4 = Minecraft.getSystemTime();
                server.pingToServer = var4 - var2;
                var3.closeChannel(new ChatComponentText("Finished"));
            }

            public void onDisconnect(IChatComponent reason) {
                if (!this.field_147403_d) {
                    OldServerPinger.logger.error("Can\'t ping " + server.serverIP + ": " + reason.getUnformattedText());
                    server.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    server.populationInfo = "";
                    OldServerPinger.this.tryCompatibilityPing(server);
                }
            }
        });

        try {
            var3.sendPacket(new C00Handshake(47, var2.getIP(), var2.getPort(), EnumConnectionState.STATUS));
            var3.sendPacket(new C00PacketServerQuery());
        } catch (Throwable var5) {
            logger.error(var5);
        }
    }

    private void tryCompatibilityPing(final ServerData server) {
        final ServerAddress var2 = ServerAddress.func_78860_a(server.serverIP);
        ((Bootstrap) ((Bootstrap) ((Bootstrap) (new Bootstrap()).group((EventLoopGroup) NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler(new ChannelInitializer() {
            private static final String __OBFID = "CL_00000894";

            protected void initChannel(Channel p_initChannel_1_) {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                } catch (ChannelException var4) {
                    ;
                }

                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                } catch (ChannelException var3) {
                    ;
                }

                p_initChannel_1_.pipeline().addLast(new ChannelHandler[]{new SimpleChannelInboundHandler() {
                    private static final String __OBFID = "CL_00000895";

                    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
                        super.channelActive(p_channelActive_1_);
                        ByteBuf var2x = Unpooled.buffer();

                        try {
                            var2x.writeByte(254);
                            var2x.writeByte(1);
                            var2x.writeByte(250);
                            char[] var3 = "MC|PingHost".toCharArray();
                            var2x.writeShort(var3.length);
                            char[] var4 = var3;
                            int var5 = var3.length;
                            int var6;
                            char var7;

                            for (var6 = 0; var6 < var5; ++var6) {
                                var7 = var4[var6];
                                var2x.writeChar(var7);
                            }

                            var2x.writeShort(7 + 2 * var2.getIP().length());
                            var2x.writeByte(127);
                            var3 = var2.getIP().toCharArray();
                            var2x.writeShort(var3.length);
                            var4 = var3;
                            var5 = var3.length;

                            for (var6 = 0; var6 < var5; ++var6) {
                                var7 = var4[var6];
                                var2x.writeChar(var7);
                            }

                            var2x.writeInt(var2.getPort());
                            p_channelActive_1_.channel().writeAndFlush(var2x).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        } finally {
                            var2x.release();
                        }
                    }

                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_) {
                        short var3 = p_channelRead0_2_.readUnsignedByte();

                        if (var3 == 255) {
                            String var4 = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                            String[] var5 = (String[]) Iterables.toArray(OldServerPinger.PING_RESPONSE_SPLITTER.split(var4), String.class);

                            if ("\u00a71".equals(var5[0])) {
                                int var6 = MathHelper.parseIntWithDefault(var5[1], 0);
                                String var7 = var5[2];
                                String var8 = var5[3];
                                int var9 = MathHelper.parseIntWithDefault(var5[4], -1);
                                int var10 = MathHelper.parseIntWithDefault(var5[5], -1);
                                server.version = -1;
                                server.gameVersion = var7;
                                server.serverMOTD = var8;
                                server.populationInfo = EnumChatFormatting.GRAY + "" + var9 + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var10;
                            }
                        }

                        p_channelRead0_1_.close();
                    }

                    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
                        p_exceptionCaught_1_.close();
                    }

                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {
                        this.channelRead0(p_channelRead0_1_, (ByteBuf) p_channelRead0_2_);
                    }
                }
                });
            }
        })).channel(NioSocketChannel.class)).connect(var2.getIP(), var2.getPort());
    }

    public void pingPendingNetworks() {
        List var1 = this.pingDestinations;

        synchronized (this.pingDestinations) {
            Iterator var2 = this.pingDestinations.iterator();

            while (var2.hasNext()) {
                NetworkManager var3 = (NetworkManager) var2.next();

                if (var3.isChannelOpen()) {
                    var3.processReceivedPackets();
                } else {
                    var2.remove();
                    var3.checkDisconnected();
                }
            }
        }
    }

    public void clearPendingNetworks() {
        List var1 = this.pingDestinations;

        synchronized (this.pingDestinations) {
            Iterator var2 = this.pingDestinations.iterator();

            while (var2.hasNext()) {
                NetworkManager var3 = (NetworkManager) var2.next();

                if (var3.isChannelOpen()) {
                    var2.remove();
                    var3.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
}
