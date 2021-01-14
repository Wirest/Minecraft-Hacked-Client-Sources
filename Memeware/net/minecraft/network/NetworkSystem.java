package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
    private static final Logger logger = LogManager.getLogger();
    public static final LazyLoadBase eventLoops = new LazyLoadBase() {
        private static final String __OBFID = "CL_00001448";

        protected NioEventLoopGroup genericLoad() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.genericLoad();
        }
    };
    public static final LazyLoadBase SERVER_LOCAL_EVENTLOOP = new LazyLoadBase() {
        private static final String __OBFID = "CL_00001449";

        protected LocalEventLoopGroup genericLoad() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.genericLoad();
        }
    };

    /**
     * Reference to the MinecraftServer object.
     */
    private final MinecraftServer mcServer;

    /**
     * True if this NetworkSystem has never had his endpoints terminated
     */
    public volatile boolean isAlive;

    /**
     * Contains all endpoints added to this NetworkSystem
     */
    private final List endpoints = Collections.synchronizedList(Lists.newArrayList());

    /**
     * A list containing all NetworkManager instances of all endpoints
     */
    private final List networkManagers = Collections.synchronizedList(Lists.newArrayList());
    private static final String __OBFID = "CL_00001447";

    public NetworkSystem(MinecraftServer server) {
        this.mcServer = server;
        this.isAlive = true;
    }

    /**
     * Adds a channel that listens on publicly accessible network ports
     */
    @SuppressWarnings("unchecked")
    public void addLanEndpoint(InetAddress address, int port) throws IOException {
        List var3 = this.endpoints;

        synchronized (this.endpoints) {
            this.endpoints.add(((ServerBootstrap) ((ServerBootstrap) (new ServerBootstrap()).channel(NioServerSocketChannel.class)).childHandler(new ChannelInitializer() {
                private static final String __OBFID = "CL_00001450";

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

                    p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new PingResponseHandler(NetworkSystem.this)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
                    NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", var2);
                    var2.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, var2));
                }
            }).group((EventLoopGroup) eventLoops.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
        }
    }

    /**
     * Adds a channel that listens locally
     */
    public SocketAddress addLocalEndpoint() {
        List var2 = this.endpoints;
        ChannelFuture var1;

        synchronized (this.endpoints) {
            var1 = ((ServerBootstrap) ((ServerBootstrap) (new ServerBootstrap()).channel(LocalServerChannel.class)).childHandler(new ChannelInitializer() {
                private static final String __OBFID = "CL_00001451";

                protected void initChannel(Channel p_initChannel_1_) {
                    NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    var2.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, var2));
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", var2);
                }
            }).group((EventLoopGroup) eventLoops.getValue()).localAddress(LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add(var1);
        }

        return var1.channel().localAddress();
    }

    /**
     * Shuts down all open endpoints (with immediate effect?)
     */
    public void terminateEndpoints() {
        this.isAlive = false;
        Iterator var1 = this.endpoints.iterator();

        while (var1.hasNext()) {
            ChannelFuture var2 = (ChannelFuture) var1.next();

            try {
                var2.channel().close().sync();
            } catch (InterruptedException var4) {
                logger.error("Interrupted whilst closing channel");
            }
        }
    }

    /**
     * Will try to process the packets received by each NetworkManager, gracefully manage processing failures and cleans
     * up dead connections
     */
    public void networkTick() {
        List var1 = this.networkManagers;

        synchronized (this.networkManagers) {
            Iterator var2 = this.networkManagers.iterator();

            while (var2.hasNext()) {
                final NetworkManager var3 = (NetworkManager) var2.next();

                if (!var3.hasNoChannel()) {
                    if (!var3.isChannelOpen()) {
                        var2.remove();
                        var3.checkDisconnected();
                    } else {
                        try {
                            var3.processReceivedPackets();
                        } catch (Exception var8) {
                            if (var3.isLocalChannel()) {
                                CrashReport var10 = CrashReport.makeCrashReport(var8, "Ticking memory connection");
                                CrashReportCategory var6 = var10.makeCategory("Ticking connection");
                                var6.addCrashSectionCallable("Connection", new Callable() {
                                    private static final String __OBFID = "CL_00002272";

                                    public String func_180229_a() {
                                        return var3.toString();
                                    }

                                    public Object call() {
                                        return this.func_180229_a();
                                    }
                                });
                                throw new ReportedException(var10);
                            }

                            logger.warn("Failed to handle packet for " + var3.getRemoteAddress(), var8);
                            final ChatComponentText var5 = new ChatComponentText("Internal server error");
                            var3.sendPacket(new S40PacketDisconnect(var5), new GenericFutureListener() {
                                private static final String __OBFID = "CL_00002271";

                                public void operationComplete(Future p_operationComplete_1_) {
                                    var3.closeChannel(var5);
                                }
                            }, new GenericFutureListener[0]);
                            var3.disableAutoRead();
                        }
                    }
                }
            }
        }
    }

    public MinecraftServer getServer() {
        return this.mcServer;
    }
}
