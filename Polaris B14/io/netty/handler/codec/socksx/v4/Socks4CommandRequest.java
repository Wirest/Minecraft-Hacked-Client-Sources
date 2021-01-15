package io.netty.handler.codec.socksx.v4;

public abstract interface Socks4CommandRequest
  extends Socks4Message
{
  public abstract Socks4CommandType type();
  
  public abstract String userId();
  
  public abstract String dstAddr();
  
  public abstract int dstPort();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */