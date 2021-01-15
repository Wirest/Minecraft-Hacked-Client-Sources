package io.netty.handler.codec.serialization;

public abstract interface ClassResolver
{
  public abstract Class<?> resolve(String paramString)
    throws ClassNotFoundException;
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ClassResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */