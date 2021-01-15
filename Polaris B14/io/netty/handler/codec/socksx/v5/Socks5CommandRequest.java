package io.netty.handler.codec.socksx.v5;

public abstract interface Socks5CommandRequest
  extends Socks5Message
{
  public abstract Socks5CommandType type();
  
  public abstract Socks5AddressType dstAddrType();
  
  public abstract String dstAddr();
  
  public abstract int dstPort();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */