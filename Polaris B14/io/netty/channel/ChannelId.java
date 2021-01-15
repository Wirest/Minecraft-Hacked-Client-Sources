package io.netty.channel;

import java.io.Serializable;

public abstract interface ChannelId
  extends Serializable, Comparable<ChannelId>
{
  public abstract String asShortText();
  
  public abstract String asLongText();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */