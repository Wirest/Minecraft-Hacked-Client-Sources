package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAware;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

abstract interface RemoteReceiverClient
  extends Client, ContextAware
{
  public abstract void setQueue(BlockingQueue<Serializable> paramBlockingQueue);
  
  public abstract boolean offer(Serializable paramSerializable);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\RemoteReceiverClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */