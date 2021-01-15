package ch.qos.logback.core.net.server;

import java.io.Closeable;

public abstract interface Client
  extends Runnable, Closeable
{
  public abstract void close();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */