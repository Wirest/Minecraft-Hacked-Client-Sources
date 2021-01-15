package io.netty.handler.codec.socksx.v5;

import java.util.List;

public abstract interface Socks5InitialRequest
  extends Socks5Message
{
  public abstract List<Socks5AuthMethod> authMethods();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5InitialRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */