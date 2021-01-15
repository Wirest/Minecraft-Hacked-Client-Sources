package io.netty.handler.codec.socksx.v5;

public abstract interface Socks5CommandResponse
  extends Socks5Message
{
  public abstract Socks5CommandStatus status();
  
  public abstract Socks5AddressType bndAddrType();
  
  public abstract String bndAddr();
  
  public abstract int bndPort();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */