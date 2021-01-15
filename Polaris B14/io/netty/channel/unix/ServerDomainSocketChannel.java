package io.netty.channel.unix;

import io.netty.channel.ServerChannel;

public abstract interface ServerDomainSocketChannel
  extends ServerChannel, UnixChannel
{
  public abstract DomainSocketAddress remoteAddress();
  
  public abstract DomainSocketAddress localAddress();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\unix\ServerDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */