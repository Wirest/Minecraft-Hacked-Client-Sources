package io.netty.handler.codec.socksx.v4;

public abstract interface Socks4CommandResponse
  extends Socks4Message
{
  public abstract Socks4CommandStatus status();
  
  public abstract String dstAddr();
  
  public abstract int dstPort();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */