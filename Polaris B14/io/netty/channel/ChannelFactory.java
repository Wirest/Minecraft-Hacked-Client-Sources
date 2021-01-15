package io.netty.channel;

public abstract interface ChannelFactory<T extends Channel>
  extends io.netty.bootstrap.ChannelFactory<T>
{
  public abstract T newChannel();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */