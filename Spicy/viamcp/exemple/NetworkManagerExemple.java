package viamcp.exemple;

import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;
import viamcp.utils.Util;

public class NetworkManagerExemple {

/*-----------------------------------------------------------------*/
// In the NetworkManager.java class made as in the example below.
/*-----------------------------------------------------------------*/

    /*public static NetworkManager func_181124_a(InetAddress p_181124_0_, int p_181124_1_, boolean p_181124_2_) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class <? extends SocketChannel> oclass;
        LazyLoadBase<? extends EventLoopGroup> lazyloadbase;

        if (Epoll.isAvailable() && p_181124_2_)
        {
            oclass = EpollSocketChannel.class;
            lazyloadbase = field_181125_e;
        }
        else
        {
            oclass = NioSocketChannel.class;
            lazyloadbase = CLIENT_NIO_EVENTLOOP;
        }

        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyloadbase.getValue())).handler(new ChannelInitializer<Channel>()
        {
            protected void initChannel(Channel p_initChannel_1_) throws Exception
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
                }
                catch (ChannelException var3)
                {
                    ;
                }

                p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter", (ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender", (ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);

//-->               /* ViaVersion */
//-->               if (p_initChannel_1_ instanceof SocketChannel) {
//-->                    UserConnection user = new VRClientSideUserConnection(p_initChannel_1_);
//-->                    new ProtocolPipeline(user).add(ViaFabricHostnameProtocol.INSTANCE);
//-->
//-->                    p_initChannel_1_.pipeline().addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME,
//-->                            new VREncodeHandler(user)).addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new VRDecodeHandler(user));
//-->                }
//-->                /* --------- */

            /*}
        })).channel(oclass)).connect(p_181124_0_, p_181124_1_).syncUninterruptibly();
        return networkmanager;
    }*/



/*-----------------------------------------------------------------*/
//Now replace the "this.channel.pipeline().addBefore...." as in the example below always in the class NetworkManager.java
/*-----------------------------------------------------------------*/

    public void setCompressionTreshold(int treshold) {
        /*f (treshold >= 0)
        {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
            {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            }
            else
            {
//-->          /* ViaVersion */
//-->           Util.decodeEncodePlacement(channel.pipeline(), "decoder", "decompress", new NettyCompressionDecoder(treshold));
//-->          /* --------- */
            /*}

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
            {
                ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            }
            else
            {
//-->          /* ViaVersion */
//-->           Util.decodeEncodePlacement(channel.pipeline(), "encoder", "compress", new NettyCompressionEncoder(treshold));
//-->          /* --------- */
           /*}
        }
        else
        {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
            {
                this.channel.pipeline().remove("decompress");
            }

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
            {
                this.channel.pipeline().remove("compress");
            }
        }*/
    }

}
