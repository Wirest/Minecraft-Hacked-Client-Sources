package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResultProvider;

public abstract interface SocksMessage
  extends DecoderResultProvider
{
  public abstract SocksVersion version();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\SocksMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */