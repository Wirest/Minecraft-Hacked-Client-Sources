package io.netty.channel.group;

import io.netty.channel.Channel;

public abstract interface ChannelMatcher
{
  public abstract boolean matches(Channel paramChannel);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\ChannelMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */