package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.SecretKey;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import rip.autumn.core.Autumn;
import rip.autumn.events.packet.ReceivePacketEvent;

public class NetworkManager extends SimpleChannelInboundHandler {
   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
   public static final Marker logMarkerPackets;
   public static final AttributeKey attrKeyConnectionState;
   public static final LazyLoadBase CLIENT_NIO_EVENTLOOP;
   public static final LazyLoadBase field_181125_e;
   public static final LazyLoadBase CLIENT_LOCAL_EVENTLOOP;
   private static final Logger logger;
   private final EnumPacketDirection direction;
   private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
   private Channel channel;
   private SocketAddress socketAddress;
   private INetHandler packetListener;
   private IChatComponent terminationReason;
   private boolean isEncrypted;
   private boolean disconnected;

   public NetworkManager(EnumPacketDirection packetDirection) {
      this.direction = packetDirection;
   }

   public static NetworkManager func_181124_a(InetAddress p_181124_0_, int p_181124_1_, boolean p_181124_2_) {
      final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      Class oclass;
      LazyLoadBase lazyloadbase;
      if (Epoll.isAvailable() && p_181124_2_) {
         oclass = EpollSocketChannel.class;
         lazyloadbase = field_181125_e;
      } else {
         oclass = NioSocketChannel.class;
         lazyloadbase = CLIENT_NIO_EVENTLOOP;
      }

      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyloadbase.getValue())).handler(new ChannelInitializer() {
         protected void initChannel(Channel p_initChannel_1_) throws Exception {
            try {
               p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, true);
            } catch (ChannelException var3) {
            }

            p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);
         }
      })).channel(oclass)).connect(p_181124_0_, p_181124_1_).syncUninterruptibly();
      return networkmanager;
   }

   public static NetworkManager provideLocalClient(SocketAddress address) {
      final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer() {
         protected void initChannel(Channel p_initChannel_1_) throws Exception {
            p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
         }
      })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
      return networkmanager;
   }

   public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
      super.channelActive(p_channelActive_1_);
      this.channel = p_channelActive_1_.channel();
      this.socketAddress = this.channel.remoteAddress();

      try {
         this.setConnectionState(EnumConnectionState.HANDSHAKING);
      } catch (Throwable var3) {
         logger.fatal(var3);
      }

   }

   public void setConnectionState(EnumConnectionState newState) {
      this.channel.attr(attrKeyConnectionState).set(newState);
      this.channel.config().setAutoRead(true);
      logger.debug("Enabled auto read");
   }

   public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
      this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
   }

   public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
      ChatComponentTranslation chatcomponenttranslation;
      if (p_exceptionCaught_2_ instanceof TimeoutException) {
         chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
      } else {
         chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Internal Exception: " + p_exceptionCaught_2_});
      }

      this.closeChannel(chatcomponenttranslation);
   }

   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception {
      ReceivePacketEvent event = new ReceivePacketEvent(p_channelRead0_2_);
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      if (!event.isCancelled()) {
         if (this.channel.isOpen()) {
            try {
               p_channelRead0_2_.processPacket(this.packetListener);
            } catch (ThreadQuickExitException var5) {
            }
         }

      }
   }

   public void sendPacket(Packet packetIn) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packetIn, (GenericFutureListener[])null);
      } else {
         ReentrantReadWriteLock lock = this.field_181680_j;
         lock.writeLock().lock();

         try {
            this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])null));
         } finally {
            lock.writeLock().unlock();
         }
      }

   }

   public void sendPacket(Packet packetIn, GenericFutureListener listener, GenericFutureListener... listeners) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packetIn, (GenericFutureListener[])ArrayUtils.add(listeners, 0, listener));
      } else {
         ReentrantReadWriteLock lock = this.field_181680_j;
         lock.writeLock().lock();

         try {
            this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])ArrayUtils.add(listeners, 0, listener)));
         } finally {
            lock.writeLock().unlock();
         }
      }

   }

   private void dispatchPacket(Packet inPacket, GenericFutureListener[] futureListeners) {
      EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
      EnumConnectionState enumconnectionstate1 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
      Channel channel = this.channel;
      if (enumconnectionstate1 != enumconnectionstate) {
         channel.config().setAutoRead(false);
      }

      if (channel.eventLoop().inEventLoop()) {
         if (enumconnectionstate != enumconnectionstate1) {
            this.setConnectionState(enumconnectionstate);
         }

         ChannelFuture channelfuture = channel.writeAndFlush(inPacket);
         if (futureListeners != null) {
            channelfuture.addListeners(futureListeners);
         }

         channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         channel.eventLoop().execute(() -> {
            if (enumconnectionstate != enumconnectionstate1) {
               this.setConnectionState(enumconnectionstate);
            }

            ChannelFuture channelfuture1 = this.channel.writeAndFlush(inPacket);
            if (futureListeners != null) {
               channelfuture1.addListeners(futureListeners);
            }

            channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
         });
      }

   }

   private void flushOutboundQueue() {
      if (this.channel != null && this.channel.isOpen()) {
         this.field_181680_j.readLock().lock();

         try {
            while(!this.outboundPacketsQueue.isEmpty()) {
               NetworkManager.InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
               this.dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
            }
         } finally {
            this.field_181680_j.readLock().unlock();
         }
      }

   }

   public void processReceivedPackets() {
      this.flushOutboundQueue();
      if (this.packetListener instanceof ITickable) {
         ((ITickable)this.packetListener).update();
      }

      this.channel.flush();
   }

   public SocketAddress getRemoteAddress() {
      return this.socketAddress;
   }

   public void closeChannel(IChatComponent message) {
      if (this.channel.isOpen()) {
         this.channel.close().awaitUninterruptibly();
         this.terminationReason = message;
      }

   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public void enableEncryption(SecretKey key) {
      this.isEncrypted = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
   }

   public boolean getIsencrypted() {
      return this.isEncrypted;
   }

   public boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public boolean hasNoChannel() {
      return this.channel == null;
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public void setNetHandler(INetHandler handler) {
      Validate.notNull(handler, "packetListener", new Object[0]);
      logger.debug("Set listener of {} to {}", new Object[]{this, handler});
      this.packetListener = handler;
   }

   public IChatComponent getExitMessage() {
      return this.terminationReason;
   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   public void setCompressionTreshold(int treshold) {
      if (treshold >= 0) {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
         }
      } else {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }

   }

   public void checkDisconnected() {
      if (this.channel != null && !this.channel.isOpen()) {
         if (!this.disconnected) {
            this.disconnected = true;
            if (this.getExitMessage() != null) {
               this.getNetHandler().onDisconnect(this.getExitMessage());
            } else if (this.getNetHandler() != null) {
               this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
            }
         } else {
            logger.warn("handleDisconnection() called twice");
         }
      }

   }

   static {
      logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
      attrKeyConnectionState = AttributeKey.valueOf("protocol");
      CLIENT_NIO_EVENTLOOP = new LazyLoadBase() {
         protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
         }
      };
      field_181125_e = new LazyLoadBase() {
         protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
         }
      };
      CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase() {
         protected LocalEventLoopGroup load() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
         }
      };
      logger = LogManager.getLogger();
   }

   static class InboundHandlerTuplePacketListener {
      private final Packet packet;
      private final GenericFutureListener[] futureListeners;

      public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener... inFutureListeners) {
         this.packet = inPacket;
         this.futureListeners = inFutureListeners;
      }
   }
}
