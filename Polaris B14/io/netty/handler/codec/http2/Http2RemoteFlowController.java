package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public abstract interface Http2RemoteFlowController
  extends Http2FlowController
{
  public abstract void sendFlowControlled(ChannelHandlerContext paramChannelHandlerContext, Http2Stream paramHttp2Stream, FlowControlled paramFlowControlled);
  
  public static abstract interface FlowControlled
  {
    public abstract int size();
    
    public abstract void error(Throwable paramThrowable);
    
    public abstract boolean write(int paramInt);
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2RemoteFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */