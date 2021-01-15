package ch.qos.logback.core.net.server;

import java.io.Closeable;
import java.io.IOException;

public abstract interface ServerListener<T extends Client>
  extends Closeable
{
  public abstract T acceptClient()
    throws IOException, InterruptedException;
  
  public abstract void close();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\ServerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */