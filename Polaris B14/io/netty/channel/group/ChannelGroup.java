package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import java.util.Set;

public abstract interface ChannelGroup
  extends Set<Channel>, Comparable<ChannelGroup>
{
  public abstract String name();
  
  public abstract Channel find(ChannelId paramChannelId);
  
  public abstract ChannelGroupFuture write(Object paramObject);
  
  public abstract ChannelGroupFuture write(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroup flush();
  
  public abstract ChannelGroup flush(ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture writeAndFlush(Object paramObject);
  
  public abstract ChannelGroupFuture writeAndFlush(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture disconnect();
  
  public abstract ChannelGroupFuture disconnect(ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture close();
  
  public abstract ChannelGroupFuture close(ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture deregister();
  
  public abstract ChannelGroupFuture deregister(ChannelMatcher paramChannelMatcher);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\ChannelGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */