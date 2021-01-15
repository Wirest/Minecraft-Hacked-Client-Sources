package io.netty.handler.stream;

import io.netty.channel.ChannelHandlerContext;

public abstract interface ChunkedInput<B>
{
  public abstract boolean isEndOfInput()
    throws Exception;
  
  public abstract void close()
    throws Exception;
  
  public abstract B readChunk(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract long length();
  
  public abstract long progress();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\stream\ChunkedInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */