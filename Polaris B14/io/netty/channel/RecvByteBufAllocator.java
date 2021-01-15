package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract interface RecvByteBufAllocator
{
  public abstract Handle newHandle();
  
  public static abstract interface Handle
  {
    public abstract ByteBuf allocate(ByteBufAllocator paramByteBufAllocator);
    
    public abstract int guess();
    
    public abstract void record(int paramInt);
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\RecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */