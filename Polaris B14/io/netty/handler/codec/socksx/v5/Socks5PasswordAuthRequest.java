package io.netty.handler.codec.socksx.v5;

public abstract interface Socks5PasswordAuthRequest
  extends Socks5Message
{
  public abstract String username();
  
  public abstract String password();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5PasswordAuthRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */