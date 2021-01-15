package io.netty.channel.unix;

public abstract interface DomainSocketChannel
  extends UnixChannel
{
  public abstract DomainSocketAddress remoteAddress();
  
  public abstract DomainSocketAddress localAddress();
  
  public abstract DomainSocketChannelConfig config();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\unix\DomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */